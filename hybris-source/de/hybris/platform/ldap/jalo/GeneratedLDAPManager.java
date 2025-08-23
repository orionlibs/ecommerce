package de.hybris.platform.ldap.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.ldap.constants.GeneratedLDAPConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedLDAPManager extends Extension
{
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("ldapsearchbase", Item.AttributeMode.INITIAL);
        tmp.put("DN", Item.AttributeMode.INITIAL);
        tmp.put("CN", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.security.Principal", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("ldapaccount", Item.AttributeMode.INITIAL);
        tmp.put("domain", Item.AttributeMode.INITIAL);
        tmp.put("ldaplogin", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.user.User", Collections.unmodifiableMap(tmp));
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public String getCN(SessionContext ctx, Principal item)
    {
        return (String)item.getProperty(ctx, GeneratedLDAPConstants.Attributes.Principal.CN);
    }


    public String getCN(Principal item)
    {
        return getCN(getSession().getSessionContext(), item);
    }


    public void setCN(SessionContext ctx, Principal item, String value)
    {
        item.setProperty(ctx, GeneratedLDAPConstants.Attributes.Principal.CN, value);
    }


    public void setCN(Principal item, String value)
    {
        setCN(getSession().getSessionContext(), item, value);
    }


    public ConfigurationMedia createConfigurationMedia(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedLDAPConstants.TC.CONFIGURATIONMEDIA);
            return (ConfigurationMedia)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ConfigurationMedia : " + e.getMessage(), 0);
        }
    }


    public ConfigurationMedia createConfigurationMedia(Map attributeValues)
    {
        return createConfigurationMedia(getSession().getSessionContext(), attributeValues);
    }


    public LDAPConfigProxyItem createLDAPConfigProxyItem(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedLDAPConstants.TC.LDAPCONFIGPROXYITEM);
            return (LDAPConfigProxyItem)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating LDAPConfigProxyItem : " + e.getMessage(), 0);
        }
    }


    public LDAPConfigProxyItem createLDAPConfigProxyItem(Map attributeValues)
    {
        return createLDAPConfigProxyItem(getSession().getSessionContext(), attributeValues);
    }


    public LDIFGroupImportCronJob createLDIFGroupImportCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedLDAPConstants.TC.LDIFGROUPIMPORTCRONJOB);
            return (LDIFGroupImportCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating LDIFGroupImportCronJob : " + e.getMessage(), 0);
        }
    }


    public LDIFGroupImportCronJob createLDIFGroupImportCronJob(Map attributeValues)
    {
        return createLDIFGroupImportCronJob(getSession().getSessionContext(), attributeValues);
    }


    public LDIFGroupImportJob createLDIFGroupImportJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedLDAPConstants.TC.LDIFGROUPIMPORTJOB);
            return (LDIFGroupImportJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating LDIFGroupImportJob : " + e.getMessage(), 0);
        }
    }


    public LDIFGroupImportJob createLDIFGroupImportJob(Map attributeValues)
    {
        return createLDIFGroupImportJob(getSession().getSessionContext(), attributeValues);
    }


    public LDIFImportCronJob createLDIFImportCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedLDAPConstants.TC.LDIFIMPORTCRONJOB);
            return (LDIFImportCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating LDIFImportCronJob : " + e.getMessage(), 0);
        }
    }


    public LDIFImportCronJob createLDIFImportCronJob(Map attributeValues)
    {
        return createLDIFImportCronJob(getSession().getSessionContext(), attributeValues);
    }


    public LDIFImportJob createLDIFImportJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedLDAPConstants.TC.LDIFIMPORTJOB);
            return (LDIFImportJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating LDIFImportJob : " + e.getMessage(), 0);
        }
    }


    public LDIFImportJob createLDIFImportJob(Map attributeValues)
    {
        return createLDIFImportJob(getSession().getSessionContext(), attributeValues);
    }


    public LDIFMedia createLDIFMedia(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedLDAPConstants.TC.LDIFMEDIA);
            return (LDIFMedia)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating LDIFMedia : " + e.getMessage(), 0);
        }
    }


    public LDIFMedia createLDIFMedia(Map attributeValues)
    {
        return createLDIFMedia(getSession().getSessionContext(), attributeValues);
    }


    public String getDN(SessionContext ctx, Principal item)
    {
        return (String)item.getProperty(ctx, GeneratedLDAPConstants.Attributes.Principal.DN);
    }


    public String getDN(Principal item)
    {
        return getDN(getSession().getSessionContext(), item);
    }


    public void setDN(SessionContext ctx, Principal item, String value)
    {
        item.setProperty(ctx, GeneratedLDAPConstants.Attributes.Principal.DN, value);
    }


    public void setDN(Principal item, String value)
    {
        setDN(getSession().getSessionContext(), item, value);
    }


    public String getDomain(SessionContext ctx, User item)
    {
        return (String)item.getProperty(ctx, GeneratedLDAPConstants.Attributes.User.DOMAIN);
    }


    public String getDomain(User item)
    {
        return getDomain(getSession().getSessionContext(), item);
    }


    public void setDomain(SessionContext ctx, User item, String value)
    {
        item.setProperty(ctx, GeneratedLDAPConstants.Attributes.User.DOMAIN, value);
    }


    public void setDomain(User item, String value)
    {
        setDomain(getSession().getSessionContext(), item, value);
    }


    public String getName()
    {
        return "ldap";
    }


    public Boolean isLdapaccount(SessionContext ctx, User item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedLDAPConstants.Attributes.User.LDAPACCOUNT);
    }


    public Boolean isLdapaccount(User item)
    {
        return isLdapaccount(getSession().getSessionContext(), item);
    }


    public boolean isLdapaccountAsPrimitive(SessionContext ctx, User item)
    {
        Boolean value = isLdapaccount(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isLdapaccountAsPrimitive(User item)
    {
        return isLdapaccountAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setLdapaccount(SessionContext ctx, User item, Boolean value)
    {
        item.setProperty(ctx, GeneratedLDAPConstants.Attributes.User.LDAPACCOUNT, value);
    }


    public void setLdapaccount(User item, Boolean value)
    {
        setLdapaccount(getSession().getSessionContext(), item, value);
    }


    public void setLdapaccount(SessionContext ctx, User item, boolean value)
    {
        setLdapaccount(ctx, item, Boolean.valueOf(value));
    }


    public void setLdapaccount(User item, boolean value)
    {
        setLdapaccount(getSession().getSessionContext(), item, value);
    }


    public String getLdaplogin(SessionContext ctx, User item)
    {
        return (String)item.getProperty(ctx, GeneratedLDAPConstants.Attributes.User.LDAPLOGIN);
    }


    public String getLdaplogin(User item)
    {
        return getLdaplogin(getSession().getSessionContext(), item);
    }


    public void setLdaplogin(SessionContext ctx, User item, String value)
    {
        item.setProperty(ctx, GeneratedLDAPConstants.Attributes.User.LDAPLOGIN, value);
    }


    public void setLdaplogin(User item, String value)
    {
        setLdaplogin(getSession().getSessionContext(), item, value);
    }


    public String getLdapsearchbase(SessionContext ctx, Principal item)
    {
        return (String)item.getProperty(ctx, GeneratedLDAPConstants.Attributes.Principal.LDAPSEARCHBASE);
    }


    public String getLdapsearchbase(Principal item)
    {
        return getLdapsearchbase(getSession().getSessionContext(), item);
    }


    public void setLdapsearchbase(SessionContext ctx, Principal item, String value)
    {
        item.setProperty(ctx, GeneratedLDAPConstants.Attributes.Principal.LDAPSEARCHBASE, value);
    }


    public void setLdapsearchbase(Principal item, String value)
    {
        setLdapsearchbase(getSession().getSessionContext(), item, value);
    }
}
