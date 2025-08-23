package de.hybris.platform.cockpit.model.editor.search.impl;

import de.hybris.platform.cockpit.model.editor.DateParametersEditorHelper;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.text.SimpleDateFormat;
import org.zkoss.zul.Datebox;

public class DefaultDateConditionUIEditor extends AbstractSimpleInputConditionUIEditor
{
    protected String getValueType()
    {
        return "DATE";
    }


    protected String getDisplayLabel(Object value)
    {
        if(value instanceof java.util.Date)
        {
            if(getParameters() != null)
            {
                String dateFormat = DateParametersEditorHelper.getDateFormat(getParameters(), new Datebox());
                return (new SimpleDateFormat(dateFormat, UISessionUtils.getCurrentSession().getLocale())).format(value);
            }
        }
        return super.getDisplayLabel(value);
    }
}
