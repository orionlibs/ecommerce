package de.hybris.platform.persistence.type;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.RelationDescriptor;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.ItemHome;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.ManagerEJB;
import de.hybris.platform.persistence.property.EJBPropertyContainer;
import de.hybris.platform.persistence.property.OldPropertyJDBC;
import de.hybris.platform.spring.CGLibUtils;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.ItemPropertyValueCollection;
import de.hybris.platform.util.SingletonCreator;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.jeeapi.YCreateException;
import de.hybris.platform.util.jeeapi.YFinderException;
import de.hybris.platform.util.jeeapi.YObjectNotFoundException;
import de.hybris.platform.util.jeeapi.YRemoveException;
import de.hybris.platform.util.typesystem.PlatformStringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.log4j.Logger;

public class TypeManagerEJB extends ManagerEJB
{
    static int ATOMIC_TYPE = 1;
    static int COMPOSED_TYPE = ATOMIC_TYPE << 1;
    static int COLLECTION_TYPE = COMPOSED_TYPE << 1;
    static int MAP_TYPE = COLLECTION_TYPE << 1;
    static int ALL = ATOMIC_TYPE | COMPOSED_TYPE | COLLECTION_TYPE | MAP_TYPE;
    private static final Logger log = Logger.getLogger(TypeManagerEJB.class);
    private static final Map<String, Set<String>> KNOWN_REDECLARED_RELATION_ATTRIBUTES = Maps.newHashMap();
    private static final int ejbErrorCode = 4711;
    private static final int NON_SETTABLE = 1828;
    private static final int NON_REDECLARABLE = 8192;

    static
    {
        KNOWN_REDECLARED_RELATION_ATTRIBUTES.put("country", Sets.newHashSet((Object[])new String[] {GeneratedCoreConstants.TC.REGION}));
        KNOWN_REDECLARED_RELATION_ATTRIBUTES.put("user",
                        Sets.newHashSet((Object[])new String[] {GeneratedCoreConstants.TC.CART, GeneratedCoreConstants.TC.ORDER, "GlobalDiscountRow", GeneratedCoreConstants.TC.QUOTE}));
        KNOWN_REDECLARED_RELATION_ATTRIBUTES.put("product", Sets.newHashSet((Object[])new String[] {"DiscountRow", "PriceRow", "TaxRow"}));
    }

    private final Map<Integer, ComposedTypeRemote> getCompTypeCache()
    {
        return (Map<Integer, ComposedTypeRemote>)getPersistencePool().getCache().getStaticCacheContent((SingletonCreator.Creator)new Object(this));
    }


    public int getTypecode()
    {
        return 80;
    }


    public boolean isLocalizationType(TypeRemote type, List context)
    {
        if(type instanceof MapTypeRemote)
        {
            MapTypeRemote mt = (MapTypeRemote)type;
            int i = 0;
            int s = (context != null) ? context.size() : 0;
            if(i < s)
            {
                TypeRemote retType = mt.getReturnType();
                while(i < s && retType instanceof MapTypeRemote)
                {
                    mt = (MapTypeRemote)retType;
                    i++;
                }
            }
            if(i == s)
            {
                TypeRemote argType = mt.getArgumentType();
                return (argType instanceof ComposedTypeRemote && 32 == ((ComposedTypeRemote)argType)
                                .getItemTypeCode());
            }
        }
        return false;
    }


    public void changeItemType(ItemRemote item, ComposedTypeRemote newType) throws EJBInvalidParameterException
    {
        if(item instanceof AttributeDescriptorRemote)
        {
            if(((AttributeDescriptorRemote)item).isInherited())
            {
                throw new EJBInvalidParameterException(null, "cannot change inherited feature type - change declared feature instead", 0);
            }
            item.setComposedType(newType);
            for(Iterator<? extends AttributeDescriptorRemote> it = getAllSubAttributeDescriptors((AttributeDescriptorRemote)item).iterator(); it.hasNext(); )
            {
                ((AttributeDescriptorRemote)it.next()).setComposedType(newType);
            }
        }
        else if(item instanceof HierarchieTypeRemote)
        {
            item.setComposedType(newType);
            for(Iterator<HierarchieTypeRemote> it = getSubTypesInternal((HierarchieTypeRemote)item, true).iterator(); it.hasNext(); )
            {
                ((HierarchieTypeRemote)it.next()).setComposedType(newType);
            }
        }
        else
        {
            item.setComposedType(newType);
        }
    }


