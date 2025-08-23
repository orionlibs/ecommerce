/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.attributechooser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * Pojo which represents an attribute. Attribute can have sub attributes e.g. for localized attributes there can be
 * specified attribute for specific language where {@link #getIsoCode()} will be define locale iso code. If attribute is
 * sub attribute it should point to it's parent {@link #getParent()} and should be defined in parent's
 * {@link #getSubAttributes()}
 */
public class Attribute implements Serializable
{
    private Attribute parent;
    private String displayName;
    private String qualifier;
    private boolean mandatory;
    private String isoCode;
    private Set<Attribute> subAttributes;


    /**
     * Creates an attribute
     *
     * @param qualifier
     *           qualifier of the attribute
     * @param displayName
     *           display name of the attribute
     * @param mandatory
     *           tells if attribute is mandatory - cannot be moved.
     */
    public Attribute(final String qualifier, final String displayName, final boolean mandatory)
    {
        this(qualifier, displayName, null, mandatory);
    }


    /**
     * Creates an attribute
     *
     * @param qualifier
     *           qualifier of the attribute
     * @param displayName
     *           display name of the attribute
     * @param isoCode
     *           iso code e.g. locale
     * @param mandatory
     *           tells if attribute is mandatory - cannot be moved.
     */
    public Attribute(final String qualifier, final String displayName, final String isoCode, final boolean mandatory)
    {
        this.qualifier = qualifier;
        this.displayName = displayName;
        this.mandatory = mandatory;
        this.isoCode = isoCode;
    }


    /**
     * Creates sub attribute of given parent.
     *
     * @param parent
     *           parent.
     * @param isoCode
     *           iso code e.g. locale
     */
    public Attribute(final Attribute parent, final String isoCode)
    {
        this(parent.getQualifier(), parent.getDisplayName(), isoCode, parent.isMandatory());
        this.parent = parent;
    }


    /**
     * Creates copy of an attribute. without making a copy of parent.
     *
     * @param toCopy
     *           attribute to copy.
     */
    public Attribute(final Attribute toCopy)
    {
        this(toCopy.getQualifier(), toCopy.getDisplayName(), toCopy.isoCode, toCopy.isMandatory());
        if(!toCopy.getSubAttributes().isEmpty())
        {
            toCopy.getSubAttributes().stream().map(Attribute::new).forEach(attr -> attr.setParent(this));
        }
    }


    public String getDisplayName()
    {
        return StringUtils.isNotBlank(displayName) ? displayName : qualifier;
    }


    public void setDisplayName(final String displayName)
    {
        this.displayName = displayName;
    }


    public String getQualifier()
    {
        return qualifier;
    }


    public void setQualifier(final String qualifier)
    {
        this.qualifier = qualifier;
    }


    public boolean isMandatory()
    {
        return mandatory;
    }


    public void setMandatory(final boolean mandatory)
    {
        this.mandatory = mandatory;
    }


    public String getIsoCode()
    {
        return isoCode;
    }


    public void setIsoCode(final String isoCode)
    {
        this.isoCode = isoCode;
    }


    public Set<Attribute> getSubAttributes()
    {
        if(subAttributes == null)
        {
            subAttributes = new HashSet<>();
        }
        return subAttributes;
    }


    public void setSubAttributes(final Set<Attribute> subAttributes)
    {
        if(subAttributes != null)
        {
            subAttributes.forEach(subAttribute -> subAttribute.setParent(this));
        }
        else
        {
            this.subAttributes = null;
        }
    }


    public boolean hasSubAttributes()
    {
        return subAttributes != null && !subAttributes.isEmpty();
    }


    public Attribute getParent()
    {
        return parent;
    }


    /**
     * Sets new parent and adds this attribute to parent's sub attributes.
     *
     * @param newParent
     *           new parent of the attribute.
     */
    public void setParent(final Attribute newParent)
    {
        if(this.parent != null)
        {
            this.parent.getSubAttributes().remove(this);
        }
        this.parent = newParent;
        if(newParent != null)
        {
            newParent.getSubAttributes().add(this);
        }
    }


    /**
     * Adds sub attribute of this attribute and sets attribute's parent to this.
     *
     * @param attribute
     *           sub attribute.
     */
    public void addSubAttribute(final Attribute attribute)
    {
        attribute.setParent(this);
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o.getClass() != this.getClass())
        {
            return false;
        }
        final Attribute attribute = (Attribute)o;
        if(mandatory != attribute.mandatory)
        {
            return false;
        }
        if(parent != null ? !parent.equals(attribute.parent) : (attribute.parent != null))
        {
            return false;
        }
        if(!qualifier.equals(attribute.qualifier))
        {
            return false;
        }
        return isoCode != null ? isoCode.equals(attribute.isoCode) : (attribute.isoCode == null);
    }


    @Override
    public int hashCode()
    {
        int result = qualifier.hashCode();
        result = 31 * result + (mandatory ? 1 : 0);
        result = 31 * result + (isoCode != null ? isoCode.hashCode() : 0);
        return result;
    }


    /**
     * Lists parents to first attribute which does not have parent. This attribute's parent will be first on the list.
     *
     * @return list of all parents.
     */
    public List<Attribute> getAllParents()
    {
        if(parent != null)
        {
            final List<Attribute> parents = new ArrayList<>();
            parents.add(parent);
            parents.addAll(parent.getAllParents());
            return parents;
        }
        return Collections.emptyList();
    }
}
