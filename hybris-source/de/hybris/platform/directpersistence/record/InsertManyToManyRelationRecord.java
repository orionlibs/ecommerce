package de.hybris.platform.directpersistence.record;

import de.hybris.platform.core.PK;

public interface InsertManyToManyRelationRecord extends RelationRecord
{
    PK getSourcePk();


    PK getTargetPk();


    Integer getSourceToTargetPosition();


    void setSourceToTargetPosition(Integer paramInteger);


    Integer getTargetToSourcePosition();


    void setTargetToSourcePosition(Integer paramInteger);
}
