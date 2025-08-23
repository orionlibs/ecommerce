/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.valuechooser;

import com.hybris.cockpitng.config.valuechooser.jaxb.Option;
import com.hybris.cockpitng.config.valuechooser.jaxb.Value;
import com.hybris.cockpitng.config.valuechooser.jaxb.ValueChooser;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;

public class ValueChooserController extends DefaultWidgetController
{
    public static final String ATTRIBUTE_SELECTION_COMPONENT_CANDIDATE = "selectionComponent";
    protected static final String OUTPUT_SOCKET_SELECTION = "selection";
    protected static final String SETTING_CONFIGURATION_CONTEXT = "configCtx";
    protected static final String SETTING_RENDERER = "renderer";
    protected static final String SETTING_FORCE_SELECTION = "forceSelection";
    protected static final String SETTING_MULTI_SELECTION = "multiSelection";
    private static final Logger LOGGER = LoggerFactory.getLogger(ValueChooserController.class);
    private static final String SCLASS_OPTION_CONTAINER = "yw-valuechooser-option-container";
    private static final String SCLASS_SELECTION_HANDLER = "yw-valuechooser-selection-handler";
    private static final String SCLASS_OPTION_SELECTED = "yw-valuechooser-option-selected";
    private static final String DEFAULT_OPTION_RENDERER = "valueChooserRenderer";
    private static final String DEFAULT_CONFIGURATION_CONTEXT = "value-chooser";
    private static final String MODEL_SELECTION = "selection";
    private static final String ATTRIBUTE_OPTION = "option";
    private static final String ATTRIBUTE_OPTION_ID = "optionId";
    private transient WidgetComponentRenderer<Component, Option, Boolean> optionRenderer;
    @Wire
    private Div valuesContainer;
    @WireVariable
    private transient CockpitConfigurationService cockpitConfigurationService;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        try
        {
            final ValueChooser configuration = loadConfiguration();
            if(getValue(MODEL_SELECTION, Set.class) == null)
            {
                initializeSelection(configuration);
            }
            getValuesContainer().getChildren().clear();
            renderOptions(configuration, getValuesContainer());
        }
        catch(final CockpitConfigurationException e)
        {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
        postponeSendingSelection(comp);
    }


    protected void postponeSendingSelection(final Component parent)
    {
        UITools.postponeExecution(parent, this::sendSelection);
    }


    protected ValueChooser loadConfiguration() throws CockpitConfigurationException
    {
        final String component = ObjectUtils
                        .toString(getWidgetSettings().getOrDefault(SETTING_CONFIGURATION_CONTEXT, DEFAULT_CONFIGURATION_CONTEXT));
        final ConfigContext context = new DefaultConfigContext(component);
        return getCockpitConfigurationService().loadConfiguration(context, ValueChooser.class, getWidgetslot().getWidgetInstance());
    }


    protected void initializeSelection(final ValueChooser configuration)
    {
        configuration.getOption().stream().filter(Option::isSelected).forEach(option -> setSelected(option, true));
        if(getWidgetSettings().getBoolean(SETTING_FORCE_SELECTION) && getSelectedOptions().isEmpty()
                        && !configuration.getOption().isEmpty())
        {
            setSelected(configuration.getOption().iterator().next(), true);
        }
        if(!getWidgetSettings().getBoolean(SETTING_MULTI_SELECTION))
        {
            while(getSelectedOptions().size() > 1)
            {
                setSelected(getSelectedOptions().iterator().next(), false);
            }
        }
    }


    protected WidgetComponentRenderer<Component, Option, Boolean> getOptionRenderer()
    {
        if(optionRenderer == null)
        {
            final String renderer = ObjectUtils
                            .toString(getWidgetSettings().getOrDefault(SETTING_RENDERER, DEFAULT_OPTION_RENDERER));
            optionRenderer = BackofficeSpringUtil.getBean(renderer, WidgetComponentRenderer.class);
        }
        return optionRenderer;
    }


    protected void renderOptions(final ValueChooser configuration, final HtmlBasedComponent parent)
    {
        configuration.getOption().forEach(option -> {
            final HtmlBasedComponent optionContainer = createOptionContainer();
            bindOptionContainer(option, optionContainer);
            markAsSelected(optionContainer, isSelected(option));
            installSelectListener(optionContainer);
            renderOption(option, optionContainer);
            parent.appendChild(optionContainer);
        });
    }


    protected HtmlBasedComponent createOptionContainer()
    {
        final Div optionContainer = new Div();
        UITools.addSClass(optionContainer, SCLASS_OPTION_CONTAINER);
        return optionContainer;
    }


    protected void bindOptionContainer(final Option option, final HtmlBasedComponent optionContainer)
    {
        optionContainer.setAttribute(ATTRIBUTE_OPTION, option);
        optionContainer.setAttribute(ATTRIBUTE_OPTION_ID, option.getId());
    }


    protected Option getOption(final HtmlBasedComponent optionContainer)
    {
        return (Option)optionContainer.getAttribute(ATTRIBUTE_OPTION);
    }


