package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.warehousing.model.SourcingConfigModel;
import de.hybris.platform.warehousing.util.builder.SourcingConfigModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;
import org.springframework.beans.factory.annotation.Required;

public class SourcingConfigs extends AbstractItems<SourcingConfigModel>
{
    public static final String SOURCING_CONFIG_NAME = "hybris";
    private WarehousingDao<SourcingConfigModel> sourcingConfigDao;


    public SourcingConfigModel HybrisConfig()
    {
        return (SourcingConfigModel)getOrSaveAndReturn(() -> (SourcingConfigModel)getSourcingConfigDao().getByCode("hybris"), () -> SourcingConfigModelBuilder.aModel().withCode("hybris").withSourcingFactorsWeight(40, 30, 20, 10).build());
    }


    public WarehousingDao<SourcingConfigModel> getSourcingConfigDao()
    {
        return this.sourcingConfigDao;
    }


    @Required
    public void setSourcingConfigDao(WarehousingDao<SourcingConfigModel> sourcingConfigDao)
    {
        this.sourcingConfigDao = sourcingConfigDao;
    }
}
