package de.hybris.platform.persistence.property;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.util.Utilities;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class EJBPropertyRowCache extends AbstractPropertyAccess implements LocalizedPropertyAccess, Cloneable
{
    private PK itemPK;
    private final PK langPK;
    private String[] names;
    private Object[] values;
    private final BitSet changeList;
    private boolean hasChangedFlag;
    private boolean inDB;


    public static EJBPropertyRowCache createLocalized(PK langPK, long timestamp, List<String> names)
    {
        return new EJBPropertyRowCache(langPK, timestamp, names.<String>toArray(new String[names.size()]), null, false, false, new BitSet(names
                        .size()));
    }


    public static EJBPropertyRowCache create(long timestamp, List<String> names)
    {
        return new EJBPropertyRowCache(PK.NULL_PK, timestamp, names.<String>toArray(new String[names.size()]), null, false, false, new BitSet(names
                        .size()));
    }


    public static EJBPropertyRowCache loadLocalized(PK langPK, long timestamp, List<String> names, List<Object> values)
    {
        return new EJBPropertyRowCache(langPK, timestamp, names.<String>toArray(new String[names.size()]), values
                        .toArray(
                                        new Object[values.size()]), false, true, new BitSet(names.size()));
    }


    public static EJBPropertyRowCache load(long timestamp, List<String> names, List<Object> values)
    {
        return new EJBPropertyRowCache(PK.NULL_PK, timestamp, names.<String>toArray(new String[names.size()]), values
                        .toArray(new Object[values.size()]), false, true, new BitSet(names.size()));
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder("EJBPropertyRowCache[");
        sb.append("itemPK:").append(this.itemPK).append(",");
        sb.append("langPK:").append(this.langPK).append(",");
        sb.append("inDB:").append(this.inDB).append(",");
        sb.append("hasChanged:").append(this.hasChangedFlag).append(",");
        sb.append("names:").append(Arrays.toString((Object[])this.names)).append(",");
        sb.append("values:").append(Arrays.toString(this.values)).append(",");
        sb.append("changeList:").append(this.changeList);
        sb.append("]:").append(System.identityHashCode(this));
        return sb.toString();
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        EJBPropertyRowCache that = (EJBPropertyRowCache)o;
        return (this.hasChangedFlag == that.hasChangedFlag && this.inDB == that.inDB &&
                        Objects.equals(this.itemPK, that.itemPK) &&
                        Objects.equals(this.langPK, that.langPK) &&
                        Arrays.equals((Object[])this.names, (Object[])that.names) &&
                        Arrays.equals(this.values, that.values) &&
                        Objects.equals(this.changeList, that.changeList));
    }


    public int hashCode()
    {
        int result = Objects.hash(new Object[] {this.itemPK, this.langPK, this.changeList, Boolean.valueOf(this.hasChangedFlag), Boolean.valueOf(this.inDB)});
        result = 31 * result + Arrays.hashCode((Object[])this.names);
        result = 31 * result + Arrays.hashCode(this.values);
        return result;
    }


    private EJBPropertyRowCache(PK langPK, long timestamp, String[] names, Object[] values, boolean changed, boolean inDB, BitSet changeList)
    {
        super(timestamp);
        this.langPK = langPK;
        this.names = names;
        if(values != null)
        {
            this.values = new Object[values.length];
            System.arraycopy(values, 0, this.values, 0, values.length);
        }
        else
        {
            this.values = new Object[names.length];
        }
        this.hasChangedFlag = changed;
        this.inDB = inDB;
        this.changeList = changeList;
    }


    public PK getItemPK()
    {
        return this.itemPK;
    }


    public void setItemPK(PK itemPK)
    {
        this.itemPK = itemPK;
    }


    public synchronized void changeColumns(List<String> newNames)
    {
        if(!newNames.equals(Arrays.asList(this.names)))
        {
            int s = this.names.length;
            Map<String, Object[]> oldDataMap = (Map)new HashMap<>(s);
            for(int i = 0; i < s; i++)
            {
                oldDataMap.put(this.names[i], new Object[] {this.values[i],
                                hasChanged(i) ? Boolean.TRUE : Boolean.FALSE});
            }
            Set<String> merged = new HashSet<>(newNames);
            for(String n : this.names)
            {
                merged.add(n);
            }
            this.names = merged.<String>toArray(new String[merged.size()]);
            Arrays.sort((Object[])this.names);
            int l = this.names.length;
            this.values = new Object[l];
            for(int j = 0; j < l; j++)
            {
                String name = this.names[j];
                Object[] data = oldDataMap.get(name);
                this.values[j] = (data != null) ? data[0] : null;
                if(data != null && ((Boolean)data[1]).booleanValue())
                {
                    this.changeList.set(j);
                }
                else
                {
                    this.changeList.clear(j);
                }
            }
        }
    }


    public int getColumnCount()
    {
        return this.names.length;
    }


    public String getName(int index)
    {
        return this.names[index];
    }


    public Object getValue(int index)
    {
        return this.values[index];
    }


    public boolean isLocalized()
    {
        return (this.langPK != null && !PK.NULL_PK.equals(this.langPK));
    }


    public PK getLangPK()
    {
        return this.langPK;
    }


    public boolean hasChanged()
    {
        return this.hasChangedFlag;
    }


    public boolean isInDatabase()
    {
        return this.inDB;
    }


    public boolean hasChanged(int index)
    {
        return this.changeList.get(index);
    }


    public BitSet getChangeSet()
    {
        return this.changeList;
    }


    private void markModified(int index)
    {
        this.hasChangedFlag = true;
        this.changeList.set(index);
        firePropertyDataChanged();
    }


    public void wroteChanges(boolean success)
    {
        this.hasChangedFlag = false;
        this.inDB = (success || this.inDB);
        this.changeList.clear();
    }


    private int getIndex(String name)
    {
        int s = this.names.length;
        for(int i = 0, j = s - 1; i < s && i <= j; i++, j--)
        {
            if(name.equalsIgnoreCase(this.names[i]))
            {
                return i;
            }
            if(i != j && name.equalsIgnoreCase(this.names[j]))
            {
                return j;
            }
        }
        return -1;
    }


    private Object setPropertyInternal(String name, Object value)
    {
        int idx = getIndex(name);
        if(idx < 0)
        {
            throw new EJBInternalException(null, "property name '" + name + "' was not found in row cache " +
                            Arrays.toString(this.names), 0);
        }
        Object old = this.values[idx];
        if(old != value && (value == null || (!value.equals(old) && !bothValuesBigDecimalAndEqual(value, old))))
        {
            this.values[idx] = Utilities.copyViaSerializationIfNecessary(value);
            markModified(idx);
            return old;
        }
        return value;
    }


    private boolean bothValuesBigDecimalAndEqual(Object value, Object old)
    {
        return (value instanceof BigDecimal && old instanceof BigDecimal && ((BigDecimal)value).compareTo((BigDecimal)old) == 0);
    }


    private Object getPropertyInternal(String name)
    {
        int idx = getIndex(name);
        if(idx < 0)
        {
            throw new EJBInternalException(null, "property name '" + name + "' was not found in row cache ( searched " + name + " in list " +
                            Arrays.toString(this.names) + ")", 0);
        }
        return Utilities.copyViaSerializationIfNecessary(this.values[idx]);
    }


    public Map<String, Object> getAllProperties()
    {
        if(isLocalized())
        {
            return Collections.EMPTY_MAP;
        }
        return getAllPropertiesInternal();
    }


    protected Map<String, Object> getAllPropertiesInternal()
    {
        if(this.names.length == 0)
        {
            return Collections.EMPTY_MAP;
        }
        int s = this.names.length;
        Map<String, Object> all = new HashMap<>(s);
        for(int i = 0; i < s; i++)
        {
            Object v = this.values[i];
            if(v != null)
            {
                all.put(this.names[i], Utilities.copyViaSerializationIfNecessary(v));
            }
        }
        return all;
    }


    public Object setProperty(String name, Object value)
    {
        return setPropertyInternal(name, value);
    }


    public Object getProperty(String name)
    {
        return getPropertyInternal(name);
    }


    public Object removeProperty(String name)
    {
        return setPropertyInternal(name, null);
    }


    public Set<String> getPropertyNames()
    {
        if(isLocalized())
        {
            return Collections.EMPTY_SET;
        }
        return getPropertyNamesInternal();
    }


    protected Set<String> getPropertyNamesInternal()
    {
        Set<String> nameSet = null;
        int s = this.names.length;
        for(int i = 0; i < s; i++)
        {
            if(this.values[i] != null)
            {
                if(nameSet == null)
                {
                    nameSet = new HashSet<>(s);
                }
                nameSet.add(this.names[i]);
            }
        }
        return (nameSet != null) ? nameSet : Collections.EMPTY_SET;
    }


    public boolean hasModifiedProperties()
    {
        return false;
    }


    public void commitProperties()
    {
    }


    public void rollbackProperties()
    {
    }


    public Map<String, Object> getAllProperties(PK langPK)
    {
        if(!langPK.equals(this.langPK))
        {
            return Collections.EMPTY_MAP;
        }
        return getAllPropertiesInternal();
    }


    public Object setProperty(String name, PK langPK, Object value)
    {
        return setProperty(name, value);
    }


    public Object getProperty(String name, PK langPK)
    {
        return getProperty(name);
    }


    public Object removeProperty(String name, PK langPK)
    {
        return removeProperty(name);
    }


    public Set<String> getPropertyNames(PK langPK)
    {
        if(!langPK.equals(this.langPK))
        {
            return Collections.EMPTY_SET;
        }
        return getPropertyNamesInternal();
    }


    protected Object clone() throws CloneNotSupportedException
    {
        if(this.hasChangedFlag)
        {
            throw new EJBInternalException(null, "you cannot clone a modified EJBPropertyRowCache object", 0);
        }
        return new EJBPropertyRowCache(this.langPK, getVersion(), this.names, this.values, this.hasChangedFlag, this.inDB, (BitSet)this.changeList
                        .clone());
    }
}
