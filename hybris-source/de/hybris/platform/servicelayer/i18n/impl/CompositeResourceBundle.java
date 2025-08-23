package de.hybris.platform.servicelayer.i18n.impl;

import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class CompositeResourceBundle extends ResourceBundle
{
    public static final int KEY_FALLBACK = 1;
    public static final int VALUE_FALLBACK = 2;
    public static final int KEY_VALUE_FALLBACK = 3;
    private Collection<ResourceBundle> bundles = null;
    private boolean isKeyFallback = true;
    private boolean isValueFallback = true;


    public CompositeResourceBundle(Collection<ResourceBundle> bundles)
    {
        this(bundles, 3);
    }


    public CompositeResourceBundle(Collection<ResourceBundle> bundles, int fallback)
    {
        ServicesUtil.validateParameterNotNull(bundles, "The ressouce bundles are null!");
        this.isKeyFallback = ((fallback & 0x1) > 0);
        this.isValueFallback = ((fallback & 0x2) > 0);
        this.bundles = bundles;
    }


    public boolean isKeyFallbackEnabled()
    {
        return this.isKeyFallback;
    }


    public boolean isValueFallbackEnabled()
    {
        return this.isValueFallback;
    }


    protected Collection<ResourceBundle> getBundlesInternal()
    {
        return this.bundles;
    }


    public Enumeration<String> getKeys()
    {
        return (Enumeration<String>)new CompositeBundleEnumeration(this);
    }


    protected Object handleGetObject(String key)
    {
        String result = null;
        for(ResourceBundle bundle : this.bundles)
        {
            try
            {
                result = (String)bundle.getObject(key);
                if(!this.isValueFallback || (result != null && !result.trim().isEmpty()))
                {
                    break;
                }
            }
            catch(MissingResourceException e)
            {
                if(!this.isKeyFallback)
                {
                    throw e;
                }
            }
        }
        return result;
    }


    public static ResourceBundle getBundle(String baseName, Locale[] locales)
    {
        return getBundle(baseName, locales, null);
    }


    public static ResourceBundle getBundle(String baseName, Locale[] locales, ClassLoader loader)
    {
        Collection<ResourceBundle> bundles = new ArrayList<>();
        for(Locale locale : locales)
        {
            if(loader == null)
            {
                bundles.add(ResourceBundle.getBundle(baseName, locale));
            }
            else
            {
                bundles.add(ResourceBundle.getBundle(baseName, locale, loader));
            }
        }
        return new CompositeResourceBundle(bundles);
    }


    public static ResourceBundle getBundle(Collection<ResourceBundle> bundles)
    {
        return new CompositeResourceBundle(bundles);
    }
}
