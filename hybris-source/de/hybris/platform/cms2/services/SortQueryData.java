package de.hybris.platform.cms2.services;

import de.hybris.platform.servicelayer.internal.dao.Dao;

public interface SortQueryData
{
    Class<? extends Dao> getTypeClass();


    String getSortCode();


    String getQuery();


    boolean isDefault();
}
