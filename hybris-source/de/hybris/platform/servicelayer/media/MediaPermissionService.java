package de.hybris.platform.servicelayer.media;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import java.util.Collection;

public interface MediaPermissionService
{
    boolean isReadAccessGranted(MediaModel paramMediaModel, PrincipalModel paramPrincipalModel);


    void grantReadPermission(MediaModel paramMediaModel, PrincipalModel paramPrincipalModel);


    void denyReadPermission(MediaModel paramMediaModel, PrincipalModel paramPrincipalModel);


    Collection<PrincipalModel> getPermittedPrincipals(MediaModel paramMediaModel);


    void setPermittedPrincipals(MediaModel paramMediaModel, Collection<PrincipalModel> paramCollection);


    Collection<PrincipalModel> getDeniedPrincipals(MediaModel paramMediaModel);


    void setDeniedPrincipals(MediaModel paramMediaModel, Collection<PrincipalModel> paramCollection);
}
