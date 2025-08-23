package de.hybris.platform.util.typesystem;

import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YDeployment;
import de.hybris.bootstrap.typesystem.YEnumType;
import de.hybris.bootstrap.typesystem.YRelation;
import de.hybris.bootstrap.typesystem.YType;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.hjmp.HJMPFindInvalidationListener;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.persistence.property.PropertyTableDefinition;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.util.Utilities;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class YPersistenceManager implements PersistenceManager
{
    private static final Logger log = Logger.getLogger(YPersistenceManager.class.getName());
    private final AbstractTenant tenant;
    private final InvalidationManager invManager;
    private String dbName;
    private volatile YPersistedTypeSystem persistedTypeSystem;
    private Map<YDeployment, ItemDeployment> itemDeployments;
    private Map<YDeployment, Set<YDeployment>> subDeployments;
    private Map<YComposedType, TypeInfoMap> typeInfos;
    private Map<Object, TypeInfoMap> runtimeTypeInfos;
    private boolean loaded = false;


    public YPersistenceManager(AbstractTenant tenant, InvalidationManager invManager)
    {
        this.tenant = tenant;
        this.invManager = invManager;
    }


    private void installRootHJMPTopic()
    {
    }


    public boolean isLoaded()
    {
        return this.loaded;
    }


    private void registerHJMPFinderListeners(YDeployment depl)
    {
        int tc = depl.getItemTypeCode();
        String tcStr = PlatformStringUtils.valueOf(tc);
        InvalidationTopic topic = this.invManager.getOrCreateInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, tcStr});
        topic.addInvalidationListener((InvalidationListener)new HJMPFindInvalidationListener(tcStr));
    }


    protected PK getPK(YComposedType type)
    {
        PK ret = system().getPK((YType)type);
        if(ret == null)
        {
            throw new IllegalStateException("missing PK for type '" + type + "'");
        }
        return ret;
    }


    protected YPersistedTypeSystem system()
    {
        if(this.persistedTypeSystem == null)
        {
            throw new IllegalStateException("system no yet loaded");
        }
        return this.persistedTypeSystem;
    }


    public boolean cachesInfoFor(PK typePK)
    {
        YType t = system().getType(typePK);
        return (t instanceof YComposedType && this.typeInfos.containsKey(t));
    }


    public void clearComposedType(PK typePK, String code)
    {
        if(this.typeInfos != null)
        {
            this.typeInfos.remove(system().getType(typePK));
        }
    }


    public Collection<PropertyTableDefinition> createInitialPersistenceInfos(Collection<ComposedTypeRemote> types, boolean reinit)
    {
        return null;
    }


    public PropertyTableDefinition getInitialPersistenceInfos(ComposedTypeRemote type, boolean reinit)
    {
        return null;
    }


    public Collection<ItemDeployment> getAllSubDeployments(ItemDeployment depl)
    {
        YDeployment yDepl;
        if(depl instanceof YItemDeploymentWrapper)
        {
            yDepl = ((YItemDeploymentWrapper)depl).getOriginal();
        }
        else
        {
            yDepl = ((YItemDeploymentWrapper)getItemDeployment(depl.getTypeCode())).getOriginal();
        }
        Set<YDeployment> subs = (this.subDeployments != null) ? this.subDeployments.get(yDepl) : null;
        Collection<ItemDeployment> ret = null;
        while(subs != null && !subs.isEmpty())
        {
            Set<YDeployment> newSubs = null;
            if(ret == null)
            {
                ret = new ArrayList<>();
            }
            for(YDeployment subD : subs)
            {
                ret.add(wrapDeployment(subD));
                Set<YDeployment> mySubs = this.subDeployments.get(subD);
                if(mySubs != null && !mySubs.isEmpty())
                {
                    if(newSubs == null)
                    {
                        newSubs = new LinkedHashSet<>();
                    }
                    newSubs.addAll(mySubs);
                }
            }
            subs = newSubs;
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }


    public Set<Integer> getBeanTypeCodes(PK typePK)
    {
        YType t = system().getType(typePK);
        if(!(t instanceof YComposedType))
        {
            throw new IllegalArgumentException("illegal type pk " + typePK + " - found " + t);
        }
        Set<Integer> ret = null;
        Collection<YComposedType> types = Collections.singleton((YComposedType)t);
        do
        {
            Collection<YComposedType> subTypes = null;
            for(YComposedType ct : types)
            {
                if(!ct.isAbstract())
                {
                    if(ret == null)
                    {
                        ret = new HashSet<>(5);
                    }
                    ret.add(Integer.valueOf(ct.getDeployment().getItemTypeCode()));
                }
                Collection<YComposedType> subs = ct.getSubtypes();
                if(!subs.isEmpty())
                {
                    if(subTypes == null)
                    {
                        subTypes = new ArrayList<>();
                    }
                    subTypes.addAll(subs);
                }
            }
            types = subTypes;
        }
        while(types != null && !types.isEmpty());
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    public Set<PK> getExternalTableTypes(PK typePK)
    {
        YType t = system().getType(typePK);
        if(!(t instanceof YComposedType))
        {
            throw new IllegalArgumentException("illegal type pk " + typePK + " - found " + t);
        }
        return getExternalTableTypes((YComposedType)t);
    }


    protected Set<PK> getExternalTableTypes(YComposedType ct)
    {
        YDeployment depl = ct.getDeployment();
        if(depl.isAbstract())
        {
            Set<PK> set = null;
            for(YComposedType subType : ct.getSubtypes())
            {
                Set<PK> myExternals = getExternalTableTypes(subType);
                if(!myExternals.isEmpty())
                {
                    if(set == null)
                    {
                        set = new HashSet<>(5);
                    }
                    set.addAll(myExternals);
                }
            }
            return (set != null) ? set : Collections.EMPTY_SET;
        }
        Set<String> tables = new HashSet<>();
        tables.add(ct.getDeployment().getTableName().toLowerCase(LocaleHelper.getPersistenceLocale()));
        Set<PK> ret = null;
        Collection<YComposedType> subTypes = ct.getSubtypes();
        while(subTypes != null && !subTypes.isEmpty())
        {
            Collection<YComposedType> nextSubtypes = null;
            for(YComposedType subType : subTypes)
            {
                YDeployment subDepl = subType.getDeployment();
                String tbl = subDepl.getTableName();
                if(!subDepl.isAbstract() && tbl != null && !tables.contains(tbl.toLowerCase(LocaleHelper.getPersistenceLocale())))
                {
                    if(ret == null)
                    {
                        ret = new HashSet<>(5);
                    }
                    ret.add(getPK(subType));
                    tables.add(tbl.toLowerCase(LocaleHelper.getPersistenceLocale()));
                }
                Set<YComposedType> mySubTypes = subType.getSubtypes();
                if(!mySubTypes.isEmpty())
                {
                    if(nextSubtypes == null)
                    {
                        nextSubtypes = new ArrayList<>();
                    }
                    nextSubtypes.addAll(mySubTypes);
                }
            }
            subTypes = nextSubtypes;
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    protected ItemDeployment wrapDeployment(YDeployment depl)
    {
        YItemDeploymentWrapper yItemDeploymentWrapper;
        if(this.itemDeployments == null)
        {
            this.itemDeployments = new LinkedHashMap<>();
        }
        ItemDeployment wrapped = this.itemDeployments.get(depl);
        if(wrapped == null)
        {
            this.itemDeployments.put(depl, yItemDeploymentWrapper = new YItemDeploymentWrapper(depl));
            YDeployment superDepl = depl.getSuperDeployment();
            if(superDepl != null)
            {
                Set<YDeployment> subs = null;
                if(this.subDeployments == null)
                {
                    this.subDeployments = new LinkedHashMap<>();
                }
                else
                {
                    subs = this.subDeployments.get(superDepl);
                }
                if(subs == null)
                {
                    this.subDeployments.put(superDepl, subs = new LinkedHashSet<>());
                }
                subs.add(depl);
            }
            registerHJMPFinderListeners(depl);
        }
        return (ItemDeployment)yItemDeploymentWrapper;
    }


    public ItemDeployment getItemDeployment(String beanOrJNDIName)
    {
        YDeployment depl = system().getDeployment(beanOrJNDIName);
        return (depl != null) ? wrapDeployment(depl) : null;
    }


    public Map<Integer, ItemDeployment> getDuplicatedItemDeployments()
    {
        return Collections.EMPTY_MAP;
    }


    public ItemDeployment getItemDeployment(int tc)
    {
        YDeployment depl = system().getDeployment(tc);
        return (depl != null) ? wrapDeployment(depl) : null;
    }


    public String getJNDIName(int typeCode)
    {
        YDeployment depl = system().getDeployment(typeCode);
        return (depl != null) ? depl.getFullName() : null;
    }


    public String getJNDIName(String code)
    {
        YType t = system().getType(code);
        return (t != null && t instanceof YComposedType) ? ((YComposedType)t).getDeployment().getFullName() : null;
    }


    public TypeInfoMap getPersistenceInfo(String typeCode)
    {
        YComposedType ct = (YComposedType)system().getType(typeCode);
        return (ct != null) ? getPersistenceInfo(ct, typeCode) : getRuntimePersistenceInfo(typeCode, null);
    }


    public TypeInfoMap getPersistenceInfo(PK typePK)
    {
        YComposedType ct = (YComposedType)system().getType(typePK);
        return (ct != null) ? getPersistenceInfo(ct, typePK) : getRuntimePersistenceInfo(null, typePK);
    }


    private TypeInfoMap getPersistenceInfo(YComposedType ct, Object argument)
    {
        TypeInfoMap ret = this.typeInfos.get(ct);
        if(ret == null)
        {
            throw new IllegalArgumentException("unknown type '" + argument + "'=" + ct + " - got no persistence info");
        }
        return ret;
    }


    private TypeInfoMap getRuntimePersistenceInfo(String code, PK typePK)
    {
        TypeInfoMap ret = (this.runtimeTypeInfos != null) ? this.runtimeTypeInfos.get((code != null) ? code.toLowerCase(LocaleHelper.getPersistenceLocale()) : typePK) : null;
        if(ret == null)
        {
            try
            {
                ComposedTypeRemote ctEJB = (code != null) ? this.tenant.getSystemEJB().getTypeManager().getComposedType(code) : (ComposedTypeRemote)this.tenant.getSystemEJB().findRemoteObjectByPK(typePK);
                String inheritancePathString = ctEJB.getInheritancePathString();
                List<PK> iPath = splitInheritancePath(inheritancePathString);
                YComposedType ct = null;
                for(int i = iPath.size() - 1; ct == null && i >= 0; i--)
                {
                    ct = (YComposedType)this.persistedTypeSystem.getType(iPath.get(i));
                }
                if(ct != null)
                {
                    TypeInfoMap original = getPersistenceInfo(ct, (code != null) ? code : typePK);
                    ret = clone(original, ctEJB);
                    if(this.runtimeTypeInfos == null)
                    {
                        this.runtimeTypeInfos = new HashMap<>();
                    }
                    this.runtimeTypeInfos
                                    .put((code != null) ? code.toLowerCase(LocaleHelper.getPersistenceLocale()).intern() : ctEJB.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()).intern(), ret);
                    this.runtimeTypeInfos.put((typePK != null) ? typePK : ctEJB.getPK(), ret);
                }
            }
            catch(EJBItemNotFoundException e)
            {
                throw new IllegalArgumentException("type '" + ((code != null) ? code : typePK) + "' doesnt exist");
            }
            catch(EJBInvalidParameterException e)
            {
                throw new IllegalArgumentException("type '" + typePK + "' doesnt exist");
            }
        }
        return ret;
    }


    private List<PK> splitInheritancePath(String inheritancePath)
    {
        List<PK> ret = new ArrayList<>();
        int pos = 1;
        int i;
        for(i = inheritancePath.indexOf(',', pos); i > 0; i = inheritancePath.indexOf(',', pos))
        {
            PK pk = PK.parse(inheritancePath.substring(pos, i));
            if(pk != null)
            {
                ret.add(pk);
            }
            pos = i + 1;
        }
        return ret;
    }


    private TypeInfoMap clone(TypeInfoMap original, ComposedTypeRemote ejbType)
    {
        return new TypeInfoMap(original, ejbType.getPK(), ejbType.getSuperTypeItemPK(), ejbType.getCode());
    }


    protected TypeInfoMap wrap(YComposedType t)
    {
        TypeInfoMap ret;
        if(this.typeInfos == null)
        {
            this.typeInfos = new HashMap<>();
        }
        if(t.isAbstract())
        {
            ret = new TypeInfoMap(getPK(t), (t.getSuperType() != null) ? getPK(t.getSuperType()) : null, t.getCode(), t instanceof YRelation, t.isAbstract() ? 1 : 0);
        }
        else
        {
            ret = new TypeInfoMap(getPK(t), (t.getSuperType() != null) ? getPK(t.getSuperType()) : null, t.getCode(), t.getDeployment().getItemTypeCode(), t instanceof YRelation, t.isAbstract() ? 1 : 0, t.getDeployment().getTableName(), t.getDeployment().getTableName(),
                            t.getDeployment().getTableName() + t.getDeployment().getTableName(), t.getDeployment().getPropsTableName(), t.getDeployment().getAuditTableName());
            Collection<String> parsed = new ArrayList<>();
            Collection<String> skipped = new ArrayList<>();
            for(YAttributeDescriptor ad : t.getAttributes())
            {
                String quali = ad.getQualifier();
                if(!ad.isPersistable())
                {
                    if(log.isDebugEnabled())
                    {
                        log.debug("skipped '" + ad + "' since it is not persistable");
                    }
                    skipped.add(quali);
                    continue;
                }
                String columnName = ad.getAttributeDeploymentOrFail().getRealColumnName(this.dbName);
                if(columnName == null)
                {
                    if(log.isDebugEnabled())
                    {
                        log.debug("skipped '" + quali + "' since there is no column name");
                    }
                    skipped.add(quali);
                    continue;
                }
                Class javaClass = ad.getAttributeDeploymentOrFail().getJavaType();
                if(javaClass == null)
                {
                    if(log.isDebugEnabled())
                    {
                        log.debug("skipped '" + quali + "' since there is no java class");
                    }
                    skipped.add(quali);
                    continue;
                }
                ret.add(quali, columnName, javaClass, (ad.getPersistenceType() == YAttributeDescriptor.PersistenceType.CMP), ad.isLocalized());
                parsed.add(quali);
            }
        }
        this.typeInfos.put(t, ret);
        return ret;
    }


    public String getSQLTypeDef(Class javaClass, String ownTypeDef)
    {
        return (ownTypeDef != null) ? ownTypeDef : system().getDBTypeMappings(this.dbName).getTypeMapping(javaClass.getName());
    }


    public PK getTypePK(String code)
    {
        YType t = system().getType(code);
        return (t != null && t instanceof YComposedType) ? getPK((YComposedType)t) : null;
    }


    public boolean isRootRelationType(String code)
    {
        YType t = system().getType(code);
        return t instanceof YRelation;
    }


    public void loadPersistenceInfos()
    {
        if(this.persistedTypeSystem == null)
        {
            synchronized(this)
            {
                if(this.persistedTypeSystem == null)
                {
                    if(!Utilities.isSystemInitialized(this.tenant.getDataSource()))
                    {
                        log.warn("system not initialized - cannot load persistence manager");
                        return;
                    }
                    installRootHJMPTopic();
                    this.dbName = this.tenant.getDataSource().getDatabaseName();
                    this.persistedTypeSystem = TypeSystemUtils.loadViaJDBC(this.tenant.getTenantSpecificExtensionNames());
                    for(YDeployment depl : this.persistedTypeSystem.getDeployments())
                    {
                        wrapDeployment(depl);
                    }
                    for(YComposedType ct : this.persistedTypeSystem.getComposedTypes())
                    {
                        if(!ct.isJaloOnly())
                        {
                            wrap(ct);
                        }
                    }
                    for(YRelation rel : this.persistedTypeSystem.getRelationTypes())
                    {
                        wrap((YComposedType)rel);
                    }
                    for(YEnumType et : this.persistedTypeSystem.getEnumTypes())
                    {
                        wrap((YComposedType)et);
                    }
                    this.loaded = true;
                }
            }
        }
    }
}
