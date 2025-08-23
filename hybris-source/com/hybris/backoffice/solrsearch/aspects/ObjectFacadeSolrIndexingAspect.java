package com.hybris.backoffice.solrsearch.aspects;

import com.hybris.backoffice.search.aspects.AbstractObjectFacadeSearchIndexingAspect;
import com.hybris.backoffice.solrsearch.events.SolrIndexSynchronizationStrategy;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectFacadeSolrIndexingAspect extends AbstractObjectFacadeSearchIndexingAspect
{
    private static final Logger LOG = LoggerFactory.getLogger(ObjectFacadeSolrIndexingAspect.class);
    public static final String CONFIG_BACKOFFICE_SEARCH_REVERSE_CATEGORY_INDEX_LOOKUP_ENABLED = "backoffice.search.reverse.category.index.lookup.enabled";
    public static final String CONFIG_BACKOFFICE_SEARCH_BACKGROUND_SOLR_INDEXING_ENABLED = "backoffice.search.background.solr.indexing.enabled";
    private SolrIndexSynchronizationStrategy solrIndexSynchronizationStrategy;


    protected Map<String, List<PK>> extractModels(JoinPoint joinPoint, Object retVal)
    {
        Map<String, List<PK>> result = extractModelsWithoutArgs(joinPoint, retVal);
        includeRequiredProducts(joinPoint, result);
        return result;
    }


    protected void removeIndexByPk(String typecode, List<PK> pkList)
    {
        getSolrIndexSynchronizationStrategy().removeItems(typecode, pkList);
    }


    protected void updateIndexByPk(String typecode, List<PK> pkList)
    {
        getSolrIndexSynchronizationStrategy().updateItems(typecode, pkList);
    }


    protected void logDebug(Map.Entry<String, List<PK>> entry)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Solr index items removed for typecode: {}, pks: {}", entry.getKey(), ((List)entry
                            .getValue()).stream().map(Objects::toString).collect(Collectors.joining(",")));
        }
    }


    protected void includeRequiredProducts(JoinPoint joinPoint, Map<String, List<PK>> result)
    {
        if(isReverseCategoryIndexLookupEnabled())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Expanding list of objects that require to be updated in the index with all Products contained in the modified Categories.");
            }
            if(result.get("Category") != null)
            {
                Object model = ((joinPoint.getArgs()).length == 0) ? null : joinPoint.getArgs()[0];
                Optional<Set<PK>> productPKs = extractProductsFromModifiedCategories(model);
                if(productPKs.isPresent())
                {
                    List<PK> originalProducts = result.get("Product");
                    Set<PK> extractedPKs = productPKs.get();
                    if(originalProducts != null)
                    {
                        extractedPKs.addAll(originalProducts);
                    }
                    result.put("Product", new ArrayList<>(extractedPKs));
                }
            }
        }
    }


    protected boolean isBackgroundIndexingEnabled()
    {
        return getConfigurationService().getConfiguration().getBoolean("backoffice.search.background.solr.indexing.enabled", false);
    }


    protected boolean isReverseCategoryIndexLookupEnabled()
    {
        return getConfigurationService().getConfiguration()
                        .getBoolean("backoffice.search.reverse.category.index.lookup.enabled", false);
    }


    protected Optional<Set<PK>> extractProductsFromModifiedCategories(Object model)
    {
        Set<PK> productPKs = new HashSet<>();
        if(model instanceof Collection)
        {
            Collection<Object> models = (Collection<Object>)model;
            Objects.requireNonNull(CategoryModel.class);
            models.stream().filter(CategoryModel.class::isInstance)
                            .forEach(cat -> productPKs.addAll(findAllProductsInSubTree((CategoryModel)cat)));
        }
        else if(model instanceof CategoryModel)
        {
            productPKs.addAll(findAllProductsInSubTree((CategoryModel)model));
        }
        return productPKs.isEmpty() ? Optional.<Set<PK>>empty() : Optional.<Set<PK>>of(productPKs);
    }


    protected Set<PK> findAllProductsInSubTree(CategoryModel cat)
    {
        Set<PK> res = new HashSet<>();
        res.addAll(getProductPKsInCategory(cat));
        cat.getAllSubcategories().forEach(subCat -> res.addAll(getProductPKsInCategory(subCat)));
        return res;
    }


    protected Set<PK> getProductPKsInCategory(CategoryModel cat)
    {
        return (Set<PK>)cat.getProducts().stream().map(AbstractItemModel::getPk).collect(Collectors.toSet());
    }


    protected SolrIndexSynchronizationStrategy getSolrIndexSynchronizationStrategy()
    {
        return this.solrIndexSynchronizationStrategy;
    }


    public void setSolrIndexSynchronizationStrategy(SolrIndexSynchronizationStrategy solrIndexSynchronizationStrategy)
    {
        this.solrIndexSynchronizationStrategy = solrIndexSynchronizationStrategy;
    }
}
