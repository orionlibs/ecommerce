package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cockpit.events.impl.SectionModelEvent;
import de.hybris.platform.cockpit.events.impl.SectionModelListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.ListBrowserSectionModel;
import de.hybris.platform.cockpit.session.SectionBrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

public class CmsDrilldownSectionModelListener implements SectionModelListener
{
    private static final Logger LOG = Logger.getLogger(CmsDrilldownSectionModelListener.class);
    private final SectionBrowserModel browserModel;
    private TypeService typeService = null;


    public CmsDrilldownSectionModelListener(SectionBrowserModel browserModel)
    {
        if(browserModel == null)
        {
            throw new IllegalArgumentException("Browser model cannot be null.");
        }
        this.browserModel = browserModel;
    }


    public void onSectionModelEvent(SectionModelEvent event)
    {
        if("selectionChange".equals(event.getName()))
        {
            if(getBrowserSectionModels() != null && !getBrowserSectionModels().isEmpty())
            {
                if(getBrowserSectionModels().contains(event.getSource()))
                {
                    handleSectionSelection((BrowserSectionModel)event.getSource());
                }
            }
        }
    }


    protected List<BrowserSectionModel> getBrowserSectionModels()
    {
        return this.browserModel.getBrowserSectionModels();
    }


    protected void handleSectionSelection(BrowserSectionModel currentSection)
    {
        currentSection.setModified(false);
        Integer selIndex = currentSection.getSelectedIndex();
        if(selIndex == null || selIndex.intValue() < 0 || selIndex.intValue() >= currentSection.getItems().size())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Selection not valid. Section='" + currentSection + "', selIndex='" + selIndex + "'.");
            }
            hideFollowingSections(currentSection);
        }
        else
        {
            ItemModel selectedItem = (ItemModel)((TypedObject)currentSection.getItems().get(selIndex.intValue())).getObject();
            if(selectedItem == null)
            {
                LOG.warn("Selected item could not be retrieved. Section='" + currentSection + "', selIndex='" + selIndex + "'.");
                hideFollowingSections(currentSection);
            }
            else if(currentSection.equals(getSectionBrowserModel().getContentSlotSection()))
            {
                if(selectedItem instanceof ContentSlotModel)
                {
                    handleContentSlotSectionSelection((ContentSlotModel)selectedItem);
                }
                else
                {
                    handleUnexpectedType(currentSection, selectedItem, "ContentSlotModel");
                }
            }
            else if(currentSection.equals(getSectionBrowserModel().getContentElementSection()))
            {
                if(selectedItem instanceof AbstractCMSComponentModel)
                {
                    handleContentElementSectionSelection((AbstractCMSComponentModel)selectedItem);
                }
                else
                {
                    handleUnexpectedType(currentSection, selectedItem, "AbstractCMSComponent");
                }
            }
            else if(currentSection.equals(getSectionBrowserModel().getSimpleElementSection()))
            {
                if(selectedItem instanceof SimpleCMSComponentModel)
                {
                    handleSimpleElementSectionSelection((SimpleCMSComponentModel)selectedItem);
                }
                else
                {
                    handleUnexpectedType(currentSection, selectedItem, "SimpleContentElementModel");
                }
            }
        }
    }


    protected void handleContentSlotSectionSelection(ContentSlotModel contentSlotModel)
    {
        List<TypedObject> contentElements = getTypeService().wrapItems(contentSlotModel.getCmsComponents());
        if(contentElements != null)
        {
            ListBrowserSectionModel contentElementSection = getSectionBrowserModel().getContentElementSection();
            contentElementSection.setItems(contentElements);
            if(!contentElementSection.isVisible())
            {
                contentElementSection.setVisible(true);
            }
            hideFollowingSections((BrowserSectionModel)contentElementSection);
        }
    }


    protected void handleContentElementSectionSelection(AbstractCMSComponentModel elementModel)
    {
        if(elementModel instanceof AbstractCMSComponentContainerModel)
        {
            AbstractCMSComponentContainerModel containerModel = (AbstractCMSComponentContainerModel)elementModel;
            List<TypedObject> simpleElements = getTypeService().wrapItems(containerModel.getSimpleCMSComponents());
            if(simpleElements != null)
            {
                ListBrowserSectionModel simpleElementSection = getSectionBrowserModel().getSimpleElementSection();
                simpleElementSection.update();
                if(!simpleElementSection.isVisible())
                {
                    simpleElementSection.setVisible(true);
                }
                hideFollowingSections((BrowserSectionModel)simpleElementSection);
            }
        }
        else if(elementModel instanceof SimpleCMSComponentModel)
        {
            handleSimpleElementSectionSelection((SimpleCMSComponentModel)elementModel);
        }
        else
        {
            handleUnexpectedType((BrowserSectionModel)getSectionBrowserModel().getSimpleElementSection(), (ItemModel)elementModel, "AbstractContentContainerModel or SimpleContentElementModel");
        }
    }


    protected void handleSimpleElementSectionSelection(SimpleCMSComponentModel simpleElementModel)
    {
        TypedObject contentElement = getTypeService().wrapItem(simpleElementModel);
        getSectionBrowserModel().getContentEditorSection().setRootItem(contentElement);
        if(getSectionBrowserModel().getContentEditorSection().getItems().isEmpty() ||
                        getSectionBrowserModel().getContentEditorSection().getItems().size() != 1 ||
                        !((TypedObject)getSectionBrowserModel().getContentEditorSection().getItems().get(0)).equals(contentElement))
        {
            getSectionBrowserModel().getContentEditorSection().setItems(Collections.singletonList(contentElement));
            getSectionBrowserModel().getContentEditorSection().update();
        }
        if(!getSectionBrowserModel().getContentEditorSection().isVisible())
        {
            getSectionBrowserModel().getContentEditorSection().setVisible(true);
        }
    }


    protected void hideFollowingSections(BrowserSectionModel section)
    {
        int pos = getBrowserSectionModels().indexOf(section);
        if(pos != -1)
        {
            for(int i = pos + 1; i < getBrowserSectionModels().size(); i++)
            {
                BrowserSectionModel browserSectionModel = getBrowserSectionModels().get(i);
                if(browserSectionModel.isVisible())
                {
                    browserSectionModel.setVisible(false);
                }
            }
        }
    }


    protected void handleUnexpectedType(BrowserSectionModel sectionModel, ItemModel item, String expected)
    {
        LOG.warn("Unexpected item type selected. Got item type " + item.getItemtype() + ", but expected " + expected + ".");
        hideFollowingSections(sectionModel);
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected CmsPageBrowserModel getSectionBrowserModel()
    {
        CmsPageBrowserModel ret = null;
        if(this.browserModel instanceof CmsPageBrowserModel)
        {
            ret = (CmsPageBrowserModel)this.browserModel;
        }
        return ret;
    }
}
