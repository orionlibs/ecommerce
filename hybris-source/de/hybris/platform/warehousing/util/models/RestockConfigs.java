package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.warehousing.model.RestockConfigModel;
import de.hybris.platform.warehousing.util.builder.RestockConfigModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;
import org.springframework.beans.factory.annotation.Required;

public class RestockConfigs extends AbstractItems<RestockConfigModel>
{
    public static final String CODE_RETURNED_BIN = "returned_bin";
    private WarehousingDao<RestockConfigModel> restockConfigDao;


    public RestockConfigModel RestockAfterReturn()
    {
        return (RestockConfigModel)getOrSaveAndReturn(() -> (RestockConfigModel)getRestockConfigDao().getByCode(""), () -> RestockConfigModelBuilder.aModel().withDelayDaysBeforeRestock(0).withIsUpdateStockAfterReturn(Boolean.valueOf(true)).withReturnedBin("returned_bin").build());
    }


    public WarehousingDao<RestockConfigModel> getRestockConfigDao()
    {
        return this.restockConfigDao;
    }


    @Required
    public void setRestockConfigDao(WarehousingDao<RestockConfigModel> restockConfigDao)
    {
        this.restockConfigDao = restockConfigDao;
    }
}
