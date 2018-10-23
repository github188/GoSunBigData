package com.hzgc.cluster.dispach.model;

import java.util.Date;

public class Dispach {
    private String id;

    private Long region;

    private String name;

    private String idcard;

    private String feature;

    private String bit_feature;

    private Float threshold;

    private String car;

    private String mac;

    private String notes;

    private Integer status;

    private Date create_time;

    private Date update_time;

    private byte[] face;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Long getRegion() {
        return region;
    }

    public void setRegion(Long region) {
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature == null ? null : feature.trim();
    }

    public String getBit_feature() {
        return bit_feature;
    }

    public void setBit_feature(String bitFeature) {
        this.bit_feature = bitFeature == null ? null : bitFeature.trim();
    }

    public Float getThreshold() {
        return threshold;
    }

    public void setThreshold(Float threshold) {
        this.threshold = threshold;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car == null ? null : car.trim();
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac == null ? null : mac.trim();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes == null ? null : notes.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date createTime) {
        this.create_time = createTime;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date updateTime) {
        this.update_time = updateTime;
    }

    public byte[] getFace() {
        return face;
    }

    public void setFace(byte[] face) {
        this.face = face;
    }
}