package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.daos.UserDao;
import de.hybris.platform.warehousing.util.builder.UserModelBuilder;
import org.springframework.beans.factory.annotation.Required;

public class Users extends AbstractItems<UserModel>
{
    public static final String UID_BOB = "bob";
    public static final String UID_NANCY = "nancy";
    public static final String UID_MANAGER_MONTREAL_DUKE = "mgr-montreal-duke";
    public static final String UID_MANAGER_MONTREAL_MAISONNEUVE = "mgr-montreal-maisonneuve";
    public static final String ADMIN_GRPOUP = "admingroup";
    public static final String WAREHOUSE_ADMINISTRATOR_GROUP = "warehouseadministratorgroup";
    public static final String WAREHOUSE_MANAGER_GRPOUP = "warehousemanagergroup";
    public static final String WAREHOUSE_AGENT_GRPOUP = "warehouseagentgroup";
    private UserDao userDao;


    public UserModel Bob()
    {
        return getOrCreateUser("bob");
    }


    public UserModel Nancy()
    {
        return getOrCreateUser("nancy");
    }


    public UserModel ManagerMontrealDuke()
    {
        return getOrCreateUser("mgr-montreal-duke");
    }


    public UserModel ManagerMontrealMaisonneuve()
    {
        return getOrCreateUser("mgr-montreal-maisonneuve");
    }


    public UserModel AdminGroup()
    {
        return getOrCreateUser("admingroup");
    }


    public UserModel WarehouseAdministratorGroup()
    {
        return getOrCreateUser("warehouseadministratorgroup");
    }


    public UserModel WarehouseManagerGroup()
    {
        return getOrCreateUser("warehousemanagergroup");
    }


    public UserModel WarehouseAgentGroup()
    {
        return getOrCreateUser("warehouseagentgroup");
    }


    protected UserModel getOrCreateUser(String uid)
    {
        return (UserModel)getOrSaveAndReturn(() -> getUserDao().findUserByUID(uid), () -> UserModelBuilder.aModel().withUid(uid).build());
    }


    public UserDao getUserDao()
    {
        return this.userDao;
    }


    @Required
    public void setUserDao(UserDao userDao)
    {
        this.userDao = userDao;
    }
}
