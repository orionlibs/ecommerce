package de.hybris.platform.servicelayer.internal.model.extractor.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.record.impl.RelationMetaInfo;
import java.text.MessageFormat;
import java.util.Locale;

public class RelationInfo
{
    private final RelationMetaInfo relationMetaInfo;
    private final PK sourcePk;
    private final PK targetPk;
    private final boolean isRemove;
    private final boolean srcToTgt;
    private final boolean clearOnSource;
    private final Locale locale;
    private final Integer position;


    public static Builder builder()
    {
        return new Builder();
    }


    private RelationInfo(Builder builder)
    {
        Preconditions.checkArgument((builder.relationMetaInfo != null), "relationMetaInfo is required");
        this.relationMetaInfo = builder.relationMetaInfo;
        this.sourcePk = builder.sourcePk;
        this.targetPk = builder.targetPk;
        this.isRemove = builder.isRemove;
        this.srcToTgt = builder.srcToTgt;
        this.clearOnSource = builder.clearOnSource;
        this.locale = builder.locale;
        this.position = builder.position;
    }


    public boolean isOneToMany()
    {
        return this.relationMetaInfo.isOneToMany();
    }


    public RelationMetaInfo getRelationMetaInfo()
    {
        return this.relationMetaInfo;
    }


    public PK getSourcePk()
    {
        return this.sourcePk;
    }


    public PK getTargetPk()
    {
        return this.targetPk;
    }


    public boolean isRemove()
    {
        return this.isRemove;
    }


    public Locale getLocale()
    {
        return this.locale;
    }


    public boolean isSrcToTgt()
    {
        return this.srcToTgt;
    }


    public boolean isClearOnSource()
    {
        return this.clearOnSource;
    }


    public boolean isLocalized()
    {
        return (this.locale != null);
    }


    public Integer getPosition()
    {
        return this.position;
    }


    public String toString()
    {
        return MessageFormat.format("RelationInfo: leftPk({0}) rightPk({1}) position({2}) src2tgt({3}) locale({4})", new Object[] {this.sourcePk, this.targetPk,
                        (this.position != null) ? this.position : "?", Boolean.valueOf(this.srcToTgt), this.locale});
    }
}
