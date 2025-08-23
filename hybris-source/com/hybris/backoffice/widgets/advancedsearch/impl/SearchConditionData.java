/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Maps;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.json.ser.PolymorphicSerialization;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.HashMap;
import java.util.Locale;

public class SearchConditionData
{
    private ValueComparisonOperator operator;
    @JsonSerialize(using = PolymorphicSerialization.Serializer.class)
    @JsonDeserialize(using = PolymorphicSerialization.Deserializer.class)
    private Object value;
    private final FieldType fieldType;


    @JsonCreator
    public SearchConditionData(@JsonProperty("fieldType") final FieldType fieldType, @JsonProperty("value") final Object value,
                    @JsonProperty("operator") final ValueComparisonOperator operator)
    {
        this.fieldType = fieldType;
        this.value = value;
        this.operator = operator;
    }


    public SearchConditionData(final SearchConditionData searchConditionData)
    {
        this.fieldType = copyFieldType(searchConditionData.fieldType);
        this.value = searchConditionData.value;
        this.operator = searchConditionData.operator;
    }


    public FieldType getFieldType()
    {
        return fieldType;
    }


    public ValueComparisonOperator getOperator()
    {
        return operator;
    }


    public Object getValue()
    {
        return value;
    }


    public void updateLocalizedValue(final Locale locale, final Object localizedEditorValue)
    {
        final HashMap<Locale, Object> map = Maps.<Locale, Object>newHashMap();
        map.put(locale, localizedEditorValue);
        value = map;
    }


    private FieldType copyFieldType(final FieldType source)
    {
        final FieldType target = new FieldType();
        target.setDisabled(source.isDisabled());
        target.setName(source.getName());
        target.setPosition(source.getPosition());
        target.setEditor(source.getEditor());
        target.setMandatory(source.isMandatory());
        target.setMergeMode(source.getMergeMode());
        target.setOperator(source.getOperator());
        target.setSelected(source.isSelected());
        target.setSortable(source.isSortable());
        target.getEditorParameter().addAll(source.getEditorParameter());
        return target;
    }


    public void resetLocalizedValues()
    {
        updateValue(null);
    }


    public void updateValue(final Object value)
    {
        this.value = value;
    }


    public void updateOperator(final ValueComparisonOperator operator)
    {
        this.operator = operator;
    }
}
