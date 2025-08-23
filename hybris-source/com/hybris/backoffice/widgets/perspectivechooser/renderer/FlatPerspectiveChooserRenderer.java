/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.perspectivechooser.renderer;

import com.hybris.backoffice.actionbar.impl.ActionComponentUtils;
import com.hybris.backoffice.actionbar.impl.MenuActionComponent;
import com.hybris.backoffice.actionbar.impl.MenuitemActionComponent;
import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.NavigationTree;
import com.hybris.backoffice.navigation.bar.NavigationContext;
import com.hybris.backoffice.navigation.bar.NavigationUtils;
import com.hybris.cockpitng.util.UITools;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

public class FlatPerspectiveChooserRenderer implements PerspectiveChooserRenderer
{
    private static final String SETTING_TEXT_MODE = "textMode";
    private static final String SCLASS_DOMAIN_NODE = "yw-domain-node";
    private static final String SCLASS_DOMAIN_NODE_LABEL = "yw-domain-node-label";
    private static final String SCLASS_PERSPECTIVE_ITEMS = "yw-perspective-items";
    private static final String SCLASS_PERSPECTIVE_DROPDOWN = "yw-perspective-dropdown";


    @Override
    public void renderTree(final Component parent, final NavigationContext context)
    {
        final NavigationTree tree = context.getActionTree();
        if(tree != null)
        {
            final Menubar menubar = new Menubar();
            UITools.modifySClass(menubar, SCLASS_PERSPECTIVE_ITEMS, true);
            menubar.setParent(parent);
            final boolean textMode = isTextMode(context);
            for(final NavigationNode node : tree.getRootNodes())
            {
                if(CollectionUtils.isEmpty(node.getChildren()))
                {
                    final Menuitem item = createMenuitem(context, node);
                    item.setParent(menubar);
                    if(textMode)
                    {
                        item.setImage(null);
                        item.setSclass(SCLASS_DOMAIN_NODE_LABEL);
                    }
                    else
                    {
                        item.setLabel(null);
                        item.setSclass(SCLASS_DOMAIN_NODE);
                    }
                }
                else
                {
                    final Menu menu = createMenu(context, node);
                    menu.setParent(menubar);
                    if(textMode)
                    {
                        menu.setImage(null);
                        menu.setSclass(SCLASS_DOMAIN_NODE_LABEL);
                    }
                    else
                    {
                        menu.setLabel(null);
                        menu.setSclass(SCLASS_DOMAIN_NODE);
                    }
                    for(final NavigationNode childNode : node.getChildren())
                    {
                        final Menuitem item = new Menuitem();
                        ActionComponentUtils.formatNodeElement(new MenuitemActionComponent(item), context, childNode);
                        if(textMode)
                        {
                            item.setImage(null);
                            item.setSclass(SCLASS_DOMAIN_NODE_LABEL);
                        }
                        else
                        {
                            item.setLabel(null);
                            item.setSclass(SCLASS_DOMAIN_NODE);
                        }
                        item.setParent(menu.getMenupopup());
                        item.addEventListener(Events.ON_CLICK, event -> NavigationUtils.nodeSelected(parent, context, item));
                    }
                }
            }
        }
    }


    protected Menuitem createMenuitem(final NavigationContext context, final NavigationNode navigationNode)
    {
        final Menuitem item = new Menuitem();
        ActionComponentUtils.formatNodeElement(new MenuitemActionComponent(item), context, navigationNode);
        return item;
    }


    protected Menu createMenu(final NavigationContext context, final NavigationNode navigationNode)
    {
        final Menu menu = new Menu();
        ActionComponentUtils.formatNodeElement(new MenuActionComponent(menu), context, navigationNode);
        final Menupopup mpop = createMenupopup(context, navigationNode);
        mpop.setParent(menu);
        return menu;
    }


    protected Menupopup createMenupopup(final NavigationContext context, final NavigationNode navigationNode)
    {
        final Menupopup mpop = new Menupopup();
        mpop.setSclass(SCLASS_PERSPECTIVE_DROPDOWN);
        return mpop;
    }


    protected boolean isTextMode(final NavigationContext context)
    {
        return context.getWidgetInstanceManager().getWidgetSettings().getBoolean(SETTING_TEXT_MODE);
    }


    @Override
    public void updatePerspectiveSelection(final Component parent, final NavigationContext context)
    {
        if(parent instanceof HtmlBasedComponent)
        {
            NavigationUtils.nodeSelectionChanged(parent, context, (HtmlBasedComponent)parent);
        }
    }
}
