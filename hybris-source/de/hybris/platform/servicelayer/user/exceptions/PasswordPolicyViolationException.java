package de.hybris.platform.servicelayer.user.exceptions;

import de.hybris.platform.servicelayer.user.PasswordPolicyViolation;
import java.util.List;

public class PasswordPolicyViolationException extends RuntimeException
{
    private final List<PasswordPolicyViolation> policyViolations;


    public PasswordPolicyViolationException(List<PasswordPolicyViolation> policyViolations)
    {
        this.policyViolations = policyViolations;
    }


    public List<PasswordPolicyViolation> getPolicyViolations()
    {
        return this.policyViolations;
    }
}
