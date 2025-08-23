/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class ObjectValuePath
{
    private static final String PATH_SEPARATOR = ".";
    private static final Pattern LOCALIZED_VALUE_PATH = Pattern.compile("([^\\[]+)\\[([^\\]]+)\\]");


    private static Matcher matcher(final String path)
    {
        return LOCALIZED_VALUE_PATH.matcher(path);
    }


    /**
     * Gets full validation path for specified elements.
     *
     * @param elements
     *           path elements
     * @return full validation path built with provided path elements
     */
    public static String getPath(final String... elements)
    {
        if(elements.length == 0)
        {
            return StringUtils.EMPTY;
        }
        else if(elements.length == 1 && StringUtils.isNotEmpty(elements[0]))
        {
            return elements[0];
        }
        else
        {
            return Stream.of(elements).filter(StringUtils::isNotEmpty).reduce((s1, s2) -> s1.concat(PATH_SEPARATOR).concat(s2))
                            .orElse(StringUtils.EMPTY);
        }
    }


    private static String getLocaleFromPath(final Matcher matcher)
    {
        return matcher.matches() ? matcher.group(2) : null;
    }


    /**
     * Gets a locale from provided path if it is a path with locale included
     *
     * @param path
     *           path to be checked
     * @return locale from path or <code>null</code>
     */
    public static String getLocaleFromPath(final String path)
    {
        return getLocaleFromPath(matcher(path));
    }


    private static boolean isLocalizedPath(final Matcher matcher)
    {
        return matcher.matches();
    }


    /**
     * Checks whether provided path is a path with locale included
     *
     * @param path
     *           path to be checked
     * @return <code>true</code> if path contains locale specification
     */
    public static boolean isLocalizedPath(final String path)
    {
        return isLocalizedPath(matcher(path));
    }


    private static boolean isLocalePath(final Matcher matcher, final String locale)
    {
        return matcher.matches() && StringUtils.equals(matcher.group(2), locale);
    }


    /**
     * Checks whether provided path contains specified locale
     *
     * @param path
     *           path to be checked
     * @param locale
     *           locale expected
     * @return <code>true</code> if path contains locale specification and it points to locale provided
     */
    public static boolean isLocalePath(final String path, final String locale)
    {
        return isLocalePath(matcher(path), locale);
    }


    private static String getNotLocalizedPath(final Matcher matcher, final String path)
    {
        if(matcher.matches())
        {
            return matcher.group(1);
        }
        else
        {
            return path;
        }
    }


    /**
     * Removes all locale specifications from path.
     *
     * @param path
     *           path to be cleared
     * @return path with no locale specifications
     */
    public static String getNotLocalizedPath(final String path)
    {
        final String result = getNotLocalizedPath(matcher(path), path);
        return result != null ? result : path;
    }


    /**
     * Appends locale specification to provided path.
     * <p>
     * Any current locale specification in provided path will be removed.
     *
     * @param path
     *           path to be decorated
     * @param locale
     *           locale to be added
     * @return path with locale added
     */
    public static String getLocalePath(final String path, final String locale)
    {
        return String.format("%s[%s]", getNotLocalizedPath(path), locale);
    }


    private final List<String> path;
    private String locale;


    /**
     * Parses provided path
     *
     * @param path
     *           full path String representation
     * @return parsed path
     * @see #buildPath()
     */
    public static ObjectValuePath parse(final String path)
    {
        if(StringUtils.isEmpty(path))
        {
            return new ObjectValuePath();
        }
        else
        {
            final Matcher matcher = matcher(path);
            final String locale = getLocaleFromPath(matcher);
            final String[] elements = getNotLocalizedPath(matcher, path).split("\\.");
            return new ObjectValuePath(elements).localize(locale);
        }
    }


    /**
     * Creates an instance of empty path
     *
     * @return empty path
     */
    public static ObjectValuePath empty()
    {
        return new ObjectValuePath();
    }


    /**
     * Creates a copy of path
     *
     * @param path
     *           path to be copied
     * @return copy
     */
    public static ObjectValuePath copy(final ObjectValuePath path)
    {
        return new ObjectValuePath().appendPath(path).localize(path.getLocale());
    }


    /**
     * Creates a reference to provided path that prevents it from changing
     *
     * @param path
     *           referenced path
     * @return a reference that may not be changed
     */
    public static ObjectValuePath unmodifiable(final ObjectValuePath path)
    {
        return new ObjectValuePath()
        {
            @Override
            protected List<String> createPath(final String... elements)
            {
                return Collections.unmodifiableList(path.path);
            }


            @Override
            public ObjectValuePath localize(final String locale)
            {
                throw new UnsupportedOperationException();
            }


            @Override
            public ObjectValuePath internationalize()
            {
                throw new UnsupportedOperationException();
            }


            @Override
            public String getLocale()
            {
                return path.getLocale();
            }
        };
    }


    protected ObjectValuePath()
    {
        path = ObjectValuePath.this.createPath();
    }


    protected ObjectValuePath(final String... elements)
    {
        path = ObjectValuePath.this.createPath(elements);
    }


    protected List<String> createPath(final String... elements)
    {
        return new ArrayList<>(Arrays.asList(elements));
    }


    /**
     * Checks whether path contains any elements
     *
     * @return <code>true</code> if path does not contain any elements
     */
    public boolean isEmpty()
    {
        return path.isEmpty();
    }


    /**
     * Gets number of elements in path
     *
     * @return number of path elements
     */
    public int size()
    {
        return path.size();
    }


    /**
     * Get a root of current path.
     *
     * @return root or empty path if no root exists
     */
    public ObjectValuePath getRoot()
    {
        if(size() < 1)
        {
            return empty();
        }
        return new ObjectValuePath(path.get(0));
    }


    /**
     * Gets a parent path for current.
     *
     * @return parent path or empty path if no parent exists
     */
    public ObjectValuePath getParent()
    {
        if(size() < 2)
        {
            return empty();
        }
        return new ObjectValuePath(path.subList(0, path.size() - 1).toArray(new String[0]));
    }


    /**
     * Adds new element of path at the end of path.
     * <P>
     * Method actually changes current path.
     *
     * @param element
     *           new path element
     * @return current path after change
     */
    public ObjectValuePath append(final String element)
    {
        path.add(element);
        return this;
    }


    /**
     * Adds path at the end of path.
     * <P>
     * Method actually changes current path.
     *
     * @param path
     *           new path
     * @return current path after change
     */
    public ObjectValuePath appendPath(final ObjectValuePath path)
    {
        this.path.addAll(path.path);
        return this;
    }


    /**
     * Parses provided path and adds it at the end of path.
     * <P>
     * Method actually changes current path.
     *
     * @param path
     *           new path
     * @return current path after change
     */
    public ObjectValuePath appendPath(final String path)
    {
        return appendPath(ObjectValuePath.parse(path));
    }


    /**
     * Adds localization to path
     * <P>
     * Method actually changes current path.
     *
     * @param locale
     *           localization symbol
     * @return current path after change
     */
    public ObjectValuePath localize(final String locale)
    {
        this.locale = locale;
        return this;
    }


    /**
     * Removes localization from path
     * <P>
     * Method actually changes current path.
     *
     * @return current path after change
     */
    public ObjectValuePath internationalize()
    {
        locale = null;
        return this;
    }


    /**
     * Checks whether path contains localization definition
     *
     * @return <code>true</code> if path is localized
     */
    public boolean isLocalized()
    {
        return locale != null;
    }


    /**
     * Gets localization code from path
     *
     * @return locale or <code>null</code> if path is not localized
     * @see Locale#getLanguage()
     */
    public String getLocale()
    {
        return locale;
    }


    /**
     * Adds new path at the beginning of path.
     * <P>
     * Method actually changes current path.
     *
     * @param element
     *           path to be prepended
     * @return current path after change
     */
    public ObjectValuePath prepend(final String element)
    {
        path.add(0, element);
        return this;
    }


    /**
     * Adds new path at the beginning of path.
     * <P>
     * Method actually changes current path.
     *
     * @param root
     *           path to be prepended
     * @return current path after change
     */
    public ObjectValuePath prepend(final ObjectValuePath root)
    {
        this.path.addAll(0, root.path);
        return this;
    }


    /**
     * Checks whether current path starts with provided
     *
     * @param root
     *           reference path
     * @return <code>true</code> if current path is relative or equal to provided
     */
    public boolean startsWith(final String root)
    {
        return startsWith(parse(root));
    }


    /**
     * Checks whether current path starts with provided
     *
     * @param root
     *           reference path
     * @return <code>true</code> if current path is relative or equal to provided
     */
    public boolean startsWith(final ObjectValuePath root)
    {
        if(root.isEmpty())
        {
            return true;
        }
        else if(path.size() >= root.size() && CollectionUtils.isEqualCollection(path.subList(0, root.path.size() - 1),
                        root.path.subList(0, root.path.size() - 1)))
        {
            String lastRoot = root.path.get(root.path.size() - 1);
            String lastPath = path.get(root.path.size() - 1);
            if(root.isLocalized())
            {
                lastRoot = getLocalePath(lastRoot, root.getLocale());
                if(isLocalized() && root.size() == this.size())
                {
                    lastPath = getLocalePath(lastPath, getLocale());
                }
            }
            else if(isLocalizedPath(lastPath))
            {
                lastPath = getNotLocalizedPath(lastPath);
            }
            return StringUtils.equals(lastPath, lastRoot);
        }
        else
        {
            return false;
        }
    }


    /**
     * Builds new path that should be interpreted as relative to provided
     *
     * @param root
     *           reference path
     * @return current path as relative to provided
     * @throws IllegalArgumentException
     *            thrown if current path is not relative to provided
     */
    public ObjectValuePath getRelative(final String root)
    {
        return getRelative(parse(root));
    }


    /**
     * Builds new path that should be interpreted as relative to provided
     *
     * @param root
     *           reference path
     * @return current path as relative to provided
     * @throws IllegalArgumentException
     *            thrown if current path is not relative to provided
     */
    public ObjectValuePath getRelative(final ObjectValuePath root)
    {
        if(startsWith(root))
        {
            return new ObjectValuePath(path.subList(root.path.size(), path.size()).toArray(new String[0]));
        }
        else
        {
            throw new IllegalArgumentException(
                            "Provided path (" + root.buildPath() + ") is not a root of this path: " + buildPath());
        }
    }


    /**
     * Splits current path to array fo elements
     *
     * @return arrays of elements in path
     */
    public String[] split()
    {
        return path.toArray(new String[path.size()]);
    }


    /**
     * Merges all path elements into single {@link String} representation
     *
     * @return path representation
     */
    public String buildPath()
    {
        if(locale != null)
        {
            return getLocalePath(getPath(split()), locale);
        }
        else
        {
            return getPath(split());
        }
    }


    @Override
    public String toString()
    {
        return buildPath();
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o.getClass() != this.getClass())
        {
            return false;
        }
        final ObjectValuePath valuePath = (ObjectValuePath)o;
        if(!path.equals(valuePath.path))
        {
            return false;
        }
        return !(getLocale() != null ? !getLocale().equals(valuePath.getLocale()) : valuePath.getLocale() != null);
    }


    @Override
    public int hashCode()
    {
        int result = path.hashCode();
        result = 31 * result + (getLocale() != null ? getLocale().hashCode() : 0);
        return result;
    }
}
