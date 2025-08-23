package de.hybris.platform.servicelayer.model.attribute;

public interface DynamicAttributeHandler<VALUE, MODEL extends de.hybris.platform.servicelayer.model.AbstractItemModel>
{
    VALUE get(MODEL paramMODEL);


    void set(MODEL paramMODEL, VALUE paramVALUE);
}
