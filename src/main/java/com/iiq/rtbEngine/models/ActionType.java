package com.iiq.rtbEngine.models;

import java.util.HashMap;
import java.util.Map;

public enum ActionType {
    ATTRIBUTION_REQUEST(0),
    BID_REQUEST(1),
    ;

    private int id;
    private static Map<Integer, ActionType> idToRequestMap = new HashMap<>();

    static {
        for(ActionType actionType : ActionType.values())
            idToRequestMap.put(actionType.getId(), actionType);
    }

    public int getId() {
        return this.id;
    }

    ActionType(int id) {
        this.id = id;
    }

    public static ActionType getActionTypeById(int id) {
        return idToRequestMap.get(id);
    }
}
