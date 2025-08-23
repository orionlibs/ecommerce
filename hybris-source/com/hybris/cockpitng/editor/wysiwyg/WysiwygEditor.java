/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.wysiwyg;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.Focusable;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibConstants;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editor.localized.AbstractLocalizedEditor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.type.ObjectValueService;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Resource;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkforge.ckez.CKeditor;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;

/**
 * WYSIWYG editor using {@link CKeditor} component.
 */
public class WysiwygEditor extends AbstractCockpitEditorRenderer<String>
{
    public static final String CONFIG_PARAM_PATH = "editorPath";
    /**
     * Base64 encoding parameter name
     */
    static final String BASE64_ENCODED_PARAM = "base64Encoded";
    static final String BASE64_ENCODED_CLIENT_ATTRIBUTE = "base64encoded";
    private static final String DEFAULT_JS_URI_CONFIG_KEY = "jsConfigUriKey";
    private static final String TOOLBAR_CONFIG = "toolbar";
    private static final String CONTAINER_SCLASS = "ye-wysiwyg";
    private static final String EDITOR_PATH = String.format("/%s/widgets/editors/wysiwygEditor/", WidgetLibConstants.CLASSPATH_RESOURCES_PATH_PREFIX);
    private static final String FALLBACK_JS_CONFIG_URI = EDITOR_PATH + "ckeditorconfig.js";
    private static final Logger LOG = LoggerFactory.getLogger(WysiwygEditor.class);
    @Resource
    private CockpitProperties cockpitProperties;
    @Resource
    private ObjectValueService objectValueService;


    @Override
    public void render(final Component parent, final EditorContext<String> context, final EditorListener<String> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final Div editorContainer = new Div();
        editorContainer.setSclass(CONTAINER_SCLASS);
        final Map<String, Object> config = new HashMap<>();
        if(!context.isEditable())
        {
            config.put("readOnly", true);
        }
        markEditorWithQualifier(context, config);
        final CKeditor cKeditor = new EditorFocusableContainer();
        cKeditor.setConfig(config);
        initAdditionalParameters(cKeditor, context);
        final String customToolbarConfig = getCustomToolbarConfig(context);
        if(StringUtils.isNotEmpty(customToolbarConfig))
        {
            cKeditor.setToolbar(customToolbarConfig);
        }
        cKeditor.setValue(context.getInitialValue());
        final String jsConfigFile = getDefaultJsConfigURI(context);
        if(StringUtils.isNotEmpty(jsConfigFile))
        {
            cKeditor.setCustomConfigurationsPath(jsConfigFile);
            LOG.debug("Default ckeditor config : [ {} ] for editor [ {} ]", jsConfigFile, context.getCode());
        }
        cKeditor.addEventListener(Events.ON_CHANGE, event -> onChangeEvent(context, listener, cKeditor));
        cKeditor.setId(Editor.DEFAULT_FOCUS_COMPONENT_ID);
        cKeditor.setParent(editorContainer);
        editorContainer.setParent(parent);
    }


    protected void initAdditionalParameters(final CKeditor editorView, final EditorContext<String> context)
    {
        editorView.setClientAttribute(BASE64_ENCODED_CLIENT_ATTRIBUTE, BooleanUtils.toStringTrueFalse(isBase64EncodingEnabled(context)));
    }


    protected boolean isBase64EncodingEnabled(final EditorContext<String> context)
    {
        return context.getParameterAsBoolean(BASE64_ENCODED_PARAM, false);
    }


    private static void markEditorWithQualifier(final EditorContext<String> context, final Map<String, Object> config)
    {
        final ObjectValuePath fullPath = ObjectValuePath.parse(context.getParameterAs(Editor.EDITOR_PROPERTY));
        final ObjectValuePath prefix = ObjectValuePath.parse(context.getParameterAs(Editor.MODEL_PREFIX));
        final ObjectValuePath qualifier = fullPath.getRelative(prefix);
        final Locale locale = context.getParameterAs(AbstractLocalizedEditor.LOCALIZED_EDITOR_LOCALE);
        final ObjectValuePath path = qualifier.localize(Objects.toString(locale));
        config.put(CONFIG_PARAM_PATH, path.buildPath());
    }


    /**
     * Resolves default configuration js file URI based on the cockpit property specified as 'defaultJsConfig' editor
     * setting in the editor definition.
     *
     * @param context
     *           editor context
     * @return file URI defined as cockpit property key 'defaultJsConfig' or empty string of no default configuration was
     *         specified for wysiwyg editor.
     */
    protected String getDefaultJsConfigURI(final EditorContext<String> context)
    {
        String result = null;
        final String jsConfigURIKey = Objects.toString(context.getParameter(DEFAULT_JS_URI_CONFIG_KEY));
        if(StringUtils.isNotEmpty(jsConfigURIKey))
        {
            result = cockpitProperties.getProperty(jsConfigURIKey);
        }
        return StringUtils.isEmpty(result) ? FALLBACK_JS_CONFIG_URI : result;
    }


    /**
     * Resolves custom toolbar configuration based on custom-attribute 'toolbar' specified for given editor instance.
     *
     * @param context
     *           editor context
     * @return file URI defined as 'customJsConfig' attribute or empty string if no custom config was specified.
     */
    protected String getCustomToolbarConfig(final EditorContext<String> context)
    {
        return Optional.ofNullable(context.getParameter(TOOLBAR_CONFIG)).orElse("").toString();
    }


    protected void onChangeEvent(final EditorContext<String> context, final EditorListener<String> listener, final CKeditor cKeditor)
    {
        final String editorValue = getEditorValue(cKeditor);

        /*
         * Fix ECP issue about Language fallback does not work after removing the text in WYSIWYG editor. https://cxjira.sap.com/browse/ECP-5369
         * Because the language fallback only works in case the value is null.
         */
        final boolean isEmpty = StringUtils.isEmpty(editorValue);
        if(isBase64EncodingEnabled(context))
        {
            listener.onValueChanged(isEmpty ? null : new String(Base64.getDecoder().decode(editorValue), StandardCharsets.UTF_8));
        }
        else
        {
            listener.onValueChanged(isEmpty ? null : editorValue);
        }
    }


    private static String getEditorValue(final CKeditor cKeditor)
    {
        return cKeditor.getValue();
    }


    public CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    public ObjectValueService getObjectValueService()
    {
        return objectValueService;
    }


    private static class EditorFocusableContainer extends CKeditor implements Focusable
    {
        @Override
        public void focus()
        {
            final String code = String.format("setTimeout(function(){ var editor = CKEDITOR.instances['%s-cnt']; "
                            + "if(editor !== undefined) { editor.focus(); }},700);", this.getUuid());
            Clients.evalJavaScript(code);
        }
    }
}
