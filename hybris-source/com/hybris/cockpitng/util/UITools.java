/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.google.common.collect.Sets;
import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.util.Validate;
import java.net.URI;
import java.text.BreakIterator;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

/**
 * Helper class for common UI related operations.
 */
public final class UITools
{
    public static final String EVENT_ON_LOOPBACK = "onLoopbackEvent";
    public static final Pattern URL_EXTENSION_PATTERN = Pattern.compile("^[^?]+\\.([^?]+)(?:\\?.*)?$");
    public static final String DOTS = "...";
    private static final Logger LOG = LoggerFactory.getLogger(UITools.class);


    private UITools()
    {
        throw new AssertionError("Utility class should not be instantiated");
    }


    /**
     * Adds a style class to the components style classes.
     *
     * @param component
     *           the component to change style classes
     * @param className
     *           the class name
     */
    public static void addSClass(final HtmlBasedComponent component, final String className)
    {
        modifySClass(component, className, true);
    }


    /**
     * Removes a style class from the components style classes.
     *
     * @param component
     *           the component to change style classes
     * @param className
     *           the class name
     */
    public static void removeSClass(final HtmlBasedComponent component, final String className)
    {
        modifySClass(component, className, false);
    }


    /**
     * Adds or removes a style class to the components style classes.
     *
     * @param component
     *           the component to change style classes
     * @param className
     *           the class name
     * @param add
     *           adding or removing the class
     */
    public static void modifySClass(final HtmlBasedComponent component, final String className, final boolean add)
    {
        if(StringUtils.isNotBlank(className) && component != null)
        {
            final Set<String> actual = Sets.newLinkedHashSet();
            final String sclass = component.getSclass();
            if(StringUtils.isNotBlank(sclass))
            {
                Collections.addAll(actual, sclass.split("\\s+"));
            }
            for(final String clazz : className.trim().split("\\s+"))
            {
                if(add)
                {
                    actual.add(clazz);
                }
                else
                {
                    actual.remove(clazz);
                }
            }
            component.setSclass(actual.isEmpty() ? null : StringUtils.join(actual, " "));
        }
    }


    /**
     * Adds a style to the components style properties.
     *
     * @param component
     *           the component to change style propreties
     * @param property
     *           style property
     * @param value
     *           value of style property
     */
    public static void addStyle(final HtmlBasedComponent component, final String property, final String value)
    {
        if(StringUtils.isNotBlank(property))
        {
            final Set<String> actual = Sets.newLinkedHashSet();
            final String style = component.getStyle();
            if(!StringUtils.isBlank(style))
            {
                Collections.addAll(actual, style.split("\\s*;\\s*"));
            }
            final StringBuilder result = new StringBuilder();
            boolean append = true;
            for(final String each : actual)
            {
                final String[] propertyValue = each.split("\\s*:\\s*");
                final String currentProperty = propertyValue[0];
                final String currentValue;
                if(propertyValue.length > 1)
                {
                    currentValue = propertyValue[1];
                }
                else if(StringUtils.equals(property, currentProperty))
                {
                    currentValue = value;
                    append = false;
                }
                else
                {
                    currentValue = StringUtils.EMPTY;
                }
                if(result.length() > 0)
                {
                    result.append(";");
                }
                result.append(currentProperty).append(": ").append(currentValue);
            }
            if(append)
            {
                if(result.length() > 0)
                {
                    result.append(";");
                }
                result.append(property).append(": ").append(value);
            }
            component.setStyle(result.toString());
        }
    }


    /**
     * Removes a style from the components style properties.
     *
     * @param component
     *           the component to change style properties
     * @param property
     *           style property
     */
    public static void removeStyle(final HtmlBasedComponent component, final String property)
    {
        if(StringUtils.isNotBlank(property))
        {
            final Set<String> actual = Sets.newLinkedHashSet();
            final String style = component.getStyle();
            if(!StringUtils.isBlank(style))
            {
                Collections.addAll(actual, style.split("\\s*;\\s*"));
            }
            final StringBuilder result = new StringBuilder();
            actual.forEach(each -> {
                final String[] propertyValue = each.split("\\s*:\\s*");
                final String currentProperty = propertyValue[0];
                if(result.length() > 0)
                {
                    result.append(";");
                }
                if(!StringUtils.equals(property, currentProperty))
                {
                    result.append(each);
                }
            });
            component.setStyle(result.toString());
        }
    }


    /**
     * The method appends leading '~/' to the URL (if needed) to enable ZK to properly resolve the URL.
     *
     * @param url
     *           the URL to be adjusted
     * @return the adjusted URL
     * @deprecated since 1808, the method was used to provide paths to medias that were directly served. Currently each web
     *             application should be able to serve medias through appropriate servlet. This allows for secure access.
     */
    @Deprecated(since = "1808", forRemoval = true)
    public static String adjustURL(final String url)
    {
        Validate.notBlank("URL to be adjusted may not be blank", url);
        final String normalizedUrl = url.trim();
        if(isUrlAbsolute(normalizedUrl))
        {
            return normalizedUrl;
        }
        else
        {
            return url.startsWith("/") ? ("~" + url) : ("~/" + url);
        }
    }


    /**
     * @param url
     *           url to be checked
     * @return true if the url argument represents an absolute path
     */
    public static boolean isUrlAbsolute(final String url)
    {
        try
        {
            final URI tmpUri = new URI(url);
            return tmpUri.isAbsolute();
        }
        catch(final Exception e)
        {
            LOG.error("Exception", e);
        }
        return false;
    }


