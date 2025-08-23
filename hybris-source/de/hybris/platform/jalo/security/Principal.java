package de.hybris.platform.jalo.security;

import de.hybris.platform.cache.RelationsCache;
import de.hybris.platform.cache.relation.RelationCacheUnit;
import de.hybris.platform.cache.relation.TypeId;
import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.type.SearchRestriction;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.security.ACLCache;
import de.hybris.platform.persistence.security.ACLEntryJDBC;
import de.hybris.platform.persistence.security.EJBSecurityException;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.FlexibleSearchUtils;
import de.hybris.platform.util.Utilities;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Principal extends GeneratedPrincipal
{
    private static final long serialVersionUID = 4959016738634953550L;
    @Deprecated(since = "ages", forRemoval = false)
    public static final String DISPLAYNAME = "displayName";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String ALLSEARCHRESTRICTIONS = "allSearchRestrictions";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String PRINCIPAL_GROUP_RELATION_NAME = GeneratedCoreConstants.Relations.PRINCIPALGROUPRELATION;
    @Deprecated(since = "ages", forRemoval = false)
    public static final String ALLGROUPS = "allGroups";


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return getUid() + "[" + getUid() + "]";
    }


    private String savedUid = null;


    @Deprecated(since = "ages", forRemoval = false)
    public String getUID()
    {
        return getUid(getSession().getSessionContext());
    }


    protected void checkConsistencyUid(String uid, String message, String composedTypeCode) throws ConsistencyCheckException
    {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", uid);
        StringBuffer query = new StringBuffer(60);
        query.append("SELECT {").append(PK).append("} FROM {").append(composedTypeCode).append("} WHERE {").append("uid")
                        .append("}=?uid");
        List<Principal> result = FlexibleSearch.getInstance().search(query.toString(), params, Collections.singletonList(Principal.class), true, true, 0, 1).getResult();
        if(!result.isEmpty())
        {
            throw new ConsistencyCheckException(null, message, 0);
        }
    }


    @SLDSafe
    public String getUid(SessionContext ctx)
    {
        if(this.savedUid == null)
        {
            String uid = super.getUid(ctx);
            if("admin".equalsIgnoreCase(uid) || "anonymous".equalsIgnoreCase(uid))
            {
                this.savedUid = uid;
            }
            return uid;
        }
        return this.savedUid;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setUID(String uid) throws ConsistencyCheckException
    {
        setUid(getSession().getSessionContext(), uid);
    }


    public abstract void checkSystemPrincipal() throws ConsistencyCheckException;


    @SLDSafe
    public Set<PrincipalGroup> getGroups(SessionContext ctx)
    {
        if(isPrincipalGroupRelationCachingEnabled())
        {
            return getFromRelationCache(ctx, GeneratedCoreConstants.Relations.PRINCIPALGROUPRELATION, "groups", getPK());
        }
        return (Set<PrincipalGroup>)(new Object(this, getTenant().getCache(), 201, "groups" +
                        getPK().getLongValueAsString(), ctx))
                        .getCached();
    }


    boolean isPrincipalGroupRelationCachingEnabled()
    {
        return RelationsCache.getDefaultInstance().getSingleCacheableAttributes(TypeId.fromTypeName(GeneratedCoreConstants.Relations.PRINCIPALGROUPRELATION)).containsAnyAttribute();
    }


    <T extends Principal> Set<T> getFromRelationCache(SessionContext ctx, String relation, String manySide, PK ownerPk)
    {
        List<PK> pks = RelationCacheUnit.createRelationCacheUnit(relation, manySide, ownerPk).getCachedPKs();
        return new HashSet<>(JaloSession.lookupItems(ctx, pks, true, false));
    }


    public Set<PrincipalGroup> getAllGroups()
    {
        return getAllGroups(getSession().getSessionContext());
    }


    @SLDSafe(portingClass = "PrincipalAllGroupsAttributeHandler", portingMethod = "get(PrincipalModel model)")
    public Set<PrincipalGroup> getAllGroups(SessionContext ctx)
    {
        Set<PrincipalGroup> ret = new HashSet<>();
        Set<PrincipalGroup> groups = getGroups(ctx);
        while(!groups.isEmpty())
        {
            Set<PrincipalGroup> nextGroups = new HashSet<>();
            for(Iterator<PrincipalGroup> iter = groups.iterator(); iter.hasNext(); )
            {
                PrincipalGroup group = iter.next();
                ret.add(group);
                nextGroups.addAll(group.getGroups());
            }
            nextGroups.removeAll(ret);
            groups = nextGroups;
        }
        return ret;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isMemberOf(PrincipalGroup grp, boolean includingSupergroups)
    {
        if(!includingSupergroups)
        {
            return getGroups().contains(grp);
        }
        Set<PrincipalGroup> done = new HashSet<>();
        Set<PrincipalGroup> groups = getGroups();
        while(groups != null && !groups.isEmpty())
        {
            Set<PrincipalGroup> nextGroups = null;
            for(PrincipalGroup group : groups)
            {
                if(group.equals(grp))
                {
                    return true;
                }
                done.add(group);
                Set<PrincipalGroup> myGroups = group.getGroups();
                if(!myGroups.isEmpty())
                {
                    if(myGroups.contains(grp))
                    {
                        return true;
                    }
                    if(nextGroups == null)
                    {
                        nextGroups = new HashSet<>();
                    }
                    nextGroups.addAll(myGroups);
                }
            }
            if(nextGroups != null)
            {
                nextGroups.removeAll(done);
            }
            groups = nextGroups;
        }
        return false;
    }


    @SLDSafe
    public void setGroups(SessionContext ctx, Set<?> groups)
    {
        getSession().getLinkManager().setLinkedItems(ctx, (Item)this, true, GeneratedCoreConstants.Relations.PRINCIPALGROUPRELATION, null,
                        (groups == null) ? null : new ArrayList(groups), false, false,
                        Utilities.getMarkModifiedOverride(PRINCIPALGROUPRELATION_MARKMODIFIED));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean addToGroup(PrincipalGroup group)
    {
        Set<PrincipalGroup> grps = getGroups();
        addToGroups(group);
        return grps.equals(getGroups());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean removeFromGroup(PrincipalGroup group)
    {
        Set<PrincipalGroup> grps = getGroups();
        removeFromGroups(group);
        return grps.equals(getGroups());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isMemberOf(PrincipalGroup g)
    {
        return isMemberOf(g, false);
    }


    @SLDSafe(portingClass = "PrincipalAllSearchRestrictionsAttributeHandler", portingMethod = "get(final PrincipalModel model)")
    public Collection<SearchRestriction> getAllSearchRestrictions()
    {
        return getAllSearchRestrictions(getSession().getSessionContext());
    }


    @SLDSafe(portingClass = "PrincipalAllSearchRestrictionsAttributeHandler", portingMethod = "get(final PrincipalModel model)")
    public Collection<SearchRestriction> getAllSearchRestrictions(SessionContext ctx)
    {
        try
        {
            Map<Object, Object> values = new HashMap<>();
            Collection<Principal> thisAndAllItsGroups = new ArrayList<>();
            thisAndAllItsGroups.add(this);
            thisAndAllItsGroups.addAll(getAllGroups(ctx));
            StringBuilder query = new StringBuilder();
            query.append("SELECT {").append(Item.PK).append("}");
            query.append(" FROM {").append(GeneratedCoreConstants.TC.SEARCHRESTRICTION).append("}");
            query.append(" WHERE ").append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{principal} IN (?principals)", "principals", "OR", thisAndAllItsGroups, values));
            query.append("ORDER BY ").append("{").append("code").append("} ASC");
            SessionContext myCtx = ctx;
            boolean useLocalCtx = !Boolean.TRUE.equals(myCtx.getAttribute("disableRestrictions"));
            if(useLocalCtx)
            {
                myCtx = getSession().createLocalSessionContext(myCtx);
                myCtx.setAttribute("disableRestrictions", Boolean.TRUE);
            }
            try
            {
                return getSession().getFlexibleSearch()
                                .search(myCtx, query.toString(), values, Collections.singletonList(SearchRestriction.class), true, true, 0, -1)
                                .getResult();
            }
            finally
            {
                if(useLocalCtx)
                {
                    getSession().removeLocalSessionContext();
                }
            }
        }
        catch(JaloItemNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(FlexibleSearchException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }


    public Map getItemPermissionsMap(List userRights)
    {
        return AccessManager.getInstance().getRestrictedItemsMapForPrincipal(this, userRights);
    }


    public void setItemPermissionsByMap(List userRights, Map permissionMap)
    {
        if(userRights == null || userRights.isEmpty())
        {
            throw new JaloInvalidParameterException("userright list was null or empty", 0);
        }
        try
        {
            Transaction.current().execute((TransactionBody)new Object(this, userRights, permissionMap));
        }
        catch(Exception e)
        {
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addGlobalPermission(UserRight permission, boolean deny)
    {
        if(permission == null)
        {
            throw new JaloInvalidParameterException("right cannot be null", 0);
        }
        try
        {
            getImplementation().getPermissionFacade().setGlobalPermission(permission.getPK(), deny);
        }
        catch(EJBSecurityException e)
        {
            throw new JaloInternalException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean addGlobalPermissions(Collection<PermissionContainer> permissions)
    {
        try
        {
            return getImplementation().getPermissionFacade().setGlobalPermissions(permissions);
        }
        catch(EJBSecurityException e)
        {
            throw new JaloInternalException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addGlobalNegativePermission(UserRight permission)
    {
        if(permission == null)
        {
            throw new JaloInvalidParameterException("right cannot be null", 0);
        }
        addGlobalPermission(permission, true);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addGlobalPositivePermission(UserRight permission)
    {
        if(permission == null)
        {
            throw new JaloInvalidParameterException("right cannot be null", 0);
        }
        addGlobalPermission(permission, false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void clearGlobalPermission(UserRight permission)
    {
        if(permission == null)
        {
            throw new JaloInvalidParameterException("right cannot be null", 0);
        }
        try
        {
            getImplementation().getPermissionFacade().removeGlobalPermission(permission.getPK());
        }
        catch(EJBSecurityException e)
        {
            throw new JaloInternalException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean clearGlobalPermissions(Collection<PermissionContainer> permissions)
    {
        try
        {
            return getImplementation().getPermissionFacade().removeGlobalPermissions(permissions);
        }
        catch(EJBSecurityException e)
        {
            throw new JaloInternalException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set getGlobalPermissions(boolean negative)
    {
        Collection<PK> collectionPK = getImplementation().getPermissionFacade().getGlobalPermissionPKs(negative);
        return new HashSet((Collection)UserManager.getInstance().wrap(collectionPK));
    }


    public Set getGlobalPositivePermissions()
    {
        return getGlobalPermissions(false);
    }


    public Set getGlobalNegativePermissions()
    {
        return getGlobalPermissions(true);
    }


    public Collection getAllGlobalPositivePermissions()
    {
        Set ret = new HashSet(getGlobalPositivePermissions());
        Set groups = getGroups();
        while(!groups.isEmpty())
        {
            Set nextGroups = new HashSet();
            for(Iterator<PrincipalGroup> it = groups.iterator(); it.hasNext(); )
            {
                PrincipalGroup g = it.next();
                ret.addAll(g.getGlobalPositivePermissions());
                nextGroups.addAll(g.getGroups());
            }
            groups = nextGroups;
        }
        return ret;
    }


    public Collection getAllGlobalNegativePermissions()
    {
        Set ret = new HashSet(getGlobalNegativePermissions());
        Set groups = getGroups();
        while(!groups.isEmpty())
        {
            Set nextGroups = new HashSet();
            for(Iterator<PrincipalGroup> it = groups.iterator(); it.hasNext(); )
            {
                PrincipalGroup g = it.next();
                ret.addAll(g.getGlobalNegativePermissions());
                nextGroups.addAll(g.getGroups());
            }
            groups = nextGroups;
        }
        return ret;
    }


    public abstract boolean isAdmin();


    @Deprecated(since = "ages", forRemoval = false)
    public int checkOwnGlobalPermission(PK userRightPK)
    {
        return getImplementation().getPermissionFacade().checkOwnGlobalPermission(userRightPK);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean checkGlobalPermission(UserRight right)
    {
        if(isAdmin())
        {
            return true;
        }
        if(right == null)
        {
            throw new JaloInvalidParameterException("right cannot be null", 0);
        }
        int match = checkOwnGlobalPermission(right.getPK());
        if(match != -1)
        {
            return ACLCache.translatePermissionToBoolean(match);
        }
        Set groups = getGroups();
        while(match == -1 && !groups.isEmpty())
        {
            Set nextGroups = new HashSet();
            int proCount = 0;
            int conCount = 0;
            for(Iterator<PrincipalGroup> it = groups.iterator(); it.hasNext(); )
            {
                PrincipalGroup grp = it.next();
                int grpMatch = grp.checkOwnGlobalPermission(right.getPK());
                switch(grpMatch)
                {
                    case 0:
                        proCount++;
                        break;
                    case 1:
                        conCount++;
                        break;
                }
                nextGroups.addAll(grp.getGroups());
            }
            if(proCount > 0)
            {
                match = (conCount > 0) ? 2 : 0;
            }
            else if(conCount > 0)
            {
                match = 1;
            }
            groups = nextGroups;
        }
        return ACLCache.translatePermissionToBoolean(match);
    }


    @SLDSafe
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        super.remove(ctx);
        ACLEntryJDBC.removeAllGlobalEntries(getPK(), Registry.getCurrentTenant().getPersistencePool().getDataSource());
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "PrincipalDisplayNameLocalizedAttributeHandler", portingMethod = "get(final PrincipalModel model)")
    public abstract String getDisplayName(SessionContext paramSessionContext);


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "PrincipalDisplayNameLocalizedAttributeHandler", portingMethod = "get(final PrincipalModel model)")
    public String getDisplayName()
    {
        return getDisplayName(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "PrincipalDisplayNameLocalizedAttributeHandler", portingMethod = "get(final PrincipalModel model, final Locale loc)")
    public abstract Map<Language, String> getAllDisplayName(SessionContext paramSessionContext);


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "PrincipalDisplayNameLocalizedAttributeHandler", portingMethod = "get(final PrincipalModel model, final Locale loc)")
    public Map<Language, String> getAllDisplayName()
    {
        return getAllDisplayName(getSession().getSessionContext());
    }
}
