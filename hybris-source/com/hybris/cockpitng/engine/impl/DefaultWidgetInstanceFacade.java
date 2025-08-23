/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetInstanceSettings;
import com.hybris.cockpitng.core.WidgetService;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.WidgetInstanceFacade;
import com.hybris.cockpitng.core.ui.WidgetInstanceService;
import com.hybris.cockpitng.core.util.impl.WidgetTreeUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation of the WidgetInstanceFacade for the default widget engine.
 */
public class DefaultWidgetInstanceFacade implements WidgetInstanceFacade
{
    public static final String CHILD_INDEX = "_childIndex";
    private WidgetInstanceService widgetInstanceService;
    private Comparator<Widget> widgetIndexComparator = new Comparator<Widget>()
    {
        @Override
        public int compare(final Widget widget1, final Widget widget2)
        {
            final int int1 = widget1.getWidgetSettings().getInt(CHILD_INDEX);
            final int int2 = widget2.getWidgetSettings().getInt(CHILD_INDEX);
            return int1 - int2;
        }
    };


    @Override
    public WidgetInstance getRootWidgetInstance(final Widget rootWidget)
    {
        WidgetInstance ret = null;
        final List<WidgetInstance> instances = widgetInstanceService.getRootWidgetInstances(rootWidget);
        if(instances.isEmpty())
        {
            if(shouldCreateInstance(rootWidget, null))
            {
                ret = widgetInstanceService.createRootWidgetInstance(rootWidget);
            }
        }
        else
        {
            ret = instances.iterator().next();
        }
        return ret;
    }


    @Override
    public List<WidgetInstance> getWidgetInstances(final WidgetInstance parentInstance, final String slotId,
                    final boolean isSingleSlot)
    {
        List<Widget> childWidgets;
        if(isComposedRootSlot(parentInstance, slotId)) // composed root situation special handling
        {
            childWidgets = Collections.singletonList(parentInstance.getWidget().getComposedRootInstance());
        }
        else if(isPartOfGroup(parentInstance)) // widget group situation special handling
        {
            childWidgets = getChildrenForSlot(parentInstance.getWidget(), slotId);
            if(childWidgets.isEmpty())
            {
                final Widget parentWidget = parentInstance.getWidget();
                final Widget rootWidget = WidgetTreeUtils.getRootWidget(parentWidget);
                if(rootWidget != null && rootWidget.getGroupContainer() != null)
                {
                    // search for "real" children
                    final String pathSlotID = WidgetTreeUtils.getSlotIdPathToComposedRoot(parentWidget)
                                    + WidgetTreeUtils.COMPOSED_SLOT_SEPARATOR + slotId;
                    childWidgets = getChildrenForSlot(rootWidget.getGroupContainer(), pathSlotID);
                }
            }
        }
        else
        {
            childWidgets = getChildrenForSlot(parentInstance.getWidget(), slotId);
        }
        return getWidgetInstances(childWidgets, parentInstance);
    }


    @Override
    public List<WidgetInstance> getWidgetInstances(final WidgetInstance parentInstance)
    {
        return getWidgetInstances(parentInstance.getWidget().getChildren(), parentInstance);
    }


    @Override
    public List<WidgetInstance> getWidgetInstances(final Widget widget, final WidgetInstance parentInstance)
    {
        final List<WidgetInstance> result = new ArrayList<WidgetInstance>();
        final List<WidgetInstance> childInstances = getWidgetInstanceService().getWidgetInstances(widget, parentInstance);
        if(childInstances.isEmpty())
        {
            if(canCreateInstance(widget, parentInstance) && isAutoCreateInstance(widget, parentInstance))
            {
                final WidgetInstance newInstance = getWidgetInstanceService().createWidgetInstance(widget, parentInstance);
                if(newInstance != null)
                {
                    result.add(newInstance);
                }
            }
        }
        else
        {
            if(canCreateInstance(widget, parentInstance))
            {
                result.addAll(childInstances);
            }
            else
            {
                // auto destroy instances of a widget which this user should not see
                for(final WidgetInstance childInstance : childInstances)
                {
                    getWidgetInstanceService().removeWidgetInstance(childInstance);
                }
            }
        }
        return result;
    }


