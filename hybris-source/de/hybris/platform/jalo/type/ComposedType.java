package de.hybris.platform.jalo.type;

import com.google.common.collect.ImmutableSet;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloOnlyItem;
import de.hybris.platform.jalo.JaloOnlySingletonItem;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.c2l.Region;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.persistence.security.ACLCache;
import de.hybris.platform.persistence.type.ComposedTypeEJBImpl;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.znerd.xmlenc.LineBreak;
import org.znerd.xmlenc.XMLOutputter;

public class ComposedType extends Type
{
    private static final Logger log = Logger.getLogger(ComposedType.class);
    private volatile transient ComposedType superType = null;
    private volatile transient List<ComposedType> allSuperTypes = null;


    public static <E extends Item> E newInstance(SessionContext ctx, Class jaloClass, Map attributeValues) throws JaloGenericCreationException, JaloAbstractTypeException, JaloItemNotFoundException
    {
        return (E)TypeManager.getInstance().getComposedType(jaloClass).newInstance(ctx, attributeValues);
    }


    public static <E extends Item> E newInstance(SessionContext ctx, Class jaloClass, Object... params) throws JaloGenericCreationException, JaloAbstractTypeException, JaloItemNotFoundException
    {
        Item.ItemAttributeMap attr = null;
        if(params != null)
        {
            if(params.length % 2 != 0)
            {
                throw new JaloInvalidParameterException("illegal attribute value list " + params + " - expected (qualifier,value)*", 0);
            }
            attr = new Item.ItemAttributeMap();
            for(int i = 0; i < params.length; i += 2)
            {
                if(params[i] == null)
                {
                    throw new JaloInvalidParameterException("illegal attribute value list " + params + " - qualifier is null at " + i, 0);
                }
                attr.put(params[i], params[i + 1]);
            }
        }
        return newInstance(ctx, jaloClass, (Map)attr);
    }


    public static <E extends Item> E newInstance(SessionContext ctx, String typeCode, Map attributeValues) throws JaloGenericCreationException, JaloAbstractTypeException, JaloItemNotFoundException
    {
        return (E)TypeManager.getInstance().getComposedType(typeCode).newInstance(ctx, attributeValues);
    }


