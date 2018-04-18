package com.bim.entity;

public class BimAppVersion {
    private Integer id;

    private String iosVersion;

    private String iosNote;

    private String iosAddress;

    private String androidVersion;

    private String androidNote;

    private String androidAddress;

    private Boolean currentVersion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIosVersion() {
        return iosVersion;
    }

    public void setIosVersion(String iosVersion) {
        this.iosVersion = iosVersion;
    }

    public String getIosNote() {
        return iosNote;
    }

    public void setIosNote(String iosNote) {
        this.iosNote = iosNote;
    }

    public String getIosAddress() {
        return iosAddress;
    }

    public void setIosAddress(String iosAddress) {
        this.iosAddress = iosAddress;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getAndroidNote() {
        return androidNote;
    }

    public void setAndroidNote(String androidNote) {
        this.androidNote = androidNote;
    }

    public String getAndroidAddress() {
        return androidAddress;
    }

    public void setAndroidAddress(String androidAddress) {
        this.androidAddress = androidAddress;
    }

    public Boolean getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(Boolean currentVersion) {
        this.currentVersion = currentVersion;
    }
}