    private List<WidgetInstance> getWidgetInstances(final List<Widget> childWidgets, final WidgetInstance parentInstance)
    {
        if(childWidgets.isEmpty())
        {
            return Collections.<WidgetInstance>emptyList(); // no widgets - no instances
        }
        final List<WidgetInstance> result = new ArrayList<WidgetInstance>();
        for(final Widget childWidget : childWidgets)
        {
            result.addAll(getWidgetInstances(childWidget, parentInstance));
        }
        return result;
    }


    private boolean isComposedRootSlot(final WidgetInstance parentInstance, final String slotId)
    {
        final Widget composedRootInstance = parentInstance.getWidget().getComposedRootInstance();
        return composedRootInstance != null && WidgetService.COMPOSED_ROOT_SLOT_ID.equals(slotId);
    }


    private boolean isPartOfGroup(final WidgetInstance parentInstance)
    {
        return parentInstance != null && parentInstance.getWidget().isPartOfGroup();
    }


    @Override
    public WidgetInstance createWidgetInstance(final Widget widget, final WidgetInstance parentInstance, final Object creator)
    {
        if(canCreateInstance(widget, parentInstance))
        {
            return getWidgetInstanceService().createWidgetInstance(widget, parentInstance, creator);
        }
        else
        {
            return null;
        }
    }


    @Override
    public WidgetInstance createWidgetInstance(final Widget widget, final WidgetInstance parentInstance)
    {
        if(canCreateInstance(widget, parentInstance))
        {
            return getWidgetInstanceService().createWidgetInstance(widget, parentInstance);
        }
        else
        {
            return null;
        }
    }


    @Override
    public void removeWidgetInstance(final WidgetInstance instance)
    {
        if(canRemoveInstance(instance))
        {
            getWidgetInstanceService().removeWidgetInstance(instance);
        }
    }


    @Override
    public boolean canCreateInstance(final Widget widget, final WidgetInstance parentInstance)
    {
        return true;
    }


    @Override
    public boolean canRemoveInstance(final WidgetInstance instance)
    {
        return true;
    }


    @Override
    public List<Widget> getPossibleWidgets(final WidgetInstance parentInstance, final String slotId)
    {
        final List<Widget> allChildren = getChildrenForSlot(parentInstance.getWidget(), slotId);
        if(allChildren.isEmpty())
        {
            return Collections.<Widget>emptyList();
        }
        else
        {
            final List<Widget> result = new ArrayList<Widget>();
            for(final Widget child : allChildren)
            {
                if(canCreateInstance(child, parentInstance))
                {
                    result.add(child);
                }
            }
            return result;
        }
    }


    private List<Widget> getChildrenForSlot(final Widget parentWidget, final String slotId)
    {
        final List<Widget> allChildWidgets = parentWidget.getChildren();
        final List<Widget> childWidgets = new ArrayList<Widget>();
        for(final Widget childWidget : allChildWidgets)
        {
            if((StringUtils.isBlank(slotId) && StringUtils.isBlank(childWidget.getSlotId()))
                            || (slotId != null && slotId.equals(childWidget.getSlotId())))
            {
                childWidgets.add(childWidget);
            }
        }
        if(getWidgetIndexComparator() != null)
        {
            Collections.sort(childWidgets, getWidgetIndexComparator());
        }
        return childWidgets;
    }


    private boolean isAutoCreateInstance(final Widget widget, final WidgetInstance parentInstance)
    {
        if(widget.isTemplate())
        {
            final WidgetInstanceSettings settings = widget.getWidgetInstanceSettings();
            return settings != null && settings.isCreateOnInit();
        }
        else
        {
            return true;
        }
    }


    private boolean shouldCreateInstance(final Widget widget, final WidgetInstance parentInstance)
    {
        return isAutoCreateInstance(widget, parentInstance) && canCreateInstance(widget, parentInstance);
    }


    public WidgetInstanceService getWidgetInstanceService()
    {
        return widgetInstanceService;
    }


    @Required
    public void setWidgetInstanceService(final WidgetInstanceService widgetInstanceService)
    {
        this.widgetInstanceService = widgetInstanceService;
    }


    public Comparator<Widget> getWidgetIndexComparator()
    {
        return widgetIndexComparator;
    }


    public void setWidgetIndexComparator(final Comparator<Widget> widgetIndexComparator)
    {
        this.widgetIndexComparator = widgetIndexComparator;
    }
}
