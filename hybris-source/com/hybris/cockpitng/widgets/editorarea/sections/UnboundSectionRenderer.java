/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.editorarea.sections;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPositioned;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomPanel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EditorArea;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Panel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Section;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Tab;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.widgets.baseeditorarea.DefaultEditorAreaController;
import com.hybris.cockpitng.widgets.common.AttributesComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.DefaultEditorAreaSectionRenderer;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;

public class UnboundSectionRenderer extends DefaultEditorAreaSectionRenderer implements AttributesComponentRenderer
{
    /** @deprecated since 6.7 use {@link DefaultEditorAreaController#EDITOR_AREA_CONFIGURATION} instead. */
    @Deprecated(since = "6.7", forRemoval = true)
    public static final String EDITOR_AREA_CONFIGURATION = DefaultEditorAreaController.EDITOR_AREA_CONFIGURATION;
    private static final Logger LOG = LoggerFactory.getLogger(UnboundSectionRenderer.class);
    private BigDecimal numberOfColumns = null;
    private boolean initiallyOpened = true;
    private Section unboundSection;


    @Override
    public void render(final Component parent, final AbstractSection abstractSectionConfiguration, final Object object,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        new ProxyRenderer<>(this, parent, abstractSectionConfiguration, object).render(createSectionRenderer(), parent,
                        getUnboundSection(widgetInstanceManager), object, dataType, widgetInstanceManager);
    }


    protected Section prepareUnboundConfiguration(final WidgetInstanceManager widgetInstanceManager, final EditorArea config,
                    final DataType dataType)
    {
        final Set<String> usedQualifiers = new HashSet<>();
        if(config.getEssentials() != null)
        {
            final Stream<AbstractPositioned> attributesStream;
            if(config.getEssentials().getEssentialSection() != null)
            {
                attributesStream = Stream.concat(config.getEssentials().getEssentialSection().getAttributeOrCustom().stream(),
                                config.getEssentials().getEssentialSection().getCustomPanelOrPanel().stream().map(Panel::getAttributeOrCustom)
                                                .flatMap(Collection::stream));
            }
            else if(config.getEssentials().getEssentialCustomSection() != null)
            {
                attributesStream = Stream.concat(config.getEssentials().getEssentialCustomSection().getAttributeOrCustom().stream(),
                                config.getEssentials().getEssentialCustomSection().getCustomPanelOrPanel().stream()
                                                .map(Panel::getAttributeOrCustom).flatMap(Collection::stream));
            }
            else
            {
                attributesStream = Stream.empty();
            }
            attributesStream.filter(Attribute.class::isInstance).map(Attribute.class::cast).map(Attribute::getQualifier)
                            .filter(StringUtils::isNoneBlank).forEach(usedQualifiers::add);
        }
        for(final AbstractTab tab : config.getCustomTabOrTab())
        {
            if(tab instanceof CustomTab)
            {
                final String springBean = ((CustomTab)tab).getSpringBean();
                final String clazz = ((CustomTab)tab).getClazz();
                registerDynamicQualifiers(widgetInstanceManager, usedQualifiers, springBean, clazz);
            }
            else
            {
                for(final AbstractSection section : ((Tab)tab).getCustomSectionOrSection())
                {
                    if(section instanceof CustomSection)
                    {
                        final String springBean = ((CustomSection)section).getSpringBean();
                        final String clazz = ((CustomSection)section).getClazz();
                        registerDynamicQualifiers(widgetInstanceManager, usedQualifiers, springBean, clazz);
                    }
                    else
                    {
                        ((Section)section).getAttributeOrCustom().stream().filter(attribute -> attribute instanceof Attribute)
                                        .forEach(attribute -> {
                                            final String qualifier = ((Attribute)attribute).getQualifier();
                                            if(StringUtils.isNotBlank(qualifier))
                                            {
                                                usedQualifiers.add(qualifier);
                                            }
                                        });
                    }
                    if(section instanceof Section)
                    {
                        final Section concreteSection = (Section)section;
                        for(final Panel panel : concreteSection.getCustomPanelOrPanel())
                        {
                            if(panel instanceof CustomPanel)
                            {
                                final String customPanelSpringBean = ((CustomPanel)panel).getSpringBean();
                                final String customPanelClazz = ((CustomPanel)panel).getClazz();
                                registerDynamicQualifiers(widgetInstanceManager, usedQualifiers, customPanelSpringBean, customPanelClazz);
                            }
                            else
                            {
                                final List<String> attributesFromPanel = panel.getAttributeOrCustom().stream()
                                                .filter(a -> a instanceof Attribute).map(a -> ((Attribute)a).getQualifier())
                                                .filter(StringUtils::isNotBlank).collect(Collectors.toList());
                                usedQualifiers.addAll(attributesFromPanel);
                            }
                        }
                    }
                }
            }
        }
        final Section sec = new Section();
        dataType.getAttributes().stream().filter(dataAttribute -> !usedQualifiers.contains(dataAttribute.getQualifier())).sorted(Comparator.comparing(DataAttribute::getQualifier))
                        .forEach(dataAttribute -> {
                            final Attribute attribute = new Attribute();
                            attribute.setQualifier(dataAttribute.getQualifier());
                            sec.getAttributeOrCustom().add(attribute);
                        });
        sec.setColumns(numberOfColumns);
        sec.setInitiallyOpened(initiallyOpened);
        return sec;
    }


