package de.hybris.platform.impex.jalo.imp;

import com.google.common.base.Joiner;
import de.hybris.platform.core.PK;
import de.hybris.platform.impex.jalo.AbstractCodeLine;
import de.hybris.platform.impex.jalo.ImpExReader;
import de.hybris.platform.impex.jalo.header.AbstractColumnDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.util.CSVMap;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

public class ValueLine
{
    private static final Joiner COMMA_JOINER = Joiner.on("| ").skipNulls();
    private String ownTypeCode = null;
    private ComposedType myType = null;
    private PK processedItemPK = null;
    private PK conflictingItemPK = null;
    private final CSVMap<ValueEntry> values;
    private final Map<Integer, String> src;
    private final HeaderDescriptor header;
    private final String location;
    private final int linenumber;
    private boolean unresolved = false;
    private String unresolvedReason = null;
    private Collection<ValueLine> hiddenLines = null;
    private boolean unrecoverable = false;
    private AbstractCodeLine afterEachCode = null;


    public ValueLine(HeaderDescriptor header, String firstCell, Map<Integer, String> cellValueStrings, int linenumber, String location)
    {
        ValueLineMetaData metaData = ValueLineMetaData.toMetaData(firstCell);
        this.ownTypeCode = metaData.getTypeCode();
        this.processedItemPK = metaData.getProcessedItemPK();
        this.unrecoverable = metaData.isUnrecoverable();
        this.unresolvedReason = metaData.getErrorMessage();
        this.conflictingItemPK = metaData.getConflictingItemPk();
        this.src = Collections.unmodifiableMap((Map<? extends Integer, ? extends String>)new CSVMap(cellValueStrings));
        this.values = new CSVMap();
        for(Map.Entry<Integer, String> e : cellValueStrings.entrySet())
        {
            if(!ImpExReader.FIRST.equals(e.getKey()))
            {
                this.values.put(e.getKey(), new ValueEntry(((Integer)e.getKey()).intValue(), e.getValue()));
            }
        }
        this.header = header;
        this.linenumber = linenumber;
        this.location = location;
    }


    private ValueLine(Map<Integer, String> src, CSVMap<ValueEntry> valueEntries, HeaderDescriptor header, int lineNumber, String location)
    {
        this.header = header;
        this.src = new HashMap<>(src);
        this.values = new CSVMap();
        for(Map.Entry<Integer, ValueEntry> e : (Iterable<Map.Entry<Integer, ValueEntry>>)valueEntries.entrySet())
        {
            this.values.put(e.getKey(), ((ValueEntry)e.getValue()).createCopy());
        }
        this.linenumber = lineNumber;
        this.location = location;
    }


    public ValueLine createCopy()
    {
        ValueLine ret = new ValueLine(this.src, this.values, this.header, this.linenumber, this.location);
        ret.ownTypeCode = this.ownTypeCode;
        ret.afterEachCode = this.afterEachCode;
        return ret;
    }


    public Collection<ValueLine> createCopies(int count)
    {
        Collection<ValueLine> ret = new ArrayList<>(count);
        for(int i = 0; i < count; i++)
        {
            ret.add(createCopy());
        }
        return ret;
    }


    public Map<Integer, String> getSource()
    {
        return this.src;
    }


    public void addHiddenLine(ValueLine line)
    {
        if(this.hiddenLines == null)
        {
            this.hiddenLines = new LinkedList<>();
        }
        this.hiddenLines.add(line);
    }


    public boolean hasHiddenLines()
    {
        return (this.hiddenLines != null && !this.hiddenLines.isEmpty());
    }


