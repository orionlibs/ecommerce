package com.hybris.datahub.client;

import com.hybris.datahub.dto.integration.RawFragmentData;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

public class RawFragmentClient extends RestClient
{
    public RawFragmentClient()
    {
        this(null, "");
    }


    public RawFragmentClient(String uri)
    {
        this(null, uri);
    }


    public RawFragmentClient(ClientConfiguration cfg, String uri)
    {
        super(cfg, uri);
    }


    public void importRawFragmentMessage()
    {
        resource("/raw-fragments/raw-fragment-message/").accept(new MediaType[] {MediaType.APPLICATION_XML_TYPE}).post(Entity.entity("", MediaType.APPLICATION_XML_TYPE));
    }


    public void importRawFragmentData(RawFragmentData rawFragmentData)
    {
        resource("/raw-fragments/raw-fragment-data/").accept(new MediaType[] {MediaType.APPLICATION_XML_TYPE}).post(Entity.entity(rawFragmentData, MediaType.APPLICATION_XML_TYPE));
    }


    public void importConcurrentMessages(String feed, int count)
    {
        resource("/integration-channel/concurrent-message-loading/" + feed + "/" + count).accept(new MediaType[] {MediaType.APPLICATION_XML_TYPE}).post(Entity.entity("", MediaType.APPLICATION_XML_TYPE));
    }
}
