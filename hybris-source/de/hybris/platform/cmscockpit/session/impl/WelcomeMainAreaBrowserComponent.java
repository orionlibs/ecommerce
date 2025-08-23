package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractMainAreaBrowserComponent;
import de.hybris.platform.cockpit.model.general.UIItemView;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.ComponentInjector;
import de.hybris.platform.cockpit.util.DefaultCockpitTemplateParser;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class WelcomeMainAreaBrowserComponent extends AbstractMainAreaBrowserComponent
{
    private static final Logger LOG = Logger.getLogger(WelcomeMainAreaBrowserComponent.class);
    protected static final String CMS_WELCOME_PAGE_SCLASS = "cmsWelcomePage";
    private ComponentInjector welcomePageInjector = null;


    public WelcomeMainAreaBrowserComponent(WelcomeBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super((AdvancedBrowserModel)model, contentBrowser);
    }


    public void setWelcomePageInjector(ComponentInjector welcomePageInjector)
    {
        this.welcomePageInjector = welcomePageInjector;
    }


    public ComponentInjector getWelcomePageInjector()
    {
        return this.welcomePageInjector;
    }


    public WelcomeBrowserModel getModel()
    {
        return (WelcomeBrowserModel)super.getModel();
    }


    protected Div createMainArea()
    {
        Div marginHelper = new Div();
        marginHelper.setStyle("margin: 0px; background: white;");
        UITools.maximize((HtmlBasedComponent)marginHelper);
        Div mainContainer = new Div();
        mainContainer.setHeight("99%");
        HtmlBasedComponent welcomePage = createWelcomePage();
        if(welcomePage == null)
        {
            mainContainer.appendChild((Component)new Label(Labels.getLabel("browser.section.nosections")));
        }
        else
        {
            mainContainer.appendChild((Component)welcomePage);
        }
        this.mainArea = mainContainer;
        marginHelper.appendChild((Component)mainContainer);
        return marginHelper;
    }


    protected HtmlBasedComponent createWelcomePage()
    {
        Div welcomeDiv = new Div();
        welcomeDiv.setSclass("cmsWelcomePage");
        try
        {
            injectWelcomeTemplate((Component)welcomeDiv);
        }
        catch(Exception e)
        {
            LOG.warn("Could not create welcome page. Reason: " + e.getMessage(), e);
            Label welcomeLabel = new Label("Welcome to CMS Cockpit!");
            welcomeLabel.setParent((Component)welcomeDiv);
        }
        return (HtmlBasedComponent)welcomeDiv;
    }


    protected void injectWelcomeTemplate(Component parent) throws IllegalArgumentException, IllegalStateException
    {
        DefaultCockpitTemplateParser defaultCockpitTemplateParser = new DefaultCockpitTemplateParser();
        String parsedTemplate = null;
        try
        {
            UICockpitPerspective perspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
            if(perspective instanceof CmsCockpitPerspective)
            {
                parsedTemplate = defaultCockpitTemplateParser.parseTemplate("<cockpit code=\"zulInclude\" value=\"" + getModel().getWelcomeTemplate() + "\"/>");
            }
            else
            {
                throw new IllegalStateException("Could not get welcome template. Reason: Current perspective not a CmsCockpitPerspective.");
            }
        }
        catch(IllegalArgumentException iae)
        {
            throw new IllegalArgumentException("Could not parse template.", iae);
        }
        if(StringUtils.isBlank(parsedTemplate))
        {
            throw new IllegalArgumentException("Parsed template is empty.");
        }
        ComponentInjector injector = getWelcomePageComponentInjector();
        Component templateComponent = Executions.createComponentsDirectly(parsedTemplate, "zul", parent,
                        Collections.singletonMap("injector", injector));
        templateComponent.detach();
        List<Component> children = new ArrayList<>(templateComponent.getChildren());
        for(Component child : children)
        {
            parent.appendChild(child);
        }
    }


    protected ComponentInjector getWelcomePageComponentInjector()
    {
        ComponentInjector injector = null;
        if(this.welcomePageInjector == null)
        {
            WelcomePageInjector welcomePageInjector = new WelcomePageInjector(this);
        }
        else
        {
            injector = this.welcomePageInjector;
        }
        return injector;
    }


    protected void cleanup()
    {
    }


    protected UIItemView getCurrentItemView()
    {
        return null;
    }


    public boolean update()
    {
        UITools.detachChildren((Component)this.mainArea);
        HtmlBasedComponent welcomePage = createWelcomePage();
        if(welcomePage == null)
        {
            this.mainArea.appendChild((Component)new Label(Labels.getLabel("browser.section.nosections")));
        }
        else
        {
            this.mainArea.appendChild((Component)welcomePage);
        }
        return false;
    }
}
