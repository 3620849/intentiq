package com.iiq.rtbEngine.service;

import com.iiq.rtbEngine.controller.DbManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CachingManager {
    ConcurrentHashMap<Integer, List<Integer>> profiles = new ConcurrentHashMap<>();
    @Autowired
    DbManager dbManager;

    List<Integer> getAttributeListByProfleId(Integer profileId){
        List<Integer> list = profiles.get(profileId);
        if(list==null){
            list= new LinkedList<>(dbManager.getProfileAttributes(profileId));
            profiles.putIfAbsent(profileId,list);
        }
        return list;
    }
    void addProfile(Integer profileId,List<Integer> list){
        profiles.putIfAbsent(profileId,list);
    }

    public void deleteProfile(Integer profileId) {
        profiles.remove(profileId);
    }

}
