package com.hybris.datahub.domain;

import javax.validation.constraints.Size;

public interface CanonicalAttributeDefinition
{
    public static final String _TYPECODE = "CanonicalAttributeDefinition";


    @Size(max = 255)
    String getRawItemType();


    void setRawItemType(String paramString);


    CanonicalAttributeModelDefinition getCanonicalAttributeModelDefinition();


    void setCanonicalAttributeModelDefinition(CanonicalAttributeModelDefinition paramCanonicalAttributeModelDefinition);


    boolean isActive();


    void setActive(boolean paramBoolean);


    @Size(max = 255)
    String getReferenceAttribute();


    void setReferenceAttribute(String paramString);
}
