/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EditorArea;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Essentials;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Tab;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Editor area configuration wrapper which hides essentials section in tabs and puts it into overview tab.
 */
public class EditorAreaConfigWithOverviewTab extends EditorArea
{
    public static final String LABEL_EDITORAREA_TAB_OVERVIEW_TOOLTIP = "editorarea.tab.overview.tooltip";
    public static final String LABEL_EDITORAREA_TAB_OVERVIEW_TITLE = "editorarea.tab.overview.title";
    private final EditorArea target;


    protected EditorAreaConfigWithOverviewTab(final EditorArea target)
    {
        this.target = target;
        customTabOrTab = EditorAreaConfigWithOverviewTab.this.createTabsWithOverView(target.getCustomTabOrTab(),
                        target.getEssentials());
    }


    protected List<AbstractTab> createTabsWithOverView(final List<AbstractTab> customTabOrTab, final Essentials essentials)
    {
        final List<AbstractTab> tabs = customTabOrTab.stream().map(this::wrapTabOrCustomTab).collect(Collectors.toList());
        tabs.add(0, createOverviewTab(essentials, getMinPosition(tabs)));
        return tabs;
    }


    protected AbstractTab wrapTabOrCustomTab(final AbstractTab abstractTab)
    {
        if(abstractTab instanceof CustomTab)
        {
            return new CustomTabWithoutEssentials((CustomTab)abstractTab);
        }
        else if(abstractTab instanceof Tab)
        {
            return new TabWithoutEssentials((Tab)abstractTab);
        }
        else
        {
            return abstractTab;
        }
    }


    @Override
    public List<AbstractTab> getCustomTabOrTab()
    {
        // explicitly override for compatibility tests
        return this.customTabOrTab;
    }


    @Override
    public Essentials getEssentials()
    {
        return target.getEssentials();
    }


    @Override
    public String getName()
    {
        return target.getName();
    }


    @Override
    public boolean isHideTabNameIfOnlyOneVisible()
    {
        return target.isHideTabNameIfOnlyOneVisible();
    }


    @Override
    public String getLogicHandler()
    {
        return target.getLogicHandler();
    }


    @Override
    public String getViewMode()
    {
        return target.getViewMode();
    }


    private static BigInteger getMinPosition(final List<? extends Positioned> positioned)
    {
        final Optional<Long> min = positioned.stream()//
                        .filter(pos -> pos.getPosition() != null)//
                        .map(pos -> pos.getPosition().longValue())//
                        .min(Long::compareTo);
        return min.isPresent() ? BigInteger.valueOf(min.get()) : null;
    }


    protected Tab createOverviewTab(final Essentials essentials, final BigInteger minPosition)
    {
        final Tab overview = new Tab();
        overview.setDisplayEssentialSectionIfPresent(true);
        overview.setEssentials(essentials);
        overview.setPosition(minPosition != null ? BigInteger.valueOf(minPosition.longValue() - 1) : BigInteger.ZERO);
        overview.setTooltipText(LABEL_EDITORAREA_TAB_OVERVIEW_TOOLTIP);
        overview.setName(LABEL_EDITORAREA_TAB_OVERVIEW_TITLE);
        return overview;
    }


    protected static class TabWithoutEssentials extends TabWithoutEssentialsBase
    {
        TabWithoutEssentials(final Tab target)
        {
            super(target);
        }
    }


    protected static class CustomTabWithoutEssentials extends CustomTabWithoutEssentialsBase
    {
        CustomTabWithoutEssentials(final CustomTab target)
        {
            super(target);
        }
    }
}
