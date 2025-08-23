package de.hybris.platform.ldap.util;

import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.ldap.LDIF2ImpExConverter;
import de.hybris.platform.ldap.connection.ConnectionData;
import de.hybris.platform.ldap.connection.JNDIConnectionImpl;
import de.hybris.platform.ldap.connection.LDAPConnectionFactory;
import de.hybris.platform.ldap.connection.LDAPGenericObject;
import de.hybris.platform.ldap.exception.LDAPNoSuchEntryException;
import de.hybris.platform.ldap.jalo.LDAPManager;
import de.hybris.platform.ldap.jalo.configuration.ConfigurationParser;
import de.hybris.platform.ldap.jalo.configuration.valueobject.ConfigValueObject;
import de.hybris.platform.util.HacLdapControllerInterface;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.naming.NamingException;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

public class DefaultHacLdapController implements HacLdapControllerInterface
{
    public Map<Object, Object> ldapData()
    {
        Map<Object, Object> result = new HashMap<>();
        try
        {
            JNDIConnectionImpl connection = (JNDIConnectionImpl)LDAPConnectionFactory.getLDAPConnection(new ConnectionData(LDAPManager.getInstance()
                            .getConfig()));
            result.put("serverAvailable", Boolean.valueOf(connection.getConnectionManager().isCurrentServerAvailable()));
        }
        catch(Exception e)
        {
            result.put("serverAvailable", Boolean.FALSE);
            result.put("reason", e.getMessage());
        }
        result.put("ldifFile", System.getProperty("HYBRIS_CONFIG_DIR") + "\\demoserver.ldif");
        result.put("confFile", System.getProperty("HYBRIS_CONFIG_DIR") + "\\configuration.xml");
        return result;
    }


    public Map<Object, Object> ldapCheck(String username, String password)
    {
        Map<Object, Object> result = new HashMap<>();
        StringBuilder log = new StringBuilder();
        boolean success = false;
        try
        {
            User user = UserManager.getInstance().getUserByLogin(username);
            if(LDAPManager.getInstance().isLdapaccountAsPrimitive(user))
            {
                success = LDAPManager.getInstance().checkPassword(user, password.toCharArray());
                if(!success)
                {
                    log.append("Unsuccessful login, please check your LDAP password!");
                }
            }
            else
            {
                throw new LDAPNoSuchEntryException(user.getUID() + " is not a configured LDAP user! Verify the setting of 'LDAP Authentication' in the hMC user editor.)");
            }
        }
        catch(Exception e)
        {
            log.append(StringUtils.capitalize(e.getMessage()));
        }
        result.put("success", Boolean.valueOf(success));
        result.put("message", log.toString());
        return result;
    }


    public Map<Object, Object> ldapImpex(String ldif, @RequestParam String conf)
    {
        Map<Object, Object> result = new HashMap<>();
        String impex = null;
        try
        {
            ConfigurationParser parser = new ConfigurationParser();
            parser.parse(new FileInputStream(conf));
            ConfigValueObject configObj = parser.getConfig();
            LDIF2ImpExConverter importer = new LDIF2ImpExConverter(ldif, configObj);
            importer.generateImpExScript();
            impex = importer.getExportAsString();
            result.put("success", Boolean.TRUE);
            result.put("impex", impex);
        }
        catch(FileNotFoundException e)
        {
            result.put("success", Boolean.FALSE);
            result.put("message", e.getMessage());
        }
        catch(ParseAbortException e)
        {
            result.put("success", Boolean.FALSE);
            result.put("message", e.getMessage());
        }
        catch(UnsupportedEncodingException e)
        {
            result.put("success", Boolean.FALSE);
            result.put("message", e.getMessage());
        }
        catch(JaloBusinessException e)
        {
            result.put("success", Boolean.FALSE);
            result.put("message", e.getMessage());
        }
        return result;
    }


    public Map<Object, Object> ldapSearch(String searchBase, String searchSelect, String searchFilter, Integer searchLimit)
    {
        Map<Object, Object> result = new HashMap<>();
        StringTokenizer tokenizer = new StringTokenizer(searchSelect, ",");
        List<String> list = new ArrayList<>();
        while(tokenizer.hasMoreTokens())
        {
            String token = tokenizer.nextToken().trim();
            list.add(token);
        }
        String[] attrs = list.<String>toArray(new String[list.size()]);
        try
        {
            Collection<LDAPGenericObject> ldapSearchResult = LDAPManager.getInstance().browseLDAP(searchBase, searchFilter, attrs, searchLimit.intValue());
            StringBuilder buf = new StringBuilder();
            for(LDAPGenericObject obj : ldapSearchResult)
            {
                buf.append(obj.getValue()).append("\n");
            }
            result.put("success", Boolean.TRUE);
            result.put("result", buf.toString());
        }
        catch(NamingException e)
        {
            result.put("success", Boolean.FALSE);
            result.put("message", e.getMessage());
        }
        return result;
    }
}
