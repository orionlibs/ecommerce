package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.cockpit.model.search.impl.DefaultSortCriterion;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSearchOrderBy;

public class AttributeSortCriterion extends DefaultSortCriterion implements GenericSearchSortCriterion
{
    private String attributeQualifier;


    public AttributeSortCriterion(String qualifier)
    {
        super(qualifier);
    }


    public String getAttributeQualifier()
    {
        return (this.attributeQualifier != null) ? this.attributeQualifier : getQualifier();
    }


    public void setAttributeQualifier(String attributeQualifier)
    {
        this.attributeQualifier = attributeQualifier;
    }


    public GenericSearchOrderBy createOrderBy(GenericQuery query, boolean asc)
    {
        return new GenericSearchOrderBy(new GenericSearchField(getAttributeQualifier()), asc);
    }
}
