package de.hybris.platform.cockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.Collection;
import java.util.List;
import org.zkoss.util.resource.Labels;

public class QueryRendererUtils
{
    public int getTotalUserCount(UserGroupModel model)
    {
        int ret = 0;
        for(PrincipalModel pm : model.getMembers())
        {
            if(pm instanceof UserModel)
            {
                ret++;
            }
        }
        return ret;
    }


    public int getCheckedUserCount(UserGroupModel model, List<PrincipalModel> readUsers)
    {
        int ret = 0;
        for(PrincipalModel pm : model.getMembers())
        {
            if(!(pm instanceof UserModel))
            {
                continue;
            }
            for(PrincipalModel readUser : readUsers)
            {
                if(pm.equals(readUser))
                {
                    ret++;
                }
            }
        }
        return ret;
    }


    public String buildGroupMenuLabel(String prefix, int checkedUserCount, int totalUserCount)
    {
        StringBuilder result = new StringBuilder();
        result.append(prefix);
        if(totalUserCount > 0)
        {
            result.append(" (");
            result.append(checkedUserCount);
            result.append("/");
            result.append(totalUserCount);
            result.append(")");
        }
        return result.toString();
    }


    public String buildGroupMenuLabel(String prefix, int totalUserCount)
    {
        StringBuilder result = new StringBuilder();
        result.append(prefix);
        if(totalUserCount > 0)
        {
            result.append(" (");
            result.append(totalUserCount);
            result.append(")");
        }
        return result.toString();
    }


    public String getGroupMenuItemTooltip(String groupName, int checkedUserCount, int totalUserCount, boolean allgroupMembersChecked)
    {
        String ret = null;
        if(totalUserCount > 0)
        {
            if(allgroupMembersChecked)
            {
                ret = Labels.getLabel("savedquery.tooltip.all.group.members", new Object[] {groupName});
            }
            else if(totalUserCount == checkedUserCount)
            {
                ret = Labels.getLabel("savedquery.tooltip.all.group.users", new Object[] {groupName});
            }
            else
            {
                ret = Labels.getLabel("savedquery.tooltip.part.group.users", new Object[] {Integer.valueOf(checkedUserCount), Integer.valueOf(totalUserCount), groupName});
            }
        }
        return ret;
    }


    public boolean isUserExclusiveQuery(UserModel user, CockpitSavedQueryModel query)
    {
        boolean ret = true;
        UserModel queryUser = query.getUser();
        boolean isCreator = (queryUser != null && queryUser.equals(user));
        if(isCreator)
        {
            Collection<PrincipalModel> readSavedQueryPrincipals = query.getReadSavedQueryPrincipals();
            for(PrincipalModel pm : readSavedQueryPrincipals)
            {
                String creatorUid = queryUser.getUid();
                String pmUid = pm.getUid();
                if(!creatorUid.equals(pmUid))
                {
                    ret = false;
                    break;
                }
            }
        }
        else
        {
            ret = false;
        }
        return ret;
    }


    public boolean isUserReceivedSavedQuery(UserModel user, CockpitSavedQueryModel query)
    {
        UserModel queryUser = query.getUser();
        boolean isCreator = (queryUser != null && queryUser.equals(user));
        return !isCreator;
    }


    public SavedQuerySharingMode getSavedQuerySharingMode(UserModel user, CockpitSavedQueryModel query)
    {
        SavedQuerySharingMode ret = null;
        boolean isUserReceivedQuery = isUserReceivedSavedQuery(user, query);
        if(isUserReceivedQuery)
        {
            ret = SavedQuerySharingMode.USER_RECEIVED;
        }
        else
        {
            boolean userExclusiveQuery = isUserExclusiveQuery(user, query);
            if(userExclusiveQuery)
            {
                ret = SavedQuerySharingMode.USER_EXCLUSIVE;
            }
            else
            {
                ret = SavedQuerySharingMode.USER_SHARED;
            }
        }
        return ret;
    }


    public String getSavedQueryDeleteLabel(SavedQuerySharingMode mode)
    {
        switch(null.$SwitchMap$de$hybris$platform$cockpit$components$navigationarea$renderer$QueryRendererUtils$SavedQuerySharingMode[mode.ordinal()])
        {
            case 1:
                ret = Labels.getLabel("savedquery.delete.nonshared");
                return ret;
            case 2:
                ret = Labels.getLabel("savedquery.delete.received");
                return ret;
            case 3:
                ret = Labels.getLabel("savedquery.delete.shared");
                return ret;
        }
        String ret = Labels.getLabel("savedquery.delete");
        return ret;
    }


    public String getSavedQueryRenameLabel(SavedQuerySharingMode mode)
    {
        switch(null.$SwitchMap$de$hybris$platform$cockpit$components$navigationarea$renderer$QueryRendererUtils$SavedQuerySharingMode[mode.ordinal()])
        {
            case 1:
                ret = Labels.getLabel("savedquery.rename.nonshared");
                return ret;
            case 2:
                ret = Labels.getLabel("savedquery.rename.received");
                return ret;
            case 3:
                ret = Labels.getLabel("savedquery.rename.shared");
                return ret;
        }
        String ret = Labels.getLabel("savedquery.rename");
        return ret;
    }
}
