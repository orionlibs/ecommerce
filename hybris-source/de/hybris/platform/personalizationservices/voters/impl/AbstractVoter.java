package de.hybris.platform.personalizationservices.voters.impl;

import de.hybris.platform.personalizationservices.voters.Vote;
import de.hybris.platform.personalizationservices.voters.Voter;
import java.util.HashSet;

public abstract class AbstractVoter implements Voter
{
    private int order;


    public AbstractVoter(int order)
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
