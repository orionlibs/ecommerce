package de.hybris.platform.adaptivesearch.daos.impl;

import de.hybris.platform.adaptivesearch.daos.AsConfigurationDao;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsConfigurationDao extends AbstractAsGenericDao<AbstractAsConfigurationModel> implements AsConfigurationDao
{
    private TypeService typeService;


    public DefaultAsConfigurationDao()
    {
        super("AbstractAsConfiguration");
    }


    public <T extends AbstractAsConfigurationModel> Optional<T> findConfigurationByUid(Class<T> type, CatalogVersionModel catalogVersion, String uid)
    {
        ComposedTypeModel composedType = this.typeService.getComposedTypeForClass(type);
        StringBuilder query = createQuery(composedType.getCode());
        Map<Object, Object> parameters = new HashMap<>();
        appendWhereClause(query);
        appendClause(query, parameters, "catalogVersion", catalogVersion);
        appendAndClause(query);
        appendClause(query, parameters, "uid", uid);
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), parameters);
        List<T> configurations = getFlexibleSearchService().search(searchQuery).getResult();
        return configurations.isEmpty() ? Optional.<T>empty() : Optional.<T>of(configurations.get(0));
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
