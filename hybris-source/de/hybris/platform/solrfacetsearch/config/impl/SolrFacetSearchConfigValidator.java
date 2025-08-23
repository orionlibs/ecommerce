package de.hybris.platform.solrfacetsearch.config.impl;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;
import java.util.Collection;

public class SolrFacetSearchConfigValidator implements ValidateInterceptor
{
    protected static final String DOCUMENT_DEPRECATED_ERROR = "XML configuration document is deprecated and is no longer used (since 4.6 version)";
    protected static final String REQUIRED_MEMBER_ITEMS = "Those member items are required : ";
    private TypeService typeService;


    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof SolrFacetSearchConfigModel)
        {
            SolrFacetSearchConfigModel config = (SolrFacetSearchConfigModel)model;
            boolean hasDocument = (config.getDocument() != null);
            if(hasDocument)
            {
                throw new InterceptorException("XML configuration document is deprecated and is no longer used (since 4.6 version)");
            }
            StringBuilder types = checkMemberItems(config);
            if(types.length() != 0)
            {
                StringBuilder message = new StringBuilder("Those member items are required : ");
                message.append(types);
                throw new InterceptorException(message.toString());
            }
        }
    }


    protected StringBuilder checkMemberItems(SolrFacetSearchConfigModel config)
    {
        Collection<SolrIndexedTypeModel> indexTypes = config.getSolrIndexedTypes();
        SolrServerConfigModel serverConfig = config.getSolrServerConfig();
        SolrIndexConfigModel indexConfig = config.getSolrIndexConfig();
        boolean hasIndexedTypes = (indexTypes != null && !indexTypes.isEmpty());
        boolean hasIndexConfig = (indexConfig != null);
        boolean hasServerConfig = (serverConfig != null);
        char separator = ',';
        int separatorLength = 1;
        StringBuilder types = new StringBuilder();
        if(!hasIndexedTypes)
        {
            types.append(getFieldDescriptor("solrIndexedTypes")).append(',');
        }
        if(!hasIndexConfig)
        {
            types.append(getFieldDescriptor("solrIndexConfig")).append(',');
        }
        if(!hasServerConfig)
        {
            types.append(getFieldDescriptor("solrServerConfig")).append(',');
        }
        if(types.length() > 0)
        {
            types.deleteCharAt(types.length() - 1);
        }
        return types;
    }


    protected String getFieldDescriptor(String qualifier)
    {
        AttributeDescriptorModel descriptor = this.typeService.getAttributeDescriptor(GeneratedSolrfacetsearchConstants.TC.SOLRFACETSEARCHCONFIG, qualifier);
        return descriptor.getName();
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
