package com.iiq.rtbEngine.service;

import com.iiq.rtbEngine.controller.DbManager;
import com.iiq.rtbEngine.models.CampaignConfig;
import com.iiq.rtbEngine.models.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActionService {
    @Autowired
    DbManager dbManager;
    @Autowired
    CachingManager cachingManager;
    @Autowired
    SearchService searchService;
    @Autowired
    CapacityManager capacityManager;

    public ResponseEntity updateProfile(Integer attributeId, Integer profileId) {
        cachingManager.deleteProfile(profileId);
        dbManager.updateProfileAttribute(profileId,attributeId);
        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity findCampaign(Integer profileId) {
        //get profile attributes from cache or from db
        List<Integer> attributeListByProidleId = cachingManager.getAttributeListByProfleId(profileId);
        //get all matched campaigns
        List<Integer> allMatchedCampaigns = searchService.findAllMatchedCampaigns(attributeListByProidleId);
        if(allMatchedCampaigns==null || allMatchedCampaigns.size()==0){
            return new ResponseEntity(ResponseType.UNMATCHED.getValue(),HttpStatus.NOT_FOUND);
        }
        //sort campaigns in map according priority and after by id
        Map<Integer, CampaignConfig> allCampaignsConfigs = dbManager.getAllCampaignsConfigs();
        List<Integer> sortedCampaigns = allCampaignsConfigs.entrySet().stream()
                .filter((entry) -> allMatchedCampaigns.contains(entry.getKey()))
                .sorted((entry1, entry2) -> {
                    int compare = entry2.getValue().getPriority()-entry1.getValue().getPriority();
                    if (compare == 0) {
                        return entry2.getKey()-entry1.getKey();
                    }
                    return compare;
                }).map((y)->y.getKey()).collect(Collectors.toList()) ;
        //filter campaigns with small or zero capacity
        Integer campaignId = capacityManager.getNotExidedCampaign(profileId,sortedCampaigns);
        if(campaignId==-1){
            return new ResponseEntity(ResponseType.CAPPED.getValue(),HttpStatus.NOT_FOUND);
        }

        //return list


        return new ResponseEntity(campaignId,HttpStatus.OK);
    }
}
