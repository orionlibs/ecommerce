package de.hybris.platform.commercefacades.storelocator.data;

import java.io.Serializable;
import java.util.List;

public class PointOfServiceDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<PointOfServiceData> pointOfServices;


    public void setPointOfServices(List<PointOfServiceData> pointOfServices)
    {
        this.pointOfServices = pointOfServices;
    }


    public List<PointOfServiceData> getPointOfServices()
    {
        return this.pointOfServices;
    }
}
