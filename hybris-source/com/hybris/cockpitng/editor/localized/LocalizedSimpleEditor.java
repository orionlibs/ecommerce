/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.localized;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.i18n.LocalizedValuesService;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;

public class LocalizedSimpleEditor extends AbstractLocalizedEditor
{
    public static final String YW_ADVANCED_SEARCH_LOCAL = "yw-advancedsearch-local";
    public static final String YW_ADVANCED_SEARCH_LOCAL_EDITOR = "yw-advancedsearch-local-editor";
    public static final String YE_AS_LANG_SELECTOR_SCLASS = "ye-as-lang-selector";
    public static final String INLINE_PARAM = "isInlineEditor";
    private LocalizedValuesService localizedValuesService;


    @Override
    public void render(final Component parent, final EditorContext editorContext, final EditorListener editorListener)
    {
        Validate.notNull("All parameters are mandatory", parent, editorContext, editorListener);
        final Map<Locale, Object> initialValue = new HashMap<>();
        if(editorContext.getInitialValue() != null)
        {
            initialValue.putAll((Map<Locale, Object>)editorContext.getInitialValue());
        }
        if(parent instanceof Editor)
        {
            setWidgetInstanceManager(((Editor)parent).getWidgetInstanceManager());
        }
        final Locale currentLocale = getLocalizedValuesService().getSelectedLocaleOrDefault(initialValue);
        final Object currentValue = getLocalizedValuesService().getCurrentValue(currentLocale, initialValue);
        final Div editorLabel = createEditorLabel(editorContext);
        parent.appendChild(editorLabel);
        final Div langContainer = new Div();
        langContainer.setSclass(YW_ADVANCED_SEARCH_LOCAL);
        final Combobox langSelector = createLangSelector(langContainer, currentLocale, editorContext);
        langContainer.setParent(parent);
        langSelector.addEventListener(Events.ON_OK, event -> editorListener.onEditorEvent(Events.ON_OK));
        final Div editorContainer = new Div();
        editorContainer.setParent(parent);
        editorContainer.setSclass(YW_ADVANCED_SEARCH_LOCAL_EDITOR);
        final Editor editor = prepareSubEditor(editorContext, currentValue);
        editor.addEventListener(Editor.ON_VALUE_CHANGED, event -> {
            initialValue.put(getSelectedLocale(langSelector), event.getData());
            editorListener.onValueChanged(initialValue);
        });
        editor.addEventListener(Editor.ON_EDITOR_EVENT, event -> editorListener.onEditorEvent((String)event.getData()));
        langSelector.addEventListener(Events.ON_SELECT,
                        event -> langSelectorEventHandler(parent, editorContext, editorListener, initialValue, langSelector, editor));
        editor.afterCompose();
        editorContainer.appendChild(editor);
    }


    private void langSelectorEventHandler(final Component parent, final EditorContext editorContext,
                    final EditorListener editorListener, final Map<Locale, Object> initialValue, final Combobox langSelector,
                    final Editor editor)
    {
        final Object value;
        final Locale selectedLocale = getSelectedLocale(langSelector);
        if(editorContext.getParameters().containsKey(INLINE_PARAM)
                        && editorContext.getParameter(INLINE_PARAM).equals(Boolean.TRUE))
        {
            value = initialValue.get(selectedLocale);
        }
        else
        {
            value = initialValue.values().stream().filter(Objects::nonNull).findFirst().orElse(null);
            initialValue.clear();
        }
        initialValue.put(selectedLocale, value);
        editorListener.onValueChanged(initialValue);
        editor.setValue(getLocalizedValuesService().getCurrentValue(selectedLocale, initialValue));
        Events.postEvent(Events.ON_SELECT, parent, selectedLocale);
    }


    private List<Locale> getEffectiveLocales(final EditorContext editorContext)
    {
        final List<Locale> effectiveLocales = Lists.newArrayList(getActiveLocales());
        effectiveLocales.retainAll(ObjectUtils.defaultIfNull(editorContext.getReadableLocales(), Collections.emptySet()));
        return effectiveLocales;
    }


    private Combobox createLangSelector(final Div langContainer, final Locale selectedLocale, final EditorContext editorContext)
    {
        final Combobox langSelector = new Combobox();
        langSelector.setReadonly(true);
        langSelector.setParent(langContainer);
        final ListModelList<Locale> listModel = new ListModelList<>();
        listModel.addAll(getEffectiveLocales(editorContext));
        if(selectedLocale != null)
        {
            listModel.setSelection(Collections.singletonList(selectedLocale));
        }
        langSelector.setModel(listModel);
        langSelector.setSclass(YE_AS_LANG_SELECTOR_SCLASS);
        langSelector.setItemRenderer(new ComboitemRenderer<Locale>()
        {
            @Override
            public void render(final Comboitem item, final Locale locale, final int index)
            {
                item.setLabel(getLocalizedValuesService().getLanguageLabelKey(locale));
                item.setValue(locale);
            }
        });
        return langSelector;
    }


    private Locale getSelectedLocale(final Combobox langSelector)
    {
        final ListModelList<Locale> langSelectorModel = (ListModelList)langSelector.getModel();
        if(langSelectorModel != null && CollectionUtils.isNotEmpty(langSelectorModel.getSelection()))
        {
            return langSelectorModel.getSelection().iterator().next();
        }
        return null;
    }


    public LocalizedValuesService getLocalizedValuesService()
    {
        if(localizedValuesService == null)
        {
            localizedValuesService = (LocalizedValuesService)SpringUtil.getBean("localizedValuesService");
        }
        return localizedValuesService;
    }


    public void setLocalizedValuesService(final LocalizedValuesService localizedValuesService)
    {
        this.localizedValuesService = localizedValuesService;
    }
}
