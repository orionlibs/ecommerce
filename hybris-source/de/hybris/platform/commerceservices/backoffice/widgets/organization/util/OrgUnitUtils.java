package de.hybris.platform.commerceservices.backoffice.widgets.organization.util;

import com.hybris.cockpitng.actions.ActionContext;
import de.hybris.platform.commerceservices.model.OrgUnitModel;
import org.apache.commons.lang3.StringUtils;

@Deprecated(since = "1811", forRemoval = true)
public final class OrgUnitUtils
{
    public static final String SETTING_NOTIFICATION_SOURCE = "notificationSource";


    public static String getNotificationSource(ActionContext<OrgUnitModel> ctx)
    {
        String parameter = (String)ctx.getParameter("notificationSource");
        return StringUtils.isNotBlank(parameter) ? parameter : ctx.getCode();
    }
}
