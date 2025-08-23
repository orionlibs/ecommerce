/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.persistence.packaging.CockpitResourceLoader;
import com.hybris.cockpitng.core.persistence.packaging.WidgetJarLibInfo;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibConstants;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.core.util.template.TemplateEngine;
import com.hybris.cockpitng.engine.CockpitWidgetEngine;
import com.hybris.cockpitng.json.WidgetJSONMapper;
import com.hybris.cockpitng.util.js.JsWidgetSessionDTO;
import com.hybris.cockpitng.util.js.JsWidgetSessionInfoCollector;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.JavaScriptUtils;
import org.zkoss.json.JSONObject;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Html;

public class HtmlBasedWidgetController extends DefaultWidgetController
{
    private static final long serialVersionUID = -3962980133108849049L;
    private static final Logger LOG = LoggerFactory.getLogger(HtmlBasedWidgetController.class);
    private static final String JSON_CLASS_PROPERTY = "$class";
    private static final String COCKPITNG_PREFIX = "cockpitng";
    private final String viewURI;
    private final transient WidgetDefinition widgetDefinition;
    @Autowired
    private transient JsWidgetSessionInfoCollector jsWidgetSessionInfoCollector;
    @Autowired
    private transient CockpitResourceLoader widgetResourceReader;
    @Autowired
    private transient WidgetLibUtils widgetLibUtils;
    @Autowired
    private transient WidgetJSONMapper jsonMapper;
    @Autowired
    private transient TemplateEngine templateEngine;


    public HtmlBasedWidgetController(final String viewURI, final WidgetDefinition widgetDefinition)
    {
        this.viewURI = viewURI;
        this.widgetDefinition = widgetDefinition;
    }


    public String getViewURI()
    {
        return viewURI;
    }


    protected String getAdaptedViewURI()
    {
        final String viewUri = getViewURI()
                        .replace(widgetDefinition.getCode() + "$", "");
        return addSlashPrefix(viewUri);
    }


    protected String addSlashPrefix(
                    final String path)
    {
        if(!path.startsWith("/"))
        {
            return "/" + path;
        }
        return path;
    }


