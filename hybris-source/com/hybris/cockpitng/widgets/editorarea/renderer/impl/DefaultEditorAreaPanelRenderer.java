/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.editorarea.renderer.impl;

import com.hybris.cockpitng.common.EditorConfigurator;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPanel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomElement;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomPanel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Panel;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotPersistedAttributeReadException;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zkmax.zul.Tablechildren;
import org.zkoss.zkmax.zul.Tablelayout;
import org.zkoss.zul.Div;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;

/**
 * Default renderer for section panels. Panels are rendered in a {@link Tablelayout} component.
 */
public class DefaultEditorAreaPanelRenderer extends AbstractEditorAreaComponentRenderer<AbstractPanel, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultEditorAreaPanelRenderer.class);
    private static final String ATTRIBUTE_GROUP_NAME = "group-name";


    @Override
    public void render(final Component component, final AbstractPanel abstractPanelConfiguration, final Object object,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Tablechildren tablechildren = new Tablechildren();
        YTestTools.modifyYTestId(tablechildren, abstractPanelConfiguration.getName());
        tablechildren.setAttribute(ATTRIBUTE_GROUP_NAME, abstractPanelConfiguration.getName());
        if(abstractPanelConfiguration.getColspan() != null)
        {
            tablechildren.setColspan(abstractPanelConfiguration.getColspan().intValue());
        }
        if(abstractPanelConfiguration.getRowspan() != null)
        {
            tablechildren.setRowspan(abstractPanelConfiguration.getRowspan().intValue());
        }
        final String descriptionText = abstractPanelConfiguration.getDescription();
        if(StringUtils.isNotBlank(descriptionText))
        {
            final Div labelContainer = new Div();
            labelContainer.setSclass(SCLASS_DESCRIPTION);
            final Label description = new Label(resolveLabel(descriptionText));
            description.setSclass(SCLASS_DESCRIPTION_LABEL);
            labelContainer.appendChild(description);
            labelContainer.setParent(tablechildren);
            fireComponentRendered(description, component, abstractPanelConfiguration, object);
        }
        if(abstractPanelConfiguration instanceof CustomPanel)
        {
            final CustomPanel customPanel = (CustomPanel)abstractPanelConfiguration;
            final String clazz = customPanel.getClazz();
            final String springBean = customPanel.getSpringBean();
            final WidgetComponentRenderer<Component, CustomPanel, Object> customRenderer = resolveCustomComponentRenderer(springBean,
                            clazz, CustomPanel.class);
            new ProxyRenderer<>(this, component, abstractPanelConfiguration, object).render(customRenderer, tablechildren,
                            customPanel, object, dataType, widgetInstanceManager);
        }
        else if(abstractPanelConfiguration instanceof Panel)
        {
            final Panel panel = (Panel)abstractPanelConfiguration;
            for(final Object element : panel.getAttributeOrCustom())
            {
                if(element instanceof Attribute)
                {
                    final Attribute attribute = (Attribute)element;
                    new ProxyRenderer<>(this, component, abstractPanelConfiguration, object).render(createAttributeRenderer(),
                                    tablechildren, attribute, object, dataType, widgetInstanceManager);
                }
                else if(element instanceof CustomElement)
                {
                    final CustomElement definition = (CustomElement)element;
                    new ProxyRenderer<>(this, component, abstractPanelConfiguration, object).render(createCustomHtmlRenderer(),
                                    tablechildren, definition, object, dataType, widgetInstanceManager);
                }
            }
        }
        component.appendChild(tablechildren);
        fireComponentRendered(tablechildren, component, abstractPanelConfiguration, object);
    }


    protected WidgetComponentRenderer<Component, Attribute, Object> createAttributeRenderer()
    {
        return new AbstractWidgetComponentRenderer<Component, Attribute, Object>()
        {
            @Override
            public void render(final Component parent, final Attribute attribute, final Object object, final DataType dataType,
                            final WidgetInstanceManager widgetInstanceManager)
            {
                final Div attributeContainer = new Div();
                attributeContainer.setSclass(SCLASS_EDITOR_CONTAINER);
                try
                {
                    final boolean canRead = getPermissionFacade().canReadInstanceProperty(object, attribute.getQualifier());
                    if(canRead)
                    {
                        final DataAttribute dataAttribute = dataType.getAttribute(attribute.getQualifier());
                        if(dataAttribute != null)
                        {
                            new ProxyRenderer<>(this, parent, attribute, object).render(
                                            createAttributeEditorWithLabelRenderer(dataAttribute.isLocalized()), attributeContainer, attribute,
                                            object, dataType, widgetInstanceManager);
                        }
                    }
                    else
                    {
                        final HtmlBasedComponent label = renderNotReadableLabel(attributeContainer, attribute, dataType,
                                        getLabelService().getAccessDeniedLabel(object));
                        fireComponentRendered(label, parent, attribute, object);
                    }
                }
                catch(final Exception e)
                {
                    if(LOG.isWarnEnabled())
                    {
                        LOG.warn(e.getMessage(), e);
                    }
                    final Label label = appendLabel(attributeContainer, "error", null);
                    fireComponentRendered(label, parent, attribute, object);
                }
                attributeContainer.setParent(parent);
                fireComponentRendered(attributeContainer, parent, attribute, object);
            }
        };
    }


    @InextensibleMethod
    private WidgetComponentRenderer<Div, Attribute, Object> createAttributeEditorWithLabelRenderer(final boolean isLocalised)
    {
        return new AbstractWidgetComponentRenderer<Div, Attribute, Object>()
        {
            @Override
            public void render(final Div attributeContainer, final Attribute attribute, final Object object, final DataType dataType,
                            final WidgetInstanceManager wim)
            {
                try
                {
                    final Editor editor = createEditor(dataType, wim, attribute, object);
                    new EditorConfigurator(editor).setGroup(Objects.toString(attributeContainer.getAttribute(ATTRIBUTE_GROUP_NAME)));
                    if(!isLocalised)
                    {
                        final Label label = appendLabelForAttributeEditor(attributeContainer,
                                        resolveAttributeLabel(attribute, dataType), attribute.getQualifier(), dataType, attribute);
                        fireComponentRendered(label, attributeContainer, attribute, object);
                    }
                    attributeContainer.appendChild(editor);
                    fireComponentRendered(editor, attributeContainer, attribute, object);
                }
                catch(final ObjectNotPersistedAttributeReadException ex)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(
                                        String.format("Cannot access an attribute:%s from not persisted %s", attribute.getQualifier(), object),
                                        ex);
                    }
                    final HtmlBasedComponent label = renderNotReadableLabel(attributeContainer, attribute, dataType,
                                    resolveLabel(ATTR_NOT_SAVED_OBJECT));
                    fireComponentRendered(label, attributeContainer, attribute, object);
                }
            }
        };
    }


    protected WidgetComponentRenderer<Component, CustomElement, Object> createCustomHtmlRenderer()
    {
        return new AbstractWidgetComponentRenderer<Component, CustomElement, Object>()
        {
            @Override
            public void render(final Component parent, final CustomElement configuration, final Object object,
                            final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
            {
                final Div customContainer = new Div();
                customContainer.setSclass(SCLASS_CUSTOM_CONTAINER);
                final Html customHtml = createCustom(dataType, configuration, object);
                customContainer.appendChild(customHtml);
                fireComponentRendered(customHtml, parent, configuration, object);
                customContainer.setParent(parent);
                fireComponentRendered(customContainer, parent, configuration, object);
            }
        };
    }


    private Label appendLabelForAttributeEditor(final Div attributeContainer, final String labelText, final String tooltip,
                    final DataType dataType, final Attribute attribute)
    {
        final Label label = appendLabel(attributeContainer, labelText, tooltip);
        attributeDescriptionIconRenderer.renderDescriptionIcon(getAttributeDescription(dataType, attribute), attributeContainer);
        return label;
    }


    private Label appendLabel(final Div attributeContainer, final String labelText, final String tooltip)
    {
        final Label label = new Label(labelText);
        label.setSclass(SCLASS_LABEL);
        if(StringUtils.isNotBlank(tooltip))
        {
            label.setTooltiptext(tooltip);
        }
        attributeContainer.appendChild(label);
        return label;
    }
}
