/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultdate;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.text.DateFormats;
import org.zkoss.util.Locales;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.impl.InputElement;

/**
 * Default Editor for {@link Date} values (incl. time).
 */
public class DefaultDateEditor extends AbstractDateTimeEditor<Datebox>
{
    public static final String TIME_ZONE_READ_ONLY = "timeZoneReadOnly";
    public static final String DISPLAYED_TIME_ZONES = "displayedTimeZones";
    public static final String DISABLE_INLINE_EDITING = "disableInlineEditing";
    public static final String DISABLE_INLINE_EDITING_SCLASS = "ye-inline-editing-disabled";
    protected static final String DATE_FORMAT = "dateFormat";
    protected static final String SHOW_CALENDAR_ON_FOCUS = "calendarOnFocus";
    protected static final String DEFAULT_TIMEZONES = "GMT-12,GMT+0,GMT+12";
    protected static final String TIMEZONES_DELIMITER = ",";
    private static final Pattern EXTENDED_FORMAT_PATTERN = Pattern.compile("([^+]+)\\+([^+]+)");
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDateEditor.class);


    @Override
    protected Datebox createEditorView()
    {
        return new MyDatebox();
    }


    @Override
    protected String getDateFormat(final String dateFormatInput)
    {
        final Matcher matcher = EXTENDED_FORMAT_PATTERN.matcher(dateFormatInput);
        if(matcher.find())
        {
            final Optional<Integer> dateFormat = getDefaultDateFormat(matcher.group(1));
            final Optional<Integer> timeFormat = getDefaultDateFormat(matcher.group(2));
            if(dateFormat.isPresent())
            {
                final int dateStyle = dateFormat.get().intValue();
                final int timeStyle = timeFormat.orElseGet(dateFormat::get).intValue();
                return getDateTimeFormat(dateStyle, timeStyle);
            }
        }
        return super.getDateFormat(dateFormatInput);
    }


    @Override
    protected String getDateFormat(final int dateStyle)
    {
        return getDateTimeFormat(dateStyle, dateStyle);
    }


    protected String getDateTimeFormat(final int dateStyle, final int timeStyle)
    {
        return timeStyle != DATE_FORMAT_NONE
                        ? DateFormats.getDateTimeFormat(getRequiredStyle(dateStyle), timeStyle, Locales.getCurrent(), null)
                        : DateFormats.getDateFormat(getRequiredStyle(dateStyle), Locales.getCurrent(), null);
    }


    @Override
    protected String getFormatConfigurationParameter()
    {
        return DATE_FORMAT;
    }


    @Override
    protected void initViewComponent(final InputElement editorView, final EditorContext<Date> context,
                    final EditorListener<Date> listener)
    {
        super.initViewComponent(editorView, context, listener);
        if(shouldInputBeDisabled(context))
        {
            editorView.setReadonly(true);
            editorView.setClass(DISABLE_INLINE_EDITING_SCLASS);
        }
        final Datebox box = (Datebox)editorView;
        box.setValue(context.getInitialValue());
        initTimeZone(box, context);
        if(context.getParameterAsBoolean(SHOW_CALENDAR_ON_FOCUS, false))
        {
            box.setButtonVisible(false);
            box.addEventListener(Events.ON_FOCUS, event -> box.open());
        }
    }


    protected boolean shouldInputBeDisabled(final EditorContext<Date> context)
    {
        final Object disableInput = context.getParameter(DISABLE_INLINE_EDITING);
        return "true".equals(disableInput);
    }


    @Override
    public void initTimeZone(final Datebox box, final EditorContext<Date> context)
    {
        super.initTimeZone(box, context);
        initTimezoneManagement(box, context);
    }


    /**
     * @deprecated since 6.5, please use the {@link #initTimeZone(Datebox, EditorContext)} instead.
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected void initTimezoneManagement(final Datebox box, final EditorContext<Date> context)
    {
        final TimeZone initialTimeZone = (TimeZone)ObjectUtils.defaultIfNull(getInitialTimeZoneFromConfig(context),
                        getZKDefaultTimeZone());
        if(!isTimeZoneReadOnly(context))
        {
            box.setTimeZonesReadonly(false);
            final String displayedTimeZones = getDisplayedTimeZones(context);
            if(initialTimeZone != null && !isInitialTimeZoneInDisplayedTimezones(displayedTimeZones, initialTimeZone))
            {
                LOG.warn("Improper configuration of Date Editor. Initial time zone ({}) should be in displayed time zones ({}).",
                                initialTimeZone.getDisplayName(), displayedTimeZones);
            }
            else
            {
                box.setTimeZone(initialTimeZone);
                box.setDisplayedTimeZones(displayedTimeZones);
            }
        }
        else
        {
            box.setTimeZone(initialTimeZone);
        }
    }


    protected String getDisplayedTimeZones(final EditorContext<Date> context)
    {
        final String displayedTimeZones = ObjectUtils.toString(context.getParameter(DISPLAYED_TIME_ZONES), null);
        return StringUtils.defaultIfBlank(displayedTimeZones, DEFAULT_TIMEZONES);
    }


    @Override
    protected Date coerceFromString(final InputElement editorView, final String text)
    {
        return ((MyDatebox)editorView).coerceFromString(text);
    }


    protected void setTimeZone(final Datebox box, final TimeZone timeZone)
    {
        box.setTimeZone(timeZone);
    }


    protected boolean isTimeZoneReadOnly(final EditorContext<Date> context)
    {
        Validate.notNull("All parameters are mandatory", context);
        final Boolean timeZoneReadOnlyParam = (Boolean)context.getParameter(TIME_ZONE_READ_ONLY);
        return BooleanUtils.toBooleanDefaultIfNull(timeZoneReadOnlyParam, true);
    }


    protected boolean isInitialTimeZoneInDisplayedTimezones(final String displayedTimeZonesString, final TimeZone initialTimeZone)
    {
        if(displayedTimeZonesString != null && initialTimeZone != null)
        {
            final List<TimeZone> displayedTimeZones = getTimeZones(displayedTimeZonesString);
            return displayedTimeZones.contains(initialTimeZone);
        }
        return false;
    }


    protected List<TimeZone> getTimeZones(final String timeZonesString)
    {
        final String[] splitTimeZoneString = timeZonesString.split(TIMEZONES_DELIMITER);
        final List<TimeZone> timeZones = new ArrayList<>();
        for(final String timeZoneString : splitTimeZoneString)
        {
            timeZones.add(TimeZone.getTimeZone(timeZoneString));
        }
        return timeZones;
    }


    private static class MyDatebox extends Datebox
    {
        private static final long serialVersionUID = 1;


        @Override
        public Date coerceFromString(final String value)
        {
            return (Date)super.coerceFromString(value);
        }
    }
}
