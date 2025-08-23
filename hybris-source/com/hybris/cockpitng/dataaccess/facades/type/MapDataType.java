/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.type;

import org.apache.commons.lang3.Validate;

/**
 *
 *
 */
public class MapDataType extends DataType
{
    private final DataType keyType;
    private final DataType valueType;


    protected MapDataType(final MapBuilder builder)
    {
        super(builder);
        this.keyType = builder.keyType;
        this.valueType = builder.valueType;
    }


    public DataType getKeyType()
    {
        return keyType;
    }


    public DataType getValueType()
    {
        return valueType;
    }


    public static class MapBuilder extends Builder
    {
        private DataType keyType;
        private DataType valueType;


        public MapBuilder(final String typeCode)
        {
            super(typeCode);
            MapBuilder.this.type(Type.MAP);
        }


        public MapBuilder keyType(final DataType keyType)
        {
            this.keyType = keyType;
            return this;
        }


        public MapBuilder valueType(final DataType valueType)
        {
            this.valueType = valueType;
            return this;
        }


        @Override
        public Builder type(final Type type)
        {
            Validate.isTrue(Type.MAP.equals(type), "MapBuilder allows only MAP type");
            return super.type(type);
        }


        @Override
        public DataType build()
        {
            return new MapDataType(this);
        }
    }
}
