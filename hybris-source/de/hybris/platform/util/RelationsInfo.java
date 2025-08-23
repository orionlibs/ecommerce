package de.hybris.platform.util;

import de.hybris.platform.core.PK;
import org.apache.commons.lang3.tuple.Pair;

public class RelationsInfo
{
    final Pair<String, PK> foreignKeyAttrPk;
    final String relationQualifier;
    final String targetItemType;
    final int typeOfCollection;
    final String orderingQualifier;
    final boolean orderingAsc;


    @Deprecated(since = "2105", forRemoval = true)
    public RelationsInfo()
    {
        this(builder());
    }


    private RelationsInfo(RelationsInfoBuilder builder)
    {
        this.targetItemType = builder.targetItemType;
        this.relationQualifier = builder.relationQualifier;
        this.foreignKeyAttrPk = builder.foreignKeyAttrPk;
        this.typeOfCollection = builder.typeOfCollection;
        this.orderingQualifier = builder.orderingQualifier;
        this.orderingAsc = builder.orderingAsc;
    }


    public static RelationsInfoBuilder builder()
    {
        return new RelationsInfoBuilder();
    }


    public Pair<String, PK> getForeignKeyAttrPk()
    {
        return this.foreignKeyAttrPk;
    }


    public String getRelationQualifier()
    {
        return this.relationQualifier;
    }


    public int getTypeOfCollection()
    {
        return this.typeOfCollection;
    }


    public String getTargetItemType()
    {
        return this.targetItemType;
    }


    public String getOrderingQualifier()
    {
        return this.orderingQualifier;
    }


    public boolean isOrderingAsc()
    {
        return this.orderingAsc;
    }
}
