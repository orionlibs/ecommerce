/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.export.wizard.renderer.attributechooser;

import com.hybris.backoffice.excel.data.SelectedAttribute;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Pojo which is used to choose attributes.
 *
 * @deprecated since 6.7 {@link com.hybris.backoffice.attributechooser.AttributeChooserForm}
 */
@Deprecated(since = "6.7", forRemoval = true)
public class AttributeChooserForm
{
    private Set<AttributeDescriptorModel> allAttributes;
    private List<SelectedAttribute> selectedAttributes;
    private Consumer<List<SelectedAttribute>> onSelectedAttributesChangeConsumer;


    public AttributeChooserForm(final Set<AttributeDescriptorModel> allAttributes)
    {
        this.allAttributes = allAttributes;
    }


    public void setSelectedAttributes(final List<SelectedAttribute> selectedAttributes)
    {
        this.selectedAttributes = selectedAttributes;
        if(onSelectedAttributesChangeConsumer != null)
        {
            onSelectedAttributesChangeConsumer.accept(getSelectedAttributes());
        }
    }


    public List<SelectedAttribute> getSelectedAttributes()
    {
        if(selectedAttributes == null)
        {
            selectedAttributes = new ArrayList<>();
        }
        return selectedAttributes;
    }


    public Set<AttributeDescriptorModel> getAllAttributes()
    {
        if(allAttributes == null)
        {
            allAttributes = new HashSet<>();
        }
        return allAttributes;
    }


    public void setOnSelectedAttributesChangeConsumer(final Consumer<List<SelectedAttribute>> onSelectedAttributesChangeConsumer)
    {
        this.onSelectedAttributesChangeConsumer = onSelectedAttributesChangeConsumer;
    }
}
