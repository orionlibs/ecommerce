/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.decimal;

import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.FormattableNumberEditor;
import com.hybris.cockpitng.editors.impl.AbstractTextBasedEditorRenderer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.impl.InputElement;

/**
 * @param <T>
 *           supported decimal classes: {@link java.lang.Float}, {@link java.lang.Double}
 */
public abstract class AbstractDecimalEditor<T extends Number> extends AbstractTextBasedEditorRenderer<T>
                implements FormattableNumberEditor<T>
{
    private static final String LABEL_OUT_OF_RANGE = "defaultdecimaleditor.value.outofrange";
    private final Class<T> valueType;
    private final T minValue;
    private final T maxValue;
    private static final Double DEFAULT_DECIMAL_VALUE = 0.0;
    private boolean isValueInvalid = false;


    public AbstractDecimalEditor(final Class<T> valueType, final T minValue, final T maxValue)
    {
        this.valueType = valueType;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }


    @Override
    protected void setRawValue(final InputElement editorView, final T rawValue)
    {
        editorView.setRawValue(rawValue);
    }


    @Override
    protected T getRawValue(final InputElement editorView)
    {
        final Object rawValue = editorView.getRawValue();
        if(isPrimitive() && null == rawValue)
        {
            return convertFromDouble(DEFAULT_DECIMAL_VALUE);
        }
        else
        {
            return convertFromDouble((Double)editorView.getRawValue());
        }
    }


    @Override
    protected T coerceFromString(final InputElement editorView, final String text)
    {
        final Double doubleValue = ((InternalDoublebox)editorView).getAsDouble(text);
        return convertFromDouble(doubleValue);
    }


    @Override
    public void render(final Component parent, final EditorContext<T> context, final EditorListener<T> listener)
    {
        final Doublebox editorView = new InternalDoublebox();
        final Double initialValue = convertToDouble(context.getInitialValue());
        editorView.setValue(initialValue);
        editorView.setConstraint((component, object) -> validateRange(component, context, (T)object));
        editorView.addEventListener(Events.ON_BLUR, event -> {
            if(isValueInvalid)
            {
                editorView.setValue(initialValue);
            }
        });
        initViewComponent(editorView, context, listener);
        editorView.setParent(parent);
    }


    protected void validateRange(final Component component, final EditorContext<T> context, final T object)
    {
        if(object == null)
        {
            isValueInvalid = !context.isOptional();
        }
        else
        {
            final double doubleValue = convertToDouble(object);
            final double doubleMinValue = convertToDouble(minValue);
            final double doubleMaxValue = convertToDouble(maxValue);
            final boolean isLowerThanMin = Double.compare(doubleValue, doubleMinValue) < 0;
            final boolean isGreaterThanMax = Double.compare(doubleValue, doubleMaxValue) > 0;
            final boolean isOutOfRange = isLowerThanMin || isGreaterThanMax;
            isValueInvalid = Double.isNaN(doubleValue) || isOutOfRange;
        }
        if(isValueInvalid)
        {
            final String errorMessage = context.getLabel(LABEL_OUT_OF_RANGE);
            throw new WrongValueException(component, errorMessage);
        }
    }


    private Double convertToDouble(final T value)
    {
        Double ret = null;
        if(value != null)
        {
            if(value.getClass().equals(Double.class))
            {
                ret = (Double)value;
            }
            else if(value.getClass().equals(Float.class))
            {
                ret = Double.valueOf(Float.toString((Float)value));
            }
        }
        return ret;
    }


    private T convertFromDouble(final Double value)
    {
        Number ret = null;
        if(value != null)
        {
            if(valueType.equals(Double.class))
            {
                ret = value;
            }
            else if(valueType.equals(Float.class))
            {
                ret = Float.valueOf(value.floatValue());
            }
        }
        return (T)ret;
    }


    private static class InternalDoublebox extends Doublebox
    {
        public Double getAsDouble(final String value)
        {
            return (Double)super.coerceFromString(value);
        }
    }
}
