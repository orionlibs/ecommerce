package de.hybris.platform.cmscockpit.services.config.impl;

import de.hybris.platform.cmscockpit.services.config.ContentEditorConfiguration;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultContentEditorConfiguration implements ContentEditorConfiguration
{
    private String template = null;
    private final Map<String, String> editorCodeMap = new HashMap<>();
    private final Map<String, Boolean> editorVisibilityMap = new HashMap<>();
    private final Map<String, Map<String, String>> params = new HashMap<>();
    private boolean groupCollections = true;
    private boolean hideEmpty = true;
    private boolean hideReadOnly = true;
    private boolean defaultConfiguration = false;


    public DefaultContentEditorConfiguration()
    {
        this(null);
    }


    public DefaultContentEditorConfiguration(String template)
    {
        this(template, null);
    }


    public DefaultContentEditorConfiguration(String template, Map<String, String> editorMap)
    {
        this(template, editorMap, null, null);
    }


    public DefaultContentEditorConfiguration(String template, Map<String, String> editorMap, Map<String, Boolean> visibilityMap, Map<String, Map<String, String>> params)
    {
        this(template, editorMap, visibilityMap, params, true, true, true);
    }


    public DefaultContentEditorConfiguration(String template, Map<String, String> editorMap, Map<String, Boolean> visibilityMap, Map<String, Map<String, String>> params, boolean hideReadOnly, boolean hideEmpty, boolean groupCollections)
    {
        this.template = template;
        if(editorMap != null && !editorMap.isEmpty())
        {
            this.editorCodeMap.putAll(editorMap);
        }
        if(visibilityMap != null && !visibilityMap.isEmpty())
        {
            this.editorVisibilityMap.putAll(visibilityMap);
        }
        if(params != null && !params.isEmpty())
        {
            this.params.putAll(params);
        }
        this.hideReadOnly = hideReadOnly;
        this.hideEmpty = hideEmpty;
        this.groupCollections = groupCollections;
    }


    public String getCockpitTemplate()
    {
        return this.template;
    }


    public void setCockpitTemplate(String template)
    {
        this.template = template;
    }


    public String getEditorCode(String propertyQualifier)
    {
        return this.editorCodeMap.get(propertyQualifier.toLowerCase(UISessionUtils.getCurrentSession().getLocale()));
    }


    public boolean isEditorVisible(String propertyQualifier)
    {
        Boolean visible = this.editorVisibilityMap.get(propertyQualifier.toLowerCase(UISessionUtils.getCurrentSession()
                        .getLocale()));
        return (visible == null) ? true : visible.booleanValue();
    }


    public Map<String, String> getEditorMap()
    {
        return Collections.unmodifiableMap(this.editorCodeMap);
    }


    public Map<String, String> getParameterMap(String propertyQualifier)
    {
        Map<String, String> paramMap = null;
        if(this.params != null && !this.params.isEmpty())
        {
            paramMap = this.params.get(propertyQualifier.toLowerCase(UISessionUtils.getCurrentSession().getLocale()));
        }
        return (paramMap == null) ? Collections.EMPTY_MAP : paramMap;
    }


    public boolean isGroupCollections()
    {
        return this.groupCollections;
    }


    public void setGroupCollections(boolean groupCollections)
    {
        this.groupCollections = groupCollections;
    }


    public boolean isHideEmpty()
    {
        return this.hideEmpty;
    }


    public void setHideEmpty(boolean hideEmpty)
    {
        this.hideEmpty = hideEmpty;
    }


    public boolean isHideReadOnly()
    {
        return this.hideReadOnly;
    }


    public void setHideReadOnly(boolean hideReadOnly)
    {
        this.hideReadOnly = hideReadOnly;
    }


    public void setDefaultConfiguration(boolean defaultConfiguration)
    {
        this.defaultConfiguration = defaultConfiguration;
    }


    public boolean isDefaultConfiguration()
    {
        return this.defaultConfiguration;
    }
}
