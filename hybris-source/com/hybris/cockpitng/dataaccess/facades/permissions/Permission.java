/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.permissions;

public class Permission
{
    private boolean inherited;
    private boolean denied;
    private final String name;
    private final String principal;
    private final String type;
    private final String field;


    public Permission(final boolean inherited, final boolean denied, final String name, final String principal, final String type,
                    final String field)
    {
        this.inherited = inherited;
        this.denied = denied;
        this.name = name;
        this.principal = principal;
        this.type = type;
        this.field = field;
    }


    public boolean isInherited()
    {
        return inherited;
    }


    public boolean isDenied()
    {
        return denied;
    }


    public String getName()
    {
        return name;
    }


    public String getPrincipal()
    {
        return principal;
    }


    public String getType()
    {
        return type;
    }


    public String getField()
    {
        return field;
    }


    public void setInherited(final boolean inherited)
    {
        this.inherited = inherited;
    }


    public void setDenied(final boolean denied)
    {
        this.denied = denied;
    }
}
