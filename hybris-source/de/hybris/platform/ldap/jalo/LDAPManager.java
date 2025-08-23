package de.hybris.platform.ldap.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.jalo.CronJobManager;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.ldap.connection.ConnectionData;
import de.hybris.platform.ldap.connection.LDAPConnection;
import de.hybris.platform.ldap.connection.LDAPConnectionFactory;
import de.hybris.platform.ldap.connection.LDAPGenericObject;
import de.hybris.platform.ldap.constants.GeneratedLDAPConstants;
import de.hybris.platform.ldap.exception.LDAPOperationException;
import de.hybris.platform.ldap.exception.LDAPUnavailableException;
import de.hybris.platform.ldap.util.LDAPInputFilter;
import de.hybris.platform.util.JspContext;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

public class LDAPManager extends GeneratedLDAPManager
{
    private static final Logger LOG = Logger.getLogger(LDAPManager.class.getName());
    private LDAPConfigProxyItem proxy = null;


    public static LDAPManager getInstance()
    {
        return (LDAPManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("ldap");
    }


    public final LDAPConfigProxyItem getConfig()
    {
        if(this.proxy == null)
        {
            this.proxy = new LDAPConfigProxyItem();
        }
        return this.proxy;
    }


    protected final Collection getPlatformUserLogins()
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT {").append("uid").append("} FROM {")
                            .append(TypeManager.getInstance().getComposedType(User.class).getCode()).append("} ");
            query.append("WHERE {").append("uid").append("} like ?uid");
            Map<Object, Object> values = new HashMap<>();
            values.put("uid", "%");
            SearchResult res = JaloSession.getCurrentSession().getFlexibleSearch().search(query.toString(), values,
                            Collections.singletonList(String.class), true, true, 0, -1);
            if(res.getCount() == 0)
            {
                LOG.warn("did not find any login for type " + TypeManager.getInstance().getComposedType(User.class));
            }
            return res.getResult();
        }
        catch(FlexibleSearchException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public Collection<LDAPGenericObject> browseLDAP(String searchbase, String filter, String[] attrs) throws NamingException
    {
        return browseLDAP(searchbase, filter, attrs, -1);
    }


    public Collection<LDAPGenericObject> browseLDAP(String searchbase, String filter, String[] attrs, int limit) throws NamingException
    {
        LDAPConnection connection = null;
        try
        {
            connection = LDAPConnectionFactory.getLDAPConnection(new ConnectionData(getConfig()));
            return connection.searchSubTree(searchbase, filter, limit, -1, attrs);
        }
        catch(LDAPUnavailableException e)
        {
            e.printStackTrace();
        }
        catch(LDAPOperationException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(connection != null)
            {
                connection.close();
            }
        }
        return Collections.EMPTY_LIST;
    }


    public Collection<LDAPGenericObject> browseLDAP(LDAPConfigProxyItem config, String searchbase, String personObjectClass, String searchfield, String searchOperation, String searchvalue, String[] attrs) throws NamingException
    {
        LDAPConnection connection = null;
        searchvalue = (searchvalue != null) ? searchvalue : "*";
        try
        {
            connection = LDAPConnectionFactory.getLDAPConnection(new ConnectionData(config));
            String filter = "(&(objectclass=" + personObjectClass + ")(" + searchfield + searchOperation + searchvalue + "))";
            return connection.searchSubTree(searchbase, filter, -1, -1, attrs);
        }
        catch(LDAPUnavailableException e)
        {
            e.printStackTrace();
        }
        catch(LDAPOperationException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(connection != null)
            {
                connection.close();
            }
        }
        return Collections.EMPTY_LIST;
    }


    public boolean checkPassword(User user, char[] plainPassword)
    {
        if(user == null)
        {
            return false;
        }
        try
        {
            String searchbase = (String)user.getAttribute(GeneratedLDAPConstants.Attributes.Principal.DN);
            String login = (user.getAttribute(GeneratedLDAPConstants.Attributes.User.LDAPLOGIN) != null) ? (String)user.getAttribute(GeneratedLDAPConstants.Attributes.User.LDAPLOGIN) : user.getLogin();
            String domain = (String)user.getAttribute(GeneratedLDAPConstants.Attributes.User.DOMAIN);
            if(domain != null && domain.length() > 0)
            {
                login = domain + "\\" + domain;
            }
            boolean isOK = checkPassword(searchbase, login, plainPassword);
            return isOK;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }


    public boolean checkPassword(String searchbase, String login, char[] plainPassword) throws NamingException
    {
        try
        {
            LDAPConnection con = LDAPConnectionFactory.getLDAPConnection(new ConnectionData(getConfig()));
            return con.checkPassword(searchbase, login, plainPassword);
        }
        catch(LDAPUnavailableException e)
        {
            LOG.error("login: '" + login + "' failed!");
            LOG.error(e.getMessage());
            return false;
        }
    }


    public void createEssentialData(Map _values, JspContext jspc) throws Exception
    {
        if(getInstance().isLdapaccount((User)UserManager.getInstance().getAdminEmployee()) == null)
        {
            getInstance().setLdapaccount((User)UserManager.getInstance().getAdminEmployee(), Boolean.FALSE);
        }
        if(getInstance().isLdapaccount((User)UserManager.getInstance().getAnonymousCustomer()) == null)
        {
            getInstance().setLdapaccount((User)UserManager.getInstance().getAnonymousCustomer(), Boolean.FALSE);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public LDIFImportCronJob createDefaultLDIFImportCronJob(boolean enableCodeExecution)
    {
        return createDefaultLDIFImportCronJob(getOrCreateLDIFImportJob(), enableCodeExecution);
    }


    public LDIFImportCronJob createDefaultLDIFImportCronJob()
    {
        LDIFImportJob job = (LDIFImportJob)CronJobManager.getInstance().getJob("LDIF Import");
        if(job == null)
        {
            return createDefaultLDIFImportCronJob(getOrCreateLDIFImportJob(), true);
        }
        return (job.getCronJobs() != null && !job.getCronJobs().isEmpty()) ? job.getCronJobs().iterator()
                        .next() : null;
    }


    public LDIFGroupImportCronJob createDefaultLDIFGroupImportCronJob()
    {
        LDIFGroupImportJob job = (LDIFGroupImportJob)CronJobManager.getInstance().getJob("LDIF Group Import");
        if(job == null)
        {
            return createDefaultLDIFGroupImportCronJob(getOrCreateLDIFGroupImportJob(), true);
        }
        return (job.getCronJobs() != null && !job.getCronJobs().isEmpty()) ? job.getCronJobs()
                        .iterator()
                        .next() : null;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public LDIFImportCronJob createDefaultLDIFImportCronJob(LDIFImportJob ldifImportJob)
    {
        return createDefaultLDIFImportCronJob(ldifImportJob, false);
    }


    public LDIFImportCronJob createDefaultLDIFImportCronJob(LDIFImportJob ldifImportJob, boolean enableCodeExecution)
    {
        JaloSession jaloSession = JaloSession.getCurrentSession();
        ComposedType ldifCronJob = jaloSession.getTypeManager().getComposedType(LDIFImportCronJob.class);
        Map<Object, Object> values = new HashMap<>();
        try
        {
            LDIFImportJob job;
            if(ldifImportJob == null)
            {
                job = getOrCreateLDIFImportJob();
            }
            else
            {
                job = ldifImportJob;
            }
            values.put("job", job);
            return (LDIFImportCronJob)ldifCronJob.newInstance(values);
        }
        catch(JaloGenericCreationException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public LDIFGroupImportCronJob createDefaultLDIFGroupImportCronJob(LDIFGroupImportJob ldifImportJob, boolean enableCodeExecution)
    {
        JaloSession jaloSession = JaloSession.getCurrentSession();
        ComposedType ldifCronJob = jaloSession.getTypeManager().getComposedType(LDIFGroupImportCronJob.class);
        Map<Object, Object> values = new HashMap<>();
        try
        {
            LDIFGroupImportJob job;
            if(ldifImportJob == null)
            {
                job = getOrCreateLDIFGroupImportJob();
            }
            else
            {
                job = ldifImportJob;
            }
            values.put("job", job);
            return (LDIFGroupImportCronJob)ldifCronJob.newInstance(values);
        }
        catch(JaloGenericCreationException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public LDIFImportJob getOrCreateLDIFImportJob()
    {
        LDIFImportJob job = (LDIFImportJob)CronJobManager.getInstance().getJob("LDIF Import");
        if(job == null)
        {
            Map<Object, Object> map = new HashMap<>();
            map.put("code", "LDIF Import");
            job = createLDIFImportJob(map);
        }
        return job;
    }


    public LDIFGroupImportJob getOrCreateLDIFGroupImportJob()
    {
        LDIFGroupImportJob job = (LDIFGroupImportJob)CronJobManager.getInstance().getJob("LDIF Group Import");
        if(job == null)
        {
            Map<Object, Object> map = new HashMap<>();
            map.put("code", "LDIF Group Import");
            job = createLDIFGroupImportJob(map);
        }
        return job;
    }


    public LDIFImportJob createLDIFImportJob(Map attributeValues)
    {
        return createLDIFImportJob(getSession().getSessionContext(), attributeValues);
    }


    public boolean isCreatorDisabled()
    {
        return false;
    }


    public String cleanse(String input)
    {
        LDAPInputFilter filter = (LDAPInputFilter)Registry.getApplicationContext().getBean("de.hybris.platform.ldap.util.LDAPInputFilter");
        return filter.cleanse(input);
    }
}
