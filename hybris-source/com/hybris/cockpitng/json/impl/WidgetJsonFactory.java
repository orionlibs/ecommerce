/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.json.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class WidgetJsonFactory extends MappingJsonFactory
{
    @Override
    public JsonFactory setCodec(final ObjectCodec oc)
    {
        return super.setCodec(oc);
    }


    public WidgetInstanceManager getWidgetInstanceManager()
    {
        return getCodec() instanceof WidgetObjectMapper ? ((WidgetObjectMapper)getCodec()).getWidgetInstanceManager() : null;
    }


    protected WidgetJsonParser createJsonParser(final JsonParser parser)
    {
        return new WidgetJsonParser(parser);
    }


    /**
     * @deprecated since 1811, use {@link #_createParser(InputStream, IOContext)} instead
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected JsonParser _createJsonParser(final InputStream in, final IOContext ctxt) throws IOException
    {
        return _createParser(in, ctxt);
    }


    @Override
    protected JsonParser _createParser(final InputStream in, final IOContext ctxt) throws IOException
    {
        return createJsonParser(super._createParser(in, ctxt));
    }


    /**
     * @deprecated since 1811, use {@link #_createParser(Reader, IOContext)} instead
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected JsonParser _createJsonParser(final Reader r, final IOContext ctxt) throws IOException
    {
        return _createParser(r, ctxt);
    }


    @Override
    protected JsonParser _createParser(final Reader r, final IOContext ctxt) throws IOException
    {
        return createJsonParser(super._createParser(r, ctxt));
    }


    /**
     * @deprecated since 1811, use {@link #_createParser(byte[], int, int, IOContext)} instead
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected JsonParser _createJsonParser(final byte[] data, final int offset, final int len, final IOContext ctxt)
                    throws IOException
    {
        return _createParser(data, offset, len, ctxt);
    }


    @Override
    protected JsonParser _createParser(final byte[] data, final int offset, final int len, final IOContext ctxt)
                    throws IOException
    {
        return createJsonParser(super._createParser(data, offset, len, ctxt));
    }


    protected JsonGenerator createJsonGenerator(final JsonGenerator generator)
    {
        return new WidgetJsonGenerator(generator);
    }


    /**
     * @deprecated since 1811, use {@link #_createGenerator(Writer, IOContext)} instead
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected JsonGenerator _createJsonGenerator(final Writer out, final IOContext ctxt) throws IOException
    {
        return _createGenerator(out, ctxt);
    }


    @Override
    protected JsonGenerator _createGenerator(final Writer out, final IOContext ctxt) throws IOException
    {
        return createJsonGenerator(super._createGenerator(out, ctxt));
    }


    /**
     * @deprecated since 1811, use {@link #_createUTF8Generator(OutputStream, IOContext)} instead
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected JsonGenerator _createUTF8JsonGenerator(final OutputStream out, final IOContext ctxt) throws IOException
    {
        return _createUTF8Generator(out, ctxt);
    }


    @Override
    protected JsonGenerator _createUTF8Generator(final OutputStream out, final IOContext ctxt) throws IOException
    {
        return createJsonGenerator(super._createUTF8Generator(out, ctxt));
    }
}
