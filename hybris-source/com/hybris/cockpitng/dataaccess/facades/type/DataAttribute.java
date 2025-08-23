/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.type;

import com.google.common.collect.Maps;
import com.hybris.cockpitng.core.util.Validate;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class holding meta information about an attribute. See {@link DataType} for further information.
 */
public class DataAttribute
{
    private final static Logger LOGGER = LoggerFactory.getLogger(DataAttribute.class);
    private final boolean mandatory;
    private final String qualifier;
    private final boolean unique;
    private final DataType valueType;
    private final boolean localized;
    private final boolean writable;
    private final boolean searchable;
    private final boolean writableOnCreation;
    private final boolean ordered;
    private final boolean partOf;
    private final boolean primitive;
    private final boolean writeThrough;
    private final String selectionOf;
    private final Object defaultValue;
    private final boolean encrypted;
    private final boolean variantAttribute;
    private final boolean runtimeAttribute;
    private final MapType mapType;
    private final Map<Locale, String> labels;
    private final Map<Locale, String> descriptions;


    private DataAttribute(final Builder builder)
    {
        super();
        this.mandatory = builder.mandatoryField;
        this.qualifier = builder.qualifierField;
        this.unique = builder.uniqueField;
        this.valueType = builder.valueTypeField;
        this.mapType = builder.mapTypeField;
        this.writable = builder.writableField;
        this.localized = builder.localizedField;
        this.writableOnCreation = builder.writableOnCreationField;
        this.labels = builder.labelsField;
        this.searchable = builder.searchableField;
        this.descriptions = builder.descriptionsField;
        this.ordered = builder.orderedField;
        this.partOf = builder.partOfField;
        this.variantAttribute = builder.variantAttribute;
        this.primitive = builder.primitiveField;
        this.writeThrough = builder.writeThrough;
        this.selectionOf = builder.selectionOf;
        this.defaultValue = builder.defaultValue;
        this.encrypted = builder.encrypted;
        this.runtimeAttribute = builder.runtimeAttribute;
    }


    /**
     * @return whether search can be performed against this attribute
     */
    public boolean isSearchable()
    {
        return searchable;
    }


    /**
     * @return true, if this attribute must have a non-null value.
     */
    public boolean isMandatory()
    {
        return this.mandatory;
    }


    /**
     * @return the qualifier of this attribute.
     */
    public String getQualifier()
    {
        return this.qualifier;
    }


    /**
     * @return true, if this attribute is unique.
     */
    public boolean isUnique()
    {
        return this.unique;
    }


    /**
     * @return actual attribute's type definition
     * @see #getValueType()
     */
    public DataType getDefinedType()
    {
        return this.valueType;
    }


    /**
     * @return element type, if attribute is collection, value type if attribute is map or defined type otherwise
     */
    public DataType getValueType()
    {
        switch(getDefinedType().getType())
        {
            case SET:
            case COLLECTION:
            case LIST:
                return ((CollectionDataType)this.valueType).getValueType();
            case MAP:
                return ((MapDataType)this.valueType).getValueType();
            case COMPOUND:
            case ENUM:
            case ATOMIC:
            case RANGE:
            default:
                return this.valueType;
        }
    }


    /**
     * @return true, if this attribute is localized, i.e. it's value could be something like a {@link Map} with keytype
     *         {@link Locale}.
     */
    public boolean isLocalized()
    {
        return this.localized;
    }


    /**
     * @return true, if this attribute is writable.
     */
    public boolean isWritable()
    {
        return this.writable;
    }


    /**
     * @return true, if this attribute is writable on creation, e.g. it could be in general not writable but is
     *         mandatory. So one can set it only once.
     */
    public boolean isWritableOnCreation()
    {
        return this.writableOnCreation;
    }


    /**
     * @return true, if this attribute is ordered, e.g. in case of Collection, when elements order can be changed
     */
    public boolean isOrdered()
    {
        return ordered;
    }


    /**
     * @return true if the attribute is a variant attribute, defaults to false and all type systems that do not provide
     *         variant attributes should return the default value
     */
    public boolean isVariantAttribute()
    {
        return variantAttribute;
    }


    /**
     * @return true if the attribute is a runtime attribute, defaults to false
     */
    public boolean isRuntimeAttribute()
    {
        return runtimeAttribute;
    }


    /**
     * @return additional information for this attribute wrapped in an instance of {@link AttributeType}.
     */
    public AttributeType getAttributeType()
    {
        switch(valueType.getType())
        {
            case COLLECTION:
                return AttributeType.COLLECTION;
            case LIST:
                return AttributeType.LIST;
            case MAP:
                return AttributeType.MAP;
            case RANGE:
                return AttributeType.RANGE;
            case SET:
                return AttributeType.SET;
            case ATOMIC:
            case ENUM:
            case COMPOUND:
            default:
                return AttributeType.SINGLE;
        }
    }


    /**
     * @return the {@link MapType} for this attribute, if it is of type {@link AttributeType#MAP} or null otherwise.
     */
    public MapType getMapType()
    {
        return mapType;
    }


    /**
     * @return the label for the given locale.
     */
    public String getLabel(final Locale locale)
    {
        return getAllLabels().get(locale);
    }


    /**
     * Returns all labels for this attribute.
     */
    public Map<Locale, String> getAllLabels()
    {
        return labels == null ? Collections.<Locale, String>emptyMap() : labels;
    }


    /**
     * @return all descriptions for this attribute.
     */
    public Map<Locale, String> getAllDescriptions()
    {
        return descriptions;
    }


    /**
     * @return the description for the given locale.
     */
    public String getDescription(final Locale locale)
    {
        return getAllDescriptions().get(locale);
    }


