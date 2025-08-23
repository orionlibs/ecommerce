/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Represents options for group styling.
 */
@JsonSerialize(using = Group.GroupSerializer.class)
@JsonDeserialize(using = Group.GroupDeserializer.class)
public class Group
{
    public static final String USE_DEFAULT_GROUPS_FIELD_NAME = "useDefaultGroups";
    /**
     * Indicates whether default groups introduced by vis.js should be taken into account. Default value is true.
     */
    private final Boolean useDefaultGroups;
    /**
     * Configuration for list of groups. A key should indicate group name, and a value should represent configuration for
     * given group. The configuration for nodes group is represented by {@link Node} object.
     */
    private final Map<String, Node> groups;


    @JsonCreator
    protected Group(@JsonProperty("useDefaultGroups") final Boolean useDefaultGroups,
                    @JsonProperty("groups") final Map<String, Node> groups)
    {
        this.useDefaultGroups = useDefaultGroups;
        this.groups = groups;
    }


    public Boolean getUseDefaultGroups()
    {
        return useDefaultGroups;
    }


    public Map<String, Node> getGroups()
    {
        return groups;
    }


    public static class Builder
    {
        private Boolean useDefaultGroups;
        private Map<String, Node> groups;


        /**
         * Indicates whether default groups introduced by vis.js should be taken into account. Default value is true.
         */
        public Builder withUseDefaultGroups(final Boolean useDefaultGroups)
        {
            this.useDefaultGroups = useDefaultGroups;
            return this;
        }


        /**
         * Configuration for list of groups. A key should indicate group name, and a value should represent configuration
         * for given group. The configuration for nodes group is represented by {@link Node}  object.
         */
        public Builder withGroups(final Map<String, Node> groups)
        {
            this.groups = groups;
            return this;
        }


        public Builder addGroup(final String groupName, final Node node)
        {
            if(groups == null)
            {
                groups = new HashMap<>();
            }
            groups.put(groupName, node);
            return this;
        }


        public Group build()
        {
            return new Group(useDefaultGroups, groups);
        }
    }


    public static class GroupSerializer extends JsonSerializer<Group>
    {
        @Override
        public void serialize(final Group value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException
        {
            jgen.writeStartObject();
            jgen.writeObjectField(USE_DEFAULT_GROUPS_FIELD_NAME, value.getUseDefaultGroups());
            for(final Map.Entry<String, Node> entry : value.getGroups().entrySet())
            {
                jgen.writeObjectField(entry.getKey(), entry.getValue());
            }
            jgen.writeEndObject();
        }
    }


    public static class GroupDeserializer extends JsonDeserializer<Group>
    {
        @Override
        public Group deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
                        throws IOException
        {
            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode mainNode = jsonParser.getCodec().readTree(jsonParser);
            final Iterator<String> fieldNames = mainNode.fieldNames();
            final Builder builder = new Builder();
            while(fieldNames.hasNext())
            {
                final String fieldName = fieldNames.next();
                final JsonNode jsonNode = mainNode.get(fieldName);
                if(USE_DEFAULT_GROUPS_FIELD_NAME.equals(fieldName))
                {
                    builder.withUseDefaultGroups(jsonNode.booleanValue());
                }
                else
                {
                    final Node mappedNode = mapper.readValue(jsonNode.asText(), Node.class);
                    builder.addGroup(fieldName, mappedNode);
                }
            }
            return builder.build();
        }
    }
}
