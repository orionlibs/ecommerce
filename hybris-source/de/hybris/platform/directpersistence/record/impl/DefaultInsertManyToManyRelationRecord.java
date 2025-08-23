package de.hybris.platform.directpersistence.record.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.record.InsertManyToManyRelationRecord;
import java.text.MessageFormat;

public class DefaultInsertManyToManyRelationRecord extends AbstractRelationRecord implements InsertManyToManyRelationRecord
{
    private final PK sourcePk;
    private final PK targetPk;
    private Integer sourceToTargetPosition;
    private Integer targetToSourcePosition;


    public DefaultInsertManyToManyRelationRecord(PK sourcePk, PK targetPk, boolean src2tgt)
    {
        super(src2tgt);
        this.sourcePk = sourcePk;
        this.targetPk = targetPk;
    }


    public PK getSourcePk()
    {
        return this.sourcePk;
    }


    public PK getTargetPk()
    {
        return this.targetPk;
    }


    public void setSourceToTargetPosition(Integer sourceToTargetPosition)
    {
        this.sourceToTargetPosition = sourceToTargetPosition;
    }


    public Integer getSourceToTargetPosition()
    {
        return this.sourceToTargetPosition;
    }


    public void setTargetToSourcePosition(Integer targetToSourcePosition)
    {
        this.targetToSourcePosition = targetToSourcePosition;
    }


    public Integer getTargetToSourcePosition()
    {
        return this.targetToSourcePosition;
    }


    public int hashCode()
    {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + ((this.sourcePk == null) ? 0 : this.sourcePk.hashCode());
        result = 31 * result + ((this.sourceToTargetPosition == null) ? 0 : this.sourceToTargetPosition.hashCode());
        result = 31 * result + ((this.targetPk == null) ? 0 : this.targetPk.hashCode());
        result = 31 * result + ((this.targetToSourcePosition == null) ? 0 : this.targetToSourcePosition.hashCode());
        return result;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(!super.equals(obj))
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        DefaultInsertManyToManyRelationRecord other = (DefaultInsertManyToManyRelationRecord)obj;
        if(this.sourcePk == null)
        {
            if(other.sourcePk != null)
            {
                return false;
            }
        }
        else if(!this.sourcePk.equals(other.sourcePk))
        {
            return false;
        }
        if(this.sourceToTargetPosition == null)
        {
            if(other.sourceToTargetPosition != null)
            {
                return false;
            }
        }
        else if(!this.sourceToTargetPosition.equals(other.sourceToTargetPosition))
        {
            return false;
        }
        if(this.targetPk == null)
        {
            if(other.targetPk != null)
            {
                return false;
            }
        }
        else if(!this.targetPk.equals(other.targetPk))
        {
            return false;
        }
        if(this.targetToSourcePosition == null)
        {
            if(other.targetToSourcePosition != null)
            {
                return false;
            }
        }
        else if(!this.targetToSourcePosition.equals(other.targetToSourcePosition))
        {
            return false;
        }
        return true;
    }


    public String toString()
    {
        return MessageFormat.format("DefaultInsertRelationRecord: sourcePk({0}), targetPk({1}), srcPos({2}), tgtPos({3}) src2tgt({4})", new Object[] {this.sourcePk, this.targetPk,
                        (this.sourceToTargetPosition != null) ? this.sourceToTargetPosition : "?",
                        (this.targetToSourcePosition != null) ? this.targetToSourcePosition : "?", Boolean.valueOf(this.srcToTgt)});
    }
}
