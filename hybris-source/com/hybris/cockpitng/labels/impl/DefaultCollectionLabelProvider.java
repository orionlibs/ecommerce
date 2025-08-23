/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels.impl;

import com.hybris.cockpitng.labels.LabelProvider;
import com.hybris.cockpitng.labels.LabelService;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Provides a string label for collection of objects. Uses {@link LabelService} for every instance in the collection and
 * spring configured separator. See {@link #setSeparator(String)}.
 */
public class DefaultCollectionLabelProvider implements LabelProvider<Collection<?>>
{
    private static final String DEFAULT_SEPARATOR = ", ";
    private LabelService labelService;
    private String separator = DEFAULT_SEPARATOR;


    @Override
    public String getLabel(final Collection<?> collection)
    {
        if(CollectionUtils.isNotEmpty(collection))
        {
            final StringBuilder result = new StringBuilder();
            for(final Iterator<?> iterator = collection.iterator(); iterator.hasNext(); )
            {
                result.append(labelService.getObjectLabel(iterator.next()));
                if(iterator.hasNext())
                {
                    result.append(getSeparator());
                }
            }
            return result.toString();
        }
        return StringUtils.EMPTY;
    }


    protected String getSeparator()
    {
        return separator;
    }


    /**
     * @param separator the separator to set
     */
    public void setSeparator(final String separator)
    {
        this.separator = separator;
    }


    @Override
    public String getDescription(final Collection<?> object)
    {
        return null;
    }


    @Override
    public String getIconPath(final Collection<?> object)
    {
        return null;
    }


    /**
     * @return the labelService
     */
    protected LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }
}
