package de.hybris.platform.directpersistence.read;

import de.hybris.platform.core.PK;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Objects;

public class RelationInformation
{
    private final boolean isOneToMany;
    private final boolean isOneSide;
    private final PK languagePK;
    private final PK pk;
    private final boolean isSource;
    private final String qualifier;
    private final boolean isSorted;
    private final boolean isSetExpexted;
    private final String targetItemType;
    private final boolean isPartOf;
    private final String foreignKeyAttr;
    private final String orderNumberAttr;
    private final OneToManyHandler<?> oneToManyHandler;


    private RelationInformation(Builder builder)
    {
        this.isOneToMany = builder.isOneToMany;
        this.isOneSide = builder.isOneSide;
        this.languagePK = builder.languagePK;
        this.pk = builder.pk;
        this.isSource = builder.isSource;
        this.qualifier = builder.qualifier;
        this.isSorted = builder.isSorted;
        this.isSetExpexted = builder.isSetExpected;
        this.targetItemType = builder.targetItemType;
        this.isPartOf = builder.isPartOf;
        this.foreignKeyAttr = builder.foreignKeyAttr;
        this.orderNumberAttr = builder.orderNumberAttr;
        this.oneToManyHandler = builder.oneToManyHandler;
    }


    public String getTargetItemType()
    {
        return this.targetItemType;
    }


    public boolean isPartOf()
    {
        return this.isPartOf;
    }


    public String getForeignKeyAttr()
    {
        return this.foreignKeyAttr;
    }


    public String getOrderNumberAttr()
    {
        return this.orderNumberAttr;
    }


    public boolean isOneToMany()
    {
        return this.isOneToMany;
    }


    public boolean isOneSide()
    {
        return this.isOneSide;
    }


    public boolean isLocalized()
    {
        return (this.languagePK != null);
    }


    public PK gtPK()
    {
        return this.pk;
    }


    public boolean isSource()
    {
        return this.isSource;
    }


    public PK getLanguagePK()
    {
        return this.languagePK;
    }


    public String getRelationQualifier()
    {
        return this.qualifier;
    }


    public boolean isSorted()
    {
        return this.isSorted;
    }


    public boolean isSetExpexted()
    {
        return this.isSetExpexted;
    }


    public OneToManyHandler<?> getOneToManyHandler()
    {
        return this.oneToManyHandler;
    }


    public static Builder builder(PK pk, String qualifier, boolean isSource)
    {
        Objects.requireNonNull(pk, "pk mustn't be null");
        Objects.requireNonNull(qualifier, "qualifier mustn't be null");
        return new Builder(pk, qualifier, isSource);
    }
}
