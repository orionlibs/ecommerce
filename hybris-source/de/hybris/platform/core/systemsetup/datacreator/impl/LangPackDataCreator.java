package de.hybris.platform.core.systemsetup.datacreator.impl;

import de.hybris.platform.core.systemsetup.datacreator.internal.CoreDataCreator;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.user.UserManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class LangPackDataCreator implements CoreDataCreator
{
    private static final Logger LOG = Logger.getLogger(LangPackDataCreator.class);
    private String langPackIsoCodes;
    private final Pattern unitNamePattern = Pattern.compile("unit\\.(\\w+)\\.name");
    private final Pattern userGroupNamePattern = Pattern.compile("userGroup\\.(\\w+)\\.name");
    private final Pattern languageNamePattern = Pattern.compile("language\\.(\\w+)\\.name");
    private final Pattern countryNamePattern = Pattern.compile("country\\.(\\w+)\\.name");
    private final Pattern currencyNamePattern = Pattern.compile("currency\\.(\\w+)\\.name");
    private final Pattern savedQueryNamePattern = Pattern.compile("savedQuery\\.(\\w+)\\.name");
    private final Pattern savedQueryDescriptionPattern = Pattern.compile("savedQuery\\.(\\w+)\\.description");
    private final Pattern orderStatusPattern = Pattern.compile("orderStatus\\.(\\w+)\\.name");
    private C2LDataCreator c2lDataCreator;


    public void populateDatabase()
    {
        for(Language language : createLanguages())
        {
            LOG.info("processing lang pack iso code '" + language.getIsoCode() + "'");
            localizeItemsForLanguage(language,
                            getLangPackMetaFileName(language.getIsoCode()));
        }
    }


    protected List<Language> createLanguages()
    {
        List<Language> result = new ArrayList<>();
        for(String langPackIsoCode : readConfiguredLangPackIsoCodes())
        {
            LOG.info("creating language '" + langPackIsoCode + "'");
            result.add(this.c2lDataCreator.createOrGetLanguage(langPackIsoCode, true));
        }
        return result;
    }


    protected Set<String> readConfiguredLangPackIsoCodes()
    {
        Set<String> ret = Collections.EMPTY_SET;
        if(StringUtils.isNotBlank(this.langPackIsoCodes))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("lang pack configuration is " + this.langPackIsoCodes);
            }
            ret = new LinkedHashSet<>();
            for(String token : this.langPackIsoCodes.split("[,; ]+"))
            {
                if(StringUtils.isNotBlank(token))
                {
                    ret.add(token);
                }
            }
        }
        return ret;
    }


    protected SessionContext createLocCtx(Language inLanguage)
    {
        SessionContext ctx = JaloSession.getCurrentSession().createSessionContext();
        ctx.setLanguage(inLanguage);
        return ctx;
    }


    protected void localizeItemsForLanguage(Language inLanguage, String metaFile)
    {
        Properties prop = loadLanguageConfiguration(inLanguage.getIsocode(), metaFile);
        if(prop != null)
        {
            localizeLanguages(inLanguage, prop);
            localizeCountries(inLanguage, prop);
            localizeCurrencies(inLanguage, prop);
            localizeUnits(inLanguage, prop);
            localizeUserGroups(inLanguage, prop);
            localizeSavedQueries(inLanguage, prop);
            localizeOrderStates(inLanguage, prop);
        }
    }


    protected String getLangPackMetaFileName(String langPackIsoCode)
    {
        return "lang-" + langPackIsoCode + ".properties";
    }


    protected Properties loadLanguageConfiguration(String langPackIsoCode, String metaFile)
    {
        InputStream resource = getClass().getClassLoader().getResourceAsStream(metaFile);
        if(resource != null)
        {
            Reader reader = null;
            try
            {
                reader = new InputStreamReader(resource, "utf-8");
                Properties prop = new Properties();
                prop.load(reader);
                return prop;
            }
            catch(IOException e)
            {
                LOG.warn("unable to load lang pack '" + langPackIsoCode + "' meta file " + metaFile + " due to " + e
                                .getMessage(), e);
            }
            finally
            {
                if(reader != null)
                {
                    IOUtils.closeQuietly(reader);
                }
                else
                {
                    IOUtils.closeQuietly(resource);
                }
            }
        }
        return null;
    }


    protected void localizeLanguages(Language inLanguage, Properties langPackProps)
    {
        SessionContext ctx = createLocCtx(inLanguage);
        matchAndProcessProperties(langPackProps, this.languageNamePattern, (LocalizationExecutor)new Object(this, ctx, inLanguage));
    }


    protected void localizeCountries(Language inLanguage, Properties langPackProps)
    {
        SessionContext ctx = createLocCtx(inLanguage);
        matchAndProcessProperties(langPackProps, this.countryNamePattern, (LocalizationExecutor)new Object(this, ctx, inLanguage));
    }


    protected void localizeCurrencies(Language inLanguage, Properties langPackProps)
    {
        SessionContext ctx = createLocCtx(inLanguage);
        matchAndProcessProperties(langPackProps, this.currencyNamePattern, (LocalizationExecutor)new Object(this, ctx, inLanguage));
    }


    protected void localizeSavedQueries(Language inLanguage, Properties langPackProps)
    {
        SessionContext ctx = createLocCtx(inLanguage);
        matchAndProcessProperties(langPackProps, this.savedQueryNamePattern, (LocalizationExecutor)new Object(this, ctx, inLanguage));
        matchAndProcessProperties(langPackProps, this.savedQueryDescriptionPattern, (LocalizationExecutor)new Object(this, ctx, inLanguage));
    }


    protected void localizeUserGroups(Language inLanguage, Properties langPackProps)
    {
        SessionContext ctx = createLocCtx(inLanguage);
        matchAndProcessProperties(langPackProps, this.userGroupNamePattern, (LocalizationExecutor)new Object(this, ctx, inLanguage));
    }


    protected void localizeUnits(Language inLanguage, Properties langPackProps)
    {
        SessionContext ctx = createLocCtx(inLanguage);
        matchAndProcessProperties(langPackProps, this.unitNamePattern, (LocalizationExecutor)new Object(this, ctx, inLanguage));
    }


    private void localizeOrderStates(Language inLanguage, Properties prop)
    {
        EnumerationManager enumerationManager = EnumerationManager.getInstance();
        EnumerationType orderStatusType = enumerationManager.getEnumerationType("OrderStatus");
        SessionContext ctx = createLocCtx(inLanguage);
        matchAndProcessProperties(prop, this.orderStatusPattern, (LocalizationExecutor)new Object(this, enumerationManager, orderStatusType, ctx, inLanguage));
    }


    protected ProductManager getProductManager()
    {
        return ProductManager.getInstance();
    }


    protected UserManager getUserManager()
    {
        return UserManager.getInstance();
    }


    protected FlexibleSearch getFlexibleSearch()
    {
        return FlexibleSearch.getInstance();
    }


    protected void matchAndProcessProperties(Properties langPackProperties, Pattern propertyPattern, LocalizationExecutor localizationExecutor)
    {
        for(Object key : langPackProperties.keySet())
        {
            Matcher matcher = propertyPattern.matcher((String)key);
            if(matcher.matches())
            {
                String itemToLocalizeID = matcher.group(1);
                String localizedName = langPackProperties.getProperty((String)key);
                if(StringUtils.isNotBlank(itemToLocalizeID) && StringUtils.isNotBlank(localizedName))
                {
                    try
                    {
                        localizationExecutor.execute(itemToLocalizeID, localizedName);
                    }
                    catch(JaloItemNotFoundException e)
                    {
                        LOG.debug(e);
                    }
                    catch(Exception e)
                    {
                        LOG.warn("unexpected error localizing " + itemToLocalizeID + " => " + localizedName + " using " + localizationExecutor + " : " + e
                                        .getMessage(), e);
                    }
                }
            }
        }
    }


    private C2LManager getC2LManager()
    {
        return C2LManager.getInstance();
    }


    private void logDebug(String msg)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(msg);
        }
    }


    @Required
    public void setLangPackIsoCodes(String langPackIsoCodes)
    {
        this.langPackIsoCodes = langPackIsoCodes;
    }


    @Required
    public void setC2lDataCreator(C2LDataCreator c2lDataCreator)
    {
        this.c2lDataCreator = c2lDataCreator;
    }
}
