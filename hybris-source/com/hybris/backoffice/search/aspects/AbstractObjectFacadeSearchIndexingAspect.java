/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.search.aspects;

import com.hybris.backoffice.cockpitng.dataaccess.facades.object.savedvalues.ItemModificationHistoryService;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeOperationResult;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.hmc.model.SavedValueEntryModel;
import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.threadpool.PoolableThread;
import de.hybris.platform.util.threadpool.ThreadPool;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.aspectj.lang.JoinPoint;
import org.assertj.core.util.Lists;

/**
 * Contains logic of aspect responsible for updating index.<br>
 * Shall be called when Backoffice {@link com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade} performs changes
 * on {@link ItemModel}.
 */
public abstract class AbstractObjectFacadeSearchIndexingAspect
{
    private ModelService modelService;
    private ItemModificationHistoryService itemModificationHistoryService;
    private ConfigurationService configurationService;


    /**
     * Logic to be called when item is being changed via {@link com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade}
     *
     * @param joinPoint
     *           aspect joint point
     * @param retVal
     *           aspect returned value
     */
    public void updateChanged(final JoinPoint joinPoint, final Object retVal)
    {
        final Map<String, List<PK>> pks = extractModels(joinPoint, retVal);
        executeIndexer(() -> {
            for(final Map.Entry<String, List<PK>> entry : pks.entrySet())
            {
                updateIndexByPk(entry.getKey(), entry.getValue());
                logDebug(entry);
            }
        });
    }


    /**
     * Logic to be called when item is being removed via {@link com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade}
     *
     * @param joinPoint
     *           aspect joint point
     * @param retVal
     *           aspect returned value
     */
    public void updateRemoved(final JoinPoint joinPoint, final Object retVal)
    {
        final Map<String, List<PK>> pks = extractModels(joinPoint, retVal);
        executeIndexer(() -> {
            for(final Map.Entry<String, List<PK>> entry : pks.entrySet())
            {
                removeIndexByPk(entry.getKey(), entry.getValue());
                logDebug(entry);
            }
        });
    }


    /**
     * Removes method param item from index
     *
     * @param joinPoint
     *           aspect joint point
     */
    public void updateRemovedForMethodParameter(final JoinPoint joinPoint)
    {
        updateRemoved(joinPoint, joinPoint.getArgs()[0]);
    }


    protected void executeIndexer(final Runnable runnable)
    {
        if(!isBackgroundIndexingEnabled())
        {
            runnable.run();
        }
        else
        {
            findBackgroundThread().execute(runnable);
        }
    }


    protected PoolableThread findBackgroundThread()
    {
        return ThreadPool.getInstance().borrowThread();
    }


    protected boolean isBackgroundIndexingEnabled()
    {
        return false;
    }


    protected abstract void removeIndexByPk(String typecode, List<PK> pkList);


    protected abstract void updateIndexByPk(String typecode, List<PK> pkList);


    protected Map<String, List<PK>> extractModels(final JoinPoint joinPoint, final Object retVal)
    {
        return extractModelsWithoutArgs(joinPoint, retVal);
    }


    protected Map<String, List<PK>> extractModelsWithoutArgs(final JoinPoint joinPoint, final Object retVal)
    {
        final Object model = joinPoint.getArgs().length == 0 ? null : joinPoint.getArgs()[0];
        if(model instanceof Collection)
        {
            final Collection<Object> models = (Collection)model;
            final Set failedModels = findFailedObjects(retVal);
            return models.stream().filter(m -> !failedModels.contains(m)).filter(ItemModel.class::isInstance)
                            .map(ItemModel.class::cast).collect(Collectors.groupingBy(getModelService()::getModelType,
                                            Collectors.mapping(ItemModel::getPk, Collectors.toList())));
        }
        else if(model instanceof ItemModel)
        {
            final List<PK> productPKs = getModifiedProductsPK((ItemModel)model);
            final Map<String, List<PK>> map = new HashMap<>();
            map.put(ProductModel._TYPECODE, productPKs);
            map.put(getModelService().getModelType(model), Collections.singletonList(((ItemModel)model).getPk()));
            return map;
        }
        else
        {
            return Collections.emptyMap();
        }
    }


    protected List<PK> getModifiedProductsPK(final ItemModel model)
    {
        if(model instanceof CategoryModel)
        {
            return getLastModifiedProductsPK((CategoryModel)model);
        }
        return Lists.emptyList();
    }


    protected List<PK> getLastModifiedProductsPK(final CategoryModel model)
    {
        final Optional<SavedValueEntryModel> lastSavedValuesForProducts = getLastSavedValuesForProducts(model);
        if(!lastSavedValuesForProducts.isPresent())
        {
            return Lists.emptyList();
        }
        final Set<ProductModel> newAndOldProducts = new HashSet<>();
        newAndOldProducts.addAll((Collection<ProductModel>)lastSavedValuesForProducts.get().getOldValue());
        newAndOldProducts.addAll((Collection<ProductModel>)lastSavedValuesForProducts.get().getNewValue());
        return newAndOldProducts.stream()
                        .filter(productModel -> productModel.getModifiedtime().after(getCurrentDateMinusOneMinute()))
                        .map(AbstractItemModel::getPk).collect(Collectors.toList());
    }


    protected Optional<SavedValueEntryModel> getLastSavedValuesForProducts(final CategoryModel model)
    {
        final List<SavedValuesModel> savedValues = getItemModificationHistoryService().getSavedValues(model);
        final List<SavedValuesModel> sortedLatestSavedValues = savedValues.stream()
                        .filter(savedValuesModel -> savedValuesModel.getCreationtime() != null
                                        && savedValuesModel.getCreationtime().after(getCurrentDateMinusOneMinute()))
                        .sorted(Comparator.comparing(ItemModel::getCreationtime)).collect(Collectors.toList());
        if(!sortedLatestSavedValues.isEmpty())
        {
            final SavedValuesModel latest = sortedLatestSavedValues.get(sortedLatestSavedValues.size() - 1);
            return latest.getSavedValuesEntries().stream()
                            .filter(savedValueEntryModel -> savedValueEntryModel.getModifiedAttribute().equals(CategoryModel.PRODUCTS))
                            .findFirst();
        }
        return Optional.empty();
    }


    protected Date getCurrentDateMinusOneMinute()
    {
        final LocalDateTime localDateTimeMinusOneMinute = LocalDateTime.now().minusMinutes(1);
        return Date.from(localDateTimeMinusOneMinute.atZone(ZoneId.systemDefault()).toInstant());
    }


    private Set findFailedObjects(final Object retVal)
    {
        final Set failedObjects;
        if(retVal instanceof ObjectFacadeOperationResult)
        {
            failedObjects = ((ObjectFacadeOperationResult)retVal).getFailedObjects();
        }
        else
        {
            failedObjects = Collections.emptySet();
        }
        return failedObjects;
    }


    protected abstract void logDebug(final Map.Entry<String, List<PK>> entry);


    protected ModelService getModelService()
    {
        return modelService;
    }


    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ItemModificationHistoryService getItemModificationHistoryService()
    {
        return itemModificationHistoryService;
    }


    public void setItemModificationHistoryService(final ItemModificationHistoryService itemModificationHistoryService)
    {
        this.itemModificationHistoryService = itemModificationHistoryService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return configurationService;
    }


    public void setConfigurationService(final ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
