package de.hybris.platform.ruleengineservices.rule.strategies.impl.mappers;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.daos.CategoryDao;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class CategoryRuleParameterValueMapper implements RuleParameterValueMapper<CategoryModel>
{
    private CategoryDao categoryDao;
    private CatalogVersionService catalogVersionService;
    private RuleParameterValueMapper<CatalogModel> catalogRuleParameterValueMapper;
    private String delimiter;
    private String catalogVersionName;


    public String toString(CategoryModel category)
    {
        ServicesUtil.validateParameterNotNull(category, "Object cannot be null");
        return String.join(getDelimiter(), new CharSequence[] {category.getCode(),
                        getCatalogRuleParameterValueMapper().toString(category.getCatalogVersion().getCatalog())});
    }


    public CategoryModel fromString(String value)
    {
        Collection<CategoryModel> categories;
        ServicesUtil.validateParameterNotNull(value, "String value cannot be null");
        String categoryCode = value;
        if(value.contains(getDelimiter()))
        {
            String catalogIdentifier = StringUtils.substringAfter(value, getDelimiter());
            CatalogModel catalog = (CatalogModel)getCatalogRuleParameterValueMapper().fromString(catalogIdentifier);
            CatalogVersionModel catalogVersion = getCatalogVersionService().getCatalogVersion(catalog.getId(), getCatalogVersionName());
            categoryCode = StringUtils.substringBefore(value, getDelimiter());
            categories = getCategoryDao().findCategoriesByCode(catalogVersion, categoryCode);
        }
        else
        {
            categories = getCategoryDao().findCategoriesByCode(value);
        }
        if(CollectionUtils.isEmpty(categories))
        {
            throw new RuleParameterValueMapperException("Cannot find category with the code: " + categoryCode);
        }
        return categories.iterator().next();
    }


    protected CategoryDao getCategoryDao()
    {
        return this.categoryDao;
    }


    @Required
    public void setCategoryDao(CategoryDao categoryDao)
    {
        this.categoryDao = categoryDao;
    }


    protected RuleParameterValueMapper<CatalogModel> getCatalogRuleParameterValueMapper()
    {
        return this.catalogRuleParameterValueMapper;
    }


    @Required
    public void setCatalogRuleParameterValueMapper(RuleParameterValueMapper<CatalogModel> catalogRuleParameterValueMapper)
    {
        this.catalogRuleParameterValueMapper = catalogRuleParameterValueMapper;
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


    protected String getCatalogVersionName()
    {
        return this.catalogVersionName;
    }


    @Required
    public void setCatalogVersionName(String catalogVersionName)
    {
        this.catalogVersionName = catalogVersionName;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }
}
