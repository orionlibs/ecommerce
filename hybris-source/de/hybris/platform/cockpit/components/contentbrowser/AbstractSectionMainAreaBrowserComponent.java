package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.events.impl.SectionModelListener;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.BrowserSectionRenderer;
import de.hybris.platform.cockpit.session.SectionBrowserModel;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public abstract class AbstractSectionMainAreaBrowserComponent extends AbstractMainAreaBrowserComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSectionMainAreaBrowserComponent.class);
    protected transient Map<BrowserSectionModel, BrowserComponent> sectionMap = null;
    protected transient Borderlayout mainArea = null;
    protected boolean initialized = false;
    private SectionModelListener sectionModelListener;


    public AbstractSectionMainAreaBrowserComponent(SectionBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super((AdvancedBrowserModel)model, contentBrowser);
    }


    public boolean initialize()
    {
        if(!this.initialized)
        {
            this.sectionMap = null;
            if(getModel() != null)
            {
                getChildren().clear();
                setHeight("100%");
                setWidth("100%");
                this.mainArea = createMainBorderLayout();
                if(this.mainArea != null)
                {
                    appendChild((Component)this.mainArea);
                }
                this.initialized = true;
            }
        }
        return this.initialized;
    }


    protected Borderlayout createMainBorderLayout()
    {
        Borderlayout area = new Borderlayout();
        area.setWidth("100%");
        area.setHeight("100%");
        area.setSclass("plainBorderlayout query_browser_content");
        Center center = new Center();
        area.appendChild((Component)center);
        center.setAutoscroll(true);
        center.setBorder("none");
        Div centerDiv = new Div();
        center.appendChild((Component)centerDiv);
        centerDiv.setHeight("100%");
        if(getModel().getBrowserSectionModels().isEmpty())
        {
            appendChild((Component)new Label(Labels.getLabel("browser.section.nosections")));
            LOG.info("No browser sections available.");
        }
        else
        {
            this.sectionMap = createSections();
            if(this.sectionMap != null && !this.sectionMap.isEmpty())
            {
                layoutSections(getModel().getBrowserSectionModels(), (HtmlBasedComponent)centerDiv);
            }
        }
        return area;
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized)
        {
            if(this.sectionMap != null)
            {
                List<BrowserSectionModel> sectionModels = new ArrayList<>(this.sectionMap.keySet());
                for(BrowserSectionModel sectionModel : sectionModels)
                {
                    if(sectionModel.isModified())
                    {
                        BrowserComponent sectionView = this.sectionMap.get(sectionModel);
                        if(sectionView == null || UITools.isFromOtherDesktop((Component)sectionView))
                        {
                            if(LOG.isDebugEnabled())
                            {
                                LOG.debug("No valid view component found for section '" + sectionModel + "'. Ignoring update.");
                            }
                            continue;
                        }
                        sectionView.update();
                    }
                }
            }
        }
        else
        {
            success = initialize();
        }
        return success;
    }


    public SectionBrowserModel getModel()
    {
        return (SectionBrowserModel)super.getModel();
    }


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties, Object reason)
    {
        if(this.sectionMap != null && !this.sectionMap.isEmpty())
        {
            List<BrowserComponent> sectionViews = new ArrayList<>();
            sectionViews.addAll(this.sectionMap.values());
            if(reason != null && this.sectionMap.containsKey(reason))
            {
                sectionViews.remove(this.sectionMap.get(reason));
            }
            for(BrowserComponent sectionView : sectionViews)
            {
                if(sectionView != null)
                {
                    sectionView.updateItem(item, modifiedProperties);
                }
            }
        }
    }


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties)
    {
        updateItem(item, modifiedProperties, null);
    }


    public void updateSection(BrowserSectionModel sectionModel)
    {
        if(this.sectionMap != null && !this.sectionMap.isEmpty())
        {
            BrowserComponent sectionView = this.sectionMap.get(sectionModel);
            if(sectionView != null)
            {
                sectionView.update();
            }
        }
    }


    public void updateSectionSelection(BrowserSectionModel sectionModel)
    {
        if(this.sectionMap != null && !this.sectionMap.isEmpty())
        {
            BrowserComponent sectionView = this.sectionMap.get(sectionModel);
            if(sectionView != null)
            {
                sectionView.updateSelectedItems();
            }
        }
    }


    public void resize()
    {
    }


    public void setActiveItem(TypedObject activeItem)
    {
        if(this.initialized && this.sectionMap != null && !this.sectionMap.isEmpty())
        {
            for(BrowserSectionModel sectionModel : getModel().getBrowserSectionModels())
            {
                BrowserComponent sectionView = this.sectionMap.get(sectionModel);
                if(sectionView != null)
                {
                    sectionView.setActiveItem(activeItem);
                    sectionView.update();
                }
            }
        }
    }


    public void updateActiveItems()
    {
        if(this.sectionMap != null && !this.sectionMap.isEmpty())
        {
            List<BrowserComponent> sectionViews = new ArrayList<>();
            sectionViews.addAll(this.sectionMap.values());
            for(BrowserComponent sectionView : sectionViews)
            {
                if(sectionView != null)
                {
                    sectionView.updateActiveItems();
                }
            }
        }
    }


    protected Map<BrowserSectionModel, BrowserComponent> createSections()
    {
        Map<BrowserSectionModel, BrowserComponent> sectionMap = null;
        if(getModel().getBrowserSectionModels().isEmpty())
        {
            sectionMap = Collections.emptyMap();
        }
        else
        {
            sectionMap = new HashMap<>(getModel().getBrowserSectionModels().size());
            for(BrowserSectionModel sectionModel : getModel().getBrowserSectionModels())
            {
                BrowserComponent sectionView = null;
                BrowserSectionRenderer renderer = sectionModel.getBrowserSectionRenderer();
                if(renderer == null)
                {
                    LOG.warn("No section renderer has been specified. Skipping section" + (
                                    StringUtils.isBlank(sectionModel.getLabel()) ? "" : (" '" + sectionModel.getLabel() + "'")) + "...");
                }
                else
                {
                    sectionView = renderer.createSectionView(sectionModel);
                }
                sectionMap.put(sectionModel, sectionView);
                if(getSectionModelListener() != null)
                {
                    sectionModel.addSectionModelListener(getSectionModelListener());
                }
            }
        }
        return sectionMap;
    }


    public void setSectionModelListener(SectionModelListener sectionModelListener)
    {
        this.sectionModelListener = sectionModelListener;
    }


    protected SectionModelListener getSectionModelListener()
    {
        return this.sectionModelListener;
    }


    protected boolean isInitialized()
    {
        return this.initialized;
    }


    public void setParent(Component parent)
    {
        super.setParent(parent);
        if(parent == null)
        {
            cleanup();
        }
    }


    public void detach()
    {
        super.detach();
        cleanup();
    }


    public void desktopRemoved(Desktop desktop)
    {
        cleanup();
    }


    protected void cleanup()
    {
        if(getModel() != null && getModel().getBrowserSectionModels() != null)
        {
            for(BrowserSectionModel sectionModel : getModel().getBrowserSectionModels())
            {
                sectionModel.removeSectionModelListener(getSectionModelListener());
            }
        }
    }


    protected abstract void layoutSections(List<BrowserSectionModel> paramList, HtmlBasedComponent paramHtmlBasedComponent);
}
