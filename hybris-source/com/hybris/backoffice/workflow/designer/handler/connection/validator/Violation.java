/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection.validator;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Contains information about violations discovered by the validator
 */
public class Violation
{
    private final String code;
    private final Collection<Object> params;


    private Violation(final String code, final Collection<Object> params)
    {
        this.code = code;
        this.params = params;
    }


    /**
     * Creates violation which will be shown to the user and also is used to prevent connection from creation.
     *
     * @param code
     *           unique code of the violation
     * @param params
     *           optional parameters of the violation, containing for example offensive nodes
     * @return violation object
     */
    public static Violation create(final String code, final Object... params)
    {
        return new Violation(code, List.of(params));
    }


    public String getCode()
    {
        return code;
    }


    public Collection<Object> getParams()
    {
        return params;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final Violation violation = (Violation)o;
        return Objects.equals(code, violation.code) && Objects.equals(params, violation.params);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(code, params);
    }


    @Override
    public String toString()
    {
        return "Violation{" + "code='" + code + '\'' + ", params=" + params + '}';
    }
}
