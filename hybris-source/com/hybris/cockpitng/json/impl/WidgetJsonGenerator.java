/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.json.impl;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

public class WidgetJsonGenerator extends JsonGenerator
{
    private final JsonGenerator generator;


    public WidgetJsonGenerator(final JsonGenerator generator)
    {
        this.generator = generator;
    }


    @Override
    public void setSchema(final FormatSchema schema)
    {
        generator.setSchema(schema);
    }


    @Override
    public boolean canUseSchema(final FormatSchema schema)
    {
        return generator.canUseSchema(schema);
    }


    @Override
    public Version version()
    {
        return generator.version();
    }


    @Override
    public Object getOutputTarget()
    {
        return generator.getOutputTarget();
    }


    @Override
    public JsonGenerator enable(final Feature feature)
    {
        return generator.enable(feature);
    }


    @Override
    public JsonGenerator disable(final Feature feature)
    {
        return generator.disable(feature);
    }


    @Override
    public boolean isEnabled(final Feature feature)
    {
        return generator.isEnabled(feature);
    }


    @Override
    public int getFeatureMask()
    {
        return generator.getFeatureMask();
    }


    @Override
    public JsonGenerator setFeatureMask(final int values)
    {
        return generator.setFeatureMask(values);
    }


    @Override
    public JsonGenerator setCodec(final ObjectCodec objectCodec)
    {
        return generator.setCodec(objectCodec);
    }


    @Override
    public ObjectCodec getCodec()
    {
        return generator.getCodec();
    }


    @Override
    public JsonGenerator setPrettyPrinter(final PrettyPrinter pp)
    {
        return generator.setPrettyPrinter(pp);
    }


    @Override
    public JsonGenerator useDefaultPrettyPrinter()
    {
        return generator.useDefaultPrettyPrinter();
    }


    @Override
    public JsonGenerator setHighestNonEscapedChar(final int charCode)
    {
        return generator.setHighestNonEscapedChar(charCode);
    }


    @Override
    public int getHighestEscapedChar()
    {
        return generator.getHighestEscapedChar();
    }


    @Override
    public CharacterEscapes getCharacterEscapes()
    {
        return generator.getCharacterEscapes();
    }


    @Override
    public JsonGenerator setCharacterEscapes(final CharacterEscapes esc)
    {
        return generator.setCharacterEscapes(esc);
    }


    @Override
    public void writeStartArray() throws IOException
    {
        generator.writeStartArray();
    }


    @Override
    public void writeEndArray() throws IOException
    {
        generator.writeEndArray();
    }


    @Override
    public void writeStartObject() throws IOException
    {
        generator.writeStartObject();
    }


    @Override
    public void writeEndObject() throws IOException
    {
        generator.writeEndObject();
    }


    @Override
    public void writeFieldName(final String s) throws IOException
    {
        generator.writeFieldName(s);
    }


    /**
     * @deprecated since 1811, use {@link #writeFieldName(SerializableString)} instead
     */
    @Deprecated(since = "1811", forRemoval = true)
    public void writeFieldName(final SerializedString name) throws IOException
    {
        generator.writeFieldName(name);
    }


    @Override
    public void writeFieldName(final SerializableString name) throws IOException
    {
        generator.writeFieldName(name);
    }


    @Override
    public void writeString(final String s) throws IOException
    {
        generator.writeString(s);
    }


    @Override
    public void writeString(final char[] chars, final int i, final int i1) throws IOException
    {
        generator.writeString(chars, i, i1);
    }


    @Override
    public void writeString(final SerializableString text) throws IOException
    {
        generator.writeString(text);
    }


    @Override
    public void writeRawUTF8String(final byte[] bytes, final int i, final int i1) throws IOException
    {
        generator.writeRawUTF8String(bytes, i, i1);
    }


    @Override
    public void writeUTF8String(final byte[] bytes, final int i, final int i1) throws IOException
    {
        generator.writeUTF8String(bytes, i, i1);
    }


    @Override
    public void writeRaw(final String s) throws IOException
    {
        generator.writeRaw(s);
    }


