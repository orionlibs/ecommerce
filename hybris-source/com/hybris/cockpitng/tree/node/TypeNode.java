/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.tree.node;

import com.hybris.backoffice.navigation.impl.SimpleNode;
import org.apache.commons.lang3.StringUtils;

/**
 * Navigation node representing a type. In addition to a simple node it holds the type code (e.g. "Product", "User",
 * ...). Interpretation of what is a type is system dependent, this just holds the type code.
 */
public class TypeNode extends SimpleNode
{
    private static final long serialVersionUID = -6669004547216437812L;
    private final String code;


    public TypeNode(final String id, final String code)
    {
        super(id);
        this.code = code;
    }


    @Override
    public boolean isActionAware()
    {
        return true;
    }


    public String getCode()
    {
        return this.code;
    }


    @Override
    public String getName()
    {
        String ret = super.getName();
        if(StringUtils.isBlank(ret))
        {
            ret = getCode();
        }
        return ret;
    }


    @Override
    public boolean equals(final Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(!super.equals(obj))
        {
            return false;
        }
        if(this.getClass() != obj.getClass())
        {
            return false;
        }
        final TypeNode other = (TypeNode)obj;
        if(this.code == null)
        {
            if(other.code != null)
            {
                return false;
            }
        }
        else if(!this.code.equals(other.code))
        {
            return false;
        }
        return true;
    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.code == null) ? 0 : this.code.hashCode());
        return result;
    }
}
