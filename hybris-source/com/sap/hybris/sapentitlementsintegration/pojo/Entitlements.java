/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.sapentitlementsintegration.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Entitlements implements Serializable
{
    /** Default serialVersionUID value. */
    private static final long serialVersionUID = 1L;
    @JsonProperty("data")
    private Data data;


    @JsonProperty("data")
    public void setData(final Data data)
    {
        this.data = data;
    }


    @JsonProperty("data")
    public Data getData()
    {
        return data;
    }
}
