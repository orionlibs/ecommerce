package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.localization.Localization;

public class CMSInverseRestriction extends GeneratedCMSInverseRestriction
{
    @Deprecated(since = "4.3")
    public String getDescription(SessionContext ctx)
    {
        return Localization.getLocalizedString("type.cmsinverserestriction.description.text") + " " + Localization.getLocalizedString("type.cmsinverserestriction.description.text");
    }
}
