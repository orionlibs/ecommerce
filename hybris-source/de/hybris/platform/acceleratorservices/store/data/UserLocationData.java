package de.hybris.platform.acceleratorservices.store.data;

import de.hybris.platform.commerceservices.store.data.GeoPoint;
import java.io.Serializable;

public class UserLocationData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String searchTerm;
    private GeoPoint point;


    public void setSearchTerm(String searchTerm)
    {
        this.searchTerm = searchTerm;
    }


    public String getSearchTerm()
    {
        return this.searchTerm;
    }


    public void setPoint(GeoPoint point)
    {
        this.point = point;
    }


    public GeoPoint getPoint()
    {
        return this.point;
    }
}
