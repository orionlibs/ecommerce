/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.adapter.extractors;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Panel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Section;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Tab;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultEditorAreaAttributeExtractor implements EditorAreaAttributeExtractor
{
    @Override
    public List<Attribute> extractUniqueAttributesFromTab(final AbstractTab abstractTab)
    {
        final List<Attribute> attributes = extractAttributesFromTab(abstractTab);
        return removeDuplicatedAttributesByQualifier(attributes.stream()).collect(Collectors.toList());
    }


    protected List<Attribute> extractAttributesFromTab(final AbstractTab editorAreaTab)
    {
        final List<Attribute> editorAreaAttributes = new ArrayList<>();
        if(editorAreaTab instanceof Tab)
        {
            final Tab tab = (Tab)editorAreaTab;
            final List<AbstractSection> sections = tab.getCustomSectionOrSection();
            editorAreaAttributes.addAll(sections.stream().map(this::extractAttributesFromSection).flatMap(Collection::stream)
                            .collect(Collectors.toList()));
        }
        return editorAreaAttributes;
    }


    @Override
    public List<Attribute> extractUniqueAttributesFromSection(final AbstractSection section)
    {
        final List<Attribute> attributes = extractAttributesFromSection(section);
        return removeDuplicatedAttributesByQualifier(attributes.stream()).collect(Collectors.toList());
    }


    protected List<Attribute> extractAttributesFromSection(final AbstractSection editorAreaAbstractSection)
    {
        if(editorAreaAbstractSection instanceof Section)
        {
            final Section editorAreaSection = (Section)editorAreaAbstractSection;
            final Stream<Attribute> attributesFromSection = editorAreaSection.getAttributeOrCustom().stream()
                            .filter(positionedElement -> positionedElement instanceof Attribute)
                            .map(editorAreaAttribute -> (Attribute)editorAreaAttribute);
            final List<Panel> panels = editorAreaSection.getCustomPanelOrPanel();
            final Stream<Attribute> attributesFromPanels = panels.stream()
                            .flatMap(DefaultEditorAreaAttributeExtractor::extractAttributesFromPanel);
            return Stream.concat(attributesFromSection, attributesFromPanels).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    private static Stream<Attribute> extractAttributesFromPanel(final Panel editorAreaPanel)
    {
        return editorAreaPanel.getAttributeOrCustom().stream().filter(ap -> ap instanceof Attribute).map(ap -> (Attribute)ap);
    }


    private static Stream<Attribute> removeDuplicatedAttributesByQualifier(final Stream<Attribute> streamWithDuplicates)
    {
        return streamWithDuplicates.filter(distinctByProperty(Attribute::getQualifier));
    }


    private static <T> Predicate<T> distinctByProperty(final Function<? super T, ?> propertyExtractor)
    {
        final Set<Object> occurenceRegister = Collections.synchronizedSet(new HashSet<>());
        return t -> occurenceRegister.add(propertyExtractor.apply(t));
    }
}
