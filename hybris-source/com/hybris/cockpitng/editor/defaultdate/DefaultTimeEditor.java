/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultdate;

import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.zkoss.util.Locales;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.impl.InputElement;

/**
 * Default Editor for {@link java.util.Date} values regarding only time.
 */
public class DefaultTimeEditor extends AbstractDateTimeEditor<Timebox>
{
    protected static final String TIME_FORMAT = "timeFormat";


    @Override
    protected Timebox createEditorView()
    {
        return new MyTimebox();
    }


    @Override
    protected String getDateFormat(final int dateStyle)
    {
        String result = null;
        final DateFormat dateInstance = DateFormat.getTimeInstance(dateStyle, Locales.getCurrent());
        if(dateInstance instanceof SimpleDateFormat)
        {
            result = ((SimpleDateFormat)dateInstance).toPattern();
        }
        return result;
    }


    @Override
    protected String getFormatConfigurationParameter()
    {
        return TIME_FORMAT;
    }


    @Override
    protected void initViewComponent(final InputElement editorView, final EditorContext<Date> context,
                    final EditorListener<Date> listener)
    {
        super.initViewComponent(editorView, context, listener);
        final Timebox box = (Timebox)editorView;
        box.setValue(context.getInitialValue());
    }


    @Override
    public void initTimeZone(final Timebox box, final EditorContext<Date> context)
    {
        super.initTimeZone(box, context);
        final TimeZone initialTimeZoneFromConfig = getInitialTimeZoneFromConfig(context);
        if(initialTimeZoneFromConfig != null)
        {
            box.setTimeZone(initialTimeZoneFromConfig);
        }
    }


    @Override
    protected Date coerceFromString(final InputElement editorView, final String text) throws WrongValueException
    {
        return ((MyTimebox)editorView).coerceFromString(text);
    }


    private static class MyTimebox extends Timebox
    {
        private static final long serialVersionUID = 1;


        @Override
        public Date coerceFromString(final String value) throws WrongValueException
        {
            return (Date)super.coerceFromString(value);
        }
    }
}
