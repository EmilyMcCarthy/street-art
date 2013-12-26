package com.mccarthy.models;

import com.cloudmine.api.CMGeoPoint;
import com.cloudmine.api.db.LocallySavableCMObject;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.List;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
public class StreetArt extends LocallySavableCMObject {

    private List<String> photoIds;
    private CMGeoPoint location;
    private String title;
    private String description;

    private StreetArt() {}

    public StreetArt(double latitude, double longitude, String title, String description, String... photoIds) {
        location = new CMGeoPoint(longitude, latitude);
        this.photoIds = Arrays.asList(photoIds);
        this.title = title;
        this.description = description;
    }

    @JsonIgnore
    public String getMainPhotoId() {
        if(photoIds == null || photoIds.isEmpty()) return null;
        return photoIds.get(0);
    }

    public List<String> getPhotoIds() {
        return photoIds;
    }

    public void setPhotoIds(List<String> photoIds) {
        this.photoIds = photoIds;
    }

    public CMGeoPoint getLocation() {
        return location;
    }

    public void setLocation(CMGeoPoint location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
