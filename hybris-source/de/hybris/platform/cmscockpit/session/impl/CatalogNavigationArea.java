package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.pages.AbstractPage;
import de.hybris.platform.cmscockpit.err.CmsCockpitBusinessException;
import de.hybris.platform.cmscockpit.events.impl.CmsNavigationEvent;
import de.hybris.platform.cmscockpit.session.CmsCatalogBrowserModelFactory;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.query.impl.UICollectionQuery;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;
import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

public class CatalogNavigationArea extends BaseUICockpitNavigationArea
{
    private static final Logger LOGGER = Logger.getLogger(CatalogNavigationArea.class);
    private CmsCatalogBrowserModelFactory cmsCatalogBrowserModelFactory;


    public void resetContext()
    {
        super.resetContext();
        showAllPages();
    }


    public void showAllPages()
    {
        ObjectTemplate rootType = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(GeneratedCms2Constants.TC.ABSTRACTPAGE);
        BrowserModel browserModel = getPerspective().getBrowserArea().getFocusedBrowser();
        if(browserModel instanceof DefaultSearchBrowserModel && !(browserModel instanceof de.hybris.platform.cockpit.session.impl.AbstractSectionSearchBrowserModel))
        {
            DefaultSearchBrowserModel searchBrowserModel = (DefaultSearchBrowserModel)browserModel;
            searchBrowserModel.setRootType(rootType);
            searchBrowserModel.setSimpleQuery("");
            searchBrowserModel.updateItems();
            getPerspective().getBrowserArea().update();
            searchBrowserModel.focus();
        }
        else
        {
            CmsCatalogBrowserModel cmsCatalogBrowserModel = createRequiredBrowserModel();
            getPerspective().getBrowserArea().addVisibleBrowser(0, (BrowserModel)cmsCatalogBrowserModel);
            getPerspective().getBrowserArea().setFocusedBrowser((BrowserModel)cmsCatalogBrowserModel);
            getPerspective().getBrowserArea().update();
            cmsCatalogBrowserModel.focus();
        }
    }


    protected CmsCatalogBrowserModel createRequiredBrowserModel()
    {
        ObjectTemplate rootType = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(GeneratedCms2Constants.TC.ABSTRACTPAGE);
        CmsCatalogBrowserModel cmsCatalogBrowserModel = this.cmsCatalogBrowserModelFactory.getInstance(rootType);
        cmsCatalogBrowserModel.setSimpleQuery("");
        cmsCatalogBrowserModel.updateItems();
        return cmsCatalogBrowserModel;
    }


    public int addToCollection(TypedObject item, UICollectionQuery collection, boolean multiDrag)
    {
        if(item != null && item.getObject() instanceof de.hybris.platform.cms2.model.pages.AbstractPageModel)
        {
            return super.addToCollection(item, collection, multiDrag);
        }
        Notification notification = new Notification("No items added to collection", "Only items of type '" + GeneratedCms2Constants.TC.ABSTRACTPAGE + "' can be added to collection.");
        getPerspective().getNotifier().setNotification(notification);
        return 0;
    }


    public void handlePasteOperation(String rawContent, UICollectionQuery collection)
    {
        String[] uids = rawContent.replaceAll("\\s", ";").replaceAll(";+", ";").split(";");
        List<String> uidList = new ArrayList<>(Arrays.asList(uids));
        try
        {
            ObjectCollection _objectCollection = pasteItemToCollection(
                            (collection != null) ? collection.getObjectCollection() : null, uidList, getSession().getUser());
            if(collection == null)
            {
                fireCollectionAdded(_objectCollection);
            }
            else
            {
                fireCollectionChanged(_objectCollection);
            }
        }
        catch(CmsCockpitBusinessException e)
        {
            LOGGER.info(e);
            displayMessage(Labels.getLabel("navigationarea.allcodesnotfound"));
        }
    }


    public ObjectCollection pasteItemToCollection(ObjectCollection collection, List<String> uids, UserModel user) throws CmsCockpitBusinessException
    {
        Object object = new Object(this, uids, collection, user);
        try
        {
            return (ObjectCollection)Transaction.current().execute((TransactionBody)object);
        }
        catch(Exception ex)
        {
            throw (CmsCockpitBusinessException)Transaction.toException(ex, CmsCockpitBusinessException.class);
        }
    }


    protected List<PK> findMatchedPage(List<String> uids)
    {
        List<PK> results = new ArrayList<>();
        SessionContext ctx = null;
        try
        {
            ctx = JaloSession.getCurrentSession().createLocalSessionContext();
            ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            for(String uid : uids)
            {
                AbstractPage page = (AbstractPage)ProductManager.getInstance().getFirstItemByAttribute(Product.class, "code", uid);
                if(page != null)
                {
                    results.add(page.getPK());
                }
            }
        }
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
        return results;
    }


    protected void displayMessage(String message)
    {
        try
        {
            Messagebox.show(message, "Information", 1, "z-msgbox z-msgbox-information");
        }
        catch(InterruptedException interruptException)
        {
            LOGGER.debug("Failed to display message", interruptException);
        }
    }


    public UISession getSession()
    {
        return UISessionUtils.getCurrentSession();
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        super.onCockpitEvent(event);
        if(event instanceof CmsNavigationEvent)
        {
            for(Section section : getSectionModel().getSections())
            {
                if(section instanceof CockpitEventAcceptor)
                {
                    if(((CmsNavigationEvent)event).getSite() != null)
                    {
                        ((CockpitEventAcceptor)section).onCockpitEvent(event);
                    }
                }
            }
        }
    }


    @Required
    public void setCmsCatalogBrowserModelFactory(CmsCatalogBrowserModelFactory cmsCatalogBrowserModelFactory)
    {
        this.cmsCatalogBrowserModelFactory = cmsCatalogBrowserModelFactory;
    }
}
