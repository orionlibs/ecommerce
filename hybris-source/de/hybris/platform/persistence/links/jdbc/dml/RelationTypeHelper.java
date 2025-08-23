package de.hybris.platform.persistence.links.jdbc.dml;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.persistence.property.TypeInfoMap;

public class RelationTypeHelper
{
    public static TypeInfoMap getTypeInfoMap(String relationCode)
    {
        PersistenceManager persistenceManager = Registry.getCurrentTenant().getPersistenceManager();
        String rootRelationCode = persistenceManager.isRootRelationType(relationCode) ? relationCode : Link.class.getSimpleName();
        return persistenceManager.getPersistenceInfo(rootRelationCode);
    }
}
