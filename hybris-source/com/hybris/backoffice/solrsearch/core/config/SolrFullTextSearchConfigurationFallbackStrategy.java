package com.hybris.backoffice.solrsearch.core.config;

import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.cockpitng.config.fulltextsearch.DefaultFullTextSearchConfigurationFallbackStrategy;
import com.hybris.cockpitng.config.fulltextsearch.jaxb.FieldListType;
import com.hybris.cockpitng.config.fulltextsearch.jaxb.FieldType;
import com.hybris.cockpitng.config.fulltextsearch.jaxb.FulltextSearch;
import com.hybris.cockpitng.core.config.ConfigContext;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class SolrFullTextSearchConfigurationFallbackStrategy extends DefaultFullTextSearchConfigurationFallbackStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(SolrFullTextSearchConfigurationFallbackStrategy.class);
    private BackofficeFacetSearchConfigService facetSearchConfigService;


    public FulltextSearch loadFallbackConfiguration(ConfigContext context, Class<FulltextSearch> configurationType)
    {
        AtomicReference<FulltextSearch> result = new AtomicReference<>();
        String contextAttribute = context.getAttribute("strategy");
        if(StringUtils.isBlank(contextAttribute) ||
                        StringUtils.equals("solr", contextAttribute))
        {
            try
            {
                String typeCode = getTypeFromContext(context);
                FacetSearchConfig searchConfig = (FacetSearchConfig)getFacetSearchConfigService().getFacetSearchConfig(typeCode);
                if(searchConfig != null)
                {
                    searchConfig.getIndexConfig().getIndexedTypes().values().stream()
                                    .filter(type -> StringUtils.equals(typeCode, type.getCode())).findFirst()
                                    .ifPresent(indexedType -> result.set(resolveFulltextSearch(indexedType)));
                }
            }
            catch(IllegalArgumentException e)
            {
                LOG.error(e.getLocalizedMessage(), e);
            }
        }
        if(result.get() == null)
        {
            result.set(super.loadFallbackConfiguration(context, configurationType));
        }
        LOG.debug("Solr fallback for {} has been called", FulltextSearch.class);
        return result.get();
    }


    protected FulltextSearch resolveFulltextSearch(IndexedType indexedType)
    {
        if(indexedType != null && MapUtils.isNotEmpty(indexedType.getIndexedProperties()))
        {
            FulltextSearch result = new FulltextSearch();
            result.setFieldList(new FieldListType());
            Objects.requireNonNull(result.getFieldList().getField());
            indexedType.getIndexedProperties().values().stream().map(attr -> {
                FieldType fieldType = new FieldType();
                fieldType.setName(attr.getName());
                fieldType.setDisplayName(attr.getDisplayName());
                return fieldType;
            }).forEach(result.getFieldList().getField()::add);
            return result;
        }
        return null;
    }


    protected BackofficeFacetSearchConfigService getFacetSearchConfigService()
    {
        return this.facetSearchConfigService;
    }


    @Deprecated(since = "2011")
    @Required
    public void setFacetSearchConfigService(BackofficeFacetSearchConfigService facetSearchConfigService)
    {
        this.facetSearchConfigService = facetSearchConfigService;
    }
}
