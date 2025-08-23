/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultdate;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractTextBasedEditorRenderer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.Locales;
import org.zkoss.util.TimeZones;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.impl.FormatInputElement;
import org.zkoss.zul.impl.InputElement;

/**
 * Abstract Editor for {@link java.util.Date} values.
 * <P>
 * Abstraction is made to extract all methods independent from whether a component is used as input for date only (
 * {@link org.zkoss.zul.Datebox}), time only ({@link org.zkoss.zul.Timebox}) or both ({@link org.zkoss.zul.Datebox}).
 */
public abstract class AbstractDateTimeEditor<C extends FormatInputElement> extends AbstractTextBasedEditorRenderer<Date>
{
    public static final String SELECTED_TIME_ZONE = "selectedTimeZone";
    protected static final int DATE_FORMAT_NONE = -DateFormat.LONG;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDateTimeEditor.class);
    private static final Map<String, Integer> DATEFORMAT_MAPPING;

    static
    {
        final Map<String, Integer> tmpDateformatMapping = new HashMap<>();
        tmpDateformatMapping.put("short", Integer.valueOf(DateFormat.SHORT));
        tmpDateformatMapping.put("medium", Integer.valueOf(DateFormat.MEDIUM));
        tmpDateformatMapping.put("long", Integer.valueOf(DateFormat.LONG));
        tmpDateformatMapping.put("full", Integer.valueOf(DateFormat.FULL));
        tmpDateformatMapping.put("none", Integer.valueOf(DATE_FORMAT_NONE));
        DATEFORMAT_MAPPING = Collections.unmodifiableMap(tmpDateformatMapping);
    }

    /**
     * Determines a format string for specified format style (see: {@link java.text.DateFormat})
     *
     * @param dateStyle
     *           format style
     * @return format string
     */
    protected abstract String getDateFormat(int dateStyle);


    /**
     * Checks if provided style is an actual style or points to {@link #DATE_FORMAT_NONE}.
     *
     * @param style
     *           date style to be checked
     * @return style provided if not {@link #DATE_FORMAT_NONE}, otherwise a default one
     */
    protected int getRequiredStyle(final int style)
    {
        return style != DATE_FORMAT_NONE ? style : DateFormat.MEDIUM;
    }


    /**
     * Determines the date format for the component.
     * <p>
     * The given date format can be either a custom date format e. g. dd/MM/yyyy (see {@link java.text.SimpleDateFormat})
     * or the following constants for getting a localized date format:
     * <ul>
     * <li>short</li>
     * <li>medium</li>
     * <li>long</li>
     * <li>full</li>
     * </ul>
     *
     * @param dateFormatInput
     *           the configured date format
     * @return date format string ({@link java.text.DateFormat}) for the component or <code>null</code> if format is
     *         invalid
     */
    protected String getDateFormat(final String dateFormatInput)
    {
        return getDefaultDateFormat(dateFormatInput).map(this::getRequiredStyle).map(this::getDateFormat)
                        .orElseGet(() -> getPatternDateFormat(dateFormatInput).orElse(null));
    }


    protected Optional<Integer> getDefaultDateFormat(final String formatInput)
    {
        if(DATEFORMAT_MAPPING.containsKey(formatInput))
        {
            final int dateStyle = DATEFORMAT_MAPPING.get(formatInput).intValue();
            return Optional.of(dateStyle);
        }
        else
        {
            return Optional.empty();
        }
    }


    protected Optional<String> getPatternDateFormat(final String formatInput)
    {
        try
        {
            // check pattern
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatInput, Locales.getCurrent());
            //noinspection ResultOfMethodCallIgnored
            simpleDateFormat.toString();
            // date format is correct
            return Optional.of(formatInput);
        }
        catch(final IllegalArgumentException e)
        {
            // date format is incorrect
            final String message = String.format("Date format '%s' is invalid! Default format is used.", formatInput);
            if(LOG.isDebugEnabled())
            {
                LOG.warn(message, e);
            }
            else
            {
                LOG.warn(message);
            }
            return Optional.empty();
        }
    }


    /**
     * Returns default date format. Called if date format was not resolved by {@link #getDateFormat(String)} method.
     *
     * @param datebox
     * @return
     */
    protected String getDefaultDateFormat(final C datebox)
    {
        return getDateFormat("medium");
    }


    /**
     * Creates a view component.
     *
     * @return view component
     */
    protected abstract C createEditorView();


    /**
     * Defines a name of configuration parameter that stands for a date format.
     *
     * @return name of configuration parameter
     */
    protected abstract String getFormatConfigurationParameter();


    /**
     * Gets format string from configuration.
     * <P>
     * Returned string should be compatible with {@link SimpleDateFormat}
     * </P>
     *
     * @param datebox
     *           component for which a format is requested
     * @param context
     *           the context for the editor creation
     * @return format string
     */
    protected String getDateFormat(final C datebox, final EditorContext<Date> context)
    {
        String result = null;
        final Object object = context.getParameter(getFormatConfigurationParameter());
        if(object instanceof String)
        {
            result = getDateFormat((String)object);
        }
        if(result == null)
        {
            result = getDefaultDateFormat(datebox);
        }
        return result;
    }


    /**
     * In case of dateTimeEditor we have to override standard implementation of handleReadOnly. readonly='true' allows
     * user to change value by datepicker. Setting disabled='true' fixes this problem.
     *
     * @param context
     * @param editorView
     */
    @Override
    protected void handleReadOnly(final EditorContext<Date> context, final InputElement editorView)
    {
        editorView.setDisabled(!context.isEditable());
    }


    @Override
    protected void initViewComponent(final InputElement editorView, final EditorContext<Date> context,
                    final EditorListener<Date> listener)
    {
        super.initViewComponent(editorView, context, listener);
        final C box = (C)editorView;
        box.setFormat(getDateFormat(box, context));
        initTimeZone(box, context);
    }


    @Override
    public void render(final Component parent, final EditorContext<Date> context, final EditorListener<Date> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final Hbox dateArea = new Hbox();
        final C editorView = createEditorView();
        initViewComponent(editorView, context, listener);
        dateArea.setParent(parent);
        dateArea.appendChild(editorView);
    }


    @Override
    protected void setRawValue(final InputElement viewComponent, final Date rawValue)
    {
        viewComponent.setRawValue(rawValue);
    }


    @Override
    protected Date getRawValue(final InputElement viewComponent)
    {
        return (Date)viewComponent.getRawValue();
    }


    public void initTimeZone(final C box, final EditorContext<Date> context)
    {
        Validate.notNull("All parameters are mandatory", box, context);
    }


    protected TimeZone getInitialTimeZoneFromConfig(final EditorContext<Date> context)
    {
        final String selectedTimeZone = ObjectUtils.toString(context.getParameter(SELECTED_TIME_ZONE));
        if(StringUtils.isNotBlank(selectedTimeZone))
        {
            return TimeZone.getTimeZone(selectedTimeZone);
        }
        return getSessionTimeZoneAttribute();
    }


    protected TimeZone getSessionTimeZoneAttribute()
    {
        final Session session = Sessions.getCurrent();
        return session == null ? null : (TimeZone)session.getAttribute(Attributes.PREFERRED_TIME_ZONE);
    }


    protected TimeZone getZKDefaultTimeZone()
    {
        return TimeZones.getCurrent();
    }
}
