package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.util.localization.Localization;
import java.util.Collection;

public class GroupRestriction extends GeneratedGroupRestriction
{
    @Deprecated(since = "4.3")
    public String getDescription(SessionContext ctx)
    {
        Collection<UserGroup> groups = getUserGroups();
        StringBuilder result = new StringBuilder();
        if(!groups.isEmpty())
        {
            String localizedString = Localization.getLocalizedString("type.CMSUserGroupRestriction.description.text");
            result.append((localizedString == null) ? "Display for user groups:" : localizedString);
            for(UserGroup group : groups)
            {
                result.append(" ").append(group.getLocName(ctx)).append(" (").append(group.getUID()).append(");");
            }
        }
        return result.toString();
    }
}
