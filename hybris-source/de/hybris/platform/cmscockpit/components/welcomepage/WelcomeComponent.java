package de.hybris.platform.cmscockpit.components.welcomepage;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cmscockpit.services.CmsCockpitService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.TaskBrowserModel;
import de.hybris.platform.workflow.WorkflowActionService;
import java.util.List;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Space;
import org.zkoss.zul.Window;

public class WelcomeComponent extends Window
{
    private static final Logger LOG = Logger.getLogger(WelcomeComponent.class);
    private WorkflowActionService workflowActionService;


    public String getTaskCount()
    {
        List<String> attachments = ((BaseUICockpitNavigationArea)UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea()).getInfoSlotAttachmentTypes();
        return String.valueOf(getWorkflowActionService().getAllUserWorkflowActionsWithAttachments(attachments).size());
    }


    public String getSiteCount()
    {
        return String.valueOf(getCmsCockpitService().getWebsites().size());
    }


    public String getDataLanguage()
    {
        return UISessionUtils.getCurrentSession().getSystemService()
                        .getLanguageForLocale(UISessionUtils.getCurrentSession().getGlobalDataLocale()).getName();
    }


    public void openTaskBrowser()
    {
        UIBrowserArea browserArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().getPerspective().getBrowserArea();
        TaskBrowserModel taskBrowser = null;
        for(BrowserModel browser : browserArea.getBrowsers())
        {
            if(browser instanceof TaskBrowserModel)
            {
                taskBrowser = (TaskBrowserModel)browser;
            }
        }
        if(taskBrowser == null)
        {
            taskBrowser = new TaskBrowserModel(((BaseUICockpitNavigationArea)UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea()).getInfoSlotAttachmentTypes());
            browserArea.addVisibleBrowser(0, (BrowserModel)taskBrowser);
        }
        taskBrowser.updateItems();
        browserArea.show((BrowserModel)taskBrowser);
        browserArea.update();
    }


    public void openUserRoleMenu()
    {
        openSubMenu(1);
    }


    public void openDataLanguageMenu()
    {
        openSubMenu(0);
    }


    public void openSubMenu(int index)
    {
        try
        {
            getMainMenu().open(getMainMenu().getParent(), "after_end");
            Menu subMenu = getMainMenu().getChildren().get(index);
            ((Menupopup)subMenu.getFirstChild()).open((Component)subMenu, "after_end");
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
    }


    public Menupopup getMainMenu()
    {
        try
        {
            return (Menupopup)((BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective())
                            .getNavigationAreaComponent().getAttribute("main_menu_popup");
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }


    public void injectLastPages(Component parent)
    {
        for(AbstractPageModel page : getCmsCockpitService().getRecentlyEditedPages(5))
        {
            parent.appendChild((Component)new PageBox(page));
        }
    }


    public void injectSites(Component parent)
    {
        boolean first = true;
        for(CMSSiteModel site : getCmsCockpitService().getSites())
        {
            if(first)
            {
                first = false;
            }
            else
            {
                Space space = new Space();
                space.setOrient("vertical");
                space.setWidth("3px");
                space.setHeight("154px");
                space.setStyle("float: left; margin-right: 3px;");
                space.setBar(true);
                parent.appendChild((Component)space);
            }
            parent.appendChild((Component)new SiteBox(site));
        }
    }


    protected CmsCockpitService getCmsCockpitService()
    {
        return (CmsCockpitService)SpringUtil.getBean("cmsCockpitService");
    }


    protected WorkflowActionService getWorkflowActionService()
    {
        if(this.workflowActionService == null)
        {
            this.workflowActionService = (WorkflowActionService)SpringUtil.getBean("workflowActionService");
        }
        return this.workflowActionService;
    }
}
