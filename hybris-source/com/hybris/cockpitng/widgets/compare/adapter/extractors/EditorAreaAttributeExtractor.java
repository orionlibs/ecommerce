/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.adapter.extractors;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import java.util.List;

/**
 * An interface capable of extracting Compare View attributes from Editor Area configuration
 */
public interface EditorAreaAttributeExtractor
{
    /**
     * @param abstractTab
     *           source editor area tab containing some attributes
     * @return list of all attributes from tab
     */
    List<Attribute> extractUniqueAttributesFromTab(final AbstractTab abstractTab);


    /**
     * @param section
     *           source editor area section containing some attributes
     * @return list of all unique attributes from tab in order of appearance
     */
    List<Attribute> extractUniqueAttributesFromSection(final AbstractSection section);
}
