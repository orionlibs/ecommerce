package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import java.util.Locale;

public class DeliveryModeModelBuilder
{
    private final DeliveryModeModel model = new DeliveryModeModel();


    private DeliveryModeModel getModel()
    {
        return this.model;
    }


    public static DeliveryModeModelBuilder aModel()
    {
        return new DeliveryModeModelBuilder();
    }


    public DeliveryModeModel build()
    {
        return getModel();
    }


    public DeliveryModeModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public DeliveryModeModelBuilder withActive(Boolean active)
    {
        getModel().setActive(active);
        return this;
    }


    public DeliveryModeModelBuilder withName(String name, Locale locale)
    {
        getModel().setName(name, locale);
        return this;
    }
}
