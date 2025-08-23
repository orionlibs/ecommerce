package de.hybris.platform.persistence.property;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInternalException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class TypeInfoMap implements Serializable
{
    static final long serialVersionUID = -5285784813091515262L;
    public static final int UNLOCALIZED = 0;
    public static final int LOCALIZED = 1;
    public static final int CORE = 2;
    public static final int UNKNOWN = 3;
    public static final TypeInfoMap EMPTY_INFOMAP = new TypeInfoMap();
    private static final Logger log = Logger.getLogger(TypeInfoMap.class);
    public static final int MOD_ABSTRACT = 1;
    public static final int MOD_VIEW_TYPE = 2;
    public static final int MOD_JALO_ONLY = 4;
    private final PK typePK;
    private final PK superTypePK;
    private final String code;
    private final boolean relationType;
    private final int modifiers;
    private final int itemTypeCode;
    private final String itemTableName;
    private final String ulTableName;
    private final String lTableName;
    private final String oldPropTableName;
    private final String auditTableName;
    private boolean tablesInitializedFlag = true;
    private Map<String, PropertyColumnInfo> coreProp2ColumnMap = null;
    private Map<String, PropertyColumnInfo> unlocProp2ColumnMap = null;
    private Map<String, PropertyColumnInfo> locProp2ColumnMap = null;
    private Set<String> encryptedProperties = null;
    private transient List<String> sortedUnlocNames = null;
    private transient List<String> sortedCoreNames = null;
    private transient List<String> sortedLocNames = null;
    private transient Set<String> unknown = null;


    public boolean tablesInitialized()
    {
        return this.tablesInitializedFlag;
    }


    public void setTablesInitialized()
    {
        if(this.tablesInitializedFlag)
        {
            throw new EJBInternalException(null, "cannot call TypeInfoMap.setTablesInitialized() twice on " + this, 0);
        }
        this.tablesInitializedFlag = true;
    }


    public List getSortedCoreNames()
    {
        if(this.sortedCoreNames == null)
        {
            sort(2);
        }
        return (this.sortedCoreNames != null) ? this.sortedCoreNames : Collections.EMPTY_LIST;
    }


    public List getSortedNames(boolean localized)
    {
        if(localized)
        {
            if(this.sortedLocNames == null)
            {
                sort(1);
            }
            return (this.sortedLocNames != null) ? this.sortedLocNames : Collections.EMPTY_LIST;
        }
        if(this.sortedUnlocNames == null)
        {
            sort(0);
        }
        return (this.sortedUnlocNames != null) ? this.sortedUnlocNames : Collections.EMPTY_LIST;
    }


    private synchronized void sort(int propertyType)
    {
        switch(propertyType)
        {
            case 2:
                if(this.coreProp2ColumnMap != null && this.sortedCoreNames == null)
                {
                    this.sortedCoreNames = Collections.unmodifiableList(extractSortedNames(this.coreProp2ColumnMap));
                }
                break;
            case 1:
                if(this.locProp2ColumnMap != null && this.sortedLocNames == null)
                {
                    this.sortedLocNames = Collections.unmodifiableList(extractSortedNames(this.locProp2ColumnMap));
                }
                break;
            case 0:
                if(this.unlocProp2ColumnMap != null && this.sortedUnlocNames == null)
                {
                    this.sortedUnlocNames = Collections.unmodifiableList(extractSortedNames(this.unlocProp2ColumnMap));
                }
                break;
        }
    }


    private List<String> extractSortedNames(Map<String, PropertyColumnInfo> colMap)
    {
        List<String> names = new ArrayList<>(colMap.size());
        for(Map.Entry<String, PropertyColumnInfo> e : colMap.entrySet())
        {
            names.add(((PropertyColumnInfo)e.getValue()).getPropertyName());
        }
        Collections.sort(names);
        return names;
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("TypeInfoMap for type = ").append(this.typePK).append("\n");
        sb.append("   code = ").append(this.code).append("\n");
        sb.append("   superType = ").append(this.superTypePK).append("\n");
        sb.append("   itemTable = ").append(this.itemTableName).append("\n");
        sb.append("   UPTable = ").append(this.ulTableName).append("\n");
        sb.append("   LTableName = ").append(this.lTableName).append("\n");
        sb.append("   PropsTable = ").append(this.oldPropTableName).append("\n");
        sb.append("   core fields = ");
        if(this.coreProp2ColumnMap != null)
        {
            sb.append("\n");
            for(Iterator<Map.Entry> it = this.coreProp2ColumnMap.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry e = it.next();
                String prop = (String)e.getKey();
                PropertyColumnInfo info = (PropertyColumnInfo)e.getValue();
                sb.append("      ").append(prop).append(" = ").append(info.toString()).append("\n");
            }
        }
        else
        {
            sb.append(" []\n");
        }
        sb.append("   unlocalized fields = ");
        if(this.unlocProp2ColumnMap != null)
        {
            sb.append("\n");
            for(Iterator<Map.Entry> it = this.unlocProp2ColumnMap.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry e = it.next();
                String prop = (String)e.getKey();
                PropertyColumnInfo info = (PropertyColumnInfo)e.getValue();
                sb.append("      ").append(prop).append(" = ").append(info.toString()).append("\n");
            }
        }
        else
        {
            sb.append(" []\n");
        }
        sb.append("   localized fields = ");
        if(this.locProp2ColumnMap != null)
        {
            sb.append("\n");
            for(Iterator<Map.Entry> it = this.locProp2ColumnMap.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry e = it.next();
                String prop = (String)e.getKey();
                PropertyColumnInfo info = (PropertyColumnInfo)e.getValue();
                sb.append("      ").append(prop).append(" = ").append(info.toString()).append("\n");
            }
        }
        else
        {
            sb.append(" []\n");
        }
        return sb.toString();
    }


    TypeInfoMap()
    {
        this.typePK = null;
        this.superTypePK = null;
        this.code = null;
        this.modifiers = 1;
        this.itemTypeCode = 0;
        this.relationType = false;
        this.itemTableName = null;
        this.ulTableName = null;
        this.lTableName = null;
        this.oldPropTableName = null;
        this.auditTableName = null;
        this.tablesInitializedFlag = false;
    }


    public TypeInfoMap(PK typePK, PK superTypePK, String code, boolean isRelationType, int modifiers)
    {
        this.typePK = typePK;
        this.superTypePK = superTypePK;
        this.code = code.intern();
        this.relationType = isRelationType;
        this.modifiers = modifiers;
        this.itemTypeCode = 0;
        this.itemTableName = null;
        this.ulTableName = null;
        this.lTableName = null;
        this.oldPropTableName = null;
        this.auditTableName = null;
        this.tablesInitializedFlag = false;
    }


    public TypeInfoMap(PK typePK, PK superTypePK, String code, int itemTypeCode, boolean isRelationType, int modifiers, String itemTableName, String ulTableName, String lTableName, String oldPropTableName, String auditTableName)
    {
        if(typePK == null)
        {
            throw new EJBInternalException(null, "typePK was null ", 0);
        }
        if(itemTableName == null && (modifiers & 0x1) == 0)
        {
            throw new EJBInternalException(null, "item table was null ", 0);
        }
        this.typePK = typePK;
        this.superTypePK = superTypePK;
        this.code = code.intern();
        this.relationType = isRelationType;
        this.modifiers = modifiers;
        this.itemTypeCode = itemTypeCode;
        this.itemTableName = (itemTableName != null) ? itemTableName.intern() : null;
        this.ulTableName = (ulTableName != null) ? ulTableName.toLowerCase(LocaleHelper.getPersistenceLocale()).intern() : null;
        this.lTableName = (lTableName != null) ? lTableName.toLowerCase(LocaleHelper.getPersistenceLocale()).intern() : null;
        this.oldPropTableName = (oldPropTableName != null) ? oldPropTableName.toLowerCase(LocaleHelper.getPersistenceLocale()).intern() : null;
        this.auditTableName = (auditTableName != null) ? auditTableName.toLowerCase(LocaleHelper.getPersistenceLocale()).intern() : null;
        this.tablesInitializedFlag = false;
    }


    public TypeInfoMap(TypeInfoMap original, PK typePK, PK superTypePK, String code)
    {
        this(typePK, superTypePK, code, original.itemTypeCode, original.relationType, original.modifiers, original.itemTableName, original.ulTableName, original.lTableName, original.oldPropTableName, original.auditTableName);
        this.tablesInitializedFlag = original.tablesInitializedFlag;
        this
                        .coreProp2ColumnMap = (this.coreProp2ColumnMap != null) ? new HashMap<>(original.coreProp2ColumnMap) : null;
        this
                        .unlocProp2ColumnMap = (this.unlocProp2ColumnMap != null) ? new HashMap<>(original.unlocProp2ColumnMap) : null;
        this
                        .locProp2ColumnMap = (this.locProp2ColumnMap != null) ? new HashMap<>(original.locProp2ColumnMap) : null;
    }


    public PK getTypePK()
    {
        return this.typePK;
    }


    public PK getSuperTypePK()
    {
        return this.superTypePK;
    }


    public String getCode()
    {
        return this.code;
    }


    public boolean isEmpty()
    {
        return (this.typePK == null);
    }


    public boolean isEncrypted(String propertyName)
    {
        return (this.encryptedProperties != null && this.encryptedProperties.contains(getCaseInsensitiveKey(propertyName)));
    }


    public int getPropertyType(String propertyName)
    {
        String key = getCaseInsensitiveKey(propertyName);
        if(this.coreProp2ColumnMap != null && this.coreProp2ColumnMap.containsKey(key))
        {
            return 2;
        }
        if(this.unlocProp2ColumnMap != null && this.unlocProp2ColumnMap.containsKey(key))
        {
            return 0;
        }
        if(this.locProp2ColumnMap != null && this.locProp2ColumnMap.containsKey(key))
        {
            return 1;
        }
        if(this.unknown == null)
        {
            this.unknown = new HashSet<>();
        }
        else if(!this.unknown.contains(key))
        {
            this.unknown.add(key);
            if(log.isDebugEnabled())
            {
                log.debug("Did not find property '" + propertyName + "' in infoMap for " + this.typePK + " table " + this.itemTableName + ",coreProp2ColumnMap=" + (
                                (this.coreProp2ColumnMap != null) ?
                                                this.coreProp2ColumnMap.keySet().toString() : "null") + ",unlocProp2ColumnMap=" + (
                                (this.unlocProp2ColumnMap != null) ?
                                                this.unlocProp2ColumnMap.keySet().toString() : "null") + ",locProp2ColumnMap=" + (
                                (this.locProp2ColumnMap != null) ?
                                                this.locProp2ColumnMap.keySet().toString() : "null"));
            }
        }
        return 3;
    }


    public void addToEncryptedProperties(String propertyName)
    {
        if(this.encryptedProperties == null)
        {
            this.encryptedProperties = new HashSet<>();
        }
        this.encryptedProperties.add(getCaseInsensitiveKey(propertyName));
    }


    public void add(String propertyName, String columnName, Class javaClass, boolean core, boolean localized)
    {
        if(tablesInitialized())
        {
            throw new EJBInternalException(null, "cannot add property column info to TypeInfoMap after its tables have been initialized", 0);
        }
        PropertyColumnInfo propertyInfo = new PropertyColumnInfo(propertyName, columnName, javaClass);
        if(core)
        {
            if(this.coreProp2ColumnMap == null)
            {
                this.coreProp2ColumnMap = new HashMap<>();
            }
            this.coreProp2ColumnMap.put(getCaseInsensitiveKey(propertyName), propertyInfo);
        }
        else if(localized)
        {
            if(this.locProp2ColumnMap == null)
            {
                this.locProp2ColumnMap = new HashMap<>();
            }
            else
            {
                for(PropertyColumnInfo colInfo : this.locProp2ColumnMap.values())
                {
                    if(columnName.equalsIgnoreCase(colInfo.getColumnName()))
                    {
                        throw new EJBInternalException(null, "tried to set duplicate localized property info for column name '" + columnName + "', propertyName '" + propertyName + "'", 0);
                    }
                }
            }
            this.locProp2ColumnMap.put(getCaseInsensitiveKey(propertyName), propertyInfo);
            this.sortedLocNames = null;
        }
        else
        {
            if(this.unlocProp2ColumnMap == null)
            {
                this.unlocProp2ColumnMap = new HashMap<>();
            }
            else
            {
                for(PropertyColumnInfo colInfo : this.unlocProp2ColumnMap.values())
                {
                    if(columnName.equalsIgnoreCase(colInfo.getColumnName()))
                    {
                        if(isNotCustomEjbRemovalException(columnName, propertyName, colInfo))
                        {
                            throw new EJBInternalException(null, "tried to set duplicate unlocalized property info for column name '" + columnName + "', propertyName '" + propertyName + "'", 0);
                        }
                    }
                }
            }
            this.unlocProp2ColumnMap.put(getCaseInsensitiveKey(propertyName), propertyInfo);
            this.sortedUnlocNames = null;
        }
    }


    private boolean isNotCustomEjbRemovalException(String columnName, String propertyName, PropertyColumnInfo colInfo)
    {
        return (!"Passwd".equalsIgnoreCase(columnName) || ((!"encodedPassword".equalsIgnoreCase(propertyName) ||
                        !"password".equalsIgnoreCase(colInfo.getPropertyName())) && (!"encodedPassword".equalsIgnoreCase(colInfo
                        .getPropertyName()) ||
                        !"password".equalsIgnoreCase(propertyName))));
    }


    public String getTableName(boolean localized)
    {
        String tabName = localized ? this.lTableName : this.ulTableName;
        if(tabName == null)
        {
            return null;
        }
        return tabName;
    }


    public String getItemTableName()
    {
        return this.itemTableName;
    }


    public String getOldPropTableName()
    {
        return this.oldPropTableName;
    }


    public String getAuditTableName()
    {
        return this.auditTableName;
    }


    public boolean hasInfos(boolean localized)
    {
        Map<String, PropertyColumnInfo> m = localized ? this.locProp2ColumnMap : this.unlocProp2ColumnMap;
        return (m != null && !m.isEmpty());
    }


    public boolean hasLocalizedColumns()
    {
        return (this.locProp2ColumnMap != null && !this.locProp2ColumnMap.isEmpty());
    }


    public boolean hasCorePropsColumns()
    {
        return (this.coreProp2ColumnMap != null && !this.coreProp2ColumnMap.isEmpty());
    }


    public PropertyColumnInfo getInfoForCoreProperty(String propertyName)
    {
        return (this.coreProp2ColumnMap != null) ? this.coreProp2ColumnMap.get(
                        getCaseInsensitiveKey(propertyName)) : null;
    }


    public PropertyColumnInfo getInfoForProperty(String propertyName, boolean localized)
    {
        Map<String, PropertyColumnInfo> m = localized ? this.locProp2ColumnMap : this.unlocProp2ColumnMap;
        return (m != null) ? m.get(getCaseInsensitiveKey(propertyName)) : null;
    }


    public boolean isRelationType()
    {
        return this.relationType;
    }


    public boolean isAbstract()
    {
        return ((this.modifiers & 0x1) == 1);
    }


    public boolean isJaloOnly()
    {
        return ((this.modifiers & 0x4) == 4);
    }


    public boolean isViewType()
    {
        return ((this.modifiers & 0x2) == 2);
    }


    public int getItemTypeCode()
    {
        return this.itemTypeCode;
    }


    public static final String getCaseInsensitiveKey(String qualifier)
    {
        return (qualifier != null) ? qualifier.toLowerCase(LocaleHelper.getPersistenceLocale()) : null;
    }
}
