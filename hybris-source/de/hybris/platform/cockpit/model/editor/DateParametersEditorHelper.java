package de.hybris.platform.cockpit.model.editor;

import de.hybris.platform.cockpit.model.editor.impl.DefaultDateUIEditor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Datebox;

public class DateParametersEditorHelper
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDateUIEditor.class);
    private static final Map<String, Integer> DATEFORMAT_MAPPING;

    static
    {
        Map<String, Integer> tmpDateformatMapping = new HashMap<>();
        tmpDateformatMapping.put("short", Integer.valueOf(3));
        tmpDateformatMapping.put("medium", Integer.valueOf(2));
        DATEFORMAT_MAPPING = Collections.unmodifiableMap(tmpDateformatMapping);
    }

    public static String getDateFormat(Map<String, ? extends Object> parameters, Datebox dateBox)
    {
        String dateFormat = null;
        String dateFormatInput = null;
        if(parameters != null)
        {
            Object object = parameters.get("dateFormat");
            if(object instanceof String)
            {
                dateFormatInput = (String)object;
            }
        }
        if(dateFormatInput != null)
        {
            if(DATEFORMAT_MAPPING.containsKey(dateFormatInput))
            {
                int dateStyle = ((Integer)DATEFORMAT_MAPPING.get(dateFormatInput)).intValue();
                DateFormat dateInstance = DateFormat.getDateInstance(dateStyle, UISessionUtils.getCurrentSession().getLocale());
                if(dateInstance instanceof SimpleDateFormat)
                {
                    dateFormat = ((SimpleDateFormat)dateInstance).toPattern();
                }
            }
            else
            {
                try
                {
                    new SimpleDateFormat(dateFormatInput, UISessionUtils.getCurrentSession().getLocale());
                    dateFormat = dateFormatInput;
                }
                catch(IllegalArgumentException e)
                {
                    LOG.warn("Date format '" + dateFormatInput + "' is invalid! Default format is used.");
                }
            }
        }
        if(dateFormat == null)
        {
            dateFormat = getDefaultDateFormat(dateBox);
        }
        return dateFormat;
    }


    protected static String getDefaultDateFormat(Datebox datebox)
    {
        String format = datebox.getFormat();
        if(format != null && !format.toLowerCase().contains("h:"))
        {
            format = format.trim() + " HH:mm";
        }
        return format;
    }


    public static boolean getLenientFlag(Map<String, ? extends Object> parameters)
    {
        String lenientDefault = getLenientDefault();
        Object lenientParam = null;
        if(parameters != null)
        {
            lenientParam = parameters.get("lenientDate");
        }
        boolean lenient = true;
        String lenientString = null;
        if(lenientParam != null)
        {
            if(lenientParam instanceof String)
            {
                lenientString = (String)lenientParam;
            }
            else
            {
                LOG.error("Lenient parameter is not a string.");
            }
        }
        if(lenientString == null)
        {
            lenientString = lenientDefault;
        }
        if(lenientString != null)
        {
            lenient = Boolean.parseBoolean(lenientString);
        }
        return lenient;
    }


    public static String getLenientDefault()
    {
        return UITools.getCockpitParameter("default.dateuieditor.lenientDate", Executions.getCurrent());
    }
}
