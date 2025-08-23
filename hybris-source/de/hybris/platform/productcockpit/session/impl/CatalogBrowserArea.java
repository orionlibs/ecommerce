package de.hybris.platform.productcockpit.session.impl;

import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserModelListener;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractBrowserArea;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatalogBrowserArea extends AbstractBrowserArea
{
    private static final Logger LOG = LoggerFactory.getLogger(CatalogBrowserArea.class);
    private final BrowserModelListener browserListener = (BrowserModelListener)new MyBrowserListener(this, this);


    public CatalogBrowserArea()
    {
        setSplittable(true);
    }


    public void saveQuery(BrowserModel browserModel)
    {
        LOG.warn("Catalog browser area does not support saving of queries.");
    }


    public void initialize(Map<String, Object> params)
    {
        if(!this.initialized)
        {
            UISessionUtils.getCurrentSession().addSessionListener(this.mySessionListener);
            CategoryTreeBrowserModel browserTree = new CategoryTreeBrowserModel();
            browserTree.setArea((UIBrowserArea)this);
            browserTree.addBrowserModelListener(this.browserListener);
            addVisibleBrowser((BrowserModel)browserTree);
            setFocusedBrowser((BrowserModel)browserTree);
            this.initialized = true;
        }
    }


    public BrowserModelListener getBrowserListener()
    {
        return this.browserListener;
    }
}
