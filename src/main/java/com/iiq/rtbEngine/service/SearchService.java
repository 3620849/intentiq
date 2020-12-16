package com.iiq.rtbEngine.service;

import com.iiq.rtbEngine.controller.DbManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class SearchService {
    Map<String, List<Integer>> campaignAttributes = new HashMap<>();

    @Autowired
    private DbManager dbManager;

    //I generate map where key is attributes of each campaign and value is list of id of campaign
    //if there is already exist such key so program will append to list id of current campaign
    @PostConstruct
    public void initSearchStructure() {
       dbManager.getAllCampaignAttributes().forEach((k,v)->{
           Collections.sort(v);
           List<Integer> idOfCampaigns = campaignAttributes.get(v.toString());
           if(idOfCampaigns==null){
               idOfCampaigns = new ArrayList();
           }
           idOfCampaigns.add(k);
           campaignAttributes.put(v.toString(),idOfCampaigns);
       });
    }

    //explanation
    //when starting to search we generate all possible variants of profiles attributes O(m*(2^m)) where m is number of attributes
    //and searching this attributes in campaignAttributes hasmap where key is ids of attributes and value is list of cmapaign id
    //hence complexity is O(1) and total complexity is O(m*(2^m))
    public List<Integer> findAllMatchedCampaigns(List<Integer> attributes){
        Collections.sort(attributes);
        List<String> allPovibleAttributesVariants = getAllPosibleAttributes(attributes);
        List matchedCampaigns = new LinkedList();

        if(attributes==null || attributes.size()<1){
            return null;
        }

        allPovibleAttributesVariants.forEach((attribute)->{
            if(campaignAttributes.containsKey(attribute)){
                matchedCampaigns.addAll(campaignAttributes.get(attribute));
            }
        });
        return matchedCampaigns;
    }

    private List<String> getAllPosibleAttributes(List<Integer> attributes) {
        List<List<Integer>>res = new ArrayList<>();
        if (attributes==null || attributes.size()==0){
            return null;
        }
        List<Integer> subsets = new ArrayList<>();
        findAllSubsets(attributes,res,subsets,0);
        List<String>allSubsetAsString = new ArrayList<>();
        res.forEach(ls-> allSubsetAsString.add(ls.toString()));
        return allSubsetAsString;
    }
    private static void findAllSubsets(List<Integer> attributes, List<List<Integer>> res, List<Integer> subsets, int startIndex) {
        res.add(new ArrayList<>(subsets));
        for(int i = startIndex;i<attributes.size();++i){
            subsets.add(attributes.get(i));
            findAllSubsets(attributes,res,subsets,i+1);
            subsets.remove(subsets.size()-1);
        }
    }
}
