/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.instant;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editor.instant.labelprovider.InstantEditorLabelProvider;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.OrderComparator;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;

public class DefaultInstantEditorLabelRenderer extends AbstractInstantEditorLabelRenderer
{
    /**
     * editor-parameter which can be used to force usage of given Spring bean as a label provider
     */
    public static final String PARAM_LABEL_PROVIDER_BEAN = "labelProviderBeanId";
    /**
     * editor-parameter which can be used to change list of label providers to be used
     */
    public static final String PARAM_LABEL_PROVIDERS_LIST = "labelProvidersList";
    private List<InstantEditorLabelProvider> instantEditorLabelProviders;


    @Override
    public void render(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final List<InstantEditorLabelProvider> providers = resolveLabelProvidersList(
                        context.getParameterAs(PARAM_LABEL_PROVIDERS_LIST));
        final String labelProviderBeanId = context.getParameterAs(PARAM_LABEL_PROVIDER_BEAN);
        final String editorType = getEditorType(context);
        final InstantEditorLabelProvider labelProvider = resolveLabelProvider(providers, editorType, labelProviderBeanId)
                        .orElseThrow(() -> createNoLabelProviderFoundException(editorType, labelProviderBeanId));
        createValueLabel(parent, context, labelProvider);
    }


    protected String getEditorType(final EditorContext<Object> context)
    {
        return context.getValueType();
    }


    protected List<InstantEditorLabelProvider> resolveLabelProvidersList(final String labelProvidersBeanId)
    {
        if(StringUtils.isNotBlank(labelProvidersBeanId))
        {
            return (List<InstantEditorLabelProvider>)SpringUtil.getBean(labelProvidersBeanId);
        }
        else
        {
            return getInstantEditorLabelProviders();
        }
    }


    protected Optional<InstantEditorLabelProvider> resolveLabelProvider(final List<InstantEditorLabelProvider> providers,
                    final String editorType, final String labelProviderBeanId)
    {
        if(StringUtils.isNotBlank(labelProviderBeanId))
        {
            return Optional.ofNullable((InstantEditorLabelProvider)SpringUtil.getBean(labelProviderBeanId));
        }
        return providers.stream().filter(provider -> provider.canHandle(editorType)).findFirst();
    }


    protected IllegalStateException createNoLabelProviderFoundException(final String editorType, final String labelProviderBeanId)
    {
        final StringBuilder messageBuilder = new StringBuilder("No label provider found for ");
        if(StringUtils.isNotBlank(labelProviderBeanId))
        {
            messageBuilder.append("bean id ").append(labelProviderBeanId);
        }
        else
        {
            messageBuilder.append("editor type ").append(editorType);
        }
        return new IllegalStateException(messageBuilder.toString());
    }


    protected List<InstantEditorLabelProvider> getInstantEditorLabelProviders()
    {
        return instantEditorLabelProviders;
    }


    @Required
    public void setInstantEditorLabelProviders(final List<InstantEditorLabelProvider> instantEditorLabelProviders)
    {
        this.instantEditorLabelProviders = instantEditorLabelProviders;
        OrderComparator.sortIfNecessary(this.instantEditorLabelProviders);
    }
}
