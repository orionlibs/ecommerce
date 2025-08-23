package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.localization.Localization;
import java.util.Collection;

public class UserRestriction extends GeneratedUserRestriction
{
    @Deprecated(since = "4.3")
    public String getDescription(SessionContext ctx)
    {
        Collection<User> users = getUsers();
        StringBuilder result = new StringBuilder();
        if(!users.isEmpty())
        {
            String localizedString = Localization.getLocalizedString("type.CMSUserRestriction.description.text");
            result.append((localizedString == null) ? "Display for users:" : localizedString);
            for(User user : users)
            {
                result.append(" ").append(user.getName(ctx)).append(" (").append(user.getUID()).append(");");
            }
        }
        return result.toString();
    }
}
