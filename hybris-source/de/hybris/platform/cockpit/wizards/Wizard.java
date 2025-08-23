package de.hybris.platform.cockpit.wizards;

import com.google.common.base.Preconditions;
import de.hybris.platform.cockpit.wizards.generic.AbstractGenericItemPage;
import de.hybris.platform.cockpit.wizards.impl.DefaultPage;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import de.hybris.platform.cockpit.wizards.impl.DefaultWizardContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.XulElement;

public class Wizard
{
    public static final String DO_BACK_ON_FIRST_PAGE = "doBackOnFirstPage";
    private static final String CLASSPATH_PREFIX = "classpath:";
    private static final Logger LOG = LoggerFactory.getLogger(Wizard.class);
    protected final List<Message> messages = new ArrayList<>();
    protected final Stack<String> history = new Stack<>();
    protected WizardPage currentPage;
    protected Window frameComponent;
    private String componentURI;
    private String width = "550px";
    private String height = "550px";
    private String title = "wizard";
    private boolean showNext = true;
    private boolean showBack = false;
    private boolean showDone = false;
    private boolean forceShowNext = false;
    private boolean forceHideDone = false;
    private boolean showCancel = true;
    private WizardPage previousPage;
    private List<WizardPage> pages;
    private WizardPageController defaultController;
    private WizardContext wizardContext;
    private String contextClass;
    private String pageRoot;


    public static void show(String beanID)
    {
        show(beanID, null);
    }


    public static void show(String beanID, WizardContext context)
    {
        Wizard wizard = (Wizard)SpringUtil.getBean(beanID, Wizard.class);
        Preconditions.checkArgument((wizard != null));
        wizard.setWizardContext(context);
        wizard.show();
    }


    public WizardPage getPage(String id)
    {
        WizardPage ret = null;
        for(WizardPage page : getPages())
        {
            if(page.getId() != null && page.getId().equals(id))
            {
                ret = page;
                break;
            }
        }
        return ret;
    }


    public void show()
    {
        checkShowBackOnFirstPage();
        this.frameComponent = createFrameComponent(getComponentURI());
        updateView();
        if(this.frameComponent != null)
        {
            resize();
            this.frameComponent.doHighlighted();
        }
    }


    protected void checkShowBackOnFirstPage()
    {
        if(getWizardContext().getAttribute("doBackOnFirstPage") instanceof EventListener)
        {
            this.showBack = true;
        }
    }


    protected Window createFrameComponent(String uri)
    {
        Window ret = (Window)Executions.createComponents(getComponentURI(), null,
                        Collections.singletonMap("wizardBean", this));
        ret.applyProperties();
        (new AnnotateDataBinder((Component)ret)).loadAll();
        return ret;
    }


    public void close()
    {
        if(this.frameComponent != null)
        {
            this.frameComponent.detach();
            this.frameComponent = null;
        }
    }


    public WizardPage getCurrentPage()
    {
        WizardPage ret = (this.currentPage != null) ? this.currentPage : (this.currentPage = getDefaultController().getFirstPage(this));
        if(ret == null && !this.pages.isEmpty())
        {
            ret = this.currentPage = this.pages.get(0);
        }
        return ret;
    }


    protected void setCurrentPage(WizardPage currentPage)
    {
        this.currentPage = currentPage;
    }


    public WizardPageController getCurrentController()
    {
        return (getCurrentPage().getController() != null) ? getCurrentPage().getController() : getDefaultController();
    }


    public String getComponentURI()
    {
        return (this.componentURI == null) ? "/cockpit/wizards/defaultWizardFrame.zul" : this.componentURI;
    }


    public void setComponentURI(String componentURI)
    {
        this.componentURI = componentURI;
    }


    public boolean isShowNext()
    {
        if(this.forceShowNext)
        {
            return true;
        }
        if(getCurrentController().next(this, getCurrentPage()) == null)
        {
            return false;
        }
        if(this.pages.size() > 1)
        {
            return this.showNext;
        }
        return false;
    }


    public void setShowNext(boolean showNext)
    {
        this.showNext = showNext;
    }


    public boolean isShowBack()
    {
        return this.showBack;
    }


    public void setShowBack(boolean showBack)
    {
        this.showBack = showBack;
    }


    public boolean isShowDone()
    {
        if(this.forceHideDone)
        {
            return false;
        }
        return (this.pages.size() == 1 || getCurrentController().next(this, getCurrentPage()) == null || this.showDone);
    }


    public void setShowDone(boolean showDone)
    {
        this.showDone = showDone;
    }


    public List<WizardPage> getPages()
    {
        return this.pages;
    }


    @Required
    public void setPages(List<WizardPage> pages)
    {
        for(WizardPage page : pages)
        {
            if(page instanceof DefaultPage)
            {
                ((DefaultPage)page).setWizard(this);
            }
        }
        this.pages = pages;
    }


    public WizardPageController getDefaultController()
    {
        return (this.defaultController == null) ? (WizardPageController)new DefaultPageController() : this.defaultController;
    }


