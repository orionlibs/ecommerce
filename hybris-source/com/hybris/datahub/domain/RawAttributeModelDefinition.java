package com.hybris.datahub.domain;

public interface RawAttributeModelDefinition extends AttributeDefinition
{
    public static final String _TYPECODE = "RawAttributeModelDefinition";


    RawItemMetadata getRawItemMetadata();


    void setRawItemMetadata(RawItemMetadata paramRawItemMetadata);
}
