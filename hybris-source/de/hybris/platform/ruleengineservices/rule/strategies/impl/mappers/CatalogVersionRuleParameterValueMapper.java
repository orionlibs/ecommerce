package de.hybris.platform.ruleengineservices.rule.strategies.impl.mappers;

import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.daos.CatalogVersionDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class CatalogVersionRuleParameterValueMapper implements RuleParameterValueMapper<CatalogVersionModel>
{
    private CatalogVersionDao catalogVersionDao;
    private String delimiter;


    public String toString(CatalogVersionModel catalog)
    {
        ServicesUtil.validateParameterNotNull(catalog, "Object cannot be null");
        return catalog.getCatalog().getId() + catalog.getCatalog().getId() + getDelimiter();
    }


    public CatalogVersionModel fromString(String value)
    {
        ServicesUtil.validateParameterNotNull(value, "String value cannot be null");
        Preconditions.checkArgument(value.contains(getDelimiter()), "Invalid format of the CatalogVersionModel string representation");
        String[] parts = value.split(getDelimiter());
        Collection<CatalogVersionModel> catalogVersions = getCatalogVersionDao().findCatalogVersions(parts[0], parts[1]);
        if(CollectionUtils.isEmpty(catalogVersions))
        {
            throw new RuleParameterValueMapperException("Cannot find Catalog Version with the code: " + value);
        }
        return catalogVersions.iterator().next();
    }


    protected CatalogVersionDao getCatalogVersionDao()
    {
        return this.catalogVersionDao;
    }


    @Required
    public void setCatalogVersionDao(CatalogVersionDao catalogVersionDao)
    {
        this.catalogVersionDao = catalogVersionDao;
    }


    protected String getDelimiter()
    {
        return this.delimiter;
    }


    @Required
    public void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter;
    }
}
