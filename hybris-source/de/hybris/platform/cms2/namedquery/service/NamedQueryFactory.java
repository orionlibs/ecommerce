package de.hybris.platform.cms2.namedquery.service;

import de.hybris.platform.cms2.exceptions.InvalidNamedQueryException;

public interface NamedQueryFactory
{
    String getNamedQuery(String paramString) throws InvalidNamedQueryException;
}
