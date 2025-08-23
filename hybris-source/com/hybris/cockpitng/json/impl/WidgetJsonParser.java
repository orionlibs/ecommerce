/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.json.impl;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

public class WidgetJsonParser extends JsonParser
{
    private final JsonParser parser;


    public WidgetJsonParser(final JsonParser parser)
    {
        this.parser = parser;
    }


    public WidgetInstanceManager getWidgetInstanceManager()
    {
        return getCodec() instanceof WidgetObjectMapper ? ((WidgetObjectMapper)getCodec()).getWidgetInstanceManager() : null;
    }


    @Override
    public ObjectCodec getCodec()
    {
        return parser.getCodec();
    }


    @Override
    public void setCodec(final ObjectCodec objectCodec)
    {
        parser.setCodec(objectCodec);
    }


    @Override
    public void setSchema(final FormatSchema schema)
    {
        parser.setSchema(schema);
    }


    @Override
    public boolean canUseSchema(final FormatSchema schema)
    {
        return parser.canUseSchema(schema);
    }


    @Override
    public Version version()
    {
        return parser.version();
    }


    @Override
    public Object getInputSource()
    {
        return parser.getInputSource();
    }


    @Override
    public void close() throws IOException
    {
        parser.close();
    }


    @Override
    public int releaseBuffered(final OutputStream out) throws IOException
    {
        return parser.releaseBuffered(out);
    }


    @Override
    public int releaseBuffered(final Writer w) throws IOException
    {
        return parser.releaseBuffered(w);
    }


    @Override
    public JsonParser enable(final Feature f)
    {
        return parser.enable(f);
    }


    @Override
    public JsonParser disable(final Feature f)
    {
        return parser.disable(f);
    }


    @Override
    public JsonParser configure(final Feature f, final boolean state)
    {
        return parser.configure(f, state);
    }


    @Override
    public boolean isEnabled(final Feature f)
    {
        return parser.isEnabled(f);
    }


    @Override
    public JsonToken nextToken() throws IOException
    {
        return parser.nextToken();
    }


    @Override
    public JsonToken nextValue() throws IOException
    {
        return parser.nextValue();
    }


    @Override
    public boolean nextFieldName(final SerializableString str) throws IOException
    {
        return parser.nextFieldName(str);
    }


    @Override
    public String nextTextValue() throws IOException
    {
        return parser.nextTextValue();
    }


    @Override
    public int nextIntValue(final int defaultValue) throws IOException
    {
        return parser.nextIntValue(defaultValue);
    }


    @Override
    public long nextLongValue(final long defaultValue) throws IOException
    {
        return parser.nextLongValue(defaultValue);
    }


    @Override
    public Boolean nextBooleanValue() throws IOException
    {
        return parser.nextBooleanValue();
    }


    @Override
    public JsonParser skipChildren() throws IOException
    {
        return parser.skipChildren();
    }


    @Override
    public boolean isClosed()
    {
        return parser.isClosed();
    }


    @Override
    public JsonToken getCurrentToken()
    {
        return parser.getCurrentToken();
    }


    @Override
    public int getCurrentTokenId()
    {
        return parser.getCurrentTokenId();
    }


    @Override
    public boolean hasCurrentToken()
    {
        return parser.hasCurrentToken();
    }


    @Override
    public boolean hasTokenId(final int id)
    {
        return parser.hasTokenId(id);
    }


    @Override
    public boolean hasToken(final JsonToken t)
    {
        return parser.hasToken(t);
    }


    @Override
    public void clearCurrentToken()
    {
        parser.clearCurrentToken();
    }


    @Override
    public String getCurrentName() throws IOException
    {
        return parser.getCurrentName();
    }


    @Override
    public JsonStreamContext getParsingContext()
    {
        return parser.getParsingContext();
    }


    @Override
    public JsonLocation getTokenLocation()
    {
        return parser.getTokenLocation();
    }


    @Override
    public JsonLocation getCurrentLocation()
    {
        return parser.getCurrentLocation();
    }


    @Override
    public JsonToken getLastClearedToken()
    {
        return parser.getLastClearedToken();
    }


    @Override
    public void overrideCurrentName(final String name)
    {
        parser.overrideCurrentName(name);
    }


    @Override
    public boolean isExpectedStartArrayToken()
    {
        return parser.isExpectedStartArrayToken();
    }


