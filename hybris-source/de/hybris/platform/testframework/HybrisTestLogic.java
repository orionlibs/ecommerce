package de.hybris.platform.testframework;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.order.price.PriceFactory;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.util.Config;
import java.util.Date;
import junit.framework.Assert;
import org.apache.log4j.Logger;

public class HybrisTestLogic
{
    private static final Logger LOG = Logger.getLogger(HybrisTestLogic.class);
    private final JaloSessionHolder jaloSessionHolder;
    private PriceFactory priceFactory;
    private PK userPK;
    private String userUid;
    private Date userTS;


    public HybrisTestLogic(JaloSessionHolder jaloSessionHolder)
    {
        this.jaloSessionHolder = jaloSessionHolder;
    }


    public static boolean intenseChecksActivated()
    {
        return (Registry.hasCurrentTenant() && Config.getBoolean("junit.intense.checks", false));
    }


    public void init() throws JaloSystemException
    {
        Assert.assertTrue(JaloSession.hasCurrentSession());
        JaloSession jaloSession = JaloSession.getCurrentSession();
        this.jaloSessionHolder.establishJaloSession(jaloSession);
        this.userPK = jaloSession.getUser().getPK();
        this.userUid = jaloSession.getUser().getUid();
        this.userTS = jaloSession.getUser().getModificationTime();
        this.priceFactory = JaloSession.getCurrentSession().getPriceFactory();
    }


    public void finish() throws JaloSecurityException
    {
        JaloSession jaloSession = this.jaloSessionHolder.takeJaloSession();
        if(!UserManager.getInstance().getAnonymousCustomer().equals(jaloSession.getUser()))
        {
            if(jaloSession.getUser() != null)
            {
                LOG.warn("session user(" + jaloSession.getUser() + ") has changed from (" +
                                UserManager.getInstance().getAnonymousCustomer() + ") to (" + jaloSession.getUser() + ")");
            }
        }
        else
        {
            try
            {
                if(!this.userPK.equals(jaloSession.getUser().getPK()))
                {
                    throw new IllegalStateException("session user(" + jaloSession.getUser() + ") PK has changed from (" + this.userPK + ") to (" + jaloSession
                                    .getUser().getPK() + ")");
                }
                if(!this.userUid.equals(jaloSession.getUser().getUid()))
                {
                    throw new IllegalStateException("session user(" + jaloSession.getUser() + ") uid has changed from (" + this.userUid + ") to (" + jaloSession
                                    .getUser().getUid() + ")");
                }
                if(!this.userTS.equals(jaloSession.getUser().getModificationTime()))
                {
                    LOG.warn("session user(" + jaloSession.getUser() + ") modified time has changed from (" + this.userTS + ") to (" + jaloSession
                                    .getUser().getModificationTime() + ")");
                }
            }
            catch(Exception e)
            {
                LOG.error("#####################################################################");
                LOG.error("Test " + getClass().getSimpleName() + " has interefered session user  , " + e.getMessage());
            }
        }
        jaloSession.setUser((User)UserManager.getInstance().getAnonymousCustomer());
        this.jaloSessionHolder.establishJaloSession(null);
        Assert.assertEquals("If you set jaloSession.priceFactory it need to be cleaned after test was performed.", this.priceFactory,
                        JaloSession.getCurrentSession().getPriceFactory());
    }


    public static Language getOrCreateLanguage(String isoCode) throws JaloSystemException
    {
        Language ret = null;
        try
        {
            ret = C2LManager.getInstance().getLanguageByIsoCode(isoCode);
        }
        catch(JaloItemNotFoundException e)
        {
            try
            {
                ret = C2LManager.getInstance().createLanguage(isoCode);
            }
            catch(ConsistencyCheckException e1)
            {
                throw new JaloSystemException(e1);
            }
        }
        return ret;
    }


    public static Currency getOrCreateCurrency(String isoCode) throws JaloSystemException
    {
        Currency ret = null;
        try
        {
            ret = C2LManager.getInstance().getCurrencyByIsoCode(isoCode);
        }
        catch(JaloItemNotFoundException e)
        {
            try
            {
                ret = C2LManager.getInstance().createCurrency(isoCode);
            }
            catch(ConsistencyCheckException e1)
            {
                throw new JaloSystemException(e1);
            }
        }
        return ret;
    }
}
