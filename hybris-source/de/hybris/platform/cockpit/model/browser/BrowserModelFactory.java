package de.hybris.platform.cockpit.model.browser;

import de.hybris.platform.cockpit.session.BrowserModel;
import org.springframework.context.ApplicationContextAware;

public interface BrowserModelFactory extends ApplicationContextAware
{
    public static final String BEAN_ID = "BrowserModelFactory";


    BrowserModel createBrowserModel(String paramString);
}
