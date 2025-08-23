package com.hybris.backoffice.solrsearch.decorators.impl;

import com.hybris.backoffice.solrsearch.decorators.SearchConditionDecorator;

public abstract class AbstractOrderedSearchConditionDecorator implements SearchConditionDecorator
{
    private int order;


    public void setOrder(int order)
    {
        this.order = order;
    }


    public int getOrder()
    {
        return this.order;
    }
}
