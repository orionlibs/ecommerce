package com.hybris.datahub.composition;

import com.hybris.datahub.domain.CanonicalAttributeDefinition;
import com.hybris.datahub.model.CompositionGroup;
import com.hybris.datahub.model.RawItem;

public interface CompositionRuleHandler
{
    <T extends com.hybris.datahub.model.CanonicalItem> T compose(CanonicalAttributeDefinition paramCanonicalAttributeDefinition, CompositionGroup<? extends RawItem> paramCompositionGroup, T paramT);


    boolean isApplicable(CanonicalAttributeDefinition paramCanonicalAttributeDefinition);


    int getOrder();
}
