package de.hybris.platform.customerreview.constants;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.SingletonCreator;
import de.hybris.platform.util.config.ConfigIntf;
import org.apache.log4j.Logger;

public class CustomerReviewConstants extends GeneratedCustomerReviewConstants
{
    private static final Logger LOG = Logger.getLogger(CustomerReviewConstants.class.getName());
    private final ConfigIntf config;
    private volatile double minRating = 0.0D;
    private volatile double maxRating = 5.0D;


    private CustomerReviewConstants()
    {
        Tenant tenant = Registry.getCurrentTenantNoFallback();
        this.config = tenant.getConfig();
        this.minRating = getMinRatingConf();
        this.maxRating = getMaxRatingConf();
        this.config.registerConfigChangeListener((key, value) -> {
            if(key.equals("customerreview.minimalrating"))
            {
                this.minRating = getMinRatingConf();
            }
            else if(key.equals("customerreview.maximalrating"))
            {
                this.maxRating = getMaxRatingConf();
            }
        });
    }


    public static CustomerReviewConstants getInstance()
    {
        return (CustomerReviewConstants)Registry.getSingleton((SingletonCreator.Creator)new Object());
    }


    private double getMaxRatingConf()
    {
        double maxRatingConf = 0.0D;
        try
        {
            maxRatingConf = this.config.getDouble("customerreview.maximalrating", 5.0D);
        }
        catch(NumberFormatException e)
        {
            LOG.error("The parameter \"customerreview.maximalrating\" has illegal format (" +
                            Config.getParameter("customerreview.maximalrating") + "), using default value: 5.0");
        }
        return maxRatingConf;
    }


    private double getMinRatingConf()
    {
        double minRatingConf = 0.0D;
        try
        {
            minRatingConf = this.config.getDouble("customerreview.minimalrating", 0.0D);
        }
        catch(NumberFormatException e)
        {
            LOG.error("The parameter \"customerreview.minimalrating\" has illegal format (" +
                            Config.getParameter("customerreview.minimalrating") + "), using default value: 0.0");
        }
        return minRatingConf;
    }


    public double getMinRating()
    {
        return this.minRating;
    }


    public double getMaxRating()
    {
        return this.maxRating;
    }
}
