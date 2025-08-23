package de.hybris.platform.ldap.jalo;

import de.hybris.platform.cronjob.jalo.MediaProcessCronJob;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedLDIFGroupImportCronJob extends MediaProcessCronJob
{
    public static final String SEARCHBASE = "searchbase";
    public static final String LDAPQUERY = "ldapquery";
    public static final String RESULTFILTER = "resultfilter";
    public static final String USERSEARCHFIELDQUALIFIER = "userSearchFieldQualifier";
    public static final String USERROOTDN = "userRootDN";
    public static final String USERRESULTFILTER = "userResultfilter";
    public static final String IMPORTUSERS = "importUsers";
    public static final String CODEEXECUTION = "codeExecution";
    public static final String LDIFFILE = "ldifFile";
    public static final String CONFIGFILE = "configFile";
    public static final String DESTMEDIA = "destMedia";
    public static final String DUMPMEDIA = "dumpMedia";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(MediaProcessCronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("searchbase", Item.AttributeMode.INITIAL);
        tmp.put("ldapquery", Item.AttributeMode.INITIAL);
        tmp.put("resultfilter", Item.AttributeMode.INITIAL);
        tmp.put("userSearchFieldQualifier", Item.AttributeMode.INITIAL);
        tmp.put("userRootDN", Item.AttributeMode.INITIAL);
        tmp.put("userResultfilter", Item.AttributeMode.INITIAL);
        tmp.put("importUsers", Item.AttributeMode.INITIAL);
        tmp.put("codeExecution", Item.AttributeMode.INITIAL);
        tmp.put("ldifFile", Item.AttributeMode.INITIAL);
        tmp.put("configFile", Item.AttributeMode.INITIAL);
        tmp.put("destMedia", Item.AttributeMode.INITIAL);
        tmp.put("dumpMedia", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isCodeExecution(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "codeExecution");
    }


    public Boolean isCodeExecution()
    {
        return isCodeExecution(getSession().getSessionContext());
    }


    public boolean isCodeExecutionAsPrimitive(SessionContext ctx)
    {
        Boolean value = isCodeExecution(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isCodeExecutionAsPrimitive()
    {
        return isCodeExecutionAsPrimitive(getSession().getSessionContext());
    }


    public void setCodeExecution(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "codeExecution", value);
    }


    public void setCodeExecution(Boolean value)
    {
        setCodeExecution(getSession().getSessionContext(), value);
    }


    public void setCodeExecution(SessionContext ctx, boolean value)
    {
        setCodeExecution(ctx, Boolean.valueOf(value));
    }


    public void setCodeExecution(boolean value)
    {
        setCodeExecution(getSession().getSessionContext(), value);
    }


    public ConfigurationMedia getConfigFile(SessionContext ctx)
    {
        return (ConfigurationMedia)getProperty(ctx, "configFile");
    }


    public ConfigurationMedia getConfigFile()
    {
        return getConfigFile(getSession().getSessionContext());
    }


    public void setConfigFile(SessionContext ctx, ConfigurationMedia value)
    {
        setProperty(ctx, "configFile", value);
    }


    public void setConfigFile(ConfigurationMedia value)
    {
        setConfigFile(getSession().getSessionContext(), value);
    }


    public ImpExMedia getDestMedia(SessionContext ctx)
    {
        return (ImpExMedia)getProperty(ctx, "destMedia");
    }


    public ImpExMedia getDestMedia()
    {
        return getDestMedia(getSession().getSessionContext());
    }


    public void setDestMedia(SessionContext ctx, ImpExMedia value)
    {
        setProperty(ctx, "destMedia", value);
    }


    public void setDestMedia(ImpExMedia value)
    {
        setDestMedia(getSession().getSessionContext(), value);
    }


    public ImpExMedia getDumpMedia(SessionContext ctx)
    {
        return (ImpExMedia)getProperty(ctx, "dumpMedia");
    }


    public ImpExMedia getDumpMedia()
    {
        return getDumpMedia(getSession().getSessionContext());
    }


    public void setDumpMedia(SessionContext ctx, ImpExMedia value)
    {
        setProperty(ctx, "dumpMedia", value);
    }


    public void setDumpMedia(ImpExMedia value)
    {
        setDumpMedia(getSession().getSessionContext(), value);
    }


    public Boolean isImportUsers(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "importUsers");
    }


    public Boolean isImportUsers()
    {
        return isImportUsers(getSession().getSessionContext());
    }


    public boolean isImportUsersAsPrimitive(SessionContext ctx)
    {
        Boolean value = isImportUsers(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isImportUsersAsPrimitive()
    {
        return isImportUsersAsPrimitive(getSession().getSessionContext());
    }


    public void setImportUsers(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "importUsers", value);
    }


    public void setImportUsers(Boolean value)
    {
        setImportUsers(getSession().getSessionContext(), value);
    }


    public void setImportUsers(SessionContext ctx, boolean value)
    {
        setImportUsers(ctx, Boolean.valueOf(value));
    }


    public void setImportUsers(boolean value)
    {
        setImportUsers(getSession().getSessionContext(), value);
    }


    public String getLdapquery(SessionContext ctx)
    {
        return (String)getProperty(ctx, "ldapquery");
    }


    public String getLdapquery()
    {
        return getLdapquery(getSession().getSessionContext());
    }


    public void setLdapquery(SessionContext ctx, String value)
    {
        setProperty(ctx, "ldapquery", value);
    }


    public void setLdapquery(String value)
    {
        setLdapquery(getSession().getSessionContext(), value);
    }


    public LDIFMedia getLdifFile(SessionContext ctx)
    {
        return (LDIFMedia)getProperty(ctx, "ldifFile");
    }


    public LDIFMedia getLdifFile()
    {
        return getLdifFile(getSession().getSessionContext());
    }


    public void setLdifFile(SessionContext ctx, LDIFMedia value)
    {
        setProperty(ctx, "ldifFile", value);
    }


    public void setLdifFile(LDIFMedia value)
    {
        setLdifFile(getSession().getSessionContext(), value);
    }


    public String getResultfilter(SessionContext ctx)
    {
        return (String)getProperty(ctx, "resultfilter");
    }


    public String getResultfilter()
    {
        return getResultfilter(getSession().getSessionContext());
    }


    public void setResultfilter(SessionContext ctx, String value)
    {
        setProperty(ctx, "resultfilter", value);
    }


    public void setResultfilter(String value)
    {
        setResultfilter(getSession().getSessionContext(), value);
    }


    public String getSearchbase(SessionContext ctx)
    {
        return (String)getProperty(ctx, "searchbase");
    }


    public String getSearchbase()
    {
        return getSearchbase(getSession().getSessionContext());
    }


    public void setSearchbase(SessionContext ctx, String value)
    {
        setProperty(ctx, "searchbase", value);
    }


    public void setSearchbase(String value)
    {
        setSearchbase(getSession().getSessionContext(), value);
    }


    public String getUserResultfilter(SessionContext ctx)
    {
        return (String)getProperty(ctx, "userResultfilter");
    }


    public String getUserResultfilter()
    {
        return getUserResultfilter(getSession().getSessionContext());
    }


    public void setUserResultfilter(SessionContext ctx, String value)
    {
        setProperty(ctx, "userResultfilter", value);
    }


    public void setUserResultfilter(String value)
    {
        setUserResultfilter(getSession().getSessionContext(), value);
    }


    public String getUserRootDN(SessionContext ctx)
    {
        return (String)getProperty(ctx, "userRootDN");
    }


    public String getUserRootDN()
    {
        return getUserRootDN(getSession().getSessionContext());
    }


    public void setUserRootDN(SessionContext ctx, String value)
    {
        setProperty(ctx, "userRootDN", value);
    }


    public void setUserRootDN(String value)
    {
        setUserRootDN(getSession().getSessionContext(), value);
    }


    public String getUserSearchFieldQualifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "userSearchFieldQualifier");
    }


    public String getUserSearchFieldQualifier()
    {
        return getUserSearchFieldQualifier(getSession().getSessionContext());
    }


    public void setUserSearchFieldQualifier(SessionContext ctx, String value)
    {
        setProperty(ctx, "userSearchFieldQualifier", value);
    }


    public void setUserSearchFieldQualifier(String value)
    {
        setUserSearchFieldQualifier(getSession().getSessionContext(), value);
    }
}
