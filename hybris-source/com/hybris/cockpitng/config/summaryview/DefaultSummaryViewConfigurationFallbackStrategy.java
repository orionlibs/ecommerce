/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.summaryview;

import com.hybris.cockpitng.config.summaryview.jaxb.Attribute;
import com.hybris.cockpitng.config.summaryview.jaxb.Section;
import com.hybris.cockpitng.config.summaryview.jaxb.SummaryView;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.AbstractCockpitConfigurationFallbackStrategy;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSummaryViewConfigurationFallbackStrategy extends AbstractCockpitConfigurationFallbackStrategy<SummaryView>
{
    protected static final String LABEL_SECTION_ESSENTIAL = "com.hybris.cockpitng.summaryView.fallback.section.essential";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSummaryViewConfigurationFallbackStrategy.class);


    @Override
    public SummaryView loadFallbackConfiguration(final ConfigContext context, final Class<SummaryView> configurationType)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Fallback for {} has been called", SummaryView.class);
        }
        Validate.notNull("Cannot create configuration for null context", context);
        final SummaryView config = new SummaryView();
        config.setDisplayThumbnail(Boolean.TRUE);
        final Section essentialSection = createEssentialSection(context);
        addMandatoryAttributesToSection(context, essentialSection);
        config.getCustomSectionOrSection().add(essentialSection);
        return config;
    }


    protected Section createEssentialSection(final ConfigContext context)
    {
        final Section section = new Section();
        section.setName(LABEL_SECTION_ESSENTIAL);
        return section;
    }


    protected void addMandatoryAttributesToSection(final ConfigContext context, final Section section)
    {
        final List<Positioned> attributes = section.getCustomAttributeOrAttributeOrActions();
        try
        {
            getMandatoryAttributes(context).forEach(attribute -> addAttributeToList(attributes, attribute));
        }
        catch(final TypeNotFoundException e)
        {
            LOG.error("Error while creating {} fallback configuration: ", SummaryView.class, e);
        }
    }


    protected Set<String> getMandatoryAttributes(final ConfigContext context) throws TypeNotFoundException
    {
        return getMandatoryAttributes(getTypeFromContext(context));
    }


    protected String getTypeFromContext(final ConfigContext context)
    {
        final String type = context.getAttribute(DefaultConfigContext.CONTEXT_TYPE);
        if(StringUtils.isBlank(type))
        {
            throw new IllegalStateException("Configuration context does not contain 'type' attribute");
        }
        return type;
    }


    protected void addAttributeToList(final List<Positioned> attributes, final String attributeName)
    {
        final Attribute attribute = new Attribute();
        attribute.setQualifier(attributeName);
        attribute.setVisible(Boolean.TRUE);
        attributes.add(attribute);
    }
}
