package com.hybris.backoffice.searchservices.providers.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.impl.ModelAttributeSnIndexerValueProvider;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateSnIndexerValueProvider extends ModelAttributeSnIndexerValueProvider
{
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    protected Object getFieldValue(SnIndexerContext indexerContext, SnIndexerFieldWrapper fieldWrapper, ItemModel source, Void data) throws SnIndexerException
    {
        Object date = super.getFieldValue(indexerContext, fieldWrapper, source, data);
        if(date instanceof Date)
        {
            return DATE_FORMAT.withZone(ZoneOffset.UTC).format(((Date)date).toInstant());
        }
        return date;
    }


    @Deprecated(since = "2205", forRemoval = true)
    public void unload()
    {
    }
}
