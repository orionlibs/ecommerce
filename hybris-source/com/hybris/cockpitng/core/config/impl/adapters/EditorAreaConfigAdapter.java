/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.adapters;

import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPanel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractSection;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EditorArea;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Panel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Section;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Tab;
import com.hybris.cockpitng.dataaccess.services.PositionedSort;
import org.springframework.beans.factory.annotation.Required;

/**
 * EditorArea configuration adapter which is responsible for arranging tabs, sections, panels, attributes in proper order
 */
public class EditorAreaConfigAdapter implements CockpitConfigurationAdapter<EditorArea>
{
    private PositionedSort positionedSort;


    @Required
    public void setPositionedSort(final PositionedSort positionedSort)
    {
        this.positionedSort = positionedSort;
    }


    @Override
    public Class<EditorArea> getSupportedType()
    {
        return EditorArea.class;
    }


    @Override
    public EditorArea adaptAfterLoad(final ConfigContext context, final EditorArea editorAreaConfig) throws CockpitConfigurationException
    {
        positionedSort.sort(editorAreaConfig.getCustomTabOrTab());
        sortEssentialSection(context, editorAreaConfig);
        for(final AbstractTab abstractTab : editorAreaConfig.getCustomTabOrTab())
        {
            abstractTab.setEssentials(editorAreaConfig.getEssentials());
            if(abstractTab instanceof Tab)
            {
                final Tab tab = (Tab)abstractTab;
                positionedSort.sort(tab.getCustomSectionOrSection());
                for(final AbstractSection abstractSection : tab.getCustomSectionOrSection())
                {
                    if(abstractSection instanceof Section)
                    {
                        final Section section = (Section)abstractSection;
                        positionedSort.sort(section.getAttributeOrCustom());
                        positionedSort.sort(section.getCustomPanelOrPanel());
                        for(final AbstractPanel abstractPanel : section.getCustomPanelOrPanel())
                        {
                            if(abstractPanel instanceof Panel)
                            {
                                final Panel panel = (Panel)abstractPanel;
                                positionedSort.sort(panel.getAttributeOrCustom());
                            }
                        }
                    }
                }
            }
        }
        return editorAreaConfig;
    }


    protected void sortEssentialSection(final ConfigContext context, final EditorArea editorAreaConfig)
    {
        if(editorAreaConfig.getEssentials() != null)
        {
            if(editorAreaConfig.getEssentials().getEssentialSection() != null)
            {
                if(editorAreaConfig.getEssentials().getEssentialSection().getCustomPanelOrPanel() != null)
                {
                    positionedSort.sort(editorAreaConfig.getEssentials().getEssentialSection().getCustomPanelOrPanel());
                }
                if(editorAreaConfig.getEssentials().getEssentialSection().getAttributeOrCustom() != null)
                {
                    positionedSort.sort(editorAreaConfig.getEssentials().getEssentialSection().getAttributeOrCustom());
                }
            }
            else if(editorAreaConfig.getEssentials().getEssentialCustomSection() != null)
            {
                if(editorAreaConfig.getEssentials().getEssentialCustomSection().getCustomPanelOrPanel() != null)
                {
                    positionedSort.sort(editorAreaConfig.getEssentials().getEssentialCustomSection().getCustomPanelOrPanel());
                }
                if(editorAreaConfig.getEssentials().getEssentialCustomSection().getAttributeOrCustom() != null)
                {
                    positionedSort.sort(editorAreaConfig.getEssentials().getEssentialCustomSection().getAttributeOrCustom());
                }
            }
        }
    }


    @Override
    public EditorArea adaptBeforeStore(final ConfigContext context, final EditorArea editorArea) throws CockpitConfigurationException
    {
        return editorArea;
    }
}
