package de.hybris.platform.servicelayer.user.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.servicelayer.user.PasswordPolicyViolation;

public class DefaultPasswordPolicyViolation implements PasswordPolicyViolation
{
    private final String id;
    private final String localizedMsg;


    public DefaultPasswordPolicyViolation(String id, String localizedMsg)
    {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(localizedMsg);
        this.id = id;
        this.localizedMsg = localizedMsg;
    }


    public String getId()
    {
        return this.id;
    }


    public String getLocalizedMessage()
    {
        return this.localizedMsg;
    }
}