    public Collection<ValueLine> getHiddenLines()
    {
        return (this.hiddenLines != null) ? this.hiddenLines : Collections.EMPTY_LIST;
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected String[] splitTypeCodeCell(String typeCodeCell)
    {
        ValueLineMetaData metaData = ValueLineMetaData.toMetaData(typeCodeCell);
        String[] result = new String[5];
        result[0] = metaData.getTypeCode();
        result[1] = (metaData.getProcessedItemPK() == null) ? null : metaData.getProcessedItemPK().getLongValueAsString();
        result[2] = metaData.isUnrecoverable() ? "true" : null;
        result[3] = (metaData.getConflictingItemPk() == null) ? null : metaData.getProcessedItemPK().getLongValueAsString();
        result[4] = metaData.getErrorMessage();
        return result;
    }


    public void markUnresolved()
    {
        this.unresolved = true;
    }


    public void markUnresolved(String reason)
    {
        this.unresolved = true;
        if(StringUtils.isEmpty(this.unresolvedReason))
        {
            this.unresolvedReason = reason;
        }
        else if(StringUtils.isNotBlank(reason) && !this.unresolvedReason.contains(reason))
        {
            this.unresolvedReason = this.unresolvedReason + ", " + this.unresolvedReason;
        }
    }


    public int getLineNumber()
    {
        return this.linenumber;
    }


    public String getLocation()
    {
        return this.location;
    }


    public String getTypeCode()
    {
        return this.ownTypeCode;
    }


    public void setTypeCode(String typeCode)
    {
        this.ownTypeCode = typeCode;
    }


    public ComposedType getComposedType() throws InsufficientDataException
    {
        if(this.myType == null)
        {
            if(getTypeCode() != null && getTypeCode().length() > 0)
            {
                try
                {
                    this.myType = TypeManager.getInstance().getComposedType(getTypeCode());
                }
                catch(JaloItemNotFoundException e)
                {
                    throw new InsufficientDataException(this, "unknown type '" +
                                    getTypeCode() + "' in line " + this + " - cannot import", 4);
                }
            }
            else
            {
                this.myType = getHeader().getDefaultComposedType();
            }
        }
        return this.myType;
    }


    public void setComposedType(ComposedType type)
    {
        this.myType = type;
        if(type != null)
        {
            this.ownTypeCode = type.getCode();
        }
    }


    public ValueEntry getValueEntry(int index)
    {
        return (ValueEntry)this.values.getCellValue(index);
    }


    public HeaderDescriptor getHeader()
    {
        return this.header;
    }


    public PK getProcessedItemPK()
    {
        return this.processedItemPK;
    }


    public Map<Integer, String> dump()
    {
        Map<Integer, String> ret = new HashMap<>();
        ValueLineMetaData valueLineMetaData = getMetaData();
        ret.put(ImpExReader.FIRST, valueLineMetaData.dump());
        this.values.entrySet().stream().forEach(e -> ((ValueEntry)e.getValue()).dump(ret, (Integer)e.getKey(), (this.unresolved || this.processedItemPK == null)));
        return ret;
    }


    public ValueLineMetaData getMetaData()
    {
        return ValueLineMetaData.builder()
                        .withTypeCode(getTypeCode())
                        .withProcessedItemPK(this.processedItemPK)
                        .withUnrecoverable(this.unrecoverable ? Boolean.TRUE : null)
                        .withConflictingItemPk(this.conflictingItemPK)
                        .withErrorMessage(dumpFullUnresolvedReason())
                        .build();
    }


    private String dumpFullUnresolvedReason()
    {
        List<String> messages = new ArrayList<>();
        if(StringUtils.isNotEmpty(this.unresolvedReason))
        {
            messages.add(this.unresolvedReason);
        }
        String valuesUnresolvedReasons = dumpValuesUnresolvedReasons();
        if(StringUtils.isNotEmpty(valuesUnresolvedReasons))
        {
            messages.add(valuesUnresolvedReasons);
        }
        return (messages.size() > 0) ? COMMA_JOINER.join(messages) : "";
    }


    private String dumpValuesUnresolvedReasons()
    {
        return this.values.entrySet().stream()
                        .filter(e -> (e.getValue() != null && ((ValueEntry)e.getValue()).hasUnresolvedReason(this.unresolvedReason)))
                        .map(e -> ((ValueEntry)e.getValue()).getUnresolvedReason())
                        .collect(Collectors.joining(";"));
    }


    public boolean isUnresolved()
    {
        if(this.unresolved)
        {
            return true;
        }
        for(ValueEntry ve : this.values.values())
        {
            if(ve != null && !ve.isUnused() && ve.isUnresolved())
            {
                return true;
            }
        }
        return false;
    }


    public boolean isUnresolved(Collection<StandardColumnDescriptor> columnDescriptors)
    {
        for(StandardColumnDescriptor cd : columnDescriptors)
        {
            if(cd.isVirtual())
            {
                continue;
            }
            ValueEntry valueEntry = getValueEntry(cd.getValuePosition());
            if(valueEntry == null)
            {
                throw new IllegalStateException("line: " +
                                getLineNumber() + ",  couldn't resolve value at position:  " + cd.getValuePosition());
            }
            if(valueEntry.isUnresolved())
            {
                return true;
            }
        }
        return false;
    }


    public Collection<StandardColumnDescriptor> getUnresolved(Collection<StandardColumnDescriptor> columnDescriptors)
    {
        Collection<StandardColumnDescriptor> ret = null;
        for(StandardColumnDescriptor cd : columnDescriptors)
        {
            if(cd.isVirtual())
            {
                continue;
            }
            ValueEntry valueEntry = getValueEntry(cd.getValuePosition());
            if(valueEntry == null)
            {
                throw new IllegalStateException("line: " +
                                getLineNumber() + ",  couldn't resolve value at position:  " + cd.getValuePosition());
            }
            if(valueEntry.isUnresolved())
            {
                if(ret == null)
                {
                    ret = new ArrayList<>(columnDescriptors.size());
                }
                ret.add(cd);
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }


    public void resolve(Item processedItem, Collection<StandardColumnDescriptor> processedColumns)
    {
        resolve((processedItem != null) ? processedItem.getPK() : null, processedColumns);
    }


    public void resolve(PK processedItemPk, Collection<StandardColumnDescriptor> processedColumns)
    {
        if(this.unresolved)
        {
            throw new IllegalStateException("cannot resolve unresolved line");
        }
        this.processedItemPK = (processedItemPk != null) ? processedItemPk : null;
        Collection<ValueEntry> unused = new ArrayList<>(this.values.values());
        for(StandardColumnDescriptor cd : processedColumns)
        {
            if(cd.isVirtual())
            {
                continue;
            }
            ValueEntry valueEntry = getValueEntry(cd.getValuePosition());
            if(valueEntry != null)
            {
                unused.remove(valueEntry);
            }
        }
        for(ValueEntry ve : unused)
        {
            if(!ve.isManuallyMarkedUnresolved())
            {
                ve.markUnused();
            }
        }
    }


    protected ValueEntry resolveMissingEntry(int index, Object resolvingValue)
    {
        ValueEntry valueEntry = new ValueEntry(index, "");
        this.values.putCellValue(index, valueEntry);
        valueEntry.resolve(resolvingValue);
        return valueEntry;
    }


    protected ValueEntry unresolveMissingEntry(int index)
    {
        ValueEntry valueEntry = new ValueEntry(index, "");
        this.values.putCellValue(index, valueEntry);
        valueEntry.markUnresolved();
        return valueEntry;
    }


    public void markUnrecoverable()
    {
        this.unrecoverable = true;
    }


    public void markUnrecoverable(String reason)
    {
        this.unrecoverable = true;
        this.unresolvedReason = reason;
    }


    public boolean isUnrecoverable()
    {
        return this.unrecoverable;
    }


    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ValueLine[");
        if(this.unresolved)
        {
            stringBuilder.append("unresolvable:").append(this.unresolvedReason);
        }
        stringBuilder.append(",").append(getLocation());
        stringBuilder.append(",").append(getTypeCode());
        stringBuilder.append(",").append(getHeader());
        stringBuilder.append(",").append(this.values);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }


    public String getUnresolvedReason()
    {
        return dumpFullUnresolvedReason();
    }


    public String getPlainUnresolvedReason()
    {
        return this.unresolvedReason;
    }


    public void setConflictingItemPK(PK conflictingItemPK)
    {
        this.conflictingItemPK = conflictingItemPK;
    }


    public PK getConflictingItemPk()
    {
        return this.conflictingItemPK;
    }


    public boolean isUsingJaloOnlyFeatures()
    {
        if(this.header != null)
        {
            if("true".equalsIgnoreCase(this.header.getDescriptorData().getModifier("impex.legacy.mode")))
            {
                return true;
            }
            for(AbstractColumnDescriptor col : this.header.getColumns())
            {
                if(!(col instanceof StandardColumnDescriptor))
                {
                    continue;
                }
                StandardColumnDescriptor columnDescritor = (StandardColumnDescriptor)col;
                if(columnDescritor.isAllowNull() || columnDescritor.isForceWrite())
                {
                    return true;
                }
            }
        }
        return false;
    }


    public void clearProcessedItemPK()
    {
        this.processedItemPK = null;
    }


    public AbstractCodeLine getAfterEachCode()
    {
        return this.afterEachCode;
    }


    public void setAfterEachCode(AbstractCodeLine abstractCodeLine)
    {
        this.afterEachCode = abstractCodeLine;
    }
}
