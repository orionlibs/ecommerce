package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.localization.Localization;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeRestriction extends GeneratedTimeRestriction
{
    @Deprecated(since = "4.3")
    public String getDescription(SessionContext ctx)
    {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        StringBuilder result = new StringBuilder();
        Date from = getActiveFrom();
        Date until = getActiveUntil();
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
}
