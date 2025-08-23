/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.instant.labelprovider.impl;

import com.hybris.cockpitng.editor.instant.labelprovider.InstantEditorLabelProvider;
import com.hybris.cockpitng.editors.EditorUtils;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import org.springframework.beans.factory.annotation.Autowired;

public class LocalizedInstantEditorLabelProvider extends AbstractInstantEditorLabelProvider
{
    private CockpitLocaleService cockpitLocaleService;
    private List<InstantEditorLabelProvider> instantEditorLabelProviders;


    @Override
    public boolean canHandle(final String editorType)
    {
        return EditorUtils.getLocalizedEditorPattern().matcher(editorType).matches();
    }


    @Override
    public String getLabel(final String editorType, final Object value)
    {
        final Object localValue = extractValueForLocale((Map<Locale, Object>)value, getCockpitLocaleService().getCurrentLocale());
        final Matcher matcher = EditorUtils.getLocalizedEditorPattern().matcher(editorType);
        if(matcher.matches())
        {
            final String nestedEditorType = matcher.group(1);
            final InstantEditorLabelProvider labelProvider = findLabelProvider(nestedEditorType).orElseThrow(
                            () -> new IllegalStateException("No label provider found for nested editor type " + nestedEditorType));
            return labelProvider.getLabel(nestedEditorType, localValue);
        }
        return Objects.toString(localValue);
    }


    protected Object extractValueForLocale(final Map<Locale, Object> values, final Locale locale)
    {
        if(values != null)
        {
            return values.get(locale);
        }
        return null;
    }


    protected Optional<InstantEditorLabelProvider> findLabelProvider(final String editorType)
    {
        return getInstantEditorLabelProviders().stream().filter(provider -> provider.canHandle(editorType)).findFirst();
    }


    protected CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    protected List<InstantEditorLabelProvider> getInstantEditorLabelProviders()
    {
        return instantEditorLabelProviders;
    }


    @Autowired
    public void setInstantEditorLabelProviders(final List<InstantEditorLabelProvider> instantEditorLabelProviders)
    {
        this.instantEditorLabelProviders = instantEditorLabelProviders;
    }
}