    public Collection getAllItemTypes()
    {
        try
        {
            return getComposedTypeHome().findAll();
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public Collection getAllCollectionTypes()
    {
        try
        {
            return getCollectionTypeHome().findAll();
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public Collection getAllMapTypes()
    {
        try
        {
            return getMapTypeHome().findAll();
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public Collection getAllAtomicTypes()
    {
        try
        {
            return getAtomicTypeHome().findAll();
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public TypeRemote getType(String code) throws EJBItemNotFoundException
    {
        return getType(code, ALL);
    }


    public TypeRemote getType(String code, int typePattern) throws EJBItemNotFoundException
    {
        TypeRemote type = null;
        if((typePattern & ATOMIC_TYPE) != 0)
        {
            type = findByCodeExact((TypeHome)getAtomicTypeHome(), code);
        }
        if(type == null && (typePattern & COMPOSED_TYPE) != 0)
        {
            type = findByCodeExact((TypeHome)getComposedTypeHome(), code);
        }
        if(type == null && (typePattern & COLLECTION_TYPE) != 0)
        {
            type = findByCodeExact((TypeHome)getCollectionTypeHome(), code);
        }
        if(type == null && (typePattern & MAP_TYPE) != 0)
        {
            type = findByCodeExact((TypeHome)getMapTypeHome(), code);
        }
        if(type != null)
        {
            return type;
        }
        throw new EJBItemNotFoundException(null, "type with code " + code + " not found", 4711);
    }


    private TypeRemote findByCodeExact(TypeHome typeHome, String code)
    {
        try
        {
            Collection<TypeRemote> coll = typeHome.findByCodeExact((code != null) ? PlatformStringUtils.toLowerCaseCached(code) : null);
            if(coll.isEmpty())
            {
                return null;
            }
            if(coll.size() == 1)
            {
                return coll.iterator().next();
            }
            throw new EJBInternalException(null, "found multiple types for exact code '" + code + "'", 0);
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public AtomicTypeRemote getOrCreateAtomicType(PK pkBase, Class javaClass) throws EJBInvalidParameterException
    {
        Set<AtomicTypeRemote> atomicTypes = getAtomicTypesForJavaClass(javaClass);
        if(atomicTypes.isEmpty())
        {
            try
            {
                return createAtomicType(pkBase, javaClass);
            }
            catch(EJBDuplicateCodeException e)
            {
                log.debug(e);
                throw new JaloSystemException(null, "!!", 4711);
            }
        }
        AtomicTypeRemote at = atomicTypes.iterator().next();
        while(at.getSuperType() != null && atomicTypes.contains(at.getSuperType()))
        {
            at = at.getSuperType();
        }
        return at;
    }


    private ComposedTypeRemote getRootFast(Collection composedTypes, String errorText) throws EJBItemNotFoundException
    {
        ComposedTypeRemote ret = null;
        int currentCount = -1;
        for(Iterator<ComposedTypeRemote> it = composedTypes.iterator(); it.hasNext(); )
        {
            ComposedTypeRemote type = it.next();
            int myCount = countPKs(type.getInheritancePathString());
            if(currentCount == -1)
            {
                currentCount = myCount;
                ret = type;
                continue;
            }
            if(currentCount > myCount)
            {
                currentCount = myCount;
                ret = type;
            }
        }
        if(ret == null)
        {
            throw new EJBItemNotFoundException(null, "no root composed type found for " + errorText, 0);
        }
        return ret;
    }


    private static final int countPKs(String pkCollectionString)
    {
        if(pkCollectionString == null)
        {
            return 99999;
        }
        int count = 0;
        for(int pos = pkCollectionString.indexOf(','); pos >= 0; pos = pkCollectionString.indexOf(',', pos + 1))
        {
            count++;
        }
        return count - 1;
    }


    public ComposedTypeRemote getRootComposedTypeForJaloClass(String jaloClassName) throws EJBItemNotFoundException
    {
        try
        {
            int currentCount;
            Iterator<ComposedTypeRemote> it;
            Collection<ComposedTypeRemote> coll = getComposedTypeHome().findByJaloClassName(jaloClassName);
            ComposedTypeRemote ret = null;
            switch(coll.size())
            {
                case 0:
                    break;
                case 1:
                    ret = coll.iterator().next();
                    break;
                default:
                    currentCount = -1;
                    for(it = coll.iterator(); it.hasNext(); )
                    {
                        ComposedTypeRemote type = it.next();
                        String path = type.getInheritancePathString();
                        int myCount = countPKs(path);
                        if(currentCount == -1)
                        {
                            currentCount = myCount;
                            ret = type;
                            continue;
                        }
                        if(currentCount > myCount)
                        {
                            currentCount = myCount;
                            ret = type;
                        }
                    }
                    break;
            }
            if(ret == null)
            {
                throw new EJBItemNotFoundException(null, "could not find any composed type having jalo class '" + jaloClassName + "'", 0);
            }
            return ret;
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public ComposedTypeRemote getRootComposedType(int typecode) throws EJBItemNotFoundException
    {
        ComposedTypeRemote result = Config.getBoolean("core.ejb.enableremotecache", true) ? getCompTypeCache().get(Integer.valueOf(typecode)) : null;
        if(result == null)
        {
            try
            {
                result = getRootFast(getComposedTypeHome().findByTypeCode(typecode), "typecode " + typecode);
                getCompTypeCache().put(Integer.valueOf(typecode), result);
            }
            catch(YFinderException e)
            {
                throw new JaloSystemException(e, "!!", 4711);
            }
        }
        return result;
    }


    public ComposedTypeRemote getComposedType(String code) throws EJBItemNotFoundException
    {
        ComposedTypeRemote result = (ComposedTypeRemote)findByCodeExact((TypeHome)
                        getComposedTypeHome(), code);
        if(result != null)
        {
            return result;
        }
        throw new EJBItemNotFoundException(null, "no composed type with code " + code + " found.", 4711);
    }


    public AtomicTypeRemote getRootAtomicType(Class javaClass) throws EJBItemNotFoundException
    {
        return (AtomicTypeRemote)getType(CGLibUtils.getOriginalClass(javaClass).getName(), ATOMIC_TYPE);
    }


    public String getProposedDatabaseColumn(AttributeDescriptorRemote feature)
    {
        if(feature.dontOptimize())
        {
            return null;
        }
        if(feature.isProperty())
        {
            return "p_" + feature.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()).replace('.', '_').replace(' ', '_').trim();
        }
        ComposedTypeRemote type = feature.getEnclosingType();
        ItemDeployment deployment = type.getDeployment();
        if(deployment == null)
        {
            return null;
        }
        String pq = feature.getPersistenceQualifier();
        ItemDeployment.Attribute attribute = (pq != null) ? deployment.getAttribute(pq) : null;
        return (attribute != null) ? attribute.getColumnName(Config.getDatabase()) : null;
    }


    public AtomicTypeRemote createAtomicType(PK pkBase, Class javaClass) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        try
        {
            return getAtomicTypeHome().create(pkBase, javaClass);
        }
        catch(YCreateException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
    }


    public AtomicTypeRemote createAtomicType(PK pkBase, AtomicTypeRemote superType, Class javaClass) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        try
        {
            return getAtomicTypeHome().create(pkBase, superType, javaClass);
        }
        catch(YCreateException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
    }


    public AtomicTypeRemote createAtomicType(PK pkBase, AtomicTypeRemote superType, String code) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        try
        {
            return getAtomicTypeHome().create(pkBase, superType, code);
        }
        catch(YCreateException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
    }


    public ComposedTypeRemote createNonRootComposedType(PK pkBase, ComposedTypeRemote superType, String code, String jaloClassName, ComposedTypeRemote metaType, boolean copySupertypeData) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        try
        {
            if(superType == null)
            {
                throw new EJBInvalidParameterException(null, "supertype must not be null for non-root composed types", 0);
            }
            if(code == null)
            {
                throw new EJBInvalidParameterException(null, "code must not be null", 0);
            }
            ComposedTypeRemote newOne = getComposedTypeHome().create(pkBase, superType, code, jaloClassName, null, metaType);
            if(copySupertypeData)
            {
                copySuperTypeData((HierarchieTypeRemote)newOne, (HierarchieTypeRemote)superType);
            }
            return newOne;
        }
        catch(YCreateException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
    }


    public ComposedTypeRemote createRootComposedType(PK pkBase, ComposedTypeRemote superType, String code, String jaloClassName, ItemDeployment deployment, ComposedTypeRemote metaType, boolean copySupertypeData) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        try
        {
            if(deployment == null)
            {
                throw new EJBInvalidParameterException(null, "deployment must not be null for root composed types", 0);
            }
            ComposedTypeRemote newOne = getComposedTypeHome().create(pkBase, superType, code, jaloClassName, deployment, metaType);
            if(copySupertypeData && superType != null)
            {
                copySuperTypeData((HierarchieTypeRemote)newOne, (HierarchieTypeRemote)superType);
            }
            return newOne;
        }
        catch(YCreateException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public CollectionTypeRemote getOrCreateCollectionType(PK pkBase, String code, TypeRemote elementType) throws EJBInvalidParameterException
    {
        return getOrCreateCollectionType(pkBase, code, elementType, 0);
    }


    public CollectionTypeRemote getOrCreateCollectionType(PK pkBase, String code, TypeRemote elementType, int collectionType) throws EJBInvalidParameterException
    {
        try
        {
            Collection<CollectionTypeRemote> types = getCollectionTypeHome().findByCodeExact((code != null) ? code.toLowerCase(LocaleHelper.getPersistenceLocale()) : null);
            if(!types.isEmpty())
            {
                return types.iterator().next();
            }
            return createCollectionType(pkBase, (code != null) ? code : (elementType.getCode() + "Collection"), elementType, collectionType);
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
        catch(EJBDuplicateCodeException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
    }


    protected MapTypeRemote getOrCreateMapType(PK pkBase, String code, TypeRemote argumentType, TypeRemote returnType) throws EJBInvalidParameterException
    {
        try
        {
            if(argumentType == null || returnType == null)
            {
                throw new EJBInvalidParameterException(null, "illegal arguments for getOrCreateMapType (argumentType=" +
                                TypeTools.asString(argumentType) + ", returnType=" + TypeTools.asString(returnType) + " )", 0);
            }
            Collection<MapTypeRemote> types = getMapTypeHome().findByTypes(EJBTools.getPK((ItemRemote)argumentType), EJBTools.getPK((ItemRemote)returnType));
            if(!types.isEmpty())
            {
                return types.iterator().next();
            }
            return createMapType(pkBase,
                            (code != null) ? code : (argumentType.getCode() + "to" + argumentType.getCode() + "MapType"), argumentType, returnType);
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
        catch(EJBDuplicateCodeException e)
        {
            throw new EJBInvalidParameterException(e, e.getMessage(), e.getErrorCode());
        }
    }


    public CollectionTypeRemote createCollectionType(PK pkBase, String code, TypeRemote elementType) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        return createCollectionType(pkBase, code, elementType, 0);
    }


    public CollectionTypeRemote createCollectionType(PK pkBase, String code, TypeRemote elementType, int collectionType) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        try
        {
            return getCollectionTypeHome().create(pkBase, code, elementType, collectionType);
        }
        catch(YCreateException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
    }


    public MapTypeRemote createMapType(PK pkBase, String code, TypeRemote argumentType, TypeRemote returnType) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        try
        {
            return getMapTypeHome().create(pkBase, code, argumentType, returnType);
        }
        catch(YCreateException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
    }


    public Collection getMapTypesByArgumentType(TypeRemote argumentType)
    {
        try
        {
            return getMapTypeHome().findByArgumentType(EJBTools.getPK((ItemRemote)argumentType));
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public Set getAtomicTypesForJavaClass(Class javaClass)
    {
        try
        {
            Collection<?> remotes = getAtomicTypeHome().findByJavaClass(
                            CGLibUtils.getOriginalClass(javaClass).getName());
            return (remotes != null && !remotes.isEmpty()) ? new HashSet(remotes) : Collections.emptySet();
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public Set getSubTypes(HierarchieTypeRemote type)
    {
        return new HashSet(getSubTypesInternal(type, false));
    }


    public Set getAllSubTypes(HierarchieTypeRemote type)
    {
        return getSubTypesInternal(type, true);
    }


    private Set getSubTypesInternal(HierarchieTypeRemote type, boolean transitive)
    {
        try
        {
            String inhstr = type.getInheritancePathString();
            String inhstrcomp = inhstr;
            Collection<?> remotes = (type instanceof AtomicTypeRemote)
                            ? (transitive ? getAtomicTypeHome().findByInheritancePath(inhstrcomp + "%") : getAtomicTypeHome().findBySuperType(EJBTools.getPK((ItemRemote)type)))
                            : (transitive ? getComposedTypeHome().findByInheritancePath(inhstrcomp + "%") : getComposedTypeHome().findBySuperType(EJBTools.getPK((ItemRemote)type)));
            Set ret = (remotes != null && !remotes.isEmpty()) ? new HashSet(remotes) : Collections.EMPTY_SET;
            if(!ret.isEmpty())
            {
                ret.remove(type);
            }
            return ret;
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public Set<? extends AttributeDescriptorRemote> getAllSubAttributeDescriptors(AttributeDescriptorRemote fd)
    {
        Set<? extends AttributeDescriptorRemote> found = getAttributeDescriptorsByInheritancePathString(fd.getQualifier(),
                        EJBTools.getPKCollectionString(fd.getInheritancePath()));
        found.remove(fd);
        return found;
    }


    protected Set getAttributeDescriptorsByInheritancePathString(String qualifier, String inheritancePathString)
    {
        try
        {
            Set found = new HashSet(getAttributeDescriptorHome().findInheritedByQualifierAndInheritancePath(
                            (qualifier != null) ? qualifier.toLowerCase(LocaleHelper.getPersistenceLocale()) : null, inheritancePathString + "%"));
            return found;
        }
        catch(YFinderException e)
        {
            log.error(e.getMessage(), (Throwable)e);
            throw new JaloSystemException(e);
        }
    }


    public Set getDeclaredAttributeDescriptors(ComposedTypeRemote enclosingType)
    {
        return getDeclaredAttributeDescriptorsInternal(enclosingType);
    }


    protected Set getDeclaredAttributeDescriptorsInternal(ComposedTypeRemote enclosingType)
    {
        try
        {
            return new HashSet(getAttributeDescriptorHome().findDeclaredByEnclosingType(EJBTools.getPK((ItemRemote)enclosingType)));
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public Set getInheritedAttributeDescriptors(ComposedTypeRemote enclosingType)
    {
        try
        {
            return new HashSet(getAttributeDescriptorHome().findInhertitedByEnclosingType(EJBTools.getPK((ItemRemote)enclosingType)));
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public void setDeclaredAttributeDescriptors(ComposedTypeRemote enclosingType, Set attributeDescriptors) throws EJBInvalidParameterException
    {
        TypeTools.setPartOf((ItemRemote)enclosingType, attributeDescriptors, (TypeTools.Binding)new Object(this));
    }


    public Set getAttributeDescriptors(ComposedTypeRemote enclosingType, boolean includePrivate)
    {
        try
        {
            return includePrivate ? getAllAttributeDescriptorsInternal(enclosingType) : new HashSet(getAttributeDescriptorHome()
                            .findPublicByEnclosingType(
                                            EJBTools.getPK((ItemRemote)enclosingType)));
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public AttributeDescriptorRemote getDeclaredAttributeDescriptor(ComposedTypeRemote enclosingType, String qualifier) throws EJBItemNotFoundException
    {
        try
        {
            AttributeDescriptorRemote re = getAttributeDescriptorHome().findDeclaredByEnclosingTypeAndQualifier(
                            EJBTools.getPK((ItemRemote)enclosingType), (qualifier != null) ? qualifier.toLowerCase(LocaleHelper.getPersistenceLocale()) : null);
            if(re != null)
            {
                return re;
            }
            throw new EJBItemNotFoundException(null, "type " +
                            TypeTools.asString(enclosingType) + " has no feature-descriptor " + qualifier, 4711);
        }
        catch(YObjectNotFoundException e)
        {
            log.debug(e);
            throw new EJBItemNotFoundException(null, "type " + TypeTools.asString(enclosingType) + " has no feature-descriptor " + qualifier, 4711);
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public AttributeDescriptorRemote getAttributeDescriptor(ComposedTypeRemote enclosingType, String qualifier) throws EJBItemNotFoundException
    {
        try
        {
            AttributeDescriptorRemote re = getAttributeDescriptorHome().findPublicByEnclosingTypeAndQualifier(
                            EJBTools.getPK((ItemRemote)enclosingType), (qualifier != null) ? qualifier.toLowerCase(LocaleHelper.getPersistenceLocale()) : null);
            if(re != null)
            {
                return re;
            }
            throw new EJBItemNotFoundException(null, "type '" + TypeTools.asString(enclosingType) + "' has no public feature-descriptor '" + qualifier + "'", 4711);
        }
        catch(YObjectNotFoundException e)
        {
            log.debug(e);
            throw new EJBItemNotFoundException(null, "type '" + TypeTools.asString(enclosingType) + "' has no public feature-descriptor '" + qualifier + "'", 4711);
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public AttributeDescriptorRemote getEveryAttributeDescriptor(ComposedTypeRemote enclosingType, String qualifier) throws EJBItemNotFoundException
    {
        if(enclosingType == null || qualifier == null)
        {
            throw new IllegalArgumentException("enclosing type or qualifier was null");
        }
        try
        {
            AttributeDescriptorRemote re = getAttributeDescriptorHome().findByEnclosingTypeAndQualifier(
                            EJBTools.getPK((ItemRemote)enclosingType), qualifier.toLowerCase(LocaleHelper.getPersistenceLocale()));
            if(re != null)
            {
                return re;
            }
            throw new EJBItemNotFoundException(null, "type " +
                            TypeTools.asString(enclosingType) + " has no feature-descriptor " + qualifier, 4711);
        }
        catch(YObjectNotFoundException e)
        {
            throw new EJBItemNotFoundException(null, "type " + TypeTools.asString(enclosingType) + " has no feature-descriptor " + qualifier, 4711);
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public AttributeDescriptorRemote getEveryAttributeDescriptor(PK enclosingTypePK, String qualifier) throws EJBItemNotFoundException
    {
        if(enclosingTypePK == null || qualifier == null)
        {
            throw new IllegalArgumentException("enclosing type PK or qualifier was null");
        }
        try
        {
            AttributeDescriptorRemote re = getAttributeDescriptorHome().findByEnclosingTypeAndQualifier(enclosingTypePK, qualifier
                            .toLowerCase(LocaleHelper.getPersistenceLocale()));
            if(re != null)
            {
                return re;
            }
            throw new EJBItemNotFoundException(null, "type " + enclosingTypePK + " has no feature-descriptor " + qualifier, 4711);
        }
        catch(YObjectNotFoundException e)
        {
            throw new EJBItemNotFoundException(null, "type " + enclosingTypePK + " has no feature-descriptor " + qualifier, 4711);
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public Set getAllAttributeDescriptors(ComposedTypeRemote enclosingType)
    {
        return getAllAttributeDescriptorsInternal(enclosingType);
    }


    protected Set getAllAttributeDescriptorsInternal(ComposedTypeRemote enclosingType)
    {
        try
        {
            return new HashSet(getAttributeDescriptorHome().findByEnclosingType(EJBTools.getPK((ItemRemote)enclosingType)));
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public Set getPropertyDescriptors(String ectPK)
    {
        try
        {
            return new HashSet(getAttributeDescriptorHome().findPropertyByEnclosingType(PK.parse(ectPK)));
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public AttributeDescriptorRemote createAttributeDescriptor(PK pkBase, ComposedTypeRemote enclosingType, String qualifier, String persistenceQualifier, TypeRemote type, AtomicTypeRemote persistenceType, int modifiers, ComposedTypeRemote metaType, boolean reinitialize)
                    throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        int illegal = modifiers & 0xFFFE9740;
        if(illegal != 0)
        {
            log.error("Cannot use modifiers " + illegal + " for attribute " + enclosingType.getCode() + "." + qualifier + " - ignored");
        }
        return createAttributeDescriptorInternal(pkBase, enclosingType, qualifier, persistenceQualifier, type, persistenceType, modifiers & 0x168BF, metaType, null, reinitialize, null);
    }


    public AtomicTypeRemote getAtomicTypeRepresentation(TypeRemote type)
    {
        if(type instanceof AtomicTypeRemote)
        {
            return (AtomicTypeRemote)type;
        }
        if(type instanceof ComposedTypeRemote)
        {
            return getAtomicItemRefType();
        }
        if(type instanceof MapTypeRemote)
        {
            return isLocalizationType(type, Collections.EMPTY_LIST) ? getAtomicTypeRepresentation(((MapTypeRemote)type)
                            .getReturnType()) :
                            getObjectType();
        }
        if(type instanceof CollectionTypeRemote)
        {
            if(((CollectionTypeRemote)type).getElementType() instanceof ComposedTypeRemote)
            {
                return getAtomicItemRefCollectionType();
            }
            return getObjectType();
        }
        return getObjectType();
    }


    private AtomicTypeRemote getObjectType()
    {
        try
        {
            return getRootAtomicType(Object.class);
        }
        catch(EJBItemNotFoundException e)
        {
            throw new JaloSystemException(e, "!!", 4711);
        }
    }


    private AtomicTypeRemote getAtomicItemRefType()
    {
        try
        {
            return getRootAtomicType(ItemPropertyValue.class);
        }
        catch(EJBItemNotFoundException e)
        {
            throw new JaloSystemException(e, "!!", 4711);
        }
    }


    private AtomicTypeRemote getAtomicItemRefCollectionType()
    {
        try
        {
            return getRootAtomicType(ItemPropertyValueCollection.class);
        }
        catch(EJBItemNotFoundException e)
        {
            throw new JaloSystemException(e, "!!", 4711);
        }
    }


    public AttributeDescriptorRemote createPropertyDescriptor(PK pkBase, ComposedTypeRemote enclosingType, String qualifier, TypeRemote type, int modifiers, ComposedTypeRemote metaType, Map sqlColumnDefs, boolean reinitialize) throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        return createPropertyDescriptor(pkBase, enclosingType, qualifier, type, modifiers, metaType, sqlColumnDefs, reinitialize, null);
    }


    public AttributeDescriptorRemote createPropertyDescriptor(PK pkBase, ComposedTypeRemote enclosingType, String qualifier, TypeRemote type, int modifiers, ComposedTypeRemote metaType, Map sqlColumnDefs, boolean reinitialize, String realColumnName)
                    throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        if(qualifier == null || qualifier.length() == 0)
        {
            throw new EJBInvalidParameterException(null, "Got empty qualifier for creating an attribte descriptor", 0);
        }
        int nonPublic = modifiers & 0xFFFE9640;
        if(nonPublic != 0)
        {
            log.error("Cannot use modfiers " + nonPublic + " for creating property descriptor '" + qualifier + "' of type " +
                            TypeTools.asString((TypeRemote)enclosingType));
        }
        int primitive = modifiers & 0x10000;
        if(primitive != 0 && !(type instanceof AtomicTypeRemote))
        {
            throw new EJBInvalidParameterException(null, "Attribute " + enclosingType.getCode() + "." + qualifier + " can not be created as primitive attribute because it's type is not an AtomicType", 0);
        }
        if(primitive != 0 && MethodUtils.getPrimitiveType(((AtomicTypeRemote)type).getJavaClass()) == null)
        {
            throw new EJBInvalidParameterException(null, "Attribute " + enclosingType.getCode() + "." + qualifier + " can not be created as primitive attribute because it's type " + ((AtomicTypeRemote)type)
                            .getJavaClass() + " has no corresponding primitive java class", 0);
        }
        AttributeDescriptorRemote fd = createAttributeDescriptorInternal(pkBase, enclosingType, qualifier, null, type,
                        getAtomicTypeRepresentation(type), 0x100 | modifiers & 0x168BF, metaType, sqlColumnDefs, reinitialize, realColumnName);
        return fd;
    }


    public AttributeDescriptorRemote redeclareAttributeDescriptor(ComposedTypeRemote enclosingType, String qualifier, TypeRemote type, int modifiers) throws EJBInvalidParameterException, EJBItemNotFoundException
    {
        AttributeDescriptorRemote ad = getEveryAttributeDescriptor(enclosingType, qualifier);
        redeclareAttributeDescriptor(ad, type, modifiers);
        return ad;
    }


    public void redeclareAttributeDescriptor(AttributeDescriptorRemote theFD, TypeRemote type, int modifiers) throws EJBInvalidParameterException
    {
        boolean typeChange = !Utilities.ejbEquals((ItemRemote)type, (ItemRemote)theFD.getAttributeType());
        int oldModifiers = theFD.getModifiers();
        int EXCLUDED = 10020;
        boolean modChange = ((modifiers & 0x168BF & 0xFFFFD8DB) != (oldModifiers & 0x168BF & 0xFFFFD8DB));
        if(typeChange || modChange)
        {
            boolean search = ((modifiers & 0x10) == 16);
            boolean reallySearchble = (search && (theFD.isProperty() || theFD.getPersistenceQualifier() != null || theFD.getDatabaseColumn() != null));
            theFD.setModifiers((
                            reallySearchble ? (modifiers & 0xFFFFD8DB) : (modifiers & 0xFFFFFFEF & 0xFFFFD8DB)) | oldModifiers & 0x2724);
            theFD.setAttributeType(type);
            Set<? extends AttributeDescriptorRemote> subFeatures = getAllSubAttributeDescriptors(theFD);
            for(Iterator<? extends AttributeDescriptorRemote> iter = subFeatures.iterator(); iter.hasNext(); )
            {
                AttributeDescriptorRemote subFD = iter.next();
                reallySearchble = (search && (subFD.isProperty() || subFD.getPersistenceQualifier() != null || subFD.getDatabaseColumn() != null));
                subFD.setModifiers((reallySearchble ? (modifiers & 0xFFFFD8DB) : (
                                modifiers & 0xFFFFFFEF & 0xFFFFD8DB)) | oldModifiers & 0x2724);
                subFD.setAttributeType(type);
            }
        }
    }


    private AttributeDescriptorRemote createAttributeDescriptorInternal(PK pkBase, ComposedTypeRemote enclosingType, String qualifier, String persistenceQualifier, TypeRemote type, AtomicTypeRemote persistenceType, int modifiers, ComposedTypeRemote metaType, Map sqlColumnDefs, boolean reinitialize,
                    String realColumnName) throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        try
        {
            AttributeDescriptorRemote fd = null;
            try
            {
                fd = reinitialize ? getEveryAttributeDescriptor(EJBTools.getPK((ItemRemote)enclosingType), qualifier) : null;
            }
            catch(EJBItemNotFoundException e1)
            {
                log.trace(e1);
            }
            if(fd != null)
            {
                fd.reinitializeAttribute(qualifier, persistenceQualifier, type, persistenceType, modifiers, null, null, metaType, null);
            }
            else
            {
                fd = getAttributeDescriptorHome().create(pkBase, enclosingType, qualifier, persistenceQualifier, type, persistenceType, modifiers, metaType);
            }
            if(reinitialize || (sqlColumnDefs != null && !sqlColumnDefs.isEmpty()))
            {
                fd.setProperty("sqlcolumndescriptions", sqlColumnDefs);
            }
            if(realColumnName != null)
            {
                fd.setDatabaseColumn(realColumnName);
            }
            createSubAttributeDescriptors(enclosingType, fd, sqlColumnDefs, reinitialize);
            return fd;
        }
        catch(YCreateException e)
        {
            throw new JaloSystemException(e, "!!", 4711);
        }
    }


    protected void copyDownParentAttributeDescriptor(ComposedTypeRemote newType, AttributeDescriptorRemote parentDescriptor) throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        if(log.isDebugEnabled())
        {
            log.debug("Copying super attribute " + TypeTools.asString(parentDescriptor) + " to " + TypeTools.asString((TypeRemote)newType));
        }
        SubAttributeDescriptorCreator creator = new SubAttributeDescriptorCreator(this, parentDescriptor, calculateSubAttributeInheritancePath(newType, parentDescriptor), false);
        try
        {
            AttributeDescriptorRemote subFD = creator.create();
            ItemPropertyValue relTypeIPV = (ItemPropertyValue)parentDescriptor.getProperty("relationType");
            EJBPropertyContainer cont = new EJBPropertyContainer();
            if(relTypeIPV != null)
            {
                cont.setProperty("relationType", relTypeIPV);
                cont.setProperty("isSource", parentDescriptor.getProperty("isSource"));
                cont.setProperty("relationName", parentDescriptor
                                .getProperty("relationName"));
            }
            cont.setProperty(AttributeDescriptor.UNIQUE, parentDescriptor.getProperty(AttributeDescriptor.UNIQUE));
            cont.setProperty(AttributeDescriptor.DEFAULTVALUE, parentDescriptor.getProperty(AttributeDescriptor.DEFAULTVALUE));
            cont.setProperty(AttributeDescriptor.DEFAULTVALUE_DEFINITIONSTRING, parentDescriptor
                            .getProperty(AttributeDescriptor.DEFAULTVALUE_DEFINITIONSTRING));
            cont.setProperty(AttributeDescriptor.ATTRIBUTE_HANDLER, parentDescriptor
                            .getProperty(AttributeDescriptor.ATTRIBUTE_HANDLER));
            cont.setProperty("hiddenForUI", parentDescriptor.getProperty("hiddenForUI"));
            cont.setProperty("readOnlyForUI", parentDescriptor.getProperty("readOnlyForUI"));
            cont.setProperty("dontCopy", parentDescriptor.getProperty("dontCopy"));
            subFD.setAllProperties(cont);
        }
        catch(Exception e)
        {
            log.error("Error copying feature " + TypeTools.asString(parentDescriptor), e);
            creator.remove();
            if(e instanceof EJBDuplicateQualifierException)
            {
                throw (EJBDuplicateQualifierException)e;
            }
            if(e instanceof EJBInvalidParameterException)
            {
                throw (EJBInvalidParameterException)e;
            }
            throw new JaloSystemException(e);
        }
    }


    protected List calculateSubAttributeInheritancePath(ComposedTypeRemote newEnclosingType, AttributeDescriptorRemote superAttribute)
    {
        return calculateSubAttributeInheritancePath(newEnclosingType, superAttribute.getDeclaringEnclosingType());
    }


    protected List calculateSubAttributeInheritancePath(ComposedTypeRemote newEnclosingType, ComposedTypeRemote superEnclosingType)
    {
        List<?> subtypePath = new ArrayList(newEnclosingType.getInheritancePath());
        int pos = subtypePath.indexOf(superEnclosingType);
        if(pos < 0)
        {
            throw new JaloSystemException(null, "enclosing type " + TypeTools.asString(superEnclosingType) + " is not part of alleged subtype " +
                            TypeTools.asString(newEnclosingType) + " inheritance path " + subtypePath, 0);
        }
        return new ArrayList(subtypePath.subList(pos, subtypePath.size()));
    }


    private void createSubAttributeDescriptors(ComposedTypeRemote parentEnclosingType, AttributeDescriptorRemote parentDescriptor, Map sqlDefs, boolean reinitialize) throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        if(parentDescriptor == null)
        {
            throw new IllegalArgumentException("parent descriptor was null");
        }
        if(parentEnclosingType == null)
        {
            throw new IllegalArgumentException("parent enclosing type was null");
        }
        List<SubAttributeDescriptorCreator> creators = new ArrayList();
        for(Iterator<ComposedTypeRemote> iter = getSubTypesInternal((HierarchieTypeRemote)parentEnclosingType, true).iterator(); iter.hasNext(); )
        {
            ComposedTypeRemote subType = iter.next();
            creators.add(new SubAttributeDescriptorCreator(this, parentDescriptor, calculateSubAttributeInheritancePath(subType, parentEnclosingType), reinitialize));
        }
        String realColumnName = parentDescriptor.getDatabaseColumn();
        for(int i = 0; i < creators.size(); i++)
        {
            SubAttributeDescriptorCreator fdCreator = creators.get(i);
            try
            {
                AttributeDescriptorRemote subFD = fdCreator.create();
                if(reinitialize || (sqlDefs != null && !sqlDefs.isEmpty()))
                {
                    subFD.setProperty("sqlcolumndescriptions", sqlDefs);
                }
                if(realColumnName != null)
                {
                    subFD.setDatabaseColumn(realColumnName);
                }
                ItemPropertyValue relTypeIPV = (ItemPropertyValue)parentDescriptor.getProperty("relationType");
                if(relTypeIPV != null)
                {
                    EJBPropertyContainer cont = new EJBPropertyContainer();
                    cont.setProperty("relationType", relTypeIPV);
                    cont.setProperty("isSource", parentDescriptor.getProperty("isSource"));
                    cont.setProperty("relationName", parentDescriptor
                                    .getProperty("relationName"));
                    subFD.setAllProperties(cont);
                }
            }
            catch(Exception e)
            {
                log.error("Error copying feature " + TypeTools.asString(parentDescriptor), e);
                for(int j = i; j >= 0; j--)
                {
                    ((SubAttributeDescriptorCreator)creators.get(j)).remove();
                }
                if(e instanceof EJBDuplicateQualifierException)
                {
                    throw (EJBDuplicateQualifierException)e;
                }
                if(e instanceof EJBInvalidParameterException)
                {
                    throw (EJBInvalidParameterException)e;
                }
                throw new JaloSystemException(e);
            }
        }
    }


    protected void copySuperTypeData(HierarchieTypeRemote type, HierarchieTypeRemote newSuperType) throws EJBInvalidParameterException
    {
        try
        {
            if(type instanceof ComposedTypeRemote)
            {
                if(newSuperType instanceof ComposedTypeRemote)
                {
                    ComposedTypeRemote newEnclosingType = (ComposedTypeRemote)type;
                    List<AttributeDescriptorRemote> selectionAttributes = new ArrayList();
                    Iterator<AttributeDescriptorRemote> iterator1 = getAllAttributeDescriptorsInternal((ComposedTypeRemote)newSuperType).iterator();
                    while(iterator1.hasNext())
                    {
                        AttributeDescriptorRemote superFD = iterator1.next();
                        if(superFD.getSelectionOf() != null)
                        {
                            selectionAttributes.add(superFD);
                            continue;
                        }
                        copyDownParentAttributeDescriptor(newEnclosingType, superFD);
                    }
                    for(Iterator<AttributeDescriptorRemote> iter = selectionAttributes.iterator(); iter.hasNext(); )
                    {
                        AttributeDescriptorRemote superFD = iter.next();
                        copyDownParentAttributeDescriptor(newEnclosingType, superFD);
                    }
                }
            }
        }
        catch(EJBDuplicateQualifierException e)
        {
            throw new EJBInvalidParameterException(e, "!!", 4711);
        }
    }


    private AtomicTypeHome getAtomicTypeHome()
    {
        return (AtomicTypeHome)getPersistencePool().getHomeProxy(
                        getPersistencePool().getPersistenceManager().getJNDIName(81));
    }


    private ComposedTypeHome getComposedTypeHome()
    {
        return (ComposedTypeHome)getPersistencePool().getHomeProxy(
                        getPersistencePool().getPersistenceManager().getJNDIName(82));
    }


    private CollectionTypeHome getCollectionTypeHome()
    {
        return (CollectionTypeHome)getPersistencePool().getHomeProxy(
                        getPersistencePool().getPersistenceManager().getJNDIName(83));
    }


    private MapTypeHome getMapTypeHome()
    {
        return (MapTypeHome)getPersistencePool().getHomeProxy(
                        getPersistencePool().getPersistenceManager().getJNDIName(84));
    }


    private AttributeDescriptorHome getAttributeDescriptorHome()
    {
        return (AttributeDescriptorHome)getPersistencePool().getHomeProxy(
                        getPersistencePool().getPersistenceManager().getJNDIName(87));
    }


    protected boolean notInstanceManagingComposedTypeWithInstances(TypeRemote item)
    {
        try
        {
            if(item instanceof ComposedTypeRemote)
            {
                ComposedTypeRemote type = (ComposedTypeRemote)item;
                return (!type.isAbstract() &&
                                !((ItemHome)getPersistencePool().getHomeProxy(type.getItemJNDIName())).findByType(EJBTools.getPK((ItemRemote)type)).isEmpty());
            }
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
        return false;
    }


    protected boolean isNonRemovableType(TypeRemote item)
    {
        return (item instanceof ComposedTypeRemote && !((ComposedTypeRemote)item).isRemovable());
    }


    protected boolean referencedType(TypeRemote item)
    {
        try
        {
            PK pk = EJBTools.getPK((ItemRemote)item);
            if(!getAttributeDescriptorHome().findByType(pk).isEmpty())
            {
                return true;
            }
            return (!getCollectionTypeHome().findByElementType(pk).isEmpty() ||
                            !getMapTypeHome().findByArgumentType(pk).isEmpty() ||
                            !getMapTypeHome().findByReturnType(pk).isEmpty());
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e, "!!", 4711);
        }
    }


    protected boolean typeWithSubTypes(TypeRemote item)
    {
        return (item instanceof HierarchieTypeRemote && !getSubTypesInternal((HierarchieTypeRemote)item, false).isEmpty());
    }


    public ComposedTypeRemote createRelationType(PK pkBase, String relationQualfier, boolean localized, AttributeDescriptorRemote sourceFD, AttributeDescriptorRemote targetFD) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        if(relationQualfier == null || sourceFD == null || targetFD == null)
        {
            throw new EJBInvalidParameterException(null, "illegal relation type arguments ( name=" + relationQualfier + ", localized=" + localized + ", sourceFD=" +
                            TypeTools.asString(sourceFD) + ", targetFD=" +
                            TypeTools.asString(targetFD) + " )", 0);
        }
        try
        {
            ComposedTypeRemote ret = createRelationType(pkBase, relationQualfier, true, false);
            connectRelation(relationQualfier, localized, ret, sourceFD, targetFD, null, null);
            return ret;
        }
        catch(EJBDuplicateCodeException e)
        {
            throw new EJBDuplicateCodeException(e, "relation name '" + relationQualfier + "' already exists", 0);
        }
    }


    protected ComposedTypeRemote getRelationMetaType()
    {
        try
        {
            return getComposedType("RelationMetaType");
        }
        catch(EJBItemNotFoundException e)
        {
            log.debug(e);
            return null;
        }
    }


    protected ComposedTypeRemote getRelationDescriptorMetaType()
    {
        try
        {
            ComposedTypeRemote t = getRootComposedTypeForJaloClass(RelationDescriptor.class.getName());
            return t;
        }
        catch(EJBItemNotFoundException e)
        {
            throw new JaloSystemException(e, "cannot get relation feature meta type", 0);
        }
    }


    protected boolean isRelationDescriptor(AttributeDescriptorRemote fd)
    {
        return Utilities.ejbEquals((ItemRemote)getRelationDescriptorMetaType(), (ItemRemote)fd.getComposedType());
    }


    protected ComposedTypeRemote getRelationRootType()
    {
        try
        {
            return getRootComposedType(7);
        }
        catch(EJBItemNotFoundException e)
        {
            throw new JaloSystemException(e, "cannot find link item type", 0);
        }
    }


    public boolean isRelationType(ComposedTypeRemote type)
    {
        ComposedTypeRemote rmt = getRelationMetaType();
        PK rmtPK = (rmt == null) ? PK.NULL_PK : rmt.getPK();
        return rmtPK.equals(type.getTypeKey());
    }


    public ComposedTypeRemote createRelationType(PK pkBase, String relationName, boolean copySupertypeData, boolean isAbstract) throws EJBDuplicateCodeException
    {
        try
        {
            ComposedTypeRemote t = isAbstract ? createNonRootComposedType(pkBase, getComposedType("Item"), relationName, null, getRelationMetaType(), copySupertypeData) : createNonRootComposedType(pkBase,
                            getRelationRootType(), relationName, null,
                            getRelationMetaType(), copySupertypeData);
            return t;
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloSystemException(e);
        }
        catch(EJBItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "2005.0", forRemoval = true)
    public ComposedTypeRemote createRootRelationType(PK pkBase, String relationQualfier, ItemDeployment deployment, boolean copySupertypeData) throws EJBDuplicateCodeException, EJBInvalidParameterException
    {
        ComposedTypeRemote t = createRootComposedType(pkBase, getRelationRootType(), relationQualfier, null, deployment,
                        getRelationMetaType(), copySupertypeData);
        return t;
    }


    public void connectRelation(String relName, boolean localized, ComposedTypeRemote relType, AttributeDescriptorRemote sourceFD, AttributeDescriptorRemote targetFD, AttributeDescriptorRemote orderingFD, AttributeDescriptorRemote locFD)
    {
        Preconditions.checkArgument((relType != null), "relType cannot be null");
        try
        {
            ItemPropertyValue relTypeIPV = new ItemPropertyValue(relType.getPK());
            EJBPropertyContainer cont = new EJBPropertyContainer();
            cont.setProperty("localized", localized ? Boolean.TRUE : Boolean.FALSE);
            if(sourceFD != null)
            {
                cont.setProperty("sourceAttribute", new ItemPropertyValue(sourceFD.getPK()));
            }
            if(targetFD != null)
            {
                cont.setProperty("targetAttribute", new ItemPropertyValue(targetFD.getPK()));
            }
            if(orderingFD != null)
            {
                cont.setProperty("orderingAttribute", new ItemPropertyValue(orderingFD.getPK()));
            }
            if(locFD != null)
            {
                cont.setProperty("localizationAttribute", new ItemPropertyValue(locFD.getPK()));
            }
            relType.setAllProperties(cont);
            if(sourceFD != null)
            {
                cont = new EJBPropertyContainer();
                cont.setProperty("relationType", relTypeIPV);
                cont.setProperty("isSource", Boolean.TRUE);
                cont.setProperty("relationName", relName);
                sourceFD.setAllProperties(cont);
                for(Iterator<? extends AttributeDescriptorRemote> iter = getAllSubAttributeDescriptors(sourceFD).iterator(); iter.hasNext(); )
                {
                    AttributeDescriptorRemote fd = iter.next();
                    fd.setAllProperties(cont);
                }
            }
            if(targetFD != null)
            {
                cont = new EJBPropertyContainer();
                cont.setProperty("relationType", relTypeIPV);
                cont.setProperty("isSource", Boolean.FALSE);
                cont.setProperty("relationName", relName);
                targetFD.setAllProperties(cont);
                for(Iterator<? extends AttributeDescriptorRemote> iter = getAllSubAttributeDescriptors(targetFD).iterator(); iter.hasNext(); )
                {
                    AttributeDescriptorRemote fd = iter.next();
                    fd.setAllProperties(cont);
                }
            }
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "2005.0", forRemoval = true)
    public CollectionTypeRemote getCollectionTypeNoCreate(String relationName, String qualifier)
    {
        String collectionTypeCodeName = getCollectionTypeCode(relationName, qualifier);
        try
        {
            Collection<CollectionTypeRemote> types = getCollectionTypeHome().findByCodeExact(collectionTypeCodeName.toLowerCase(LocaleHelper.getPersistenceLocale()));
            if(types != null && !types.isEmpty())
            {
                return types.iterator().next();
            }
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e, "Can not find a collection type " + collectionTypeCodeName, 0);
        }
        return null;
    }


    @Deprecated(since = "2005.0", forRemoval = true)
    public boolean hasSameElementType(CollectionTypeRemote collType, ComposedTypeRemote newTypeDescriptor)
    {
        if(collType != null && collType.getElementType() != null && newTypeDescriptor != null)
        {
            TypeRemote elementType = collType.getElementType();
            if(elementType instanceof ComposedTypeRemote && elementType.getCode().equals(newTypeDescriptor.getCode()))
            {
                return true;
            }
        }
        return false;
    }


    public AttributeDescriptorRemote createRelationDescriptor(PK pkBase, String relationName, String qualifier, ComposedTypeRemote enclosingType, ComposedTypeRemote type, int modifiers, boolean localized, boolean reinit, int typeOfCollection)
                    throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        TypeRemote attributeType;
        AttributeDescriptorRemote relAd = null;
        if(reinit)
        {
            try
            {
                relAd = getEveryAttributeDescriptor(enclosingType, qualifier);
            }
            catch(EJBItemNotFoundException e)
            {
                log.trace(e);
            }
        }
        String collectionTypeCode = getCollectionTypeCode(relationName, qualifier);
        String mapTypeCode = relationName + relationName + "LocMap";
        CollectionTypeRemote collType = getOrCreateCollectionType(null, collectionTypeCode, (TypeRemote)type, typeOfCollection);
        ComposedTypeRemote relDescType = getRelationDescriptorMetaType();
        try
        {
            attributeType = localized ? (TypeRemote)getOrCreateMapType(null, mapTypeCode, (TypeRemote)getRootComposedType(32), (TypeRemote)collType) : (TypeRemote)collType;
        }
        catch(EJBInvalidParameterException e1)
        {
            throw new JaloSystemException(e1);
        }
        catch(EJBItemNotFoundException e1)
        {
            throw new JaloSystemException(e1);
        }
        int filteredFlags = enclosingType.isAbstract() ? 784 : 768;
        int addedFlags = localized ? 512 : 0;
        relAd = createAttributeDescriptorInternal(pkBase, enclosingType, qualifier, null, attributeType, null, modifiers & (filteredFlags ^ 0xFFFFFFFF) | addedFlags, relDescType, null, reinit, null);
        return relAd;
    }


    private String getCollectionTypeCode(String relationName, String qualifier)
    {
        String collectionTypeCode = relationName + relationName + "Coll";
        return collectionTypeCode;
    }


    @Deprecated(since = "2005.0", forRemoval = true)
    public AttributeDescriptorRemote createPropertyRelationDescriptor(String qualifier, ComposedTypeRemote enclosingType, ComposedTypeRemote type, int modifiers, boolean reinit) throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        ComposedTypeRemote relDescType = getRelationDescriptorMetaType();
        AttributeDescriptorRemote relAd = null;
        try
        {
            relAd = getEveryAttributeDescriptor(enclosingType, qualifier);
        }
        catch(EJBItemNotFoundException e)
        {
            log.trace(e);
        }
        if((relAd != null && relAd.getDatabaseColumn() != null && !relAd.isProperty()) ||
                        excludeKnownRedeclaredRelationAttributes(enclosingType.getCode(), qualifier))
        {
            if(relAd == null)
            {
                throw new IllegalStateException("relAd must not be null");
            }
            if(!relAd.isProperty())
            {
                relAd.setModifiers(modifiers & 0x168BF);
            }
            if(!Utilities.ejbEquals((ItemRemote)relAd.getAttributeType(), (ItemRemote)type))
            {
                relAd.setAttributeType((TypeRemote)type);
            }
            if(!Utilities.ejbEquals((ItemRemote)relAd.getComposedType(), (ItemRemote)relDescType))
            {
                relAd.setComposedType(relDescType);
            }
            createSubAttributeDescriptors(enclosingType, relAd, null, true);
        }
        else
        {
            relAd = createPropertyDescriptor(null, enclosingType, qualifier, (TypeRemote)type, modifiers, relDescType, null, reinit);
        }
        return relAd;
    }


    private boolean excludeKnownRedeclaredRelationAttributes(String enclosingTypeCode, String qualifier)
    {
        return (KNOWN_REDECLARED_RELATION_ATTRIBUTES.containsKey(qualifier) && ((Set)KNOWN_REDECLARED_RELATION_ATTRIBUTES
                        .get(qualifier)).contains(enclosingTypeCode));
    }


    public SearchRestrictionRemote createRestriction(PK principalPK, PK typePK, String query, String code, Boolean active)
    {
        try
        {
            SearchRestrictionHome home = (SearchRestrictionHome)getPersistencePool().getHomeProxy(
                            getPersistencePool().getPersistenceManager().getJNDIName(90));
            return home.create(principalPK, typePK, query, code, active);
        }
        catch(YCreateException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public Collection getRestrictions(PK principalPK, PK typePK)
    {
        if(principalPK == null)
        {
            return getTypeRestrictions(typePK);
        }
        if(typePK == null)
        {
            return getPrincipalRestrictions(principalPK);
        }
        try
        {
            SearchRestrictionHome home = (SearchRestrictionHome)getPersistencePool().getHomeProxy(
                            getPersistencePool().getPersistenceManager().getJNDIName(90));
            return home.findRestrictions(principalPK, typePK);
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    private Collection getPrincipalRestrictions(PK principalPK)
    {
        try
        {
            SearchRestrictionHome home = (SearchRestrictionHome)getPersistencePool().getHomeProxy(
                            getPersistencePool().getPersistenceManager().getJNDIName(90));
            return home.findByPrincipal(principalPK);
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    private Collection getTypeRestrictions(PK typePK)
    {
        try
        {
            SearchRestrictionHome home = (SearchRestrictionHome)getPersistencePool().getHomeProxy(
                            getPersistencePool().getPersistenceManager().getJNDIName(90));
            return home.findByRestrictedType(typePK);
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    private void removePrincipalRestrictions(PK pk) throws ConsistencyCheckException
    {
        try
        {
            SearchRestrictionHome home = (SearchRestrictionHome)getPersistencePool().getHomeProxy(
                            getPersistencePool().getPersistenceManager().getJNDIName(90));
            Collection toRemove = home.findByPrincipal(pk);
            for(Iterator<SearchRestrictionRemote> it = toRemove.iterator(); it.hasNext(); )
            {
                removeItem((ItemRemote)it.next());
            }
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    private void removeTypeRestrictions(PK pk) throws ConsistencyCheckException
    {
        try
        {
            SearchRestrictionHome home = (SearchRestrictionHome)getPersistencePool().getHomeProxy(
                            getPersistencePool().getPersistenceManager().getJNDIName(90));
            Collection toRemove = home.findByRestrictedType(pk);
            for(Iterator<SearchRestrictionRemote> it = toRemove.iterator(); it.hasNext(); )
            {
                removeItem((ItemRemote)it.next());
            }
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    private boolean hasSelectionDependencies(AttributeDescriptorRemote descriptor)
    {
        try
        {
            AttributeDescriptorHome home = (AttributeDescriptorHome)getPersistencePool().getHomeProxy(
                            getPersistencePool().getPersistenceManager().getJNDIName(87));
            Collection dep = home.findByEnclosingTypeAndSelectionDescriptor(descriptor.getEnclosingType().getPK(), descriptor
                            .getPK());
            return (dep != null && !dep.isEmpty());
        }
        catch(YObjectNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
        catch(YFinderException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public boolean canRemoveItem(ItemRemote item) throws ConsistencyCheckException
    {
        if(item instanceof AttributeDescriptorRemote)
        {
            if(!((AttributeDescriptorRemote)item).isRemovable())
            {
                throw new ConsistencyCheckException(null, "attribute descriptor " +
                                TypeTools.asString((AttributeDescriptorRemote)item) + " is not removable.", 4711);
            }
            if(((AttributeDescriptorRemote)item).isInherited())
            {
                throw new ConsistencyCheckException(null, "attribute descriptor " +
                                TypeTools.asString((AttributeDescriptorRemote)item) + " is inherited and cannot be removed alone - remove declaring feature.", 4711);
            }
            if(hasSelectionDependencies((AttributeDescriptorRemote)item))
            {
                throw new ConsistencyCheckException(null, "attribute descriptor " +
                                TypeTools.asString((AttributeDescriptorRemote)item) + " has selection dependencies to other descriptors and cannot be removed.", 4711);
            }
            if(isRelationDescriptor((AttributeDescriptorRemote)item))
            {
                throw new ConsistencyCheckException(null, "attribute descriptor " +
                                TypeTools.asString((AttributeDescriptorRemote)item) + " cannot be removed alone - remove whole relation instead.", 4711);
            }
        }
        else if(item instanceof TypeRemote)
        {
            TypeRemote tr = (TypeRemote)item;
            if(isNonRemovableType(tr))
            {
                throw new ConsistencyCheckException(null, "type " + TypeTools.asString(tr) + " is not removable.", 4711);
            }
            if(referencedType(tr))
            {
                throw new ConsistencyCheckException(null, "type " + TypeTools.asString(tr) + " is still referenced.", 4711);
            }
            if(typeWithSubTypes(tr))
            {
                throw new ConsistencyCheckException(null, "type " + TypeTools.asString(tr) + " has subtypes.", 4711);
            }
            if(notInstanceManagingComposedTypeWithInstances(tr))
            {
                throw new ConsistencyCheckException(null, "type " + TypeTools.asString(tr) + " still has instances.", 4711);
            }
        }
        return true;
    }


    public void prepareItemRemove(ItemRemote item) throws ConsistencyCheckException
    {
        if(item instanceof ComposedTypeRemote)
        {
            for(Iterator<AttributeDescriptorRemote> iter = getAllAttributeDescriptorsInternal((ComposedTypeRemote)item).iterator(); iter.hasNext(); )
            {
                removeAttributeDescriptor(iter.next());
            }
            removeTypeRestrictions(item.getPkString());
            if(isRelationType((ComposedTypeRemote)item))
            {
                AttributeDescriptorRemote sourceFeature = (AttributeDescriptorRemote)EJBTools.instantiateItemPropertyValue((ItemPropertyValue)((ComposedTypeRemote)item)
                                .getProperty("sourceAttribute"));
                AttributeDescriptorRemote targetFeature = (AttributeDescriptorRemote)EJBTools.instantiateItemPropertyValue((ItemPropertyValue)((ComposedTypeRemote)item)
                                .getProperty("targetAttribute"));
                if(sourceFeature != null)
                {
                    removeRelationDescriptor(sourceFeature);
                }
                if(targetFeature != null)
                {
                    removeRelationDescriptor(targetFeature);
                }
            }
        }
        else if(item instanceof AttributeDescriptorRemote)
        {
            EJBTools.removeBeanCollection(getAllSubAttributeDescriptors((AttributeDescriptorRemote)item));
        }
        else if(TypeManager.getInstance().wrap(item) instanceof de.hybris.platform.jalo.security.Principal)
        {
            removePrincipalRestrictions(item.getPkString());
        }
    }


    public void removeItem(ItemRemote item) throws ConsistencyCheckException
    {
        super.removeItem(item);
    }


    public void notifyItemRemove(ItemRemote item)
    {
        System.err.println("deprecated call to TypeManagerEJB.notifyItemRemove( " + item + " ) at ");
        Thread.dumpStack();
    }


    protected boolean isInstanceOf(ItemRemote item, ComposedTypeRemote ct)
    {
        ComposedTypeRemote current = item.getComposedType();
        if(Utilities.ejbEquals((ItemRemote)current, (ItemRemote)ct))
        {
            return true;
        }
        for(ComposedTypeRemote subT : getAllSubTypes((HierarchieTypeRemote)ct))
        {
            if(Utilities.ejbEquals((ItemRemote)current, (ItemRemote)subT))
            {
                return true;
            }
        }
        return false;
    }


    protected void removeRelationDescriptor(AttributeDescriptorRemote fd) throws ConsistencyCheckException
    {
        if(isInstanceOf((ItemRemote)fd, getRelationDescriptorMetaType()))
        {
            TypeRemote type = fd.getAttributeType();
            boolean property = fd.isProperty();
            TypeRemote localizedCollectionType = fd.isLocalized() ? ((MapTypeRemote)type).getReturnType() : null;
            removeAttributeDescriptor(fd);
            if(!property)
            {
                removeItem((ItemRemote)type);
                if(localizedCollectionType != null)
                {
                    if(localizedCollectionType instanceof CollectionTypeRemote)
                    {
                        removeItem((ItemRemote)localizedCollectionType);
                    }
                    else
                    {
                        log.warn("Localized relation descriptor " + fd + " does not hold a collection, instead following type was found: " + localizedCollectionType);
                    }
                }
            }
        }
        else
        {
            log.warn("cannot remove plain attribute " + fd.getEnclosingType().getCode() + "." + fd.getQualifier() + " as relation descriptor (expected " +
                            getRelationDescriptorMetaType().getCode() + " but got " + fd
                            .getComposedType().getCode() + " as type)");
        }
    }


    public void removeAttributeDescriptor(AttributeDescriptorRemote fd)
    {
        try
        {
            boolean removeDumpData = (fd.isProperty() && fd.getDatabaseColumn() == null);
            String propertyName = removeDumpData ? fd.getQualifier() : null;
            String dumpTable = removeDumpData ? fd.getEnclosingType().getDeployment().getDumpPropertyTableName() : null;
            Collection<PK> enclosingTypePKs = removeDumpData ? new ArrayList<>() : null;
            if(removeDumpData)
            {
                enclosingTypePKs.add(EJBTools.getPK((ItemRemote)fd.getEnclosingType()));
            }
            Set<? extends AttributeDescriptorRemote> subfds = getAllSubAttributeDescriptors(fd);
            if(removeDumpData)
            {
                for(Iterator<? extends AttributeDescriptorRemote> it = subfds.iterator(); it.hasNext(); )
                {
                    AttributeDescriptorRemote subFD = it.next();
                    enclosingTypePKs.add(EJBTools.getPK((ItemRemote)subFD.getEnclosingType()));
                }
            }
            EJBTools.removeBeanCollection(subfds);
            fd.remove();
            if(removeDumpData)
            {
                OldPropertyJDBC.removeAllPropertyData(enclosingTypePKs, propertyName, dumpTable);
            }
        }
        catch(YRemoveException e)
        {
            throw new JaloSystemException(e);
        }
    }
}
