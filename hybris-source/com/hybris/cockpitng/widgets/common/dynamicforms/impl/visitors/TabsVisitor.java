/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.dynamicforms.impl.visitors;

import com.google.common.collect.Sets;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicForms;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicTab;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.ScriptingConfig;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;
import com.hybris.cockpitng.widgets.common.dynamicforms.ComponentsVisitor;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Tab;

/**
 * Visitor of {@link Tab} from UI elements. Uses as a configuration {@link DynamicForms#getTab()}
 */
public class TabsVisitor extends AbstractComponentsVisitor<Tab, DynamicTab>
{
    private static final Logger LOG = LoggerFactory.getLogger(TabsVisitor.class);


    @Override
    protected boolean canHandle(final Component component)
    {
        if(component instanceof Tab)
        {
            final String tabId = getComponentKey((Tab)component);
            if(tabId != null)
            {
                final Predicate<DynamicTab> isTabUsedInGoToTab = tab -> StringUtils.equals(tab.getGotoTabId(), tabId);
                final Predicate<DynamicTab> isTabUsedAsQualifier = tab -> StringUtils.equals(tab.getQualifier(), tabId);
                final Predicate<DynamicTab> isTabUsedAsFallback = tab -> StringUtils.equals(tab.getFallbackGotoTabId(), tabId);
                return getDynamicElements().stream().anyMatch(isTabUsedAsQualifier.or(isTabUsedInGoToTab).or(isTabUsedAsFallback));
            }
        }
        return false;
    }


    @Override
    protected String getComponentKey(final Tab component)
    {
        final Object abstractTab = component.getAttribute(ComponentsVisitor.COMPONENT_CTX);
        if(abstractTab instanceof AbstractTab)
        {
            return ((AbstractTab)abstractTab).getName();
        }
        return null;
    }


    @Override
    protected List<DynamicTab> getDynamicElements()
    {
        return getDynamicForms().getTab();
    }


    @Override
    protected void visitComponents(final DynamicTab dynamicTab, final Object target, final boolean initial)
    {
        if(StringUtils.isNotEmpty(dynamicTab.getQualifier()))
        {
            final Collection<Tab> zkTabs = getComponentKeyComponentsMap().get(dynamicTab.getQualifier());
            if(CollectionUtils.isNotEmpty(zkTabs))
            {
                applyVisibleIf(zkTabs, dynamicTab, target);
                applyDisabledIf(zkTabs, dynamicTab, target);
            }
        }
        if(!initial && StringUtils.isNotEmpty(dynamicTab.getGotoTabId()) && isGoToTab(dynamicTab, target))
        {
            gotoTabId(dynamicTab.getGotoTabId());
        }
    }


    /**
     * Opens a tab with given id. The tab is search in collected components.
     *
     * @param tabId tab id.
     * @return true if operation was successful.
     */
    protected boolean gotoTabId(final String tabId)
    {
        if(StringUtils.isNotBlank(tabId))
        {
            final Collection<Tab> gotoZkTabs = getComponentKeyComponentsMap().get(tabId);
            return goToFirstAccessibleTab(gotoZkTabs);
        }
        return false;
    }


    /**
     * Goes to tab with id defined in {@link DynamicTab#getFallbackGotoTabId()}
     *
     * @param zkTabs all tabs in the tabBox
     * @param dynamicTab dynamicForms tab config.
     */
    protected void gotoTabIdOrFirstAccessibleTab(final Collection<Tab> zkTabs, final DynamicTab dynamicTab)
    {
        if(!gotoTabId(dynamicTab.getFallbackGotoTabId()))
        {
            if(StringUtils.isNotEmpty(dynamicTab.getFallbackGotoTabId()) && CollectionUtils.isNotEmpty(zkTabs))
            {
                final Predicate<Tab> accessibleTab = tab -> dynamicTab.getFallbackGotoTabId().equals(getComponentKey(tab))
                                && tab.isVisible() && !tab.isDisabled();
                final Optional<Tab> tabToGoTo = zkTabs.stream().filter(accessibleTab).findFirst();
                if(tabToGoTo.isPresent())
                {
                    setSelectedTab(tabToGoTo.get());
                    return;
                }
            }
            goToFirstAccessibleTab(zkTabs);
        }
    }


