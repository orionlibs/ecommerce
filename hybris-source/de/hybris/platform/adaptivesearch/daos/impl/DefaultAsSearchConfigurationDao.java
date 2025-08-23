package de.hybris.platform.adaptivesearch.daos.impl;

import de.hybris.platform.adaptivesearch.daos.AsSearchConfigurationDao;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsSearchConfigurationDao extends AbstractAsGenericDao<AbstractAsSearchConfigurationModel> implements AsSearchConfigurationDao
{
    protected static final String BASE_QUERY = "SELECT {pk} FROM {AbstractAsSearchConfiguration} WHERE";
    private TypeService typeService;


    public DefaultAsSearchConfigurationDao()
    {
        super("AbstractAsSearchConfiguration");
    }


    public List<AbstractAsSearchConfigurationModel> findAllSearchConfigurations()
    {
        return find();
    }


    public List<AbstractAsSearchConfigurationModel> findSearchConfigurationsByCatalogVersion(CatalogVersionModel catalogVersion)
    {
        StringBuilder query = new StringBuilder("SELECT {pk} FROM {AbstractAsSearchConfiguration} WHERE");
        Map<String, Object> parameters = new HashMap<>();
        appendClause(query, parameters, "catalogVersion", catalogVersion);
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), parameters);
        return getFlexibleSearchService().search(searchQuery).getResult();
    }


    public Optional<AbstractAsSearchConfigurationModel> findSearchConfigurationByUid(CatalogVersionModel catalogVersion, String uid)
    {
        StringBuilder query = new StringBuilder("SELECT {pk} FROM {AbstractAsSearchConfiguration} WHERE");
        Map<Object, Object> parameters = new HashMap<>();
        appendClause(query, parameters, "catalogVersion", catalogVersion);
        appendAndClause(query);
        appendClause(query, parameters, "uid", uid);
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), parameters);
        List<AbstractAsSearchConfigurationModel> searchConfigurations = getFlexibleSearchService().search(searchQuery).getResult();
        return searchConfigurations.isEmpty() ? Optional.<AbstractAsSearchConfigurationModel>empty() : Optional.<AbstractAsSearchConfigurationModel>of(searchConfigurations.get(0));
    }


    public <T extends AbstractAsSearchConfigurationModel> List<T> findSearchConfigurations(Class<T> type, Map<String, Object> filters)
    {
        String typeCode = this.typeService.getComposedTypeForClass(type).getCode();
        StringBuilder query = new StringBuilder(256);
        query.append("SELECT {");
        query.append("pk");
        query.append("} FROM {");
        query.append(typeCode);
        query.append("} WHERE");
        Map<String, Object> parameters = new HashMap<>();
        boolean firstParam = true;
        for(Map.Entry<String, Object> filter : filters.entrySet())
        {
            if(!firstParam)
            {
                appendAndClause(query);
            }
            appendClause(query, parameters, filter.getKey(), filter.getValue());
            firstParam = false;
        }
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), parameters);
        return getFlexibleSearchService().search(searchQuery).getResult();
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
