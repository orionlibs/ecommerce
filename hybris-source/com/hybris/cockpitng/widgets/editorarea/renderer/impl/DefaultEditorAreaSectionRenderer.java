/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.editorarea.renderer.impl;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.components.validation.EditorValidation;
import com.hybris.cockpitng.components.validation.ValidationRenderer;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPanel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomElement;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EssentialCustomSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EssentialSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Panel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Section;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotPersistedAttributeReadException;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.widgets.baseeditorarea.DefaultEditorAreaController;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.dynamicforms.ComponentsVisitor;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zkmax.zul.Tablechildren;
import org.zkoss.zkmax.zul.Tablelayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;

/**
 * Default renderer for editor area sections.
 */
public class DefaultEditorAreaSectionRenderer extends AbstractEditorAreaSectionRenderer<Object>
{
    public static final String MODEL_ESSENTIAL_SECTION_IS_OPEN = "essentialSectionIsOpen";
    /**
     * Used to notify that EditorArea content was detached
     */
    public static final String MODEL_EDITORS_DETACHED = "editorsDetached";
    public static final String SCLASS_CELL_LABEL_CONTAINER = "yw-editorarea-label-container";
    protected static final String MODEL_CURRENT_OBJECT = DefaultEditorAreaController.MODEL_CURRENT_OBJECT;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultEditorAreaSectionRenderer.class);
    private static final double HUNDRED = 100d;
    private static final String PERCENT = "%";
    private static final String ATTRIBUTE_GROUP_NAME = "group-name";
    private static final String SCLASS_COLLAPSEBUTTON = "yw-expandCollapse";
    private static final String SCLASS_GRPBOX = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox";
    private static final String SCLASS_CELL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell";
    private static final String SCLASS_CELL_LABEL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label";
    private static final String SCLASS_GRPBOX_CAPTION = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-caption";
    private static final String SCLASS_GRPBOX_ESSENTIAL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-essential";
    private static final String SCLASS_GRPBOX_CAPTION_ESSENTIAL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-caption-essential";
    private static final String SCLASS_MANDATORY_ATTRIBUTE_LABEL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label-mandatory-attribute";
    private WidgetComponentRenderer<Component, AbstractPanel, Object> editorAreaPanelRenderer;
    private ValidationRenderer validationRenderer;


    @Override
    public void render(final Component parent, final AbstractSection abstractSectionConfiguration, final Object object,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Groupbox sectionGrpBox = prepareContainer(parent, abstractSectionConfiguration, object, dataType,
                        widgetInstanceManager);
        sectionGrpBox.setAttribute(ATTRIBUTE_GROUP_NAME, abstractSectionConfiguration.getName());
        sectionGrpBox.setAttribute(ComponentsVisitor.COMPONENT_CTX, abstractSectionConfiguration);
        final Label description = renderDescription(abstractSectionConfiguration, sectionGrpBox);
        if(description != null)
        {
            fireComponentRendered(description, parent, abstractSectionConfiguration, object);
        }
        if(abstractSectionConfiguration instanceof EssentialSection
                        || abstractSectionConfiguration instanceof EssentialCustomSection)
        {
            sectionGrpBox.addEventListener(Events.ON_OPEN, (final OpenEvent event) -> widgetInstanceManager.getModel()
                            .setValue(MODEL_ESSENTIAL_SECTION_IS_OPEN, Boolean.valueOf(event.isOpen())));
        }
        if(abstractSectionConfiguration instanceof CustomSection)
        {
            final CustomSection customSection = (CustomSection)abstractSectionConfiguration;
            renderCustomSection(customSection, sectionGrpBox, customSection.isInitiallyOpened(), parent, object, dataType,
                            widgetInstanceManager);
        }
        else if(abstractSectionConfiguration instanceof Section)
        {
            renderSection((Section)abstractSectionConfiguration, sectionGrpBox, abstractSectionConfiguration.isInitiallyOpened(),
                            parent, object, dataType, widgetInstanceManager);
        }
        fireComponentRendered(sectionGrpBox, parent, abstractSectionConfiguration, object);
    }


    protected void renderCustomSection(final CustomSection customSection, final Groupbox sectionGrpBox,
                    final boolean initiallyOpened, final Component parent, final Object object, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final WidgetComponentRenderer<Component, CustomSection, Object> renderer = createCustomSectionRenderer(
                        customSection.getSpringBean(), customSection.getClazz());
        final ProxyRenderer<Component, AbstractSection, Object> proxyRenderer = new ProxyRenderer<>(this, parent, customSection,
                        object);
        //If not initially open, add listeners to wait for the event to load the attributes in the section
        if(!initiallyOpened)
        {
            addSectionOpenListener(customSection, sectionGrpBox, renderer, object, dataType, widgetInstanceManager, proxyRenderer);
        }
        else
        {
            proxyRenderer.render(renderer, sectionGrpBox, (CustomSection)customSection, object, dataType, widgetInstanceManager);
        }
    }


    private void addSectionOpenListener(final Section section, final Groupbox sectionGrpBox,
                    final WidgetComponentRenderer renderer, final Object object, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager, final ProxyRenderer<Component, AbstractSection, Object> proxyRenderer)
    {
        final SectionOpenListener sectionOpenListener = new SectionOpenListener(this, sectionGrpBox, section);
        sectionOpenListener.prepareOnEventData(proxyRenderer, renderer, object, dataType, widgetInstanceManager);
        sectionGrpBox.addEventListener(Events.ON_CLICK, sectionOpenListener);
    }


    protected void renderSection(final Section section, final Groupbox sectionGrpBox, final boolean initiallyOpened,
                    final Component parent, final Object object, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final WidgetComponentRenderer<Component, Section, Object> renderer = createSectionRenderer();
        final ProxyRenderer<Component, AbstractSection, Object> proxyRenderer = new ProxyRenderer<>(this, parent, section, object);
        //If not initially open, add listeners to wait for the event to load the attributes in the section
        if(!initiallyOpened)
        {
            addSectionOpenListener(section, sectionGrpBox, renderer, object, dataType, widgetInstanceManager, proxyRenderer);
        }
        else
        {
            proxyRenderer.render(renderer, sectionGrpBox, (Section)section, object, dataType, widgetInstanceManager);
        }
    }


    protected Groupbox prepareContainer(final Component parent, final AbstractSection abstractSectionConfiguration,
                    final Object object, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Groupbox sectionGrpBox = new Groupbox();
        final var sectionSClass = String.format("%s-%s", SCLASS_GRPBOX, abstractSectionConfiguration.getName().replace(".", "-"));
        UITools.modifySClass(sectionGrpBox, SCLASS_GRPBOX, true);
        if(isEssentialSection(abstractSectionConfiguration))
        {
            UITools.modifySClass(sectionGrpBox, SCLASS_GRPBOX_ESSENTIAL, true);
        }
        else
        {
            UITools.modifySClass(sectionGrpBox, sectionSClass, true);
        }
        sectionGrpBox.setParent(parent);
        setSectionOpenAttribute(abstractSectionConfiguration, sectionGrpBox, widgetInstanceManager);
        final Caption caption = prepareContainerCaption(abstractSectionConfiguration);
        if(caption != null)
        {
            sectionGrpBox.appendChild(caption);
            final Button expandButton = new Button();
            expandButton.setSclass(SCLASS_COLLAPSEBUTTON);
            expandButton.addEventListener(Events.ON_CLICK, e -> {
                // If this section not initially render, then render this section.
                if(!isSectionRendered(sectionGrpBox))
                {
                    if(abstractSectionConfiguration instanceof CustomSection)
                    {
                        final CustomSection customSection = (CustomSection)abstractSectionConfiguration;
                        renderCustomSection(customSection, sectionGrpBox, true, parent, object, dataType, widgetInstanceManager);
                    }
                    if(abstractSectionConfiguration instanceof Section)
                    {
                        renderSection((Section)abstractSectionConfiguration, sectionGrpBox, true, parent, object, dataType,
                                        widgetInstanceManager);
                    }
                }
                sectionGrpBox.setOpen(!sectionGrpBox.isOpen());
            });
            caption.appendChild(expandButton);
            fireComponentRendered(caption, parent, abstractSectionConfiguration, object);
        }
        YTestTools.modifyYTestId(sectionGrpBox, abstractSectionConfiguration.getName());
        return sectionGrpBox;
    }


    /**
     * @deprecated since 2205, please use the
     *             {@link #prepareContainer(Component, AbstractSection, Object, DataType, WidgetInstanceManager)} instead.
     */
    @Deprecated(since = "2205", forRemoval = true)
    protected Groupbox prepareContainer(final Component parent, final AbstractSection abstractSectionConfiguration,
                    final Object object, final WidgetInstanceManager widgetInstanceManager)
    {
        final Groupbox sectionGrpBox = new Groupbox();
        UITools.modifySClass(sectionGrpBox, SCLASS_GRPBOX, true);
        if(isEssentialSection(abstractSectionConfiguration))
        {
            UITools.modifySClass(sectionGrpBox, SCLASS_GRPBOX_ESSENTIAL, true);
        }
        sectionGrpBox.setParent(parent);
        setSectionOpenAttribute(abstractSectionConfiguration, sectionGrpBox, widgetInstanceManager);
        final Caption caption = prepareContainerCaption(abstractSectionConfiguration);
        if(caption != null)
        {
            sectionGrpBox.appendChild(caption);
            final Button expandButton = new Button();
            expandButton.setSclass(SCLASS_COLLAPSEBUTTON);
            expandButton.addEventListener(Events.ON_CLICK, e -> sectionGrpBox.setOpen(!sectionGrpBox.isOpen()));
            caption.appendChild(expandButton);
            fireComponentRendered(caption, parent, abstractSectionConfiguration, object);
        }
        YTestTools.modifyYTestId(sectionGrpBox, abstractSectionConfiguration.getName());
        return sectionGrpBox;
    }


    private boolean isSectionRendered(final Groupbox sectionGrpBox)
    {
        // If this section has SectionOpenListener, then set isSectionRendered = false and remove this listener.
        for(final EventListener<? extends Event> eventEventListener : sectionGrpBox.getEventListeners(Events.ON_CLICK))
        {
            if(eventEventListener instanceof SectionOpenListener)
            {
                sectionGrpBox.removeEventListener(Events.ON_CLICK, eventEventListener);
                return false;
            }
        }
        return true;
    }


    protected Caption prepareContainerCaption(final AbstractSection abstractSectionConfiguration)
    {
        final String sectionName = resolveLabel(abstractSectionConfiguration.getName());
        final Caption caption = new Caption(sectionName);
        caption.setTooltiptext(createSectionTooltipText(abstractSectionConfiguration));
        YTestTools.modifyYTestId(caption, abstractSectionConfiguration.getName() + "_caption");
        caption.setSclass(SCLASS_GRPBOX_CAPTION);
        if(isEssentialSection(abstractSectionConfiguration))
        {
            UITools.modifySClass(caption, SCLASS_GRPBOX_CAPTION_ESSENTIAL, true);
        }
        return caption;
    }


    private String createSectionTooltipText(final AbstractSection section)
    {
        final StringBuilder result = new StringBuilder();
        result.append(section.getName());
        if(isPositionAvailable(section))
        {
            result.append(" [");
            result.append(section.getPosition().intValue());
            result.append("]");
        }
        return result.toString();
    }


    private boolean isPositionAvailable(final AbstractSection section)
    {
        return section.getPosition() != null;
    }


    protected Label renderDescription(final AbstractSection abstractSectionConfiguration, final Component sectionGrpBox)
    {
        final String descriptionText = abstractSectionConfiguration.getDescription();
        if(StringUtils.isNotBlank(descriptionText))
        {
            final Div labelContainer = new Div();
            labelContainer.setSclass(SCLASS_DESCRIPTION);
            final Label description = new Label(resolveLabel(descriptionText));
            description.setSclass(SCLASS_DESCRIPTION_LABEL);
            labelContainer.appendChild(description);
            labelContainer.setParent(sectionGrpBox);
            return description;
        }
        else
        {
            return null;
        }
    }


    protected void setSectionOpenAttribute(final AbstractSection abstractSectionConfiguration, final Groupbox sectionGrpBox,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        if(abstractSectionConfiguration instanceof EssentialSection
                        || abstractSectionConfiguration instanceof EssentialCustomSection)
        {
            final Boolean essentialSectionIsOpen = widgetInstanceManager.getModel().getValue(MODEL_ESSENTIAL_SECTION_IS_OPEN,
                            Boolean.class);
            if(essentialSectionIsOpen != null)
            {
                sectionGrpBox.setOpen(essentialSectionIsOpen.booleanValue());
            }
            else
            {
                sectionGrpBox.setOpen(abstractSectionConfiguration.isInitiallyOpened());
                widgetInstanceManager.getModel().setValue(MODEL_ESSENTIAL_SECTION_IS_OPEN,
                                Boolean.valueOf(abstractSectionConfiguration.isInitiallyOpened()));
            }
            final ValueObserver essentialSectionOpenObserver = () -> {
                final Boolean currentValue = widgetInstanceManager.getModel().getValue(MODEL_ESSENTIAL_SECTION_IS_OPEN,
                                Boolean.class);
                if(currentValue != null)
                {
                    sectionGrpBox.setOpen(currentValue.booleanValue());
                }
            };
            widgetInstanceManager.getModel().addObserver(MODEL_ESSENTIAL_SECTION_IS_OPEN, essentialSectionOpenObserver);
            final EventListener<Event> detachListener = new EventListener<Event>()
            {
                @Override
                public void onEvent(final Event event) throws Exception
                {
                    widgetInstanceManager.getModel().removeObserver(MODEL_ESSENTIAL_SECTION_IS_OPEN, essentialSectionOpenObserver);
                    widgetInstanceManager.getWidgetslot().removeEventListener(DefaultEditorAreaController.ON_EDITORS_DETACHED, this);
                }
            };
            widgetInstanceManager.getWidgetslot().addEventListener(DefaultEditorAreaController.ON_EDITORS_DETACHED, detachListener);
        }
        else
        {
            sectionGrpBox.setOpen(abstractSectionConfiguration.isInitiallyOpened());
        }
    }


    protected WidgetComponentRenderer<Component, Section, Object> createSectionRenderer()
    {
        return new AbstractWidgetComponentRenderer<Component, Section, Object>()
        {
            @Override
            public void render(final Component parent, final Section section, final Object object, final DataType dataType,
                            final WidgetInstanceManager widgetInstanceManager)
            {
                final int columns = getNoOfColumns(section);
                final String width = calculateWidthPercentage(columns);
                final List<Panel> panels = section.getCustomPanelOrPanel();
                final ProxyRenderer<Component, Section, Object> proxyRenderer = new ProxyRenderer<>(this, parent, section, object);
                if(CollectionUtils.isNotEmpty(panels))
                {
                    renderAttributesInPanels(columns, proxyRenderer, panels, widgetInstanceManager, dataType, width, object);
                }
                renderAttributes(columns, proxyRenderer, width, dataType, widgetInstanceManager, object);
            }
        };
    }


    protected WidgetComponentRenderer<Component, CustomSection, Object> createCustomSectionRenderer(final String springBean,
                    final String clazz)
    {
        return resolveCustomComponentRenderer(springBean, clazz, CustomSection.class);
    }


    private int getNoOfColumns(final Section section)
    {
        int columns = section.getColumns().intValue();
        if(columns < 1)
        {
            columns = 1;
        }
        return columns;
    }


    protected void renderAttributes(final List<? extends Positioned> attributeOrCustom,
                    final ProxyRenderer<Component, Section, Object> proxyRenderer, final int noOfColumns, final String columnWidth,
                    final DataType genericType, final WidgetInstanceManager widgetInstanceManager, final Object object)
    {
        if(CollectionUtils.isNotEmpty(attributeOrCustom))
        {
            Hbox hbox = new Hbox();
            for(int index = 0; index < attributeOrCustom.size(); index++)
            {
                final Positioned element = attributeOrCustom.get(index);
                hbox = createNewRowIfNeeded(hbox, proxyRenderer.getParent(), noOfColumns, index);
                final Cell cell = new Cell();
                hbox.appendChild(cell);
                cell.setWidth(columnWidth);
                cell.setSclass(SCLASS_CELL);
                cell.setAttribute(ATTRIBUTE_GROUP_NAME,
                                Objects.toString(proxyRenderer.getParent().getAttribute(ATTRIBUTE_GROUP_NAME)));
                if(element instanceof Attribute)
                {
                    final Attribute attribute = (Attribute)element;
                    proxyRenderer.render(createAttributeRenderer(), cell, attribute, object, genericType, widgetInstanceManager);
                }
                else if(element instanceof CustomElement)
                {
                    final CustomElement definition = (CustomElement)element;
                    final WidgetComponentRenderer<Cell, CustomElement, Object> renderer = new AbstractWidgetComponentRenderer<Cell, CustomElement, Object>()
                    {
                        @Override
                        public void render(final Cell cell, final CustomElement configuration, final Object o, final DataType dataType,
                                        final WidgetInstanceManager widgetInstanceManager)
                        {
                            final Html html = renderCustomElement(definition, cell, genericType, object);
                            fireComponentRendered(html, cell, configuration, o);
                        }
                    };
                    proxyRenderer.render(renderer, cell, definition, object, genericType, widgetInstanceManager);
                }
            }
        }
    }


    protected void renderAttributes(final int noOfColumns, final ProxyRenderer<Component, Section, Object> proxyRenderer,
                    final String columnWidth, final DataType genericType, final WidgetInstanceManager widgetInstanceManger,
                    final Object object)
    {
        final List<? extends Positioned> attributes = proxyRenderer.getConfig().getAttributeOrCustom();
        renderAttributes(attributes, proxyRenderer, noOfColumns, columnWidth, genericType, widgetInstanceManger, object);
    }


    protected WidgetComponentRenderer<Cell, Attribute, Object> createAttributeRenderer()
    {
        return new AttributeRenderer();
    }


    protected WidgetComponentRenderer<Cell, Attribute, Object> createAttributeEditorWithLabelRenderer()
    {
        return new AttributeEditorWithLabelRenderer();
    }


    protected Html renderCustomElement(final CustomElement definition, final Component parent, final DataType genericType,
                    final Object object)
    {
        final Html html = createCustom(genericType, definition, object);
        parent.appendChild(html);
        return html;
    }


    protected void renderAttributesInPanels(final int columns, final ProxyRenderer<Component, Section, Object> proxyRenderer,
                    final List<Panel> panels, final WidgetInstanceManager widgetInstanceManger, final DataType dataType,
                    final String columnWidth, final Object object)
    {
        final Tablelayout tablelayout = new Tablelayout();
        tablelayout.setColumns(columns);
        tablelayout.setParent(proxyRenderer.getParent());
        for(final AbstractPanel panel : panels)
        {
            proxyRenderer.render(getEditorAreaPanelRenderer(), tablelayout, panel, object, dataType, widgetInstanceManger);
            final Component lastChild = tablelayout.getLastChild();
            if(lastChild instanceof Tablechildren)
            {
                ((Tablechildren)lastChild).setWidth(columnWidth);
            }
        }
        if(LOG.isDebugEnabled() && CollectionUtils.isNotEmpty(proxyRenderer.getConfig().getAttributeOrCustom()))
        {
            final String errorMsg = "Section [" + proxyRenderer.getConfig().getName()
                            + "] is composed with panels. Attributes and custom elements ignored! Add them to the panel " + "configuration.";
            LOG.debug(errorMsg);
        }
    }


    private Hbox createNewRowIfNeeded(final Hbox hbox, final Component parent, final int noOfColumns, final int currentCellIndex)
    {
        Hbox result = hbox;
        if(hbox == null || currentCellIndex % noOfColumns == 0)
        {
            result = new Hbox();
            result.setParent(parent);
        }
        return result;
    }


    protected String calculateWidthPercentage(final int columns)
    {
        return ((int)Math.ceil(HUNDRED / columns)) + PERCENT;
    }


    protected boolean isEssentialSection(final AbstractSection abstractSectionConfiguration)
    {
        return (abstractSectionConfiguration instanceof EssentialCustomSection
                        || abstractSectionConfiguration instanceof EssentialSection);
    }


    /**
     * @return the editorAreaPanelRenderer
     */
    protected WidgetComponentRenderer<Component, AbstractPanel, Object> getEditorAreaPanelRenderer()
    {
        return editorAreaPanelRenderer;
    }


    @Required
    public void setEditorAreaPanelRenderer(final WidgetComponentRenderer<Component, AbstractPanel, Object> editorAreaPanelRenderer)
    {
        this.editorAreaPanelRenderer = editorAreaPanelRenderer;
    }


    protected ValidationRenderer getValidationRenderer()
    {
        return validationRenderer;
    }


    @Required
    public void setValidationRenderer(final ValidationRenderer validationRenderer)
    {
        this.validationRenderer = validationRenderer;
    }


    private class AttributeEditorWithLabelRenderer extends AbstractWidgetComponentRenderer<Cell, Attribute, Object>
    {
        @Override
        public void render(final Cell cell, final Attribute attribute, final Object object, final DataType genericType,
                        final WidgetInstanceManager wim)
        {
            final String qualifier = attribute.getQualifier();
            final DataAttribute dataAttribute = genericType.getAttribute(qualifier);
            try
            {
                final Editor editor = createEditor(genericType, wim, attribute, object);
                if(!dataAttribute.isLocalized())
                {
                    final Div labelCtr = new Div();
                    UITools.modifySClass(labelCtr, SCLASS_CELL_LABEL_CONTAINER, true);
                    if(attribute.getLabel() == null || StringUtils.isNotEmpty(attribute.getLabel()))
                    {
                        final Label label = new Label(resolveAttributeLabel(attribute, genericType));
                        label.setTooltiptext(qualifier);
                        editor.addEventListener(EditorValidation.ON_VALIDATION_CHANGED, e -> {
                            final ValidationResult validationResult = (ValidationResult)e.getData();
                            final String validationSclass = validationRenderer
                                            .getSeverityStyleClass(validationResult.getHighestSeverity());
                            validationRenderer.cleanAllValidationCss(labelCtr);
                            if(StringUtils.isNotBlank(validationSclass))
                            {
                                UITools.modifySClass(labelCtr, validationSclass, true);
                            }
                        });
                        if(dataAttribute.isMandatory())
                        {
                            UITools.modifySClass(label, SCLASS_MANDATORY_ATTRIBUTE_LABEL, true);
                        }
                        else
                        {
                            UITools.modifySClass(label, SCLASS_CELL_LABEL, true);
                        }
                        labelCtr.appendChild(label);
                    }
                    final String desc = getAttributeDescription(genericType, attribute);
                    attributeDescriptionIconRenderer.renderDescriptionIcon(desc, labelCtr);
                    cell.appendChild(labelCtr);
                    fireComponentRendered(labelCtr, cell, attribute, object);
                }
                cell.appendChild(editor);
                fireComponentRendered(editor, cell, attribute, object);
            }
            catch(final ObjectNotPersistedAttributeReadException ex)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("Cannot access an attribute:%s from not persisted %s", qualifier, object), ex);
                }
                final HtmlBasedComponent label = renderNotReadableLabel(cell, attribute, genericType,
                                resolveLabel(ATTR_NOT_SAVED_OBJECT));
                fireComponentRendered(label, cell, attribute, object);
            }
        }
    }


    private class AttributeRenderer extends AbstractWidgetComponentRenderer<Cell, Attribute, Object>
    {
        @Override
        public void render(final Cell cell, final Attribute attribute, final Object object, final DataType genericType,
                        final WidgetInstanceManager widgetInstanceManager)
        {
            final String qualifier = attribute.getQualifier();
            final boolean canReadProperty = getPermissionFacade().canReadInstanceProperty(object, qualifier);
            final ProxyRenderer<Cell, Attribute, Object> proxyRenderer = new ProxyRenderer<>(this, cell, attribute, object);
            if(canReadProperty)
            {
                final DataAttribute dataAttribute = genericType.getAttribute(attribute.getQualifier());
                if(dataAttribute == null)
                {
                    LOG.error("Property {} was not found for type {}", attribute.getQualifier(), genericType.getCode());
                }
                else
                {
                    final WidgetComponentRenderer<Cell, Attribute, Object> renderer = createAttributeEditorWithLabelRenderer();
                    proxyRenderer.render(renderer, cell, attribute, object, genericType, widgetInstanceManager);
                }
            }
            else
            {
                final WidgetComponentRenderer<HtmlBasedComponent, Attribute, String> renderer = createNotReadableAttributeLabelRenderer();
                final String accessDeniedLabelValue = getLabelService().getAccessDeniedLabel(object);
                proxyRenderer.render(renderer, cell, attribute, accessDeniedLabelValue, genericType, widgetInstanceManager);
            }
        }
    }


    /**
     * Section listener which will render the attributes of the section when open/click event is received.
     */
    private class SectionOpenListener implements EventListener<Event>
    {
        private DefaultEditorAreaSectionRenderer sectionRenderer;
        private ProxyRenderer<Component, AbstractSection, Object> proxyRenderer;
        private WidgetComponentRenderer renderer;
        private Groupbox sectionGrpBox;
        private AbstractSection section;
        private Object object;
        private DataType dataType;
        private WidgetInstanceManager widgetInstanceManager;


        public SectionOpenListener(final DefaultEditorAreaSectionRenderer sectionRenderer, final Groupbox sectionGrpBox,
                        final AbstractSection section)
        {
            this.sectionRenderer = sectionRenderer;
            this.sectionGrpBox = sectionGrpBox;
            this.section = section;
        }


        public void prepareOnEventData(final ProxyRenderer<Component, AbstractSection, Object> proxyRenderer,
                        final WidgetComponentRenderer renderer, final Object object, final DataType dataType,
                        final WidgetInstanceManager widgetInstanceManager)
        {
            this.proxyRenderer = proxyRenderer;
            this.renderer = renderer;
            this.object = object;
            this.dataType = dataType;
            this.widgetInstanceManager = widgetInstanceManager;
        }


        @Override
        public void onEvent(final Event event) throws Exception
        {
            proxyRenderer.render(renderer, sectionGrpBox, section, object, dataType, widgetInstanceManager);
            sectionGrpBox.removeEventListener(Events.ON_CLICK, this);
            sectionRenderer.fireComponentRendered(sectionGrpBox, sectionGrpBox.getParent(), section, object);
        }
    }
}
