package de.hybris.platform.warehousing.util.builder;

import com.google.common.collect.Sets;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.warehousing.model.SourcingConfigModel;

public class SourcingConfigModelBuilder
{
    private final SourcingConfigModel model = new SourcingConfigModel();


    private SourcingConfigModel getModel()
    {
        return this.model;
    }


    public static SourcingConfigModelBuilder aModel()
    {
        return new SourcingConfigModelBuilder();
    }


    public SourcingConfigModel build()
    {
        return getModel();
    }


    public SourcingConfigModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public SourcingConfigModelBuilder withBaseStore(BaseStoreModel baseStore)
    {
        getModel().setBaseStores(Sets.newHashSet((Object[])new BaseStoreModel[] {baseStore}));
        return this;
    }


    public SourcingConfigModelBuilder withSourcingFactorsWeight(int distanceWeightFactor, int allocationWeightFactor, int priorityWeightFactor, int scoreWeightFactor)
    {
        SourcingConfigModel sourcingConfig = getModel();
        sourcingConfig.setDistanceWeightFactor(distanceWeightFactor);
        sourcingConfig.setAllocationWeightFactor(allocationWeightFactor);
        sourcingConfig.setPriorityWeightFactor(priorityWeightFactor);
        sourcingConfig.setScoreWeightFactor(scoreWeightFactor);
        return this;
    }
}
