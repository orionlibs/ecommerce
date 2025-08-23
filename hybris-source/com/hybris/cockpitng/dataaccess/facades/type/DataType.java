/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.type;

import com.hybris.cockpitng.core.util.Validate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds meta information about a type, especially which attributes it has.
 */
public class DataType
{
    public static final DataType NULL = new Builder("null_object").type(Type.ATOMIC).build();
    public static final DataType NULL_COMPOUND = new Builder("null_compound").type(Type.COMPOUND).clazz(Object.class).build();
    public static final DataType BOOLEAN = new Builder(Boolean.class.getName()).type(Type.ATOMIC).clazz(Boolean.class).build();
    public static final DataType STRING = new Builder(String.class.getName()).type(Type.ATOMIC).clazz(String.class).build();
    public static final DataType BYTE = new Builder(Byte.class.getName()).type(Type.ATOMIC).clazz(Byte.class).build();
    public static final DataType CHARACTER = new Builder(Character.class.getName()).type(Type.ATOMIC).clazz(Character.class)
                    .build();
    public static final DataType SHORT = new Builder(Short.class.getName()).type(Type.ATOMIC).clazz(Short.class).build();
    public static final DataType INTEGER = new Builder(Integer.class.getName()).type(Type.ATOMIC).clazz(Integer.class).build();
    public static final DataType LONG = new Builder(Long.class.getName()).type(Type.ATOMIC).clazz(Long.class).build();
    public static final DataType FLOAT = new Builder(Float.class.getName()).type(Type.ATOMIC).clazz(Float.class).build();
    public static final DataType DOUBLE = new Builder(Double.class.getName()).type(Type.ATOMIC).clazz(Double.class).build();
    public static final DataType BIG_DECIMAL = new Builder(BigDecimal.class.getName()).type(Type.ATOMIC).clazz(BigDecimal.class)
                    .build();
    public static final DataType DATE = new Builder(Date.class.getName()).type(Type.ATOMIC).clazz(Date.class).build();
    private static final Logger LOG = LoggerFactory.getLogger(DataType.class);
    private final Class<?> clazz;
    private final String code;
    private final Map<String, DataAttribute> attributes = new LinkedHashMap<>();
    private final Type type;
    private final Map<Locale, String> labels;
    private final List<String> subtypes;
    private final String superType;
    private final boolean searchable;
    private final boolean abstractType;
    private final boolean singleton;
    private final List<String> allSuperTypes;


    protected DataType(final Builder builder)
    {
        super();
        code = builder.typeCode;
        clazz = builder.builderClazz;
        labels = builder.labelsField;
        subtypes = builder.subtypes;
        searchable = builder.searchable;
        superType = builder.superType;
        abstractType = builder.abstractType;
        allSuperTypes = builder.allSupertypes;
        singleton = builder.singleton;
        for(final DataAttribute attribute : builder.attributes)
        {
            attributes.put(attribute.getQualifier(), attribute);
        }
        if(!attributes.isEmpty() && (builder.type == null || Type.ATOMIC.equals(builder.type)))
        {
            type = Type.COMPOUND;
        }
        else if(builder.type != null)
        {
            type = builder.type;
        }
        else
        {
            type = Type.ATOMIC;
        }
    }


    /**
     * Returns the attributes this type provides, or an empty collection, if this type is atomic.
     */
    public Collection<DataAttribute> getAttributes()
    {
        return this.attributes.values();
    }


    /**
     * Returns the corresponding java {@link Class} for this type.
     */
    public Class getClazz()
    {
        return this.clazz;
    }


    /**
     * Returns the (unique) code of this type.
     */
    public String getCode()
    {
        return this.code;
    }


    /**
     * Returns the {@link DataAttribute} for the given qualifier or null, if not existing.
     */
    public DataAttribute getAttribute(final String qualifier)
    {
        if(!attributes.containsKey(qualifier))
        {
            for(final Map.Entry<String, DataAttribute> entry : attributes.entrySet())
            {
                if(StringUtils.equalsIgnoreCase(entry.getKey(), qualifier))
                {
                    LOG.warn("Qualifier [{}] not found. Case insenitive resolution have found matching qualifier [{}]", qualifier,
                                    entry.getKey());
                    return entry.getValue();
                }
            }
        }
        return attributes.get(qualifier);
    }


    /**
     * Returns true, if this Type is atomic (i.e. doesn't provide any attributes).
     */
    public boolean isAtomic()
    {
        return Type.ATOMIC.equals(type);
    }


    /**
     * Returns true, if this Type is an enumeration
     */
    public boolean isEnum()
    {
        return Type.ENUM.equals(type);
    }


    /**
     * @return Type's family
     */
    public Type getType()
    {
        return type;
    }


