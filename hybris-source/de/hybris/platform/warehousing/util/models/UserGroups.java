package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.user.daos.impl.DefaultUserGroupDao;
import de.hybris.platform.warehousing.util.builder.UserGroupModelBuilder;
import org.springframework.beans.factory.annotation.Required;

public class UserGroups extends AbstractItems<UserGroupModel>
{
    public static final String ADMIN_GRPOUP = "admingroup";
    public static final String WAREHOUSE_ADMINISTRATOR_GROUP = "warehouseadministratorgroup";
    public static final String WAREHOUSE_MANAGER_GRPOUP = "warehousemanagergroup";
    public static final String WAREHOUSE_AGENT_GRPOUP = "warehouseagentgroup";
    public static final String CUSTOMER_GRPOUP = "customergroup";
    private DefaultUserGroupDao userGroupDao;


    public UserGroupModel AdminGroup()
    {
        return getOrCreateUserGroup("admingroup");
    }


    public UserGroupModel WarehouseAdministratorGroup()
    {
        return getOrCreateUserGroup("warehouseadministratorgroup");
    }


    public UserGroupModel WarehouseManagerGroup()
    {
        return getOrCreateUserGroup("warehousemanagergroup");
    }


    public UserGroupModel WarehouseAgentGroup()
    {
        return getOrCreateUserGroup("warehouseagentgroup");
    }


    public UserGroupModel CustomerGroup()
    {
        return getOrCreateUserGroup("customergroup");
    }


    protected UserGroupModel getOrCreateUserGroup(String uid)
    {
        return (UserGroupModel)getOrSaveAndReturn(() -> getUserGroupDao().findUserGroupByUid(uid), () -> UserGroupModelBuilder.aModel().withUid(uid).build());
    }


    protected DefaultUserGroupDao getUserGroupDao()
    {
        return this.userGroupDao;
    }


    @Required
    public void setUserGroupDao(DefaultUserGroupDao userGroupDao)
    {
        this.userGroupDao = userGroupDao;
    }
}
