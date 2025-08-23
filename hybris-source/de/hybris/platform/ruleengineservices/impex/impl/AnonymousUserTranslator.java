package de.hybris.platform.ruleengineservices.impex.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.SingleValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.user.UserConstants;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

public class AnonymousUserTranslator extends SingleValueTranslator
{
    protected static final String USER_SERVICE = "userService";
    private boolean usePk;


    public void init(StandardColumnDescriptor descriptor)
    {
        super.init(descriptor);
        this.usePk = Boolean.valueOf(Config.getParameter("ruleengineservices.target.customer.condition.use.pk")).booleanValue();
    }


    protected Object convertToJalo(String value, Item item)
    {
        return isUsePk() ?
                        value.replace(UserConstants.ANONYMOUS_CUSTOMER_UID, getUserService().getAnonymousUser().getPk().getLongValueAsString()) : value;
    }


    protected String convertToString(Object object)
    {
        return object.toString();
    }


    protected boolean isUsePk()
    {
        return this.usePk;
    }


    protected UserService getUserService()
    {
        return (UserService)Registry.getApplicationContext().getBean("userService", UserService.class);
    }
}
