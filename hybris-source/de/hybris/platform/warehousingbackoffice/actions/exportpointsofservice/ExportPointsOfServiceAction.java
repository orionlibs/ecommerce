package de.hybris.platform.warehousingbackoffice.actions.exportpointsofservice;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.storelocator.model.OpeningDayModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.model.SpecialOpeningDayModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.Filedownload;

public class ExportPointsOfServiceAction implements CockpitAction<Map, Object>
{
    protected static final String CSV_HEADERS = "Store code,Business name,Address line 1,Address line 2,Locality,Administrative area,Country,Postal code,Latitude,Longitude,Primary phone,Website,Primary category,Sunday hours,Monday hours,Tuesday hours,Wednesday hours,Thursday hours,Friday hours,Saturday hours,Special hours";
    protected static final String MODEL_PAGEABLE = "pageable";
    private static final Logger LOG = LoggerFactory.getLogger(ExportPointsOfServiceAction.class);
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    @Resource
    private SiteBaseUrlResolutionService siteBaseUrlResolutionService;


    public ActionResult<Object> perform(ActionContext<Map> actionContext)
    {
        Pageable pageable = (Pageable)((Map)actionContext.getData()).get("pageable");
        String csvContent = createCsv(pageable.getAllResults());
        writeBinaryResponse(csvContent);
        return new ActionResult("success");
    }


    protected void writeBinaryResponse(String csvContent)
    {
        Filedownload.save(csvContent, "text/comma-separated-values;charset=UTF-8", "list.csv");
        LOG.info("Points of Service CSV generated");
    }


