package de.hybris.platform.cockpit.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class PerspectivePermissionsDeniedException extends AuthenticationException
{
    public PerspectivePermissionsDeniedException(String msg)
    {
        super(msg);
    }
}
