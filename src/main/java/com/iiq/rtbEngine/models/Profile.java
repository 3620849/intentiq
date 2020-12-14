package com.iiq.rtbEngine.models;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Profile {
    private Integer id;
    private Lock lock ;
    private HashMap<Integer, Integer> campaignsAndCapacity;

    public Profile(Integer id) {
        this.id = id;
        this.lock= new ReentrantLock();
    }

    public HashMap<Integer, Integer> getCampaignsAndCapacity() {
        return campaignsAndCapacity;
    }

    public void setCampaignsAndCapacity(HashMap<Integer, Integer> campaignsAndCapacity) {
        this.campaignsAndCapacity = campaignsAndCapacity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return id.equals(profile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
