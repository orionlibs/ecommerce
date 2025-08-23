/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;

/**
 * Cockpit components utility class.
 */
public class CockpitComponentsUtils
{
    public static String getLabel(final WidgetInstanceManager wim, final String key, final Object... args)
    {
        return getLabel(key, args, wim::getLabel, Labels::getLabel);
    }


    public static String getLabel(final EditorContext<?> editorContext, final WidgetInstanceManager wim, final String key,
                    final Object... args)
    {
        return getLabel(key, args, editorContext::getLabel, wim::getLabel, Labels::getLabel);
    }


    protected static String getLabel(final String key, final Object[] args,
                    final BiFunction<String, Object[], String>... providers)
    {
        return Stream.of(providers).map(provider -> provider.apply(key, args)).filter(label -> label != null).findFirst()
                        .orElse(key);
    }


    /**
     * Finds closes component of given type and sclass.
     *
     * @param component
     *           given component and all its ancestors will be scanned.
     * @param parentComponentType
     *           type of parent component to be found.
     * @param parentSclass
     *           searched parentSclass without ., if empty then first window will be returned no matter parentSclass.
     * @return component if found.
     */
    public static <T extends Component> Optional<T> findClosestComponent(final Component component,
                    final Class<T> parentComponentType, final String parentSclass)
    {
        if(component == null)
        {
            return Optional.empty();
        }
        if(parentComponentType.isInstance(component)
                        && (StringUtils.isEmpty(parentSclass) || (component instanceof HtmlBasedComponent
                        && StringUtils.contains(((HtmlBasedComponent)component).getSclass(), parentSclass))))
        {
            return Optional.of((T)component);
        }
        return findClosestComponent(component.getParent(), parentComponentType, parentSclass);
    }


    /**
     * Static interface class to resove bean ids.
     */
    public interface BeanResolver
    {
        Object getBean(String id, Class<?> clazz);
    }
}
