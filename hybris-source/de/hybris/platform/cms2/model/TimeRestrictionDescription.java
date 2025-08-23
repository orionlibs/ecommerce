package de.hybris.platform.cms2.model;

import de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeRestrictionDescription implements DynamicAttributeHandler<String, CMSTimeRestrictionModel>
{
    public String get(CMSTimeRestrictionModel model)
    {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        StringBuilder result = new StringBuilder();
        Date from = model.getActiveFrom();
        Date until = model.getActiveUntil();
        Object[] args = {from, until};
        String localizedString = Localization.getLocalizedString("type.CMSTimeRestriction.description.text", args);
        if(localizedString == null && from != null && until != null)
        {
            result.append("Display from ").append(format.format(from));
            result.append(" until ").append(format.format(until));
        }
        else
        {
            return localizedString;
        }
        return result.toString();
    }


    public void set(CMSTimeRestrictionModel model, String value)
    {
        throw new UnsupportedOperationException();
    }
}
