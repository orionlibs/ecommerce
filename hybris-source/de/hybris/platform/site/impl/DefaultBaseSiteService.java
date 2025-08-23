package de.hybris.platform.site.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.basecommerce.site.dao.BaseSiteDao;
import de.hybris.platform.basecommerce.strategies.ActivateBaseSiteInSessionStrategy;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultBaseSiteService implements BaseSiteService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBaseSiteService.class);
    protected static final String CURRENTSITE = "currentSite";
    @Resource
    private SessionService sessionService;
    @Resource
    private BaseSiteDao baseSiteDao;
    private ActivateBaseSiteInSessionStrategy<BaseSiteModel> activateBaseSiteInSessionStrategy;


    protected ActivateBaseSiteInSessionStrategy<BaseSiteModel> getActivateBaseSiteInSessionStrategy()
    {
        return this.activateBaseSiteInSessionStrategy;
    }


    public void setActivateBaseSiteInSessionStrategy(ActivateBaseSiteInSessionStrategy<BaseSiteModel> activateBaseSiteInSessionStrategy)
    {
        this.activateBaseSiteInSessionStrategy = activateBaseSiteInSessionStrategy;
    }


    public Collection<BaseSiteModel> getAllBaseSites()
    {
        return getBaseSiteDao().findAllBaseSites();
    }


    public BaseSiteModel getBaseSiteForUID(String siteUid)
    {
        return getBaseSiteDao().findBaseSiteByUID(siteUid);
    }


    protected BaseSiteModel getCurrentBaseSiteImpl()
    {
        try
        {
            return (BaseSiteModel)getSessionService().getAttribute("currentSite");
        }
        catch(JaloObjectNoLongerValidException ex)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("Session Site no longer valid. Removing from session. getCurrentBaseSite will return null. {}", ex
                                .getMessage());
            }
            getSessionService().setAttribute("currentSite", null);
            return null;
        }
    }


    public BaseSiteModel getCurrentBaseSite()
    {
        BaseSiteModel site = getCurrentBaseSiteImpl();
        if(LOG.isDebugEnabled() && site == null)
        {
            LOG.debug("No current site set");
        }
        return site;
    }


    public List<CatalogModel> getProductCatalogs(BaseSiteModel site)
    {
        List<CatalogModel> result = new ArrayList<>();
        if(site == null)
        {
            return result;
        }
        Collection<BaseStoreModel> stores = site.getStores();
        Set<CatalogModel> collectedCatalogs = (Set<CatalogModel>)stores.stream().flatMap(store -> store.getCatalogs().stream()).filter(this::isPlainCatalogModel).collect(Collectors.toSet());
        result.addAll(collectedCatalogs);
        return result;
    }


    public void setCurrentBaseSite(String siteUid, boolean activateAdditionalSessionAdjustments)
    {
        BaseSiteModel baseSite = getBaseSiteForUID(siteUid);
        setCurrentBaseSite(baseSite, activateAdditionalSessionAdjustments);
    }


    public void setCurrentBaseSite(BaseSiteModel newBaseSite, boolean activateAdditionalSessionAdjustments)
    {
        setCurrentBaseSiteImpl(newBaseSite);
        if(activateAdditionalSessionAdjustments)
        {
            getActivateBaseSiteInSessionStrategy().activate(newBaseSite);
        }
    }


    protected boolean isPlainCatalogModel(CatalogModel catalog)
    {
        return (catalog != null && catalog.getClass() == CatalogModel.class);
    }


    protected void setCurrentBaseSiteImpl(BaseSiteModel newBaseSite)
    {
        getSessionService().setAttribute("currentSite", newBaseSite);
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected BaseSiteDao getBaseSiteDao()
    {
        return this.baseSiteDao;
    }


    @Required
    public void setBaseSiteDao(BaseSiteDao baseSiteDao)
    {
        this.baseSiteDao = baseSiteDao;
    }
}
