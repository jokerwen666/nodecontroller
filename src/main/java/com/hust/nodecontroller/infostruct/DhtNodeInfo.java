package com.hust.nodecontroller.infostruct;

import com.hust.nodecontroller.infostruct.AnswerStruct.NormalMsg;
import org.springframework.stereotype.Component;

/**
 * @author ：xxx
 * @description：TODO
 * @date ：2020/10/29 11:15
 */

@Component
public class DhtNodeInfo extends NormalMsg {
    String domainName;
    String nodeID;
    String latitude;
    String longtitude;
    String city;
    String province;
    String enterprise;
    String orgName;
    String identityNum;

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince() {
        return province;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    public String getEnterprise() {
        return enterprise;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setIdentityNum(String identityNum) {
        this.identityNum = identityNum;
    }

    public String getIdentityNum() {
        return identityNum;
    }

    @Override
    public String toString()
    {
        return nodeID+"/"+latitude+"/"+longtitude+"/"+city+"/"+province+"/"+enterprise+"/"+orgName+"/"+identityNum;
    }

}