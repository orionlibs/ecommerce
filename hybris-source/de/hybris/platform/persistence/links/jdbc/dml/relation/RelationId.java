package de.hybris.platform.persistence.links.jdbc.dml.relation;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

class RelationId
{
    private final Long languagePK;
    private final Long parentPK;


    public RelationId(long languagePK, long parentPK)
    {
        this.languagePK = Long.valueOf(languagePK);
        this.parentPK = Long.valueOf(parentPK);
    }


    public long getParentPK()
    {
        return this.parentPK.longValue();
    }


    public long getLanguagePK()
    {
        return this.languagePK.longValue();
    }


    public int hashCode()
    {
        return Objects.hashCode(new Object[] {this.parentPK, this.languagePK});
    }


    public boolean equals(Object obj)
    {
        if(obj == this)
        {
            return true;
        }
        if(obj == null || !(obj instanceof RelationId))
        {
            return false;
        }
        RelationId other = (RelationId)obj;
        return (this.languagePK.equals(other.languagePK) && this.parentPK.equals(other.parentPK));
    }


    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("parentPK", this.parentPK).add("languagePK", this.languagePK).toString();
    }
}