    public void setDefaultController(WizardPageController defaultController)
    {
        this.defaultController = defaultController;
    }


    public Window getFrameComponent()
    {
        return this.frameComponent;
    }


    public String getWidth()
    {
        return this.width;
    }


    public void setWidth(String width)
    {
        this.width = width;
    }


    public String getHeight()
    {
        return this.height;
    }


    public void setHeight(String height)
    {
        this.height = height;
    }


    public boolean isShowCancel()
    {
        return this.showCancel;
    }


    public void setShowCancel(boolean showCancel)
    {
        this.showCancel = showCancel;
    }


    public String getTitle()
    {
        String ret = this.title;
        if(this.title != null)
        {
            ret = Labels.getLabel(this.title);
        }
        if(ret == null && this.title != null)
        {
            ret = "[" + this.title + "]";
        }
        return ret;
    }


    public void setTitle(String i3key)
    {
        this.title = i3key;
    }


    protected Component getPageContainer()
    {
        return getFrameComponent().getFellow("contentFrame");
    }


    protected void setWizardTitle(String title)
    {
        getFrameComponent().setTitle(title);
    }


    protected void updateView()
    {
        Component pageContainer = getPageContainer();
        pageContainer.getChildren().clear();
        WizardPage currentPage = getCurrentPage();
        if(currentPage instanceof AbstractGenericItemPage && ((AbstractGenericItemPage)currentPage).getWizard() == null)
        {
            ((AbstractGenericItemPage)currentPage).setWizard(this);
        }
        Component pageComp;
        pageContainer.appendChild(pageComp = createPageComponent(pageContainer, currentPage, getCurrentController()));
        resize();
        currentPage.initView(this, pageComp);
        String firstTitle = getTitle();
        String secondTitle = currentPage.getTitle();
        setWizardTitle((firstTitle != null) ? ((secondTitle != null) ? (firstTitle + " - " + firstTitle) : firstTitle) : (
                        (secondTitle != null) ? secondTitle : null));
        if(getCurrentPage() != null && getCurrentPage().equals(getCurrentController().getFirstPage(this)))
        {
            checkShowBackOnFirstPage();
        }
        setForceHideDone(false);
        setForceShowNext(false);
        getCurrentController().initPage(this, currentPage);
        refreshButtons();
    }


    protected Component createPageComponent(Component parent, WizardPage page, WizardPageController controller)
    {
        Component ret;
        parent.setVariable("pageBean", page, true);
        parent.setVariable("controllerBean", controller, true);
        parent.setVariable("wizardBean", this, true);
        String pageCmpURI = page.getComponentURI();
        if(StringUtils.isBlank(pageCmpURI))
        {
            Div div = new Div();
            if(page instanceof DefaultPage)
            {
                ((DefaultPage)page).renderView((Component)div);
            }
        }
        else
        {
            if(StringUtils.isNotBlank(getPageRoot()) && pageCmpURI.charAt(0) != '/' && !pageCmpURI.startsWith("classpath:"))
            {
                pageCmpURI = getPageRoot() + "/" + getPageRoot();
            }
            if(pageCmpURI.startsWith("classpath:"))
            {
                String viewURI = pageCmpURI.replaceFirst("classpath:", "");
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(viewURI);
                if(inputStream == null)
                {
                    LOG.error("Could not open specified uri '" + viewURI + "'");
                    Div div = new Div();
                }
                else
                {
                    try
                    {
                        ret = Executions.createComponentsDirectly(new InputStreamReader(inputStream), null, parent, null);
                    }
                    catch(IOException e)
                    {
                        LOG.error("Could not create view with URI '" + viewURI + "', reason: ", e);
                        Div div = new Div();
                    }
                }
            }
            else
            {
                ret = Executions.createComponents(pageCmpURI, parent, null);
            }
        }
        ret.applyProperties();
        List<Component> newones = parent.getChildren();
        (new AnnotateDataBinder(newones.<Component>toArray(new Component[newones.size()]))).loadAll();
        return ret;
    }


    public void refreshButtons()
    {
        if(getFrameComponent() != null)
        {
            getFrameComponent().getFellow("done").setVisible(isShowDone());
            getFrameComponent().getFellow("next").setVisible(isShowNext());
            getFrameComponent().getFellow("cancel").setVisible(isShowCancel());
            getFrameComponent().getFellow("back").setVisible(isShowBack());
        }
    }


    public void doNext()
    {
        try
        {
            WizardPage nextPage = getCurrentController().next(this, getCurrentPage());
            if(nextPage != null)
            {
                this.messages.clear();
                if(getCurrentController().validate(this, getCurrentPage()))
                {
                    if(this.history.size() == 0 || !this.currentPage.getId().equals(this.history.lastElement()))
                    {
                        this.history.push(this.currentPage.getId());
                        setPreviousPage(this.currentPage);
                    }
                    getCurrentController().beforeNext(this, getCurrentPage());
                    setCurrentPage(nextPage);
                    WizardPage currentNextPage = getCurrentController().next(this, nextPage);
                    setShowBack(true);
                    if(currentNextPage == null)
                    {
                        setShowDone(true);
                        setShowNext(false);
                    }
                    else
                    {
                        setShowDone(false);
                        setShowNext(true);
                    }
                }
            }
        }
        finally
        {
            updateView();
        }
    }