    @Override
    public void writeRaw(final String s, final int i, final int i1) throws IOException
    {
        generator.writeRaw(s, i, i1);
    }


    @Override
    public void writeRaw(final char[] chars, final int i, final int i1) throws IOException
    {
        generator.writeRaw(chars, i, i1);
    }


    @Override
    public void writeRaw(final char c) throws IOException
    {
        generator.writeRaw(c);
    }


    @Override
    public void writeRawValue(final String s) throws IOException
    {
        generator.writeRawValue(s);
    }


    @Override
    public void writeRawValue(final String s, final int i, final int i1) throws IOException
    {
        generator.writeRawValue(s, i, i1);
    }


    @Override
    public void writeRawValue(final char[] chars, final int i, final int i1) throws IOException
    {
        generator.writeRawValue(chars, i, i1);
    }


    @Override
    public void writeBinary(final Base64Variant base64Variant, final byte[] bytes, final int i, final int i1) throws IOException
    {
        generator.writeBinary(base64Variant, bytes, i, i1);
    }


    @Override
    public void writeBinary(final byte[] data, final int offset, final int len) throws IOException
    {
        generator.writeBinary(data, offset, len);
    }


    @Override
    public void writeBinary(final byte[] data) throws IOException
    {
        generator.writeBinary(data);
    }


    @Override
    public int writeBinary(final Base64Variant bv, final InputStream data, final int dataLength) throws IOException
    {
        return generator.writeBinary(bv, data, dataLength);
    }


    @Override
    public void writeNumber(final int i) throws IOException
    {
        generator.writeNumber(i);
    }


    @Override
    public void writeNumber(final long l) throws IOException
    {
        generator.writeNumber(l);
    }


    @Override
    public void writeNumber(final BigInteger bigInteger) throws IOException
    {
        generator.writeNumber(bigInteger);
    }


    @Override
    public void writeNumber(final double v) throws IOException
    {
        generator.writeNumber(v);
    }


    @Override
    public void writeNumber(final float v) throws IOException
    {
        generator.writeNumber(v);
    }


    @Override
    public void writeNumber(final BigDecimal bigDecimal) throws IOException
    {
        generator.writeNumber(bigDecimal);
    }


    @Override
    public void writeNumber(final String s) throws IOException, UnsupportedOperationException
    {
        generator.writeNumber(s);
    }


    @Override
    public void writeBoolean(final boolean b) throws IOException
    {
        generator.writeBoolean(b);
    }


    @Override
    public void writeNull() throws IOException
    {
        generator.writeNull();
    }


    @Override
    public void writeObject(final Object o) throws IOException
    {
        if(getCodec() instanceof WidgetObjectMapper)
        {
            final String json = ((WidgetObjectMapper)getCodec()).toJSONString(o);
            final JsonNode node = ((WidgetObjectMapper)getCodec()).fromJSONString(json, JsonNode.class);
            writeTree(node);
        }
        else
        {
            generator.writeObject(o);
        }
    }


    /**
     * @deprecated since 1811, use {@link #writeTree(TreeNode)} instead
     */
    @Deprecated(since = "1811", forRemoval = true)
    public void writeTree(final JsonNode jsonNode) throws IOException
    {
        writeTree((TreeNode)jsonNode);
    }


    @Override
    public void writeTree(final TreeNode rootNode) throws IOException
    {
        generator.writeTree(rootNode);
    }


    @Override
    public void writeStringField(final String fieldName, final String value) throws IOException
    {
        generator.writeStringField(fieldName, value);
    }


    @Override
    public void copyCurrentEvent(final JsonParser jsonParser) throws IOException
    {
        generator.copyCurrentEvent(jsonParser);
    }


    @Override
    public void copyCurrentStructure(final JsonParser jsonParser) throws IOException
    {
        generator.copyCurrentStructure(jsonParser);
    }


    @Override
    public JsonStreamContext getOutputContext()
    {
        return generator.getOutputContext();
    }


    @Override
    public void flush() throws IOException
    {
        generator.flush();
    }


    @Override
    public boolean isClosed()
    {
        return generator.isClosed();
    }


    @Override
    public void close() throws IOException
    {
        generator.close();
    }
}
