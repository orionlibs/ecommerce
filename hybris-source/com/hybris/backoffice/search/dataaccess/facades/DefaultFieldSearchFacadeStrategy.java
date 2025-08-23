/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.search.dataaccess.facades;

import com.hybris.backoffice.constants.BackofficeConstants;
import com.hybris.backoffice.search.daos.ItemModelSearchDAO;
import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchMode;
import com.hybris.backoffice.widgets.advancedsearch.engine.AdvancedSearchQueryData;
import com.hybris.backoffice.widgets.advancedsearch.engine.PageableWithFullTextDataCallback;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.search.AutosuggestionSupport;
import com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacadeStrategy;
import com.hybris.cockpitng.dataaccess.facades.search.OrderedFieldSearchFacadeStrategy;
import com.hybris.cockpitng.dataaccess.facades.search.impl.FieldSearchFacadeStrategyRegistry;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.search.data.AutosuggestionQueryData;
import com.hybris.cockpitng.widgets.collectionbrowser.CollectionBrowserController;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.util.Config;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public abstract class DefaultFieldSearchFacadeStrategy<T extends ItemModel>
                implements AutosuggestionSupport, OrderedFieldSearchFacadeStrategy<T>
{
    private BackofficeFacetSearchConfigService facetSearchConfigService;
    private CommonI18NService commonI18NService;
    private ItemModelSearchDAO itemModelSearchDAO;
    private int strategyLoadOrder;


    @Override
    public boolean canHandle(final String typeCode)
    {
        return canHandle(typeCode, new DefaultContext());
    }


    private boolean isMatched(final Context context)
    {
        String strategyName = Config.getString(BackofficeConstants.PROPERTY_SEARCH_STRATEGY, "");
        if(context != null)
        {
            final String preferedStrategyName = StringUtils.trim((String)context
                            .getAttribute(FieldSearchFacadeStrategyRegistry.CONTEXT_ATTR_PREFERRED_STRATEGY_NAME));
            if(StringUtils.isNotEmpty(preferedStrategyName))
            {
                strategyName = preferedStrategyName;
            }
        }
        return strategyName.equalsIgnoreCase(getStrategyName());
    }


    @Override
    public boolean canHandle(final String typeCode, final Context context)
    {
        if(!isMatched(context))
        {
            return false;
        }
        if(context != null)
        {
            if(context.getAttribute(FieldSearchFacadeStrategy.CONTEXT_ORIGINAL_QUERY) != null)
            {
                final Object query = context.getAttribute(FieldSearchFacadeStrategy.CONTEXT_ORIGINAL_QUERY);
                if(query instanceof AdvancedSearchQueryData
                                && ((AdvancedSearchQueryData)query).getAdvancedSearchMode() != AdvancedSearchMode.SIMPLE)
                {
                    return false;
                }
            }
            final Object modelPageable = context.getAttribute(CollectionBrowserController.MODEL_PAGEABLE);
            if(modelPageable != null)
            {
                final boolean isPageable = modelPageable instanceof PageableWithFullTextDataCallback
                                && ((PageableWithFullTextDataCallback)modelPageable).getPageable() instanceof AbstractBackofficeSearchPageable;
                return isPageable && facetSearchConfigService.isValidSearchConfiguredForType(typeCode);
            }
        }
        return facetSearchConfigService.isValidSearchConfiguredForType(typeCode);
    }


    @Override
    public boolean isSortable(final DataType type, final String attributeQualifier, final Context context)
    {
        return false;
    }


    @Override
    public Map<String, Collection<String>> getAutosuggestionsForQuery(final AutosuggestionQueryData queryData)
    {
        return getAutosuggestionsForQuery(queryData, new DefaultContext());
    }


    protected List<ItemModel> getFilteredResults(final List<ItemModel> itemModels)
    {
        return itemModels;
    }


    public void setCommonI18NService(final CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public CommonI18NService getCommonI18NService()
    {
        return commonI18NService;
    }


    public void setFacetSearchConfigService(final BackofficeFacetSearchConfigService facetSearchConfigService)
    {
        this.facetSearchConfigService = facetSearchConfigService;
    }


    public BackofficeFacetSearchConfigService getFacetSearchConfigService()
    {
        return facetSearchConfigService;
    }


    public void setItemModelSearchDAO(final ItemModelSearchDAO itemModelSearchDAO)
    {
        this.itemModelSearchDAO = itemModelSearchDAO;
    }


    public ItemModelSearchDAO getItemModelSearchDAO()
    {
        return itemModelSearchDAO;
    }


    @Override
    public int getOrder()
    {
        return strategyLoadOrder;
    }


    public void setOrder(final int order)
    {
        this.strategyLoadOrder = order;
    }
}
