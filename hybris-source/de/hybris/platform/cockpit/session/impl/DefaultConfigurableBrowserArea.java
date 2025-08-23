package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserModelListener;
import de.hybris.platform.cockpit.session.ConfigurableBrowserArea;
import de.hybris.platform.cockpit.session.ConfigurableBrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.spring.SpringUtil;

public class DefaultConfigurableBrowserArea extends AbstractBrowserArea implements ConfigurableBrowserArea
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultConfigurableBrowserArea.class);
    private String defaultBrowser;
    private final List<String> supportedBrowsers = new ArrayList<>();


    public BrowserModelListener getBrowserListener()
    {
        return null;
    }


    public void saveQuery(BrowserModel browserModel)
    {
    }


    public void initialize(Map<String, Object> params)
    {
        if(!this.initialized)
        {
            UISessionUtils.getCurrentSession().addSessionListener(this.mySessionListener);
            if(this.visibleBrowsers == null || this.visibleBrowsers.isEmpty())
            {
                List<BrowserModel> openBrowsers = createInitiallyOpenedBrowsers();
                if(CollectionUtils.isEmpty(openBrowsers))
                {
                    LOG.error("Could not create browsers. Reason: No initially open browsers configured.");
                }
                else
                {
                    List<BrowserModel> browsers = openBrowsers.subList(1, openBrowsers.size());
                    for(BrowserModel browserModel : browsers)
                    {
                        addVisibleBrowser(browserModel);
                    }
                    BrowserModel defaultBrowser = openBrowsers.get(0);
                    addVisibleBrowser(0, defaultBrowser);
                    setFocusedBrowser(defaultBrowser);
                    for(BrowserModel browserModel : openBrowsers)
                    {
                        browserModel.updateItems();
                    }
                }
            }
            this.initialized = true;
        }
    }


    protected List<BrowserModel> createInitiallyOpenedBrowsers()
    {
        List<BrowserModel> openBrowsers = new ArrayList<>();
        openBrowsers.add(createBrowser(getDefaultBrowserId()));
        for(String browserId : getSupportedBrowserIds())
        {
            if(!browserId.equals(getDefaultBrowserId()))
            {
                try
                {
                    BrowserModel browser = createBrowser(browserId);
                    if(browser instanceof ConfigurableBrowserModel && ((ConfigurableBrowserModel)browser).isInitiallyOpen())
                    {
                        openBrowsers.add(browser);
                    }
                }
                catch(Exception e)
                {
                    LOG.warn("Could not create browser with id '" + browserId + "'.", e);
                }
            }
        }
        return openBrowsers;
    }


    private BrowserModel createBrowser(String browserId)
    {
        return (BrowserModel)SpringUtil.getBean(browserId);
    }


    public BrowserModel createNewDefaultBrowser()
    {
        return (BrowserModel)SpringUtil.getBean(this.defaultBrowser);
    }


    public <T extends BrowserModel> T createBrowser(String browserId, Class<T> expectedClass)
    {
        BrowserModel browserModel1;
        T browserModel = null;
        if(isBrowserSupported(browserId))
        {
            try
            {
                browserModel1 = (BrowserModel)SpringUtil.getBean(browserId, expectedClass);
            }
            catch(ClassCastException cce)
            {
                LOG.error("Could not create browser with bean ID ='" + browserId + "'.", cce);
            }
        }
        else
        {
            LOG.error("Could not create browser with id '" + browserId + "'. Reason: Browser not supported.");
        }
        return (T)browserModel1;
    }


    public boolean providesDefaultBrowser()
    {
        return StringUtils.isNotBlank(this.defaultBrowser);
    }


    public boolean isBrowserSupported(String browserId)
    {
        boolean supported = false;
        if(StringUtils.isNotBlank(browserId) && (
                        getDefaultBrowserId().equals(browserId) || getSupportedBrowserIds().contains(browserId)))
        {
            supported = true;
        }
        return supported;
    }


    public String getDefaultBrowserId()
    {
        return this.defaultBrowser;
    }


    public List<String> getSupportedBrowserIds()
    {
        List<String> supportedIds = new ArrayList<>();
        if(!this.supportedBrowsers.contains(getDefaultBrowserId()))
        {
            supportedIds.add(getDefaultBrowserId());
        }
        supportedIds.addAll(this.supportedBrowsers);
        return Collections.unmodifiableList(supportedIds);
    }


    @Required
    public void setDefaultBrowserId(String id)
    {
        this.defaultBrowser = id;
    }


    @Required
    public void setSupportedBrowserIds(List<String> browserIds)
    {
        this.supportedBrowsers.clear();
        if(CollectionUtils.isNotEmpty(browserIds))
        {
            this.supportedBrowsers.addAll(browserIds);
        }
    }


    public BrowserModel getBrowserModel(String browserCode)
    {
        BrowserModel browser = null;
        if(StringUtils.isNotBlank(browserCode))
        {
            List<BrowserModel> browsers = getBrowsers();
            if(CollectionUtils.isNotEmpty(browsers))
            {
                for(BrowserModel browserModel : browsers)
                {
                    if(browserModel instanceof ConfigurableBrowserModel && browserCode
                                    .equals(((ConfigurableBrowserModel)browserModel).getBrowserCode()))
                    {
                        browser = browserModel;
                        break;
                    }
                }
            }
        }
        else
        {
            LOG.warn("Can not get browser. Reason: No browser code specified.");
        }
        return browser;
    }
}
