package de.hybris.platform.commerceservices.storefinder.data;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

public class StoreFinderSearchPageData<RESULT> extends SearchPageData<RESULT>
{
    private String locationText;
    private double sourceLatitude;
    private double sourceLongitude;
    private double boundNorthLatitude;
    private double boundEastLongitude;
    private double boundSouthLatitude;
    private double boundWestLongitude;


    public void setLocationText(String locationText)
    {
        this.locationText = locationText;
    }


    public String getLocationText()
    {
        return this.locationText;
    }


    public void setSourceLatitude(double sourceLatitude)
    {
        this.sourceLatitude = sourceLatitude;
    }


    public double getSourceLatitude()
    {
        return this.sourceLatitude;
    }


    public void setSourceLongitude(double sourceLongitude)
    {
        this.sourceLongitude = sourceLongitude;
    }


    public double getSourceLongitude()
    {
        return this.sourceLongitude;
    }


    public void setBoundNorthLatitude(double boundNorthLatitude)
    {
        this.boundNorthLatitude = boundNorthLatitude;
    }


    public double getBoundNorthLatitude()
    {
        return this.boundNorthLatitude;
    }


    public void setBoundEastLongitude(double boundEastLongitude)
    {
        this.boundEastLongitude = boundEastLongitude;
    }


    public double getBoundEastLongitude()
    {
        return this.boundEastLongitude;
    }


    public void setBoundSouthLatitude(double boundSouthLatitude)
    {
        this.boundSouthLatitude = boundSouthLatitude;
    }


    public double getBoundSouthLatitude()
    {
        return this.boundSouthLatitude;
    }


    public void setBoundWestLongitude(double boundWestLongitude)
    {
        this.boundWestLongitude = boundWestLongitude;
    }


    public double getBoundWestLongitude()
    {
        return this.boundWestLongitude;
    }
}
