package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.core.PK;
import de.hybris.platform.util.CSVUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SyncSchedule
{
    public static final char DELIMITER = ',';
    public static final char[] ESCAPE = new char[] {','};
    private final PK srcPK;
    private final PK tgtPK;
    private final PK timestampPK;
    private final Set<String> pendingAttributes;
    private final Map<PK, PK> parentMappings;
    private final boolean deadlockVictim;
    private final int weight;
    private int lineNumber = -1;


    public String toString()
    {
        return "<" + this.srcPK + ((this.timestampPK != null) ? ("-(" + this.timestampPK + ")") : "") + "->" + this.tgtPK + " " + this.pendingAttributes + " deadlock(" + this.deadlockVictim + ")>";
    }


    protected SyncSchedule(PK srcPK, PK tgtPK, PK timestampPK, Collection<String> pendingAttributes, Map<PK, PK> parentMappings)
    {
        this(srcPK, tgtPK, timestampPK, pendingAttributes, parentMappings, false);
    }


    protected SyncSchedule(PK srcPK, PK tgtPK, PK timestampPK, Collection<String> pendingAttributes, Map<PK, PK> parentMappings, boolean deadlockVictim)
    {
        if(timestampPK != null && timestampPK.getTypeCode() != 619)
        {
            throw new IllegalArgumentException("wrong timestamp PK '" + timestampPK + "* (tc:" + timestampPK.getTypeCode() + "<>619)");
        }
        if(srcPK != null && srcPK.getTypeCode() == 619)
        {
            throw new IllegalArgumentException("wrong srcPK '" + srcPK + "* (tc:" + srcPK.getTypeCode() + "==619)");
        }
        if(tgtPK != null && tgtPK.getTypeCode() == 619)
        {
            throw new IllegalArgumentException("wrong tgtPK '" + tgtPK + "* (tc:" + tgtPK.getTypeCode() + "==619)");
        }
        this.srcPK = srcPK;
        this.tgtPK = tgtPK;
        this.timestampPK = timestampPK;
        this.pendingAttributes = (pendingAttributes != null) ? new LinkedHashSet<>(pendingAttributes) : null;
        this.parentMappings = (parentMappings != null) ? new LinkedHashMap<>(parentMappings) : null;
        this.deadlockVictim = deadlockVictim;
        this.weight = calculateWeight();
    }


    protected int calculateWeight()
    {
        int weight = 0;
        if(this.srcPK != null)
        {
            weight += 16;
        }
        if(this.tgtPK != null)
        {
            weight += 8;
        }
        return weight;
    }


    protected SyncSchedule(Map<Integer, String> line)
    {
        String srcPKStr = line.get(Integer.valueOf(0));
        this.srcPK = (srcPKStr != null && srcPKStr.length() > 0) ? PK.parse(srcPKStr) : null;
        String tgtPKStr = line.get(Integer.valueOf(1));
        this.tgtPK = (tgtPKStr != null && tgtPKStr.length() > 0) ? PK.parse(tgtPKStr) : null;
        String tsPKStr = line.get(Integer.valueOf(2));
        this.timestampPK = (tsPKStr != null && tsPKStr.length() > 0) ? PK.parse(tsPKStr) : null;
        if(this.timestampPK != null && this.timestampPK.getTypeCode() != 619)
        {
            throw new IllegalArgumentException("wrong timestamp PK '" + this.timestampPK + "* (tc:" + this.timestampPK.getTypeCode() + "<>619) line:" + line);
        }
        if(this.srcPK != null && this.srcPK.getTypeCode() == 619)
        {
            throw new IllegalArgumentException("wrong srcPK '" + this.srcPK + "* (tc:" + this.srcPK.getTypeCode() + "==619) line:" + line);
        }
        if(this.tgtPK != null && this.tgtPK.getTypeCode() == 619)
        {
            throw new IllegalArgumentException("wrong tgtPK '" + this.tgtPK + "* (tc:" + this.tgtPK.getTypeCode() + "==619) line:" + line);
        }
        String attributesStr = line.get(Integer.valueOf(3));
        this
                        .pendingAttributes = (attributesStr != null) ? new LinkedHashSet<>(CSVUtils.splitAndUnescape(attributesStr, ESCAPE, true)) : null;
        String mappingsStr = line.get(Integer.valueOf(4));
        if(mappingsStr != null && mappingsStr.length() > 0)
        {
            this.parentMappings = new LinkedHashMap<>();
            for(String mapping : CSVUtils.splitAndUnescape(mappingsStr, ESCAPE, true))
            {
                String[] tokens = mapping.split("->");
                this.parentMappings.put(PK.parse(tokens[0]), PK.parse(tokens[1]));
            }
        }
        else
        {
            this.parentMappings = null;
        }
        String deadlockVictimStr = line.get(Integer.valueOf(5));
        this.deadlockVictim = Boolean.valueOf(deadlockVictimStr).booleanValue();
        this.weight = calculateWeight();
    }


    public int getLineNumber()
    {
        if(this.lineNumber == -1)
        {
            throw new IllegalStateException("line number has not been set yet");
        }
        return this.lineNumber;
    }


    protected void setLineNumber(int linenumber)
    {
        this.lineNumber = linenumber;
    }


    public PK getSrcPK()
    {
        return this.srcPK;
    }


    public PK getTgtPK()
    {
        return this.tgtPK;
    }


    public PK getTimestampPK()
    {
        return this.timestampPK;
    }


    public Set<String> getPendingAttributes()
    {
        return (this.pendingAttributes != null) ? this.pendingAttributes : Collections.EMPTY_SET;
    }


    public Map<PK, PK> getParentMappings()
    {
        return (this.parentMappings != null) ? this.parentMappings : Collections.EMPTY_MAP;
    }


    protected Map<Integer, String> toCsv()
    {
        Map<Integer, String> line = new LinkedHashMap<>();
        line.put(Integer.valueOf(0), (getSrcPK() != null) ? getSrcPK().toString() : null);
        line.put(Integer.valueOf(1), (getTgtPK() != null) ? getTgtPK().toString() : null);
        line.put(Integer.valueOf(2), (getTimestampPK() != null) ? getTimestampPK().toString() : null);
        if(!getPendingAttributes().isEmpty())
        {
            line.put(Integer.valueOf(3),
                            CSVUtils.joinAndEscape(new ArrayList<>(getPendingAttributes()), ESCAPE, ',', true));
        }
        if(!getParentMappings().isEmpty())
        {
            String[] mappings = new String[getParentMappings().size()];
            int index = 0;
            for(Map.Entry<PK, PK> e : getParentMappings().entrySet())
            {
                mappings[index] = ((PK)e.getKey()).toString() + "->" + ((PK)e.getKey()).toString();
                index++;
            }
            line.put(Integer.valueOf(4), CSVUtils.joinAndEscape(Arrays.asList(mappings), ESCAPE, ',', true));
        }
        line.put(Integer.valueOf(5), Boolean.valueOf(this.deadlockVictim).toString());
        return line;
    }


    public boolean isDeadlockVictim()
    {
        return this.deadlockVictim;
    }


    public int getWeight()
    {
        return this.weight;
    }
}
