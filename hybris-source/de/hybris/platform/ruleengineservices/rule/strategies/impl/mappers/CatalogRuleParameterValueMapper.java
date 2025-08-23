package de.hybris.platform.ruleengineservices.rule.strategies.impl.mappers;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.springframework.beans.factory.annotation.Required;

public class CatalogRuleParameterValueMapper implements RuleParameterValueMapper<CatalogModel>
{
    private CatalogService catalogService;


    public String toString(CatalogModel catalog)
    {
        ServicesUtil.validateParameterNotNull(catalog, "Object cannot be null");
        return catalog.getId();
    }


    public CatalogModel fromString(String value)
    {
        ServicesUtil.validateParameterNotNull(value, "String value cannot be null");
        CatalogModel catalog = getCatalogService().getCatalogForId(value);
        if(catalog == null)
        {
            throw new RuleParameterValueMapperException("Cannot find Catalog with the id: " + value);
        }
        return catalog;
    }


    protected CatalogService getCatalogService()
    {
        return this.catalogService;
    }


    @Required
    public void setCatalogService(CatalogService catalogService)
    {
        this.catalogService = catalogService;
    }
}
