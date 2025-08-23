package de.hybris.platform.solrfacetsearch.common;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractYSolrService
{
    protected FacetSearchConfigService facetSearchConfigService;
    protected FieldNameProvider solrFieldNameProvider;
    private static final Logger LOG = Logger.getLogger(AbstractYSolrService.class);


    protected void filterQualifyingIndexProperties(Collection<String> fields, LanguageModel language, SolrIndexedPropertyModel prop)
    {
        if(checkIfIndexPropertyQualifies(prop))
        {
            try
            {
                fields.add(resolveIndexedPropertyFieldName(prop, language));
            }
            catch(FacetConfigServiceException e)
            {
                LOG.error("Could not resolve indexed field name for property [" + prop.getName() + "], language[" + language
                                .getIsocode() + "]", (Throwable)e);
            }
        }
    }


    protected abstract boolean checkIfIndexPropertyQualifies(SolrIndexedPropertyModel paramSolrIndexedPropertyModel);


    protected String resolveIndexedPropertyFieldName(SolrIndexedPropertyModel indexedProperty, LanguageModel language) throws FacetConfigServiceException
    {
        return this.solrFieldNameProvider.getFieldName(indexedProperty, indexedProperty.isLocalized() ? language.getIsocode() : null, FieldNameProvider.FieldType.INDEX);
    }


    @Required
    public void setFacetSearchConfigService(FacetSearchConfigService facetSearchConfigService)
    {
        this.facetSearchConfigService = facetSearchConfigService;
    }


    @Required
    public void setSolrFieldNameProvider(FieldNameProvider solrFieldNameProvider)
    {
        this.solrFieldNameProvider = solrFieldNameProvider;
    }
}
