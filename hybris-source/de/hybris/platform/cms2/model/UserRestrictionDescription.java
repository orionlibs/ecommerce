package de.hybris.platform.cms2.model;

import com.google.common.base.Strings;
import de.hybris.platform.cms2.model.restrictions.CMSUserRestrictionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;
import java.util.Collection;

public class UserRestrictionDescription implements DynamicAttributeHandler<String, CMSUserRestrictionModel>
{
    public String get(CMSUserRestrictionModel model)
    {
        Collection<UserModel> users = model.getUsers();
        StringBuilder result = new StringBuilder();
        if(users != null && !users.isEmpty())
        {
            String localizedString = Localization.getLocalizedString("type.CMSUserRestriction.description.text");
            result.append((localizedString == null) ? "Display for users:" : localizedString);
            for(UserModel user : users)
            {
                if(!Strings.isNullOrEmpty(user.getName()))
                {
                    result.append(" ").append(user.getName());
                }
                result.append(" (").append(user.getUid()).append(");");
            }
        }
        return result.toString();
    }


    public void set(CMSUserRestrictionModel model, String value)
    {
        throw new UnsupportedOperationException();
    }
}
