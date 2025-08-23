package de.hybris.platform.jalo.user;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Region;
import de.hybris.platform.jalo.numberseries.NumberSeriesConstants;
import de.hybris.platform.jalo.numberseries.NumberSeriesManager;
import de.hybris.platform.jalo.security.PasswordEncoderNotFoundException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.PrincipalGroup;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.security.EJBPasswordEncoderNotFoundException;
import de.hybris.platform.persistence.security.PasswordEncoder;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.JaloPropertyContainer;
import java.io.ObjectStreamException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

public class UserManager extends Manager
{
    private static final Logger LOG = Logger.getLogger(UserManager.class.getName());
    public static final String BEAN_NAME = "core.userManager";
    private static final String LOGIN_TOKEN_URL_ENABLED = "login.token.url.enabled";
    private static PasswordCheckingStrategy passwordCheckingStrategy = (PasswordCheckingStrategy)new DefaultUserPasswordCheckingStrategy();
    private volatile PK anonymousPK = null;
    private volatile PK adminPK = null;
    private volatile Set<PK> allAdminPks;
    public static final String FIELD_NAME = "fieldName";
    public static final String SEARCH_PATTERN = "searchPattern";


    public void init()
    {
        InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.addInvalidationListener((InvalidationListener)new UserManagerInvalidationListener());
        Registry.registerTenantListener((TenantListener)new Object(this));
    }


