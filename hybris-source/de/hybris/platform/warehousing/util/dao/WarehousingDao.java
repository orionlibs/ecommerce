package de.hybris.platform.warehousing.util.dao;

public interface WarehousingDao<T>
{
    T getByCode(String paramString);
}
