package com.hybris.datahub.service.publication;

import com.hybris.datahub.model.TargetItem;
import com.hybris.datahub.model.TargetSystemItemKey;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public interface PublicationContext
{
    Set<Locale> getLocales(TargetSystemItemKey paramTargetSystemItemKey);


    List<Long> getLastProcessedIds(TargetSystemItemKey paramTargetSystemItemKey);


    void addToContext(List<TargetItem> paramList);
}
