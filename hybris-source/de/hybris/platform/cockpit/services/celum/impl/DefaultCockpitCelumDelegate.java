package de.hybris.platform.cockpit.services.celum.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.celum.CockpitCelumDelegate;
import de.hybris.platform.cockpit.services.celum.CockpitCelumService;
import de.hybris.platform.core.model.ItemModel;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;

public class DefaultCockpitCelumDelegate implements CockpitCelumDelegate
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCockpitCelumDelegate.class);
    private static final String REMOVED_IMAGE = "/cockpit/images/stop_klein.jpg";
    private Boolean celumAvailable = null;
    private CockpitCelumService celumService = null;


    public boolean isCelumAvailable()
    {
        if(this.celumAvailable == null)
        {
            if(getCockpitCelumService() != null)
            {
                this.celumAvailable = Boolean.TRUE;
            }
            if(this.celumService != null)
            {
                this.celumAvailable = this.celumService.isCelumAlive();
            }
        }
        return (this.celumAvailable == null) ? false : this.celumAvailable.booleanValue();
    }


    public String getCelumLink(ItemModel item, String celumAssetId, Map<String, String> params) throws CelumNotAvailableException
    {
        String link = "";
        if(isCelumAvailable())
        {
            link = getCockpitCelumService().getDirectCelumLink(item, celumAssetId, params);
        }
        else
        {
            throw new CelumNotAvailableException("Failed to retrieve Celum link");
        }
        return StringUtils.isBlank(link) ? "" : link;
    }


    public String getCelumLink(String celumAssetId, Map<String, String> params) throws CelumNotAvailableException
    {
        return getCelumLink(null, celumAssetId, params);
    }


    protected CockpitCelumService getCockpitCelumService()
    {
        if(this.celumService == null)
        {
            try
            {
                this.celumService = (CockpitCelumService)SpringUtil.getBean("cockpitCelumService");
            }
            catch(Exception e)
            {
                LOG.warn("Cockpit Celum Service not available. Please check your Spring configuration.", e);
            }
        }
        return this.celumService;
    }


    public void createSynchPopup(Component button, TypedObject celumAsset, EditorListener listener) throws CelumNotAvailableException, IllegalArgumentException
    {
        if(button == null)
        {
            throw new IllegalArgumentException("Can not create synhronization dialog. Reason: Button is null");
        }
        button.addEventListener("onClick", (EventListener)new Object(this, celumAsset, listener, button));
    }


    public String getLocalMediaUrl(TypedObject celumAsset) throws CelumNotAvailableException, IllegalArgumentException
    {
        String localUrl = "";
        try
        {
            if(isCelumAvailable())
            {
                if(celumAsset == null)
                {
                    throw new IllegalArgumentException("Celum asset model is null.");
                }
                localUrl = getCockpitCelumService().getLocalUrl(celumAsset);
            }
            else
            {
                throw new CelumNotAvailableException("Failed to retrieve local media URL.");
            }
        }
        catch(Exception e)
        {
            LOG.warn("Could not get local URL.", e);
        }
        return StringUtils.isBlank(localUrl) ? "/cockpit/images/stop_klein.jpg" : localUrl;
    }


    public String getMediaUrl(TypedObject celumAsset) throws CelumNotAvailableException, IllegalArgumentException
    {
        String url = "";
        try
        {
            if(isCelumAvailable())
            {
                if(celumAsset == null)
                {
                    throw new IllegalArgumentException("Celum asset model is null.");
                }
                url = getCockpitCelumService().getCelumUrl(celumAsset);
            }
            else
            {
                throw new CelumNotAvailableException("Failed to retrieve media URL.");
            }
        }
        catch(Exception e)
        {
            LOG.warn("Could not get media URL.", e);
        }
        String randomParam = RandomStringUtils.randomNumeric(6);
        return StringUtils.isBlank(url) ? "/cockpit/images/stop_klein.jpg" : (url + "&" + url);
    }


    public void unSynchronize(TypedObject celumAsset) throws CelumNotAvailableException, IllegalArgumentException
    {
        if(isCelumAvailable())
        {
            if(celumAsset == null)
            {
                throw new IllegalArgumentException("Celum asset model is null.");
            }
            getCockpitCelumService().unSynchronize(celumAsset);
        }
        else
        {
            throw new CelumNotAvailableException("Could not un-synchronize Celum asset.");
        }
    }


    public void synchronize(TypedObject celumAsset) throws CelumNotAvailableException, IllegalArgumentException
    {
        if(isCelumAvailable())
        {
            if(celumAsset == null)
            {
                throw new IllegalArgumentException("Celum asset model is null.");
            }
            getCockpitCelumService().synchronize(celumAsset);
        }
        else
        {
            throw new CelumNotAvailableException("Could not synchronize Celum asset.");
        }
    }


    public boolean isSynchronized(TypedObject celumAsset) throws CelumNotAvailableException, IllegalArgumentException
    {
        boolean synched = true;
        if(isCelumAvailable())
        {
            if(celumAsset == null)
            {
                throw new IllegalArgumentException("Celum asset model is null.");
            }
            synched = getCockpitCelumService().isSynchronized(celumAsset);
        }
        else
        {
            throw new CelumNotAvailableException("Could not synchronize Celum asset.");
        }
        return synched;
    }


    public boolean isCelumAssetValid(TypedObject celumAsset) throws CelumNotAvailableException
    {
        boolean valid = false;
        if(isCelumAvailable())
        {
            if(celumAsset == null)
            {
                throw new IllegalArgumentException("Celum asset model is null.");
            }
            valid = getCockpitCelumService().isAssetValid(celumAsset);
        }
        else
        {
            throw new CelumNotAvailableException("Could not synchronize Celum asset.");
        }
        return valid;
    }


    public String getOriginalMediaFormat() throws CelumNotAvailableException
    {
        String origMediaFormat = null;
        if(isCelumAvailable())
        {
            origMediaFormat = getCockpitCelumService().getOriginalMediaFormat();
        }
        else
        {
            throw new CelumNotAvailableException("Could not retrieve original media format.");
        }
        return origMediaFormat;
    }


    public Integer getCelumAssetId(ItemModel asset)
    {
        return getCockpitCelumService().getCelumAssetId(asset);
    }
}