    protected Optional<HtmlBasedComponent> getOptionContainer(final Option option)
    {
        final Component component = getValuesContainer().query("[" + ATTRIBUTE_OPTION_ID + "='" + option.getId() + "']");
        if(component instanceof HtmlBasedComponent)
        {
            return Optional.of((HtmlBasedComponent)component);
        }
        else
        {
            return Optional.empty();
        }
    }


    protected void renderOption(final Option option, final HtmlBasedComponent parent)
    {
        getOptionRenderer().render(parent, option, isSelected(option), DataType.BOOLEAN, getWidgetInstanceManager());
    }


    protected void installSelectListener(final HtmlBasedComponent optionContainer)
    {
        final HtmlBasedComponent selectionComponent = getOnClickComponent(optionContainer);
        UITools.addSClass(selectionComponent, SCLASS_SELECTION_HANDLER);
        selectionComponent.addEventListener(Events.ON_CLICK, createSelectionHandler(optionContainer));
    }


    protected HtmlBasedComponent getOnClickComponent(final HtmlBasedComponent optionContainer)
    {
        final Component candidate = optionContainer.query(String.format("[%s=true]", ATTRIBUTE_SELECTION_COMPONENT_CANDIDATE));
        return candidate instanceof HtmlBasedComponent ? (HtmlBasedComponent)candidate : optionContainer;
    }


    protected <E extends Event> EventListener<E> createSelectionHandler(final HtmlBasedComponent optionContainer)
    {
        return event -> handleSelection(optionContainer);
    }


    protected void handleSelection(final HtmlBasedComponent optionContainer)
    {
        final Option option = getOption(optionContainer);
        final boolean newSelected = !isSelected(option);
        if(newSelected && !getWidgetSettings().getBoolean(SETTING_MULTI_SELECTION))
        {
            final Set<Option> previouslySelectedOptions = new HashSet<>(getSelectedOptions());
            previouslySelectedOptions.forEach(selectedOption -> getOptionContainer(selectedOption)
                            .ifPresent(container -> handleSelectionImmediately(container, false)));
        }
        if(newSelected || getSelectedOptions().size() > 1 || !getWidgetSettings().getBoolean(SETTING_FORCE_SELECTION))
        {
            handleSelectionImmediately(optionContainer, newSelected);
            sendSelection();
        }
    }


    protected void handleSelectionImmediately(final HtmlBasedComponent optionContainer, final boolean selected)
    {
        final Option option = getOption(optionContainer);
        setSelected(option, selected);
        optionContainer.getChildren().clear();
        renderOption(option, optionContainer);
        markAsSelected(optionContainer, selected);
    }


    protected void setSelected(final Option option, final boolean selected)
    {
        Set<Option> selectedOptions = getValue(MODEL_SELECTION, Set.class);
        if(selectedOptions == null && selected)
        {
            selectedOptions = new HashSet<>();
            setValue(MODEL_SELECTION, selectedOptions);
        }
        if(selected)
        {
            selectedOptions.add(option);
        }
        else if(selectedOptions != null)
        {
            selectedOptions.remove(option);
        }
    }


    protected Boolean isSelected(final Option option)
    {
        return getSelectedOptions().contains(option);
    }


    protected Set<Option> getSelectedOptions()
    {
        final Set<Option> selectedOptions = getValue(MODEL_SELECTION, Set.class);
        return selectedOptions != null ? Collections.unmodifiableSet(selectedOptions) : Collections.emptySet();
    }


    protected void markAsSelected(final HtmlBasedComponent optionComponent, final boolean selected)
    {
        UITools.modifySClass(optionComponent, SCLASS_OPTION_SELECTED, selected);
    }


    protected void sendSelection()
    {
        final Collection<?> values = getSelectedValues();
        sendOutput(OUTPUT_SOCKET_SELECTION, values);
    }


    protected <T extends Object> Collection<T> getSelectedValues()
    {
        return getSelectedOptions().stream().map(Option::getValue).flatMap(Collection::stream).<T>map(this::getValueInstance)
                        .collect(Collectors.toSet());
    }


    protected <R> R getValueInstance(final Value value)
    {
        if(StringUtils.isNotEmpty(value.getBeanFactory()))
        {
            final BeanFactory beanFactory = BackofficeSpringUtil.getBean(value.getBeanFactory(), BeanFactory.class);
            return (R)beanFactory.getBean(value.getExpression());
        }
        else if(StringUtils.equals(String.class.getName(), value.getType()))
        {
            return (R)value.getExpression();
        }
        else if(StringUtils.isNotEmpty(value.getType()))
        {
            try
            {
                final Class<? extends R> valueType = (Class<? extends R>)Class.forName(value.getType());
                if(StringUtils.isNotEmpty(value.getFactoryMethod()))
                {
                    final Method factoryMethod = valueType.getMethod(value.getFactoryMethod(), String.class);
                    return (R)factoryMethod.invoke(null, value.getExpression());
                }
                else
                {
                    return valueType.getConstructor(String.class).newInstance(value.getExpression());
                }
            }
            catch(final ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException
                        | InstantiationException e)
            {
                LOGGER.error(e.getLocalizedMessage(), e);
            }
        }
        return null;
    }


    protected Div getValuesContainer()
    {
        return valuesContainer;
    }


    protected CockpitConfigurationService getCockpitConfigurationService()
    {
        return cockpitConfigurationService;
    }
}
