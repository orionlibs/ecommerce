package de.hybris.platform.basecommerce.messages.impl;

import de.hybris.platform.basecommerce.messages.ResourceBundleProvider;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.servicelayer.i18n.impl.CompositeResourceBundle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class DefaultResourceBundleProvider implements ResourceBundleProvider
{
    private static final Logger LOG = Logger.getLogger(DefaultResourceBundleProvider.class);
    private final Map<Locale, ResourceBundle> bundlesMap = new HashMap<>();
    private String resourceBundle;


    public ResourceBundle getResourceBundle(Locale locale)
    {
        CompositeResourceBundle compositeResourceBundle;
        ResourceBundle result = this.bundlesMap.get(locale);
        if(result == null)
        {
            Collection<String> extensions = ExtensionManager.getInstance().getExtensionNames();
            List<ResourceBundle> bundles = new ArrayList<>();
            for(String extension : extensions)
            {
                try
                {
                    bundles.add(getBundleWithFallback(locale, extension + "." + extension));
                }
                catch(MissingResourceException e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(extension + "/" + extension + " is NOT available" + this.resourceBundle);
                    }
                }
            }
            Collections.reverse(bundles);
            compositeResourceBundle = new CompositeResourceBundle(bundles);
            this.bundlesMap.put(locale, compositeResourceBundle);
        }
        return (ResourceBundle)compositeResourceBundle;
    }


    protected ResourceBundle getBundleWithFallback(Locale locale, String resourceKey)
    {
        ResourceBundle.Control control = ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES);
        ResourceBundle bundle = null;
        try
        {
            if(locale.equals(Locale.getDefault()))
            {
                bundle = ResourceBundle.getBundle(resourceKey, control);
            }
            else
            {
                bundle = ResourceBundle.getBundle(resourceKey, locale, control);
            }
        }
        catch(MissingResourceException e)
        {
            if(locale.equals(Locale.getDefault()))
            {
                bundle = ResourceBundle.getBundle(resourceKey);
            }
            else
            {
                bundle = ResourceBundle.getBundle(resourceKey, locale);
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Can't load fallback default resource bundle " + resourceKey + ", reason: " + e);
            }
        }
        return bundle;
    }


    public void setResourceBundle(String resourceBundle)
    {
        this.resourceBundle = resourceBundle;
    }
}
