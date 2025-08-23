package de.hybris.platform.ldap.jalo;

import de.hybris.platform.cronjob.jalo.MediaProcessCronJob;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedLDIFImportCronJob extends MediaProcessCronJob
{
    public static final String SEARCHBASE = "searchbase";
    public static final String LDAPQUERY = "ldapquery";
    public static final String RESULTFILTER = "resultfilter";
    public static final String IMPORTMODE = "importmode";
    public static final String LDIFFILE = "ldifFile";
    public static final String CONFIGFILE = "configFile";
    public static final String CONFIGFILE2 = "configFile2";
    public static final String DESTMEDIA = "destMedia";
    public static final String DUMPMEDIA = "dumpMedia";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(MediaProcessCronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("searchbase", Item.AttributeMode.INITIAL);
        tmp.put("ldapquery", Item.AttributeMode.INITIAL);
        tmp.put("resultfilter", Item.AttributeMode.INITIAL);
        tmp.put("importmode", Item.AttributeMode.INITIAL);
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


    public ConfigurationMedia getConfigFile2()
    {
        return getConfigFile2(getSession().getSessionContext());
    }


    public void setConfigFile2(ConfigurationMedia value)
    {
        setConfigFile2(getSession().getSessionContext(), value);
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


    public EnumerationValue getImportmode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "importmode");
    }


    public EnumerationValue getImportmode()
    {
        return getImportmode(getSession().getSessionContext());
    }


    public void setImportmode(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "importmode", value);
    }


    public void setImportmode(EnumerationValue value)
    {
        setImportmode(getSession().getSessionContext(), value);
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


    public abstract ConfigurationMedia getConfigFile2(SessionContext paramSessionContext);


    public abstract void setConfigFile2(SessionContext paramSessionContext, ConfigurationMedia paramConfigurationMedia);
}