    /**
     * Returns the label for the given locale.
     */
    public String getLabel(final Locale locale)
    {
        return getAllLabels().get(locale);
    }


    /**
     * Returns all labels for this type.
     */
    public Map<Locale, String> getAllLabels()
    {
        return labels == null ? Collections.emptyMap() : labels;
    }


    /**
     * Returns a set of names of all available subtypes that directly inherit from the type (1-st level subtypes).
     */
    public List<String> getSubtypes()
    {
        return subtypes;
    }


    /**
     * @return true if the instances of the type may be found using search mechanism. For example database views may be
     *         declared as types but may not be a valid search target types.
     */
    public boolean isSearchable()
    {
        return searchable;
    }


    /**
     * @return Code of the supertype if exists, null otherwise.
     */
    public String getSuperType()
    {
        return superType;
    }


    /**
     * @return true if the type is abstract
     */
    public boolean isAbstract()
    {
        return abstractType;
    }


    /**
     * @return true if the type is a singleton ie. only one instance of the type is allowed
     */
    public boolean isSingleton()
    {
        return singleton;
    }


    public List<String> getAllSuperTypes()
    {
        return Collections.unmodifiableList(allSuperTypes);
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
        final DataType dataType = (DataType)o;
        return code.equals(dataType.code);
    }


    @Override
    public int hashCode()
    {
        return code.hashCode();
    }


    public enum Type
    {
        ATOMIC, COMPOUND, ENUM, COLLECTION, LIST, SET, MAP, RANGE
    }


    /**
     * Builder class for {@link DataType}.
     */
    public static class Builder
    {
        private final String typeCode;
        private final List<DataAttribute> attributes = new ArrayList<>();
        private final List<String> subtypes = new ArrayList<>();
        private List<String> allSupertypes = new ArrayList<>();
        private boolean searchable = true;
        private Class<?> builderClazz;
        private Type type;
        private Map<Locale, String> labelsField;
        private String superType;
        private boolean abstractType;
        private boolean singleton;


        public Builder(final String typeCode)
        {
            Validate.notBlank(typeCode, "Type code may not be blank");
            this.typeCode = typeCode;
        }


        public Builder(final String typeCode, final DataType parentType)
        {
            this(typeCode);
            this.attributes.addAll(parentType.getAttributes());
            this.subtypes.addAll(parentType.getSubtypes());
            this.allSupertypes.addAll(parentType.getAllSuperTypes());
            this.searchable = parentType.isSearchable();
            this.builderClazz = parentType.clazz;
            this.type = parentType.getType();
            this.labelsField = new HashMap<>();
            this.labelsField.putAll(parentType.getAllLabels());
            this.superType = parentType.getSuperType();
            this.abstractType = parentType.isAbstract();
        }


        public Builder type(final Type type)
        {
            this.type = type;
            return this;
        }


        public Builder abstractType(final boolean abstractType)
        {
            this.abstractType = abstractType;
            return this;
        }


        public Builder singleton(final boolean singleton)
        {
            this.singleton = singleton;
            return this;
        }


        public Builder searchable(final boolean searchable)
        {
            this.searchable = searchable;
            return this;
        }


        public Builder subtype(final String subtype)
        {
            this.subtypes.add(subtype);
            return this;
        }


        public Builder supertype(final String superType)
        {
            this.superType = superType;
            return this;
        }


        public Builder attribute(final DataAttribute attribute)
        {
            attributes.add(attribute);
            return this;
        }


        public Builder allSuperTypes(final List<String> allSupertypes)
        {
            this.allSupertypes = allSupertypes;
            return this;
        }


        /**
         * @deprecated since 5.6.0, use type(Type.ATOMIC)
         * @see #type(Type)
         */
        @Deprecated(since = "5.6.0", forRemoval = true)
        public Builder atomic(final boolean atomic)
        {
            if(atomic)
            {
                type(Type.ATOMIC);
            }
            return this;
        }


        public Builder labels(final Map<Locale, String> labels)
        {
            labelsField = labels;
            return this;
        }


        public Builder label(final Locale locale, final String label)
        {
            if(labelsField == null)
            {
                labelsField = new HashMap<>();
            }
            labelsField.put(locale, label);
            return this;
        }


        public Builder clazz(final Class<?> clazz)
        {
            this.builderClazz = clazz;
            if(type == null)
            {
                if(ClassUtils.isPrimitiveOrWrapper(clazz) || String.class.equals(clazz) || BigDecimal.class.equals(clazz)
                                || Date.class.equals(clazz))
                {
                    type(Type.ATOMIC);
                }
                else if(clazz.isEnum())
                {
                    type(Type.ENUM);
                }
                else
                {
                    type(Type.COMPOUND);
                }
            }
            return this;
        }


        public DataType build()
        {
            return new DataType(this);
        }
    }
}
