package de.hybris.platform.directpersistence.record.impl;

import com.google.common.base.Objects;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.record.RemoveManyToManyRelationsRecord;
import java.text.MessageFormat;

public class DefaultRemoveManyToManyRelationsRecord extends AbstractRelationRecord implements RemoveManyToManyRelationsRecord
{
    protected final PK pk;


    public DefaultRemoveManyToManyRelationsRecord(PK pk, boolean srcToTgs)
    {
        super(srcToTgs);
        this.pk = pk;
    }


    public PK getPk()
    {
        return this.pk;
    }


    public String toString()
    {
        return MessageFormat.format("DefaultRemoveRelationsRecord: pk({0}), src2tgt({1})", new Object[] {this.pk, Boolean.valueOf(this.srcToTgt)});
    }


    public boolean equals(Object obj)
    {
        if(obj == null || !(obj instanceof DefaultRemoveManyToManyRelationsRecord))
        {
            return false;
        }
        DefaultRemoveManyToManyRelationsRecord other = (DefaultRemoveManyToManyRelationsRecord)obj;
        return (this.srcToTgt == other.srcToTgt && Objects.equal(this.pk, other.pk));
    }


    public int hashCode()
    {
        int pkHash = ((this.pk == null) ? Long.valueOf(0L) : this.pk.getLong()).hashCode();
        int srcToTgsHash = this.srcToTgt ? 1 : 0;
        return pkHash << 1 | srcToTgsHash;
    }
}
