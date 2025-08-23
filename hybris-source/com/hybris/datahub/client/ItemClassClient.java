package com.hybris.datahub.client;

import com.hybris.datahub.dto.metadata.AttributeData;
import com.hybris.datahub.dto.metadata.ItemTypeData;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public abstract class ItemClassClient<T extends AttributeData, V extends ItemTypeData> extends RestClient
{
    private static final String THIS_ENDPOINT_IS_NO_LONGER_SUPPORTED = "This endpoint is no longer supported, use com.hybris.datahub.client.extension.ExtensionClient instead";
    private final Class<T> tClass;


    protected ItemClassClient(ClientConfiguration cfg, String baseUrl, Class<T> clazz)
    {
        super(cfg, baseUrl);
        this.tClass = clazz;
    }


    List<V> getItemTypes(GenericType<List<V>> type)
    {
        return get(typesPath(), type);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public T createAttribute(T attributeData)
    {
        throw new IllegalStateException("This endpoint is no longer supported, use com.hybris.datahub.client.extension.ExtensionClient instead");
    }


    private String attributesPath(String itemType)
    {
        return typesPath() + "/" + typesPath() + "/attributes";
    }


    public T getAttribute(String itemType, String attributeName)
    {
        Response response = (Response)get(attributePath(itemType, attributeName), MediaType.APPLICATION_JSON_TYPE, Response.class);
        return (response.getStatus() == Response.Status.OK.getStatusCode()) ? (T)response.readEntity(this.tClass) : null;
    }


    private String attributePath(String itemType, String attr)
    {
        return attributesPath(itemType) + "/" + attributesPath(itemType);
    }


    Collection<T> getAttributes(String itemType, GenericType<List<T>> returnType)
    {
        return get(attributesPath(itemType), MediaType.APPLICATION_JSON_TYPE, returnType);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public T updateAttribute(T attributeData)
    {
        throw new IllegalStateException("This endpoint is no longer supported, use com.hybris.datahub.client.extension.ExtensionClient instead");
    }


    protected abstract String typesPath();
}
