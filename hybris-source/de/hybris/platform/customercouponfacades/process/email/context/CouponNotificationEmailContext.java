package de.hybris.platform.customercouponfacades.process.email.context;

import com.sap.security.core.server.csi.XSSEncoder;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponData;
import de.hybris.platform.customercouponservices.model.CouponNotificationProcessModel;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

public class CouponNotificationEmailContext extends AbstractEmailContext<CouponNotificationProcessModel>
{
    private Locale emailLocale;
    private static final String COUPON_TITLE = "couponTitle";
    private static final String COUPON_SUMMARY = "couponSummary";
    private static final String COUPON_NOTIFICATION_TYPE = "couponNotificationType";
    private static final String COUPON_LINK = "couponLink";
    private static final String VALIDITY_DATE = "validityDate";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String START_DATE_YEAR = "startDateYear";
    private static final String END_DATE_YEAR = "endDateYear";
    private static final String COUPON_EFFECTIVE = "COUPON_EFFECTIVE";
    private static final String COUPON_EFFECTIVE_MESSAGE = "effective";
    private static final String COUPON_EXPIRE_MESSAGE = "expire";
    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private transient Converter<CustomerCouponModel, CustomerCouponData> customerCouponConverter;
    private static final Logger LOG = Logger.getLogger(CouponNotificationEmailContext.class);


    public void init(CouponNotificationProcessModel businessProcessModel, EmailPageModel emailPageModel)
    {
        super.init((BusinessProcessModel)businessProcessModel, emailPageModel);
        setEmailLocale(businessProcessModel);
        updateBaseUrl(businessProcessModel, this.emailLocale);
        updateTitle(businessProcessModel, this.emailLocale);
        updateCouponTitle(businessProcessModel, this.emailLocale);
        updateCouponSummary(businessProcessModel, this.emailLocale);
        updateCouponNotificationType(businessProcessModel);
        updateCouponValidDate(businessProcessModel);
        updateCouponLink(businessProcessModel);
    }


    protected void updateCouponTitle(CouponNotificationProcessModel businessProcessModel, Locale emailLocale)
    {
        put("couponTitle", businessProcessModel.getCouponNotification().getCustomerCoupon().getName(emailLocale));
    }


    protected void updateCouponSummary(CouponNotificationProcessModel businessProcessModel, Locale emailLocale)
    {
        put("couponSummary", businessProcessModel.getCouponNotification().getCustomerCoupon().getDescription(emailLocale));
    }


    protected void updateCouponNotificationType(CouponNotificationProcessModel businessProcessModel)
    {
        if(businessProcessModel.getNotificationType().getCode().equals("COUPON_EFFECTIVE"))
        {
            put("couponNotificationType", "effective");
        }
        else
        {
            put("couponNotificationType", "expire");
        }
    }


    protected void updateCouponLink(CouponNotificationProcessModel businessProcessModel)
    {
        CustomerCouponData couponData = (CustomerCouponData)getCustomerCouponConverter().convert(businessProcessModel.getCouponNotification().getCustomerCoupon());
        put("couponLink", couponData.getProductUrl());
    }


    protected void updateCouponValidDate(CouponNotificationProcessModel businessProcessModel)
    {
        CustomerCouponData couponData = (CustomerCouponData)getCustomerCouponConverter().convert(businessProcessModel.getCouponNotification().getCustomerCoupon());
        DateTime startDateTime = new DateTime(couponData.getStartDate());
        DateTime endDateTime = new DateTime(couponData.getEndDate());
        String validPeriod = startDateTime.toString("yyyy-MM-dd HH:mm:ss") + " ~ " + startDateTime.toString("yyyy-MM-dd HH:mm:ss");
        put("validityDate", validPeriod);
        put("startDate", startDateTime.toString("yyyy-MM-dd HH:mm:ss"));
        put("endDate", endDateTime.toString("yyyy-MM-dd HH:mm:ss"));
        put("startDateYear", String.valueOf(startDateTime.getYear()));
        put("endDateYear", String.valueOf(endDateTime.getYear()));
    }


    protected void setEmailLocale(CouponNotificationProcessModel businessProcessModel)
    {
        String isoCode = getEmailLanguage(businessProcessModel).getIsocode();
        this.emailLocale = new Locale(isoCode);
    }


    protected void updateTitle(CouponNotificationProcessModel businessProcessModel, Locale emailLocale)
    {
        String title = "";
        if(businessProcessModel.getCouponNotification() != null && businessProcessModel
                        .getCouponNotification().getCustomer().getTitle() != null)
        {
            title = businessProcessModel.getCouponNotification().getCustomer().getTitle().getName(emailLocale);
        }
        put("title", title);
    }


    protected void updateBaseUrl(CouponNotificationProcessModel businessProcessModel, Locale emailLocale)
    {
        String baseUrl = (String)get("baseUrl");
        String baseSecrueUrl = (String)get("secureBaseUrl");
        String defaultIsoCode = businessProcessModel.getCouponNotification().getBaseSite().getDefaultLanguage().getIsocode();
        String siteIsoCode = emailLocale.getLanguage();
        put("baseUrl", baseUrl.replaceAll("/" + defaultIsoCode + "$", "/" + siteIsoCode));
        put("secureBaseUrl", baseSecrueUrl.replaceAll("/" + defaultIsoCode + "$", "/" + siteIsoCode));
    }


    protected static String encodeUrl(String url)
    {
        try
        {
            return XSSEncoder.encodeURL(url);
        }
        catch(UnsupportedEncodingException e)
        {
            LOG.error(e);
            return url;
        }
    }


    protected BaseSiteModel getSite(CouponNotificationProcessModel couponNotificationProcessModel)
    {
        return couponNotificationProcessModel.getCouponNotification().getBaseSite();
    }


    protected CustomerModel getCustomer(CouponNotificationProcessModel couponNotificationProcessModel)
    {
        return couponNotificationProcessModel.getCouponNotification().getCustomer();
    }


    protected LanguageModel getEmailLanguage(CouponNotificationProcessModel couponNotificationProcessModel)
    {
        return couponNotificationProcessModel.getLanguage();
    }


    protected Converter<CustomerCouponModel, CustomerCouponData> getCustomerCouponConverter()
    {
        return this.customerCouponConverter;
    }


    public void setCustomerCouponConverter(Converter<CustomerCouponModel, CustomerCouponData> customerCouponConverter)
    {
        this.customerCouponConverter = customerCouponConverter;
    }
}
