/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.integral;

import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.FormattableNumberEditor;
import com.hybris.cockpitng.editors.impl.AbstractTextBasedEditorRenderer;
import java.util.Arrays;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.impl.InputElement;

/**
 * @param <T>
 *           supported java.lang.Number subclasses: Byte, Short, Integer, Long
 */
public abstract class AbstractIntegralEditor<T extends Number> extends AbstractTextBasedEditorRenderer<T> implements
                FormattableNumberEditor<T>
{
    private static final List<Class> SUPPORTED_CLASSES = Arrays.asList(Long.class, Integer.class, Short.class, Byte.class);
    private final Class<T> valueType;
    private final long minValue;
    private final long maxValue;


    public AbstractIntegralEditor(final Class<T> valueType, final long minValue, final long maxValue)
    {
        this.valueType = valueType;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }


    public Class<T> getValueType()
    {
        return valueType;
    }


    public long getMinValue()
    {
        return minValue;
    }


    public long getMaxValue()
    {
        return maxValue;
    }


    @Override
    protected void setRawValue(final InputElement editorView, final T rawValue)
    {
        editorView.setRawValue(convertToLong(rawValue));
    }


    @Override
    protected T getRawValue(final InputElement editorView)
    {
        return (T)editorView.getRawValue();
    }


    @Override
    protected T coerceFromString(final InputElement editorView, final String text) throws WrongValueException
    {
        final Long longValue = ((InternalLongbox)editorView).coerceFromString(text);
        validateRange(editorView, longValue);
        return convertFromNumber(longValue);
    }


    @Override
    public void render(final Component parent, final EditorContext<T> context, final EditorListener<T> listener)
    {
        final InternalLongbox editorView = new InternalLongbox();
        editorView.setConstraint((comp, value) -> {
            final Long longValue = (Long)value;
            validateRange(comp, longValue);
        });
        editorView.setValue(convertToLong(context.getInitialValue()));
        initViewComponent(editorView, context, listener);
        editorView.setParent(parent);
    }


    private Long convertToLong(final T number)
    {
        Long ret = null;
        if((number != null) && SUPPORTED_CLASSES.contains(number.getClass()))
        {
            ret = Long.valueOf(number.longValue());
        }
        return ret;
    }


    private T convertFromNumber(final Number value)
    {
        Number ret = null;
        if(value != null)
        {
            if(valueType.equals(Byte.class))
            {
                ret = value.byteValue();
            }
            else if(valueType.equals(Short.class))
            {
                ret = value.shortValue();
            }
            else if(valueType.equals(Integer.class))
            {
                ret = value.intValue();
            }
            else if(valueType.equals(Long.class))
            {
                ret = value.longValue();
            }
        }
        return (T)ret;
    }


    private void validateRange(final Component component, final Long longValue)
    {
        if((longValue != null) && (longValue < minValue || longValue > maxValue))
        {
            throw new WrongValueException(component, "Value [" + longValue + "] is out of range: [" + minValue + ";" + maxValue
                            + "]");
        }
    }


    private class InternalLongbox extends Longbox
    {
        protected static final int DEFAULT_INTEGER_VALUE = 0;


        @Override
        public Long coerceFromString(final String value) throws WrongValueException
        {
            return (Long)super.coerceFromString(value);
        }


        @Override
        public Object getRawValue()
        {
            final Object rawValue = super.getRawValue();
            if(isPrimitive() && null == rawValue)
            {
                return DEFAULT_INTEGER_VALUE;
            }
            if(rawValue instanceof Number)
            {
                return AbstractIntegralEditor.this.convertFromNumber((Number)rawValue);
            }
            if(rawValue != null)
            {
                throw new IllegalStateException("Expected " + Number.class.getCanonicalName() + " but got "
                                + rawValue.getClass().getCanonicalName());
            }
            return null;
        }
    }
}
