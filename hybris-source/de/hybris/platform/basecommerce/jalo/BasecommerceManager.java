package de.hybris.platform.basecommerce.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.CronJobManager;
import de.hybris.platform.cronjob.jalo.Trigger;
import de.hybris.platform.deeplink.jalo.media.BarcodeMedia;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloTypeException;
import de.hybris.platform.ordercancel.jalo.OrderCancelConfig;
import de.hybris.platform.ordercancel.model.OrderCancelConfigModel;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerJob;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.storelocator.jalo.GeocodeAddressesCronJob;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.JspContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class BasecommerceManager extends GeneratedBasecommerceManager
{
    private static final Logger LOG = Logger.getLogger(BasecommerceManager.class.getName());
    private static final String NO_OPTION = "no";
    private static final String YES_OPTION = "yes";
    private static final String CREATE_GEOCODECRON_JOB = "create geocoding cron job";


    public BasecommerceManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of BasecommerceManager called.");
        }
    }


    public static BasecommerceManager getInstance()
    {
        return (BasecommerceManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("basecommerce");
    }


    protected void createGeocodingCronJob()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Creating geocoding cron job");
        }
        try
        {
            int batchSize = Config.getInt("geocode.cronjob.batchsize", 100);
            int internalDelay = Config.getInt("geocode.cronjob.internaldelay", 3);
            int triggerInterval = Config.getInt("geocode.cronjob.triggerinterval", 15);
            createGeocodeAddressesCronJob(Integer.valueOf(batchSize), Integer.valueOf(internalDelay), triggerInterval);
        }
        catch(NumberFormatException nfe)
        {
            LOG.error("Could not create GeocodeAddressCronJob. Batch size and 'internal delay' parameters must be integers", nfe);
        }
        catch(JaloTypeException e)
        {
            LOG.error("Could not create GeocodeAddressCronJob due to :" + e.getMessage(), (Throwable)e);
        }
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
        createOrderCancelConfig(3600);
        if(params.get("create geocoding cron job") == null || ((String)params.get("create geocoding cron job")).equals("yes"))
        {
            createGeocodingCronJob();
        }
    }


    public GeocodeAddressesCronJob createGeocodeAddressesCronJob(Integer batchSize, Integer internalDelay, int triggerInterval) throws JaloTypeException
    {
        String code = CronJobManager.getInstance().getNextCronjobNumber() + "-Geocode POS Addresses";
        return createGeocodeAddressesCronJob(code, batchSize, internalDelay, triggerInterval);
    }


    public GeocodeAddressesCronJob createGeocodeAddressesCronJob(String code, Integer batchSize, Integer internalDelay, int triggerInterval) throws JaloTypeException
    {
        JaloSession jaloSession = JaloSession.getCurrentSession();
        ComposedType geocodeAddressesCronJob = jaloSession.getTypeManager().getComposedType(GeocodeAddressesCronJob.class);
        ServicelayerJob servicelayerJob = getGeocodeAddressesJob();
        Map<String, Object> values = new HashMap<>();
        values.put("code", code);
        values.put("batchSize", batchSize);
        values.put("internalDelay", internalDelay);
        values.put("active", Boolean.TRUE);
        values.put("logToDatabase", Boolean.FALSE);
        values.put("logToFile", Boolean.FALSE);
        values.put("logLevelDatabase",
                        EnumerationManager.getInstance().getEnumerationValue("JobLogLevel", JobLogLevel.WARNING.name()));
        values.put("logLevelFile",
                        EnumerationManager.getInstance().getEnumerationValue("JobLogLevel", JobLogLevel.WARNING.name()));
        values.put("job", servicelayerJob);
        GeocodeAddressesCronJob cronJob = (GeocodeAddressesCronJob)geocodeAddressesCronJob.newInstance(values);
        CronJobManager cronjobManager = (CronJobManager)getSession().getExtensionManager().getExtension("processing");
        Trigger trigger = cronjobManager.createTrigger((CronJob)cronJob, 0, triggerInterval, 0, 0, true);
        trigger.setActive(true);
        return cronJob;
    }


    public ServicelayerJob getGeocodeAddressesJob()
    {
        FlexibleSearch flexibleSearch = FlexibleSearch.getInstance();
        String query = " select {PK} from {ServicelayerJob} where {CODE} = ?code ";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", "geocodeAddressesJob");
        List<ServicelayerJob> result = flexibleSearch.search(" select {PK} from {ServicelayerJob} where {CODE} = ?code ", parameters, ServicelayerJob.class).getResult();
        if(result == null || result.isEmpty())
        {
            throw new FlexibleSearchException("Job definition (geocodeAddressesJob) wasn't found");
        }
        if(result.size() != 1)
        {
            throw new FlexibleSearchException("Code geocodeAddressesJob is ambiguous.");
        }
        return result.get(0);
    }


    public Collection<BarcodeMedia> getBarcodes(SessionContext ctx, Product item)
    {
        String queryString = "SELECT {barcode.PK} FROM {BarcodeMedia as barcode} WHERE {barcode.contextItem} = ?contextItem";
        Map<Object, Object> params = new HashMap<>();
        params.put("contextItem", item);
        SearchResult result = FlexibleSearch.getInstance().search("SELECT {barcode.PK} FROM {BarcodeMedia as barcode} WHERE {barcode.contextItem} = ?contextItem", params,
                        Collections.singletonList(BarcodeMedia.class), true, true, 0, -1);
        return result.getResult();
    }


    protected void createOrderCancelConfig(int queueLength)
    {
        SearchResult result = FlexibleSearch.getInstance().search("SELECT {PK} FROM {OrderCancelConfig}", null,
                        Collections.singletonList(OrderCancelConfig.class), true, true, 0, -1);
        if(result.getCount() == 0)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Creating order cancel configuration");
            }
            ModelService modelService = (ModelService)Registry.getApplicationContext().getBean("modelService");
            OrderCancelConfigModel config = (OrderCancelConfigModel)modelService.create(OrderCancelConfigModel.class);
            config.setOrderCancelAllowed(true);
            config.setPartialCancelAllowed(true);
            config.setPartialOrderEntryCancelAllowed(true);
            config.setCancelAfterWarehouseAllowed(true);
            config.setQueuedOrderWaitingTime(queueLength);
            modelService.save(config);
        }
    }


    public Collection<String> getCreatorParameterNames()
    {
        List<String> parameterNames = new ArrayList<>();
        parameterNames.add("create geocoding cron job");
        return parameterNames;
    }


    public List<String> getCreatorParameterPossibleValues(String param)
    {
        List<String> parameterNames = new ArrayList<>();
        parameterNames.add("no");
        parameterNames.add("yes");
        return parameterNames;
    }
}
