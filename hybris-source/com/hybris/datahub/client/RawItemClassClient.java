package com.hybris.datahub.client;

import com.hybris.datahub.dto.metadata.AttributeData;
import com.hybris.datahub.dto.metadata.ItemTypeData;
import com.hybris.datahub.dto.metadata.RawAttributeData;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.GenericType;

public class RawItemClassClient extends ItemClassClient<RawAttributeData, ItemTypeData>
{
    public RawItemClassClient(ClientConfiguration cfg, String baseUrl)
    {
        super(cfg, baseUrl, RawAttributeData.class);
    }


    public RawItemClassClient(String uri)
    {
        this(null, uri);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public RawAttributeData createAttribute(String type, String attrName)
    {
        return (RawAttributeData)createAttribute((AttributeData)createAttributeData(type, attrName, false));
    }


    @Deprecated(since = "ages", forRemoval = true)
    public RawAttributeData createSecuredAttribute(String type, String attrName)
    {
        return (RawAttributeData)createAttribute((AttributeData)createAttributeData(type, attrName, true));
    }


    private static RawAttributeData createAttributeData(String type, String attrName, boolean isSecured)
    {
        RawAttributeData rawAttributeData = new RawAttributeData();
        rawAttributeData.setItemType(type);
        rawAttributeData.setName(attrName);
        rawAttributeData.setIsSecured(isSecured);
        return rawAttributeData;
    }


    public List<ItemTypeData> getItemTypes()
    {
        return getItemTypes((GenericType)new Object(this));
    }


    public Collection<RawAttributeData> getAttributes(String itemType)
    {
        return getAttributes(itemType, (GenericType)new Object(this));
    }


    protected String typesPath()
    {
        return "/item-classes/raw/item-types";
    }
}
