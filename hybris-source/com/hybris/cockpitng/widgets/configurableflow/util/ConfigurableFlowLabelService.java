/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.util;

import com.hybris.cockpitng.config.jaxb.wizard.InitializeType;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;

/**
 * Service supporting labels
 */
public class ConfigurableFlowLabelService
{
    public static final String WIZARD_CURRENT_CONTEXT = "currentContext";
    private static final String TYPE_CODE = "TYPE_CODE";
    private final transient WidgetInstanceManager widgetInstanceManager;
    private final transient LabelService labelService;
    private final TypeFacade typeFacade;
    private transient List<InitializeType> initializeList = Collections.emptyList();
    private ConfigurableFlowExpressions configurableFlowExpressions;


    /**
     * Creates an instance of ConfigurableFlowLabelService
     *
     * @param widgetInstanceManager
     *           necessary to retrieve widget framework related functionality
     * @param labelService
     *           service for extracting labels
     * @deprecated since 1905
     */
    @Deprecated(since = "1905", forRemoval = true)
    public ConfigurableFlowLabelService(final WidgetInstanceManager widgetInstanceManager, final LabelService labelService)
    {
        this(widgetInstanceManager, labelService, null);
    }


    /**
     * Creates an instance of ConfigurableFlowLabelService
     *
     * @param widgetInstanceManager
     *           necessary to retrieve widget framework related functionality
     * @param labelService
     *           service for extracting labels
     * @param typeFacade
     *           facade for getting item type
     */
    public ConfigurableFlowLabelService(final WidgetInstanceManager widgetInstanceManager, final LabelService labelService,
                    final TypeFacade typeFacade)
    {
        this.widgetInstanceManager = widgetInstanceManager;
        this.labelService = labelService;
        this.typeFacade = typeFacade;
    }


    /**
     * Get localized label for ConfigurableFlow's item from configuration.
     *
     * @param expression
     *           - given expression to be translated into appropriate label
     * @return translated label
     */
    public String getLabel(final String expression)
    {
        if(StringUtils.isBlank(expression))
        {
            return StringUtils.EMPTY;
        }
        final String expressionToParse = expression.trim();
        final Pattern pattern = Pattern.compile("^\\s*([\\w.]+)\\((.*)\\)\\s*$", Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(expressionToParse);
        final String[] args;
        final String key;
        if(matcher.matches())
        {
            key = matcher.group(1);
            args = StringUtils.defaultIfBlank(matcher.group(2), StringUtils.EMPTY).split(",");
        }
        else
        {
            final String[] split = expressionToParse.split(",");
            key = split[0];
            args = Arrays.copyOfRange(split, 1, split.length);
        }
        final List<String> ultimateArguments = new ArrayList<>();
        for(final String argument : args)
        {
            ultimateArguments.add(interpretArg(argument));
        }
        String i18nLabel = widgetInstanceManager.getLabel(key, ultimateArguments.toArray());
        if(StringUtils.isBlank(i18nLabel))
        {
            i18nLabel = Labels.getLabel(key, ultimateArguments.toArray());
        }
        return i18nLabel == null ? expression : i18nLabel;
    }


    private String interpretArg(final String arg)
    {
        final Object evaluatedObject = getConfigurableFlowExpressions().evalExpression(widgetInstanceManager.getModel(), arg);
        if(evaluatedObject instanceof String)
        {
            final String objectLabel = labelService.getObjectLabel(evaluatedObject);
            if(objectLabel != null)
            {
                return objectLabel;
            }
            return (String)evaluatedObject;
        }
        else
        {
            final String objectLabel = labelService.getObjectLabel(arg);
            if(objectLabel != null)
            {
                return objectLabel;
            }
            return arg;
        }
    }


    /**
     * Retrieves qualifier human-readable label according to given prefix and qualifier.
     *
     * @param prefix
     *           given prefix
     * @param qualifier
     *           given qualifier
     * @return qualifier label
     */
    public String getQualifierLabel(final String prefix, final String qualifier)
    {
        String ret = StringUtils.EMPTY;
        final Optional<String> qualifierPrefixLabel = getQualifierLabelUsingPrefix(prefix, qualifier);
        if(qualifierPrefixLabel.isPresent())
        {
            ret = qualifierPrefixLabel.get();
        }
        if(StringUtils.isBlank(ret))
        {
            ret = Labels.getLabel(qualifier);
            if(StringUtils.isBlank(ret))
            {
                ret = "[" + qualifier + "]";
            }
        }
        else if(widgetInstanceManager.getModel().getValue(WIZARD_CURRENT_CONTEXT, Map.class) != null
                        && StringUtils.isBlank(prefix))
        {
            final Map<String, Object> ctx = widgetInstanceManager.getModel().getValue(WIZARD_CURRENT_CONTEXT, Map.class);
            final String typeCode = (String)ctx.get(TYPE_CODE);
            if(StringUtils.isNotBlank(typeCode))
            {
                ret = labelService.getObjectLabel(typeCode + "." + qualifier);
            }
        }
        return ret;
    }


    protected Optional<String> getQualifierLabelUsingPrefix(final String prefix, final String qualifier)
    {
        String qualifierLabel = StringUtils.EMPTY;
        if(StringUtils.isNotBlank(prefix))
        {
            final Object item = widgetInstanceManager.getModel().getValue(prefix, Object.class);
            if(item != null && typeFacade != null)
            {
                final String type = typeFacade.getType(item);
                qualifierLabel = resolveTypeLabel(type, qualifier);
            }
            if(StringUtils.isBlank(qualifierLabel) && CollectionUtils.isNotEmpty(initializeList))
            {
                final Optional<String> qualifierInitTypeLabel = getQualifierLabelUsingInitType(prefix, qualifier);
                qualifierLabel = qualifierInitTypeLabel.orElse(qualifierLabel);
            }
        }
        return Optional.ofNullable(qualifierLabel);
    }


    protected Optional<String> getQualifierLabelUsingInitType(final String prefix, final String qualifier)
    {
        for(final InitializeType initType : initializeList)
        {
            if(StringUtils.equals(initType.getProperty(), prefix))
            {
                return Optional
                                .ofNullable(resolveTypeLabel(resolveTypeName(initType.getType(), widgetInstanceManager.getModel()), qualifier));
            }
        }
        return Optional.empty();
    }


    /**
     * The initializeList is required by getQualifierLabel method
     */
    public void setInitializeList(final List<InitializeType> initializeList)
    {
        this.initializeList = initializeList;
    }


    protected ConfigurableFlowExpressions getConfigurableFlowExpressions()
    {
        if(configurableFlowExpressions == null)
        {
            configurableFlowExpressions = new ConfigurableFlowExpressions();
        }
        return configurableFlowExpressions;
    }


    protected String resolveTypeName(final String initialTypeName, final WidgetModel model)
    {
        final Object dynamicTypeName = getConfigurableFlowExpressions().evalExpression(model, initialTypeName);
        return dynamicTypeName instanceof String ? (String)dynamicTypeName : initialTypeName;
    }


    protected String resolveTypeLabel(final String typeName, final String qualifier)
    {
        final String key = typeName + "." + qualifier;
        final String ret = labelService.getObjectLabel(key);
        return key.equals(ret) ? null : ret;
    }
}
