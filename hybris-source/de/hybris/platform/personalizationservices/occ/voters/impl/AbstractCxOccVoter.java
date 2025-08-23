package de.hybris.platform.personalizationservices.occ.voters.impl;

import de.hybris.platform.personalizationservices.occ.voters.CxOccVoter;
import de.hybris.platform.personalizationservices.voters.Vote;
import java.util.HashSet;

public abstract class AbstractCxOccVoter implements CxOccVoter
{
    private int order;


    public AbstractCxOccVoter(int order)
    {
        this.order = order;
    }


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }


    protected Vote getDefaultVote()
    {
        Vote result = new Vote();
        result.setRecalculateActions(new HashSet());
        return result;
    }
}
