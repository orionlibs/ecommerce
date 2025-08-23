package de.hybris.platform.servicelayer.security.permissions;

public interface PermissionCheckResult
{
    boolean isGranted();


    boolean isDenied();


    PermissionCheckValue getCheckValue();
}