    protected String removeCockpitngPrefix(
                    final String pathValue)
    {
        String path = pathValue;
        if(path.startsWith("/"))
        {
            path = path.replaceFirst("/", "");
        }
        if(path.startsWith(COCKPITNG_PREFIX))
        {
            path = path.replaceFirst(COCKPITNG_PREFIX, "");
        }
        return path;
    }


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        comp.addEventListener(Events.ON_CREATE, event -> initializeController(comp));
    }


    protected void initializeController(final Component comp)
    {
        final Html widgetContainer = new Html();
        comp.appendChild(widgetContainer);
        final boolean template = getWidgetSettings().getBoolean("isTemplate");
        if(template)
        {
            final String viewUri = WidgetLibConstants.JAR_RESOURCES_PATH_PREFIX + "/" + widgetDefinition.getCode() + getAdaptedViewURI();
            try(final InputStream is = templateEngine.applyTemplate(widgetContainer, viewUri, Collections.emptyMap()))
            {
                widgetContainer.setContent(IOUtils.toString(is));
            }
            catch(final IOException e)
            {
                LOG.error(e.getLocalizedMessage(), e);
            }
        }
        else
        {
            final WidgetJarLibInfo widgetLibInfo = widgetLibUtils.getWidgetJarLibInfo(widgetDefinition.getCode());
            try(final InputStream is = widgetResourceReader.getResourceAsStream(widgetLibInfo, getAdaptedViewURI()))
            {
                widgetContainer.setContent(IOUtils.toString(is));
            }
            catch(final IOException e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        initGeneralProperties(widgetContainer);
        initLocalization(widgetContainer);
        initSocketsSupport(widgetContainer);
        initConfigurationSupport(widgetContainer);
        initModelSupport(widgetContainer);
        storeSessionInfoInJsWidget(widgetContainer);
        initViewScript(widgetContainer);
        setCurrentWidgetId(widgetContainer);
        fireWidgetReady(widgetContainer);
    }


    protected void initViewScript(final Html html)
    {
        final String jsScriptLibPath = getJSScriptLibPath();
        final WidgetJarLibInfo widgetLibInfo = widgetLibUtils.getWidgetJarLibInfo(widgetDefinition.getCode());
        if(widgetResourceReader.hasResource(widgetLibInfo, jsScriptLibPath))
        {
            final String jsScriptWidgetClassPath = getJSScriptWidgetClassPath();
            final String src = String.format("<script type=\"%s\" src=\"%s\"></script>", "text/javascript", jsScriptWidgetClassPath);
            html.setContent(html.getContent() + "\n" + src);
        }
    }


    protected String getJSScriptLibPath()
    {
        final String adaptedViewUri = getAdaptedViewURI();
        final String filePath = FilenameUtils.getPath(adaptedViewUri).isEmpty() ? getLocalFileName(adaptedViewUri) : getFileNameWithPath(adaptedViewUri);
        final String filePathWithSlashPrefix = addSlashPrefix(filePath);
        return filePathWithSlashPrefix;
    }


    protected String getJSScriptWidgetClassPath()
    {
        final String jsScriptLibPath = getJSScriptLibPath();
        final String pathWithoutCockpitngPrefix = removeCockpitngPrefix(jsScriptLibPath);
        final String pathWithResourcesPrefix = WidgetLibConstants.CLASSPATH_RESOURCES_PATH_PREFIX + pathWithoutCockpitngPrefix;
        final String pathNormalized = FilenameUtils.normalize(pathWithResourcesPrefix, true);
        return pathNormalized;
    }


    private static String getLocalFileName(final String view)
    {
        return FilenameUtils.getBaseName(view) + ".js";
    }


    private static String getFileNameWithPath(final String view)
    {
        return FilenameUtils.getPath(view) + getLocalFileName(view);
    }


    protected void setCurrentWidgetId(final Html html)
    {
        html.setContent(String.format(
                        "<script>document.$currentWidget = \"%s\";</script>\n%s\n<script>document.$currentWidget = undefined;</script>",
                        html.getUuid(), html.getContent()));
    }


    protected void initGeneralProperties(final Html html)
    {
        final String uuid = JavaScriptUtils.javaScriptEscape(html.getUuid());
        final String code = getWidgetslot().getWidgetInstance().getWidget().getWidgetDefinitionId();
        appendInitScript(html, "CockpitNG.assignCode(\"" + uuid + "\", \"" + code + "\")");
    }


    protected void storeSessionInfoInJsWidget(final Html html)
    {
        final String uuid = JavaScriptUtils.javaScriptEscape(html.getUuid());
        final JsWidgetSessionDTO sessionInfo = jsWidgetSessionInfoCollector.gatherSessionInfo();
        final String json = JavaScriptUtils.javaScriptEscape(jsonMapper.toJSONString(sessionInfo));
        final String script = "CockpitNG.assignSessionInfo(" + uuid + ", \'" + json + "\');";
        appendInitScript(html, script);
    }


    protected void initSocketsSupport(final Component html)
    {
        // add socket event listeners
        final List<WidgetSocket> outputs = widgetDefinition.getOutputs();
        for(final WidgetSocket widgetSocket : outputs)
        {
            final EventListener<Event> listener = event -> {
                Object data = event.getData();
                if(data instanceof JSONObject)
                {
                    final JSONObject json = (JSONObject)data;
                    if(json.get(JSON_CLASS_PROPERTY) instanceof String)
                    {
                        final String className = (String)json.get(JSON_CLASS_PROPERTY);
                        final Class<?> clazz = ClassUtils.getClass(className);
                        json.remove(JSON_CLASS_PROPERTY);
                        data = jsonMapper.fromJSONString(getWidgetInstanceManager(), json.toJSONString(), clazz);
                    }
                }
                sendOutput(widgetSocket.getId(), data);
            };
            html.addEventListener("onSocketOutput_" + widgetSocket.getId(), listener);
        }
        final List<WidgetSocket> inputs = widgetDefinition.getInputs();
        for(final WidgetSocket widgetSocket : inputs)
        {
            html.getParent().addEventListener("onSocketInput_" + widgetSocket.getId(), event -> {
                String json = jsonMapper.toJSONString(getWidgetInstanceManager(), event.getData());
                final Map<String, Object> map = jsonMapper.fromJSONString(json, Map.class);
                if(map != null)
                {
                    map.put(JSON_CLASS_PROPERTY, event.getData().getClass().getName());
                    json = jsonMapper.toJSONString(map);
                }
                final String uuid = JavaScriptUtils.javaScriptEscape(html.getUuid());
                final String socket = JavaScriptUtils.javaScriptEscape(widgetSocket.getId());
                json = JavaScriptUtils.javaScriptEscape(json);
                Clients.evalJavaScript(
                                String.format("CockpitNG.notifySocketEventHandlers(\"%s\", \"%s\", \"%s\")", uuid, socket, json));
            });
        }
    }


    protected void initModelSupport(final Component html)
    {
        appendInitScript(html, getUpdateModelScript(html));
        html.addEventListener("onModelChange", event -> {
            final JSONObject data = (JSONObject)event.getData();
            final Object key = data.get("key");
            Object value = data.get("value");
            if(value instanceof JSONObject)
            {
                final JSONObject json = (JSONObject)value;
                if(json.get(JSON_CLASS_PROPERTY) instanceof String)
                {
                    final String className = (String)json.get(JSON_CLASS_PROPERTY);
                    final Class<?> clazz = ClassUtils.getClass(className);
                    json.remove(JSON_CLASS_PROPERTY);
                    value = jsonMapper.fromJSONString(getWidgetInstanceManager(), json.toJSONString(), clazz);
                }
            }
            getWidgetInstanceManager().getModel().setValue(Objects.toString(key, ""), value);
        });
        getWidgetInstanceManager().getModel().addObserver(StringUtils.EMPTY,
                        () -> Clients.evalJavaScript(getUpdateModelScript(html)));
    }


    protected String getUpdateModelScript(final Component html)
    {
        final Object model = getWidgetInstanceManager().getWidgetslot().getWidgetInstance().getModel();
        final String uuid = html != null ? ("\"" + JavaScriptUtils.javaScriptEscape(html.getUuid()) + "\"") : "null";
        final String json = JavaScriptUtils.javaScriptEscape(jsonMapper.toJSONString(model));
        return "CockpitNG.assignModel(" + uuid + ", \"" + json + "\");";
    }


    protected String getInitLocalizationScript(final Component html)
    {
        final String uuid = "\"" + JavaScriptUtils.javaScriptEscape(html.getUuid()) + "\"";
        Map<String, Object> labels = (Map<String, Object>)getWidgetslot().getAttribute(CockpitWidgetEngine.LABELS_PARAM);
        final String jsonLocal = JavaScriptUtils.javaScriptEscape(jsonMapper.toJSONString(labels));
        labels = Labels.getSegmentedLabels();
        final String jsonGlobal = JavaScriptUtils.javaScriptEscape(jsonMapper.toJSONString(labels));
        return "CockpitNG.assignLocalization(" + uuid + ", \"" + jsonLocal + "\", \"" + jsonGlobal + "\");";
    }


    protected void appendInitScript(final Component component, final String script)
    {
        if(component instanceof Html)
        {
            final Html html = (Html)component;
            final String src = String.format("<script> %s </script>", script);
            html.setContent(html.getContent() + "\n" + src);
        }
    }


    protected void initLocalization(final Component component)
    {
        appendInitScript(component, getInitLocalizationScript(component));
    }


    protected String getConfigurationSupportScript(final Component component)
    {
        final Object model = getWidgetInstanceManager().getWidgetSettings().getAll();
        final String uuid = component != null ? ("\"" + JavaScriptUtils.javaScriptEscape(component.getUuid()) + "\"") : "null";
        final String json = JavaScriptUtils.javaScriptEscape(jsonMapper.toJSONString(model));
        return "CockpitNG.assignSettings(" + uuid + ", \"" + json + "\");";
    }


    protected void initConfigurationSupport(final Component component)
    {
        appendInitScript(component, getConfigurationSupportScript(component));
    }


    protected void fireWidgetReady(final Component component)
    {
        final String uuid = JavaScriptUtils.javaScriptEscape(component.getUuid());
        Clients.evalJavaScript(" CockpitNG.notifyAdapterReady(\"" + uuid + "\"); ");
    }
}
