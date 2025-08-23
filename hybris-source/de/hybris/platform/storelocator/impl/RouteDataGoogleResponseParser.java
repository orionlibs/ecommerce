package de.hybris.platform.storelocator.impl;

import de.hybris.platform.storelocator.data.RouteData;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;
import java.io.IOException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class RouteDataGoogleResponseParser implements ResponseExtractor<RouteData>
{
    public RouteData extractData(ClientHttpResponse response) throws IOException
    {
        RouteData routeData = new RouteData();
        XPath xpath = XPathFactory.newInstance().newXPath();
        InputSource inputSource = new InputSource(response.getBody());
        try
        {
            Node root = (Node)xpath.evaluate("/", inputSource, XPathConstants.NODE);
            String status = xpath.evaluate("/DirectionsResponse/status", root);
            if(!"OK".equalsIgnoreCase(status))
            {
                throw new GeoServiceWrapperException("Could not get directions : ", status);
            }
            routeData.setCoordinates(xpath.evaluate("/DirectionsResponse/route/overview_polyline/points", root));
            routeData.setDuration(Double.parseDouble(xpath.evaluate("/DirectionsResponse/route/leg/duration/value", root)));
            routeData.setDurationText(xpath.evaluate("/DirectionsResponse/route/leg/duration/text", root));
            routeData.setDistance(Double.parseDouble(xpath.evaluate("/DirectionsResponse/route/leg/distance/value", root)));
            routeData.setDistanceText(xpath.evaluate("/DirectionsResponse/route/leg/distance/text", root));
        }
        catch(XPathExpressionException e)
        {
            throw new GeoServiceWrapperException("Cannot get Google response due to :", e);
        }
        return routeData;
    }
}
