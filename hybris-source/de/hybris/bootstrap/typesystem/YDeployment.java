package de.hybris.bootstrap.typesystem;

import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.ddl.StandalonePropertiesLoader;
import de.hybris.bootstrap.util.LocaleHelper;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;

public class YDeployment extends YNameSpaceElement
{
    private static final Map<String, String> NATIVE_TYPES_MAP = (Map<String, String>)new CaseInsensitiveMap();
    private static final String AUDIT_TABLE_SUFFIX = "sn";
    private static final String AUDITING_ENABLED = "auditing.enabled";
    private final String packageName;
    private final String name;
    private final String superDeploymentName;

    static
    {
        NATIVE_TYPES_MAP.put("long", Long.class.getName());
        NATIVE_TYPES_MAP.put("int", Integer.class.getName());
        NATIVE_TYPES_MAP.put("double", Double.class.getName());
        NATIVE_TYPES_MAP.put("float", Float.class.getName());
        NATIVE_TYPES_MAP.put("byte", Byte.class.getName());
        NATIVE_TYPES_MAP.put("short", Short.class.getName());
        NATIVE_TYPES_MAP.put("char", Character.class.getName());
        NATIVE_TYPES_MAP.put("boolean", Boolean.class.getName());
        NATIVE_TYPES_MAP.put("HYBRIS.PK", "de.hybris.platform.core.PK");
        NATIVE_TYPES_MAP.put("HYBRIS.LONG_STRING", String.class.getName());
        NATIVE_TYPES_MAP.put("HYBRIS.COMMA_SEPARATED_PKS", String.class.getName());
        NATIVE_TYPES_MAP.put("HYBRIS.JSON", String.class.getName());
    }

    public static final String convertNativeTypes(String className)
    {
        String mapped = NATIVE_TYPES_MAP.get(className);
        return (mapped != null) ? mapped : className;
    }


    private boolean abstractVariable = false;
    private boolean generic = false;
    private boolean finalVariable = false;
    private boolean nonItemDeployment = false;
    private final int itemTypeCode;
    private transient YDeployment superDeployment;
    private String propsTableName = "Props".toLowerCase(LocaleHelper.getPersistenceLocale());
    private String auditTableName;
    private final String tableName;


    public YDeployment(YNamespace container, String packageName, String name, String superDeploymentName, String tableName, int itemTypeCode)
    {
        super(container);
        if(name == null || name.length() == 0)
        {
            throw new IllegalArgumentException("name is required, was '" + name + "'");
        }
        this.packageName = packageName;
        this.name = name;
        this.superDeploymentName = superDeploymentName;
        this.tableName = (tableName != null) ? tableName.toLowerCase(LocaleHelper.getPersistenceLocale()) : null;
        this.itemTypeCode = itemTypeCode;
        this
                        .auditTableName = (this.tableName == null) ? null : (StringUtils.truncate(this.tableName, 15) + StringUtils.truncate(this.tableName, 15) + "sn");
    }


    public String toString()
    {
        return getFullName() + "::" + getFullName();
    }


    public void resetCaches()
    {
        super.resetCaches();
        this.superDeployment = null;
    }


    public void validate()
    {
        super.validate();
        if(this.itemTypeCode > 0 && (this.tableName == null || this.tableName.length() == 0))
        {
            throw new IllegalArgumentException("table name is required, was '" + this.tableName + "'");
        }
        getSuperDeployment();
        if(!isAbstract() && (getTableName() == null || (!isNonItemDeployment() && getPropsTableName() == null)))
        {
            throw new IllegalStateException("invalid non-abstract deployment " + this + " due to missing item or props table name ");
        }
    }


    public String getFullName()
    {
        return (getPackageName() != null) ? (getPackageName() + "." + getPackageName()) : getName();
    }


    public String getName()
    {
        return this.name;
    }


    public String getClassBaseName()
    {
        if(isGeneric())
        {
            return getSuperDeployment().getClassBaseName();
        }
        return getFullName();
    }


    public String getTableName()
    {
        return (this.tableName != null) ? this.tableName : null;
    }


    public Set<YIndexDeployment> getCoreTableIndexDeployments()
    {
        return getIndexDeployments(YAttributeDeployment.TableType.CORE);
    }


    public Set<YIndexDeployment> getLocTableIndexDeployments()
    {
        return getIndexDeployments(YAttributeDeployment.TableType.LOC);
    }


