package de.hybris.platform.personalizationservices.stub;

import de.hybris.platform.personalizationservices.dynamic.CxCustomizationActiveAttributeHandler;
import de.hybris.platform.personalizationservices.dynamic.CxCustomizationRankAttributeHandler;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.strategies.RankAssignmentStrategy;
import de.hybris.platform.personalizationservices.strategies.impl.DefaultRankAssignmentStrategy;
import de.hybris.platform.servicelayer.time.TimeService;

public class CxCustomizationModelStub extends CxCustomizationModel
{
    private final CxCustomizationRankAttributeHandler rankHandler;
    private final CxCustomizationActiveAttributeHandler activeHandler;


    public CxCustomizationModelStub()
    {
        this((TimeService)new MockTimeService());
    }


    public CxCustomizationModelStub(TimeService timeService)
    {
        this.rankHandler = new CxCustomizationRankAttributeHandler();
        this.rankHandler.setRankAssigmentStrategy((RankAssignmentStrategy)new DefaultRankAssignmentStrategy());
        this.activeHandler = new CxCustomizationActiveAttributeHandler();
        this.activeHandler.setTimeService(timeService);
    }


    public void setRank(Integer value)
    {
        this.rankHandler.set(this, value);
    }


    public Integer getRank()
    {
        return this.rankHandler.get(this);
    }


    public boolean isActive()
    {
        return this.activeHandler.get(this).booleanValue();
    }
}
