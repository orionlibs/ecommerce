package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageLockingService;
import de.hybris.platform.cmscockpit.services.CmsCockpitService;
import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Collections;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;

public class CmsCreatePersonalizedPageAction extends AbstractListViewAction
{
    private static final String CREATE_PERSONALIZED_PAGE_AVAILABLE_ACTION_URL = "/cmscockpit/images/icon_func_new_personalized_page.png";
    private static final String CREATE_PERSONALIZED_PAGE_UNAVAILABLE_ACTION_URL = "/cmscockpit/images/icon_func_new_personalized_page_unavailable.png";
    private final Textbox nameBox = new Textbox();


    public String getImageURI(ListViewAction.Context context)
    {
        return isPageLocked(context.getItem()) ? "/cmscockpit/images/icon_func_new_personalized_page_unavailable.png" :
                        "/cmscockpit/images/icon_func_new_personalized_page.png";
    }


    public Popup getCMSPopup(ListViewAction.Context context)
    {
        if(isPageLocked(context.getItem()))
        {
            return null;
        }
        Popup namePopup = new Popup();
        namePopup.setSclass("create_pers_page_popup");
        Label label = new Label(Labels.getLabel("browser.page.createNewPersonalizedPage.enterName"));
        namePopup.appendChild((Component)label);
        namePopup.appendChild((Component)this.nameBox);
        Object object = new Object(this, namePopup, context);
        this.nameBox.addEventListener("onOK", (EventListener)object);
        this.nameBox.addEventListener("onCancel", (EventListener)new Object(this, namePopup));
        Button newButton = new Button(Labels.getLabel("general.done"));
        newButton.addEventListener("onClick", (EventListener)object);
        Div buttonDiv = new Div();
        buttonDiv.setAlign("right");
        buttonDiv.appendChild((Component)newButton);
        namePopup.appendChild((Component)buttonDiv);
        return namePopup;
    }


    protected void clonePageAndSelect(String name, ListViewAction.Context context)
    {
        TypedObject clonePageFirstLevel = getCmsCockpitService().clonePageFirstLevel(context.getItem(), name);
        if(clonePageFirstLevel == null)
        {
            UISessionUtils.getCurrentSession()
                            .getCurrentPerspective()
                            .getNotifier()
                            .setNotification(new Notification(
                                            Labels.getLabel("general.error"), Labels.getLabel("browser.page.error.createPage")));
        }
        else
        {
            BrowserModel focusedBrowser = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser();
            focusedBrowser.updateItems();
            if(focusedBrowser.getItems() != null)
            {
                int index = focusedBrowser.getItems().indexOf(clonePageFirstLevel);
                if(index >= 0)
                {
                    focusedBrowser.setSelectedIndexes(Collections.singletonList(Integer.valueOf(index)));
                }
            }
        }
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("browser.page.createNewPersonalizedPage");
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    protected CMSPageLockingService getCmsPageLockingService()
    {
        return (CMSPageLockingService)SpringUtil.getBean("cmsPageLockingService");
    }


    protected CmsCockpitService getCmsCockpitService()
    {
        return (CmsCockpitService)SpringUtil.getBean("cmsCockpitService");
    }


    protected boolean isPageLocked(TypedObject wrappedPageModel)
    {
        return getCmsPageLockingService().isPageLockedFor((AbstractPageModel)wrappedPageModel.getObject(),
                        UISessionUtils.getCurrentSession().getSystemService().getCurrentUser());
    }


    public void focus()
    {
        this.nameBox.focus();
    }
}
