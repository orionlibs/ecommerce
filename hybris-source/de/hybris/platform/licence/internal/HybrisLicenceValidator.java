package de.hybris.platform.licence.internal;

import com.google.common.base.Preconditions;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.licence.Licence;
import java.util.Date;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;

public class HybrisLicenceValidator
{
    private static final Logger LOG = Logger.getLogger(HybrisLicenceValidator.class);
    public static final int DEMO_LICENCE_DAYS = 30;
    private HybrisLicenceCrypto hybrisLicenceCrypto;
    private HybrisLicenceDAO hybrisLicenceDAO;


    public ValidationResult checkLicence(Licence licence) throws IllegalStateException, NullPointerException
    {
        boolean result;
        String message;
        if(licence == null)
        {
            result = false;
            message = "no hybris licence found or wrong class format. Install a correct hybrislicence.jar.";
        }
        else if(!hasLincenceValidSignature(licence))
        {
            result = false;
            message = "hybris licence error: licence does not comply with signature.\nReason might be that you are using an old hybris 3 licence. Please request a new licence file.";
        }
        else if(licence.getExpirationDate() != null && getValidDaysLeft(licence) <= 0L)
        {
            result = false;
            message = "license is expired since " + -getValidDaysLeft(licence) + " days";
        }
        else
        {
            result = true;
            message = "";
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug(message);
        }
        return new ValidationResult(result, message);
    }


    private boolean hasLincenceValidSignature(Licence licence)
    {
        try
        {
            return getHybrisCrypto().verifyAgainstPublicKey(licence);
        }
        catch(IllegalStateException e)
        {
            return false;
        }
    }


    private long getValidDaysLeft(Licence licence)
    {
        long time = licence.getExpirationDate().getTime() - (new Date()).getTime();
        long days = Math.round(time / 8.64E7D);
        return days;
    }


    protected HybrisLicenceCrypto getHybrisCrypto()
    {
        if(this.hybrisLicenceCrypto == null)
        {
            this.hybrisLicenceCrypto = new HybrisLicenceCrypto();
        }
        return this.hybrisLicenceCrypto;
    }


    public boolean isLicenceExpiredIfDemoLicence(Licence licence, HybrisDataSource dataSource)
    {
        if(licence.isDemoOrDevelopLicence())
        {
            DateTime expirationDate = getExpirationDate(dataSource);
            DateTime now = DateTime.now();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Checking demo licence [current date: " + now + " expiration date: " + expirationDate + "]");
            }
            return now.toLocalDate().isAfter((ReadablePartial)expirationDate.toLocalDate());
        }
        return false;
    }


    public Integer getDaysLeft(Licence licence, HybrisDataSource dataSource)
    {
        if(licence.isDemoOrDevelopLicence())
        {
            DateTime expirationDate = getExpirationDate(dataSource);
            DateTime now = DateTime.now();
            return Integer.valueOf(Days.daysBetween((ReadableInstant)now.withTimeAtStartOfDay(), (ReadableInstant)expirationDate.withTimeAtStartOfDay()).getDays());
        }
        return null;
    }


    private DateTime getExpirationDate(HybrisDataSource dataSource)
    {
        Date date = getHybrisLicenceDAO().getStartingPointDateForPlatformInstance(dataSource);
        Preconditions.checkNotNull(date, "Current system is not initialized, no anonymous user has been found");
        return (new DateTime(date)).plusDays(30);
    }


    protected HybrisLicenceDAO getHybrisLicenceDAO()
    {
        if(this.hybrisLicenceDAO == null)
        {
            this.hybrisLicenceDAO = new HybrisLicenceDAO();
        }
        return this.hybrisLicenceDAO;
    }
}
