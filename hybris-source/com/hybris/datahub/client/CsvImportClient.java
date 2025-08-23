package com.hybris.datahub.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

public class CsvImportClient extends RestClient
{
    public CsvImportClient()
    {
        this(null, "");
    }


    public CsvImportClient(String uri)
    {
        this(null, uri);
    }


    public CsvImportClient(ClientConfiguration cfg, String baseUri)
    {
        super(cfg, baseUri);
    }


    public Response importCsv(String feedName, String rawItemType, String[] csvLines)
    {
        String csv = StringUtils.join((Object[])csvLines, System.lineSeparator());
        return importCsv(feedName, rawItemType, csv);
    }


    public Response importCsv(String feedName, String rawItemType, String csvInput)
    {
        return importCsv(feedName, rawItemType, toInputStream(csvInput));
    }


    public Response importCsv(String feedName, String itemType, InputStream csvInput)
    {
        log().info("Uploading raw {} item data to feed: {}", itemType, feedName);
        return resource("/data-feeds/" + feedName + "/items/" + itemType)
                        .accept(new MediaType[] {MediaType.APPLICATION_XML_TYPE}).post(Entity.entity(csvInput, MediaType.APPLICATION_OCTET_STREAM_TYPE));
    }


    public boolean uploadCsv(String feedName, String rawItemType, String[] csvLines)
    {
        String csv = StringUtils.join((Object[])csvLines, System.lineSeparator());
        return uploadCsv(feedName, rawItemType, csv);
    }


    public boolean uploadCsv(String feedName, String rawItemType, String csvInput)
    {
        return uploadCsv(feedName, rawItemType, toInputStream(csvInput));
    }


    public boolean uploadCsv(String feedName, String itemType, InputStream csvInput)
    {
        log().info("Uploading raw {} item data to feed {}", itemType, feedName);
        Response response = resource("/data-feeds/" + feedName + "/items/" + itemType).accept(new MediaType[] {MediaType.APPLICATION_XML_TYPE}).post(Entity.entity(csvInput, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        return (Response.Status.OK.getStatusCode() == response.getStatus());
    }


    private ByteArrayInputStream toInputStream(String csv)
    {
        return new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8));
    }
}
