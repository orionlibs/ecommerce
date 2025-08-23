package de.hybris.bootstrap.ddl;

import org.apache.commons.lang.StringUtils;

class InitUserPasswordService
{
    private static final String INIT_PASSWORD_PROPERTY = "initialpassword.%s";
    private final PropertiesLoader properties;


    InitUserPasswordService(PropertiesLoader properties)
    {
        this.properties = properties;
    }


    String readUserPassword(String userId)
    {
        String requestedPassword = this.properties.getProperty(findPasswordProperty(userId));
        if(StringUtils.isNotBlank(requestedPassword))
        {
            return requestedPassword;
        }
        String msg = String.join(userId, new CharSequence[] {"****************************************************\nThere is no initial password provided for user: '", "'.\nProvide password in order to run initialization correctly.\nSet: 'initialpassword.",
                        "' property.\n****************************************************"});
        System.err.println(msg);
        throw new IllegalStateException(
                        String.format("You must provide password for %s user. Set: 'initialpassword.%s' property", new Object[] {userId, userId}));
    }


    private String findPasswordProperty(String userId)
    {
        return String.format("initialpassword.%s", new Object[] {userId});
    }
}
