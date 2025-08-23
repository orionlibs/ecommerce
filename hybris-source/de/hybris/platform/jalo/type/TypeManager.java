package de.hybris.platform.jalo.type;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.type.AtomicTypeRemote;
import de.hybris.platform.persistence.type.AttributeDescriptorRemote;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.persistence.type.EJBDuplicateCodeException;
import de.hybris.platform.persistence.type.EJBDuplicateQualifierException;
import de.hybris.platform.persistence.type.TypeManagerEJB;
import de.hybris.platform.persistence.type.TypeRemote;
import de.hybris.platform.spring.CGLibUtils;
import de.hybris.platform.util.Utilities;
import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

@Deprecated(since = "ages", forRemoval = false)
public class TypeManager extends Manager
{
    private static final Logger log = Logger.getLogger(TypeManager.class.getName());
    public static final String BEAN_NAME = "core.typeManager";
    private final ConcurrentMap<String, Type> stringToTypeCache = new ConcurrentHashMap<>(1000, 0.75F, 16);
    private final ConcurrentMap<Class, Type> classToTypeCache = (ConcurrentMap)new ConcurrentHashMap<>(1000, 0.75F, 16);


    public void init()
    {
        InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.addInvalidationListener((InvalidationListener)new TypeManagerInvalidationListener(this));
    }


    public Class getRemoteManagerClass()
    {
        return TypeManagerEJB.class;
    }


