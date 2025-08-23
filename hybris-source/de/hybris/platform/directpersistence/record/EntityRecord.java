package de.hybris.platform.directpersistence.record;

import de.hybris.platform.core.PK;

public interface EntityRecord extends Record
{
    <V> V accept(EntityRecordVisitor<V> paramEntityRecordVisitor);


    PK getPK();


    String getType();


    long getVersion();
}
