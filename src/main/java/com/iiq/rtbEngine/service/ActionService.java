package com.iiq.rtbEngine.service;

import com.iiq.rtbEngine.controller.DbManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ActionService {
    @Autowired
    DbManager dbManager;

    public ResponseEntity saveAtribure(Integer attributeId, Integer profileId) {
        dbManager.updateProfileAttribute(profileId,attributeId);
        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity getBid(Integer attributeId, Integer profileId) {


        return new ResponseEntity(HttpStatus.OK);
    }
}