    private void cleanCachedValues()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Cleaning cache" + getTenant() + " manager = " + this + " values (" + Joiner.on(",")
                            .join("anonymous:",
                                            (this.anonymousPK == null) ? "null" : this.anonymousPK, new Object[] {"admin:",
                                                            (this.adminPK == null) ? "null" : this.adminPK, "alladmin:",
                                                            (this.allAdminPks == null) ? "null" : this.allAdminPks}) + ")");
        }
        this.anonymousPK = null;
        this.adminPK = null;
        this.allAdminPks = null;
    }


    public static UserManager getInstance()
    {
        return Registry.getCurrentTenant().getJaloConnection().getUserManager();
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
        if(item instanceof Principal)
        {
            ((Principal)item).checkSystemPrincipal();
        }
    }


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
    }


    protected String getUserTypeCode()
    {
        try
        {
            return getSession().getTypeManager().getComposedType(User.class).getCode();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "User type code not found", 0);
        }
    }


    protected String getUserGroupTypeCode()
    {
        try
        {
            return getSession().getTypeManager().getComposedType(UserGroup.class).getCode();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "UserGroup type code not found", 0);
        }
    }


    protected String getTitleTypeCode()
    {
        try
        {
            return getSession().getTypeManager().getComposedType(Title.class).getCode();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "Title type code not found", 0);
        }
    }


    protected String getAddressTypeCode()
    {
        try
        {
            return getSession().getTypeManager().getComposedType(Address.class).getCode();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "Address type code not found", 0);
        }
    }


    protected ComposedType getCustomerType()
    {
        try
        {
            return getSession().getTypeManager().getComposedType(Customer.class);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "Customer type code not found", 0);
        }
    }


    protected ComposedType getEmployeeType()
    {
        try
        {
            return getSession().getTypeManager().getComposedType(Employee.class);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "Employee type code not found", 0);
        }
    }


    public Collection getAllUsers()
    {
        return findUsers(null, null, null, null);
    }


    public Collection findUsers(ComposedType type, String uid, String name, String description)
    {
        return findUsers(type, uid, name, description, false);
    }


    public Collection<? extends User> findUsers(ComposedType type, String uid, String name, String description, boolean disableRestrictions)
    {
        String typeCode = (type != null) ? type.getCode() : getUserTypeCode();
        Map values = Maps.newHashMapWithExpectedSize(3);
        String query = createQueryForPrincipalWithValues(typeCode, uid, name, description, values);
        return getSearchResult(query, values, disableRestrictions, User.class);
    }


    private String createQueryForPrincipalWithValues(String typeCode, String uid, String name, String description, Map<String, String> values)
    {
        StringBuilder query = (new StringBuilder("SELECT {")).append(Item.PK).append("} FROM {").append(typeCode).append("}");
        boolean firstCondition = true;
        if(uid != null)
        {
            if(firstCondition)
            {
                query.append(" WHERE ");
            }
            query.append("{").append("uid").append("} = ?uid");
            values.put("uid", uid);
            firstCondition = false;
        }
        if(name != null)
        {
            if(firstCondition)
            {
                query.append(" WHERE ");
            }
            else
            {
                query.append(" AND ");
            }
            query.append("{").append("name").append("} = ?name");
            values.put("name", name);
            firstCondition = false;
        }
        if(description != null)
        {
            if(firstCondition)
            {
                query.append(" WHERE ");
            }
            else
            {
                query.append(" AND ");
            }
            query.append("{").append("description").append("} = ?description");
            values.put("description", description);
        }
        query.append(" ORDER BY {uid}");
        return query.toString();
    }


    private <T> Collection<? extends T> getSearchResult(String query, Map values, boolean disableRestrictions, Class<T> clazz)
    {
        try
        {
            SessionContext ctx = getSession().getSessionContext();
            if(disableRestrictions)
            {
                ctx = getSession().createLocalSessionContext(ctx);
                ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            }
            SearchResult res = getSession().getFlexibleSearch().search(ctx, query, values,
                            Collections.singletonList(clazz), true, true, 0, -1);
            return res.getResult();
        }
        finally
        {
            if(disableRestrictions)
            {
                getSession().removeLocalSessionContext();
            }
        }
    }


    public Collection getAllPrincipals()
    {
        Collection principals = new ArrayList();
        principals.addAll(findUsers(null, null, null, null));
        principals.addAll(getAllUserGroups());
        return principals;
    }


    public Collection getAllCustomers()
    {
        return findUsers(getCustomerType(), null, null, null);
    }


    public Collection getAllEmployees()
    {
        return findUsers(getEmployeeType(), null, null, null);
    }


    public Collection getAllUserGroups()
    {
        return findUserGroups(null, null, null, null);
    }


    public Collection findUserGroups(ComposedType type, String groupId, String name, String description)
    {
        return findUserGroups(type, groupId, name, description, false);
    }


    public Collection<? extends UserGroup> findUserGroups(ComposedType type, String groupId, String name, String description, boolean disableRestrictions)
    {
        String typeCode = (type != null) ? type.getCode() : getUserGroupTypeCode();
        Map values = Maps.newHashMapWithExpectedSize(3);
        String query = createQueryForPrincipalWithValues(typeCode, groupId, name, description, values);
        return getSearchResult(query, values, disableRestrictions, UserGroup.class);
    }


    public Collection getAllTitles()
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getTitleTypeCode() + "} ORDER BY {code} ASC", null,
                        Collections.singletonList(Title.class), true, true, 0, -1);
        return Collections.unmodifiableCollection(res.getResult());
    }


    public Collection getAllAddresses()
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getAddressTypeCode() + "} ORDER BY {" + Address.USER + "} ASC, {" + Item.CREATION_TIME + "} DESC", null,
                        Collections.singletonList(Address.class), true, true, 0, -1);
        return Collections.unmodifiableCollection(res.getResult());
    }


    public Customer getCustomerByLogin(String login) throws JaloItemNotFoundException
    {
        if(login != null)
        {
            Collection<Customer> customers = findUsers(getCustomerType(), login, null, null);
            if(customers.isEmpty())
            {
                throw new JaloItemNotFoundException("could not find customer with login \"" + login + "\"", 0);
            }
            return customers.iterator().next();
        }
        throw new NullPointerException("customer login was null");
    }


    public Employee getEmployeeByLogin(String login) throws JaloItemNotFoundException
    {
        if(login != null)
        {
            Collection<Employee> emloyees = findUsers(getEmployeeType(), login, null, null);
            if(emloyees.isEmpty())
            {
                throw new JaloItemNotFoundException("could not find employee with login \"" + login + "\"", 0);
            }
            return emloyees.iterator().next();
        }
        throw new NullPointerException("employee login was null");
    }


    public User getUserByLogin(String login) throws JaloItemNotFoundException
    {
        if(login != null)
        {
            Collection<User> users = findUsers(getSession().getTypeManager().getComposedType(User.class), login, null, null);
            if(users.isEmpty())
            {
                throw new JaloItemNotFoundException("could not find user with login \"" + login + "\"", 0);
            }
            return users.iterator().next();
        }
        throw new IllegalArgumentException("user login was <null>");
    }


    public UserGroup getUserGroupByGroupID(String groupID) throws JaloItemNotFoundException
    {
        if(groupID != null)
        {
            Collection<UserGroup> userGroupCollection = findUserGroups(null, groupID, null, null);
            if(userGroupCollection.isEmpty())
            {
                throw new JaloItemNotFoundException("could not find usergroup for groupid \"" + groupID + "\"", 0);
            }
            return userGroupCollection.iterator().next();
        }
        throw new NullPointerException("group ID was null");
    }


    public Customer createCustomer(String login) throws ConsistencyCheckException
    {
        return createCustomer(null, login);
    }


    public Customer createCustomer(PK pk, String login) throws ConsistencyCheckException
    {
        try
        {
            return (Customer)ComposedType.newInstance(getSession().getSessionContext(), Customer.class, new Object[] {Customer.PK, pk, "uid", login});
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


    public Employee createEmployee(String login) throws ConsistencyCheckException
    {
        return createEmployee(null, login);
    }


    public Employee createEmployee(PK pk, String login) throws ConsistencyCheckException
    {
        return (Employee)createUser(pk, login, TypeManager.getInstance().getComposedType(Employee.class));
    }


    public UserGroup createUserGroup(String uid) throws ConsistencyCheckException
    {
        return createUserGroup(null, uid);
    }


    public UserGroup createUserGroup(PK pkBase, String groupId) throws ConsistencyCheckException
    {
        try
        {
            return (UserGroup)ComposedType.newInstance(getSession().getSessionContext(), UserGroup.class, new Object[] {Item.PK, pkBase, "uid", groupId});
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


    public User createUser(String uid) throws ConsistencyCheckException
    {
        return createUser(null, uid);
    }


    public User createUser(PK pk, String uid) throws ConsistencyCheckException
    {
        return createUser(pk, uid, TypeManager.getInstance().getComposedType(User.class));
    }


    public User createUser(PK pkBase, String userID, ComposedType type) throws ConsistencyCheckException
    {
        try
        {
            Item.ItemAttributeMap params = new Item.ItemAttributeMap();
            params.put(Item.PK, pkBase);
            params.put("uid", userID);
            if(type == null)
            {
                type = TypeManager.getInstance().getComposedType(User.class);
            }
            return (User)type.newInstance(getSession().getSessionContext(), (Map)params);
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


    public Title createTitle(String code) throws ConsistencyCheckException
    {
        return createTitle(null, code);
    }


    public Title createTitle(PK pkBase, String code) throws ConsistencyCheckException
    {
        try
        {
            return
                            (Title)ComposedType.newInstance(getSession().getSessionContext(), Title.class, new Object[] {Item.PK, pkBase, "code", code});
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


    public Title getTitleByCode(String code) throws JaloItemNotFoundException
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getTitleTypeCode() + "} WHERE {code} = ?code ORDER BY {" + Item.CREATION_TIME + "} DESC",
                        Collections.singletonMap("code", code),
                        Collections.singletonList(Address.class), true, true, 0, -1);
        Collection<Title> coll = res.getResult();
        if(coll.size() < 1)
        {
            throw new JaloItemNotFoundException("Title with code '" + code + "' wasn't found", 0);
        }
        return coll.iterator().next();
    }


    public Employee getAdminEmployee()
    {
        Employee ret = null;
        if(this.adminPK != null)
        {
            ret = (Employee)getSession().getItem(this.adminPK);
        }
        else
        {
            List<Employee> users = (List)findUsers(getEmployeeType(), Constants.USER.ADMIN_EMPLOYEE, null, null, true);
            if(users.isEmpty())
            {
                throw new JaloInternalException(null, "admin user not found", 0);
            }
            ret = users.get(0);
            this.adminPK = ret.getPK();
        }
        return ret;
    }


    public boolean isAdmin(User user)
    {
        PK upk = user.getPK();
        return ((canUseCachedAdminPKs() && isCachedAdminPK(upk)) || isAdminNoCache(upk));
    }


    public boolean isAdmin(Employee employee)
    {
        PK upk = employee.getPK();
        return (upk.equals(this.adminPK) || (canUseCachedAdminPKs() && isCachedAdminPK(upk)) || isAdminNoCache(upk));
    }


    public boolean isAdmin(Customer customer)
    {
        if(isCustomersAllowedAsAdmin())
        {
            PK upk = customer.getPK();
            if(upk.equals(this.anonymousPK))
            {
                return false;
            }
            if(canUseCachedAdminPKs())
            {
                return isCachedAdminPK(upk);
            }
            return isAdminNoCache(upk);
        }
        return false;
    }


    private boolean isAdminNoCache(PK upk)
    {
        return loadAllAdminUserPKs().contains(upk);
    }


    private static Object[] DUMMY_KEY = new Object[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY,
                    Integer.toString(201)};
    public static final String CONFIG_ALLOW_CUSTOMER_ADMIN = "allow.customer.admin";
    public static final boolean ALLOW_CUSTOMER_ADMIN_DEFAULT = false;
    private static volatile Boolean allowCustomerAsAdminCache;


    private boolean canUseCachedAdminPKs()
    {
        Transaction tx = Transaction.current();
        return (!tx.isRunning() || !tx.isInvalidated(DUMMY_KEY));
    }


    private boolean isCachedAdminPK(PK upk)
    {
        Set<PK> adminPKs = this.allAdminPks;
        if(adminPKs == null)
        {
            adminPKs = loadAllAdminUserPKs();
            this.allAdminPks = adminPKs;
        }
        return adminPKs.contains(upk);
    }


    private Set<PK> loadAllAdminUserPKs()
    {
        Set<PK> tmp = new HashSet<>();
        tmp.add(getAdminEmployee().getPK());
        Set<PrincipalGroup> allGroups = new HashSet<>();
        SessionContext ctx = new SessionContext();
        ctx.setAttribute("disableRestrictions", Boolean.TRUE);
        ctx.setAttribute("disableRestrictionGroupInheritance", Boolean.TRUE);
        Set<PrincipalGroup> groups = (Set)Collections.singleton(getAdminUserGroup());
        while(CollectionUtils.isNotEmpty(groups))
        {
            Set<PrincipalGroup> nextGroups = null;
            for(PrincipalGroup group : groups)
            {
                if(allGroups.add(group))
                {
                    for(Principal p : group.getMembers(ctx))
                    {
                        if(p instanceof User)
                        {
                            tmp.add(p.getPK());
                            continue;
                        }
                        if(nextGroups == null)
                        {
                            nextGroups = new HashSet<>();
                        }
                        nextGroups.add((PrincipalGroup)p);
                    }
                }
            }
            groups = nextGroups;
        }
        return Collections.unmodifiableSet(tmp);
    }


    public static boolean isCustomersAllowedAsAdmin()
    {
        Boolean ret = allowCustomerAsAdminCache;
        if(ret == null)
        {
            ret = Boolean.valueOf(Config.getBoolean("allow.customer.admin", false));
            allowCustomerAsAdminCache = ret;
        }
        return ret.booleanValue();
    }


    public Customer getAnonymousCustomer()
    {
        Customer ret = null;
        if(this.anonymousPK != null)
        {
            ret = (Customer)getSession().getItem(this.anonymousPK);
        }
        else
        {
            List<Customer> users = (List)findUsers(getCustomerType(), Constants.USER.ANONYMOUS_CUSTOMER, null, null, true);
            if(users.isEmpty())
            {
                throw new JaloInternalException(null, "anonymous customer not found", 0);
            }
            ret = users.get(0);
            this.anonymousPK = ret.getPK();
            Preconditions.checkArgument((ret.getTenant() == getTenant()));
        }
        return ret;
    }


    public UserGroup getAdminUserGroup()
    {
        Collection<UserGroup> users = (Collection)findUserGroups(null, Constants.USER.ADMIN_USERGROUP, null, null, true);
        if(users.isEmpty())
        {
            throw new JaloInternalException(null, "admin user group not found", 0);
        }
        return users.iterator().next();
    }


    public Collection getAllAddresses(Item owner)
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getAddressTypeCode() + "} WHERE {" + Item.OWNER + "} = ?" + Item.OWNER + " AND {original} IS NULL ORDER BY {" + Item.CREATION_TIME + "} DESC",
                        Collections.singletonMap(Item.OWNER, owner), Collections.singletonList(Address.class), true, true, 0, -1);
        return res.getResult();
    }


    public Address createAddress(Item item)
    {
        return createAddress(null, item);
    }


    public Address createAddress(PK pkBase, Item item, boolean asCopy)
    {
        try
        {
            Address address = (Address)ComposedType.newInstance(getSession().getSessionContext(), Address.class, new Object[] {Item.PK, pkBase, Address.OWNER, item});
            if(asCopy)
            {
                address.setOriginal(address);
                address.setDuplicate(Boolean.TRUE);
            }
            return address;
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


    public Address createAddress(PK pkBase, Item item)
    {
        try
        {
            return (Address)ComposedType.newInstance(getSession().getSessionContext(), Address.class, new Object[] {Item.PK, pkBase, Address.OWNER, item});
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


    protected Address createAddress(PK pkBase, Item owner, Address original, Country country, Region region, Title title, JaloPropertyContainer props)
    {
        try
        {
            return (Address)ComposedType.newInstance(getSession().getSessionContext(), Address.class, new Object[] {
                            Item.PK, pkBase, Address.OWNER, owner, "original", original, "country", country, "region", region,
                            "title", title});
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


    public String generateCustomerID()
    {
        NumberSeriesManager nsm = NumberSeriesManager.getInstance();
        String id = nsm.getUniqueNumber("customer_id", NumberSeriesConstants.Series.CUSTOMER_ID_DIGITS);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("generated new customer ID " + id);
        }
        return id;
    }


    public LoginToken getLoginToken(HttpServletRequest request)
    {
        CookieBasedLoginToken cookieBasedLoginToken;
        if(request == null)
        {
            return null;
        }
        LoginToken token = null;
        StringBasedLoginToken stringBasedLoginToken = getURLBasedLoginToken(request);
        if(stringBasedLoginToken == null)
        {
            cookieBasedLoginToken = getCookieBasedLoginToken(request);
        }
        return (LoginToken)cookieBasedLoginToken;
    }


    public CookieBasedLoginToken getCookieBasedLoginToken(HttpServletRequest request)
    {
        if(request == null)
        {
            return null;
        }
        Cookie cookie = getLoginTokenCookie(Config.getParameter("login.token.name"), request);
        return (cookie != null) ? new CookieBasedLoginToken(cookie) : null;
    }


    public StringBasedLoginToken getURLBasedLoginToken(HttpServletRequest request)
    {
        if(request == null || !isURLBasedLoginTokenEnabled())
        {
            return null;
        }
        if(request.getParameter(Config.getParameter("login.token.url.parameter")) != null)
        {
            return new StringBasedLoginToken(request.getParameter(Config.getParameter("login.token.url.parameter")));
        }
        return null;
    }


    private boolean isURLBasedLoginTokenEnabled()
    {
        return Config.getBoolean("login.token.url.enabled", false);
    }


    private Cookie getLoginTokenCookie(String name, HttpServletRequest request)
    {
        if(request == null)
        {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        boolean found = false;
        if(cookies != null)
        {
            for(int i = 0; i < cookies.length; i++)
            {
                if(cookies[i] != null && cookies[i].getName().equals(name))
                {
                    return cookies[i];
                }
            }
        }
        return null;
    }


    @Deprecated(since = "2011", forRemoval = true)
    public String generateLoginTokenCookieValue(String uid, String languageISO, String plainTextPassword) throws EJBPasswordEncoderNotFoundException
    {
        return doGenerateLoginTokenCookieValue(uid, languageISO, plainTextPassword, getUserByLogin(uid), null);
    }


    public String generateLoginTokenCookieValue(User user, String languageISO) throws EJBPasswordEncoderNotFoundException
    {
        return doGenerateLoginTokenCookieValue(user.getUid(), languageISO, null, user);
    }


    public String generateLoginTokenCookieValue(String uid, String languageISO, String plainTextPassword, Integer ttl) throws EJBPasswordEncoderNotFoundException
    {
        return doGenerateLoginTokenCookieValue(uid, languageISO, plainTextPassword, getUserByLogin(uid), ttl);
    }


    @Deprecated(since = "2011", forRemoval = true)
    protected String doGenerateLoginTokenCookieValue(String uid, String languageISO, String plainTextPassword, User user) throws EJBPasswordEncoderNotFoundException
    {
        return doGenerateLoginTokenCookieValue(uid, languageISO, plainTextPassword, user, null);
    }


    protected String doGenerateLoginTokenCookieValue(String uid, String languageISO, String plainTextPassword, User user, Integer ttl) throws EJBPasswordEncoderNotFoundException
    {
        String del = Config.getParameter("login.token.delimiter");
        Integer ttlValue = Integer.valueOf((ttl == null) ? Config.getInt("login.token.ttl", 15000) : ttl.intValue());
        TokenParams paramsForToken = (new TokenParams.Builder()).withUid(uid).withLanguageIso(languageISO).withPlainTextPassword(plainTextPassword).withUser(user).withTTL(ttlValue).withDelimiter(del).build();
        return TokenGeneratorProvider.getInstance().getTokenGenerator().generateToken(paramsForToken);
    }


    protected String getEncodedPasswordForLoginCookie(String uid, String plainTextPassword, User user) throws EJBPasswordEncoderNotFoundException
    {
        if(plainTextPassword != null)
        {
            return getLoginTokenPasswordEncoder(user).encode(uid, plainTextPassword);
        }
        if(isPlainTextPasswordStored(user))
        {
            return getLoginTokenPasswordEncoder(user).encode(user.getUID(), user.getEncodedPassword());
        }
        return user.getEncodedPassword();
    }


    protected String getEncodedPasswordWithSalt(User user, String passwordToEncode, String saltToAdd)
    {
        PasswordEncoder encoder = getEncoder(user);
        return encoder.encode(user.getUid(), passwordToEncode + passwordToEncode);
    }


    private PasswordEncoder getEncoder(User user)
    {
        String encoding = (user.getPasswordEncoding() != null) ? user.getPasswordEncoding() : "*";
        PasswordEncoder enc = Registry.getCurrentTenant().getJaloConnection().getPasswordEncoder(encoding);
        if(enc instanceof de.hybris.platform.persistence.security.PlainTextPasswordEncoder)
        {
            return Registry.getCurrentTenant().getJaloConnection()
                            .getPasswordEncoder(Config.getParameter("login.token.encoder"));
        }
        return enc;
    }


    public void storeLoginTokenCookie(String uid, String language, String passwd, HttpServletResponse response) throws EJBPasswordEncoderNotFoundException
    {
        storeLoginTokenCookie(uid, language, passwd, Config.getParameter("login.token.path"), response);
    }


    public void storeLoginTokenCookie(String uid, String language, String passwd, String path, HttpServletResponse response) throws EJBPasswordEncoderNotFoundException
    {
        storeLoginTokenCookie(Config.getParameter("login.token.name"), uid, language, passwd, path, response);
    }


    public void storeLoginTokenCookie(String name, String uid, String language, String passwd, String path, HttpServletResponse response) throws EJBPasswordEncoderNotFoundException
    {
        storeLoginTokenCookie(name, uid, language, passwd, path, Config.getParameter("login.token.domain"),
                        Boolean.parseBoolean(Config.getParameter("login.token.secure")),
                        Integer.parseInt(Config.getParameter("login.token.ttl")), response);
    }


    public void storeLoginTokenCookie(String name, String uid, String language, String passwd, String path, String domain, boolean secure, int ttl, HttpServletResponse response) throws EJBPasswordEncoderNotFoundException
    {
        String encryptedValue, plainTextValue = generateLoginTokenCookieValue(uid, language, passwd, Integer.valueOf(ttl));
        if(response == null || plainTextValue == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Cookie creation aborted! User '" + uid + "' still exists?");
            }
            return;
        }
        try
        {
            encryptedValue = Registry.getMasterTenant().getValueEncryptor().encrypt(plainTextValue);
        }
        catch(InvalidKeyException | java.security.NoSuchAlgorithmException | java.security.NoSuchProviderException | javax.crypto.NoSuchPaddingException | java.security.InvalidAlgorithmParameterException | javax.crypto.IllegalBlockSizeException | javax.crypto.BadPaddingException |
              java.io.UnsupportedEncodingException e)
        {
            throw new JaloSystemException(e);
        }
        Cookie cookie = new Cookie(name, encryptedValue);
        cookie.setMaxAge(ttl);
        cookie.setPath(path);
        cookie.setHttpOnly(true);
        if(domain != null && domain.length() > 0)
        {
            cookie.setDomain(domain);
        }
        cookie.setSecure(secure);
        response.addCookie(cookie);
    }


    public void deleteLoginTokenCookie(HttpServletRequest request, HttpServletResponse response)
    {
        if(request == null || response == null)
        {
            return;
        }
        Cookie[] cookies = request.getCookies();
        boolean found = false;
        if(cookies != null)
        {
            for(int i = 0; i < cookies.length && !found; i++)
            {
                if(cookies[i] != null && cookies[i].getName().equals(Config.getParameter("login.token.name")))
                {
                    found = true;
                    cookies[i].setMaxAge(0);
                    cookies[i].setPath(Config.getParameter("login.token.path"));
                    if(Config.getParameter("login.token.domain") != null &&
                                    Config.getParameter("login.token.domain").trim().length() > 0)
                    {
                        cookies[i].setDomain(Config.getParameter("login.token.domain"));
                    }
                    cookies[i].setSecure(Boolean.parseBoolean(Config.getParameter("login.token.secure")));
                    response.addCookie(cookies[i]);
                }
            }
        }
    }


    protected boolean isPlainTextPasswordStored(User user)
    {
        String encoding = user.getPasswordEncoding();
        if(encoding == null)
        {
            return true;
        }
        try
        {
            return Registry.getCurrentTenant()
                            .getJaloConnection()
                            .getPasswordEncoder(encoding) instanceof de.hybris.platform.persistence.security.PlainTextPasswordEncoder;
        }
        catch(PasswordEncoderNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
    }


    protected PasswordEncoder getLoginTokenPasswordEncoder(User user) throws EJBPasswordEncoderNotFoundException
    {
        return Registry.getCurrentTenant().getJaloConnection().getPasswordEncoder(Config.getParameter("login.token.encoder"));
    }


    public Object writeReplace() throws ObjectStreamException
    {
        return new UserManagerSerializableDTO(getTenant());
    }


    public boolean checkPassword(User user, String plainPassword) throws EJBPasswordEncoderNotFoundException
    {
        return passwordCheckingStrategy.checkPassword(user, plainPassword);
    }


    public boolean checkPassword(User user, LoginToken token) throws EJBPasswordEncoderNotFoundException
    {
        String hashedPassword = token.getPassword();
        String encodedPasswordFromDB = getEncodedPassword(user);
        if(verifyIfNewEnhancedTokenIsUsed(token))
        {
            PasswordEncoder passwordEncoder = getEncoder(user);
            return passwordEncoder.check(user.getUid(), hashedPassword, encodedPasswordFromDB + encodedPasswordFromDB);
        }
        return encodedPasswordFromDB.equals(hashedPassword);
    }


    private boolean verifyIfNewEnhancedTokenIsUsed(LoginToken token)
    {
        return (token instanceof CookieBasedLoginToken && Config.getBoolean("login.token.extended", true));
    }


    private String getEncodedPassword(User user)
    {
        String encoding = (user.getPasswordEncoding() != null) ? user.getPasswordEncoding() : "*";
        PasswordEncoder enc = Registry.getCurrentTenant().getJaloConnection().getPasswordEncoder(encoding);
        if(enc instanceof de.hybris.platform.persistence.security.PlainTextPasswordEncoder)
        {
            PasswordEncoder tokenEncoder = Registry.getCurrentTenant().getJaloConnection().getPasswordEncoder(Config.getParameter("login.token.encoder"));
            return tokenEncoder.encode(user.getUid(), user.getEncodedPassword());
        }
        return user.getEncodedPassword();
    }


    public void setPasswordCheckingStrategy(PasswordCheckingStrategy paramPasswordCheckingStrategy)
    {
        passwordCheckingStrategy = paramPasswordCheckingStrategy;
    }


    public PasswordCheckingStrategy getPasswordCheckingStrategy()
    {
        return passwordCheckingStrategy;
    }


    public void removeItem(Item item) throws ConsistencyCheckException
    {
        if(!(item instanceof Address) && !(item instanceof User) && !(item instanceof UserGroup))
        {
            throw new JaloSystemException(null, "UserManager is not responsible for removing item: " + item.getClass().getName(), 0);
        }
        item.remove();
    }
}