    public static <E extends Item> E newInstance(SessionContext ctx, String typeCode, Object... params) throws JaloGenericCreationException, JaloAbstractTypeException, JaloItemNotFoundException
    {
        Item.ItemAttributeMap attr = null;
        if(params != null)
        {
            if(params.length % 2 != 0)
            {
                throw new JaloInvalidParameterException("illegal attribute value list " + params + " - expected (qualifier,value)*", 0);
            }
            attr = new Item.ItemAttributeMap();
            for(int i = 0; i < params.length; i += 2)
            {
                if(params[i] == null)
                {
                    throw new JaloInvalidParameterException("illegal attribute value list " + params + " - qualifier is null at " + i, 0);
                }
                attr.put(params[i], params[i + 1]);
            }
        }
        return newInstance(ctx, typeCode, (Map)attr);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute(SUPERTYPE, allAttributes, missing) ? 1 : 0) | (!checkMandatoryAttribute(CODE, allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing parameters " + missing + " - got " + allAttributes, 0);
        }
        return
                        (Item)JaloSession.getCurrentSession()
                                        .getTypeManager()
                                        .createComposedType((PK)allAttributes.get(PK), (ComposedType)allAttributes.get(SUPERTYPE), (String)allAttributes
                                                        .get(CODE));
    }


    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap ret = super.getNonInitialAttributes(ctx, allAttributes);
        ret.remove(PK);
        ret.remove(SUPERTYPE);
        ret.remove(CODE);
        return ret;
    }


    public static final String SUPERTYPE = "superType".intern();
    public static final String SUBTYPES = "subtypes".intern();
    public static final String DECLAREDATTRIBUTEDESCRIPTORS = "declaredattributedescriptors".intern();
    public static final String INHERITEDATTRIBUTEDESCRIPTORS = "inheritedattributedescriptors".intern();
    public static final String ATTRIBUTEDESCRIPTORS = "attributedescriptors".intern();
    public static final String JALOCLASS = "jaloclass".intern();
    protected static final String INHERITANCEPATH = "inheritancePathString".intern();
    public static final String SINGLETON = "singleton".intern();
    public static final String JALOONLY = "jaloonly".intern();
    public static final String DYNAMIC = "dynamic".intern();
    public static final String ABSTRACT = "abstract".intern();
    public static final String TABLE = "table".intern();
    public static final String DUMPPROPERTYTABLE = "dumpPropertyTable".intern();
    public static final String JNDINAME = "jndiName".intern();
    public static final String ALLSUPERTYPES = "allSuperTypes".intern();
    public static final String ALLSUBTYPES = "allSubTypes".intern();


    private DescriptorsCache getDescriptorsCache()
    {
        return (DescriptorsCache)(new Object(this, getTenant().getCache(), 87, "descriptorMap" +
                        getPK().getLongValueAsString()))
                        .getCached();
    }


    public String toString(SessionContext ctx, Object value) throws JaloInvalidParameterException
    {
        String valueStr;
        if(value == null)
        {
            return "<NULL>";
        }
        Class<?> jaloClasss = getJaloClass();
        if(Type.class.isAssignableFrom(jaloClasss))
        {
            valueStr = ((Type)value).getCode();
        }
        else if(Descriptor.class.isAssignableFrom(jaloClasss))
        {
            valueStr = ((AttributeDescriptor)value).getEnclosingType().getCode() + "." + ((AttributeDescriptor)value).getEnclosingType().getCode();
        }
        else if(Language.class.isAssignableFrom(jaloClasss))
        {
            valueStr = ((Language)value).getIsocode(ctx);
        }
        else if(Currency.class.isAssignableFrom(jaloClasss))
        {
            valueStr = ((Currency)value).getIsocode(ctx);
        }
        else if(Country.class.isAssignableFrom(jaloClasss))
        {
            valueStr = ((Country)value).getIsocode(ctx);
        }
        else if(Region.class.isAssignableFrom(jaloClasss))
        {
            valueStr = ((Region)value).getIsocode(ctx);
        }
        else
        {
            valueStr = ((Item)value).getPK().toString();
        }
        return "\"" + AtomicType.escape(valueStr, '"') + "\"";
    }


    public Object parseValue(SessionContext ctx, String value) throws JaloInvalidParameterException
    {
        if(value == null || "<NULL>".equals(value))
        {
            return null;
        }
        int[] range = AtomicType.getEscapedStringPositions(value, '"', 0);
        String valueStr = AtomicType.unescape(value.substring(range[0] + 1, range[1]), '"');
        if("<NULL>".equals(valueStr))
        {
            return null;
        }
        Class<?> jaloClasss = getJaloClass();
        if(Type.class.isAssignableFrom(jaloClasss))
        {
            return TypeManager.getInstance().getType(valueStr);
        }
        if(Descriptor.class.isAssignableFrom(jaloClasss))
        {
            int dot = valueStr.indexOf('.');
            return TypeManager.getInstance().getComposedType(valueStr.substring(0, dot))
                            .getEveryAttributeDescriptor(valueStr.substring(dot + 1));
        }
        if(Language.class.isAssignableFrom(jaloClasss))
        {
            return C2LManager.getInstance().getLanguageByIsoCode(valueStr);
        }
        if(Currency.class.isAssignableFrom(jaloClasss))
        {
            return C2LManager.getInstance().getCurrencyByIsoCode(valueStr);
        }
        if(Country.class.isAssignableFrom(jaloClasss))
        {
            return C2LManager.getInstance().getCountryByIsoCode(valueStr);
        }
        if(Region.class.isAssignableFrom(jaloClasss))
        {
            return C2LManager.getInstance().getRegionByCode(valueStr);
        }
        return getSession().getItem(PK.parse(valueStr));
    }


    public String getTable()
    {
        String table = getComposedTypeImpl().getTable();
        if(table == null && !isAbstract())
        {
            ComposedType superType = getSuperType();
            table = superType.getTable();
        }
        return table;
    }


    public String getDumpPropertyTable()
    {
        String table = getComposedTypeImpl().getDumpPropertyTable();
        if(table == null && !isAbstract())
        {
            ComposedType superType = getSuperType();
            table = superType.getDumpPropertyTable();
        }
        return table;
    }


    public String getJNDIName()
    {
        return getComposedTypeImpl().getJNDIName();
    }


    public int getItemTypeCode()
    {
        return getComposedTypeImpl().getItemTypeCode();
    }


    public Set<Integer> getAllConcreteItemTypeCodes()
    {
        Set<Integer> ret = new HashSet();
        if(!isAbstract())
        {
            ret.add(Integer.valueOf(getItemTypeCode()));
        }
        for(Iterator<ComposedType> it = getAllSubTypes().iterator(); it.hasNext(); )
        {
            ComposedType subType = it.next();
            if(!subType.isAbstract())
            {
                ret.add(Integer.valueOf(subType.getItemTypeCode()));
            }
        }
        return ret;
    }


    public Class getJaloClass()
    {
        Class cl = getDeclaredJaloClass();
        if(cl == null)
        {
            for(ComposedType superType = getSuperType(); cl == null && superType != null; superType = superType.getSuperType())
            {
                cl = superType.getJaloClass();
            }
        }
        return cl;
    }


    public Class getDeclaredJaloClass()
    {
        return (Class)(new Object(this, JALOCLASS))
                        .get(null);
    }


    public void setJaloClass(Class newOne)
    {
        (new Object(this, JALOCLASS, newOne))
                        .set(null);
    }


    public Set<AttributeDescriptor> getInheritedAttributeDescriptors()
    {
        return getDescriptorsCache().getInheritedDescriptors();
    }


    public List<ComposedType> getAllSuperTypes()
    {
        if(this.allSuperTypes == null)
        {
            List<ComposedType> ret = null;
            for(ComposedType st = getSuperType(); st != null; st = st.getSuperType())
            {
                if(ret == null)
                {
                    ret = new ArrayList();
                }
                ret.add(st);
            }
            this.allSuperTypes = (ret != null) ? ret : Collections.EMPTY_LIST;
        }
        return this.allSuperTypes;
    }


    public ComposedType getSuperType()
    {
        if(this.superType == null)
        {
            this.superType = getComposedTypeImpl().getSuperType();
        }
        return this.superType;
    }


    protected String getInheritancePathString()
    {
        return getComposedTypeImpl().getInheritancePathString();
    }


    public Set<ComposedType> getSubTypes()
    {
        return getComposedTypeImpl().getSubTypes();
    }


    public Set<ComposedType> getAllSubTypes()
    {
        return getComposedTypeImpl().getAllSubTypes();
    }


    public Set getInitialAttributeDescriptors()
    {
        return getDescriptorsCache().getInitialDescriptors();
    }


    protected void _sortAttributesByQualifier(List<AttributeDescriptor> lst)
    {
        Collections.sort(lst, (Comparator<? super AttributeDescriptor>)new Object(this));
    }


    public Set getDeclaredAttributeDescriptors()
    {
        return getDescriptorsCache().getDeclaredDescriptors();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setDeclaredAttributeDescriptors(Set fds) throws JaloInvalidParameterException
    {
        getComposedTypeImpl().setDeclaredAttributeDescriptors(fds);
    }


    public Set<AttributeDescriptor> getAttributeDescriptors()
    {
        return getDescriptorsCache().getNonPrivateDescriptors();
    }


    public Set<AttributeDescriptor> getPartOfAutoRemovalAttributeDescriptors()
    {
        return getDescriptorsCache().getPartOfDescriptors();
    }


    public Set<AttributeDescriptor> getAttributeDescriptorsIncludingPrivate()
    {
        return (Set<AttributeDescriptor>)ImmutableSet.copyOf(getDescriptorsCache().getDescriptorMap().values());
    }


    public Set<AttributeDescriptor> getAttributeDescriptorsIncludingPrivate(Collection<String> qualifiers)
    {
        Set<AttributeDescriptor> ret = new LinkedHashSet<>(qualifiers.size());
        Map<String, AttributeDescriptor> adMap = getDescriptorsCache().getDescriptorMap();
        for(String q : qualifiers)
        {
            AttributeDescriptor ad = adMap.get(q.toLowerCase(LocaleHelper.getPersistenceLocale()));
            if(ad == null)
            {
                throw new JaloInvalidParameterException("type " + getCode() + " got no attribute " + q, 0);
            }
            ret.add(ad);
        }
        return ret;
    }


    public AttributeDescriptor getAttributeDescriptorIncludingPrivate(String qualifier) throws JaloItemNotFoundException
    {
        AttributeDescriptor ret = (AttributeDescriptor)getDescriptorsCache().getDescriptorMap().get(qualifier.toLowerCase(LocaleHelper.getPersistenceLocale()));
        if(ret == null)
        {
            throw new JaloItemNotFoundException("no attribute " + getCode() + "." + qualifier + " found", 0);
        }
        return ret;
    }


    public AttributeDescriptor getDeclaredAttributeDescriptor(String qualifier) throws JaloItemNotFoundException
    {
        AttributeDescriptor ret = (AttributeDescriptor)getDescriptorsCache().getDescriptorMap().get(qualifier.toLowerCase(LocaleHelper.getPersistenceLocale()));
        if(ret != null && !ret.isInherited())
        {
            return ret;
        }
        throw new JaloItemNotFoundException("no declared attribute " + getCode() + "." + qualifier + " found", 0);
    }


    public AttributeDescriptor getAttributeDescriptor(String qualifier) throws JaloItemNotFoundException
    {
        AttributeDescriptor ret = (AttributeDescriptor)getDescriptorsCache().getDescriptorMap().get(qualifier.toLowerCase(LocaleHelper.getPersistenceLocale()));
        if(ret != null && !ret.isPrivate())
        {
            return ret;
        }
        throw new JaloItemNotFoundException("no public attribute " + getCode() + "." + qualifier + " found", 0);
    }


    public boolean hasAttribute(String qualifier) throws JaloItemNotFoundException
    {
        AttributeDescriptor desc = (AttributeDescriptor)getDescriptorsCache().getDescriptorMap().get(qualifier.toLowerCase(LocaleHelper.getPersistenceLocale()));
        return (desc != null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public AttributeDescriptor getEveryAttributeDescriptor(String qualifier) throws JaloItemNotFoundException
    {
        return getAttributeDescriptorIncludingPrivate(qualifier);
    }


    public Set<RelationType> getRelations()
    {
        return getDescriptorsCache().getRelations();
    }


    public AttributeDescriptor createAttributeDescriptor(String qualifier, Type valueType, int modifiers) throws JaloDuplicateQualifierException, JaloInvalidParameterException
    {
        return createAttributeDescriptor(null, qualifier, valueType, modifiers);
    }


    public AttributeDescriptor createAttributeDescriptor(PK pk, String qualifier, Type valueType, int modifiers) throws JaloDuplicateQualifierException, JaloInvalidParameterException
    {
        return getComposedTypeImpl().createAttributeDescriptor(pk, qualifier, valueType, modifiers);
    }


    public Item newInstance(Map attributeAssignment) throws JaloGenericCreationException, JaloAbstractTypeException
    {
        return newInstance(getSession().getSessionContext(), attributeAssignment);
    }


    public Item newInstance(SessionContext ctx, Map attributeAssignment) throws JaloGenericCreationException, JaloAbstractTypeException
    {
        if(isAbstract())
        {
            throw new JaloAbstractTypeException(this);
        }
        if(isJaloOnly())
        {
            return createJaloOnlyInstance(ctx, attributeAssignment);
        }
        Class<? extends Item> jaloClass = getJaloClass();
        if(jaloClass == null)
        {
            throw new JaloGenericCreationException("type " + this + " did not provide jalo class - cannot create", 0);
        }
        try
        {
            return getCreatorInstance(jaloClass).newInstance(ctx, this, attributeAssignment);
        }
        catch(Exception e)
        {
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            if(e instanceof JaloGenericCreationException)
            {
                throw (JaloGenericCreationException)e;
            }
            if(e instanceof JaloAbstractTypeException)
            {
                throw (JaloAbstractTypeException)e;
            }
            throw new JaloGenericCreationException(e, 0);
        }
    }


    protected Item getCreatorInstance(Class<? extends Item> jaloClass) throws JaloGenericCreationException
    {
        ApplicationContext actx = Registry.getGlobalApplicationContext();
        String name = jaloClass.getName();
        if(actx.containsBean(name))
        {
            return (Item)actx.getBean(name);
        }
        try
        {
            return jaloClass.newInstance();
        }
        catch(InstantiationException e)
        {
            throw new JaloGenericCreationException(e, 0);
        }
        catch(IllegalAccessException e)
        {
            throw new JaloGenericCreationException(e, 0);
        }
    }


    public Map getAllDefaultValues()
    {
        return getAllDefaultValues(getSession().getSessionContext());
    }


    public Map getAllDefaultValues(SessionContext ctx)
    {
        boolean inCreate = (ctx != null && Boolean.TRUE.equals(ctx.getAttribute("core.types.creation.initial")));
        DescriptorsCache dc = getDescriptorsCache();
        if(dc.hasNoDefaultValues(inCreate))
        {
            return Collections.EMPTY_MAP;
        }
        Map<String, Object> ret = null;
        for(AttributeDescriptor fd : dc.getDefaultValueAttributes(inCreate))
        {
            Object value = fd.getDefaultValue(ctx);
            if(value != null)
            {
                if(ret == null)
                {
                    ret = new HashMap<>();
                }
                ret.put(fd.getQualifier(), value);
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_MAP;
    }


    public Map getDefaultValues(Set fdCodes) throws JaloItemNotFoundException
    {
        return getDefaultValues(getSession().getSessionContext(), fdCodes);
    }


    public Map getDefaultValues(SessionContext ctx, Set fdCodes) throws JaloItemNotFoundException, JaloInvalidParameterException
    {
        Item.ItemAttributeMap<String, Object> itemAttributeMap = new Item.ItemAttributeMap();
        boolean inCreate = (ctx != null && Boolean.TRUE.equals(ctx.getAttribute("core.types.creation.initial")));
        for(Iterator<String> it = fdCodes.iterator(); it.hasNext(); )
        {
            AttributeDescriptor fd = getAttributeDescriptorIncludingPrivate(it.next());
            if(fd.isWritable() || (inCreate && fd.isInitial()))
            {
                Object value = fd.getDefaultValue(ctx);
                if(value != null)
                {
                    itemAttributeMap.put(fd.getQualifier(), value);
                }
            }
        }
        return (Map)itemAttributeMap;
    }


    public Set getAllInstances()
    {
        return getAllInstances(getSession().getSessionContext());
    }


    protected Item createJaloOnlyInstance(SessionContext ctx, Map attributes)
    {
        try
        {
            Class<JaloOnlyItem> cl = getJaloClass();
            JaloOnlyItem creationDummyItem = cl.newInstance();
            Item item = ((Item)creationDummyItem).newInstance(ctx, this, attributes);
            item.setTenant(getTenant());
            return item;
        }
        catch(IllegalAccessException e)
        {
            throw new JaloSystemException(e);
        }
        catch(InstantiationException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public Set getAllInstances(SessionContext ctx)
    {
        if(isJaloOnly())
        {
            return isSingleton() ? Collections.<Item>singleton(
                            createJaloOnlyInstance(ctx, Collections.EMPTY_MAP)) : Collections.EMPTY_SET;
        }
        String query = "GET {" + getCode() + "} ORDER BY {" + Item.CREATION_TIME + "} DESC, {" + Item.PK + "} ASC";
        return new HashSet(getSession().getFlexibleSearch()
                        .search(ctx, query, Collections.EMPTY_MAP, Collections.singletonList(getJaloClass()), true, true, 0, -1)
                        .getResult());
    }


    public long getAllInstancesCount()
    {
        if(isSingleton() && isJaloOnly())
        {
            return 1L;
        }
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT({").append(Item.PK).append("}) FROM {").append(getCode()).append("}");
        List list = getSession().getFlexibleSearch().search(query.toString(), Collections.EMPTY_MAP, Collections.singletonList(Long.class), false, true, 0, -1).getResult();
        if(list.isEmpty())
        {
            return 0L;
        }
        long l = 0L;
        for(Iterator<Long> it = list.iterator(); it.hasNext(); )
        {
            l += ((Long)it.next()).longValue();
        }
        return l;
    }


    public boolean isSingleton()
    {
        return ((Boolean)(new Object(this, SINGLETON))
                        .get(null)).booleanValue();
    }


    public boolean isJaloOnly()
    {
        boolean b = Boolean.TRUE.equals(getProperty(JALOONLY));
        if(!b && JaloOnlyItem.class.isAssignableFrom(getJaloClass()))
        {
            setProperty(JALOONLY, Boolean.TRUE);
            b = true;
        }
        return b;
    }


    public boolean isDynamic()
    {
        return Boolean.TRUE.equals(getProperty(DYNAMIC));
    }


    public boolean isAbstract()
    {
        return ((Boolean)(new Object(this, ABSTRACT))
                        .get(null)).booleanValue();
    }


    protected boolean isJaloClassAbstract()
    {
        Class c = getJaloClass();
        return (c != null && (c.getModifiers() & 0x400) != 0);
    }


    public void setSingleton(boolean isSingleton) throws JaloInvalidParameterException
    {
        if(isSingleton() != isSingleton)
        {
            try
            {
                (new Object(this, SINGLETON, isSingleton))
                                .set(null);
            }
            catch(de.hybris.platform.jalo.Item.JaloCachedComputationException e)
            {
                Throwable cause = e.getCause();
                if(cause instanceof JaloInvalidParameterException)
                {
                    throw (JaloInvalidParameterException)cause;
                }
                if(cause instanceof RuntimeException)
                {
                    throw (RuntimeException)cause;
                }
                throw new JaloSystemException(cause);
            }
        }
    }


    public void setJaloOnly(boolean isJaloOnly) throws JaloInvalidParameterException
    {
        Class<?> jaloClass = getJaloClass();
        if(isJaloOnly && (jaloClass == null || !JaloOnlySingletonItem.class.isAssignableFrom(jaloClass)))
        {
            throw new JaloInvalidParameterException("cannot mark type " + this + " as jalo-only singleton since the jalo class (" + jaloClass + ") does not implement the interface " + JaloOnlySingletonItem.class
                            .getName(), 0);
        }
        setProperty(JALOONLY, isJaloOnly ? Boolean.TRUE : Boolean.FALSE);
    }


    protected Map getXMLCustomProperties(String forExtension)
    {
        return Collections.EMPTY_MAP;
    }


    public String getXMLDefinition()
    {
        return getXMLDefinition(getExtensionName());
    }


    public String getXMLDefinition(String forExtension)
    {
        XMLOutputter xmlOut;
        try
        {
            xmlOut = new XMLOutputter(new StringWriter(), "UTF-8");
            xmlOut.setEscaping(true);
            xmlOut.setLineBreak(LineBreak.UNIX);
            xmlOut.setIndentation("\t");
            xmlOut.setQuotationMark('"');
        }
        catch(UnsupportedEncodingException e)
        {
            throw new JaloSystemException(e);
        }
        return exportXMLDefinition(xmlOut, forExtension);
    }


    public String exportXMLDefinition(XMLOutputter xout)
    {
        return exportXMLDefinition(xout, getExtensionName());
    }


    public String exportXMLDefinition(XMLOutputter xout, String forExtension)
    {
        try
        {
            String ownExtensionName = getExtensionName();
            boolean justExtensionAttributes = (forExtension != null && ownExtensionName != forExtension && (ownExtensionName == null || !ownExtensionName.equals(forExtension)));
            ComposedType supertype = getSuperType();
            xout.startTag("itemtype");
            xout.attribute("code", getCode());
            if(!justExtensionAttributes)
            {
                ItemDeployment depl = ((ComposedTypeRemote)((ComposedTypeEJBImpl)getImplementation()).getRemote()).getDeployment();
                boolean showDeployment = hasOwnDeployment();
                xout.attribute("extends", (supertype != null) ? supertype.getCode() : "");
                xout.attribute("jaloclass", (getJaloClass() != null) ? getJaloClass().getName() : "");
                xout.attribute("generate", String.valueOf(isGenerate()));
                xout.attribute("singleton", String.valueOf(isSingleton()));
                xout.attribute("jaloonly", String.valueOf(isJaloOnly()));
                if(showDeployment && !depl.isGeneric())
                {
                    xout.attribute("deployment", (getJNDIName() != null) ? getJNDIName() : "");
                }
                if(!ComposedType.class.equals(getClass()))
                {
                    xout.attribute("metatype", getComposedType().getCode());
                }
                xout.attribute("autocreate", String.valueOf(isAutocreate()));
                if(showDeployment && depl.isGeneric())
                {
                    xout.startTag("deployment");
                    xout.attribute("table", depl.getDatabaseTableName());
                    xout.attribute("typecode", Integer.toString(depl.getTypeCode()));
                    xout.endTag();
                }
                Map custProps = getXMLCustomProperties(forExtension);
                if(custProps != null && !custProps.isEmpty())
                {
                    xout.startTag("custom-properties");
                    for(Iterator<Map.Entry> it = custProps.entrySet().iterator(); it.hasNext(); )
                    {
                        Map.Entry e = it.next();
                        xout.startTag("property");
                        xout.attribute("name", (String)e.getKey());
                        xout.startTag("value");
                        xout.pcdata((String)e.getValue());
                        xout.endTag();
                        xout.endTag();
                    }
                    xout.endTag();
                }
            }
            Collection<AttributeDescriptor> exportFeatures = new ArrayList();
            for(Iterator<AttributeDescriptor> iter = getAttributeDescriptorsIncludingPrivate().iterator(); iter.hasNext(); )
            {
                AttributeDescriptor ad = iter.next();
                if(justExtensionAttributes && forExtension.equals(ad.getExtensionName()) && !(ad instanceof RelationDescriptor) && (
                                !ad.isInherited() || ad.isRedeclared()))
                {
                    exportFeatures.add(ad);
                    continue;
                }
                if(!justExtensionAttributes && !(ad instanceof RelationDescriptor) && (!ad.isInherited() || ad.isRedeclared()))
                {
                    exportFeatures.add(ad);
                }
            }
            if(!exportFeatures.isEmpty())
            {
                xout.startTag("attributes");
                for(Iterator<AttributeDescriptor> it = exportFeatures.iterator(); it.hasNext(); )
                {
                    ((AttributeDescriptor)it.next()).exportXMLDefinition(xout);
                }
                xout.endTag();
            }
            xout.endTag();
        }
        catch(IOException e)
        {
            throw new JaloSystemException(e);
        }
        return xout.getWriter().toString();
    }


    public final boolean hasOwnDeployment()
    {
        ComposedType supertype = getSuperType();
        ItemDeployment superDepl = (supertype != null) ? ((ComposedTypeRemote)((ComposedTypeEJBImpl)supertype.getImplementation()).getRemote()).getDeployment() : null;
        ItemDeployment depl = ((ComposedTypeRemote)((ComposedTypeEJBImpl)getImplementation()).getRemote()).getDeployment();
        return !Objects.equals(superDepl, depl);
    }


    protected ComposedTypeImpl getComposedTypeImpl()
    {
        return (ComposedTypeImpl)this.impl;
    }


    public boolean isAssignableFrom(Type type)
    {
        return (type instanceof ComposedType && (
                        equals(type) || (getJaloClass().isAssignableFrom(((ComposedType)type).getJaloClass()) && isSameOrSuperTypeOf((ComposedType)type))));
    }


    protected boolean isSameOrSuperTypeOf(ComposedType type)
    {
        if(type == null)
        {
            return false;
        }
        if(equals(type))
        {
            return true;
        }
        for(ComposedType superType = type.getSuperType(); superType != null; superType = superType.getSuperType())
        {
            if(equals(superType))
            {
                return true;
            }
        }
        return false;
    }


    public boolean isInstance(Object o)
    {
        return (o instanceof Item && isAssignableFrom(((Item)o).getComposedType()));
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        Type superType = getSuperType();
        StringBuilder sb = new StringBuilder();
        sb.append("Composed Type '").append(getCode()).append("'[").append(getPK().getLongValueAsString()).append("]");
        sb.append((superType != null) ? (" extends " + superType.getCode()) : "").append(" (")
                        .append(getDeclaredAttributeDescriptors()).append(")");
        return sb.toString();
    }


    @Deprecated(since = "5.0.1", forRemoval = false)
    protected boolean setTypeSearchSessionContex()
    {
        SessionContext ctx = getSession().getSessionContext();
        if(!Boolean.TRUE.equals(ctx.getAttribute("disableRestrictions")))
        {
            ctx = getSession().createLocalSessionContext();
            ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            return true;
        }
        return false;
    }


    @Deprecated(since = "5.0.1", forRemoval = false)
    protected void unsetTypeSearchContext(boolean useLocal)
    {
        if(useLocal)
        {
            getSession().removeLocalSessionContext();
        }
    }


    public boolean checkTypePermission(UserRight right)
    {
        return checkTypePermission((Principal)getSession().getSessionContext().getUser(), right);
    }


    public boolean checkTypePermission(Principal p, UserRight right)
    {
        if(p.isAdmin())
        {
            return true;
        }
        int match = checkItemPermission(p, right);
        if(match == -1)
        {
            for(ComposedType superType = getSuperType(); match == -1 && superType != null;
                            superType = superType.getSuperType())
            {
                match = superType.checkItemPermission(p, right);
            }
        }
        return ACLCache.translatePermissionToBoolean(match);
    }


    protected int checkItemPermission(Principal principal, UserRight right)
    {
        return super.checkItemPermission(principal, right);
    }


    public Item getSingletonInstance()
    {
        if(!isSingleton())
        {
            throw new JaloInvalidParameterException("ComposedType " + getCode() + " is no singleton type", 0);
        }
        Set<Item> items = getAllInstances();
        switch(items.size())
        {
            case 0:
                try
                {
                    ret = newInstance(Collections.EMPTY_MAP);
                }
                catch(JaloGenericCreationException e)
                {
                    throw new JaloSystemException(e);
                }
                catch(JaloAbstractTypeException e)
                {
                    throw new JaloSystemException(e);
                }
                return ret;
            case 1:
                ret = items.iterator().next();
                return ret;
        }
        log.error("Singleton type " + getCode() + " has more than one instance!!");
        Item ret = items.iterator().next();
        return ret;
    }
}
