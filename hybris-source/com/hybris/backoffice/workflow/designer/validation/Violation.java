/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Violation
{
    public enum Level
    {
        INFO, WARN, ERROR
    }


    private final Level level;
    private final String messageKey;
    private final List<String> messageParameters;


    public Violation(final Level level, final String messageKey, final List<String> messageParameters)
    {
        this.messageKey = messageKey;
        this.messageParameters = messageParameters;
        this.level = level;
    }


    public String getMessageKey()
    {
        return messageKey;
    }


    public List<String> getMessageParameters()
    {
        return messageParameters;
    }


    public Level getLevel()
    {
        return level;
    }


    public static Violation info(final String messageKey, final String... messageParameters)
    {
        return ofLevel(Level.INFO, messageKey, messageParameters);
    }


    public static Violation warn(final String messageKey, final String... messageParameters)
    {
        return ofLevel(Level.WARN, messageKey, messageParameters);
    }


    public static Violation error(final String messageKey, final String... messageParameters)
    {
        return ofLevel(Level.ERROR, messageKey, messageParameters);
    }


    private static Violation ofLevel(final Level level, final String messageKey, final String... messageParameters)
    {
        return new Violation(level, messageKey, Arrays.asList(messageParameters));
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final Violation violation = (Violation)o;
        return Objects.equals(level, violation.level) && Objects.equals(messageKey, violation.messageKey)
                        && Objects.equals(messageParameters, violation.messageParameters);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(level, messageKey, messageParameters);
    }
}
