package de.hybris.platform.persistence.links;

import de.hybris.platform.jalo.type.RelationType;
import java.util.Collection;

public interface NonNavigableRelationsDAO
{
    Collection<String> getNonNavigableRelationCodesForType(String paramString);


    Collection<RelationType> getNonNavigableRelationsForTypeCode(String paramString);
}
