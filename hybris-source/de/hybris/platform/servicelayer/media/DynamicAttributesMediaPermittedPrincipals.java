package de.hybris.platform.servicelayer.media;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

public class DynamicAttributesMediaPermittedPrincipals implements DynamicAttributeHandler<Collection<PrincipalModel>, MediaModel>
{
    private MediaPermissionService mediaPermissionService;


    public Collection<PrincipalModel> get(MediaModel model)
    {
        return this.mediaPermissionService.getPermittedPrincipals(model);
    }


    public void set(MediaModel model, Collection<PrincipalModel> permittedPrincipals)
    {
        this.mediaPermissionService.setPermittedPrincipals(model, permittedPrincipals);
    }


    @Required
    public void setMediaPermissionService(MediaPermissionService mediaPermissionService)
    {
        this.mediaPermissionService = mediaPermissionService;
    }
}
