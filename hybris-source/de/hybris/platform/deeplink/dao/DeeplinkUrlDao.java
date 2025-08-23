package de.hybris.platform.deeplink.dao;

import de.hybris.platform.deeplink.model.rules.DeeplinkUrlModel;
import de.hybris.platform.deeplink.model.rules.DeeplinkUrlRuleModel;
import java.util.List;

public interface DeeplinkUrlDao
{
    DeeplinkUrlModel findDeeplinkUrlModel(String paramString);


    List<DeeplinkUrlRuleModel> findDeeplinkUrlRules();


    Object findObject(String paramString);
}
