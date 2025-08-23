/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.type;

import java.util.Arrays;
import org.apache.commons.lang3.Validate;

/**
 *
 *
 */
public class CollectionDataType extends DataType
{
    private final DataType valueType;


    protected CollectionDataType(final CollectionBuilder builder)
    {
        super(builder);
        this.valueType = builder.valueType;
    }


    public DataType getValueType()
    {
        return valueType;
    }


    public static class CollectionBuilder extends Builder
    {
        private DataType valueType;


        public CollectionBuilder(final String typeCode)
        {
            this(typeCode, Type.COLLECTION);
        }


        public CollectionBuilder(final String typeCode, final Type type)
        {
            super(typeCode);
            CollectionBuilder.this.type(type);
        }


        @Override
        public Builder type(final Type type)
        {
            Validate.isTrue(Arrays.asList(Type.COLLECTION, Type.LIST, Type.SET).contains(type), "CollectionBuilder allows only one of types: COLLECTION, LIST, SET");
            return super.type(type);
        }


        public CollectionBuilder valueType(final DataType valueType)
        {
            this.valueType = valueType;
            return this;
        }


        @Override
        public DataType build()
        {
            return new CollectionDataType(this);
        }
    }
}
