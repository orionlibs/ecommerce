package de.hybris.platform.persistence.links.jdbc.dml;

public interface RelationModifictionContext
{
    void consume(Iterable<RelationModification> paramIterable);


    void addNewLinkToChild(long paramLong1, long paramLong2, long paramLong3, int paramInt1, int paramInt2);


    @Deprecated(since = "2205", forRemoval = true)
    void shiftExistingLink(long paramLong1, int paramInt, long paramLong2);


    void shiftExistingLink(long paramLong1, int paramInt, long paramLong2, long paramLong3, Long paramLong);


    @Deprecated(since = "2205", forRemoval = true)
    void removeExistingLink(long paramLong1, long paramLong2);


    void removeExistingLink(long paramLong1, long paramLong2, long paramLong3, Long paramLong);


    void touchExistingItem(long paramLong);


    void flush();
}
