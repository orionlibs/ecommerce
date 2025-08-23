package de.hybris.platform.solrfacetsearch.solr.impl;

import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.solr.IndexNameResolver;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultIndexNameResolver implements IndexNameResolver
{
    private static Pattern forbiddenCharacters = Pattern.compile("[^a-zA-Z0-9_\\\\-]+");
    private String separator;
    private TenantService tenantService;


    public String getSeparator()
    {
        return this.separator;
    }


    @Required
    public void setSeparator(String separator)
    {
        this.separator = separator;
    }


    public TenantService getTenantService()
    {
        return this.tenantService;
    }


    @Required
    public void setTenantService(TenantService tenantService)
    {
        this.tenantService = tenantService;
    }


    public String resolve(FacetSearchConfig facetSearchConfig, IndexedType indexedType, String qualifier)
    {
        StringBuilder result = new StringBuilder();
        result.append(this.tenantService.getCurrentTenantId());
        result.append(this.separator).append(
                        (indexedType.getIndexNameFromConfig() != null) ? indexedType.getIndexNameFromConfig() : facetSearchConfig.getName());
        result.append(this.separator).append((indexedType.getIndexName() != null) ? indexedType.getIndexName() : indexedType.getCode());
        if(StringUtils.isNotBlank(qualifier))
        {
            result.append(this.separator).append(qualifier);
        }
        return forbiddenCharacters.matcher(result.toString()).replaceAll("");
    }
}
