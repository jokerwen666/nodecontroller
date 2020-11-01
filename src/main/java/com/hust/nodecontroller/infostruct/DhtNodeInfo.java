package com.hust.nodecontroller.infostruct;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author ：xxx
 * @description：TODO
 * @date ：2020/10/29 11:15
 */

@Component("DhtNodeInfo")
public class DhtNodeInfo{
    @JsonIgnore
    static boolean Flag=false;
    String DomainName;
    String NodeID;
    String Latitude;
    String Longtitude;
    String City;
    String IdentityNum;

    @JsonProperty(value = "DomainName")
    public void setDomainName(String DomainName) {
        this.DomainName = DomainName;
    }

    public String getDomainName() {
        return DomainName;
    }

    @JsonProperty(value = "NodeID")
    public void setNodeID(String NodeID) {
        this.NodeID = NodeID;
    }

    public String getNodeID() {
        return NodeID;
    }

    @JsonProperty(value = "Latitude")
    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    @JsonProperty(value = "Longtitude")
    public void setLongtitude(String Longtitude) {
        this.Longtitude = Longtitude;
    }

    public String getLongtitude() {
        return Longtitude;
    }

    @JsonProperty(value = "City")
    public void setCity(String City) {
        this.City = City;
    }

    public String getCity() {
        return City;
    }

    @JsonProperty(value = "IdentityNum")
    public void setIdentityNum(String IdentityNum) {
        this.IdentityNum = IdentityNum;
    }

    public String getIdentityNum() {
        return IdentityNum;
    }

    @JsonIgnore
    static public void setFlag(boolean input) {
        Flag=input;

    }

    static public boolean getFlag() {
        return Flag;
    }

    @Override
    public String toString()
    {
        return NodeID+"/"+Latitude+"/"+Longtitude+"/"+City+"/"+IdentityNum;
    }

}
