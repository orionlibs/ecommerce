/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.dynamicforms.impl.visitors;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicAttribute;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicForms;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.ScriptingConfig;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.Locales;
import org.zkoss.zk.ui.Component;

/**
 * Visitor of {@link Editor} form UI elements. Uses as a configuration {@link DynamicForms#getAttribute()}
 */
public class EditorsVisitor extends AbstractComponentsVisitor<Editor, DynamicAttribute>
{
    private static final Logger LOG = LoggerFactory.getLogger(EditorsVisitor.class);


    @Override
    protected boolean canHandle(final Component component)
    {
        if(component instanceof Editor)
        {
            final String property = getComponentKey((Editor)component);
            if(property != null)
            {
                final Predicate<DynamicAttribute> isDynamicEditor = attr -> StringUtils.equals(getPathToAttributeInModel(attr),
                                property);
                return getDynamicElements().stream().anyMatch(isDynamicEditor);
            }
        }
        return false;
    }


    @Override
    protected String getComponentKey(final Editor component)
    {
        return component.getProperty();
    }


    @Override
    public String getElementQualifierKey(final DynamicAttribute dynamicElement)
    {
        return getPathToAttributeInModel(dynamicElement);
    }


    @Override
    protected List<DynamicAttribute> getDynamicElements()
    {
        return getDynamicForms().getAttribute();
    }


    @Override
    protected void visitComponents(final DynamicAttribute dynamicAttribute, final Object target, final boolean initial)
    {
        final Collection<Editor> matchingEditors = getComponentKeyComponentsMap().get(getPathToAttributeInModel(dynamicAttribute));
        if(CollectionUtils.isNotEmpty(matchingEditors))
        {
            applyVisibleIf(matchingEditors, dynamicAttribute, target);
            applyDisabledIf(matchingEditors, dynamicAttribute, target);
        }
        if(StringUtils.isNotBlank(dynamicAttribute.getComputedValue()))
        {
            if(StringUtils.isNotBlank(dynamicAttribute.getParamName()))
            {
                if(CollectionUtils.isNotEmpty(matchingEditors))
                {
                    final Object computedValue = computeValue(target, dynamicAttribute);
                    modifyEditorsParameter(matchingEditors, dynamicAttribute.getParamName(), computedValue);
                }
            }
            else if(!initial)
            {
                if(canChangeProperty(target, dynamicAttribute.getQualifier()))
                {
                    final Object computedValue = computeValue(target, dynamicAttribute);
                    setAttributeValue(target, dynamicAttribute, computedValue);
                }
                else
                {
                    LOG.warn("Current user has insufficient permissions to set computed value on attribute {}", dynamicAttribute);
                }
            }
        }
    }


    protected void modifyEditorsParameter(final Collection<Editor> editors, final String paramName, final Object computedValue)
    {
        if(StringUtils.isNotBlank(paramName))
        {
            if(computedValue != null)
            {
                editors.forEach(editor -> {
                    if(ObjectUtils.notEqual(editor.getParameters().get(paramName), computedValue))
                    {
                        editor.addParameter(paramName, computedValue);
                        editor.reload();
                    }
                });
            }
            else
            {
                editors.forEach(editor -> {
                    if(editor.getParameters().get(paramName) != null)
                    {
                        editor.removeParameter(paramName);
                        editor.reload();
                    }
                });
            }
        }
    }


    protected void applyVisibleIf(final Collection<Editor> editors, final DynamicAttribute dynamicAttribute, final Object target)
    {
        if(StringUtils.isNotEmpty(dynamicAttribute.getVisibleIf()))
        {
            final boolean visible = isVisible(dynamicAttribute, target);
            editors.forEach(editor -> editor.getParent().setVisible(visible));
        }
    }


