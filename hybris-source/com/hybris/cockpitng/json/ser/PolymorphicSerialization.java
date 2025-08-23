/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.json.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * Class containing all tools and classes needed to serialize and deserialize polymorphic maps
 */
public final class PolymorphicSerialization
{
    private PolymorphicSerialization()
    {
        //Utility class
    }


    /**
     * Serializes provided object as a compound type (incl. class name).
     *
     * @param o
     *           object to be serialized (may be <code>null</code>)
     * @param jsonGenerator
     *           generator
     * @throws IOException
     *            thrown when object may not be serialized
     */
    public static void serializeCompound(final Object o, final JsonGenerator jsonGenerator) throws IOException
    {
        if(o == null)
        {
            jsonGenerator.writeNull();
        }
        else if(o instanceof String)
        {
            jsonGenerator.writeString(o.toString());
        }
        else if(o instanceof Map)
        {
            jsonGenerator.writeStartArray();
            jsonGenerator.writeString(o.getClass().getName());
            final Map<?, ?> map = (Map<?, ?>)o;
            for(final Map.Entry<?, ?> entry : map.entrySet())
            {
                serializeCompound(entry.getKey(), jsonGenerator);
                serializeCompound(entry.getValue(), jsonGenerator);
            }
            jsonGenerator.writeEndArray();
        }
        else if(o instanceof Collection)
        {
            jsonGenerator.writeStartArray();
            jsonGenerator.writeString(o.getClass().getName());
            for(final Object each : (Collection)o)
            {
                serializeCompound(each, jsonGenerator);
            }
            jsonGenerator.writeEndArray();
        }
        else
        {
            jsonGenerator.writeStartArray();
            jsonGenerator.writeString(o.getClass().getName());
            jsonGenerator.writeObject(o);
            jsonGenerator.writeEndArray();
        }
    }


    /**
     * Deserializes object from provided JSON node. An object may be <code>null</code>, {@link String} or compound value
     * in format of [class, value].
     *
     * @param node
     *           JSON node to be interpreted
     * @param deserializationContext
     *           context
     * @param <T>
     *           type of value to be read
     * @return value read
     * @throws IOException
     *            thrown when read is not possible
     */
    public static <T> T deserializeCompound(final JsonNode node, final DeserializationContext deserializationContext)
                    throws IOException
    {
        final T result;
        if(node.isNull())
        {
            result = null;
        }
        else if(node.isTextual())
        {
            result = (T)node.textValue();
        }
        else if(node.isArray() && node.size() > 0)
        {
            final String type = node.get(0).textValue();
            try
            {
                final Class<?> clazz = Class.forName(type);
                result = (T)deserializeCompound(clazz, node, 1, deserializationContext);
            }
            catch(final InstantiationException | IllegalAccessException | ClassNotFoundException e)
            {
                throw deserializationContext.instantiationException(Map.class, e);
            }
        }
        else
        {
            throw deserializationContext.mappingException("Unexpected data structure");
        }
        return result;
    }


    /**
     * Deserializes object of specified type using provided parser. Method assumes that parser is set on first token to
     * be read by method. It end execution leaving parser right after last read token.
     *
     * @param type
     *           expected type of value
     * @param deserializationContext
     *           context
     * @param <T>
     *           type of value to be read
     * @return value read
     * @throws IOException
     *            thrown when read is not possible
     */
    public static <T> T deserializeCompound(final Class<T> type, final JsonNode arrayNode, final int currentIndex,
                    final DeserializationContext deserializationContext) throws IllegalAccessException, InstantiationException, IOException
    {
        if(Map.class.isAssignableFrom(type))
        {
            final T result = type.newInstance();
            for(int idx = currentIndex; idx < arrayNode.size() - 1; idx += 2)
            {
                final Object key = deserializeCompound(arrayNode.get(idx), deserializationContext);
                final Object value = deserializeCompound(arrayNode.get(idx + 1), deserializationContext);
                ((Map)result).put(key, value);
            }
            return result;
        }
        else if(Collection.class.isAssignableFrom(type))
        {
            final T result = type.newInstance();
            for(int idx = currentIndex; idx < arrayNode.size(); idx++)
            {
                final Object value = deserializeCompound(arrayNode.get(idx), deserializationContext);
                ((Collection)result).add(value);
            }
            return result;
        }
        else
        {
            final ObjectCodec codec = deserializationContext.getParser().getCodec();
            return codec.treeAsTokens(arrayNode.get(currentIndex)).readValueAs(type);
        }
    }


    public static class Serializer extends StdSerializer<Object>
    {
        public Serializer()
        {
            super(Object.class);
        }


        @Override
        public void serialize(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider)
                        throws IOException
        {
            if(o != null)
            {
                serializeCompound(o, jsonGenerator);
            }
            else
            {
                jsonGenerator.writeNull();
            }
        }


        @Override
        public JsonNode getSchema(final SerializerProvider serializerProvider, final Type type) throws JsonMappingException
        {
            return createSchemaNode(type.getTypeName());
        }
    }


    public static class Deserializer extends StdDeserializer<Object>
    {
        protected Deserializer()
        {
            super(Object.class);
        }


        @Override
        public Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
                        throws IOException
        {
            final ObjectCodec codec = jsonParser.getCodec();
            final JsonNode node = codec.readTree(jsonParser);
            if(node.isNull())
            {
                return null;
            }
            else if(node.isTextual())
            {
                return node.textValue();
            }
            else if(node.isArray() && node.size() > 0)
            {
                final String type = node.get(0).textValue();
                try
                {
                    final Class<?> clazz = Class.forName(type);
                    return deserializeCompound(clazz, node, 1, deserializationContext);
                }
                catch(final InstantiationException | IllegalAccessException | ClassNotFoundException e)
                {
                    throw deserializationContext.instantiationException(Map.class, e);
                }
            }
            else
            {
                throw deserializationContext.mappingException("Unexpected data structure");
            }
        }
    }
}
