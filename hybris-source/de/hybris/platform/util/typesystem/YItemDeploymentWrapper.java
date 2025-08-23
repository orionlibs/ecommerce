package de.hybris.platform.util.typesystem;

import de.hybris.bootstrap.typesystem.YAttributeDeployment;
import de.hybris.bootstrap.typesystem.YDeployment;
import de.hybris.bootstrap.typesystem.YFinder;
import de.hybris.bootstrap.typesystem.YIndexDeployment;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.persistence.GenericBMPBean;
import de.hybris.platform.persistence.GenericItemEJB;
import de.hybris.platform.persistence.link.GenericLinkBMPBean;
import de.hybris.platform.persistence.link.LinkEJB;
import de.hybris.platform.util.Config;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class YItemDeploymentWrapper implements ItemDeployment
{
    private final YDeployment depl;
    private transient String tablePrefix;
    private transient Class implClass;
    private transient Class homeClass;
    private transient Class remoteClass;
    private transient Class concreteImplClass;
    private transient String name;
    private transient String superDeploymentName;
    private transient String table;
    private transient String propsTable;
    private transient String auditTableName;
    private final Map<String, ItemDeployment.Attribute> attributesMap;
    private final Map<String, ItemDeployment.FinderMethod> findersMap;
    private final Map<String, ItemDeployment.Index> indexesMap;


    public static final String adjustType(String type)
    {
        if("de.hybris.platform.util.ItemPropertyValueCollection".equals(type))
        {
            return "java.lang.String";
        }
        if("de.hybris.platform.util.ItemPropertyValue".equals(type))
        {
            return "de.hybris.platform.core.PK";
        }
        return type;
    }


    private final Map<String, String> columnNameCache = new ConcurrentHashMap<>(30, 0.75F, 64);


    public YItemDeploymentWrapper(YDeployment depl)
    {
        this.depl = depl;
        this.attributesMap = createAttributes(depl);
        this.findersMap = createFinders(depl);
        this.indexesMap = createIndexes(depl);
    }


    protected Map<String, ItemDeployment.Attribute> attributes()
    {
        return this.attributesMap;
    }


    protected Map<String, ItemDeployment.Attribute> createAttributes(YDeployment depl)
    {
        Map<Object, Object> attributes = new LinkedHashMap<>();
        for(YAttributeDeployment ad : depl.getAttributeDeployments())
        {
            attributes.put(ad.getPersistenceQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()), new YAttributeWrapper(this, ad));
        }
        return (Map)Collections.unmodifiableMap(attributes);
    }


    protected Map<String, ItemDeployment.FinderMethod> finders()
    {
        return this.findersMap;
    }


    protected Map<String, ItemDeployment.FinderMethod> createFinders(YDeployment depl)
    {
        StringBuilder sb = new StringBuilder();
        Map<Object, Object> finders = new LinkedHashMap<>();
        for(YFinder f : depl.getFinders())
        {
            sb.setLength(0);
            sb.append(f.getName()).append("(");
            boolean first = true;
            for(String sig : f.getSignatureClassNames())
            {
                if(first)
                {
                    first = false;
                }
                else
                {
                    sb.append(",");
                }
                sb.append(sig);
            }
            sb.append(")");
            finders.put(sb.toString(), new YFinderWrapper(this, f));
        }
        return (Map)Collections.unmodifiableMap(finders);
    }


    protected Map<String, ItemDeployment.Index> indexes()
    {
        return this.indexesMap;
    }


    protected Map<String, ItemDeployment.Index> createIndexes(YDeployment depl)
    {
        Map<Object, Object> indexes = new LinkedHashMap<>();
        for(YIndexDeployment id : depl.getIndexDeployments())
        {
            indexes.put(id.getIndexName().toLowerCase(LocaleHelper.getPersistenceLocale()), new YIndexWrapper(this, id));
        }
        return (Map)Collections.unmodifiableMap(indexes);
    }


    public ItemDeployment.Attribute getAttribute(String persistenceQualifier)
    {
        return attributes().get(persistenceQualifier.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    public Collection<ItemDeployment.Attribute> getAttributes()
    {
        return attributes().values();
    }


    private final Class getClass(String className)
    {
        try
        {
            return Class.forName(className, false, getClass().getClassLoader());
        }
        catch(ClassNotFoundException e)
        {
            System.err.println("cannot find class '" + className + "'");
            return null;
        }
    }


    public Class getConcreteEJBImplementationClass()
    {
        if(this.concreteImplClass == null)
        {
            Class implClass = getImplClass();
            if(LinkEJB.class.equals(implClass))
            {
                this.concreteImplClass = GenericLinkBMPBean.class;
            }
            else if(GenericItemEJB.class.equals(implClass))
            {
                this.concreteImplClass = GenericBMPBean.class;
            }
            else
            {
                this.concreteImplClass = getClass(getName() + "_HJMPWrapper");
            }
        }
        return this.concreteImplClass;
    }


    public String getDatabaseTableName()
    {
        if(this.table == null)
        {
            if(this.depl.getTableName() != null)
            {
                this.table = getTablePrefix() + getTablePrefix();
            }
        }
        return this.table;
    }


    public String getDumpPropertyTableName()
    {
        if(this.propsTable == null)
        {
            if(this.depl.getPropsTableName() != null)
            {
                this.propsTable = getTablePrefix() + getTablePrefix();
            }
        }
        return this.propsTable;
    }


    public String getAuditTableName()
    {
        if(this.auditTableName == null)
        {
            if(this.depl.getAuditTableName() != null)
            {
                this.auditTableName = getTablePrefix() + getTablePrefix();
            }
        }
        return this.auditTableName;
    }


    public ItemDeployment.FinderMethod getFinderMethod(String methodName, String signature)
    {
        return finders().get(methodName + "(" + methodName + ")");
    }


    public Collection<ItemDeployment.FinderMethod> getFinderMethods()
    {
        return finders().values();
    }


    public Class getHomeInterface()
    {
        if(this.homeClass == null)
        {
            this.homeClass = getClass(this.depl.getClassBaseName() + "Home");
        }
        return this.homeClass;
    }


    public Class getImplClass()
    {
        if(this.implClass == null)
        {
            this.implClass = getClass(this.depl.getClassBaseName() + "EJB");
        }
        return this.implClass;
    }


    public ItemDeployment.Index getIndex(String name)
    {
        return indexes().get(name.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    public Collection<ItemDeployment.Index> getIndexes()
    {
        return indexes().values();
    }


    public String getName()
    {
        if(this.name == null)
        {
            this.name = this.depl.getFullName().intern();
        }
        return this.name;
    }


    public Class getRemoteInterface()
    {
        if(this.remoteClass == null)
        {
            this.remoteClass = getClass(this.depl.getClassBaseName() + "Remote");
        }
        return this.remoteClass;
    }


    public String getSuperDeploymentName()
    {
        if(this.superDeploymentName == null)
        {
            this
                            .superDeploymentName = (this.depl.getSuperDeploymentName() != null) ? this.depl.getSuperDeployment().getFullName().intern() : null;
        }
        return this.superDeploymentName;
    }


    public int getTypeCode()
    {
        return this.depl.getItemTypeCode();
    }


    public boolean isAbstract()
    {
        return this.depl.isAbstract();
    }


    public boolean isFinal()
    {
        return this.depl.isFinal();
    }


    public boolean isGeneric()
    {
        return this.depl.isGeneric();
    }


    String getTablePrefix()
    {
        if(this.tablePrefix == null)
        {
            this.tablePrefix = Config.getString("db.tableprefix", "");
        }
        return this.tablePrefix;
    }


    YDeployment getOriginal()
    {
        return this.depl;
    }


    public boolean isNonItemDeployment()
    {
        return getOriginal().isNonItemDeployment();
    }


    public String toString()
    {
        return getName();
    }


    public String getColumnName(String attribut, String database)
    {
        String ret = this.columnNameCache.get(attribut);
        if(ret == null)
        {
            ret = getAttribute(attribut).getColumnName(database);
            this.columnNameCache.put(attribut, ret);
        }
        return ret;
    }
}
