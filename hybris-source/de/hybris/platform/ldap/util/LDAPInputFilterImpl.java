package de.hybris.platform.ldap.util;

import org.apache.log4j.Logger;

public class LDAPInputFilterImpl implements LDAPInputFilter
{
    private static final Logger log = Logger.getLogger(LDAPInputFilterImpl.class.getName());


    public String cleanse(String input)
    {
        if(log.isDebugEnabled())
        {
            log.debug("cleansing: " + input);
        }
        return input;
    }
}
