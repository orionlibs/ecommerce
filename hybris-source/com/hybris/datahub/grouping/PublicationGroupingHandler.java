package com.hybris.datahub.grouping;

import java.util.List;

public interface PublicationGroupingHandler
{
    <T extends com.hybris.datahub.model.CanonicalItem> List<T> group(T paramT, TargetItemCreationContext paramTargetItemCreationContext);


    <T extends com.hybris.datahub.model.CanonicalItem> boolean isApplicable(T paramT, TargetItemCreationContext paramTargetItemCreationContext);


    int getOrder();
}
