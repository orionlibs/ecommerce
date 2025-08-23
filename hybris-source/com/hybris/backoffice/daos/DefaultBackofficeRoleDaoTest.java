package com.hybris.backoffice.daos;

import com.hybris.backoffice.model.user.BackofficeRoleModel;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class DefaultBackofficeRoleDaoTest extends ServicelayerTransactionalTest
{
    private BackofficeRoleModel roleSimple;
    private BackofficeRoleModel roleAdvanced;
    @Resource
    private BackofficeRoleDao backofficeRoleDao;
    @Resource
    private ModelService modelService;


    @Before
    public void setUp()
    {
        this.roleSimple = new BackofficeRoleModel();
        this.roleSimple.setUid("role_simple");
        this.modelService.save(this.roleSimple);
        this.roleAdvanced = new BackofficeRoleModel();
        this.roleAdvanced.setUid("role_advanced");
        this.modelService.save(this.roleAdvanced);
    }


    @Test
    public void testFindAllBackofficeRoles()
    {
        Set<BackofficeRoleModel> backOfficeRoles = this.backofficeRoleDao.findAllBackofficeRoles();
        Assert.assertNotNull("The object should not be null at this point", backOfficeRoles);
        Assert.assertEquals("There should be 2 BackofficeRoleModel found", 2L, backOfficeRoles.size());
        List<String> roleUids = new ArrayList<>();
        for(BackofficeRoleModel role : new ArrayList(backOfficeRoles))
        {
            roleUids.add(role.getUid());
        }
        Assert.assertTrue("The configured Uids should exists in the returned Set", roleUids
                        .containsAll(Arrays.asList((Object[])new String[] {this.roleSimple.getUid(), this.roleAdvanced.getUid()})));
    }
}
