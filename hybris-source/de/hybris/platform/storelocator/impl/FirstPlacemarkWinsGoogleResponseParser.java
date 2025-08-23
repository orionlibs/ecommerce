package de.hybris.platform.storelocator.impl;

import de.hybris.platform.storelocator.data.MapLocationData;
import de.hybris.platform.storelocator.exception.GeoDocumentParsingException;
import java.io.IOException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class FirstPlacemarkWinsGoogleResponseParser implements ResponseExtractor<MapLocationData>
{
    public MapLocationData extractData(ClientHttpResponse response) throws IOException
    {
        MapLocationData locationData = new MapLocationData();
        XPath xpath = XPathFactory.newInstance().newXPath();
        InputSource inputSource = new InputSource(response.getBody());
        try
        {
            Node root = (Node)xpath.evaluate("/", inputSource, XPathConstants.NODE);
            String lat = xpath.evaluate("/GeocodeResponse/result[1]/geometry/location/lat", root);
            String longitude = xpath.evaluate("/GeocodeResponse/result[1]/geometry/location/lng", root);
            locationData.setLatitude(lat);
            locationData.setLongitude(longitude);
        }
        catch(XPathExpressionException e)
        {
            throw new GeoDocumentParsingException(e.getMessage(), e);
        }
        return locationData;
    }
}
