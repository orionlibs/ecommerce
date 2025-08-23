package de.hybris.platform.jalo.security;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.licence.Licence;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.persistence.meta.MetaInformationEJB;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.persistence.security.ACLEntryJDBC;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.ItemPropertyValueCollection;
import de.hybris.platform.util.Utilities;
import java.io.ObjectStreamException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AccessManager extends Manager
{
    public static final String BEAN_NAME = "core.accessManager";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String READ = "read";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String CHANGE = "change";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String CHANGE_PERMISSIONS = "changerights";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String CREATE = "create";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String REMOVE = "remove";


    public static AccessManager getInstance()
    {
        return Registry.getCurrentTenant().getJaloConnection().getAccessManager();
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
    }


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
        if(item instanceof Principal)
        {
            List userrights = new ArrayList(getAllUserRights());
            if(!userrights.isEmpty())
            {
                ((Principal)item).setItemPermissionsByMap(userrights, Collections.EMPTY_MAP);
            }
        }
    }


    public boolean checkPermissionOn(Item item, Principal principal, UserRight permission)
    {
        return item.checkPermission(principal, permission);
    }


    public boolean checkPermissionOn(Item item, Principal principal, String permissionCode)
    {
        return item.checkPermission(principal, getUserRightByCode(permissionCode));
    }


    public void removePermissionOn(Item item, Principal caller, Principal principal, UserRight permission) throws JaloSecurityException
    {
        item.clearPermission(principal, permission);
    }


    public void addPositivePermissionOn(Item item, Principal caller, Principal principal, UserRight permission) throws JaloSecurityException
    {
        item.addPositivePermission(principal, permission);
    }


    public void addNegativePermissionOn(Item item, Principal caller, Principal principal, UserRight permission) throws JaloSecurityException
    {
        item.addNegativePermission(principal, permission);
    }


    public void addGlobalPermissions(Principal caller, Collection permissions) throws JaloSecurityException
    {
        try
        {
            Map<PK, List> principalMap = new HashMap<>();
            for(Iterator<PermissionContainer> it = permissions.iterator(); it.hasNext(); )
            {
                PermissionContainer pc = it.next();
                List<PermissionContainer> l = principalMap.get(pc.getPrincipalPK());
                if(l == null)
                {
                    principalMap.put(pc.getPrincipalPK(), l = new ArrayList());
                }
                l.add(pc);
            }
            boolean res = false;
            for(Iterator<Map.Entry> iterator = principalMap.entrySet().iterator(); iterator.hasNext(); )
            {
                Map.Entry e = iterator.next();
                Principal principalJalo = (Principal)WrapperFactory.getCachedItem(
                                getTenant().getPersistencePool().getTenant()
                                                .getCache(), (PK)e.getKey());
                res = (principalJalo.addGlobalPermissions((List)e.getValue()) || res);
            }
        }
        catch(JaloInternalException e)
        {
            throw new JaloSecurityException(e.getMessage(), 0);
        }
    }


    public boolean checkPermission(Principal principal, UserRight permission)
    {
        return principal.checkGlobalPermission(permission);
    }


    public boolean checkPermission(Principal principal, String permissionCode)
    {
        return principal.checkGlobalPermission(getUserRightByCode(permissionCode));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeGlobalPermission(Principal caller, Principal principal, UserRight permission) throws JaloSecurityException
    {
        principal.clearGlobalPermission(permission);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addGlobalPositivePermission(Principal caller, Principal principal, UserRight permission) throws JaloSecurityException
    {
        principal.addGlobalPositivePermission(permission);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addGlobalNegativePermission(Principal caller, Principal principal, UserRight permission) throws JaloSecurityException
    {
        principal.addGlobalNegativePermission(permission);
    }


    public Collection getGlobalPositivePermissions(Principal principal)
    {
        return principal.getGlobalPositivePermissions();
    }


    public Collection getGlobalNegativePermissions(Principal principal)
    {
        return principal.getGlobalNegativePermissions();
    }


    public Collection getAllGlobalPositivePermissions(Principal principal)
    {
        return principal.getAllGlobalPositivePermissions();
    }


    public Collection getAllGlobalNegativePermissions(Principal principal)
    {
        return principal.getAllGlobalNegativePermissions();
    }


    public UserRight createUserRight(String code) throws ConsistencyCheckException
    {
        try
        {
            return (UserRight)ComposedType.newInstance(getSession().getSessionContext(), UserRight.class, new Object[] {"code", code});
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
            if(jaloGenericCreationException1 instanceof ConsistencyCheckException)
            {
                throw (ConsistencyCheckException)jaloGenericCreationException1;
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


    public Collection getUserRightsByCode(String code)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        StringBuffer query = new StringBuffer(60);
        query.append("SELECT {").append(Item.PK).append("} FROM {").append(GeneratedCoreConstants.TC.USERRIGHT).append("} WHERE {")
                        .append("code").append("} LIKE ?code");
        return FlexibleSearch.getInstance()
                        .search(query.toString(), params, Collections.singletonList(UserRight.class), true, true, 0, 1)
                        .getResult();
    }


    public UserRight getUserRightByCode(String code)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        StringBuffer query = new StringBuffer(60);
        query.append("SELECT {").append(Item.PK).append("} FROM {").append(GeneratedCoreConstants.TC.USERRIGHT).append("} WHERE {")
                        .append("code").append("}=?code");
        List<UserRight> result = FlexibleSearch.getInstance().search(query.toString(), params, Collections.singletonList(UserRight.class), true, true, 0, 1).getResult();
        if(result.isEmpty())
        {
            return null;
        }
        if(result.size() == 1)
        {
            return result.get(0);
        }
        throw new JaloSystemException("!!", 0);
    }


    public UserRight getOrCreateUserRightByCode(String code)
    {
        UserRight right = getUserRightByCode(code);
        if(right == null)
        {
            try
            {
                right = createUserRight(code);
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e);
            }
        }
        return right;
    }


    public Collection getAllUserRights()
    {
        String query = "SELECT {PK} FROM {" + GeneratedCoreConstants.TC.USERRIGHT + "}";
        return FlexibleSearch.getInstance().search(query, null, Collections.singletonList(UserRight.class), true, true, 0, -1)
                        .getResult();
    }


    public boolean isEditable(Item item, AttributeDescriptor descriptor)
    {
        for(Iterator<Extension> e = ExtensionManager.getInstance().getExtensions().iterator(); e.hasNext(); )
        {
            Extension extension = e.next();
            if(extension instanceof Extension.RightsProvider)
            {
                if(!((Extension.RightsProvider)extension).isEditable(item, descriptor))
                {
                    return false;
                }
            }
        }
        return true;
    }


    public Set<Language> getRestrictedLanguages(Item item)
    {
        if(JaloSession.getCurrentSession().getUser().isAdmin())
        {
            return null;
        }
        Set<Language> result = null;
        for(Extension extension : ExtensionManager.getInstance().getExtensions())
        {
            if(extension instanceof Extension.RestrictedLanguagesProvider)
            {
                Set<? extends Language> restrictedLanguages = ((Extension.RestrictedLanguagesProvider)extension).getRestrictedLanguages(item);
                if(restrictedLanguages != null)
                {
                    if(result == null)
                    {
                        result = new HashSet<>(restrictedLanguages);
                        continue;
                    }
                    result.retainAll(restrictedLanguages);
                }
            }
        }
        return result;
    }


    public ArrayList checkLicence()
    {
        ArrayList<LicenceInfo> result = new ArrayList();
        Licence licence = Licence.getDefaultLicence();
        Date currentDate = new Date();
        if(licence.getExpirationDate() == null)
        {
            result.add(new LicenceInfo(LicenceInfo.Status.INFO, currentDate.toString(), "unlimited", "This licence has no expiration date."));
        }
        else
        {
            long days = licence.getDaysLeft().longValue();
            if(days < 0L)
            {
                result.add(new LicenceInfo(LicenceInfo.Status.ERROR, currentDate
                                .toString(), licence.getExpirationDate().toString(), "This licence has expired since " + -1L * days + " days"));
            }
            else if(0L <= days && days < 3L)
            {
                result.add(new LicenceInfo(LicenceInfo.Status.WARNING, currentDate.toString(), licence
                                .getExpirationDate().toString(), "The licence will expire tomorrow!"));
            }
            else if(3L <= days && days <= 14L)
            {
                result.add(new LicenceInfo(LicenceInfo.Status.WARNING, currentDate.toString(), licence
                                .getExpirationDate().toString(), "The licence will expire in the next " + days + " days"));
            }
            else
            {
                result.add(new LicenceInfo(LicenceInfo.Status.INFO, currentDate
                                .toString(), licence.getExpirationDate().toString(), "" + days + " days left before licence will expire"));
            }
        }
        FlexibleSearch fs = FlexibleSearch.getInstance();
        SessionContext searchCtx = JaloSession.getCurrentSession().createSessionContext();
        searchCtx.setAttribute("disableRestrictions", Boolean.TRUE);
        for(Iterator<Map.Entry<Object, Object>> iter = licence.getLicenceProperties().entrySet().iterator(); iter.hasNext(); )
        {
            Map.Entry element = iter.next();
            String key = (String)element.getKey();
            if(key.startsWith("licence.itemlimit."))
            {
                String item = key.substring(18);
                try
                {
                    TypeManager.getInstance().getComposedType(item);
                    String query = "SELECT {PK} FROM {" + item + "*} ";
                    SearchResult sr = fs.search(searchCtx, query, Collections.EMPTY_MAP,
                                    Collections.singletonList(Item.class), true, true, 0, -1);
                    int currentValue = sr.getResult().size();
                    long allowedValue = Long.parseLong((String)element.getValue());
                    long threshold = Math.round(allowedValue * 0.9D);
                    if(sr.getResult().size() < threshold)
                    {
                        result.add(new LicenceInfo(LicenceInfo.Status.INFO, String.valueOf(currentValue),
                                        String.valueOf(allowedValue), "There are " + currentValue + " out of a maximum of " + allowedValue + " instances of the type " + item + " in the platform."));
                        continue;
                    }
                    if(threshold <= currentValue && currentValue <= allowedValue)
                    {
                        result.add(new LicenceInfo(LicenceInfo.Status.WARNING, String.valueOf(currentValue),
                                        String.valueOf(allowedValue), "Warning! There are " + currentValue + " out of a maximum of " + allowedValue + " instances of the type " + item + " in the platform. The licence limitation is soon reached."));
                        continue;
                    }
                    result.add(new LicenceInfo(LicenceInfo.Status.ERROR, String.valueOf(currentValue),
                                    String.valueOf(allowedValue), "Attention! There are " + currentValue + " out of a maximum of " + allowedValue + " instances of the type " + item + " in the platform. You have to update your licence!"));
                }
                catch(JaloItemNotFoundException e)
                {
                    result.add(new LicenceInfo(LicenceInfo.Status.INFO, "n/a", (String)element.getValue(), "The licence allows using the composedType \"" + item + "\" but currently, the related extension is not active."));
                }
            }
        }
        boolean current_AllowedClustering = licence.isClusteringPermitted();
        boolean clusterMode = Registry.getMasterTenant().isClusteringEnabled();
        if(current_AllowedClustering)
        {
            if(clusterMode)
            {
                result.add(new LicenceInfo(LicenceInfo.Status.INFO, String.valueOf(clusterMode),
                                String.valueOf(current_AllowedClustering), "The licence allows clustering: clustering enabled."));
            }
            else
            {
                result.add(new LicenceInfo(LicenceInfo.Status.WARNING, String.valueOf(clusterMode),
                                String.valueOf(current_AllowedClustering), "The licence allows clustering: clustering disabled."));
            }
        }
        else if(clusterMode)
        {
            result.add(new LicenceInfo(LicenceInfo.Status.ERROR,
                            String.valueOf(clusterMode),
                            String.valueOf(current_AllowedClustering), "Clustering is enabled even though your licence does not allow clustering. Please upgrade your licence if you want to use clustering."));
        }
        else
        {
            result.add(new LicenceInfo(LicenceInfo.Status.INFO, String.valueOf(clusterMode),
                            String.valueOf(current_AllowedClustering), "Your licence does not allow clustering: clustering disabled."));
        }
        boolean hpo = licence.isHighPerformanceOptionPermitted();
        if(hpo)
        {
            result.add(new LicenceInfo(LicenceInfo.Status.INFO, "n/a", String.valueOf(hpo), "hybris High Performance Option is licenced."));
        }
        else
        {
            result.add(new LicenceInfo(LicenceInfo.Status.INFO, "n/a", String.valueOf(hpo), "hybris High Performance Option is not licenced."));
        }
        boolean aso = licence.isAdvancedSecurityPermitted();
        if(aso)
        {
            result.add(new LicenceInfo(LicenceInfo.Status.INFO, "n/a", String.valueOf(aso), "hybris Advanced Security Option is licenced."));
        }
        else
        {
            result.add(new LicenceInfo(LicenceInfo.Status.INFO, "n/a", String.valueOf(aso), "hybris Advanced Security Option is not licenced."));
        }
        int allowedCacheLimit = licence.getCacheLimit();
        int currentCacheValue = Registry.getCurrentTenant().getCache().getMaxAllowedSize();
        long cachethreshold = Math.round(allowedCacheLimit * 0.9D);
        if(currentCacheValue < 0)
        {
            result.add(new LicenceInfo(LicenceInfo.Status.ERROR, String.valueOf(currentCacheValue),
                            String.valueOf(allowedCacheLimit), "The current cache value is negative!"));
        }
        else if(0 <= currentCacheValue && currentCacheValue < cachethreshold)
        {
            result.add(new LicenceInfo(LicenceInfo.Status.INFO, String.valueOf(currentCacheValue),
                            String.valueOf(allowedCacheLimit), "The current cache value is " + currentCacheValue + ". The cache limit is " + allowedCacheLimit + "."));
        }
        else if(cachethreshold <= currentCacheValue && currentCacheValue <= allowedCacheLimit)
        {
            result.add(new LicenceInfo(LicenceInfo.Status.WARNING, String.valueOf(currentCacheValue),
                            String.valueOf(allowedCacheLimit), "Attention! The current cache value is " + currentCacheValue + ". The cache limit is " + allowedCacheLimit + ". The licence limit is soon reached!"));
        }
        else
        {
            result.add(new LicenceInfo(LicenceInfo.Status.ERROR, String.valueOf(currentCacheValue),
                            String.valueOf(allowedCacheLimit), "The current cache value (" + currentCacheValue + ") exceeds the licenced cache limit of " + allowedCacheLimit + ". You have to update your licence!"));
        }
        return result;
    }


    public Map getRestrictedItemsMapForPrincipal(Principal p, List permissionList)
    {
        ItemPropertyValue principalRef = new ItemPropertyValue(p.getPK());
        return (Map)wrap(
                        getACLEntriesForPrincipal(principalRef,
                                        (List)WrapperFactory.unwrap(getTenant().getCache(), permissionList, true)));
    }


    private Map getACLEntriesForPrincipal(ItemPropertyValue principal, List<?> permissions)
    {
        if(permissions == null || permissions.isEmpty() || principal == null)
        {
            throw new JaloSystemException(null, "invalid params for getACLEntriesForPrincipal : principal = " + principal + ", userright list = " + permissions, 0);
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getTenant().getPersistencePool().getDataSource().getConnection();
            StringBuilder sb = new StringBuilder();
            List<PK> values = new ArrayList(permissions);
            sb.append("SELECT ").append("ItemPK").append(", ").append("PermissionPK").append(", ")
                            .append("Negative");
            sb.append(" FROM " + ACLEntryJDBC.ACLENTRIES_TABLE() + " WHERE ").append("ItemPK").append(" <> ? AND ")
                            .append("PrincipalPK").append(" = ? AND ");
            sb.append("PermissionPK").append(" IN (");
            values.add(0, MetaInformationEJB.DEFAULT_PRIMARY_KEY);
            values.add(1, principal.getPK());
            for(int i = 0, s = permissions.size(); i < s; i++)
            {
                sb.append("?");
                if(i + 1 < s)
                {
                    sb.append(",");
                }
            }
            sb.append(")");
            stmt = conn.prepareStatement(sb.toString());
            stmt.setFetchSize(1000);
            JDBCValueMappings vm = JDBCValueMappings.getInstance();
            JDBCValueMappings.ValueReader<PK, ?> pkReader = vm.PK_READER;
            int j = 1;
            for(Iterator<PK> it = values.iterator(); it.hasNext(); j++)
            {
                Object o = it.next();
                JDBCValueMappings.ValueWriter vw = vm.getValueWriter(o.getClass());
                vw.setValue(stmt, j, o);
            }
            rs = stmt.executeQuery();
            List<Object[]> result = new LinkedList();
            while(rs.next())
            {
                result.add(new Object[] {new ItemPropertyValue((PK)pkReader
                                .getValue(rs, 1)), new ItemPropertyValue((PK)pkReader
                                .getValue(rs, 2)),
                                rs.getBoolean(3) ? Boolean.TRUE : Boolean.FALSE});
            }
            Map<Object, Object> ret = new HashMap<>();
            for(Iterator<Object[]> iterator = result.iterator(); iterator.hasNext(); )
            {
                Object[] row = iterator.next();
                ItemPropertyValue item = (ItemPropertyValue)row[0];
                ItemPropertyValue permission = (ItemPropertyValue)row[1];
                Boolean negative = (Boolean)row[2];
                List<?> itemPermissionRow = (List)ret.get(item);
                if(itemPermissionRow == null)
                {
                    ret.put(item, itemPermissionRow = new ArrayList(permissions));
                    Collections.fill(itemPermissionRow, null);
                }
                itemPermissionRow.set(permissions.indexOf(permission), negative);
            }
            return ret;
        }
        catch(SQLException e)
        {
            throw new EJBInternalException(e, "error getting item permission map for '" + principal + "' and permissions " + permissions + ": " + e, 0);
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, stmt, rs);
        }
    }


    public Collection getGlobalRestrictedPrincipals(UserRight permission)
    {
        return (Collection)wrap(getGlobalRestrictedPrincipals(permission.getPK()));
    }


    public Collection getGlobalRestrictedPrincipals(PK permissionPK)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();
        try
        {
            conn = getTenant().getPersistencePool().getDataSource().getConnection();
            sb.append("SELECT DISTINCT ").append("PrincipalPK").append(" ");
            sb.append("FROM " + ACLEntryJDBC.ACLENTRIES_TABLE() + " ");
            sb.append("WHERE ").append("ItemPK").append("=? AND ").append("PermissionPK").append("=?");
            stmt = conn.prepareStatement(sb.toString());
            stmt.setFetchSize(1000);
            JDBCValueMappings vm = JDBCValueMappings.getInstance();
            JDBCValueMappings.ValueWriter<PK, ?> pkWriter = vm.PK_WRITER;
            JDBCValueMappings.ValueReader<PK, ?> pkReader = vm.PK_READER;
            pkWriter.setValue(stmt, 1, MetaInformationEJB.DEFAULT_PRIMARY_KEY);
            pkWriter.setValue(stmt, 2, permissionPK);
            rs = stmt.executeQuery();
            ItemPropertyValueCollection<ItemPropertyValue> itemPropertyValueCollection = new ItemPropertyValueCollection();
            while(rs.next())
            {
                itemPropertyValueCollection.add(new ItemPropertyValue((PK)pkReader.getValue(rs, 1)));
            }
            return (Collection)itemPropertyValueCollection;
        }
        catch(SQLException e)
        {
            throw new EJBInternalException(e, "error getting global restricted principals for permission '" + permissionPK + "' ( query = " + sb
                            .toString() + "):" + e, 0);
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, stmt, rs);
        }
    }


    public Object writeReplace() throws ObjectStreamException
    {
        return new AccessManagerSerializableDTO(getTenant());
    }
}
