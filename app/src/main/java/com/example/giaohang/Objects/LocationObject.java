package com.example.giaohang.Objects;

import com.google.android.gms.maps.model.LatLng;

/**
 Ví trí đi và đến
 */
public class LocationObject {
    private LatLng coordinates;
    private String name = "";
    /**
     coordinates - tọa độ
     */
    public LocationObject(LatLng coordinates, String name){
        this.coordinates = coordinates;
        this.name = name;
    }
    public LocationObject(){
    }
    public LatLng getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
