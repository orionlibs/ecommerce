package de.hybris.platform.personalizationservices.customization.dao.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationservices.customization.dao.CxCustomizationGroupDao;
import de.hybris.platform.personalizationservices.dao.impl.AbstractCxDao;
import de.hybris.platform.personalizationservices.model.CxCustomizationsGroupModel;
import java.util.Collections;
import java.util.Optional;

public class DefaultCxCustomizationGroupDao extends AbstractCxDao<CxCustomizationsGroupModel> implements CxCustomizationGroupDao
{
    private static final String DEFAULT_CUSTOMIZATION_GROUP_QUERY = "SELECT {pk} FROM {CxCustomizationsGroup} WHERE {catalogVersion} =?catalogVersion";


    public DefaultCxCustomizationGroupDao()
    {
        super("CxCustomizationsGroup");
    }


    public CxCustomizationsGroupModel getDefaultGroup(CatalogVersionModel catalogVersion)
    {
        Optional<CxCustomizationsGroupModel> result = querySingle("SELECT {pk} FROM {CxCustomizationsGroup} WHERE {catalogVersion} =?catalogVersion",
                        Collections.singletonMap("catalogVersion", catalogVersion));
        return result.orElse(null);
    }


    public boolean isDefaultGroup(CatalogVersionModel catalogVersion)
    {
        Optional<CxCustomizationsGroupModel> result = querySingle("SELECT {pk} FROM {CxCustomizationsGroup} WHERE {catalogVersion} =?catalogVersion",
                        Collections.singletonMap("catalogVersion", catalogVersion));
        return result.isPresent();
    }
}
