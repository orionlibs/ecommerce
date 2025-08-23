package com.hybris.datahub.grouping;

import com.hybris.datahub.model.CompositionGroup;
import java.util.List;

public interface GroupingHandler
{
    <T extends com.hybris.datahub.model.RawItem> List<CompositionGroup<T>> group(CompositionGroup<T> paramCompositionGroup);


    <T extends com.hybris.datahub.model.RawItem> boolean isApplicable(CompositionGroup<T> paramCompositionGroup);


    int getOrder();
}
