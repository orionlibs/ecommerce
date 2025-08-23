package de.hybris.platform.servicelayer.user.daos.impl;

import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.user.daos.TitleDao;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DefaultTitleDao extends DefaultGenericDao<TitleModel> implements TitleDao
{
    public DefaultTitleDao()
    {
        super("Title");
    }


    public Collection<TitleModel> findTitles()
    {
        return find();
    }


    public TitleModel findTitleByCode(String code)
    {
        return findUnique(Collections.singletonMap("code", code));
    }


    private TitleModel findUnique(Map<String, Object> params)
    {
        List<TitleModel> results = find(params);
        if(results.size() > 1)
        {
            throw new AmbiguousIdentifierException("Found " + results.size() + " objects from type Title with " + params
                            .toString() + "'");
        }
        return results.isEmpty() ? null : results.get(0);
    }
}
