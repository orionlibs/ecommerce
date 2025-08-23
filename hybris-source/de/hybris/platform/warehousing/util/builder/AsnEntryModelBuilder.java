package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.warehousing.model.AdvancedShippingNoticeEntryModel;

public class AsnEntryModelBuilder
{
    private final AdvancedShippingNoticeEntryModel model = new AdvancedShippingNoticeEntryModel();


    private AdvancedShippingNoticeEntryModel getModel()
    {
        return this.model;
    }


    public static AsnEntryModelBuilder aModel()
    {
        return new AsnEntryModelBuilder();
    }


    public AdvancedShippingNoticeEntryModel build()
    {
        return getModel();
    }


    public AsnEntryModelBuilder withProductCode(String productCode)
    {
        getModel().setProductCode(productCode);
        return this;
    }


    public AsnEntryModelBuilder withQuantity(int qty)
    {
        getModel().setQuantity(qty);
        return this;
    }
}
