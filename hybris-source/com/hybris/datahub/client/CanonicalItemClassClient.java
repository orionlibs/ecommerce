package com.hybris.datahub.client;

import com.google.common.collect.Sets;
import com.hybris.datahub.dto.metadata.AttributeData;
import com.hybris.datahub.dto.metadata.CanonicalAttributeData;
import com.hybris.datahub.dto.metadata.CanonicalItemTypeData;
import com.hybris.datahub.dto.metadata.CanonicalTransformationData;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.GenericType;

public class CanonicalItemClassClient extends ItemClassClient<CanonicalAttributeData, CanonicalItemTypeData>
{
    public CanonicalItemClassClient()
    {
        this(null, "");
    }


    public CanonicalItemClassClient(String baseUrl)
    {
        this(null, baseUrl);
    }


    public CanonicalItemClassClient(ClientConfiguration cfg, String baseUrl)
    {
        super(cfg, baseUrl, CanonicalAttributeData.class);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public CanonicalAttributeData createLocalizedAttribute(String canonicalType, String name, String rawType)
    {
        return (CanonicalAttributeData)createAttribute((AttributeData)createLocalizedAttributeData(canonicalType, name, rawType));
    }


    private static CanonicalAttributeData createAttributeData(String type, String name)
    {
        CanonicalAttributeData attribute = new CanonicalAttributeData();
        attribute.setItemType(type);
        attribute.setName(name);
        return attribute;
    }


    private static CanonicalTransformationData createTransformationData(String rawType, String rawAttribute)
    {
        CanonicalTransformationData transformationData = new CanonicalTransformationData();
        transformationData.setRawItemType(rawType);
        transformationData.setExpression(rawAttribute);
        return transformationData;
    }


    private static CanonicalAttributeData createLocalizedAttributeData(String canonicalType, String name, String rawType)
    {
        CanonicalAttributeData attribute = createAttributeData(canonicalType, name);
        attribute.setIsLocalizable(true);
        if(rawType != null)
        {
            attribute.setTransformations(Sets.newHashSet((Object[])new CanonicalTransformationData[] {createTransformationData(rawType, name)}));
        }
        return attribute;
    }


    public List<CanonicalItemTypeData> getItemTypes()
    {
        return getItemTypes((GenericType)new Object(this));
    }


    public Collection<CanonicalAttributeData> getAttributes(String itemType)
    {
        return getAttributes(itemType, (GenericType)new Object(this));
    }


    protected String typesPath()
    {
        return "/item-classes/canonical/item-types";
    }
}
