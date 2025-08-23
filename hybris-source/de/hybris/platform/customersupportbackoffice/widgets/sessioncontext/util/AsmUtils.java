package de.hybris.platform.customersupportbackoffice.widgets.sessioncontext.util;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customersupportbackoffice.widgets.sessioncontext.model.SessionContextModel;
import de.hybris.platform.util.Config;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.commons.collections.CollectionUtils;

public class AsmUtils
{
    public static final String ASM_DEEPLINK_SHOW_PARAM = "cscokpit.assistedservice.deeplink";
    public static final String ASM_DEEPLINK_PARAM = "assistedservicestorefront.deeplink.link";


    public static boolean showAsmButton()
    {
        return (Config.getBoolean("cscokpit.assistedservice.deeplink", false) && Config.getParameter("assistedservicestorefront.deeplink.link") != null);
    }


    public static String getAsmDeepLink(BaseSiteModel currentSite, SessionContextModel sessionContext)
    {
        StringBuilder deepLink = new StringBuilder();
        deepLink.append(Config.getParameter("website." + currentSite.getUid() + ".https"));
        if(null == sessionContext || null == sessionContext.getCurrentCustomer())
        {
            deepLink.append("?asm=true");
            return deepLink.toString();
        }
        CustomerType type = ((CustomerModel)sessionContext.getCurrentCustomer()).getType();
        String customerUid = encodeURLString(sessionContext.getCurrentCustomer().getUid());
        deepLink.append(Config.getParameter("assistedservicestorefront.deeplink.link") + "?customerId=" + Config.getParameter("assistedservicestorefront.deeplink.link"));
        if(sessionContext.getCurrentOrder() != null)
        {
            deepLink.append("&orderId=");
            deepLink.append(sessionContext.getCurrentOrder().getGuid());
            return deepLink.toString();
        }
        if(CollectionUtils.isEmpty(sessionContext.getCurrentCustomer().getCarts()))
        {
            return deepLink.toString();
        }
        for(CartModel cart : sessionContext.getCurrentCustomer().getCarts())
        {
            if(cart.getSite().getUid().equals(currentSite.getUid()))
            {
                deepLink.append("&cartId=");
                deepLink.append(cart.getCode());
                break;
            }
        }
        return deepLink.toString();
    }


    private static String encodeURLString(String str)
    {
        try
        {
            return URLEncoder.encode(str, "UTF-8");
        }
        catch(UnsupportedEncodingException uee)
        {
            return str;
        }
    }
}
