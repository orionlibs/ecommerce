/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EditorArea;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EssentialSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Essentials;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Tab;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a default configuration if no configuration is provided via cockpit configuration XML.
 */
public class DefaultEditorAreaConfigFallbackStrategy extends AbstractCockpitConfigurationFallbackStrategy<EditorArea>
{
    public static final String HMC_UNBOUND = "hmc.unbound";
    public static final String UNBOUND_SECTION_RENDERER_BEAN_ID = "unboundSectionRenderer";
    private static final String EDITORAREA_FALLBACK_TAB_OTHER = "editorarea.fallback.tab.other";
    private static final String EDITORAREA_FALLBACK_TAB_ADMINISTRATION = "editorarea.fallback.tab.administration";
    private static final String EDITORAREA_FALLBACK_SECTION_PREFIX = "editorarea.fallback.section.";
    private static final String EDITORAREA_FALLBACK_SECTION_ESSENTIAL = "essential";
    private static final String FALLBACK = "Fallback";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultEditorAreaConfigFallbackStrategy.class);


    private List<Attribute> getSpecificAttributes(final Set<String> attributeQualifiers)
    {
        final List<Attribute> attributeList = new ArrayList<>();
        for(final String attributeQualifier : attributeQualifiers)
        {
            final Attribute sectionAttribute = new Attribute();
            sectionAttribute.setQualifier(attributeQualifier);
            attributeList.add(sectionAttribute);
        }
        return attributeList;
    }


    @Override
    public EditorArea loadFallbackConfiguration(final ConfigContext context, final Class<EditorArea> configurationType)
    {
        EditorArea fallbackConfig = null;
        final String typeCode = context.getAttribute(DefaultConfigContext.CONTEXT_TYPE);
        try
        {
            final Set<String> mandatoryAttributes = getMandatoryAttributes(typeCode);
            final Set<String> additionalUniqueAttributes = getUniqueAttributes(typeCode);
            additionalUniqueAttributes.removeAll(mandatoryAttributes);
            final Set<String> unboundAttributes = getAttributes(typeCode, OTHER, INITIAL);
            unboundAttributes.removeAll(additionalUniqueAttributes);
            unboundAttributes.removeAll(mandatoryAttributes);
            fallbackConfig = new EditorArea();
            fallbackConfig.setName(FALLBACK);
            Essentials essentials = null;
            if(CollectionUtils.isNotEmpty(mandatoryAttributes))
            {
                essentials = new Essentials();
                final EssentialSection essentialSection = new EssentialSection();
                essentialSection.setName(EDITORAREA_FALLBACK_SECTION_PREFIX + EDITORAREA_FALLBACK_SECTION_ESSENTIAL);
                essentialSection.getAttributeOrCustom().addAll(getSpecificAttributes(mandatoryAttributes));
                essentialSection.getAttributeOrCustom().addAll(getSpecificAttributes(additionalUniqueAttributes));
                essentials.setEssentialSection(essentialSection);
                fallbackConfig.setEssentials(essentials);
                final Tab administrationTab = new Tab();
                administrationTab.setName(EDITORAREA_FALLBACK_TAB_ADMINISTRATION);
                administrationTab.setEssentials(essentials);
                fallbackConfig.getCustomTabOrTab().add(administrationTab);
            }
            if(!unboundAttributes.isEmpty())
            {
                final CustomSection otherAttributesSection = new CustomSection();
                otherAttributesSection.setName(HMC_UNBOUND);
                otherAttributesSection.setSpringBean(UNBOUND_SECTION_RENDERER_BEAN_ID);
                final Tab otherAttributesTab = new Tab();
                otherAttributesTab.setName(EDITORAREA_FALLBACK_TAB_OTHER);
                otherAttributesTab.getCustomSectionOrSection().add(otherAttributesSection);
                if(essentials != null)
                {
                    otherAttributesTab.setEssentials(essentials);
                }
                fallbackConfig.getCustomTabOrTab().add(otherAttributesTab);
            }
        }
        catch(final TypeNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e.getMessage(), e);
            }
            if(LOG.isWarnEnabled())
            {
                LOG.warn(e.getMessage());
            }
        }
        return fallbackConfig;
    }
}