    public Set<YIndexDeployment> getIndexDeployments(YAttributeDeployment.TableType tableType)
    {
        Set<YIndexDeployment> ret = null;
        for(YIndexDeployment iDepl : getIndexDeployments())
        {
            YAttributeDeployment.TableType idxTT = null;
            for(YAttributeDeployment aDepl : iDepl.getIndexedAttributes())
            {
                if(idxTT != null && idxTT != aDepl.getTableType())
                {
                    throw new IllegalStateException("invalid index " + this + " due to holding attributes from different table types " + idxTT + "<>" + aDepl
                                    .getTableType());
                }
                idxTT = aDepl.getTableType();
            }
            if(tableType.equals(idxTT))
            {
                if(ret == null)
                {
                    ret = new LinkedHashSet<>();
                }
                ret.add(iDepl);
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    public Set<YIndexDeployment> getIndexDeployments()
    {
        Set<String> controlSet = new HashSet<>();
        Set<YAttributeDeployment> permittedAttributeDeployments = null;
        Set<YIndexDeployment> indexDeployments = new HashSet<>();
        for(YDeployment d = this; d != null; d = d.getSuperDeployment())
        {
            label22:
            for(YIndexDeployment idx : getTypeSystem().getIndexDeployments(d.getName()))
            {
                if(controlSet.add(idx.getIndexName().toLowerCase(LocaleHelper.getPersistenceLocale())))
                {
                    if(permittedAttributeDeployments == null)
                    {
                        permittedAttributeDeployments = getAttributeDeployments();
                    }
                    for(YAttributeDeployment idxAttr : idx.getIndexedAttributes())
                    {
                        if(!permittedAttributeDeployments.contains(idxAttr))
                        {
                            continue label22;
                        }
                    }
                    indexDeployments.add(idx);
                }
            }
        }
        return indexDeployments;
    }


    public YIndexDeployment getIndexDeployment(String indexName)
    {
        for(YIndexDeployment idx : getIndexDeployments())
        {
            if(indexName.equalsIgnoreCase(idx.getIndexName()))
            {
                return idx;
            }
        }
        return null;
    }


    public Set<YAttributeDeployment> getCoreTableAttributeDeployments()
    {
        return getAttributeDeployments(YAttributeDeployment.TableType.CORE);
    }


    public Set<YAttributeDeployment> getLocTableAttributeDeployments()
    {
        return getAttributeDeployments(YAttributeDeployment.TableType.LOC);
    }


    public Set<YAttributeDeployment> getPropsTableAttributeDeployments()
    {
        return getAttributeDeployments(YAttributeDeployment.TableType.PROPS);
    }


    public boolean isLocalized()
    {
        for(YAttributeDeployment ad : getAttributeDeployments())
        {
            if(ad.getTableType() == YAttributeDeployment.TableType.LOC)
            {
                return true;
            }
        }
        return false;
    }


    protected Set<YAttributeDeployment> getAttributeDeployments(YAttributeDeployment.TableType tableType)
    {
        Set<YAttributeDeployment> ret = new HashSet<>();
        for(YAttributeDeployment ad : getAttributeDeployments())
        {
            if(ad.getTableType().equals(tableType))
            {
                ret.add(ad);
            }
        }
        return ret;
    }


    public Set<YAttributeDeployment> getAttributeDeployments()
    {
        Set<String> controlSet = null;
        Set<YAttributeDeployment> ret = null;
        for(YDeployment depl : getInheritancePath())
        {
            for(YAttributeDeployment col : getTypeSystem().getAttributeDeployments(depl.getName()))
            {
                if(controlSet == null)
                {
                    controlSet = new LinkedHashSet<>();
                }
                if(controlSet.add(col.getPersistenceQualifier().toLowerCase(LocaleHelper.getPersistenceLocale())))
                {
                    if(ret == null)
                    {
                        ret = new LinkedHashSet<>();
                    }
                    ret.add(col);
                }
            }
        }
        if(ret != null)
        {
            Set<YComposedType> myLegalTypes = getTypeSystem().getDeploymentTypes(this);
            for(Iterator<YAttributeDeployment> it = ret.iterator(); it.hasNext(); )
            {
                YAttributeDeployment aDepl = it.next();
                YAttributeDescriptor attDesc = aDepl.getAttributeDescriptor();
                if(attDesc != null && !myLegalTypes.contains(attDesc.getEnclosingType()))
                {
                    it.remove();
                }
            }
            return ret;
        }
        return Collections.EMPTY_SET;
    }


    public YAttributeDeployment getAttributeDeployment(String persistenceQualifier)
    {
        for(YAttributeDeployment ad : getAttributeDeployments())
        {
            if(persistenceQualifier.equalsIgnoreCase(ad.getPersistenceQualifier()))
            {
                return ad;
            }
        }
        return null;
    }


    public Set<YFinder> getFinders()
    {
        Set<String> controlSet = new HashSet<>();
        Set<YFinder> finders = new HashSet<>();
        for(YDeployment d = this; d != null; d = d.getSuperDeployment())
        {
            for(YFinder f : getTypeSystem().getFinders(d.getName()))
            {
                if(controlSet.add(f.getName().toLowerCase(LocaleHelper.getPersistenceLocale())))
                {
                    finders.add(f);
                }
            }
        }
        return finders;
    }


    public int getItemTypeCode()
    {
        return this.itemTypeCode;
    }


    public String getPackageName()
    {
        return this.packageName;
    }


    public YDeployment getSuperDeployment()
    {
        if(this.superDeployment == null && getSuperDeploymentName() != null)
        {
            this.superDeployment = getTypeSystem().getDeployment(getSuperDeploymentName());
            if(this.superDeployment == null)
            {
                throw new IllegalStateException("illegal deployment " + this + " due to missing super deployment " +
                                getSuperDeploymentName());
            }
        }
        return (this.superDeployment == this) ? null : this.superDeployment;
    }


    public List<YDeployment> getSuperDeployments()
    {
        LinkedList<YDeployment> ret = null;
        for(YDeployment sd = getSuperDeployment(); sd != null; sd = sd.getSuperDeployment())
        {
            if(ret == null)
            {
                ret = new LinkedList<>();
            }
            ret.addFirst(sd);
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }


    protected List<YDeployment> getInheritancePath()
    {
        List<YDeployment> superDeployments = getSuperDeployments();
        if(superDeployments.isEmpty())
        {
            return Collections.singletonList(this);
        }
        superDeployments.add(this);
        return superDeployments;
    }


    public String getSuperDeploymentName()
    {
        return this.superDeploymentName;
    }


    public boolean isAbstract()
    {
        return (this.abstractVariable || getItemTypeCode() == 0);
    }


    public void setAbstract(boolean isAbstract)
    {
        this.abstractVariable = isAbstract;
    }


    public boolean isGeneric()
    {
        return this.generic;
    }


    public void setGeneric(boolean isGeneric)
    {
        this.generic = isGeneric;
    }


    public String getPropsTableName()
    {
        return this.propsTableName;
    }


    public void setPropsTableName(String propsTableName)
    {
        this.propsTableName = (propsTableName != null) ? propsTableName.toLowerCase(LocaleHelper.getPersistenceLocale()) : null;
    }


    public String getAuditTableName()
    {
        return this.auditTableName;
    }


    public void setAuditTableName(String auditTableName)
    {
        this.auditTableName = (auditTableName != null) ? auditTableName.toLowerCase(LocaleHelper.getPersistenceLocale()) : null;
    }


    public boolean isNonItemDeployment()
    {
        return this.nonItemDeployment;
    }


    public void setNonItemDeployment(boolean isNonItemDeployment)
    {
        this.nonItemDeployment = isNonItemDeployment;
        if(this.nonItemDeployment)
        {
            setPropsTableName(null);
            setAuditTableName(null);
            setGeneric(false);
            setAbstract(true);
        }
    }


    public boolean isFinal()
    {
        return this.finalVariable;
    }


    public void setFinal(boolean isFinal)
    {
        this.finalVariable = isFinal;
    }


    @Deprecated(forRemoval = true)
    public boolean isAuditingEnabled(PlatformConfig platformConfig)
    {
        return isAuditingEnabled(platformConfig, "master");
    }


    public boolean isAuditingEnabled(PlatformConfig platformConfig, String tenantId)
    {
        StandalonePropertiesLoader loader = new StandalonePropertiesLoader(platformConfig, tenantId);
        return Boolean.parseBoolean(loader.getProperty("auditing.enabled", "false"));
    }
}
