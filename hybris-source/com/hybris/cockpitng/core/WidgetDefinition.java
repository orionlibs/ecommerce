/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

import com.hybris.cockpitng.core.impl.DefaultCockpitComponentDefinitionService;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Widget definition. Represents a widget type. Managed by component registry.
 */
public class WidgetDefinition extends AbstractCockpitComponentDefinition
{
    private String defaultTitle;
    private String controller;
    private String controllerID;
    private String viewURI;
    private Widget composedWidgetRoot;
    private boolean stubWidget;
    private List<WidgetSocket> inputs;
    private List<WidgetSocket> outputs;
    private Map<String, String> forwardMap;


    public String getDefaultTitle()
    {
        return defaultTitle;
    }


    public String getViewURI()
    {
        return viewURI;
    }


    public boolean hasView()
    {
        return !DefaultCockpitComponentDefinitionService.NO_VIEW_ZUL.equals(getViewURI());
    }


    public void setDefaultTitle(final String defaultTitle)
    {
        this.defaultTitle = defaultTitle;
    }


    public void setViewURI(final String viewURI)
    {
        this.viewURI = viewURI;
    }


    public void setComposedWidgetRoot(final Widget composedWidgetRoot)
    {
        this.composedWidgetRoot = composedWidgetRoot;
    }


    @Override
    public void setInputs(final List<WidgetSocket> inputs)
    {
        this.inputs = inputs;
    }


    @Override
    public void setOutputs(final List<WidgetSocket> outputs)
    {
        this.outputs = outputs;
    }


    @Override
    public List<WidgetSocket> getInputs()
    {
        return inputs == null ? Collections.emptyList() : Collections.unmodifiableList(inputs);
    }


    @Override
    public List<WidgetSocket> getOutputs()
    {
        return outputs == null ? Collections.emptyList() : Collections.unmodifiableList(outputs);
    }


    public Widget getComposedWidgetRoot()
    {
        return composedWidgetRoot;
    }


    public String getController()
    {
        return controller;
    }


    public void setController(final String controller)
    {
        this.controller = controller;
    }


    @Override
    public void setForwardMap(final Map<String, String> forwardMap)
    {
        this.forwardMap = forwardMap;
    }


    @Override
    public Map<String, String> getForwardMap()
    {
        return forwardMap == null ? Collections.emptyMap() : forwardMap;
    }


    public String getControllerID()
    {
        return controllerID;
    }


    public void setControllerID(final String controllerID)
    {
        this.controllerID = controllerID;
    }


    /**
     *
     * @deprecated since 1811, use {@link #getParentCode()} instead
     */
    @Deprecated(since = "1811", forRemoval = true)
    public String getParentDefinitionID()
    {
        return getParentCode();
    }


    /**
     *
     * @deprecated since 1811, use {@link #setParentCode(String)} instead
     */
    @Deprecated(since = "1811", forRemoval = true)
    public void setParentDefinitionID(final String parentDefinitionID)
    {
        setParentCode(parentDefinitionID);
    }


    public boolean isStubWidget()
    {
        return stubWidget;
    }


    public void setStubWidget(final boolean stubWidget)
    {
        this.stubWidget = stubWidget;
    }
}
