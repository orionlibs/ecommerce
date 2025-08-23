package de.hybris.platform.cmscockpit.services.config.impl;

import de.hybris.platform.cmscockpit.services.config.ContentEditorConfiguration;
import de.hybris.platform.cmscockpit.services.config.jaxb.contenteditor.ContentEditor;
import de.hybris.platform.cmscockpit.services.config.jaxb.contenteditor.EditorList;
import de.hybris.platform.cmscockpit.services.config.jaxb.contenteditor.Parameter;
import de.hybris.platform.cmscockpit.services.config.jaxb.contenteditor.Property;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.impl.JAXBBasedUIComponentConfigurationFactory;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;

public class ContentEditorConfigurationFactory extends JAXBBasedUIComponentConfigurationFactory<ContentEditorConfiguration, ContentEditor>
{
    private static final Logger LOG = Logger.getLogger(ContentEditorConfigurationFactory.class);


    protected ContentEditorConfiguration createUIComponent(ObjectTemplate objectTemplate, ObjectTemplate originalObjectTemplate, ContentEditor xmlContentEditor)
    {
        DefaultContentEditorConfiguration defaultContentEditorConfiguration;
        ContentEditorConfiguration conf = null;
        if(validateType(objectTemplate))
        {
            String template = xmlContentEditor.getTemplate();
            Map<String, String> editorMap = createEditorMap(xmlContentEditor);
            Map<String, Boolean> visibilityMap = createVisibilityMap(xmlContentEditor);
            Map<String, Map<String, String>> paramMap = createParameterMap(xmlContentEditor);
            defaultContentEditorConfiguration = new DefaultContentEditorConfiguration(template, editorMap, visibilityMap, paramMap, xmlContentEditor.isHideReadOnly(), xmlContentEditor.isHideEmpty(), xmlContentEditor.isGroupCollections());
        }
        return (ContentEditorConfiguration)defaultContentEditorConfiguration;
    }


    public UIComponentConfiguration createDefault(ObjectTemplate objectTemplate)
    {
        DefaultContentEditorConfiguration defConf = null;
        if(validateType(objectTemplate))
        {
            defConf = new DefaultContentEditorConfiguration(getDefaultTemplate());
            defConf.setHideEmpty(true);
            defConf.setHideReadOnly(true);
            defConf.setGroupCollections(true);
            defConf.setDefaultConfiguration(true);
        }
        return (UIComponentConfiguration)defConf;
    }


    public Class getComponentClass()
    {
        return ContentEditorConfiguration.class;
    }


    protected Map<String, String> createEditorMap(ContentEditor xmlContentEditor)
    {
        Map<String, String> editorMap = null;
        if(xmlContentEditor != null)
        {
            editorMap = new HashMap<>();
            EditorList customEditors = xmlContentEditor.getCustomEditors();
            if(customEditors != null)
            {
                List<Property> properties = customEditors.getProperty();
                if(properties != null)
                {
                    for(Property prop : properties)
                    {
                        if(prop != null)
                        {
                            editorMap.put(prop.getQualifier().toLowerCase(UISessionUtils.getCurrentSession().getLocale()), prop
                                            .getEditorCode());
                        }
                    }
                }
            }
        }
        else
        {
            LOG.warn("No content editor object available.");
        }
        return editorMap;
    }


    protected Map<String, Boolean> createVisibilityMap(ContentEditor xmlContentEditor)
    {
        Map<String, Boolean> visMap = null;
        if(xmlContentEditor != null)
        {
            visMap = new HashMap<>();
            EditorList customEditors = xmlContentEditor.getCustomEditors();
            if(customEditors != null)
            {
                List<Property> properties = customEditors.getProperty();
                if(properties != null)
                {
                    for(Property prop : properties)
                    {
                        if(prop != null)
                        {
                            visMap.put(prop.getQualifier().toLowerCase(UISessionUtils.getCurrentSession().getLocale()), prop.isVisible());
                        }
                    }
                }
            }
        }
        else
        {
            LOG.warn("No content editor object available.");
        }
        return visMap;
    }


    protected Map<String, Map<String, String>> createParameterMap(ContentEditor xmlContentEditor)
    {
        Map<String, Map<String, String>> paramMap = null;
        if(xmlContentEditor != null)
        {
            paramMap = new HashMap<>();
            EditorList editorList = xmlContentEditor.getCustomEditors();
            if(editorList != null)
            {
                List<Property> editorEntries = editorList.getProperty();
                if(editorEntries != null)
                {
                    Locale locale = UISessionUtils.getCurrentSession().getLocale();
                    for(Property entry : editorEntries)
                    {
                        if(entry != null)
                        {
                            List<Parameter> parameters = entry.getParameter();
                            if(parameters != null && !parameters.isEmpty())
                            {
                                Map<String, String> editorParams = new HashMap<>();
                                paramMap.put(entry.getQualifier().toLowerCase(locale), editorParams);
                                for(Parameter param : parameters)
                                {
                                    editorParams.put(param.getName(), param.getValue());
                                }
                            }
                        }
                    }
                }
            }
        }
        else
        {
            LOG.warn("Could not create parameter map. Reason: No content editor object available.");
        }
        return paramMap;
    }


    protected String getDefaultTemplate()
    {
        StringBuilder templateStrBuilder = new StringBuilder();
        templateStrBuilder.append("<table width=\"100%\" class=\"structViewCnt\" style=\"margin:0;padding:0\">\n");
        templateStrBuilder.append("<tbody>\n");
        templateStrBuilder.append("<tr><td><div style=\"height:6px\"/></td></tr>\n");
        templateStrBuilder.append("<tr><td><cockpit code=\"general\"/></td></tr>\n");
        templateStrBuilder.append("<tr><td><div style=\"height:3px\"/></td></tr>\n");
        templateStrBuilder.append("<tr><td><cockpit code=\"list\"/></td></tr>\n");
        templateStrBuilder.append("</tbody>\n");
        templateStrBuilder.append("</table>\n");
        return templateStrBuilder.toString();
    }


    protected boolean validateType(ObjectTemplate type)
    {
        boolean isOK = false;
        try
        {
            if(type == null)
            {
                LOG.warn("Can not create default configuration. Reason: No Object template specified.");
            }
            else
            {
                isOK = true;
            }
        }
        catch(Exception e)
        {
            LOG.error("Could not validate type.", e);
            isOK = false;
        }
        return isOK;
    }
}
