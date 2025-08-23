package de.hybris.platform.cmscockpit.components.contentbrowser;

import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.DefaultSectionMainAreaBrowserComponent;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.SectionBrowserModel;
import java.util.List;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Space;

public abstract class AbstractCmsPageMainAreaBrowserComponent extends DefaultSectionMainAreaBrowserComponent
{
    private static final Logger LOG = Logger.getLogger(AbstractCmsPageMainAreaBrowserComponent.class);
    protected static final String CMS_STRUC_MAIN_AREA_SCLASS = "cmsStructManArea";
    protected static final String STRUCTURE_VIEW_SECTION_SCLASS = "structureViewSection";
    protected static final String STRUCTURE_VIEW_EDITOR_RSECTION_SCLASS = "structureViewEditorSection";
    protected static final String STRUCT_VIEW_PAGE = "structView";
    protected static final String CMS_STRUCT_SECTION_SCLASS = "cmsStructSection";
    protected static final String ADD_BTN = "/cmscockpit/images/add_btn.gif";
    protected static final String SPLITTER_IMG = "/cockpit/images/splitter_grey.gif";
    protected transient HtmlBasedComponent innerParent = (HtmlBasedComponent)new Div();


    public AbstractCmsPageMainAreaBrowserComponent(SectionBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
        setSclass("cmsStructManArea");
    }


    protected void layoutSections(List<BrowserSectionModel> sectionModels, HtmlBasedComponent parent)
    {
        this.innerParent.setParent((Component)parent);
        this.innerParent.setHeight("100%");
        this.innerParent.setSclass("cmsStructSection");
        renderInternal();
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized)
        {
            if(getModel() == null)
            {
                LOG.error("It is not possible to render browser because model is empty!");
                return false;
            }
            clearParentContainer();
            this.sectionMap = createSections();
            renderInternal();
        }
        else
        {
            success = initialize();
        }
        return success;
    }


    public CmsPageBrowserModel getModel()
    {
        CmsPageBrowserModel ret = null;
        if(super.getModel() instanceof CmsPageBrowserModel)
        {
            ret = (CmsPageBrowserModel)super.getModel();
        }
        else
        {
            LOG.warn("Current model is not proper model for Structure View");
        }
        return ret;
    }


    protected boolean containsOnlyContentEditorSection()
    {
        List<BrowserSectionModel> sectionModels = getModel().getBrowserSectionModels();
        return (sectionModels.size() == 1 &&
                        getModel().getBrowserSectionModels().get(0) instanceof de.hybris.platform.cmscockpit.session.impl.ContentEditorBrowserSectionModel);
    }


    protected Space createVerticalSpacer()
    {
        Space verticalSpacer = new Space();
        verticalSpacer.setOrient("vertical");
        verticalSpacer.setHeight("5px");
        return verticalSpacer;
    }


    protected void clearParentContainer()
    {
        if(this.innerParent != null)
        {
            this.innerParent.getChildren().clear();
        }
    }


    protected abstract void renderInternal();
}