    public static TypeManager getInstance()
    {
        return Registry.getCurrentTenant().getJaloConnection().getTypeManager();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Type getType(String code) throws JaloItemNotFoundException
    {
        try
        {
            return (Type)wrap(getTypeManagerEJB().getType(code));
        }
        catch(EJBItemNotFoundException e)
        {
            throw new JaloItemNotFoundException(e, 4711);
        }
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
        if(item instanceof AttributeDescriptor || item instanceof Type)
        {
            super.checkBeforeItemRemoval(ctx, item);
        }
    }


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
        if(item instanceof Principal)
        {
            StringBuilder errorSB = null;
            for(Iterator<SearchRestriction> iter = ((Principal)item).getSearchRestrictions().iterator(); iter.hasNext(); )
            {
                SearchRestriction sr = iter.next();
                try
                {
                    sr.remove(ctx);
                }
                catch(ConsistencyCheckException e)
                {
                    if(errorSB == null)
                    {
                        errorSB = (new StringBuilder("error(s) during removal of principal ")).append(item);
                    }
                    errorSB.append(Utilities.getStackTraceAsString((Throwable)e)).append("\n");
                }
            }
            if(errorSB != null && log.isEnabledFor((Priority)Level.ERROR))
            {
                log.error(errorSB.toString());
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set getAllTypes()
    {
        try
        {
            return getComposedType(Type.class).getAllInstances();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "did not find root AtomicType type", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<? extends AtomicType> getAllAtomicTypes()
    {
        try
        {
            return getComposedType(AtomicType.class).getAllInstances();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "did not find root AtomicType type", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<? extends ComposedType> getAllComposedTypes()
    {
        try
        {
            return getComposedType(ComposedType.class).getAllInstances();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "did not find root ComposedType type", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<? extends CollectionType> getAllCollectionTypes()
    {
        try
        {
            return getComposedType(CollectionType.class).getAllInstances();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "did not find root CollectionType type", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<? extends MapType> getAllMapTypes()
    {
        try
        {
            return getComposedType(MapType.class).getAllInstances();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "did not find root MapType type", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set getAtomicTypesForJavaClass(Class javaClass)
    {
        return (Set)wrap(getTypeManagerEJB().getAtomicTypesForJavaClass(javaClass));
    }


    public AtomicType getRootAtomicType(Class javaClass) throws JaloItemNotFoundException
    {
        try
        {
            return (AtomicType)wrap(getTypeManagerEJB().getRootAtomicType(javaClass));
        }
        catch(EJBItemNotFoundException e)
        {
            throw new JaloItemNotFoundException(e, 4711);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ComposedType getComposedType(Class jaloClass) throws JaloItemNotFoundException
    {
        Type type = this.classToTypeCache.get(jaloClass);
        if(type == null)
        {
            composedType = getRootComposedTypeForJaloClass(jaloClass);
            this.classToTypeCache.put(jaloClass, composedType);
        }
        if(!(composedType instanceof ComposedType))
        {
            throw new JaloItemNotFoundException("not found", 0);
        }
        ComposedType composedType = (ComposedType)composedType.getCacheBoundItem();
        return composedType;
    }


    public ComposedType getRootComposedType(Class jaloClass) throws JaloItemNotFoundException
    {
        ComposedType currentType = getRootComposedTypeForJaloClass(jaloClass);
        for(ComposedType superType = currentType.getSuperType(); superType != null && !superType.isAbstract();
                        superType = currentType.getSuperType())
        {
            currentType = superType;
        }
        return currentType;
    }


    public ComposedType getRootComposedType(int typeCode) throws JaloItemNotFoundException
    {
        try
        {
            return (ComposedType)wrap(getTypeManagerEJB().getRootComposedType(typeCode));
        }
        catch(EJBItemNotFoundException e)
        {
            throw new JaloItemNotFoundException(e, 4711);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ComposedType getComposedType(String code) throws JaloItemNotFoundException
    {
        Type type = this.stringToTypeCache.get(code);
        if(type == null)
        {
            try
            {
                composedType = (ComposedType)wrap(getTypeManagerEJB().getComposedType(code));
            }
            catch(EJBItemNotFoundException e)
            {
                throw new JaloItemNotFoundException(e, 0);
            }
            this.stringToTypeCache.put(code, composedType);
        }
        if(!(composedType instanceof ComposedType))
        {
            throw new JaloItemNotFoundException("not found", 0);
        }
        ComposedType composedType = (ComposedType)composedType.getCacheBoundItem();
        return composedType;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public AtomicType createAtomicType(Class javaClass) throws JaloInvalidParameterException, JaloDuplicateCodeException
    {
        return createAtomicType((PK)null, javaClass);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public AtomicType createAtomicType(PK pkBase, Class javaClass) throws JaloDuplicateCodeException, JaloInvalidParameterException
    {
        try
        {
            assureNoItem(javaClass);
            return (AtomicType)wrap(getTypeManagerEJB().createAtomicType(pkBase, javaClass));
        }
        catch(EJBDuplicateCodeException e)
        {
            throw new JaloDuplicateCodeException(e, 4711);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public AtomicType createAtomicType(AtomicType superType, Class javaClass) throws JaloInvalidParameterException, JaloDuplicateCodeException
    {
        return createAtomicType(null, superType, javaClass);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public AtomicType createAtomicType(PK pkBase, AtomicType superType, Class javaClass) throws JaloDuplicateCodeException, JaloInvalidParameterException
    {
        try
        {
            if(superType == null)
            {
                throw new JaloInvalidParameterException("super type must not be null", 0);
            }
            assureNoItem(javaClass);
            AtomicTypeRemote itemRemote = (AtomicTypeRemote)extractRequiredRemoteFromItem((Item)superType, AtomicTypeRemote.class);
            return (AtomicType)wrap(getTypeManagerEJB().createAtomicType(pkBase, itemRemote, javaClass));
        }
        catch(EJBDuplicateCodeException e)
        {
            throw new JaloDuplicateCodeException(e, 4711);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public AtomicType createAtomicType(AtomicType superType, String code) throws JaloInvalidParameterException, JaloDuplicateCodeException
    {
        return createAtomicType(null, superType, code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public AtomicType createAtomicType(PK pkBase, AtomicType superType, String code) throws JaloDuplicateCodeException, JaloInvalidParameterException
    {
        try
        {
            if(superType == null)
            {
                throw new JaloInvalidParameterException("super type must not be null", 0);
            }
            AtomicTypeRemote itemRemote = (AtomicTypeRemote)extractRequiredRemoteFromItem((Item)superType, AtomicTypeRemote.class);
            return (AtomicType)wrap(getTypeManagerEJB().createAtomicType(pkBase, itemRemote, code));
        }
        catch(EJBDuplicateCodeException e)
        {
            throw new JaloDuplicateCodeException(e, 4711);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ComposedType createComposedType(ComposedType superType, String code) throws JaloInvalidParameterException, JaloDuplicateCodeException
    {
        return createComposedType(null, superType, code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ComposedType createComposedType(PK pkBase, ComposedType superType, String code) throws JaloDuplicateCodeException, JaloInvalidParameterException
    {
        if(superType == null)
        {
            throw new JaloInvalidParameterException("supertype was null", 0);
        }
        try
        {
            ComposedTypeRemote itemRemote = (ComposedTypeRemote)extractRequiredRemoteFromItem((Item)superType, ComposedTypeRemote.class);
            return (ComposedType)wrap(getTypeManagerEJB().createNonRootComposedType(pkBase, itemRemote, code, null, null, true));
        }
        catch(EJBDuplicateCodeException e)
        {
            throw new JaloDuplicateCodeException(e, 4711);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CollectionType createCollectionType(String code, Type type) throws JaloInvalidParameterException, JaloDuplicateCodeException
    {
        return createCollectionType(null, code, type);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CollectionType createCollectionType(PK pk, String code, Type type) throws JaloInvalidParameterException, JaloDuplicateCodeException
    {
        return createCollectionType(pk, code, type, 0);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CollectionType createCollectionType(String code, Type type, int typeOfCollection) throws JaloInvalidParameterException, JaloDuplicateCodeException
    {
        return createCollectionType(null, code, type, typeOfCollection);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CollectionType createCollectionType(PK pkBase, String code, Type type, int typeOfCollection) throws JaloDuplicateCodeException, JaloInvalidParameterException
    {
        try
        {
            TypeRemote itemRemote = (TypeRemote)extractRequiredRemoteFromItem((Item)type, TypeRemote.class);
            return (CollectionType)wrap(getTypeManagerEJB().createCollectionType(pkBase, code, itemRemote, typeOfCollection));
        }
        catch(EJBDuplicateCodeException e)
        {
            throw new JaloDuplicateCodeException(e, 4711);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public MapType createMapType(String code, Type argumentType, Type returnType) throws JaloInvalidParameterException, JaloDuplicateCodeException
    {
        return createMapType(null, code, argumentType, returnType);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public MapType createMapType(PK pkBase, String code, Type argumentType, Type returnType) throws JaloDuplicateCodeException, JaloInvalidParameterException
    {
        try
        {
            TypeRemote argumentRemote = (TypeRemote)extractRequiredRemoteFromItem((Item)argumentType, TypeRemote.class);
            TypeRemote returnRemote = (TypeRemote)extractRequiredRemoteFromItem((Item)returnType, TypeRemote.class);
            return (MapType)wrap(getTypeManagerEJB().createMapType(pkBase, code, argumentRemote, returnRemote));
        }
        catch(EJBDuplicateCodeException e)
        {
            throw new JaloDuplicateCodeException(e, 4711);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 4711);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public SearchRestriction createRestriction(String code, Principal principal, ComposedType restrictedType, String query) throws JaloInvalidParameterException
    {
        return createRestriction(principal, restrictedType, query, code, true);
    }


    @Deprecated(since = "ages", forRemoval = false)
    private SearchRestriction createRestriction(Principal principal, ComposedType restrictedType, String query, String code, boolean active)
    {
        if(principal == null)
        {
            throw new JaloInvalidParameterException("principal must not be NULL", 0);
        }
        if(restrictedType == null)
        {
            throw new JaloInvalidParameterException("type must not be NULL", 0);
        }
        if(query == null)
        {
            throw new JaloInvalidParameterException("query must not be NULL", 0);
        }
        SearchRestriction.checkUniqueCodeTypePrincipal(principal, restrictedType, code);
        try
        {
            return (SearchRestriction)ComposedType.newInstance(
                            getSession().getSessionContext(), SearchRestriction.class, new Object[] {"principal", principal, "query", query, "code", code, "restrictedType", restrictedType, "active",
                                            Boolean.valueOf(active)});
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public SearchRestriction getSearchRestriction(ComposedType ct, String code)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("type", ct);
        params.put("code", code.toLowerCase(LocaleHelper.getPersistenceLocale()));
        List<SearchRestriction> rows = FlexibleSearch.getInstance()
                        .search("SELECT {" + Item.PK + "} FROM {" + getComposedType(SearchRestriction.class).getCode() + "} WHERE {restrictedType}=?type AND LOWER( {code} ) = ?code ", params, Collections.singletonList(SearchRestriction.class), true, true, 0, -1).getResult();
        if(rows.isEmpty())
        {
            return null;
        }
        return rows.get(0);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getSearchRestrictions(Principal principal, ComposedType restrictedType)
    {
        return getSearchRestrictions(principal, restrictedType, false, false, false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<SearchRestriction> getSearchRestrictions(Principal principal, ComposedType type, boolean includeGroups, boolean includeSuperTypes, boolean includeSubtypes)
    {
        Collection<SearchRestriction> ret;
        try
        {
            Set groups = (principal != null && includeGroups) ? principal.getAllGroups() : Collections.EMPTY_SET;
            List<ComposedType> superTypes = (type != null && includeSuperTypes) ? type.getAllSuperTypes() : Collections.EMPTY_LIST;
            Set<ComposedType> subtypes = (type != null && includeSubtypes) ? type.getAllSubTypes() : Collections.EMPTY_SET;
            StringBuilder q = new StringBuilder();
            Map<Object, Object> values = new HashMap<>();
            q.append("SELECT {").append(Item.PK).append("} FROM {").append(getComposedType(SearchRestriction.class).getCode())
                            .append("*} WHERE ");
            if(principal != null)
            {
                q.append("( {").append("principal").append("} IS NULL OR {").append("principal")
                                .append("}");
                values.put("principal", principal);
                if(groups.isEmpty())
                {
                    q.append("=?principal");
                }
                else
                {
                    q.append(" IN ( ?principal, ?groups )");
                    values.put("groups", groups);
                }
                q.append(")");
            }
            else
            {
                q.append("{").append("principal").append("} IS NULL ");
            }
            q.append(" AND ");
            if(type != null)
            {
                q.append("( {").append("restrictedType").append("} IS NULL OR {")
                                .append("restrictedType").append("}");
                values.put("type", type);
                if(superTypes.isEmpty() && subtypes.isEmpty())
                {
                    q.append("=?type");
                    values.put("type", type);
                }
                else if(subtypes.isEmpty() && !superTypes.isEmpty())
                {
                    q.append(" IN ( ?type, ?supertypes )");
                    values.put("supertypes", superTypes);
                }
                else if(!subtypes.isEmpty() && superTypes.isEmpty())
                {
                    q.append(" IN ( ?type, ?subtypes )");
                    values.put("subtypes", subtypes);
                }
                else
                {
                    q.append(" IN ( ?type, ?subtypes, ?supertypes )");
                    values.put("subtypes", subtypes);
                    values.put("supertypes", superTypes);
                }
                q.append(")");
            }
            else
            {
                q.append("{").append("restrictedType").append("} IS NULL ");
            }
            q.append(" AND {").append("active").append("} = ?active ");
            values.put("active", Boolean.TRUE);
            SessionContext myCtx = getSession().getSessionContext();
            boolean useLocalCtx = !Boolean.TRUE.equals(myCtx.getAttribute("disableRestrictions"));
            if(useLocalCtx)
            {
                myCtx = getSession().createLocalSessionContext();
                myCtx.setAttribute("disableRestrictions", Boolean.TRUE);
            }
            try
            {
                ret = getTenant().getJaloConnection().getFlexibleSearch().search(myCtx, q.toString(), values, Collections.singletonList(SearchRestriction.class), true, true, 0, -1).getResult();
            }
            finally
            {
                if(useLocalCtx)
                {
                    getSession().removeLocalSessionContext();
                }
            }
            if(!superTypes.isEmpty() && !ret.isEmpty())
            {
                Map<String, SearchRestriction> restrictionsMap = new HashMap<>();
                Set<SearchRestriction> hidden = null;
                for(SearchRestriction sr : ret)
                {
                    ComposedType t = sr.getRestrictionType();
                    if(superTypes.contains(t) || t.equals(type))
                    {
                        String code = sr.getCode().toLowerCase(LocaleHelper.getPersistenceLocale());
                        SearchRestriction current = restrictionsMap.get(code);
                        if(current == null)
                        {
                            restrictionsMap.put(code, sr);
                            continue;
                        }
                        if(t.equals(type) || superTypes.indexOf(t) < superTypes.indexOf(current.getRestrictionType()))
                        {
                            if(hidden == null)
                            {
                                hidden = new HashSet<>();
                            }
                            hidden.add(current);
                            restrictionsMap.put(code, sr);
                            continue;
                        }
                        if(hidden == null)
                        {
                            hidden = new HashSet<>();
                        }
                        hidden.add(sr);
                    }
                }
                if(hidden != null)
                {
                    ret = new HashSet<>(ret);
                    ret.removeAll(hidden);
                }
            }
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloInternalException(e, "cannot find SearchRestriction type", 0);
        }
        return ret;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public RelationType createRelationType(String relationName, boolean localized, AttributeDescriptor sourceFD, AttributeDescriptor targetFD) throws JaloInvalidParameterException, JaloDuplicateCodeException, JaloDuplicateQualifierException
    {
        return createRelationType(null, relationName, localized, sourceFD, targetFD);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public RelationType createRelationType(PK pk, String relationName, boolean localized, AttributeDescriptor sourceFD, AttributeDescriptor targetFD) throws JaloInvalidParameterException, JaloDuplicateCodeException, JaloDuplicateQualifierException
    {
        checkForRelationAttribute(localized, sourceFD);
        checkForRelationAttribute(localized, targetFD);
        RelationType ret = createRelationTypeInternal(pk, relationName, localized, sourceFD, targetFD);
        if(!(sourceFD instanceof RelationDescriptor))
        {
            WrapperFactory.rewrap((Item)sourceFD);
        }
        if(!(targetFD instanceof RelationDescriptor))
        {
            WrapperFactory.rewrap((Item)targetFD);
        }
        return ret;
    }


    @Deprecated(since = "ages", forRemoval = false)
    private RelationType createRelationTypeInternal(PK pkBase, String relationName, boolean localized, AttributeDescriptor sourceFD, AttributeDescriptor targetFD) throws JaloDuplicateCodeException
    {
        try
        {
            AttributeDescriptorRemote sourceRemote = (AttributeDescriptorRemote)extractRequiredRemoteFromItem((Item)sourceFD, AttributeDescriptorRemote.class);
            AttributeDescriptorRemote targetRemote = (AttributeDescriptorRemote)extractRequiredRemoteFromItem((Item)targetFD, AttributeDescriptorRemote.class);
            return (RelationType)wrap(
                            getTypeManagerEJB().createRelationType(pkBase, relationName, localized, sourceRemote, targetRemote));
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, e.getErrorCode());
        }
        catch(EJBDuplicateCodeException e)
        {
            throw new JaloDuplicateCodeException(e, e.getErrorCode());
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected RelationDescriptor createRelationDescriptor(String relationName, ComposedType enclosingType, String qualifier, ComposedType otherEndItemType, int modifiers, boolean localized) throws JaloDuplicateQualifierException
    {
        return createRelationDescriptor(null, relationName, enclosingType, qualifier, otherEndItemType, modifiers, localized);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected RelationDescriptor createRelationDescriptor(PK pk, String relationName, ComposedType enclosingType, String qualifier, ComposedType otherEndItemType, int modifiers, boolean localized) throws JaloDuplicateQualifierException
    {
        return createRelationAttribute(pk, relationName, enclosingType, qualifier, otherEndItemType, modifiers, localized);
    }


    protected void checkForRelationAttribute(boolean localized, AttributeDescriptor fd)
    {
        if(fd == null)
        {
            throw new JaloInvalidParameterException("relation ends cannot be null ", 0);
        }
        if(fd instanceof RelationDescriptor)
        {
            if(((RelationDescriptor)fd).getRelationType() != null)
            {
                throw new JaloInvalidParameterException("descriptor " + fd + " is already a relation descriptor of " + ((RelationDescriptor)fd)
                                .getRelationType(), 0);
            }
        }
        if(fd.isProperty())
        {
            throw new JaloInvalidParameterException("descriptor " + fd + " cannot be relation descriptor - isProperty=" + fd
                            .isProperty(), 0);
        }
        if(localized != fd.isLocalized())
        {
            throw new JaloInvalidParameterException("descriptor " + fd + " cannot be relation descriptor - relation is " + (
                            localized ? "localized" : "unlocalized") + " but fd is not", 0);
        }
        Type endType = fd.getRealAttributeType();
        if(localized)
        {
            try
            {
                if(!(endType instanceof MapType) ||
                                !getTenant().getJaloConnection().getTypeManager().getRootComposedType(Language.class).isAssignableFrom(((MapType)endType).getArgumentType()) ||
                                !(((MapType)endType).getReturnType() instanceof CollectionType) ||
                                !(((CollectionType)((MapType)endType).getReturnType()).getElementType() instanceof ComposedType))
                {
                    throw new JaloInvalidParameterException("descriptor " + fd + " cannot be localized relation descriptor - it got to be of type MapType(Language,CollectionType(ComposedType))", 0);
                }
            }
            catch(JaloItemNotFoundException e)
            {
                throw new JaloSystemException(e);
            }
        }
        else if(!(endType instanceof CollectionType) || !(((CollectionType)endType).getElementType() instanceof ComposedType))
        {
            throw new JaloInvalidParameterException("descriptor " + fd + " cannot be unlocalized relation descriptor - it got to be of type CollectionType(ComposedType)", 0);
        }
    }


    protected TypeManagerEJB getTypeManagerEJB()
    {
        return (TypeManagerEJB)getRemote();
    }


    public ComposedType getRootComposedTypeForJaloClass(Class jaloClass) throws JaloItemNotFoundException
    {
        try
        {
            return (ComposedType)wrap(getTypeManagerEJB().getRootComposedTypeForJaloClass(
                            CGLibUtils.getOriginalClass(jaloClass).getName()));
        }
        catch(EJBItemNotFoundException e)
        {
            throw new JaloItemNotFoundException(e.getMessage(), e.getErrorCode());
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getRestrictions(Principal principal, ComposedType type)
    {
        if(principal == null && type == null)
        {
            throw new JaloInvalidParameterException("either principal or type must not be NULL", 0);
        }
        return (Collection)wrap(getTypeManagerEJB().getRestrictions((principal != null) ? principal.getPK() : null,
                        (type != null) ? type.getPK() : null));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public RelationDescriptor createRelationAttribute(PK pkBase, String relationName, ComposedType enclosingType, String qualifier, ComposedType attributeType, int modifiers, boolean localized) throws JaloDuplicateQualifierException
    {
        return createRelationAttribute(pkBase, relationName, enclosingType, qualifier, attributeType, modifiers, localized, 0);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public RelationDescriptor createRelationAttribute(PK pkBase, String relationName, ComposedType enclosingType, String qualifier, ComposedType attributeType, int modifiers, boolean localized, int typeOfCollection) throws JaloDuplicateQualifierException
    {
        try
        {
            ComposedTypeRemote enclosingRemote = (ComposedTypeRemote)extractRequiredRemoteFromItem((Item)enclosingType, ComposedTypeRemote.class);
            ComposedTypeRemote attributeRemote = (ComposedTypeRemote)extractRequiredRemoteFromItem((Item)attributeType, ComposedTypeRemote.class);
            return (RelationDescriptor)wrap(getTypeManagerEJB().createRelationDescriptor(pkBase, relationName, qualifier, enclosingRemote, attributeRemote, modifiers, localized, false, typeOfCollection));
        }
        catch(EJBDuplicateQualifierException e)
        {
            throw new JaloDuplicateQualifierException(e, e.getErrorCode());
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, e.getErrorCode());
        }
    }


    protected void assureNoItem(Class<?> c) throws JaloInvalidParameterException
    {
        if(Item.class.isAssignableFrom(c))
        {
            throw new JaloInvalidParameterException("atomic types may not describe items ... use composed types instead", 4711);
        }
    }


    public Object writeReplace() throws ObjectStreamException
    {
        return new TypeManagerSerializableDTO(getTenant());
    }
}
