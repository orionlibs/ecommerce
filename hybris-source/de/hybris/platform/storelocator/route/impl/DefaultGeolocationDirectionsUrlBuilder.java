package de.hybris.platform.storelocator.route.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;
import de.hybris.platform.storelocator.route.GeolocationDirectionsUrlBuilder;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class DefaultGeolocationDirectionsUrlBuilder implements GeolocationDirectionsUrlBuilder
{
    private static final String DEFAULT_RESPONSE_TYPE = "xml";
    private static final String DEFAULT_MODE = "driving";
    private static final String SEPARATOR = "+";
    private String responseType;
    private boolean sensor = true;
    private String mode;


    public String getWebServiceUrl(String baseUrl, GPS start, GPS destination, Map params)
    {
        ServicesUtil.validateParameterNotNull(baseUrl, "Base Url cannot be null");
        ServicesUtil.validateParameterNotNull(start, "START cannot be null");
        ServicesUtil.validateParameterNotNull(destination, "DESTINATION cannot be null");
        StringBuilder result = new StringBuilder(baseUrl);
        result.append("/api/directions/").append(getResponseType()).append("?origin=").append(formatCoordinates(start))
                        .append("&destination=").append(formatCoordinates(destination)).append("&sensor=").append(isSensor()).append("&mode=")
                        .append(getMode());
        return result.toString();
    }


    public String getWebServiceUrl(String baseUrl, AddressData startAddress, AddressData destinationAddress, Map params)
    {
        ServicesUtil.validateParameterNotNull(baseUrl, "Base Url cannot be null");
        ServicesUtil.validateParameterNotNull(startAddress, "START cannot be null");
        ServicesUtil.validateParameterNotNull(destinationAddress, "DESTINATION cannot be null");
        StringBuilder result = new StringBuilder(baseUrl);
        try
        {
            result.append("/api/directions/").append(getResponseType()).append("?origin=").append(addressData2String(startAddress))
                            .append("&destination=").append(addressData2String(destinationAddress)).append("&sensor=").append(isSensor())
                            .append("&mode=").append(getMode());
        }
        catch(UnsupportedEncodingException e)
        {
            throw new GeoServiceWrapperException(e);
        }
        return result.toString();
    }


    protected String getMode()
    {
        if(StringUtils.isEmpty(this.mode))
        {
            return "driving";
        }
        return this.mode;
    }


    protected boolean isSensor()
    {
        return this.sensor;
    }


    protected String getResponseType()
    {
        if(StringUtils.isEmpty(this.responseType))
        {
            return "xml";
        }
        return this.responseType;
    }


    protected String formatCoordinates(GPS coordinates)
    {
        DecimalFormat format = new DecimalFormat(".######");
        return format.format(coordinates.getDecimalLatitude()).replace(',', '.') + "," + format.format(coordinates.getDecimalLatitude()).replace(',', '.');
    }


    public void setSensor(boolean sensor)
    {
        this.sensor = sensor;
    }


    public void setMode(String mode)
    {
        this.mode = mode;
    }


    public void setResponseType(String responseType)
    {
        this.responseType = responseType;
    }


    protected String addressData2String(AddressData addressData) throws UnsupportedEncodingException
    {
        List<String> collection = Lists.newArrayList((Object[])new String[] {addressData.getStreet(), addressData.getBuilding(), addressData.getZip(), addressData
                        .getCity(), addressData.getCountryCode()});
        return Joiner.on("+").join(Iterables.filter(collection, Predicates.notNull()));
    }
}
