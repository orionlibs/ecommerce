/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.retail.sapppspricing.impl;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.retail.opps.v1.dto.PriceCalculate;
import com.sap.retail.opps.v1.dto.PriceCalculateResponse;
import com.sap.retail.sapppspricing.PPSClient;
import com.sap.retail.sapppspricing.PPSConfigService;
import com.sap.retail.sapppspricing.SapPPSPricingRuntimeException;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedOAuthCredentialModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.store.services.BaseStoreService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * PPS client supporting remote call via REST. For the remote call OAuth
 * authentication is supported. via configuration (see {@link PPSConfigService})
 */
public class DefaultPPSClient extends DefaultPPSClientBeanAccessor implements PPSClient
{
    // ID to relate this request to entry in server log
    protected static final String X_REQUEST_ID = "x-request-id";
    protected static final String AUTHORIZATION = "Authorization";
    protected static final int SIZE = 8;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPPSClient.class);
    private BaseStoreService baseStoreService;
    private FlexibleSearchService flexibleSearchService;
    protected static final String DESTINATIONID = "scpOPPServiceDestination";
    private static final double TIMECONVERSION_TO_MS = 1000000;
    protected static final String DESTINATIONTARGET = "scpOPPSDestinationTarget";
    protected static final String grantType = "client_credentials";
    private DestinationService destinationService;


    /**
     * @return the destinationService
     */
    public DestinationService getDestinationService()
    {
        return destinationService;
    }


    /**
     * @param destinationService
     *           the destinationService to set
     */
    public void setDestinationService(final DestinationService destinationService)
    {
        this.destinationService = destinationService;
    }


    public PriceCalculateResponse callPPS(final PriceCalculate priceCalculate, final SAPConfigurationModel sapConfig)
    {
        LOG.debug("entering callPPSRemote()");
        final long t1 = System.nanoTime();
        PriceCalculateResponse responseBody = null;
        String str;
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                        HttpClients.createDefault());
        RestTemplate rest = new RestTemplate(requestFactory);
        HttpHeaders httpHeaders;
        final ConsumedDestinationModel destinationModel = (ConsumedDestinationModel)getDestinationService().getDestinationByIdAndByDestinationTargetId(DESTINATIONID, DESTINATIONTARGET);
        if(destinationModel == null)
        {
            throw new ModelNotFoundException("Provided destination was not found.");
        }
        final ConsumedOAuthCredentialModel credential = (ConsumedOAuthCredentialModel)destinationModel.getCredential();
        final String username = credential.getClientId();
        final String password = credential.getClientSecret();
        final String tokenUrl = credential.getOAuthUrl();
        try
        {
            httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            ArrayList<MediaType> al = new ArrayList<>();
            al.add(MediaType.APPLICATION_JSON);
            httpHeaders.setAccept(al);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(Include.NON_NULL);
            str = mapper.writeValueAsString(priceCalculate);
            LOG.debug("Request Body ={}", str);
        }
        catch(JsonProcessingException e)
        {
            LOG.error("Json processing exception {}", e.getMessage());
            return new PriceCalculateResponse();
        }
        String token = getToken(username, password, tokenUrl);
        httpHeaders.set(AUTHORIZATION, "Bearer " + token);
        try
        {
            ResponseEntity<PriceCalculateResponse> result = rest.exchange(destinationModel.getUrl(), HttpMethod.POST,
                            new HttpEntity<String>(str, httpHeaders), PriceCalculateResponse.class);
            responseBody = result.getBody();
            LOG.debug("Response Body ={}", responseBody);
            if(!result.getStatusCode().is2xxSuccessful())
            {
                throw new SapPPSPricingRuntimeException("Unexpected return code " + result.getStatusCode().value());
            }
            else
            {
                final long t2 = System.nanoTime();
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Request duration in ms {}", (t2 - t1) / TIMECONVERSION_TO_MS);
                }
                return responseBody;
            }
        }
        catch(Exception e)
        {
            LOG.error("Exception occured within Rest {}", e.getMessage());
        }
        finally
        {
            LOG.debug("exiting");
        }
        return responseBody;
    }


    private String getToken(String clientId, String clientSecret, String tokenUrl)
    {
        String result = "";
        URL url = null;
        InputStream stream = null;
        HttpURLConnection urlConnection = null;
        String[] token = null;
        try
        {
            url = new URL(tokenUrl);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            String data = URLEncoder.encode("client_id", "UTF-8") + "=" + URLEncoder.encode(clientId, "UTF-8");
            data += "&" + URLEncoder.encode("client_secret", "UTF-8") + "=" + URLEncoder.encode(clientSecret, "UTF-8");
            data += "&" + URLEncoder.encode("grant_type", "UTF-8") + "=" + URLEncoder.encode(grantType, "UTF-8");
            urlConnection.connect();
            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(data);
            wr.flush();
            stream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), SIZE);
            result = reader.readLine();
            String[] json = result.split(":");
            token = json[1].split("\"");
            result = token[1];
        }
        catch(IOException e)
        {
            LOG.error("Exception occured within getToken method {}", e.getMessage());
            return result;
        }
        finally
        {
            if(urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }
        return result;
    }


    public BaseStoreService getBaseStoreService()
    {
        return baseStoreService;
    }


    public void setBaseStoreService(final BaseStoreService baseStoreService)
    {
        this.baseStoreService = baseStoreService;
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return flexibleSearchService;
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
