package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.warehousing.model.RestockConfigModel;

public class RestockConfigModelBuilder
{
    private final RestockConfigModel model = new RestockConfigModel();


    private RestockConfigModel getModel()
    {
        return this.model;
    }


    public static RestockConfigModelBuilder aModel()
    {
        return new RestockConfigModelBuilder();
    }


    public RestockConfigModelBuilder withReturnedBin(String returnedBin)
    {
        getModel().setReturnedBinCode(returnedBin);
        return this;
    }


    public RestockConfigModelBuilder withIsUpdateStockAfterReturn(Boolean isUpdateStockAfterReturn)
    {
        getModel().setIsUpdateStockAfterReturn(isUpdateStockAfterReturn);
        return this;
    }


    public RestockConfigModelBuilder withDelayDaysBeforeRestock(int delayDaysBeforeRestock)
    {
        getModel().setDelayDaysBeforeRestock(0);
        return this;
    }


    public RestockConfigModel build()
    {
        return getModel();
    }
}
