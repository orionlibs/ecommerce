/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.navigation;

import com.hybris.cockpitng.core.context.CockpitContext;
import com.hybris.cockpitng.tree.node.TypeNode;
import com.hybris.cockpitng.widgets.common.explorertree.ExplorerTreeController;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;

/**
 * Decorate NavigationNode with functionality of evaluating text to display
 */
public class NavigationNodeDecorator implements NavigationNode
{
    private final NavigationNode target;
    private String uiLabel;


    public NavigationNodeDecorator(final NavigationNode target)
    {
        this.target = target;
    }


    public NavigationNode getTarget()
    {
        return target;
    }


    public void calculateUiLabel(final ExplorerTreeController widgetController)
    {
        // 1) try the name localization key first; making this the highest priority you can always override localization any type of node (e.g. type node)
        if(StringUtils.isNotBlank(target.getNameLocKey()))
        {
            String label = widgetController.getLabel(target.getNameLocKey());
            if(StringUtils.isNotEmpty(label))
            {
                uiLabel = label;
                return;
            }
            else
            {
                // 2) try the global (not widget) level
                label = Labels.getLabel(target.getNameLocKey());
                if(StringUtils.isNotEmpty(label))
                {
                    uiLabel = label;
                    return;
                }
            }
        }
        // 3) handle a type node if this is a type node
        final String typeNodeLocalization = checkTypeNodeLocalization(widgetController);
        if(StringUtils.isNotBlank(typeNodeLocalization))
        {
            uiLabel = typeNodeLocalization;
            return;
        }
        if(StringUtils.isNotBlank(target.getNameLocKey()))
        {
            // 4) use the node name (if set)
            uiLabel = (StringUtils.isEmpty(target.getName()) ? target.getNameLocKey() : target.getName());
            return;
        }
        // 5) fallback to the node name or ID
        uiLabel = (StringUtils.isEmpty(target.getName()) ? target.getId() : target.getName());
    }


    private String checkTypeNodeLocalization(final ExplorerTreeController widgetController)
    {
        if(target instanceof TypeNode)
        {
            final String code = ((TypeNode)target).getCode();
            if(StringUtils.isNotBlank(code))
            {
                final String typeLabel = widgetController.getLabelService().getObjectLabel(code);
                if(typeLabel != null)
                {
                    return typeLabel;
                }
            }
        }
        return null;
    }


    public String getUiLabel(final ExplorerTreeController controller)
    {
        if(uiLabel == null)
        {
            calculateUiLabel(controller);
        }
        return uiLabel;
    }


    @Override
    public String getId()
    {
        return target.getId();
    }


    @Override
    public Object getData()
    {
        return target.getData();
    }


    @Override
    public String getNameLocKey()
    {
        return target.getNameLocKey();
    }


    @Override
    public String getName()
    {
        return target.getName();
    }


    @Override
    public String getDescriptionLocKey()
    {
        return target.getDescriptionLocKey();
    }


    @Override
    public String getDescription()
    {
        return target.getDescription();
    }


    @Override
    public String getIconUriLocKey()
    {
        return target.getIconUriLocKey();
    }


    @Override
    public String getIconUri()
    {
        return target.getIconUri();
    }


    @Override
    public NavigationNode getParent()
    {
        return target.getParent();
    }


    @Override
    public List<NavigationNode> getChildren()
    {
        return target.getChildren();
    }


    @Override
    public void setChildren(final List<NavigationNode> children)
    {
        target.setChildren(children);
    }


    @Override
    public void addChild(final NavigationNode child)
    {
        target.addChild(child);
    }


    @Override
    public void setParent(final NavigationNode parent)
    {
        target.setParent(parent);
    }


    @Override
    public int getLevel()
    {
        return target.getLevel();
    }


    @Override
    public boolean isDirectory()
    {
        return target.isDirectory();
    }


    @Override
    public boolean isExpandedByDefault()
    {
        return target.isExpandedByDefault();
    }


    @Override
    public boolean equals(final Object obj)
    {
        if(obj == null)
        {
            return false;
        }
        if(obj.getClass() == this.getClass())
        {
            return target.equals(((NavigationNodeDecorator)obj).getTarget());
        }
        return super.equals(obj);
    }


    @Override
    public int hashCode()
    {
        return target.hashCode();
    }


    @Override
    public CockpitContext getContext()
    {
        return target.getContext();
    }


    @Override
    public void setContext(final CockpitContext context)
    {
        target.setContext(context);
    }
}
