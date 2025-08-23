package de.hybris.platform.personalizationservices.stub;

import de.hybris.platform.personalizationservices.dynamic.CxVariationActiveAttributeHandler;
import de.hybris.platform.personalizationservices.dynamic.CxVariationRankAttributeHandler;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.strategies.RankAssignmentStrategy;
import de.hybris.platform.personalizationservices.strategies.impl.DefaultRankAssignmentStrategy;
import de.hybris.platform.servicelayer.session.MockSessionService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.time.impl.DefaultTimeService;

public class CxVariationModelStub extends CxVariationModel
{
    private final CxVariationRankAttributeHandler rankHandler;
    private final CxVariationActiveAttributeHandler activeHandler;


    public CxVariationModelStub()
    {
        this.rankHandler = new CxVariationRankAttributeHandler();
        this.rankHandler.setRankAssigmentStrategy((RankAssignmentStrategy)new DefaultRankAssignmentStrategy());
        DefaultTimeService timeService = new DefaultTimeService();
        timeService.setSessionService((SessionService)new MockSessionService());
        this.activeHandler = new CxVariationActiveAttributeHandler();
    }


    public Integer getRank()
    {
        return this.rankHandler.get(this);
    }


    public void setRank(Integer value)
    {
        this.rankHandler.set(this, value);
    }


    public boolean isActive()
    {
        return this.activeHandler.get(this).booleanValue();
    }
}
