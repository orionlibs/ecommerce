/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetConnection;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.impl.WidgetSocketUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Script;

public class JsPlumbSymbolicModeRenderer extends DefaultSymbolicModeRenderer
{
    public static final String PROPERTY_JSPLUMB_DISABLED = "cockpitadmin.symbolic.jsplumb.disabled";
    private static final String CNG_WIDGETS_MAP = "_cng_widgets_map";
    private static final String CNG_CONNECTIONS_LIST = "_cng_connections_list";
    private CockpitProperties cockpitProperties;
    private Boolean jsPlumbEnabled;


    @Override
    public void render(final Widgetslot widgetslot, final AdminmodeWidgetEngine engine)
    {
        if(isJsPlumbEnabled())
        {
            final WidgetInstance widgetInstance = widgetslot.getWidgetInstance();
            if(widgetInstance != null && widgetInstance.getWidget() != null)
            {
                final Widget widget = widgetInstance.getWidget();
                widgetslot.setAttribute(AdminmodeWidgetEngine.LABELS_PARAM,
                                engine.createLabelMap(getWidgetDefinition(widget), widget));
                final boolean isRoot = widget.getParent() == null;
                if(isRoot)
                {
                    getCreateConnectionsList().clear();
                }
                super.render(widgetslot, engine);
                final String mainSlotId = lookupMainSlotId();
                if(mainSlotId.equals(widgetslot.getSlotID()))
                {
                    getCreateWidgetsMap().put(widget.getId(), widgetslot.getLastChild().getUuid());
                }
                else
                {
                    getCreateWidgetsMap().put(widget.getId(), widgetslot.getFirstChild().getUuid());
                }
                if(isRoot)
                {
                    final Script plumbScript = new Script();
                    widgetslot.getParent().appendChild(plumbScript);
                    final StringBuilder snippetBuilder = new StringBuilder();
                    snippetBuilder.append("jsPlumb.setRenderMode(jsPlumb.SVG);jsPlumb.Defaults.Container = $(\"body\");") //
                                    .append("jsPlumb.deleteEveryEndpoint();jsPlumb.detachEveryConnection();") //
                                    .append(" if(!conn_visible) return;\n");
                    final List<WidgetConnection> connectionsList = getCreateConnectionsList();
                    for(final WidgetConnection widgetConnection : connectionsList)
                    {
                        final String sourceID = widgetConnection.getSource().getId();
                        final String targetID = widgetConnection.getTarget().getId();
                        final String sourceCmpId = getCreateWidgetsMap().get(sourceID);
                        final String targetCmpId = getCreateWidgetsMap().get(targetID);
                        if(sourceCmpId != null && targetCmpId != null)
                        {
                            String typeLabel = StringUtils.EMPTY;
                            int sourceIndex = 0;
                            final WidgetDefinition sourceWidgetDefinition = engine.getWidgetDefinition(widgetConnection.getSource());
                            final List<WidgetSocket> outputs = sourceWidgetDefinition.getOutputs();
                            if(outputs != null)
                            {
                                for(final WidgetSocket widgetSocket : outputs)
                                {
                                    if(widgetSocket.getId().equals(widgetConnection.getOutputId()))
                                    {
                                        typeLabel = widgetSocket.getDataType();
                                        break;
                                    }
                                    sourceIndex++;
                                }
                            }
                            int targetIndex = 0;
                            final WidgetDefinition targetWidgetDefinition = engine.getWidgetDefinition(widgetConnection.getTarget());
                            final List<WidgetSocket> inputs = targetWidgetDefinition.getInputs();
                            if(inputs != null)
                            {
                                for(final WidgetSocket widgetSocket : inputs)
                                {
                                    if(widgetSocket.getId().equals(widgetConnection.getInputId()))
                                    {
                                        break;
                                    }
                                    targetIndex++;
                                }
                            }
                            snippetBuilder
                                            .append(createConnectionJSSnippet(sourceCmpId, targetCmpId,
                                                            widgetConnection.getOutputId() + " >>> " + widgetConnection.getInputId() + " (" + typeLabel
                                                                            + ")",
                                                            sourceIndex, targetIndex, sourceWidgetDefinition.isStubWidget(),
                                                            targetWidgetDefinition.isStubWidget()));
                        }
                    }
                    snippetBuilder.insert(0, "function drawConnectionsFunction(uid) { ");
                    snippetBuilder.append("}");
                    plumbScript.setContent(snippetBuilder.toString());
                }
                final Object placeholder = widgetslot.getAttribute(CNG_PLACEHOLDER_CONTENT);
                final AbstractComponent listenerComp = placeholder instanceof AbstractComponent ? (AbstractComponent)placeholder
                                : widgetslot;
                listenerComp.setWidgetListener(Events.ON_CLICK,
                                "selectWidgetConnections(\"" + widgetslot.getFirstChild().getUuid() + "\"); event.stop();");
            }
        }
        else
        {
            super.render(widgetslot, engine);
        }
    }