    /**
     * Truncates the text with default append of "...". See com.hybris.cockpitng.util.UITools#truncateText(java.lang.String,
     * int, java.lang.String) for more details.
     *
     * @param text
     *           Text to be optionally truncated.
     * @param maxLength
     *           Maximal length of the output text.
     * @return Truncated text.
     * @see UITools#truncateText(java.lang.String, int, java.lang.String)
     */
    public static String truncateText(final String text, final int maxLength)
    {
        return truncateText(text, maxLength, DOTS);
    }


    /**
     * Truncates the input text to be no longer than the specified maximal length. If needed an additional text is appended
     * to the truncated version (e.g. "..."). When the text is truncated it always chooses full words to the output texts.
     * For example for "Lorem ipsum dolor sit amet.", max length 15 and "..." as the append the result would be "Lorem
     * ipsum...".
     *
     * @param text
     *           Text to be optionally truncated.
     * @param maxLength
     *           Maximal length of the output text.
     * @param append
     *           Text to be appended to the result in case when the input text needs to be shortened. The length of the
     *           appended text is calculated to its total length i.e. length of trimmed text plus length of appended text is
     *           less or equal to the maxLength.
     * @return Truncated text.
     */
    public static String truncateText(final String text, final int maxLength, final String append)
    {
        String ret;
        if(maxLength > 0 && StringUtils.isNotBlank(text))
        {
            final String trim = text.trim();
            ret = trim;
            if(trim.length() > maxLength)
            {
                final int maxLengthInternal = maxLength - append.length();
                if(maxLengthInternal <= 0)
                {
                    LOG.warn("The appended string [{}] is longer than the maximal number of characters allowed " + "[{}]", append,
                                    maxLength);
                    ret = append.substring(0, maxLength);
                }
                else
                {
                    final BreakIterator iterator = BreakIterator.getWordInstance();
                    iterator.setText(trim);
                    if(iterator.isBoundary(maxLengthInternal))
                    {
                        ret = trim.substring(0, iterator.current()).trim() + append;
                    }
                    else
                    {
                        final String preceding = trim.substring(0, iterator.preceding(maxLengthInternal)).trim();
                        if(StringUtils.isNotBlank(preceding))
                        {
                            ret = preceding + append;
                        }
                        else
                        {
                            ret = text.substring(0, maxLengthInternal) + append;
                        }
                    }
                }
            }
        }
        else
        {
            ret = text == null ? StringUtils.EMPTY : text.trim();
        }
        return ret;
    }


    public static String extractExtension(final String url)
    {
        final Matcher extensionMatcher = URL_EXTENSION_PATTERN.matcher(url);
        if(extensionMatcher.matches())
        {
            final String extension = extensionMatcher.group(1);
            if(StringUtils.isNotBlank(extension))
            {
                return extension;
            }
        }
        return null;
    }


    /**
     * Executes specified {@link Executable} in next UI processing iteration.
     * <P>
     * Method may be used to assure that particular logic is executed after all UI updates related to request are already
     * performed and finished.
     * </P>
     *
     * @param parent
     *           any DOM element that belongs to a {@link org.zkoss.zk.ui.Desktop} which UI processing iteration should be
     *           used
     * @param executable
     *           operation to be scheduled
     */
    public static void postponeExecution(final Component parent, final Executable executable)
    {
        final String eventName = EVENT_ON_LOOPBACK + "-" + executable.hashCode() + "-" + parent.hashCode();
        parent.addEventListener(eventName, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event)
            {
                executable.execute();
                parent.removeEventListener(eventName, this);
            }
        });
        Events.echoEvent(eventName, parent, null);
    }


    /**
     * Removes all event listener of the given type from the given component.
     *
     * @param component
     *           Component from which the listeners should be removed
     * @param eventName
     *           Event type for which the listeners should be removed
     */
    public static void removeAllEventListeners(final AbstractComponent component, final String eventName)
    {
        component.getEventListeners(eventName).forEach(listener -> component.removeEventListener(eventName, listener));
    }


    /**
     * Method checks if component on specified index in available in children list and meets provided requirement. If not,
     * then new child is being obtained and added to parent.
     *
     * @param parent
     *           component which child is requested
     * @param childIndex
     *           index on which a child should be available
     * @param childRequirement
     *           if a any child is available at provided index, it is checked against this requirement and replace with new,
     *           if not meet
     * @param newChildSupplier
     *           if a child does not exist on specified index or does not meet a requirement, then new child is obtained
     *           from this supplier
     * @param <CMP>
     *           type of child expected
     * @return child found or obtained from supplier
     * @throws IllegalArgumentException
     *            if provided index is lower then actual number of children - new child cannot be added on specified index
     */
    public static <CMP extends Component> CMP appendChildIfNeeded(final Component parent, final int childIndex,
                    final Predicate<Component> childRequirement, final Supplier<CMP> newChildSupplier)
    {
        final int childrenElementsCount = parent.getChildren().size();
        if(childrenElementsCount < childIndex)
        {
            throw new IllegalArgumentException("Children may be appended sequentially. Unable to append child on index " + childIndex
                            + " when only " + childrenElementsCount + " children already exists.");
        }
        boolean addChild = childrenElementsCount == childIndex;
        if(childrenElementsCount > childIndex && !childRequirement.test(parent.getChildren().get(childIndex)))
        {
            parent.getChildren().remove(childIndex);
            addChild = true;
        }
        if(addChild)
        {
            final CMP child = newChildSupplier.get();
            parent.getChildren().add(childIndex, child);
            return child;
        }
        else
        {
            return (CMP)parent.getChildren().get(childIndex);
        }
    }
}
