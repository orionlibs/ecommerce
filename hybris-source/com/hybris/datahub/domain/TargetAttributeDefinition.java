package com.hybris.datahub.domain;

import javax.validation.constraints.Size;

public interface TargetAttributeDefinition extends AttributeDefinition
{
    public static final String _TYPECODE = "TargetAttributeDefinition";


    TargetItemMetadata getTargetItemMetadata();


    void setTargetItemMetadata(TargetItemMetadata paramTargetItemMetadata);


    @Size(max = 255)
    String getTransformationExpression();


    void setTransformationExpression(String paramString);


    @Size(max = 255)
    String getExportCode();


    void setExportCode(String paramString);


    boolean isMandatoryInHeader();


    void setMandatoryInHeader(boolean paramBoolean);


    boolean isExportCodeExpression();


    void setExportCodeExpression(boolean paramBoolean);


    boolean isActive();


    void setActive(boolean paramBoolean);
}