    @Override
    protected void renderOutputConnectionBars(final Component parent, final Widget currentWidget,
                    final AdminmodeWidgetEngine engine)
    {
        if(isJsPlumbEnabled())
        {
            final Collection<WidgetSocket> outputs = WidgetSocketUtils.getAllOutputs(currentWidget,
                            engine.getWidgetDefinition(currentWidget));
            if(outputs != null)
            {
                for(final WidgetSocket widgetOutputSocket : outputs)
                {
                    final List<WidgetConnection> connectors = engine.getWidgetService()
                                    .getWidgetConnectionsForOutputWidgetAndSocketID(currentWidget, widgetOutputSocket.getId());
                    for(final WidgetConnection cockpitWidgetConnector : connectors)
                    {
                        if(engine.checkVisibility(cockpitWidgetConnector.getTarget()))
                        {
                            getCreateConnectionsList().add(cockpitWidgetConnector);
                        }
                    }
                }
            }
        }
        else
        {
            super.renderOutputConnectionBars(parent, currentWidget, engine);
        }
    }


    private static String createConnectionJSSnippet(final String sourceID, final String targetID, final String label,
                    final int sourceIndex, final int targetIndex, final boolean isSourceStub, final boolean isTargetStub)
    {
        final StringBuilder snippetBuilder = new StringBuilder();
        snippetBuilder.append("if(uid==null || uid == \"").append(sourceID).append("\" || uid == \"").append(targetID)
                        .append("\") createWidgetConnection(\"").append(sourceID).append("\",\"").append(targetID).append("\", \"")
                        .append(label).append("\", ").append(sourceIndex).append(", ").append(targetIndex).append(",uid!=null && uid==\"")
                        .append(targetID).append("\", uid ").append(", ").append(isSourceStub).append(", ").append(isTargetStub)
                        .append(");\n");
        return snippetBuilder.toString();
    }


    private static List<WidgetConnection> getCreateConnectionsList()
    {
        final Object attribute = getDesktop().getAttribute(CNG_CONNECTIONS_LIST);
        if(attribute instanceof List)
        {
            return (List<WidgetConnection>)attribute;
        }
        else
        {
            final List<WidgetConnection> connectionList = new ArrayList<>();
            getDesktop().setAttribute(CNG_CONNECTIONS_LIST, connectionList);
            return connectionList;
        }
    }


    private static Map<String, String> getCreateWidgetsMap()
    {
        final Object attribute = getDesktop().getAttribute(CNG_WIDGETS_MAP);
        if(attribute instanceof Map)
        {
            return (Map<String, String>)attribute;
        }
        else
        {
            final HashMap<String, String> hashMap = new HashMap<>();
            getDesktop().setAttribute(CNG_WIDGETS_MAP, hashMap);
            return hashMap;
        }
    }


    private static Desktop getDesktop()
    {
        return Executions.getCurrent().getDesktop();
    }


    public Boolean isJsPlumbEnabled()
    {
        if(jsPlumbEnabled == null)
        {
            jsPlumbEnabled = Boolean.valueOf(!Boolean.parseBoolean(getCockpitProperties().getProperty(PROPERTY_JSPLUMB_DISABLED)));
        }
        return jsPlumbEnabled;
    }


    protected CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }
}
