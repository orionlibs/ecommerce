package de.hybris.platform.cms2.services;

import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;
import java.util.Optional;

public interface SortQueryDataRegistry
{
    Optional<SortQueryData> getSortQueryData(Class<? extends Dao> paramClass, String paramString);


    Optional<SortQueryData> getDefaultSortQueryData(Class<? extends Dao> paramClass);


    Collection<SortQueryData> getAllSortQueryData();
}
