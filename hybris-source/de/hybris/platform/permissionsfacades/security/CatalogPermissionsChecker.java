package de.hybris.platform.permissionsfacades.security;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface CatalogPermissionsChecker
{
    boolean hasAccessToCatalog(Authentication paramAuthentication, HttpServletRequest paramHttpServletRequest, String paramString1, String paramString2);
}
