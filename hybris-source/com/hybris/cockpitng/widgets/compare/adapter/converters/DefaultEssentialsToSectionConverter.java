/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.adapter.converters;

import com.hybris.cockpitng.config.compareview.jaxb.Section;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Essentials;
import com.hybris.cockpitng.widgets.baseeditorarea.EditorAreaConfigWithOverviewTab;
import com.hybris.cockpitng.widgets.compare.adapter.extractors.EditorAreaAttributeExtractor;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Required;

public class DefaultEssentialsToSectionConverter implements CompareViewConverter<Essentials, Section>
{
    private EditorAreaAttributeExtractor editorAreaAttributeExtractor;
    private CompareViewConverter<com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute, com.hybris.cockpitng.config.compareview.jaxb.Attribute> attributeToAttributeConverter;


    @Override
    public Section convert(@Nonnull final Essentials essentials)
    {
        final Section compareViewSection = new Section();
        compareViewSection.setPosition(BigInteger.ZERO);
        compareViewSection.setName(EditorAreaConfigWithOverviewTab.LABEL_EDITORAREA_TAB_OVERVIEW_TITLE);
        compareViewSection.setTooltipText(EditorAreaConfigWithOverviewTab.LABEL_EDITORAREA_TAB_OVERVIEW_TOOLTIP);
        compareViewSection.setInitiallyOpened(essentials.isInitiallyOpened());
        final List<Attribute> attributes = getEditorAreaAttributeExtractor()
                        .extractUniqueAttributesFromSection(essentials.getEssentialSection());
        final Collection<com.hybris.cockpitng.config.compareview.jaxb.Attribute> compareAttributes = attributes.stream() //
                        .map(getAttributeToAttributeConverter()::convert) //
                        .collect(Collectors.toList());
        compareViewSection.getAttribute().addAll(compareAttributes);
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
