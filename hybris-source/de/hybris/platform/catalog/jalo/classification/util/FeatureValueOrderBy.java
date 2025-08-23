package de.hybris.platform.catalog.jalo.classification.util;

import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSearchOrderBy;

public class FeatureValueOrderBy extends GenericSearchOrderBy
{
    public static FeatureValueOrderBy orderBy(ClassAttributeAssignment assignment)
    {
        return new FeatureValueOrderBy(assignment, null, true);
    }


    public static FeatureValueOrderBy orderBy(ClassAttributeAssignment assignment, boolean asc)
    {
        return new FeatureValueOrderBy(assignment, null, asc);
    }


    protected FeatureValueOrderBy(ClassAttributeAssignment assignment, String productTypeAlias, boolean asc)
    {
        super((GenericSearchField)new FeatureField(assignment, productTypeAlias, null, true, true), asc);
    }
}
