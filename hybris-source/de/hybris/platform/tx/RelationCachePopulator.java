package de.hybris.platform.tx;

import de.hybris.platform.core.PK;
import java.util.List;

public interface RelationCachePopulator
{
    PK createSourceItem();


    PK createTargetItem(PK paramPK);


    List<PK> getTargetSide(PK paramPK);


    boolean deleteItem(PK paramPK);


    boolean deleteTargetSideItems(PK paramPK);


    boolean deleteRelationInstanceNonPersistenceLayer(PK paramPK1, PK paramPK2);


    boolean deleteRelationInstancePersistenceLayer(PK paramPK1, PK paramPK2);
}
