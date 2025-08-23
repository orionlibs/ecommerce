package de.hybris.platform.testframework.runlistener;

import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class C2LSetupRunListener extends RunListener
{
    private static final Logger LOG = Logger.getLogger(C2LSetupRunListener.class.getName());
    private String baseCurrencyBefore;


    public void testStarted(Description description) throws Exception
    {
        readBaseCurrencySetup();
        readLanguageSetup();
    }


    public void testFinished(Description description) throws Exception
    {
        checkBaseCurrencySetup(description);
        checkLanguageSetup(description);
    }


    private final Set<String> languagesBefore = new HashSet<>();
    private final Set<String> languagesAfter = new HashSet<>();


    private void readLanguageSetup()
    {
        this.languagesBefore.clear();
        for(Language l : C2LManager.getInstance().getAllLanguages())
        {
            this.languagesBefore.add(l.getIsoCode());
        }
    }


    private void checkLanguageSetup(Description description)
    {
        this.languagesAfter.clear();
        for(Language l : C2LManager.getInstance().getAllLanguages())
        {
            this.languagesAfter.add(l.getIsoCode());
        }
        if(this.languagesBefore.size() != this.languagesAfter.size() || !this.languagesBefore.containsAll(this.languagesAfter))
        {
            LOG.error("Test " + description.getDisplayName() + " changed language setup (before:" + this.languagesBefore + ", after:" + this.languagesAfter + ")", new IllegalStateException("Illegal C2L setup change"));
        }
    }


    private void checkBaseCurrencySetup(Description description)
    {
        Currency baseCurrency = C2LManager.getInstance().getBaseCurrency();
        String baseCurrencyAfter = (baseCurrency == null) ? "<NULL>" : baseCurrency.getIsoCode();
        if(!this.baseCurrencyBefore.equalsIgnoreCase(baseCurrencyAfter))
        {
            LOG.error("Test " + description.getDisplayName() + " changed base currency (before:" + this.baseCurrencyBefore + ", after:" + baseCurrencyAfter + ")", new IllegalStateException("Illegal C2L setup change"));
        }
    }


    private void readBaseCurrencySetup()
    {
        Currency baseCurrency = C2LManager.getInstance().getBaseCurrency();
        this.baseCurrencyBefore = (baseCurrency == null) ? "<NULL>" : baseCurrency.getIsoCode();
    }
}