    protected String createCsv(List<PointOfServiceModel> pointsOfService)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                        "Store code,Business name,Address line 1,Address line 2,Locality,Administrative area,Country,Postal code,Latitude,Longitude,Primary phone,Website,Primary category,Sunday hours,Monday hours,Tuesday hours,Wednesday hours,Thursday hours,Friday hours,Saturday hours,Special hours\n");
        createCsvContent(stringBuilder, pointsOfService);
        return stringBuilder.toString();
    }


    protected void createCsvContent(StringBuilder stringBuilder, List<PointOfServiceModel> pointsOfService)
    {
        pointsOfService.stream().filter(this::validatePointOfServiceMeetsRequirements).forEach(pointOfService -> buildCsv(stringBuilder, pointOfService));
    }


    protected void buildCsv(StringBuilder stringBuilder, PointOfServiceModel pointOfService)
    {
        stringBuilder.append(prepareCsvValue(pointOfService.getName())).append(',');
        stringBuilder.append(prepareCsvValue(pointOfService.getDisplayName())).append(',');
        stringBuilder.append(prepareCsvValue(pointOfService.getAddress().getLine1())).append(',');
        stringBuilder.append(prepareCsvValue(pointOfService.getAddress().getLine2())).append(',');
        stringBuilder.append(prepareCsvValue(pointOfService.getAddress().getTown())).append(',');
        stringBuilder.append(prepareCsvValue(pointOfService.getAddress().getRegion().getName())).append(',');
        stringBuilder.append(prepareCsvValue(pointOfService.getAddress().getCountry().getName())).append(',');
        stringBuilder.append(prepareCsvValue(pointOfService.getAddress().getPostalcode())).append(',');
        stringBuilder
                        .append((pointOfService.getAddress().getLatitude() != null) ? pointOfService.getAddress().getLatitude() : ",");
        stringBuilder.append(
                        (pointOfService.getAddress().getLongitude() != null) ? pointOfService.getAddress().getLongitude() : ",");
        stringBuilder.append(pointOfService.getAddress().getPhone1()).append(',');
        BaseSiteModel baseSiteModel;
        String url = ((baseSiteModel = pointOfService.getBaseStore().getCmsSites().stream().findFirst().orElse(null)) != null) ? getSiteBaseUrlResolutionService().getMediaUrlForSite(baseSiteModel, true) : null;
        stringBuilder.append((url != null) ? url : "").append(',');
        stringBuilder.append(prepareCsvValue(pointOfService.getBusinessCategory())).append(',');
        stringBuilder.append(getDailySchedule(pointOfService, 1)).append(',');
        stringBuilder.append(getDailySchedule(pointOfService, 2)).append(',');
        stringBuilder.append(getDailySchedule(pointOfService, 3)).append(',');
        stringBuilder.append(getDailySchedule(pointOfService, 4)).append(',');
        stringBuilder.append(getDailySchedule(pointOfService, 5)).append(',');
        stringBuilder.append(getDailySchedule(pointOfService, 6)).append(',');
        stringBuilder.append(getDailySchedule(pointOfService, 7)).append(',');
        if(pointOfService.getOpeningSchedule() != null && !pointOfService.getOpeningSchedule().getOpeningDays().isEmpty())
        {
            stringBuilder.append(prepareCsvValue(pointOfService.getOpeningSchedule().getOpeningDays().stream()
                            .filter(day -> day instanceof SpecialOpeningDayModel)
                            .map(day -> getSpecialDaySchedule((SpecialOpeningDayModel)day)).reduce((a, b) -> a + "," + a).orElse("")));
        }
        stringBuilder.append('\n');
    }


    protected boolean validatePointOfServiceMeetsRequirements(PointOfServiceModel pointOfService)
    {
        String url;
        if(pointOfService.getBaseStore() == null)
        {
            url = null;
        }
        else
        {
            BaseSiteModel baseSiteModel;
            url = ((baseSiteModel = pointOfService.getBaseStore().getCmsSites().stream().findFirst().orElse(null)) != null) ? getSiteBaseUrlResolutionService().getMediaUrlForSite(baseSiteModel, true) : null;
        }
        return isPointOfServiceMeetsRequirements(pointOfService, url);
    }


    protected boolean isPointOfServiceMeetsRequirements(PointOfServiceModel pointOfService, String url)
    {
        boolean pointOfServiceMeetsRequirements = true;
        if(pointOfService.getAddress() == null)
        {
            pointOfServiceMeetsRequirements = false;
        }
        else if(pointOfService.getAddress().getRegion() == null || pointOfService.getAddress().getCountry() == null)
        {
            pointOfServiceMeetsRequirements = false;
        }
        else if(pointOfService.getAddress().getPhone1() == null || (pointOfService.getAddress().getPhone1().isEmpty() && (url == null || url
                        .isEmpty())))
        {
            pointOfServiceMeetsRequirements = false;
        }
        else if(pointOfService.getBusinessCategory() == null || pointOfService.getBusinessCategory()
                        .isEmpty())
        {
            pointOfServiceMeetsRequirements = false;
        }
        return pointOfServiceMeetsRequirements;
    }


    protected String getDailySchedule(PointOfServiceModel pointOfService, int dayOfWeek)
    {
        Calendar calendar = Calendar.getInstance();
        Calendar openingTime = Calendar.getInstance();
        Calendar closingTime = Calendar.getInstance();
        if(pointOfService.getOpeningSchedule() != null)
        {
            OpeningDayModel openingDay = pointOfService.getOpeningSchedule().getOpeningDays().stream().filter(day -> {
                if(!(day instanceof SpecialOpeningDayModel))
                {
                    calendar.setTime(day.getOpeningTime());
                    return (calendar.get(7) == dayOfWeek);
                }
                return false;
            }).findFirst().orElse(null);
            if(openingDay != null)
            {
                openingTime.setTime(openingDay.getOpeningTime());
                closingTime.setTime(openingDay.getClosingTime());
                return this.timeFormat.format(openingTime.getTime()) + "-" + this.timeFormat.format(openingTime.getTime());
            }
        }
        return "";
    }


    protected String getSpecialDaySchedule(SpecialOpeningDayModel specialOpeningDayModel)
    {
        Calendar calendar = Calendar.getInstance();
        StringBuilder stringBuilder = new StringBuilder();
        calendar.setTime(specialOpeningDayModel.getDate());
        stringBuilder.append(this.dateFormat.format(calendar.getTime())).append(": ");
        if(specialOpeningDayModel.isClosed())
        {
            stringBuilder.append('x');
        }
        else
        {
            Calendar openingTime = Calendar.getInstance();
            Calendar closingTime = Calendar.getInstance();
            openingTime.setTime(specialOpeningDayModel.getOpeningTime());
            closingTime.setTime(specialOpeningDayModel.getClosingTime());
            stringBuilder.append(this.timeFormat.format(openingTime.getTime())).append("-")
                            .append(this.timeFormat.format(closingTime.getTime()));
        }
        return stringBuilder.toString();
    }


    protected String prepareCsvValue(String value)
    {
        return "\"" + value.trim() + "\"";
    }


    public boolean canPerform(ActionContext<Map> ctx)
    {
        return true;
    }


    public boolean needsConfirmation(ActionContext<Map> ctx)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<Map> ctx)
    {
        return null;
    }


    protected SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
    {
        return this.siteBaseUrlResolutionService;
    }
}
