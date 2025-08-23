package de.hybris.platform.samlsinglesignon;

import de.hybris.platform.samlsinglesignon.model.SamlUserGroupModel;
import java.util.Optional;

public interface SamlUserGroupDAO
{
    Optional<SamlUserGroupModel> findSamlUserGroup(String paramString);
}
