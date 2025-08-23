package de.hybris.platform.storelocator.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.data.MapLocationData;
import de.hybris.platform.storelocator.data.RouteData;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.route.GeolocationDirectionsUrlBuilder;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

public class GoogleMapTools
{
    private static final String SEPARATOR = "+";
    private static final String CLIEND_ID = "client";
    private static final String API_KEY = "key";
    private ResponseExtractor<MapLocationData> addressLocationParser;
    private ResponseExtractor<RouteData> routeDataParser;
    private GeolocationDirectionsUrlBuilder directionsUrlBuilder;
    private String baseUrl;
    private String cliendId;
    private String cryptoKey;
    private String googleKey;


    public String getGoogleQuery(AddressData addressData)
    {
        List<String> collection = Lists.newArrayList((Object[])new String[] {addressData.getStreet(), addressData.getBuilding(), addressData.getZip(), addressData
                        .getCity(), addressData.getCountryCode()});
        return Joiner.on("+").join(Iterables.filter(collection, Predicates.notNull()));
    }


    public GPS geocodeAddress(Location address)
    {
        Preconditions.checkNotNull(address, "Geocoding failed! Address cannot be null");
        Preconditions.checkNotNull(address.getAddressData(), "Geocoding failed! Address cannot be null");
        return geocodeAddress(address.getAddressData());
    }


    public RouteData getDistanceAndRoute(Location start, Location destination)
    {
        String urlAddress = appendBusinessParams(this.directionsUrlBuilder.getWebServiceUrl(this.baseUrl, start.getAddressData(), destination
                        .getAddressData(), Collections.emptyMap()));
        RouteData routeData = getRouteData(urlAddress);
        double distance = GeometryUtils.getElipticalDistanceKM(start.getGPS(), destination.getGPS());
        routeData.setEagleFliesDistance(distance);
        return routeData;
    }


    public RouteData getDistanceAndRoute(GPS start, GPS destination)
    {
        String urlAddress = appendBusinessParams(getDirectionsUrlBuilder().getWebServiceUrl(getBaseUrl(), start, destination,
                        Collections.emptyMap()));
        RouteData routeData = getRouteData(urlAddress);
        double distance = GeometryUtils.getElipticalDistanceKM(start, destination);
        routeData.setEagleFliesDistance(distance);
        return routeData;
    }


    protected RouteData getRouteData(String urlAddress)
    {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler((ResponseErrorHandler)new Object(this));
        if(isBusinessAPI())
        {
            return (RouteData)restTemplate.execute(singAndEncodeURL(urlAddress), HttpMethod.GET, null, getRouteDataParser());
        }
        return (RouteData)restTemplate.execute(urlAddress, HttpMethod.GET, null, getRouteDataParser(), Collections.emptyMap());
    }


    public GPS geocodeAddress(AddressData addressData)
    {
        try
        {
            RestTemplate restTemplate = new RestTemplate();
            String urlAddress = appendBusinessParams(getBaseUrl() + "xml?address=" + getBaseUrl() + "&sensor=true");
            MapLocationData locationData = isBusinessAPI() ? (MapLocationData)restTemplate.execute(singAndEncodeURL(urlAddress), HttpMethod.GET, null, getAddressLocationParser()) : (MapLocationData)restTemplate.execute(urlAddress, HttpMethod.GET, null,
                            getAddressLocationParser(), new Object[0]);
            String latitude = locationData.getLatitude();
            String longitude = locationData.getLongitude();
            if(StringUtils.isNotBlank(latitude) && StringUtils.isNotBlank(longitude))
            {
                return (new DefaultGPS()).create(Double.parseDouble(latitude), Double.parseDouble(longitude));
            }
            throw new GeoServiceWrapperException(GeoServiceWrapperException.getErrorMessagesDesc(locationData.getCode()));
        }
        catch(GeoLocatorException | org.springframework.web.client.ResourceAccessException e)
        {
            throw new GeoServiceWrapperException(e);
        }
    }


    protected String appendBusinessParams(String urlAddress)
    {
        if(isBusinessAPI())
        {
            StringBuilder sb = new StringBuilder(urlAddress);
            sb.append("&");
            sb.append("client");
            sb.append("=");
            sb.append(getCliendId());
            return sb.toString();
        }
        if(!StringUtils.isEmpty(getGoogleKey()))
        {
            StringBuilder sb = new StringBuilder(urlAddress);
            sb.append("&");
            sb.append("key");
            sb.append("=");
            sb.append(getGoogleKey());
            String result = sb.toString();
            if(!result.startsWith("https"))
            {
                return result.replaceFirst("http", "https");
            }
            return result;
        }
        return urlAddress;
    }


    protected URI singAndEncodeURL(String urlAddress)
    {
        try
        {
            UrlSigner signer = new UrlSigner(getCryptoKey());
            URL url = new URL(urlAddress);
            String singnedUrl = signer.signRequest(url.getPath(), UriUtils.encodeQuery(url.getQuery(), "UTF-8"));
            return new URI(url.getProtocol() + "://" + url.getProtocol() + url.getHost());
        }
        catch(Exception e)
        {
            throw new GeoServiceWrapperException("Couldn't sign the request", e);
        }
    }


    protected boolean isBusinessAPI()
    {
        return (!StringUtils.isEmpty(getCliendId()) && !StringUtils.isEmpty(getCryptoKey()));
    }


    @Required
    public void setAddressLocationParser(ResponseExtractor<MapLocationData> addressLocationParser)
    {
        this.addressLocationParser = addressLocationParser;
    }


    protected ResponseExtractor<MapLocationData> getAddressLocationParser()
    {
        return this.addressLocationParser;
    }


    @Required
    public void setRouteDataParser(ResponseExtractor<RouteData> routeDataParser)
    {
        this.routeDataParser = routeDataParser;
    }


    protected ResponseExtractor<RouteData> getRouteDataParser()
    {
        return this.routeDataParser;
    }


    public void setBaseUrl(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }


    protected String getBaseUrl()
    {
        return this.baseUrl;
    }


    public void setGoogleKey(String googleKey)
    {
        this.googleKey = googleKey;
    }


    protected String getGoogleKey()
    {
        return this.googleKey;
    }


    @Required
    public void setDirectionsUrlBuilder(GeolocationDirectionsUrlBuilder directionsUrlBuilder)
    {
        this.directionsUrlBuilder = directionsUrlBuilder;
    }


    protected GeolocationDirectionsUrlBuilder getDirectionsUrlBuilder()
    {
        return this.directionsUrlBuilder;
    }


    public void setCliendId(String cliendId)
    {
        this.cliendId = cliendId;
    }


    protected String getCliendId()
    {
        return this.cliendId;
    }


    public void setCryptoKey(String signature)
    {
        this.cryptoKey = signature;
    }


    protected String getCryptoKey()
    {
        return this.cryptoKey;
    }
}
