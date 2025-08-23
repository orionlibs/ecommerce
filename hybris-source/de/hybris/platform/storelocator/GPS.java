package de.hybris.platform.storelocator;

import java.io.Serializable;

public interface GPS extends Serializable
{
    double getDecimalLongitude();


    double getDecimalLatitude();


    GPS create(String paramString1, String paramString2);


    GPS create(double paramDouble1, double paramDouble2);


    String toString();


    String toDMSString();


    String toGeocodeServiceFormat();
}
