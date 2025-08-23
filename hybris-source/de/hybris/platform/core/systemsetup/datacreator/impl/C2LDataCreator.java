package de.hybris.platform.core.systemsetup.datacreator.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.systemsetup.datacreator.internal.CoreDataCreator;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import org.apache.log4j.Logger;

public class C2LDataCreator implements CoreDataCreator
{
    private static final Logger LOG = Logger.getLogger(C2LDataCreator.class);
    private static final String EURO_ISOCODE = "EUR";
    private static final String EURO_SYMBOL = "€";


    public void populateDatabase()
    {
        if(cleanUpArtificialCurrency() || getC2LManager().getAllCurrencies().isEmpty())
        {
            createDefaultCurrency();
        }
    }


    public Language createOrGetLanguage(String isoCode, boolean setActiveOnCreate)
    {
        Preconditions.checkArgument((isoCode != null), "isoCode is required");
        try
        {
            Language language;
            try
            {
                language = getC2LManager().getLanguageByIsoCode(isoCode);
            }
            catch(JaloItemNotFoundException e)
            {
                LOG.trace(e);
                language = getC2LManager().createLanguage(isoCode);
                language.setActive(setActiveOnCreate);
            }
            return language;
        }
        catch(ConsistencyCheckException e)
        {
            throw new SystemException(e.getMessage(), e);
        }
    }


    private void createDefaultCurrency()
    {
        logDebug("Creating default currency: EUR and setting it to current session context");
        JaloSession currentSession = getCurrentSession();
        Currency currency = createOrGetCurrency("EUR", "€");
        currency.setBase();
        currentSession.getSessionContext().setCurrency(currency);
    }


    private JaloSession getCurrentSession()
    {
        return JaloSession.getCurrentSession();
    }


    public Currency createOrGetCurrency(String isoCode, String symbol)
    {
        Preconditions.checkArgument((isoCode != null), "isoCode is required");
        Preconditions.checkArgument((symbol != null), "symbol is required");
        try
        {
            Currency currency;
            try
            {
                currency = getC2LManager().getCurrencyByIsoCode(isoCode);
            }
            catch(JaloItemNotFoundException e)
            {
                LOG.trace(e);
                currency = getC2LManager().createCurrency(isoCode);
                currency.setActive(true);
                currency.setSymbol(symbol);
                currency.setDigits(2);
                currency.setConversion(1.0D);
            }
            return currency;
        }
        catch(ConsistencyCheckException e)
        {
            throw new SystemException(e.getMessage(), e);
        }
    }


    public Country createOrGetCountry(String isoCode, boolean setActiveOnCreate)
    {
        Preconditions.checkArgument((isoCode != null), "isoCode is required");
        try
        {
            Country country;
            try
            {
                country = getC2LManager().getCountryByIsoCode(isoCode.toUpperCase());
            }
            catch(JaloItemNotFoundException e)
            {
                LOG.trace(e);
                country = getC2LManager().createCountry(isoCode.toUpperCase());
                country.setActive(setActiveOnCreate);
            }
            return country;
        }
        catch(ConsistencyCheckException e)
        {
            throw new SystemException(e.getMessage(), e);
        }
    }


    private boolean cleanUpArtificialCurrency()
    {
        try
        {
            getC2LManager().getCurrencyByIsoCode("---").remove();
            logDebug("Clearing up artificial currency '---'");
            return true;
        }
        catch(Exception e)
        {
            logDebug("No need to clear up artificial currency '---' - not existent");
            return false;
        }
    }


    private void logDebug(String msg)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(msg);
        }
    }


    private C2LManager getC2LManager()
    {
        return C2LManager.getInstance();
    }
}