    protected void cleanupHistory()
    {
        String currentPageID = getCurrentPage().getId();
        while(this.history.size() > 0)
        {
            if(this.history.lastElement() != null && ((String)this.history.lastElement()).equals(currentPageID))
            {
                this.history.pop();
                setPreviousPage((this.history.size() > 0) ? getPage(this.history.lastElement()) : null);
                break;
            }
            this.history.pop();
        }
    }


    public void doBack()
    {
        WizardPage previousPage = getCurrentController().previous(this, getCurrentPage());
        if(previousPage != null)
        {
            this.messages.clear();
            getCurrentController().beforeBack(this, getCurrentPage());
            setCurrentPage(previousPage);
            cleanupHistory();
            setShowBack((getCurrentController().previous(this, previousPage) != null));
            setShowDone(false);
            setShowNext(true);
            updateView();
        }
        else if(getWizardContext().getAttribute("doBackOnFirstPage") instanceof EventListener)
        {
            try
            {
                ((EventListener)getWizardContext().getAttribute("doBackOnFirstPage"))
                                .onEvent((Event)new WizardEvent(this, (Component)this.frameComponent));
            }
            catch(Exception e)
            {
                LOG.error("Could not execute 'DO_BACK_ON_FIRST_PAGE', reason: ", e);
            }
        }
    }


    public void doCancel()
    {
        getCurrentController().cancel(this, getCurrentPage());
        close();
    }


    public void doDone()
    {
        this.messages.clear();
        if(getCurrentController().validate(this, getCurrentPage()))
        {
            try
            {
                getCurrentController().done(this, getCurrentPage());
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Done pressed, showing all collected attributes");
                }
                for(Map.Entry<String, Object> entry : getAllPageAttributes().entrySet())
                {
                    LOG.info((String)entry.getKey() + " = " + (String)entry.getKey());
                }
                close();
            }
            catch(RuntimeException e)
            {
                LOG.error("Could not finish wizard, reason: ", e);
                updateView();
            }
        }
        else
        {
            updateView();
        }
    }


    public Map<String, Object> getAllPageAttributes()
    {
        Map<String, Object> ret = new HashMap<>();
        for(WizardPage page : getPages())
        {
            Map<String, Object> attributes = page.getAttributes();
            if(attributes != null)
            {
                ret.putAll(attributes);
            }
        }
        return ret;
    }


    public void addMessage(Message msg)
    {
        this.messages.add(msg);
    }


    public List<Message> getMessages()
    {
        return this.messages;
    }


    public WizardPage getPreviousPage()
    {
        return this.previousPage;
    }


    protected void setPreviousPage(WizardPage previousPage)
    {
        this.previousPage = previousPage;
    }


    public void resize()
    {
        String width = getCurrentPage().getWidth();
        String height = getCurrentPage().getHeight();
        if(StringUtils.isBlank(width))
        {
            width = getWidth();
        }
        if(StringUtils.isBlank(height))
        {
            height = getHeight();
        }
        XulElement containerBox = (XulElement)this.frameComponent.getFellow("containerBox");
        containerBox.setWidth(width);
        containerBox.setHeight(height);
    }


    public WizardContext getWizardContext()
    {
        if(this.wizardContext == null)
        {
            this.wizardContext = (WizardContext)new DefaultWizardContext();
        }
        return this.wizardContext;
    }


    public void setWizardContext(WizardContext context)
    {
        if(getContextClass() != null)
        {
            try
            {
                Class<?> contextClass = Class.forName(getContextClass());
                if(context == null || !contextClass.isAssignableFrom(context.getClass()))
                {
                    LOG.error("Could not set wizard context, must be an instance of " + getContextClass() + " but is " + (
                                    (context == null) ? "null" : context.getClass().getName()));
                }
            }
            catch(ClassNotFoundException e)
            {
                LOG.error("Could not set wizard context, reason: ", e);
            }
        }
        this.wizardContext = context;
    }


    public String getContextClass()
    {
        return this.contextClass;
    }


    public void setContextClass(String contextClass)
    {
        this.contextClass = contextClass;
    }


    public boolean isForceHideDone()
    {
        return this.forceHideDone;
    }


    public void setForceHideDone(boolean forceHideDone)
    {
        this.forceHideDone = forceHideDone;
    }


    public String getPageRoot()
    {
        return this.pageRoot;
    }


    public void setPageRoot(String pageRoot)
    {
        this.pageRoot = pageRoot;
    }


    public boolean isForceShowNext()
    {
        return this.forceShowNext;
    }


    public void setForceShowNext(boolean forceShowNext)
    {
        this.forceShowNext = forceShowNext;
    }
}
