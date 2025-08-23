package de.hybris.platform.personalizationservices.strategies.impl;

import de.hybris.platform.personalizationservices.strategies.RankAssignmentStrategy;

public class DefaultRankAssignmentStrategy implements RankAssignmentStrategy
{
    public int getRank(Integer suggested, int collectionSize)
    {
        if(suggested == null)
        {
            return collectionSize;
        }
        int val = suggested.intValue();
        if(val < 0)
        {
            return 0;
        }
        if(val > collectionSize)
        {
            return collectionSize;
        }
        return val;
    }
}
