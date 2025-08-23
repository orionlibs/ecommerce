package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.events.impl.SectionModelListener;
import de.hybris.platform.cockpit.model.general.UIItemView;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.SectionBrowserModel;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;

public class DefaultSectionMainAreaBrowserComponent extends AbstractSectionMainAreaBrowserComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSectionMainAreaBrowserComponent.class);


    public DefaultSectionMainAreaBrowserComponent(SectionBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
    }


    protected void layoutSections(List<BrowserSectionModel> sectionModels, HtmlBasedComponent parent)
    {
        for(BrowserSectionModel sectionModel : sectionModels)
        {
            BrowserComponent view = (BrowserComponent)this.sectionMap.get(sectionModel);
            if(view != null)
            {
                parent.appendChild((Component)view);
                view.initialize();
            }
        }
    }


    public void updateSelectedItems()
    {
        updateSectionSelection(getModel().getBrowserSectionModel(0));
    }


    public void setSectionModelListener(SectionModelListener sectionModelListener)
    {
        if(getModel() != null && super.getSectionModelListener() != null)
        {
            for(BrowserSectionModel sectionModel : getModel().getBrowserSectionModels())
            {
                sectionModel.removeSectionModelListener(super.getSectionModelListener());
            }
        }
        super.setSectionModelListener(sectionModelListener);
        if(getModel() != null && getSectionModelListener() != null)
        {
            for(BrowserSectionModel sectionModel : getModel().getBrowserSectionModels())
            {
                sectionModel.addSectionModelListener(getSectionModelListener());
            }
        }
    }


    protected SectionModelListener getSectionModelListener()
    {
        if(super.getSectionModelListener() == null)
        {
            super.setSectionModelListener((SectionModelListener)new MySectionModelListener(this));
        }
        return super.getSectionModelListener();
    }


    protected Div createMainArea()
    {
        return null;
    }


    protected UIItemView getCurrentItemView()
    {
        return null;
    }
}
