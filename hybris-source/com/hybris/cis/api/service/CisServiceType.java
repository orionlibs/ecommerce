package com.hybris.cis.api.service;

public enum CisServiceType
{
    AVS("avs"),
    PSP("psp"),
    SUB("sub"),
    TAX("tax"),
    SHIPPING("shipping"),
    GEOLOCATION("geolocation"),
    FRAUD("fraud");
    private final String serviceId;


    CisServiceType(String serviceId)
    {
        this.serviceId = serviceId;
    }


    public String toString()
    {
        return this.serviceId;
    }
}
