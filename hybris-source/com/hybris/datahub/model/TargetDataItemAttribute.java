package com.hybris.datahub.model;

public interface TargetDataItemAttribute extends DataItemAttribute
{
    String getExportCode();


    boolean isExportCodeExpression();
}