    @Override
    public String getText() throws IOException
    {
        return parser.getText();
    }


    @Override
    public char[] getTextCharacters() throws IOException
    {
        return parser.getTextCharacters();
    }


    @Override
    public int getTextLength() throws IOException
    {
        return parser.getTextLength();
    }


    @Override
    public int getTextOffset() throws IOException
    {
        return parser.getTextOffset();
    }


    @Override
    public boolean hasTextCharacters()
    {
        return parser.hasTextCharacters();
    }


    @Override
    public Number getNumberValue() throws IOException
    {
        return parser.getNumberValue();
    }


    @Override
    public NumberType getNumberType() throws IOException
    {
        return parser.getNumberType();
    }


    @Override
    public byte getByteValue() throws IOException
    {
        return parser.getByteValue();
    }


    @Override
    public short getShortValue() throws IOException
    {
        return parser.getShortValue();
    }


    @Override
    public int getIntValue() throws IOException
    {
        return parser.getIntValue();
    }


    @Override
    public long getLongValue() throws IOException
    {
        return parser.getLongValue();
    }


    @Override
    public BigInteger getBigIntegerValue() throws IOException
    {
        return parser.getBigIntegerValue();
    }


    @Override
    public float getFloatValue() throws IOException
    {
        return parser.getFloatValue();
    }


    @Override
    public double getDoubleValue() throws IOException
    {
        return parser.getDoubleValue();
    }


    @Override
    public BigDecimal getDecimalValue() throws IOException
    {
        return parser.getDecimalValue();
    }


    @Override
    public boolean getBooleanValue() throws IOException
    {
        return parser.getBooleanValue();
    }


    @Override
    public Object getEmbeddedObject() throws IOException
    {
        return parser.getEmbeddedObject();
    }


    @Override
    public byte[] getBinaryValue(final Base64Variant base64Variant) throws IOException
    {
        return parser.getBinaryValue(base64Variant);
    }


    @Override
    public byte[] getBinaryValue() throws IOException
    {
        return parser.getBinaryValue();
    }


    @Override
    public int getValueAsInt() throws IOException
    {
        return parser.getValueAsInt();
    }


    @Override
    public int getValueAsInt(final int defaultValue) throws IOException
    {
        return parser.getValueAsInt(defaultValue);
    }


    @Override
    public long getValueAsLong() throws IOException
    {
        return parser.getValueAsLong();
    }


    @Override
    public long getValueAsLong(final long defaultValue) throws IOException
    {
        return parser.getValueAsLong(defaultValue);
    }


    @Override
    public double getValueAsDouble() throws IOException
    {
        return parser.getValueAsDouble();
    }


    @Override
    public double getValueAsDouble(final double defaultValue) throws IOException
    {
        return parser.getValueAsDouble(defaultValue);
    }


    @Override
    public boolean getValueAsBoolean() throws IOException
    {
        return parser.getValueAsBoolean();
    }


    @Override
    public boolean getValueAsBoolean(final boolean defaultValue) throws IOException
    {
        return parser.getValueAsBoolean(defaultValue);
    }


    @Override
    public String getValueAsString(final String def) throws IOException
    {
        return parser.getValueAsString(def);
    }


    @Override
    public <T> T readValueAs(final Class<T> valueType) throws IOException
    {
        if(getCodec() instanceof WidgetObjectMapper)
        {
            final JsonNode node = readValueAsTree();
            final String json = ((WidgetObjectMapper)getCodec()).toJSONString(node);
            return ((WidgetObjectMapper)getCodec()).fromJSONString(json, valueType);
        }
        else
        {
            return parser.readValueAs(valueType);
        }
    }


    @Override
    public <T> T readValueAs(final TypeReference<?> valueTypeRef) throws IOException
    {
        return parser.readValueAs(valueTypeRef);
    }


    @Override
    public <T> Iterator<T> readValuesAs(final Class<T> valueType) throws IOException
    {
        return parser.readValuesAs(valueType);
    }


    @Override
    public <T> Iterator<T> readValuesAs(final TypeReference<T> valueTypeRef) throws IOException
    {
        return parser.readValuesAs(valueTypeRef);
    }


    @Override
    public JsonNode readValueAsTree() throws IOException
    {
        return parser.readValueAsTree();
    }
}