    protected void registerDynamicQualifiers(final WidgetInstanceManager widgetInstanceManager, final Set<String> usedQualifiers,
                    final String springBean, final String clazz)
    {
        if(StringUtils.isNotBlank(springBean))
        {
            usedQualifiers
                            .addAll(resolveDynamicAttributes(widgetInstanceManager, BackofficeSpringUtil.getBean(springBean, Object.class)));
        }
        if(StringUtils.isNotBlank(clazz))
        {
            usedQualifiers.addAll(
                            resolveDynamicAttributes(widgetInstanceManager, BackofficeSpringUtil.createClassInstance(clazz, Object.class)));
        }
    }


    protected Collection<? extends String> resolveDynamicAttributes(final WidgetInstanceManager widgetInstanceManager,
                    final Object bean)
    {
        if((bean instanceof AttributesComponentRenderer) && !Objects.equals(bean.getClass(), getClass()))
        {
            final Collection<String> qualifiers = ((AttributesComponentRenderer)bean).getRenderedQualifiers(widgetInstanceManager);
            if(qualifiers != null)
            {
                return qualifiers;
            }
        }
        return Collections.emptyList();
    }


    protected Collection<String> getRenderedQualifiers(final Section section)
    {
        if(section != null)
        {
            return section.getAttributeOrCustom().stream().filter(aoc -> aoc instanceof Attribute)
                            .map(attribute -> ((Attribute)attribute).getQualifier()).collect(Collectors.toList());
        }
        else
        {
            return Collections.emptyList();
        }
    }


    protected Section getUnboundSection(final WidgetInstanceManager widgetInstanceManager)
    {
        if(unboundSection == null)
        {
            final EditorArea configuration = widgetInstanceManager.getModel()
                            .getValue(DefaultEditorAreaController.EDITOR_AREA_CONFIGURATION, EditorArea.class);
            if(configuration != null)
            {
                final DataType dataType = widgetInstanceManager.getModel()
                                .getValue(DefaultEditorAreaController.MODEL_CURRENT_OBJECT_TYPE, DataType.class);
                unboundSection = prepareUnboundConfiguration(widgetInstanceManager, configuration, dataType);
            }
            else if(LOG.isWarnEnabled())
            {
                LOG.warn("Editor area configuration not found. Skipping rendering.");
            }
        }
        return unboundSection != null ? unboundSection : new Section();
    }


    @Override
    public Collection<String> getRenderedQualifiers(final WidgetInstanceManager widgetInstanceManager)
    {
        return getRenderedQualifiers(getUnboundSection(widgetInstanceManager));
    }


    public void setNumberOfColumns(final BigDecimal columns)
    {
        this.numberOfColumns = columns;
    }


    public void setInitiallyOpened(final boolean initiallyOpened)
    {
        this.initiallyOpened = initiallyOpened;
    }
}
