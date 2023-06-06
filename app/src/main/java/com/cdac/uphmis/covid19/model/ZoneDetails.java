package com.cdac.uphmis.covid19.model;

public class ZoneDetails {

    private String zoneId;
    private String zoneName;

    public ZoneDetails(String zoneId, String zoneName) {
        this.zoneId = zoneId;
        this.zoneName = zoneName;
    }


    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }


    @Override
    public String toString() {
        return this.zoneName;
    }
}
