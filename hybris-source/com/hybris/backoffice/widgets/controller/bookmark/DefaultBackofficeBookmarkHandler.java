/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.controller.bookmark;

import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.widgets.controller.bookmark.DefaultBookmarkHandler;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultBackofficeBookmarkHandler extends DefaultBookmarkHandler
{
    /**
     * The field is not used anymore.
     *
     * @deprecated since 2005
     */
    @Deprecated(since = "2005", forRemoval = true)
    public static final String QUERY = String.format("SELECT {entity:%s} FROM {%s AS entity} WHERE {entity:%s}=?%s", ItemModel.PK,
                    ItemModel._TYPECODE, ItemModel.PK, ItemModel.PK);
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBackofficeBookmarkHandler.class);
    private final FlexibleSearchService flexibleSearchService;


    public DefaultBackofficeBookmarkHandler(final FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    /**
     * This implementation is based on Flexible Search in order to get search restrictions applied to the entity. Simply
     * loading the entity via Model Service would lead to omitting the restrictions.
     *
     * @param id
     *           identifier of the entity to load
     * @param <T>
     *           the type of the entity to be loaded
     * @return the entity (ItemModel) denoted by the type or the result of calling super-type's {@link #loadObject(String)}
     *         method if the id does not denote an ItemModel
     * @throws ObjectNotFoundException
     *            in case the id does not denote a valid entity (entity can't be found)
     */
    @Override
    public <T> T loadObject(final String id) throws ObjectNotFoundException
    {
        try
        {
            final PK pk = PK.parse(id);
            final ComposedType composedType = TypeManager.getInstance().getRootComposedType(pk.getTypeCode());
            final String typeCode = composedType.getCode();
            if(getPermissionFacade().canReadType(typeCode))
            {
                final String flexibleQuery = String.format("SELECT {entity:%s} FROM {%s AS entity} WHERE {entity:%s}=?%s",
                                ItemModel.PK, typeCode, ItemModel.PK, ItemModel.PK);
                final FlexibleSearchQuery query = new FlexibleSearchQuery(flexibleQuery, Collections.singletonMap(ItemModel.PK, pk));
                final SearchResult<Object> searchResult = getFlexibleSearchService().search(query);
                final List<Object> result = searchResult.getResult();
                if(result.isEmpty())
                {
                    throw new ObjectNotFoundException(String.format("Could not find object denoted by id: %s", id));
                }
                if(result.size() > 1)
                {
                    throw new IllegalStateException(String.format("Given identifier is ambiguous: %s", id));
                }
                return (T)result.get(0);
            }
            else
            {
                if(LOG.isWarnEnabled())
                {
                    LOG.warn(String.format("Current user has insufficient rights to see instances of type: %s", typeCode));
                }
                return null;
            }
        }
        catch(final PK.PKException pke)
        {
            LOG.warn("Could not parse given id as PK", pke);
        }
        return super.loadObject(id);
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return flexibleSearchService;
    }
}