    public boolean isPartOf()
    {
        return partOf;
    }


    public boolean isPrimitive()
    {
        return primitive;
    }


    /**
     * @return true if the act of setting the attribute value may lead to a side effect of persisting the entity. For
     *         example platform's jalo attributes behave this way.
     */
    public boolean isWriteThrough()
    {
        return writeThrough;
    }


    /**
     * @return the selectionOf property
     */
    public String getSelectionOf()
    {
        return selectionOf;
    }


    /**
     * @return the defaultValue property
     */
    public Object getDefaultValue()
    {
        return defaultValue;
    }


    public boolean isEncrypted()
    {
        return encrypted;
    }


    @Override
    public String toString()
    {
        return valueType != null ? String.format("%s(%s)", valueType.getCode(), qualifier) : qualifier;
    }


    /**
     * Enumeration for the different types of attributes.
     */
    public enum AttributeType
    {
        SINGLE, COLLECTION, LIST, SET, MAP, RANGE
    }


    /**
     * Map key and value type information.
     */
    public static class MapType
    {
        private final DataType keyType;
        private final DataType valueType;


        public MapType(final DataType keyType, final DataType valueType)
        {
            super();
            this.keyType = keyType;
            this.valueType = valueType;
        }


        public DataType getKeyType()
        {
            return keyType;
        }


        public DataType getValueType()
        {
            return valueType;
        }
    }


    /**
     * Builder for {@link DataAttribute}.
     */
    public static class Builder
    {
        private final String qualifierField;
        private boolean mandatoryField;
        private boolean writableField;
        private boolean writableOnCreationField;
        private boolean uniqueField;
        private boolean localizedField;
        private boolean searchableField;
        private boolean variantAttribute;
        private DataType valueTypeField;
        private boolean orderedField;
        private MapType mapTypeField;
        private Map<Locale, String> labelsField = Maps.newHashMap();
        private Map<Locale, String> descriptionsField = Maps.newHashMap();
        private boolean partOfField;
        private DataAttribute.AttributeType attributeType;
        private boolean primitiveField;
        private boolean writeThrough;
        private String selectionOf;
        private Object defaultValue;
        private boolean encrypted;
        private boolean runtimeAttribute;


        public Builder(final String qualifier)
        {
            Validate.notBlank("Qualifier may not be blank", qualifier);
            this.qualifierField = qualifier;
        }


        protected DataType ensureWrapped(final DataType type, final AttributeType attributeType, final DataType.Type dataType)
        {
            if(attributeType.equals(this.attributeType) && !dataType.equals(type.getType()))
            {
                LOGGER.error("Type {} is not of type {} but {}. Wrapping to {} will be done!", type.getCode(), attributeType,
                                type.getType(), dataType);
                return new CollectionDataType.CollectionBuilder(type.getCode() + dataType.name(), dataType).valueType(type).build();
            }
            else
            {
                return type;
            }
        }


        public Builder valueType(DataType type)
        {
            type = ensureWrapped(type, AttributeType.COLLECTION, DataType.Type.COLLECTION);
            type = ensureWrapped(type, AttributeType.LIST, DataType.Type.LIST);
            type = ensureWrapped(type, AttributeType.SET, DataType.Type.SET);
            this.valueTypeField = type;
            return this;
        }


        public Builder valueType(final MapType type)
        {
            this.mapTypeField = type;
            return this;
        }


        public Builder mandatory(final boolean mandatory)
        {
            this.mandatoryField = mandatory;
            return this;
        }


        public Builder searchable(final boolean searchable)
        {
            this.searchableField = searchable;
            return this;
        }


        public Builder localized(final boolean localized)
        {
            this.localizedField = localized;
            return this;
        }


        public Builder writable(final boolean writable)
        {
            this.writableField = writable;
            return this;
        }


        public Builder ordered(final boolean ordered)
        {
            this.orderedField = ordered;
            return this;
        }


        public Builder writableOnCreation(final boolean writableOnCreation)
        {
            this.writableOnCreationField = writableOnCreation;
            return this;
        }


        public Builder unique(final boolean unique)
        {
            this.uniqueField = unique;
            return this;
        }


        public Builder labels(final Map<Locale, String> labels)
        {
            labelsField = labels;
            return this;
        }


        public Builder label(final Locale locale, final String label)
        {
            labelsField.put(locale, label);
            return this;
        }


        public Builder descriptions(final Map<Locale, String> descriptions)
        {
            descriptionsField = descriptions;
            return this;
        }


        public Builder description(final Locale locale, final String description)
        {
            descriptionsField.put(locale, description);
            return this;
        }


        public Builder partOf(final boolean partOf)
        {
            this.partOfField = partOf;
            return this;
        }


        public Builder variantAttribute(final boolean variantAttribute)
        {
            this.variantAttribute = variantAttribute;
            return this;
        }


        public Builder primitive(final boolean primitive)
        {
            this.primitiveField = primitive;
            return this;
        }


        public Builder writeThrough(final boolean writeThrough)
        {
            this.writeThrough = writeThrough;
            return this;
        }


        public Builder selectionOf(final String selectionOf)
        {
            this.selectionOf = selectionOf;
            return this;
        }


        public Builder defaultValue(final Object defaultValue)
        {
            this.defaultValue = defaultValue;
            return this;
        }


        public Builder encrypted(final boolean encrypted)
        {
            this.encrypted = encrypted;
            return this;
        }


        public Builder runtimeAttribute(final boolean runtimeAttribute)
        {
            this.runtimeAttribute = runtimeAttribute;
            return this;
        }


        public DataAttribute build()
        {
            return new DataAttribute(this);
        }
    }
}
