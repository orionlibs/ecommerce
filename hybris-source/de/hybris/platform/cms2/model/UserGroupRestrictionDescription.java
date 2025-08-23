package de.hybris.platform.cms2.model;

import com.google.common.base.Strings;
import de.hybris.platform.cms2.model.restrictions.CMSUserGroupRestrictionModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;
import java.util.Collection;

public class UserGroupRestrictionDescription implements DynamicAttributeHandler<String, CMSUserGroupRestrictionModel>
{
    public String get(CMSUserGroupRestrictionModel model)
    {
        Collection<UserGroupModel> groups = model.getUserGroups();
        StringBuilder result = new StringBuilder();
        if(groups != null && !groups.isEmpty())
        {
            String localizedString = Localization.getLocalizedString("type.CMSUserGroupRestriction.description.text");
            result.append((localizedString == null) ? "Display for user groups:" : localizedString);
            for(UserGroupModel group : groups)
            {
                if(!Strings.isNullOrEmpty(group.getLocName()))
                {
                    result.append(" ").append(group.getLocName());
                }
                result.append(" (").append(group.getUid()).append(");");
            }
        }
        return result.toString();
    }


    public void set(CMSUserGroupRestrictionModel model, String value)
    {
        throw new UnsupportedOperationException();
    }
}
