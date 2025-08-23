package de.hybris.platform.storelocator;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.location.impl.DistanceUnawareLocation;
import de.hybris.platform.storelocator.model.GeocodeAddressesCronJobModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import org.apache.log4j.Logger;

public class GeocodingJob extends AbstractJobPerformable<CronJobModel>
{
    private static final Logger LOG = Logger.getLogger(GeocodingJob.class.getName());
    private PointOfServiceDao pointOfServiceDao;
    private GeoWebServiceWrapper geoServiceWrapper;


    public PerformResult perform(CronJobModel cronJob)
    {
        cronJob.setLogToDatabase(Boolean.TRUE);
        cronJob.setLogToFile(Boolean.TRUE);
        try
        {
            PerformResult performResult = performInternal(cronJob);
            if(performResult != null)
            {
                return performResult;
            }
        }
        catch(Exception e)
        {
            LOG.error("Unexpected error", e);
            return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
        }
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    private PerformResult performInternal(CronJobModel cronJob)
    {
        Optional<GeocodeAddressesCronJobModel> cronJobModelOptional = validateCronJob(cronJob);
        if(cronJobModelOptional.isPresent())
        {
            GeocodeAddressesCronJobModel cronJobModel = cronJobModelOptional.get();
            Collection<PointOfServiceModel> posBatch = this.pointOfServiceDao.getPosToGeocode(cronJobModel.getBatchSize());
            for(PointOfServiceModel pos : posBatch)
            {
                if(clearAbortRequestedIfNeeded(cronJob))
                {
                    return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
                }
                storeDistanceUnawareLocation(pos);
                if(clearAbortRequestedIfNeeded(cronJob))
                {
                    return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
                }
                if(!processInternalDelay(cronJobModel))
                {
                    return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
                }
            }
        }
        else
        {
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
        }
        return null;
    }


    protected boolean processInternalDelay(GeocodeAddressesCronJobModel cronJob)
    {
        try
        {
            Thread.sleep(cronJob.getInternalDelay() * 1000L);
            return true;
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
            LOG.error("CronJob: " + cronJob.getCode() + " interrupted: " + e.getMessage());
            return false;
        }
    }


    protected void storeDistanceUnawareLocation(PointOfServiceModel pos)
    {
        DistanceUnawareLocation location = new DistanceUnawareLocation(pos);
        try
        {
            GPS gps = getGeoServiceWrapper().geocodeAddress((Location)location);
            pos.setLatitude(Double.valueOf(gps.getDecimalLatitude()));
            pos.setLongitude(Double.valueOf(gps.getDecimalLongitude()));
            pos.setGeocodeTimestamp(new Date());
            this.modelService.save(pos);
        }
        catch(GeoServiceWrapperException e)
        {
            LOG.error(buildErrorMessage(pos, location, e));
        }
    }


    protected Optional<GeocodeAddressesCronJobModel> validateCronJob(CronJobModel cronJob)
    {
        if(cronJob instanceof GeocodeAddressesCronJobModel)
        {
            GeocodeAddressesCronJobModel cronJobModel = (GeocodeAddressesCronJobModel)cronJob;
            if(cronJobModel.getBatchSize() == 0)
            {
                LOG.warn("Batch size should be greater than 0");
            }
            if(cronJobModel.getInternalDelay() == 0)
            {
                LOG.warn("Internal delay should be greater than 0");
            }
            return Optional.of(cronJobModel);
        }
        LOG.error("Unexpected cronjob type: " + cronJob);
        return Optional.empty();
    }


    protected String buildErrorMessage(PointOfServiceModel pos, DistanceUnawareLocation location, GeoServiceWrapperException geoServiceWrapperException)
    {
        AddressData address = location.getAddressData();
        StringBuilder buf = new StringBuilder();
        buf.append("Failed to Geocode POS (").append(pos.getPk()).append(") ").append(pos.getName());
        buf.append(" Error: ")
                        .append((geoServiceWrapperException.getGoogleResponseCode() == null) ? geoServiceWrapperException.getMessage() : (" Google response code: " +
                                        geoServiceWrapperException.getGoogleResponseCode()));
        buf.append(" Address: ").append(address.getStreet()).append(", ").append(address.getZip()).append(", ")
                        .append(address.getCity()).append(", ").append(address.getCountryCode());
        return buf.toString();
    }


    public boolean isAbortable()
    {
        return true;
    }


    protected PointOfServiceDao getPointOfServiceDao()
    {
        return this.pointOfServiceDao;
    }


    public void setPointOfServiceDao(PointOfServiceDao pointOfServiceDao)
    {
        this.pointOfServiceDao = pointOfServiceDao;
    }


    protected GeoWebServiceWrapper getGeoServiceWrapper()
    {
        return this.geoServiceWrapper;
    }


    public void setGeoServiceWrapper(GeoWebServiceWrapper geoServiceWrapper)
    {
        this.geoServiceWrapper = geoServiceWrapper;
    }
}
