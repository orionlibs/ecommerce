package de.hybris.platform.ruleengine.dao;

import de.hybris.platform.ruleengine.model.DroolsKIEModuleMediaModel;
import java.util.Optional;

public interface DroolsKIEModuleMediaDao
{
    Optional<DroolsKIEModuleMediaModel> findKIEModuleMedia(String paramString1, String paramString2);
}
