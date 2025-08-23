package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.servicelayer.user.daos.UserGroupDao;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.warehousing.util.builder.CsAgentGroupModelBuilder;
import org.springframework.beans.factory.annotation.Required;

public class CsAgentGroups extends AbstractItems<CsAgentGroupModel>
{
    public static final String UID_FRAUDAGENT = "fraudAgentGroup";
    private UserGroupDao userGroupDao;


    public CsAgentGroupModel fraudAgentGroup()
    {
        return getOrCreateCsAgentGroup("fraudAgentGroup");
    }


    protected CsAgentGroupModel getOrCreateCsAgentGroup(String uid)
    {
        return (CsAgentGroupModel)getOrSaveAndReturn(() -> (CsAgentGroupModel)getUserGroupDao().findUserGroupByUid(uid), () -> CsAgentGroupModelBuilder.aModel().withUid(uid).build());
    }


    public UserGroupDao getUserGroupDao()
    {
        return this.userGroupDao;
    }


    @Required
    public void setUserGroupDao(UserGroupDao userGroupDao)
    {
        this.userGroupDao = userGroupDao;
    }
}
