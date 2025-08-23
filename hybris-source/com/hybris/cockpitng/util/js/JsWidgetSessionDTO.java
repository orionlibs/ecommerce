/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.js;

import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * DTO for storing session related data (user name, current locale), which will be exposed to JS widgets
 */
public class JsWidgetSessionDTO
{
    private String currentUser;
    private boolean admin;
    private Locale currentLocale;
    private Locale[] enabledDataLocales;
    private AuthorityGroup activeAuthorityGroup;
    final private Map<String, Object> customAttributes = new HashMap<>();


    public String getCurrentUser()
    {
        return currentUser;
    }


    public void setCurrentUser(final String currentUser)
    {
        this.currentUser = currentUser;
    }


    public boolean isAdmin()
    {
        return admin;
    }


    public void setAdmin(final boolean admin)
    {
        this.admin = admin;
    }


    public Locale getCurrentLocale()
    {
        return currentLocale;
    }


    public void setCurrentLocale(final Locale currentLocale)
    {
        this.currentLocale = currentLocale;
    }


    public Locale[] getEnabledDataLocales()
    {
        return enabledDataLocales;
    }


    public void setEnabledDataLocales(final Locale[] enabledDataLocales)
    {
        this.enabledDataLocales = enabledDataLocales;
    }


    public AuthorityGroup getActiveAuthorityGroup()
    {
        return activeAuthorityGroup;
    }


    public void setActiveAuthorityGroup(final AuthorityGroup activeAuthorityGroup)
    {
        this.activeAuthorityGroup = activeAuthorityGroup;
    }


    public Map<String, Object> getCustomAttributes()
    {
        return customAttributes;
    }


    public void addCustomAttribute(final String key, final Object value)
    {
        customAttributes.put(key, value);
    }
}