    protected void applyDisabledIf(final Collection<Editor> editors, final DynamicAttribute dynamicAttribute, final Object target)
    {
        if(StringUtils.isNotEmpty(dynamicAttribute.getDisabledIf()))
        {
            final boolean disabled = isDisabled(dynamicAttribute, target);
            editors.forEach(editor -> disableEditor(editor, disabled));
        }
    }


    protected void disableEditor(final Editor editor, final boolean disabled)
    {
        editor.setReadOnly(disabled);
    }


    @Override
    protected boolean isDisabled(final DynamicAttribute element, final Object target)
    {
        return super.isDisabled(element, target) || !canChangeProperty(target, element.getQualifier());
    }


    protected Object computeValue(final Object target, final DynamicAttribute dynamicAttribute)
    {
        final ScriptingConfig sc = dynamicAttribute.getScriptingConfig() == null ? getDefaultScriptingConfig()
                        : dynamicAttribute.getScriptingConfig();
        return getExpressionEvaluator().evaluateExpression(sc.getComputedValueLanguage(), sc.getComputedValueScriptType(),
                        dynamicAttribute.getComputedValue(), target);
    }


    protected void setAttributeValue(final Object target, final DynamicAttribute dynamicAttribute, final Object computedValue)
    {
        if(isAttributeLocalized(dynamicAttribute))
        {
            final Set<Locale> languagesToSetValue = getLanguagesToSetValue(dynamicAttribute, target);
            if(CollectionUtils.isNotEmpty(languagesToSetValue))
            {
                Map currentValue = getWidgetInstanceManager().getModel().getValue(getPathToAttributeInModel(dynamicAttribute),
                                Map.class);
                if(currentValue == null)
                {
                    currentValue = Maps.newHashMap();
                }
                for(final Locale locale : languagesToSetValue)
                {
                    currentValue.put(locale, computedValue);
                }
                getWidgetInstanceManager().getModel().setValue(getPathToAttributeInModel(dynamicAttribute), currentValue);
            }
        }
        else
        {
            getWidgetInstanceManager().getModel().setValue(getPathToAttributeInModel(dynamicAttribute), computedValue);
        }
    }


    protected boolean isAttributeLocalized(final DynamicAttribute dynamicAttribute)
    {
        final DataType dataType = getDataType();
        if(dataType != null)
        {
            final DataAttribute dataTypeAttribute = dataType.getAttribute(dynamicAttribute.getQualifier());
            return dataTypeAttribute != null && dataTypeAttribute.isLocalized();
        }
        return false;
    }


    protected Set<Locale> getLanguagesToSetValue(final DynamicAttribute dynamicAttribute, final Object target)
    {
        final Set<Locale> writableLocalesForUser = getPermissionFacade().getWritableLocalesForInstance(target);
        if(StringUtils.isNotEmpty(dynamicAttribute.getLang()))
        {
            if("*".equals(dynamicAttribute.getLang()))
            {
                return writableLocalesForUser;
            }
            else
            {
                final List<String> languages = Arrays.asList(dynamicAttribute.getLang().split(","));
                final Set<Locale> locales = writableLocalesForUser.stream().filter(loc -> languages.contains(loc.toString()))
                                .collect(Collectors.toSet());
                if(CollectionUtils.isNotEmpty(locales))
                {
                    return locales;
                }
                else if(LOG.isWarnEnabled())
                {
                    LOG.warn(String.format("Given user cannot edit attribute:%s.%s value for any of provided locales:%s",
                                    getTypeCode(), dynamicAttribute.getQualifier(), languages.toString()));
                }
            }
        }
        else
        {
            final Locale currentLocale = Locales.getCurrent();
            if(writableLocalesForUser.contains(currentLocale))
            {
                return Sets.newHashSet(currentLocale);
            }
            else if(LOG.isWarnEnabled())
            {
                LOG.warn(String.format("Given user cannot edit attribute:%s.%s value for current locale:%s", getTypeCode(),
                                dynamicAttribute.getQualifier(), currentLocale.toString()));
            }
        }
        return Sets.newHashSet();
    }
}
