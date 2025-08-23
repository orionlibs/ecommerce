package de.hybris.platform.directpersistence.record.impl;

import com.google.common.collect.ImmutableSet;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.record.EntityRecord;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.log4j.Logger;

public class DeleteRecord extends AbstractEntityRecord
{
    private static final Logger LOG = Logger.getLogger(DeleteRecord.class);
    private Set<String> relationNames;


    public DeleteRecord(PK pk, String type, long version)
    {
        super(pk, type, version);
    }


    public <V> V accept(EntityRecord.EntityRecordVisitor<V> visitor)
    {
        return (V)visitor.visit(this);
    }


    public String toString()
    {
        return MessageFormat.format("DeleteRecord: pk({0}), type({1}), version({2})", new Object[] {getPK(), getType(),
                        Long.toString(getVersion())});
    }


    public void addRelationName(String... attr)
    {
        ensureRelationNamesCreated();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Adding relation attributes '" + Arrays.toString((Object[])attr) + "' to DeleteRecord.");
        }
        Collections.addAll(this.relationNames, attr);
    }


    public Set<String> getRelationNames()
    {
        if(this.relationNames == null)
        {
            return Collections.emptySet();
        }
        return (Set<String>)ImmutableSet.copyOf(this.relationNames);
    }


    private void ensureRelationNamesCreated()
    {
        if(this.relationNames == null)
        {
            this.relationNames = new LinkedHashSet<>();
        }
    }
}
