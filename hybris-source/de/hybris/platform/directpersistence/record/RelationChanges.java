package de.hybris.platform.directpersistence.record;

import de.hybris.platform.directpersistence.record.impl.RelationMetaInfo;

public interface RelationChanges
{
    <V> V accept(RelationMetaInfo paramRelationMetaInfo, RelationChangesVisitor<V> paramRelationChangesVisitor);


    void groupOrderInformation();


    RelationMetaInfo getRelationMetaInfo();
}
