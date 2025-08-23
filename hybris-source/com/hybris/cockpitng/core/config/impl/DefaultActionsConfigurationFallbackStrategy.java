/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.CockpitConfigurationFallbackStrategy;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Action;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.ActionGroup;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Actions;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

public class DefaultActionsConfigurationFallbackStrategy implements CockpitConfigurationFallbackStrategy<Actions>
{
    public static final String DEFAULT_ACTION_GROUP_QUALIFIER = "common";
    public static final String CREATE_ACTION_ID = "com.hybris.cockpitng.action.create";
    public static final String CREATE_ACTION_PROPERTY = "pageable.typeCode";
    public static final String SELECTED_OBJECTS_PROPERTY = "selectedObjects";
    public static final String DELETE_ACTION_ID = "com.hybris.cockpitng.action.delete";
    public static final String EXPORT_CSV_ACTION_ID = "com.hybris.cockpitng.listview.action.export.csv";
    public static final String EXPORT_COLUMNS_AND_DATA_PROPERTY = "exportColumnsAndData";
    private Set<String> componentCodes;
    private List<Action> actions;


    @Override
    public Actions loadFallbackConfiguration(final ConfigContext context, final Class<Actions> configurationType)
    {
        final String componentCode = resolveRequiredComponentCode(context);
        if(componentCode != null && CollectionUtils.isNotEmpty(componentCodes) && !componentCodes.contains(componentCode))
        {
            return null;
        }
        final Actions fallbackActions = new Actions();
        final ActionGroup group = new ActionGroup();
        fallbackActions.getGroup().add(group);
        group.setQualifier(DEFAULT_ACTION_GROUP_QUALIFIER);
        if(actions == null)
        {
            final Action deleteAction = new Action();
            deleteAction.setActionId(DELETE_ACTION_ID);
            deleteAction.setProperty(SELECTED_OBJECTS_PROPERTY);
            deleteAction.setTriggerOnKeys("#del");
            group.getActions().add(deleteAction);
            final Action createAction = new Action();
            createAction.setActionId(CREATE_ACTION_ID);
            createAction.setProperty(CREATE_ACTION_PROPERTY);
            group.getActions().add(createAction);
            final Action exportCsvAction = new Action();
            exportCsvAction.setActionId(EXPORT_CSV_ACTION_ID);
            exportCsvAction.setProperty(EXPORT_COLUMNS_AND_DATA_PROPERTY);
            group.getActions().add(exportCsvAction);
        }
        else
        {
            group.getActions().addAll(actions);
        }
        return fallbackActions;
    }


    protected String resolveRequiredComponentCode(final ConfigContext context)
    {
        return context.getAttribute(DefaultConfigContext.CONTEXT_COMPONENT);
    }


    /**
     * @return the componentCodes
     */
    public Set<String> getComponentCodes()
    {
        return componentCodes;
    }


    /**
     * @param componentCodes the componentCodes to set
     */
    public void setComponentCodes(final Set<String> componentCodes)
    {
        this.componentCodes = componentCodes;
    }


    /**
     * @return the actions
     */
    public List<Action> getActions()
    {
        return actions;
    }


    /**
     * @param actions the actions to set
     */
    public void setActions(final List<Action> actions)
    {
        this.actions = actions;
    }
}
