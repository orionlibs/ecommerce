package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.impl;

import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EChangesPropertiesService;
import de.hybris.e2e.hybrisrootcauseanalysis.utils.E2EUtils;
import de.hybris.platform.licence.Licence;
import de.hybris.platform.util.Base64;
import java.util.Properties;
import org.apache.log4j.Logger;

public class DefaultLicenceChangesService implements E2EChangesPropertiesService
{
    private static final Logger LOG = Logger.getLogger(DefaultLicenceChangesService.class.getName());
    private boolean sorted;
    private String nameFile;
    private static final String PREFIX_LICENCE = "licence";


    public Properties getInfo()
    {
        Properties properties = new Properties();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Getting info from Licence");
        }
        Licence defaultLicence = Licence.getDefaultLicence();
        setToProperties(defaultLicence, properties);
        if(isSorted())
        {
            properties = E2EUtils.getSortedProperties(properties);
        }
        return properties;
    }


    protected void setToProperties(Licence defaultLicence, Properties prop)
    {
        prop.putAll(defaultLicence.getLicenceProperties());
        prop.put("licence.dayleft", E2EUtils.isNull(String.valueOf(defaultLicence.getDaysLeft())));
        prop.put("licence.demolicence.days",
                        E2EUtils.isNull(String.valueOf(defaultLicence.getDemoLicenseDays())));
        prop.put("licence.expiration.date", E2EUtils.isNull(String.valueOf(defaultLicence.getExpirationDate())));
        prop.put("licence.signature", E2EUtils.isNull(Base64.encodeBytes(defaultLicence.getSignature())));
        prop.put("licence.chachelimit", E2EUtils.isNull(String.valueOf(defaultLicence.getCacheLimit())));
    }


    public String getNameFile()
    {
        return this.nameFile;
    }


    public boolean isSorted()
    {
        return this.sorted;
    }


    public void setSorted(boolean sorted)
    {
        this.sorted = sorted;
    }


    public void setNameFile(String nameFile)
    {
        this.nameFile = nameFile;
    }
}
