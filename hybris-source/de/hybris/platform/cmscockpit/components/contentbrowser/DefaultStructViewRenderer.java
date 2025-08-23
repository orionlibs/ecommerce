package de.hybris.platform.cmscockpit.components.contentbrowser;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cockpit.components.contentbrowser.BrowserComponent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.util.DefaultCockpitTemplateParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;

public class DefaultStructViewRenderer implements StructViewRenderer
{
    private CmsPageBrowserModel browserModel = null;
    private transient Map<BrowserSectionModel, BrowserComponent> sectionMap = null;


    public DefaultStructViewRenderer(CmsPageBrowserModel model, Map<BrowserSectionModel, BrowserComponent> sectionMap)
    {
        this.browserModel = model;
        this.sectionMap = sectionMap;
    }


    public void renderStructView(TypedObject item, HtmlBasedComponent parent) throws IllegalArgumentException
    {
        AbstractPageModel currentPage = null;
        Object currentObject = item.getObject();
        if(currentObject instanceof AbstractPageModel)
        {
            currentPage = (AbstractPageModel)currentObject;
        }
        String templateLayout = (currentPage != null && currentPage.getMasterTemplate() != null) ? currentPage.getMasterTemplate().getVelocityTemplate() : "";
        if(StringUtils.isBlank(templateLayout))
        {
            throw new IllegalArgumentException("No template specified.");
        }
        DefaultCockpitTemplateParser defaultCockpitTemplateParser = new DefaultCockpitTemplateParser();
        String parsedTemplate = null;
        try
        {
            parsedTemplate = defaultCockpitTemplateParser.parseTemplate(templateLayout);
        }
        catch(IllegalArgumentException iae)
        {
            throw new IllegalArgumentException("Could not parse template.", iae);
        }
        if(StringUtils.isBlank(parsedTemplate))
        {
            throw new IllegalArgumentException("Parsed template is empty.");
        }
        CMSStructViewInjector injector = new CMSStructViewInjector(this.browserModel, this.sectionMap);
        Component templateComponent = Executions.createComponentsDirectly(parsedTemplate, "zul", (Component)parent,
                        Collections.singletonMap("injector", injector));
        templateComponent.detach();
        List<Component> children = new ArrayList<>(templateComponent.getChildren());
        for(Component child : children)
        {
            parent.appendChild(child);
        }
    }


    public CmsPageBrowserModel getBrowserModel()
    {
        return this.browserModel;
    }
}
