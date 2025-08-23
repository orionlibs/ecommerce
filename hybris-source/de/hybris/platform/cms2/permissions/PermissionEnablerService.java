package de.hybris.platform.cms2.permissions;

public interface PermissionEnablerService
{
    boolean isTypeVerifiable(String paramString);


    boolean isAttributeVerifiable(String paramString1, String paramString2);
}
