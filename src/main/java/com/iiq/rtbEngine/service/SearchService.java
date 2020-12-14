package com.iiq.rtbEngine.service;

import com.iiq.rtbEngine.controller.DbManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {
    Map<Integer, List<Integer>> campaignAttributes;

    @Autowired
    private DbManager dbManager;

    @PostConstruct
    public void initSearchStructure() {
        campaignAttributes = dbManager.getAllCampaignAttributes();
    }
    //the best possible case scenario for containsALL is O(m) in HashSet
    //but convertation list to HasSet cost O(n)
    //in particular complexity is O(n*log(n) + m*log(m)) for unsorted ArrayList
    //where n - campaigns attributes m-current profile attributes
    public List<Integer> findAllMatchedCampaigns(List<Integer> attributes){
        List matchedCampaigns = new LinkedList();
        if(attributes==null || attributes.size()<1){
            return null;
        }
        campaignAttributes.forEach((k,v)->{
            if(attributes.containsAll(v)){
                matchedCampaigns.add(k);
            }
        });
        return matchedCampaigns;
    }
}
