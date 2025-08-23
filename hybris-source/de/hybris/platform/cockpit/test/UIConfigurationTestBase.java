package de.hybris.platform.cockpit.test;

import de.hybris.platform.cockpit.jalo.CockpitManager;
import de.hybris.platform.cockpit.jalo.CockpitTransactionalTest;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.login.LoginService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.model.ModelService;
import java.net.URL;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public abstract class UIConfigurationTestBase extends CockpitTransactionalTest
{
    protected UserModel productManagerUser;
    protected UserGroup cockpitGroup;
    protected UserGroup defaultRoleGroup;
    protected UIConfigurationService uiConfigurationService;
    protected TypeService typeService;
    protected ModelService modelService;
    protected User productManagerUserItem;
    protected LoginService loginService;


    @Before
    public void setUp() throws Exception
    {
        UserManager userManager = UserManager.getInstance();
        this.cockpitGroup = getOrCreateUserGroup(userManager, "cockpitusergroup");
        this.defaultRoleGroup = getOrCreateUserGroup(userManager, this.uiConfigurationService.getFallbackRole().getName());
        this.cockpitGroup.addMember((Principal)this.defaultRoleGroup);
        this.productManagerUserItem = getOrCreateUser(userManager, "productmanager");
        this.productManagerUserItem.setPassword("test");
        userManager.getAdminUserGroup().addMember((Principal)this.productManagerUserItem);
        this.defaultRoleGroup.addMember((Principal)this.productManagerUserItem);
        this.productManagerUser = (UserModel)this.modelService.get(this.productManagerUserItem);
        CockpitManager cockpitManager = CockpitManager.getInstance();
        Media media1 = createMedia("TestListViewConfigurationProductManager.xml");
        cockpitManager.createCockpitUIComponentConfiguration("listViewConfigurationFactory", "listViewProductManager", "Product.test", (Principal)this.defaultRoleGroup, media1);
        Media media2 = createMedia("TestListViewConfigurationCockpitUser.xml");
        cockpitManager.createCockpitUIComponentConfiguration("listViewConfigurationFactory", "listViewCockpitUser", "Product.test", (Principal)this.cockpitGroup, media2);
        Media media3 = createMedia("TestEditorProductManager.xml");
        cockpitManager.createCockpitUIComponentConfiguration("editorConfigurationFactory", "editorProductManager", "Product.test", (Principal)this.defaultRoleGroup, media3);
        Media media4 = createMedia("TestAdvancedSearchProductManager.xml");
        cockpitManager.createCockpitUIComponentConfiguration("advancedSearchConfigurationFactory", "advancedSearchProductManager", "Product.test", (Principal)this.defaultRoleGroup, media4);
        Media media5 = createMedia("TestBaseProductManager.xml");
        cockpitManager.createCockpitUIComponentConfiguration("baseConfigurationFactory", "base", "Product.test", null, media5);
        CockpitManager.getInstance().createCockpitItemTemplate("test", Product.class);
        getOrCreateLanguage("de");
        this.loginService.doLogin("productmanager", "test", null);
    }


    protected User getOrCreateUser(UserManager userManager, String login)
    {
        User ret = null;
        try
        {
            ret = userManager.getUserByLogin(login);
        }
        catch(JaloItemNotFoundException e)
        {
            try
            {
                ret = userManager.createUser(login);
            }
            catch(ConsistencyCheckException e1)
            {
                throw new JaloSystemException(e1);
            }
        }
        return ret;
    }


    protected UserGroup getOrCreateUserGroup(UserManager userManager, String groupID)
    {
        UserGroup ret;
        try
        {
            ret = userManager.getUserGroupByGroupID(groupID);
        }
        catch(JaloItemNotFoundException e)
        {
            try
            {
                ret = userManager.createUserGroup(groupID);
            }
            catch(ConsistencyCheckException e1)
            {
                throw new JaloSystemException(e1);
            }
        }
        return ret;
    }


    private Media createMedia(String file) throws JaloBusinessException
    {
        MediaManager mediaManager = MediaManager.getInstance();
        Media media = mediaManager.createMedia(randomMediaName());
        URL resource = getClass().getResource("/test/" + file);
        if(resource == null)
        {
            throw new NullPointerException("No such configuration XML file found: " + file);
        }
        media.setURL(resource.toExternalForm());
        media.setRealFileName(file);
        return media;
    }


    private String randomMediaName()
    {
        return String.format("listViewConfiguration_%s", new Object[] {Long.toString(System.currentTimeMillis())});
    }


    public void setUiConfigurationService(UIConfigurationService uiConfigurationService)
    {
        this.uiConfigurationService = uiConfigurationService;
    }


    public void setCockpitTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setLoginService(LoginService loginService)
    {
        this.loginService = loginService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
