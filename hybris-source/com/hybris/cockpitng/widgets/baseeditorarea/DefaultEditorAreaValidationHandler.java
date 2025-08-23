/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import com.google.common.collect.Sets;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.components.validation.ValidatableContainer;
import com.hybris.cockpitng.components.validation.ValidationFocusTransferHandler;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPositioned;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EditorArea;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Panel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Section;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.editor.util.FocusUtils;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.validation.ValidationContext;
import com.hybris.cockpitng.validation.ValidationHandler;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.widgets.common.AttributesComponentRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRendererEvent;
import com.hybris.cockpitng.widgets.common.WidgetComponentRendererListener;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaRendererUtils;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.DefaultEditorAreaRenderer;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

public class DefaultEditorAreaValidationHandler
                implements ValidationFocusTransferHandler, WidgetComponentRendererListener<Component, EditorArea, Object>
{
    private static final String SELECTOR_TABBOX = Tabbox.class.getSimpleName();
    private static final String PATTERN_SELECTOR_ANCHOR = String.format("%s[%s=\"%%s\"]", Editor.class.getSimpleName(),
                    EditorAreaRendererUtils.getEditorAnchorAttributeName());
    private static final String PATTERN_SELECTOR_TAB = String.format("%s[%s^=\"%%s\"]", Tab.class.getSimpleName(), DefaultEditorAreaRenderer.TAB_ID_ATTRIBUTE);
    private final Component parent;
    private final DefaultEditorAreaController controller;
    private final ValidatableContainer validatableContainer;


    public DefaultEditorAreaValidationHandler(final Component parent, final DefaultEditorAreaController controller,
                    final ValidatableContainer validatableContainer)
    {
        this.parent = parent;
        this.controller = controller;
        this.validatableContainer = validatableContainer;
        final WidgetComponentRendererListener<Component, EditorArea, Object> oldListener = EditorAreaRendererUtils
                        .putRendererListener(controller.getModel(), this);
        if(oldListener != null)
        {
            controller.getRenderer().removeRendererListener(oldListener);
        }
        controller.getRenderer().addRendererListener(this);
    }


    @Override
    public void componentRendered(final WidgetComponentRendererEvent<Component, EditorArea, Object> event)
    {
        if(event.getSource() instanceof Editor)
        {
            final Editor editor = (Editor)event.getSource();
            editor.setAttribute(EditorAreaRendererUtils.getEditorAnchorAttributeName(),
                            EditorAreaRendererUtils.getEditorAnchor(editor));
            editor.initValidation(validatableContainer, createProxyValidationHandler());
        }
    }


    protected ValidationHandler createProxyValidationHandler()
    {
        final EditorAreaLogicHandler editorAreaLogicHandler = controller
                        .lookupEditorAreaLogicHandler(controller.getEditorAreaConfiguration());
        return new ValidationHandler()
        {
            @Override
            public List<ValidationInfo> validate(final Object objectToValidate, final ValidationContext validationContext)
            {
                return editorAreaLogicHandler.performValidation(controller.getWidgetInstanceManager(), objectToValidate,
                                validationContext);
            }


            @Override
            public List<ValidationInfo> validate(final Object objectToValidate, final List<String> qualifiers,
                            final ValidationContext validationContext)
            {
                return editorAreaLogicHandler.performValidation(controller.getWidgetInstanceManager(), objectToValidate, qualifiers,
                                validationContext);
            }
        };
    }


    @Override
    public int focusValidationPath(final Component parent, final String property)
    {
        final List<Component> componentList = Selectors.find(parent, SELECTOR_TABBOX);
        final Tabbox tabbox = componentList.isEmpty() ? null : (Tabbox)componentList.iterator().next();
        final Component component = tabbox != null ? findPropertyComponent(tabbox.getSelectedPanel(), property) : null;
        if(component != null)
        {
            visitEditorComponent(component, property);
            return TRANSFER_SUCCESS;
        }
        else if(tabbox != null)
        {
            final Tab tab = findPropertyTab(EditorAreaRendererUtils.getRelativeAttributePath(property));
            if(tab != null)
            {
                final Tabpanel selectedTabPanel = (Tabpanel)tab.getTabbox().getTabpanels().getChildren().get(tab.getIndex());
                if(Objects.equals(selectedTabPanel.getAttribute(DefaultEditorAreaRenderer.TAB_INITIALIZED_FLAG), Boolean.TRUE))
                {
                    tab.getTabbox().addEventListener(Events.ON_SELECT, new EventListener<SelectEvent>()
                    {
                        @Override
                        public void onEvent(final SelectEvent event)
                        {
                            visitComponent(tabbox.getSelectedPanel(), property);
                            tab.getTabbox().removeEventListener(Events.ON_SELECT, this);
                        }
                    });
                }
                else
                {
                    controller.getRenderer().addRendererListener(new WidgetComponentRendererListener<Component, EditorArea, Object>()
                    {
                        @Override
                        public void componentRendered(final WidgetComponentRendererEvent<Component, EditorArea, Object> e)
                        {
                            if(e.getSource() instanceof Tabpanel)
                            {
                                visitComponent(tabbox.getSelectedPanel(), property);
                                e.getRenderer().removeRendererListener(this);
                            }
                        }
                    });
                }
                tabbox.setSelectedIndex(tab.getIndex());
                Events.postEvent(tabbox, new SelectEvent<>(Events.ON_SELECT, tabbox, Sets.newHashSet(tab)));
                return TRANSFER_SUCCESS;
            }
            else
            {
                return TRANSFER_ERROR_UNKNOWN_PATH;
            }
        }
        else
        {
            return TRANSFER_ERROR_OTHER;
        }
    }


    protected void visitEditorComponent(final Component component, final String property)
    {
        component.addEventListener(ON_FOCUS_TRANSFER_REQUESTED, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                try
                {
                    FocusUtils.focusComponent(component, property);
                }
                finally
                {
                    component.removeEventListener(ON_FOCUS_TRANSFER_REQUESTED, this);
                }
                Events.echoEvent(ON_FOCUS_TRANSFERRED, component, null);
            }
        });
        Events.echoEvent(ON_FOCUS_TRANSFER_REQUESTED, component, null);
    }


    protected Component findPropertyComponent(final Component comp, final String property)
    {
        final String anchor = EditorAreaRendererUtils.getEditorAnchor(property);
        final List<Component> components = Selectors.find(comp, String.format(PATTERN_SELECTOR_ANCHOR, anchor));
        if(!components.isEmpty())
        {
            return components.stream().findFirst().orElse(null);
        }
        else
        {
            return null;
        }
    }


    protected Component visitComponent(final Component parent, final String property)
    {
        final Component component = parent != null ? findPropertyComponent(parent, property) : null;
        if(component != null)
        {
            visitEditorComponent(component, property);
        }
        return component;
    }


    protected boolean containsProperty(final Stream<AbstractPositioned> stream, final String property)
    {
        return stream.filter(aoc -> aoc instanceof Attribute).map(abstractPositioned -> (Attribute)abstractPositioned)
                        .anyMatch(attribute -> StringUtils.equals(attribute.getQualifier(), property));
    }


    protected boolean containsCustomProperty(final Section section, final String property)
    {
        if(section instanceof CustomSection)
        {
            final CustomSection customSection = (CustomSection)section;
            final WidgetComponentRenderer<?, ?, ?> renderer;
            if(StringUtils.isNotBlank(customSection.getSpringBean()))
            {
                renderer = BackofficeSpringUtil.getBean(customSection.getSpringBean(), WidgetComponentRenderer.class);
            }
            else if(StringUtils.isNotBlank(customSection.getClazz()))
            {
                renderer = BackofficeSpringUtil.createClassInstance(customSection.getClazz(), WidgetComponentRenderer.class);
            }
            else
            {
                renderer = null;
            }
            if(renderer instanceof AttributesComponentRenderer)
            {
                return ((AttributesComponentRenderer)renderer).getRenderedQualifiers(controller.getWidgetInstanceManager())
                                .contains(property);
            }
        }
        return false;
    }


    protected boolean containsProperty(final AbstractSection sectionDefinition, final String property)
    {
        if(sectionDefinition instanceof Section)
        {
            final Section section = (Section)sectionDefinition;
            return containsProperty(section.getAttributeOrCustom().stream(), property) || containsProperty(
                            section.getCustomPanelOrPanel().stream().map(Panel::getAttributeOrCustom).flatMap(Collection::stream), property)
                            || containsCustomProperty(section, property);
        }
        else
        {
            return false;
        }
    }


    protected boolean containsProperty(final AbstractTab tabDefinition, final String property)
    {
        if(tabDefinition instanceof com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Tab)
        {
            final com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Tab tab = (com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Tab)tabDefinition;
            final Collection<AbstractSection> sections = EditorAreaRendererUtils.getSections(tab);
            final Optional<AbstractSection> firstSection = sections.stream().filter(section -> containsProperty(section, property))
                            .findFirst();
            return firstSection.isPresent();
        }
        else
        {
            return false;
        }
    }


    protected AbstractTab findPropertyTabDefinition(final String property)
    {
        final Optional<AbstractTab> firstTab = controller.getEditorAreaConfiguration().getCustomTabOrTab().stream()
                        .filter(tab -> containsProperty(tab, ObjectValuePath.getNotLocalizedPath(property))).findFirst();
        return firstTab.orElse(null);
    }


    protected Tab findPropertyTab(final String property)
    {
        final AbstractTab config = findPropertyTabDefinition(property);
        if(config != null)
        {
            final List<Component> components = Selectors.find(parent, String.format(PATTERN_SELECTOR_TAB, config.getName()));
            if(!components.isEmpty())
            {
                return (Tab)components.iterator().next();
            }
        }
        return null;
    }
}
