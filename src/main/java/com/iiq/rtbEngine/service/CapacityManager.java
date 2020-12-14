package com.iiq.rtbEngine.service;

import com.iiq.rtbEngine.controller.DbManager;
import com.iiq.rtbEngine.models.CampaignConfig;
import com.iiq.rtbEngine.models.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;

@Service
public class CapacityManager {
    @Autowired
    DbManager dbManager;
    //profile_id,  map campaign capacity
    CopyOnWriteArrayList<Profile> profiles = new CopyOnWriteArrayList<>();
    Map<Integer, CampaignConfig> allCampaignsConfigs = new HashMap<>();

    @PostConstruct
    void initMethod() {
        allCampaignsConfigs = dbManager.getAllCampaignsConfigs();
    }

    public Integer getNotExidedCampaign(Integer profileId, List<Integer> sortedCampaigns) {
        Profile profile = new Profile(profileId);
        boolean porfileExist = profiles.contains(profile);
        HashMap<Integer, Integer> newMap = new HashMap<>();
        int updatedProfile = -1;
        if (!porfileExist) {
            synchronized (profiles) {
                //double check locking
                if (!profiles.contains(profile)) {
                    //find first default campaign
                    for (Integer firstMatchedId : sortedCampaigns) {
                        CampaignConfig campaignConfigFromDb = allCampaignsConfigs.get(firstMatchedId);
                        if (campaignConfigFromDb != null && campaignConfigFromDb.getCapacity() - 1 >= 0) {
                            newMap.put(firstMatchedId, campaignConfigFromDb.getCapacity() - 1);
                            profile.setCampaignsAndCapacity(newMap);
                            profiles.add(profile);
                            return firstMatchedId;
                        }
                    }
                } else {
                    //profile not empy do the same as below
                    updatedProfile = decrementCapacityAndGetCampaign(profile, sortedCampaigns);
                }
            }
        } else {
            updatedProfile = decrementCapacityAndGetCampaign(profile, sortedCampaigns);
        }
        return updatedProfile;
    }

    private int decrementCapacityAndGetCampaign(Profile profile, List<Integer> sortedCampaigns) {
        for (int i = 0; i < profiles.size(); ++i) {
            if (profiles.get(i).equals(profile)) {

                //get list of campaings from map
                Profile localStorageProfile = profiles.get(i);
                Lock lock = localStorageProfile.getLock();
                lock.lock();
                    HashMap<Integer, Integer> campaignsAndCapacity = localStorageProfile.getCampaignsAndCapacity();
                    //get each sortedCampaigns and try to decrement
                    for (Integer firstMatchedId : sortedCampaigns) {
                        Integer capacity = campaignsAndCapacity.get(firstMatchedId);
                        if (capacity != null) {
                            if (capacity - 1 >= 0) {
                                //if decrement sucseed return id of that campaign
                                campaignsAndCapacity.put(firstMatchedId, capacity - 1);
                                lock.unlock();
                                return firstMatchedId;
                            }
                        } else {
                            //capacity not found in local map go to cached db and search
                            CampaignConfig campaignConfigFromDb = allCampaignsConfigs.get(firstMatchedId);
                            //if in db empty or 0 continue for next value
                            if (campaignConfigFromDb != null && campaignConfigFromDb.getCapacity() - 1 >= 0) {
                                campaignsAndCapacity.put(firstMatchedId, campaignConfigFromDb.getCapacity() - 1);
                                lock.unlock();
                                return firstMatchedId;
                            }
                        }
                    }
            }
        }
        //if no decrement happened return -1;
        return -1;
    }

}
