/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * An output stream which buffers all output until condition is matched. Then it redirects the buffer to consumer.
 */
public class StringBufferOutputStream extends OutputStream
{
    private static final Pattern NEW_LINE_PATTERN = Pattern.compile(".*" + System.lineSeparator());
    private final Consumer<String> redirection;
    private final Predicate<String> condition;
    private StringBuffer buffer;


    public StringBufferOutputStream(final Consumer<String> redirection, final Predicate<String> condition)
    {
        this.redirection = redirection;
        this.condition = condition;
        this.buffer = new StringBuffer();
    }


    public StringBufferOutputStream(final Consumer<String> redirection, final String regexp)
    {
        this(redirection, Pattern.compile(regexp).asPredicate());
    }


    public StringBufferOutputStream(final Consumer<String> redirection)
    {
        this(redirection, NEW_LINE_PATTERN.asPredicate());
    }


    @Override
    public void write(final int b) throws IOException
    {
        buffer.append(String.valueOf(new char[]
                        {(char)b}));
        if(condition.test(buffer.toString()))
        {
            flush();
        }
    }


    @Override
    public void flush() throws IOException
    {
        if(buffer.length() > 0)
        {
            redirection.accept(buffer.toString());
            buffer = new StringBuffer();
        }
    }


    @Override
    public void close() throws IOException
    {
        flush();
        buffer = null;
    }
}
