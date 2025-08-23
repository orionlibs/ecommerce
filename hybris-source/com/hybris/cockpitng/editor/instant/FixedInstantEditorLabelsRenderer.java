/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.instant;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.expression.ExpressionResolver;
import com.hybris.cockpitng.core.expression.ExpressionResolverFactory;
import com.hybris.cockpitng.editor.instant.labelprovider.InstantEditorLabelProvider;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;

/**
 * A label renderer for Instant Editor with fixed labels depending on value to be rendered.
 * <P>
 * Renderer assumes that there are parameters set in editor context that specifies expressions returning
 * <code>true</code> or <code>false</code> named according to convention: <code>expression_NAME</code>. If some
 * expression returns true then a value of setting <code>label_NAME</code> is returned as label (checking if it is a
 * localized label key).
 * </P>
 */
public class FixedInstantEditorLabelsRenderer extends AbstractInstantEditorLabelRenderer
{
    private static final Pattern PARAMETER_PATTERN_EXPRESSION = Pattern.compile("expression_(.+)");
    private static final String PARAMETER_TEMPLATE_LABEL = "label_%s";
    private static final String PARAMETER_FALLBACK_LABEL = "fallbackLabel";
    private ExpressionResolverFactory expressionResolverFactory;


    @Override
    public void render(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener)
    {
        createValueLabel(parent, context, createLabelProvider(context));
    }


    protected InstantEditorLabelProvider createLabelProvider(final EditorContext<Object> context)
    {
        return new FixedLabelProvider(context);
    }


    protected Optional<String> getMatchingLabelName(final EditorContext<Object> editorContext, final Object value)
    {
        final ExpressionResolver resolver = getExpressionResolverFactory().createResolver();
        return editorContext.getParameters().entrySet().stream()
                        .filter(parameter -> PARAMETER_PATTERN_EXPRESSION.matcher(parameter.getKey()).matches())
                        .filter(parameter -> resolver.getValue(value, Objects.toString(parameter.getValue()))).findFirst()
                        .map(Map.Entry::getKey).map(PARAMETER_PATTERN_EXPRESSION::matcher).filter(Matcher::find).map(matcher -> matcher.group(1));
    }


    protected String getLabel(final EditorContext<Object> editorContext, final String labelName)
    {
        return Optional.ofNullable(labelName).map(name -> String.format(PARAMETER_TEMPLATE_LABEL, name))
                        .map(editorContext::<String>getParameterAs)
                        .map(label -> getLabel(editorContext.<WidgetInstanceManager>getParameterAs(Editor.WIDGET_INSTANCE_MANAGER), label))
                        .orElse(getFallbackLabel(editorContext));
    }


    protected String getLabel(final WidgetInstanceManager wim, final String label)
    {
        return Optional.ofNullable(wim.getLabel(label)).orElse(label);
    }


    protected String getFallbackLabel(final EditorContext<Object> editorContext)
    {
        return Optional.ofNullable(editorContext.<String>getParameterAs(PARAMETER_FALLBACK_LABEL))
                        .map(editorContext.<WidgetInstanceManager>getParameterAs(Editor.WIDGET_INSTANCE_MANAGER)::getLabel)
                        .orElse(getLabelService().getObjectLabel(editorContext.getInitialValue()));
    }


    protected ExpressionResolverFactory getExpressionResolverFactory()
    {
        return expressionResolverFactory;
    }


    @Required
    public void setExpressionResolverFactory(final ExpressionResolverFactory expressionResolverFactory)
    {
        this.expressionResolverFactory = expressionResolverFactory;
    }


    protected class FixedLabelProvider implements InstantEditorLabelProvider
    {
        private final EditorContext<Object> editorContext;


        public FixedLabelProvider(final EditorContext<Object> editorContext)
        {
            this.editorContext = editorContext;
        }


        @Override
        public boolean canHandle(final String editorType)
        {
            return true;
        }


        @Override
        public String getLabel(final String editorType, final Object value)
        {
            return getMatchingLabelName(editorContext, value)
                            .map(labelName -> FixedInstantEditorLabelsRenderer.this.getLabel(editorContext, labelName))
                            .orElse(getFallbackLabel(editorContext));
        }


        @Override
        public int getOrder()
        {
            return 0;
        }
    }
}
