package de.hybris.platform.servicelayer.security.strategies.impl;

import de.hybris.platform.servicelayer.security.permissions.PermissionCheckResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue;
import de.hybris.platform.servicelayer.security.strategies.PermissionCheckValueMappingStrategy;

public class DefaultPermissionCheckValueMappingStrategy implements PermissionCheckValueMappingStrategy
{
    private static final DefaultPermissionCheckResult RESULT_ALLOWED = new DefaultPermissionCheckResult(PermissionCheckValue.ALLOWED);
    private static final DefaultPermissionCheckResult RESULT_DENIED = new DefaultPermissionCheckResult(PermissionCheckValue.DENIED);
    private static final DefaultPermissionCheckResult RESULT_CONFLICTING = new DefaultPermissionCheckResult(PermissionCheckValue.CONFLICTING);
    private static final DefaultPermissionCheckResult RESULT_NOT_DEFINED = new DefaultPermissionCheckResult(PermissionCheckValue.NOT_DEFINED);


    public PermissionCheckResult getPermissionCheckResult(PermissionCheckValue value)
    {
        switch(null.$SwitchMap$de$hybris$platform$servicelayer$security$permissions$PermissionCheckValue[value.ordinal()])
        {
            case 1:
                return (PermissionCheckResult)RESULT_ALLOWED;
            case 2:
                return (PermissionCheckResult)RESULT_DENIED;
            case 3:
                return (PermissionCheckResult)RESULT_CONFLICTING;
            case 4:
                return (PermissionCheckResult)RESULT_NOT_DEFINED;
        }
        throw new IllegalStateException("unrecognized PermissionCheckValue: " + value);
    }
}
