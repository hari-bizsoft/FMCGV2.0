/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by GopiKing on 28-12-2017.
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountGroup {
    @JsonProperty("Id")
    Long Id;
    @JsonProperty("GroupName")
    String GroupName;
    @JsonProperty("UnderGroupId")
    Long UnderGroupId;
    @JsonProperty("Company")
    Company company;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public Long getUnderGroupId() {
        return UnderGroupId;
    }

    public void setUnderGroupId(Long underGroupId) {
        UnderGroupId = underGroupId;
    }
}
