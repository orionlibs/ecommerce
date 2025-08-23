package de.hybris.platform.persistence.links.jdbc.dml;

public interface Relation
{
    long getLanguagePK();


    Iterable<RelationModification> getModificationsForInsertion(Iterable<Long> paramIterable, int paramInt);


    Iterable<RelationModification> getModificationsForSetting(Iterable<Long> paramIterable);


    Iterable<RelationModification> getModificationsForRemoval(Iterable<Long> paramIterable);
}
