/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.adapter.converters;

import com.hybris.cockpitng.config.compareview.jaxb.Section;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.widgets.compare.adapter.extractors.EditorAreaAttributeExtractor;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTabToSectionConverter implements CompareViewConverter<AbstractTab, Section>
{
    private static final String OBJECT_TO_CONVERT_CAN_NOT_BE_NULL = "Object to convert can not be null";
    private EditorAreaAttributeExtractor editorAreaAttributeExtractor;
    private CompareViewConverter<com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute, com.hybris.cockpitng.config.compareview.jaxb.Attribute> attributeToAttributeConverter;


    @Override
    public Section convert(final AbstractTab tab)
    {
        if(tab == null)
        {
            throw new IllegalArgumentException(OBJECT_TO_CONVERT_CAN_NOT_BE_NULL);
        }
        final Section compareViewSection = new Section();
        compareViewSection.setName(tab.getName());
        compareViewSection.setTooltipText(tab.getTooltipText());
        compareViewSection.setPosition(tab.getPosition());
        compareViewSection.setInitiallyOpened(tab.isInitiallyOpened());
        final List<Attribute> attributes = getEditorAreaAttributeExtractor().extractUniqueAttributesFromTab(tab);
        compareViewSection.getAttribute()
                        .addAll(attributes.stream().map(getAttributeToAttributeConverter()::convert).collect(Collectors.toList()));
        return compareViewSection;
    }


    public EditorAreaAttributeExtractor getEditorAreaAttributeExtractor()
    {
        return editorAreaAttributeExtractor;
    }


    @Required
    public void setEditorAreaAttributeExtractor(final EditorAreaAttributeExtractor editorAreaAttributeExtractor)
    {
        this.editorAreaAttributeExtractor = editorAreaAttributeExtractor;
    }


    public CompareViewConverter<com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute, com.hybris.cockpitng.config.compareview.jaxb.Attribute> getAttributeToAttributeConverter()
    {
        return attributeToAttributeConverter;
    }


    @Required
    public void setAttributeToAttributeConverter(
                    final CompareViewConverter<com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute, com.hybris.cockpitng.config.compareview.jaxb.Attribute> attributeToAttributeConverter)
    {
        this.attributeToAttributeConverter = attributeToAttributeConverter;
    }
}
