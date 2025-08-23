/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.summaryview.renderer;

import com.hybris.cockpitng.config.summaryview.jaxb.AbstractSection;
import com.hybris.cockpitng.config.summaryview.jaxb.Actions;
import com.hybris.cockpitng.config.summaryview.jaxb.Attribute;
import com.hybris.cockpitng.config.summaryview.jaxb.CustomAttribute;
import com.hybris.cockpitng.config.summaryview.jaxb.CustomSection;
import com.hybris.cockpitng.config.summaryview.jaxb.DataQualityGroup;
import com.hybris.cockpitng.config.summaryview.jaxb.Section;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.dynamicforms.ComponentsVisitor;
import com.hybris.cockpitng.widgets.summaryview.CustomRendererClassUtil;
import com.hybris.cockpitng.widgets.summaryview.SummaryViewController;
import com.hybris.cockpitng.widgets.util.UILabelUtil;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Vlayout;

public class DefaultSummaryViewSectionRenderer extends AbstractWidgetComponentRenderer<Component, AbstractSection, Object>
{
    protected static final String SCLASS_SECTION_CONTAINER = "yw-summaryview-section-container";
    protected static final String SCLASS_SECTION_CONTAINER_CAPTION = "yw-summaryview-section-container-caption";
    protected static final String SCLASS_SECTION_CONTAINER_CONTENT = "yw-summaryview-section-container-content";
    protected static final String SCLASS_ACTIONS_COMPONENT = "yw-summaryview-actions-slot";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSummaryViewSectionRenderer.class);
    private ObjectValueService objectValueService;
    private WidgetComponentRenderer<Component, Attribute, Object> summaryViewAttributeRenderer;
    private WidgetComponentRenderer<Component, Attribute, Object> dataQualityGroupRenderer;


    @Override
    public void render(final Component parent, final AbstractSection sectionConfiguration, final Object data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Groupbox sectionContainer = createSectionContainer(parent, sectionConfiguration, data);
        sectionContainer.setAttribute(ComponentsVisitor.COMPONENT_CTX, sectionConfiguration);
        parent.appendChild(sectionContainer);
        if(sectionConfiguration instanceof CustomSection)
        {
            renderCustomSectionContent(sectionContainer, (CustomSection)sectionConfiguration, data, dataType,
                            widgetInstanceManager);
        }
        else if(sectionConfiguration instanceof Section)
        {
            renderSectionContent(sectionContainer, ((Section)sectionConfiguration), data, dataType, widgetInstanceManager);
        }
        fireComponentRendered(sectionContainer, parent, sectionConfiguration, data);
        fireComponentRendered(parent, sectionConfiguration, data);
    }


    protected Groupbox createSectionContainer(final Component parent, final AbstractSection sectionConfiguration,
                    final Object data)
    {
        final Groupbox sectionContainer = new Groupbox();
        sectionContainer.setSclass(SCLASS_SECTION_CONTAINER);
        YTestTools.modifyYTestId(sectionContainer, sectionConfiguration.getName());
        if(sectionConfiguration.getName() != null)
        {
            final Caption caption = createSectionContainerCaption(sectionConfiguration);
            sectionContainer.appendChild(caption);
            fireComponentRendered(caption, parent, sectionConfiguration, data);
        }
        return sectionContainer;
    }


    protected Caption createSectionContainerCaption(final AbstractSection sectionConfiguration)
    {
        final String sectionName = UILabelUtil.resolveLocalizedLabel(sectionConfiguration.getName());
        final Caption caption = new Caption(sectionName);
        caption.setTooltiptext(sectionName);
        caption.setSclass(SCLASS_SECTION_CONTAINER_CAPTION);
        YTestTools.modifyYTestId(caption, sectionConfiguration.getName() + "_caption");
        return caption;
    }


    protected void renderSectionContent(final Component parent, final Section sectionConfiguration, final Object data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Vlayout sectionContent = createSectionContentContainer();
        fireComponentRendered(sectionContent, parent, sectionConfiguration, data);
        sectionContent.setParent(parent);
        renderSectionElement(sectionContent, sectionConfiguration.getCustomAttributeOrAttributeOrActions(), data, dataType,
                        widgetInstanceManager);
    }


    protected void renderCustomSectionContent(final Component parent, final CustomSection sectionConfiguration, final Object data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final String beanName = sectionConfiguration.getSpringBean();
        final String className = sectionConfiguration.getClazz();
        final WidgetComponentRenderer<Component, CustomSection, Object> customSectionRenderer = CustomRendererClassUtil
                        .createRenderer(beanName, className);
        if(customSectionRenderer != null)
        {
            new ProxyRenderer(this, parent, sectionConfiguration, data).render(customSectionRenderer, parent, sectionConfiguration,
                            data, dataType, widgetInstanceManager);
        }
        else
        {
            LOG.error("Couldn't load custom renderer for setion {}. Bean name: [{}], class name: [{}]",
                            sectionConfiguration.getName(), beanName, className);
        }
    }


    protected Vlayout createSectionContentContainer()
    {
        final Vlayout sectionContent = new Vlayout();
        sectionContent.setSclass(SCLASS_SECTION_CONTAINER_CONTENT);
        sectionContent.setSpacing("auto");
        return sectionContent;
    }


    protected void renderSectionElement(final Component parent, final List<Positioned> elements, final Object data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        for(final Object element : elements)
        {
            if(element instanceof Actions)
            {
                new ProxyRenderer(this, parent, element, data).render(createActionsRenderer(), parent, element, data, dataType,
                                widgetInstanceManager);
            }
            else if(element instanceof CustomAttribute)
            {
                new ProxyRenderer(this, parent, element, data).render(createCustomAttributeRenderer((CustomAttribute)element),
                                parent, element, data, dataType, widgetInstanceManager);
            }
            else if(element instanceof Attribute)
            {
                new ProxyRenderer(this, parent, element, data).render(createAttributeRenderer(), parent, element, data, dataType,
                                widgetInstanceManager);
            }
            else if(element instanceof DataQualityGroup)
            {
                new ProxyRenderer(this, parent, element, data).render(createDataQualityGroupRenderer(), parent, element, data, dataType,
                                widgetInstanceManager);
            }
        }
    }


    protected com.hybris.cockpitng.components.Actions createActionsSlot(final Actions actions, final Object data,
                    final String typeCode, final WidgetInstanceManager widgetInstanceManager)
    {
        final com.hybris.cockpitng.components.Actions toolbar = new com.hybris.cockpitng.components.Actions();
        toolbar.setConfig(String.format("component=%s,type=%s", actions.getComponentId(), typeCode));
        toolbar.setGroup(actions.getGroup());
        toolbar.setWidgetInstanceManager(widgetInstanceManager);
        toolbar.setInputValue(data);
        final String renderer = StringUtils.isNotBlank(actions.getRenderer()) ? actions.getRenderer()
                        : widgetInstanceManager.getWidgetSettings().getString(SummaryViewController.SETTING_DEFAULT_ACTIONS_RENDERER);
        if(StringUtils.isNotBlank(renderer))
        {
            toolbar.setRenderer(renderer);
        }
        toolbar.initialize();
        UITools.modifySClass(toolbar, SCLASS_ACTIONS_COMPONENT, true);
        return toolbar;
    }


    protected WidgetComponentRenderer<Component, Attribute, Object> createCustomAttributeRenderer(
                    final CustomAttribute customAttributeConfiguration)
    {
        return CustomRendererClassUtil.createRenderer(customAttributeConfiguration.getSpringBean(),
                        customAttributeConfiguration.getClazz());
    }


    public WidgetComponentRenderer<Component, Attribute, Object> createAttributeRenderer()
    {
        return summaryViewAttributeRenderer;
    }


    protected WidgetComponentRenderer<Component, Actions, Object> createActionsRenderer()
    {
        return new AbstractWidgetComponentRenderer<Component, Actions, Object>()
        {
            @Override
            public void render(final Component component, final Actions configuration, final Object data, final DataType dataType,
                            final WidgetInstanceManager widgetInstanceManager)
            {
                final com.hybris.cockpitng.components.Actions actions = createActionsSlot(configuration, data, dataType.getCode(),
                                widgetInstanceManager);
                fireComponentRendered(actions, component, configuration, data);
                component.appendChild(actions);
            }
        };
    }


    private WidgetComponentRenderer createDataQualityGroupRenderer()
    {
        return dataQualityGroupRenderer;
    }


    protected ObjectValueService getObjectValueService()
    {
        return objectValueService;
    }


    @Required
    public void setObjectValueService(final ObjectValueService objectValueService)
    {
        this.objectValueService = objectValueService;
    }


    public void setSummaryViewAttributeRenderer(
                    final WidgetComponentRenderer<Component, Attribute, Object> summaryViewAttributeRenderer)
    {
        this.summaryViewAttributeRenderer = summaryViewAttributeRenderer;
    }


    public void setDataQualityGroupRenderer(final WidgetComponentRenderer<Component, Attribute, Object> dataQualityGroupRenderer)
    {
        this.dataQualityGroupRenderer = dataQualityGroupRenderer;
    }
}
