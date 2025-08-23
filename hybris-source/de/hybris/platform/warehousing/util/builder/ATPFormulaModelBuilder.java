package de.hybris.platform.warehousing.util.builder;

import com.google.common.collect.Sets;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.warehousing.model.AtpFormulaModel;

public class ATPFormulaModelBuilder
{
    private final AtpFormulaModel model = new AtpFormulaModel();


    private AtpFormulaModel getModel()
    {
        return this.model;
    }


    public static ATPFormulaModelBuilder aModel()
    {
        return new ATPFormulaModelBuilder();
    }


    public AtpFormulaModel build()
    {
        return getModel();
    }


    public ATPFormulaModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public ATPFormulaModelBuilder withBaseStore(BaseStoreModel baseStore)
    {
        getModel().setBaseStores(Sets.newHashSet((Object[])new BaseStoreModel[] {baseStore}));
        return this;
    }


    public ATPFormulaModelBuilder withFormula(Boolean availability, Boolean allocation, Boolean cancellation, Boolean increase, Boolean reserved, Boolean shrinkage, Boolean wastage, Boolean returned, Boolean external)
    {
        getModel().setAvailability(availability);
        getModel().setAllocation(allocation);
        getModel().setCancellation(cancellation);
        getModel().setIncrease(increase);
        getModel().setReserved(reserved);
        getModel().setShrinkage(shrinkage);
        getModel().setWastage(wastage);
        getModel().setReturned(returned);
        getModel().setExternal(external);
        return this;
    }
}
