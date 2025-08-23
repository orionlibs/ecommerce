package de.hybris.platform.solrfacetsearch.loader.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.loader.ModelLoader;
import de.hybris.platform.solrfacetsearch.loader.ModelLoadingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Required;

public class DefaultModelLoader implements ModelLoader<Object>
{
    private static final String CODE = "code";
    private ModelService modelService;


    public List<Object> loadModels(Collection<SolrDocument> documents) throws ModelLoadingException
    {
        if(documents == null)
        {
            throw new IllegalArgumentException("Collection of SolrDocuments must not be null");
        }
        if(documents.isEmpty())
        {
            return Collections.emptyList();
        }
        List<Object> result = new ArrayList(documents.size());
        for(SolrDocument doc : documents)
        {
            PK pk = getPKFromDocument(doc);
            Object model = this.modelService.get(pk);
            result.add(model);
        }
        return result;
    }


    public List<String> loadCodes(Collection<SolrDocument> documents) throws ModelLoadingException
    {
        if(documents == null)
        {
            throw new IllegalArgumentException("Collection of SolrDocuments must not be null");
        }
        if(documents.isEmpty())
        {
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<>(documents.size());
        for(SolrDocument doc : documents)
        {
            PK pk = getPKFromDocument(doc);
            Object model = this.modelService.get(pk);
            result.add(getModelAttribute("code", model, pk));
        }
        return result;
    }


    protected String getModelAttribute(String attribute, Object model, PK pk) throws ModelLoadingException
    {
        try
        {
            return (String)this.modelService.getAttributeValue(model, attribute);
        }
        catch(IllegalArgumentException e)
        {
            throw new ModelLoadingException("Could not load attribute [code] from  Item [" + pk.toString() + "]", e);
        }
    }


    protected PK getPKFromDocument(SolrDocument doc) throws ModelLoadingException
    {
        Long pk = (Long)doc.getFirstValue("pk");
        if(pk == null)
        {
            throw new ModelLoadingException("SolrDocument does not contain field 'pk'");
        }
        return PK.fromLong(pk.longValue());
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
