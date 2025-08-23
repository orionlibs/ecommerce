package com.hybris.datahub.model;

public interface DataItemAttribute
{
    String getPropertyName();


    Class<?> getPropertyType();


    boolean isLocalizable();


    boolean isCollection();


    boolean isSecured();


    String getItemType();
}