    /**
     * Goes to first tab which is not disabled or hidden
     *
     * @param gotoZkTabs all tabs in the tabBox
     * @return true if operation was successful.
     */
    protected boolean goToFirstAccessibleTab(final Collection<Tab> gotoZkTabs)
    {
        if(CollectionUtils.isNotEmpty(gotoZkTabs))
        {
            final Predicate<Tab> notSelectedAccessibleTab = tab -> !tab.isSelected() && !tab.isDisabled() && tab.isVisible();
            final Optional<Tab> firstAccessibleTab = gotoZkTabs.stream().filter(notSelectedAccessibleTab).findFirst();
            if(firstAccessibleTab.isPresent())
            {
                setSelectedTab(firstAccessibleTab.get());
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("Tab with id %s has been selected", getComponentKey(firstAccessibleTab.get())));
                }
                return true;
            }
        }
        return false;
    }


    protected void applyVisibleIf(final Collection<Tab> zkTabs, final DynamicTab dynamicTab, final Object target)
    {
        if(StringUtils.isNotEmpty(dynamicTab.getVisibleIf()))
        {
            final boolean visible = isVisible(dynamicTab, target);
            zkTabs.forEach(zkTab -> {
                if(!visible && zkTab.isSelected())
                {
                    gotoTabIdOrFirstAccessibleTab(getAllTabsFromParent(zkTab), dynamicTab);
                }
                zkTab.setVisible(visible);
            });
        }
    }


    protected void applyDisabledIf(final Collection<Tab> zkTabs, final DynamicTab dynamicTab, final Object target)
    {
        if(StringUtils.isNotEmpty(dynamicTab.getDisabledIf()))
        {
            final boolean disabled = isDisabled(dynamicTab, target);
            zkTabs.forEach(zkTab -> {
                if(disabled && zkTab.isSelected())
                {
                    gotoTabIdOrFirstAccessibleTab(getAllTabsFromParent(zkTab), dynamicTab);
                }
                zkTab.setDisabled(disabled);
            });
        }
    }


    private Collection<Tab> getAllTabsFromParent(final Tab zkTab)
    {
        return zkTab.getTabbox().getTabs().getChildren();
    }


    /**
     * Checks configuration from {@link DynamicTab#gotoTabIf} against target object.
     *
     * @param element
     *           element of configuration
     * @param target
     *           target object
     * @return true if should go to tab with id {@link DynamicTab#gotoTabId}
     */
    protected boolean isGoToTab(final DynamicTab element, final Object target)
    {
        final ScriptingConfig sc = element.getScriptingConfig() == null ? getDefaultScriptingConfig() : element
                        .getScriptingConfig();
        return StringUtils.isNotBlank(element.getGotoTabIf())
                        && StringUtils.isNotBlank(element.getGotoTabId())
                        && Boolean.TRUE.equals(getExpressionEvaluator().evaluateExpression(sc.getGotoTabIfLanguage(),
                        sc.getGotoTabIfScriptType(), element.getGotoTabIf(), target));
    }


    /**
     * Sets selected tab and sends {@link Events#ON_SELECT event}.
     *
     * @param zkTab
     *           selected tab
     */
    protected void setSelectedTab(final Tab zkTab)
    {
        zkTab.getTabbox().setSelectedIndex(zkTab.getIndex());
        Events.postEvent(zkTab.getTabbox(), new SelectEvent<>(Events.ON_SELECT, zkTab.getTabbox(), Sets.newHashSet(zkTab)));
    }
}
