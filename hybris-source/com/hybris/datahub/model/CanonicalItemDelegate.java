package com.hybris.datahub.model;

import com.hybris.datahub.runtime.domain.CanonicalItemPublicationStatus;
import java.util.Set;

public interface CanonicalItemDelegate
{
    Set<CanonicalItemPublicationStatus> getPublicationStatuses();


    Set<RawItem> getRawItems();
}
