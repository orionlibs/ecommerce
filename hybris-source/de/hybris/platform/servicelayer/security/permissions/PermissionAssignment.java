package de.hybris.platform.servicelayer.security.permissions;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class PermissionAssignment
{
    private final String permissionName;
    private final PrincipalModel principal;
    private final boolean denied;


    public PermissionAssignment(String permissionName, PrincipalModel principal)
    {
        this(permissionName, principal, false);
    }


    public PermissionAssignment(String permissionName, PrincipalModel principal, boolean denied)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("permissionName", permissionName);
        ServicesUtil.validateParameterNotNullStandardMessage("principal", principal);
        this.permissionName = permissionName;
        this.principal = principal;
        this.denied = denied;
    }


    public String getPermissionName()
    {
        return this.permissionName;
    }


    public PrincipalModel getPrincipal()
    {
        return this.principal;
    }


    public boolean isDenied()
    {
        return this.denied;
    }


    public boolean isGranted()
    {
        return !this.denied;
    }


    public boolean equals(Object obj)
    {
        if(obj == this)
        {
            return true;
        }
        if(obj instanceof PermissionAssignment)
        {
            PermissionAssignment other = (PermissionAssignment)obj;
            return (this.permissionName.equals(other.permissionName) && this.principal.equals(other.principal) && this.denied == other.denied);
        }
        return false;
    }


    public int hashCode()
    {
        return (this.permissionName + "_" + this.permissionName + this.principal.getUid()).hashCode();
    }


    public String toString()
    {
        return "\"" + this.principal + "\" is " + (isGranted() ? "granted" : "denied") + " \"" + this.permissionName + "\" permission";
    }
}
