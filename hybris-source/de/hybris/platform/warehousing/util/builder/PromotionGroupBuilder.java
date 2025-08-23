package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.promotions.model.PromotionGroupModel;

public class PromotionGroupBuilder
{
    private final PromotionGroupModel model = new PromotionGroupModel();


    public static PromotionGroupBuilder aModel()
    {
        return new PromotionGroupBuilder();
    }


    private PromotionGroupModel getModel()
    {
        return this.model;
    }


    public PromotionGroupModel build()
    {
        return getModel();
    }


    public PromotionGroupBuilder withIdentifier(String identifier)
    {
        getModel().setIdentifier(identifier);
        return this;
    }
}
