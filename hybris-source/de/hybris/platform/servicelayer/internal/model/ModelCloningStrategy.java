package de.hybris.platform.servicelayer.internal.model;

public interface ModelCloningStrategy
{
    <T> T clone(T paramT, ModelCloningContext paramModelCloningContext);


    <T> T clone(Object paramObject, String paramString, ModelCloningContext paramModelCloningContext);
}
