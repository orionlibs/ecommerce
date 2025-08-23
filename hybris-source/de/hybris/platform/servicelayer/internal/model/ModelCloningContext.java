package de.hybris.platform.servicelayer.internal.model;

public interface ModelCloningContext
{
    boolean skipAttribute(Object paramObject, String paramString);


    boolean treatAsPartOf(Object paramObject, String paramString);


    boolean usePresetValue(Object paramObject, String paramString);


    Object getPresetValue(Object paramObject, String paramString);
}
