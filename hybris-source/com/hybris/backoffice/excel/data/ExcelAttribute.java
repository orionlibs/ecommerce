package com.hybris.backoffice.excel.data;

public interface ExcelAttribute
{
    String getName();


    boolean isLocalized();


    String getIsoCode();


    String getQualifier();


    boolean isMandatory();


    String getType();


    boolean isMultiValue();
}
