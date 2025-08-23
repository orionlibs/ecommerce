/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;

/**
 * General purpose class delivering validation methods.
 */
public final class Validate
{
    private Validate()
    {
        // blocks the possibility of create a new instance
    }


    public static void assertTrue(final String message, final boolean value)
    {
        if(!value)
        {
            throw new IllegalArgumentException(message);
        }
    }


    public static void notEmpty(final String message, final Collection<?> collection)
    {
        if(collection == null || collection.isEmpty())
        {
            throw new IllegalArgumentException(message);
        }
    }


    public static void atLeastOneNotNull(final String message, final Object... args)
    {
        for(final Object arg : args)
        {
            if(arg != null)
            {
                return;
            }
        }
        throw new IllegalArgumentException(message);
    }


    public static void isInstanceOf(final Class<?> clazz, final Object target)
    {
        if(target == null || !clazz.isAssignableFrom(target.getClass()))
        {
            throw new IllegalArgumentException(String.format("Expected instance of type [%s] but got [%s]", clazz.getCanonicalName(),
                            target == null ? null : target.getClass().getCanonicalName()));
        }
    }


    public static void notBlank(final String message, final String... values)
    {
        final Result result = validate(message, str -> StringUtils.isNotBlank(str), values);
        if(!result.isValid())
        {
            throw new IllegalArgumentException(result.getMessage());
        }
    }


    public static void notNull(final String message, final Object... args)
    {
        final Result result = validate(message, obj -> obj != null, args);
        if(!result.isValid())
        {
            throw new IllegalArgumentException(result.getMessage());
        }
    }


    /**
     * Validates arguments.
     *
     * @param message
     *           the error message prefix.
     * @param checker
     *           the checker which verifies if every argument is valid.
     * @param args
     *           the arguments.
     * @return result which stores information if arguments are valid or not. If not, then it contains error message.
     */
    private static <T> Result validate(final String message, final Predicate<T> checker, final T... args)
    {
        if(args.length == 1)
        {
            if(!checker.test(args[0]))
            {
                return new Result(message);
            }
            return new Result();
        }
        final List<CharSequence> indexes = new LinkedList<>();
        for(int i = 0; i < args.length; ++i)
        {
            if(!checker.test(args[i]))
            {
                indexes.add(Integer.toString(i));
            }
        }
        if(!indexes.isEmpty())
        {
            return new Result(
                            String.format("%s (index%s: %s)", message, indexes.size() > 1 ? "es" : "", String.join(", ", indexes)));
        }
        return new Result();
    }


    private static final class Result
    {
        private final String message;


        private Result()
        {
            this(null);
        }


        private Result(final String message)
        {
            this.message = message;
        }


        private boolean isValid()
        {
            return message == null;
        }


        private String getMessage()
        {
            return message;
        }
    }
}
