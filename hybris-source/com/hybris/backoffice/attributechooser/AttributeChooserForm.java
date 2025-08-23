/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.attributechooser;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Pojo which is used to choose attributes. It has {@link #getAvailableAttributes()} which contain attributes which can
 * be chosen and {@link #getChosenAttributes()} which represents chosen attributes. If {@link #isIncludeAll()} is true
 * then all attributes both from available and selected will be returned as selected through
 * {@link #getSelectedAttributes()}
 */
public class AttributeChooserForm implements Serializable
{
    private Set<Attribute> availableAttributes;
    private Set<Attribute> chosenAttributes;
    private boolean includeAll;


    public AttributeChooserForm()
    {
    }


    public AttributeChooserForm(final Set<Attribute> availableAttributes, final Set<Attribute> chosenAttributes)
    {
        this.availableAttributes = availableAttributes;
        this.chosenAttributes = chosenAttributes;
    }


    /**
     * Tells if form has populated data {@link #getChosenAttributes()} or {@link #getAvailableAttributes()} is not empty.
     *
     * @return true if data has been populated.
     */
    public boolean hasPopulatedAttributes()
    {
        return CollectionUtils.isNotEmpty(availableAttributes) || CollectionUtils.isNotEmpty(chosenAttributes);
    }


    /**
     * Tells {@link #getSelectedAttributes()} is not empty.
     *
     * @return true if attributes are selected.
     */
    public boolean hasSelectedAttributes()
    {
        return CollectionUtils.isNotEmpty(getChosenAttributes())
                        || (isIncludeAll() && CollectionUtils.isNotEmpty(getAvailableAttributes()));
    }


    /**
     * Returns list of selected attributes. If {@link #isIncludeAll()} is true the it return merged list of
     * {@link #getChosenAttributes()} and {@link #getAvailableAttributes()}. If include all is not selected it returns just
     * {@link #getChosenAttributes()}
     *
     * @return list of selected attributes
     */
    public Set<Attribute> getSelectedAttributes()
    {
        if(!isIncludeAll())
        {
            return getChosenAttributes();
        }
        else if(getChosenAttributes().isEmpty())
        {
            return getAvailableAttributes();
        }
        else if(getAvailableAttributes().isEmpty())
        {
            return getChosenAttributes();
        }
        final Set<Attribute> copyOfAvailable = getAvailableAttributes().stream().map(Attribute::new).collect(Collectors.toSet());
        final Set<Attribute> copyOfChosen = getChosenAttributes().stream().map(Attribute::new).collect(Collectors.toSet());
        return mergeAttributes(copyOfAvailable, copyOfChosen);
    }


    protected Set<Attribute> mergeAttributes(final Set<Attribute> available, final Set<Attribute> choosen)
    {
        final Set<Attribute> attributes = new HashSet<>();
        available.forEach(availableAttribute -> {
            final Optional<Attribute> chosenAttribute = choosen.stream().filter(availableAttribute::equals).findAny();
            attributes.add(availableAttribute);
            if(chosenAttribute.isPresent())
            {
                choosen.remove(chosenAttribute.get());
                availableAttribute.setSubAttributes(
                                mergeAttributes(availableAttribute.getSubAttributes(), chosenAttribute.get().getSubAttributes()));
            }
        });
        attributes.addAll(choosen);
        return attributes;
    }


    /**
     * @return list of available to choose attributes.
     */
    public Set<Attribute> getAvailableAttributes()
    {
        return availableAttributes != null ? availableAttributes : Collections.emptySet();
    }


    /**
     * Sets list of available attributes.
     *
     * @param availableAttributes
     *           list of chosen attributes.
     */
    public void setAvailableAttributes(final Set<Attribute> availableAttributes)
    {
        this.availableAttributes = availableAttributes;
    }


    /**
     * @return list of chosen attributes.
     */
    public Set<Attribute> getChosenAttributes()
    {
        return chosenAttributes != null ? chosenAttributes : Collections.emptySet();
    }


    /**
     * Sets list of chosen attributes.
     *
     * @param chosenAttributes
     *           list of chosen attributes.
     */
    public void setChosenAttributes(final Set<Attribute> chosenAttributes)
    {
        this.chosenAttributes = chosenAttributes;
    }


    /**
     * Tells if form should return all attributes both from available and chosen as selected ones.
     *
     * @return true if all should be included in {@link #getSelectedAttributes()}.
     */
    public boolean isIncludeAll()
    {
        return includeAll;
    }


    /**
     * Defines if form should return all attributes both from available and chosen as selected ones.
     */
    public void setIncludeAll(final boolean includeAll)
    {
        this.includeAll = includeAll;
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
        final AttributeChooserForm form = (AttributeChooserForm)o;
        if(includeAll != form.includeAll)
        {
            return false;
        }
        if(availableAttributes != null ? !availableAttributes.equals(form.availableAttributes) : (form.availableAttributes != null))
        {
            return false;
        }
        return chosenAttributes != null ? chosenAttributes.equals(form.chosenAttributes) : (form.chosenAttributes == null);
    }


    @Override
    public int hashCode()
    {
        int result = availableAttributes != null ? availableAttributes.hashCode() : 0;
        result = 31 * result + (chosenAttributes != null ? chosenAttributes.hashCode() : 0);
        result = 31 * result + (includeAll ? 1 : 0);
        return result;
    }
}
