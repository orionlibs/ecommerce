package de.hybris.platform.licence;

import com.google.common.base.Preconditions;
import de.hybris.platform.cache.impl.CacheFactory;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.licence.internal.HybrisLicenceValidator;
import de.hybris.platform.licence.internal.ValidationResult;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.TreeSet;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class Licence implements Serializable
{
    private static final Logger LOG = Logger.getLogger(Licence.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy.MM.dd").withLocale(Locale.US);
    private static final int REGION_CACHE_LIMIT = 170000;


    public abstract Properties getLicenceProperties();


    public abstract byte[] getSignature();


    public static final Licence getDefaultLicence()
    {
        return Registry.getMasterTenant().getLicence();
    }


    public ValidationResult validateDemoExpiration()
    {
        HybrisLicenceValidator validator = new HybrisLicenceValidator();
        HybrisDataSource dataSource = Registry.getMasterTenant().getDataSource();
        boolean expired = validator.isLicenceExpiredIfDemoLicence(this, dataSource);
        logInfoAboutDemoLicense(validator, dataSource);
        if(expired)
        {
            return new ValidationResult(false, "Your demo/develop license has expired, it is valid only for 30 days. If you're using develop licence you need to reinitialize database to continue your work.");
        }
        return new ValidationResult(true, "");
    }


    private void logInfoAboutDemoLicense(HybrisLicenceValidator validator, HybrisDataSource dataSource)
    {
        LOG.info("##########################################################################");
        LOG.info("This Hybris licence is only for demo or develop usage and is valid for 30 days.");
        LOG.info("After this time you have to reinitialize database to continue your work.");
        LOG.info("Remaining days ====> " + validator.getDaysLeft(this, dataSource));
        LOG.info("##########################################################################");
    }


    public ValidationResult validate()
    {
        HybrisLicenceValidator validator = new HybrisLicenceValidator();
        return validator.checkLicence(this);
    }


    public boolean isMasterServerEnabled()
    {
        String eulaVer = getLicenceProperties().getProperty("licence.eulaversion");
        if(StringUtils.isEmpty(eulaVer))
        {
            return false;
        }
        try
        {
            BigDecimal ver = new BigDecimal(eulaVer);
            return (ver.compareTo(BigDecimal.valueOf(2L)) >= 0);
        }
        catch(NumberFormatException e)
        {
            return false;
        }
    }


    public String getLicenceProperty(String key)
    {
        return getLicenceProperties().getProperty(key);
    }


    public Date getExpirationDate()
    {
        String dateString = getLicenceProperty("licence.expiration");
        if(StringUtils.isBlank(dateString) || dateString.equalsIgnoreCase("never"))
        {
            return null;
        }
        return getLicenceDateFromString(dateString);
    }


    private Date getLicenceDateFromString(String dateString)
    {
        try
        {
            return DateTime.parse(dateString, getDateTimeFormatter()).toDate();
        }
        catch(Exception e)
        {
            LOG.error("Parsing date for license object: " + e.getMessage(), e);
            return null;
        }
    }


    protected DateTimeFormatter getDateTimeFormatter()
    {
        return FORMATTER;
    }


    public String getID()
    {
        return getLicenceProperty("licence.id");
    }


    public String getName()
    {
        return getLicenceProperty("licence.name");
    }


    public int getItemCount(String itemName)
    {
        String count = getLicenceProperty("licence.itemlimit." + itemName);
        try
        {
            return Integer.parseInt(count);
        }
        catch(NumberFormatException numberFormatException)
        {
            return -1;
        }
    }


    public boolean isAdvancedSecurityPermitted()
    {
        String mode = getLicenceProperty("licence.advancedsecurity");
        return (mode != null && mode.toUpperCase(Locale.US).trim().equals("TRUE"));
    }


    public boolean isClusteringPermitted()
    {
        String clusterMode = getLicenceProperty("licence.clustering");
        return (clusterMode != null && clusterMode.toUpperCase(Locale.US).trim().equals("TRUE"));
    }


    public boolean isHighPerformanceOptionPermitted()
    {
        String mode = getLicenceProperty("licence.highperformance");
        return (mode != null && mode.toUpperCase(Locale.US).trim().equals("TRUE"));
    }


    public int getCacheLimit()
    {
        if(isHighPerformanceOptionPermitted())
        {
            return 100000000;
        }
        if(CacheFactory.isLegacyMode(Registry.getCurrentTenant()))
        {
            return 100000;
        }
        return 170000;
    }


    public String toString()
    {
        return getLicenceText();
    }


    private String getLicenceText()
    {
        StringBuilder sb = new StringBuilder();
        Properties props = getLicenceProperties();
        if(props != null)
        {
            TreeSet keys = new TreeSet(props.keySet());
            sb.append("hybris licence [");
            for(Iterator<String> it = keys.iterator(); it.hasNext(); )
            {
                String key = it.next();
                sb.append(key);
                sb.append('=');
                sb.append(props.getProperty(key));
                sb.append(';');
            }
            sb.append("]");
        }
        return sb.toString();
    }


    public Integer getDaysLeft()
    {
        if(isDemoOrDevelopLicence())
        {
            Tenant currentTenant = Registry.getCurrentTenantNoFallback();
            Preconditions.checkNotNull(currentTenant, "no tenant active in the system");
            HybrisLicenceValidator validator = new HybrisLicenceValidator();
            return validator.getDaysLeft(this, currentTenant.getDataSource());
        }
        Date expirationDate = getExpirationDate();
        if(expirationDate != null)
        {
            DateTime _expirationDate = new DateTime(expirationDate);
            DateTime today = DateTime.now();
            return Integer.valueOf(Days.daysBetween((ReadableInstant)today.withTimeAtStartOfDay(), (ReadableInstant)_expirationDate.withTimeAtStartOfDay())
                            .getDays());
        }
        return null;
    }


    public int getDemoLicenseDays()
    {
        return 30;
    }


    public static final void checkLicenceForExtension(String name, String licencekey) throws Error
    {
        Licence licence = getDefaultLicence();
        LOG.debug("~~~ extension " + name + ": " + isTrue(licence.getLicenceProperty(licencekey)));
        boolean extensionLicence = (isUnrestrictedForAllExtensions(licence) || isTrue(licence
                        .getLicenceProperty(licencekey)));
        if(!extensionLicence)
        {
            System.err.println("*****************************************************************");
            System.err.println("*****************************************************************");
            System.err.println("**");
            System.err.println("** Licence Error - no valid licence found for extension " + name + ".");
            System.err.println("** Be sure you have installed a hybrislicence.jar which permits");
            System.err.println("** the use of this extension. ");
            System.err.println("**");
            System.err.println("*****************************************************************");
            System.err.println("*****************************************************************");
            throw new Error("Licence does not allow the use of the " + name + " Extension.");
        }
    }


    private static boolean isTrue(Object value)
    {
        return Boolean.parseBoolean((String)value);
    }


    public boolean isDemoOrDevelopLicence()
    {
        return getID().matches("0+1-.*");
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static boolean isUnrestrictedForAllExtensions(Licence license)
    {
        return license.isDemoOrDevelopLicence();
    }
}
