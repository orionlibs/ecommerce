package de.hybris.platform.servicelayer.media.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.security.permissions.PermissionAssignment;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;

public class MediaDeniedPrincipalsHandler extends AbstractMediaPrincipalsHandler implements DynamicAttributeHandler<Collection<PrincipalModel>, MediaModel>
{
    public Collection<PrincipalModel> get(MediaModel model)
    {
        return getDeniedPrincipals(model);
    }


    public void set(MediaModel model, Collection<PrincipalModel> principals)
    {
        Collection<PermissionAssignment> newAndCurrentPermissions = new ArrayList<>(getPermittedPermissions(model));
        if(CollectionUtils.isNotEmpty(principals))
        {
            for(PrincipalModel principal : principals)
            {
                newAndCurrentPermissions.add(new PermissionAssignment("read", principal, true));
            }
        }
        this.permissionManagementService.setItemPermissions((ItemModel)model, newAndCurrentPermissions);
    }
}
