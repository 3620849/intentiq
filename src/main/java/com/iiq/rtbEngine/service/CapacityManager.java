package com.iiq.rtbEngine.service;

import com.iiq.rtbEngine.controller.DbManager;
import com.iiq.rtbEngine.models.CampaignConfig;
import com.iiq.rtbEngine.models.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

@Service
public class CapacityManager {
    @Autowired
    DbManager dbManager;
    //profile_id,  map campaign capacity
    ConcurrentHashMap<Integer,Profile> profiles = new ConcurrentHashMap<>();
    Map<Integer, CampaignConfig> allCampaignsConfigs = new HashMap<>();

    @PostConstruct
    void initMethod() {
        allCampaignsConfigs = dbManager.getAllCampaignsConfigs();
    }

    public Integer getNotExidedCampaign(Integer profileId, List<Integer> sortedCampaigns) {
        HashMap<Integer, Integer> newMap = new HashMap<>();
        if(!profiles.containsKey(profileId)){
            Profile profile = new Profile(profileId);
            for (Integer firstMatchedId : sortedCampaigns) {
                CampaignConfig campaignConfigFromDb = allCampaignsConfigs.get(firstMatchedId);
                if (campaignConfigFromDb != null && campaignConfigFromDb.getCapacity()>= 0) {
                    newMap.put(firstMatchedId, campaignConfigFromDb.getCapacity());
                    profile.setCampaignsAndCapacity(newMap);
                }
            }
            //if profile not in data structure we create new profile with all default capacity and save
            profiles.putIfAbsent(profileId,profile);
        }
        //decrement capacity from data structure
        return decrementCapacityAndGetCampaign(profileId, sortedCampaigns);
    }

    private int decrementCapacityAndGetCampaign(Integer profileId, List<Integer> sortedCampaigns) {
                //get list of campaings from map
                Profile localStorageProfile = profiles.get(profileId);
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

        //if no decrement happened return -1;
        return -1;
    }

}
