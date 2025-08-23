package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel;
import java.util.Locale;

public class PickUpDeliveryModeModelBuilder
{
    private final PickUpDeliveryModeModel model = new PickUpDeliveryModeModel();


    private PickUpDeliveryModeModel getModel()
    {
        return this.model;
    }


    public static PickUpDeliveryModeModelBuilder aModel()
    {
        return new PickUpDeliveryModeModelBuilder();
    }


    public PickUpDeliveryModeModel build()
    {
        return getModel();
    }


    public PickUpDeliveryModeModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public PickUpDeliveryModeModelBuilder withActive(Boolean active)
    {
        getModel().setActive(active);
        return this;
    }


    public PickUpDeliveryModeModelBuilder withName(String name, Locale locale)
    {
        getModel().setName(name, locale);
        return this;
    }
}
