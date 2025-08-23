package de.hybris.platform.permissionswebservices.utils;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.security.UserRightModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.security.permissions.PermissionAssignment;
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PermissionsImportUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(PermissionsImportUtils.class);


    public static void importGlobalPermission(String principalUid, String permissionName)
    {
        FlexibleSearchService flexibleSearchService = (FlexibleSearchService)Registry.getApplicationContext().getBean("flexibleSearchService", FlexibleSearchService.class);
        ModelService modelService = (ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class);
        PermissionManagementService pms = (PermissionManagementService)Registry.getApplicationContext().getBean("permissionManagementService", PermissionManagementService.class);
        UserRightModel userRightExample = new UserRightModel();
        userRightExample.setCode(permissionName);
        try
        {
            flexibleSearchService.getModelByExample(userRightExample);
        }
        catch(ModelNotFoundException e)
        {
            LOG.info("Could not find user right {} . Will be created on the fly.", userRightExample.getCode());
            modelService.save(userRightExample);
        }
        PrincipalModel principalExample = new PrincipalModel();
        principalExample.setUid(principalUid);
        PrincipalModel principal = (PrincipalModel)flexibleSearchService.getModelByExample(principalExample);
        pms.addGlobalPermission(new PermissionAssignment[] {new PermissionAssignment(permissionName, principal)});
    }
}
