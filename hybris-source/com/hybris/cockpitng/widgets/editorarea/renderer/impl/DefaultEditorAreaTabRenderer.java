/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.editorarea.renderer.impl;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Tab;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaRendererUtils;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;

public class DefaultEditorAreaTabRenderer extends AbstractEditorAreaComponentRenderer<AbstractTab, Object>
{
    private WidgetComponentRenderer<Component, AbstractSection, Object> editorAreaSectionRenderer;


    @Override
    public void render(final Component parent, final AbstractTab abstractTabConfiguration, final Object object,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Div panelContainer = new Div();
        panelContainer.setParent(parent);
        final ProxyRenderer<Component, AbstractTab, Object> proxyRenderer = new ProxyRenderer<>(this, parent,
                        abstractTabConfiguration, object);
        if(abstractTabConfiguration instanceof CustomTab)
        {
            final CustomTab customTab = (CustomTab)abstractTabConfiguration;
            final String clazz = customTab.getClazz();
            final String springBean = customTab.getSpringBean();
            final WidgetComponentRenderer<Component, CustomTab, Object> customRenderer = resolveCustomComponentRenderer(springBean,
                            clazz, CustomTab.class);
            if(customTab.isDisplayEssentialSectionIfPresent() && customTab.getEssentials() != null)
            {
                final AbstractSection essentialSection = customTab.getEssentials().getEssentialSection() != null
                                ? customTab.getEssentials().getEssentialSection() : customTab.getEssentials().getEssentialCustomSection();
                if(essentialSection != null)
                {
                    proxyRenderer.render(getEditorAreaSectionRenderer(), panelContainer, essentialSection, object, dataType,
                                    widgetInstanceManager);
                }
            }
            proxyRenderer.render(customRenderer, panelContainer, customTab, object, dataType, widgetInstanceManager);
        }
        else if(abstractTabConfiguration instanceof Tab)
        {
            final Tab tab = (Tab)abstractTabConfiguration;
            final Collection<AbstractSection> sections = EditorAreaRendererUtils.getSections(tab);
            if(CollectionUtils.isNotEmpty(sections))
            {
                for(final AbstractSection abstractSection : sections)
                {
                    proxyRenderer.render(getEditorAreaSectionRenderer(), panelContainer, abstractSection, object, dataType,
                                    widgetInstanceManager);
                }
            }
        }
    }


    protected WidgetComponentRenderer<Component, AbstractSection, Object> getEditorAreaSectionRenderer()
    {
        return editorAreaSectionRenderer;
    }


    @Required
    public void setEditorAreaSectionRenderer(
                    final WidgetComponentRenderer<Component, AbstractSection, Object> editorAreaSectionRenderer)
    {
        this.editorAreaSectionRenderer = editorAreaSectionRenderer;
    }
}
