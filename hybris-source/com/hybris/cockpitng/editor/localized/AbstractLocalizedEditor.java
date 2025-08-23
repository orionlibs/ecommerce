/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.localized;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorRegistry;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.WidgetInstanceManagerAware;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.UITools;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public abstract class AbstractLocalizedEditor extends AbstractCockpitEditorRenderer<Object> implements WidgetInstanceManagerAware
{
    public static final String LOCALIZED_EDITOR_LOCALE = "localizedEditor.locale";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractLocalizedEditor.class);
    public static final String YW_LOCEDITOR_CAPTION = "yw-loceditor-caption";
    public static final String SCLASS_CELL_LABEL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label";
    public static final String SCLASS_MANDATORY_ATTRIBUTE_LABEL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-label-mandatory-attribute";
    private CockpitLocaleService cockpitLocaleService;
    private CockpitUserService cockpitUserService;
    private WidgetInstanceManager widgetInstanceManager;
    private LabelService labelService;
    private CockpitComponentDefinitionService componentDefinitionService;
    private EditorRegistry editorRegistry;


    protected Div createEditorLabel(final EditorContext editorContext)
    {
        final Div wrapperCaption = new Div();
        wrapperCaption.setSclass(YW_LOCEDITOR_CAPTION);
        UITools.modifySClass(wrapperCaption, YW_EDITOR_AREA_LABEL_CONTAINER, true);
        if(StringUtils.isNotBlank(editorContext.getEditorLabel()))
        {
            final Label label = new Label(editorContext.getEditorLabel());
            label.setParent(wrapperCaption);
            if(editorContext.isOptional())
            {
                UITools.modifySClass(label, SCLASS_CELL_LABEL, true);
            }
            else
            {
                UITools.modifySClass(label, SCLASS_MANDATORY_ATTRIBUTE_LABEL, true);
            }
            final Object tooltip = editorContext.getParameter(HEADER_LABEL_TOOLTIP);
            if(tooltip instanceof String && StringUtils.isNotBlank((String)tooltip))
            {
                label.setTooltiptext((String)tooltip);
            }
        }
        return wrapperCaption;
    }


    /**
     * @param editorContext
     *           context
     * @param currentValue
     *           editors's value
     * @return the editor
     */
    protected Editor prepareSubEditor(final EditorContext<Object> editorContext, final Object currentValue)
    {
        final Editor subEditor = new Editor();
        subEditor.setReadOnly(!editorContext.isEditable());
        subEditor.setInitialValue(currentValue);
        subEditor.setOptional(editorContext.isOptional());
        subEditor.setWidgetInstanceManager(getWidgetInstanceManager());
        subEditor.setOrdered(editorContext.isOrdered());
        subEditor.setWritableLocales(editorContext.getWritableLocales());
        subEditor.setReadableLocales(editorContext.getReadableLocales());
        final Map<String, Object> parameters = editorContext.getParameters();
        final String defaultEditor = editorContext.getParameterAs(Editor.VALUE_EDITOR);
        if(StringUtils.isNotBlank(defaultEditor))
        {
            final String embeddedEditor = extractEmbeddedEditor(defaultEditor);
            parameters.put(Editor.VALUE_EDITOR, embeddedEditor);
            subEditor.setDefaultEditor(embeddedEditor);
        }
        subEditor.setType(extractEmbeddedType(editorContext));
        subEditor.addParameters(parameters);
        return subEditor;
    }


    /**
     * @param editorContext
     *           context
     * @param currentValue
     *           editors's value
     * @param locale
     *           sub editor's locale
     * @return the editor
     * @since 2.1.0
     */
    protected Editor prepareSubEditor(final EditorContext<Object> editorContext, final Object currentValue, final Locale locale)
    {
        if(editorContext.getParameters().containsKey(LOCALIZED_EDITOR_LOCALE))
        {
            final Object ctxLocale = editorContext.getParameter(LOCALIZED_EDITOR_LOCALE);
            if(!Objects.equals(ctxLocale, locale))
            {
                LOG.warn("Passed locale is different than passed via context: {} vs {}", locale, ctxLocale);
            }
        }
        final Editor subEditor = prepareSubEditor(editorContext, currentValue);
        if(locale == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Passed locale is null. Removing from context parameters passed to sub-editor");
            }
            subEditor.removeParameter(LOCALIZED_EDITOR_LOCALE);
        }
        else
        {
            subEditor.addParameter(LOCALIZED_EDITOR_LOCALE, locale);
        }
        return subEditor;
    }


    protected List<Locale> getActiveLocales()
    {
        final List<Locale> allLocales = new ArrayList<>();
        final String currentUser = getCockpitUserService().getCurrentUser();
        if(StringUtils.isNotBlank(currentUser))
        {
            allLocales.addAll(getCockpitLocaleService().getEnabledDataLocales(currentUser));
        }
        else
        {
            allLocales.addAll(getCockpitLocaleService().getAllLocales());
        }
        return allLocales;
    }


    protected CockpitLocaleService getCockpitLocaleService()
    {
        if(cockpitLocaleService == null)
        {
            cockpitLocaleService = (CockpitLocaleService)SpringUtil.getBean("cockpitLocaleService");
        }
        return cockpitLocaleService;
    }


    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    protected CockpitUserService getCockpitUserService()
    {
        if(cockpitUserService == null)
        {
            cockpitUserService = (CockpitUserService)SpringUtil.getBean("cockpitUserService");
        }
        return cockpitUserService;
    }


    public void setCockpitUserService(final CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }


    public LabelService getLabelService()
    {
        if(labelService == null)
        {
            labelService = (LabelService)SpringUtil.getBean("labelService");
        }
        return labelService;
    }


    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public CockpitComponentDefinitionService getComponentDefinitionService()
    {
        if(componentDefinitionService == null)
        {
            componentDefinitionService = (CockpitComponentDefinitionService)SpringUtil.getBean("componentDefinitionService");
        }
        return componentDefinitionService;
    }


    public void setComponentDefinitionService(final CockpitComponentDefinitionService componentDefinitionService)
    {
        this.componentDefinitionService = componentDefinitionService;
    }


    public EditorRegistry getEditorRegistry()
    {
        if(editorRegistry == null)
        {
            editorRegistry = (EditorRegistry)SpringUtil.getBean("editorRegistry");
        }
        return editorRegistry;
    }


    public void setEditorRegistry(final EditorRegistry editorRegistry)
    {
        this.editorRegistry = editorRegistry;
    }


    public WidgetInstanceManager getWidgetInstanceManager()
    {
        return widgetInstanceManager;
    }


    public void setWidgetInstanceManager(final WidgetInstanceManager widgetInstanceManager)
    {
        this.widgetInstanceManager = widgetInstanceManager;
    }
}
