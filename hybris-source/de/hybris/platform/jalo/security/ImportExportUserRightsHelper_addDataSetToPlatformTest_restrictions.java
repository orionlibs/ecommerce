package de.hybris.platform.jalo.security;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@IntegrationTest
public class ImportExportUserRightsHelper_addDataSetToPlatformTest_restrictions extends ImportExportUserRightsHelper_addDataSetToPlatformTest
{
    @Resource
    public TypeService typeService;
    private static final String RESTRICTION_CODE = "userGroupSearchRestriction";


    @Before
    public void setRestrictions()
    {
        ModelService modelService = (ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class);
        TypeService typeService = (TypeService)Registry.getApplicationContext().getBean("typeService", TypeService.class);
        SearchRestrictionModel searchRestrictionModel = (SearchRestrictionModel)modelService.create(SearchRestrictionModel.class);
        searchRestrictionModel.setQuery("{pk} IS NULL");
        searchRestrictionModel.setPrincipal((PrincipalModel)getCurrentUser());
        searchRestrictionModel.setActive(Boolean.TRUE);
        searchRestrictionModel.setGenerate(Boolean.FALSE);
        searchRestrictionModel.setCode("userGroupSearchRestriction");
        searchRestrictionModel.setRestrictedType(typeService.getComposedTypeForClass(UserGroupModel.class));
        modelService.save(searchRestrictionModel);
    }


    @After
    public void clearRestrictions()
    {
        ModelService modelService = (ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class);
        FlexibleSearchService fss = (FlexibleSearchService)Registry.getApplicationContext().getBean("flexibleSearchService", FlexibleSearchService.class);
        SearchRestrictionModel restr = fss.search("select {pk} from {SearchRestriction} where {code}='userGroupSearchRestriction'").getResult().get(0);
        modelService.remove(restr);
    }


    protected static UserModel getCurrentUser()
    {
        ModelService modelService = (ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class);
        return (UserModel)modelService.toModelLayer(JaloSession.getCurrentSession().getUser());
    }


    protected void toggleRestriction(boolean toggle)
    {
        SearchRestrictionModel restr = this.flexibleSearchService.search("select {pk} from {searchrestriction} where {code}='userGroupSearchRestriction'").getResult().get(0);
        restr.setActive(Boolean.valueOf(toggle));
        this.modelService.save(restr);
    }


    protected String prefixed(String uid)
    {
        return "import_userrightshelper_restr_" + uid;
    }


    @Test
    public void shouldAssignUserToValidUserGroupWhileCreating()
    {
        String name = prefixed("T06_valid_usergroup");
        ImportExportUserRightsHelper_addDataSetToPlatformTest.ImpexRow row = new ImportExportUserRightsHelper_addDataSetToPlatformTest.ImpexRow("User", name, new String[] {"admingroup"});
        ImportResult importResult = this.importService.importData(getCompleteImpexConfig(new ImportExportUserRightsHelper_addDataSetToPlatformTest.ImpexRow[] {row}));
        toggleRestriction(false);
        UserModel user = this.userService.getUserForUID(name);
        Assert.assertTrue(importResult.isFinished());
        Assert.assertTrue(importResult.isSuccessful());
        Assert.assertEquals(0L, user.getGroups().size());
    }


    @Test
    public void shouldCreateValidUserGroup()
    {
        String name = prefixed("T02_newgroup");
        toggleRestriction(false);
        int countGroups = selectCountFrom("UserGroup");
        toggleRestriction(true);
        ImportExportUserRightsHelper_addDataSetToPlatformTest.ImpexRow row = new ImportExportUserRightsHelper_addDataSetToPlatformTest.ImpexRow("UserGroup", name, new String[] {"admingroup"});
        ImportResult importResult = this.importService.importData(getCompleteImpexConfig(new ImportExportUserRightsHelper_addDataSetToPlatformTest.ImpexRow[] {row}));
        toggleRestriction(false);
        Assert.assertTrue(importResult.isFinished());
        Assert.assertTrue(importResult.isSuccessful());
        Assert.assertEquals((countGroups + 1), selectCountFrom("UserGroup"));
        Assert.assertEquals(0L, this.userService.getUserGroupForUID(name).getGroups().size());
    }


    @Ignore
    @Test
    public void shouldNotUpdateNonuserPrincipal()
    {
    }


    @Ignore
    @Test
    public void shouldAssignUserToValidUserGroupSubtype()
    {
    }


    @Ignore
    @Test
    public void shouldModifyUserAndNotRemoveGroups()
    {
    }


    @Ignore
    @Test
    public void shouldNotUpdateUserGroupSubtypeAsBaseType()
    {
    }


    @Ignore
    @Test
    public void shouldModifyUserAndKeepGroups()
    {
    }


    @Ignore
    @Test
    public void shouldAssignUserToBothSubgroupAndSuperGroup()
    {
    }


    @Ignore
    @Test
    public void shouldCreateUserGroupAndAssignToValidOtherGroup()
    {
    }


    @Ignore
    @Test
    public void shouldNotCreateUserGroupWhenUIDExists()
    {
    }


    @Ignore
    @Test
    public void shouldAssignUserToValidUserGroupWhileUpdating()
    {
    }


    @Ignore
    @Test
    public void shouldBelongToCurrentAndSuperGroups()
    {
    }


    @Ignore
    @Test
    public void shouldCreateAndModifyUserGroupSubtype()
    {
    }


    @Ignore
    @Test
    public void shouldNotAssignUserGroupToSelfWhileModifying()
    {
    }


    @Ignore
    @Test
    public void shouldNotAssignUserGroupToSelfWhileCreating()
    {
    }


    @Ignore
    @Test
    public void shouldNotUpdateUserGroupAsItsSubtype()
    {
    }


    @Ignore
    @Test
    public void shouldAssignUserGroupToItsSubgroupAndSupergroup()
    {
    }


    @Ignore
    @Test
    public void shouldNotAssignUserGroupToNongroupPrincipal()
    {
    }


    @Ignore
    @Test
    public void shouldNotAssignUserGroupToMakeCycles()
    {
    }


    @Ignore
    @Test
    public void shouldNotUpdateUserGroupWithoutMembers()
    {
    }
}
