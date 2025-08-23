package de.hybris.platform.platformbackoffice.actions.cronjob;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckingService;
import javax.annotation.Resource;

public abstract class PermissionAwareCronJobAction implements CockpitAction<CronJobModel, Object>
{
    @Resource
    private PermissionCheckingService permissionCheckingService;


    protected boolean isCurrentUserAllowedToRun(ActionContext<CronJobModel> ctx)
    {
        CronJobModel data = (CronJobModel)ctx.getData();
        if(data == null)
        {
            return false;
        }
        return this.permissionCheckingService.checkItemPermission((ItemModel)data, "change").isGranted();
    }
}
