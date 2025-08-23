package de.hybris.platform.cmscockpit.components.listsectionbrowser.impl;

import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cockpit.components.LockableGroupbox;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.ListBrowserSectionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;

public class CmsContentSlotListBrowserSectionComponent extends CmsListBrowserSectionComponent
{
    protected CMSAdminContentSlotService cmsAdminSlotService = null;
    private TypeService typeService;


    public CmsContentSlotListBrowserSectionComponent(ListBrowserSectionModel sectionModel)
    {
        super(sectionModel);
    }


    protected LockableGroupbox createSectionView()
    {
        LockableGroupbox groupBox = new LockableGroupbox();
        if(getSectionModel().getIcon() != null || getSectionModel().getPreLabel() != null)
        {
            Hbox preLabelHbox = new Hbox();
            if(getSectionModel().getIcon() != null)
            {
                Image icon = new Image(getSectionModel().getIcon());
                if(getSectionModel().getPreLabel() != null)
                {
                    icon.setSclass("advancedGroupboxIcon");
                }
                preLabelHbox.appendChild((Component)icon);
            }
            if(getSectionModel().getPreLabel() != null)
            {
                preLabelHbox.appendChild((Component)getSectionModel().getPreLabel());
            }
            Div preLabelComponent = groupBox.getPreLabelComponent();
            preLabelComponent.setSclass("advancedGroupboxPreLabel");
            preLabelComponent.appendChild((Component)preLabelHbox);
        }
        groupBox.setLabel(getSectionModel().getLabel());
        groupBox.setOpen(Boolean.TRUE.booleanValue());
        this.groupBoxContent = new Div();
        this.groupBoxContent.setSclass("browserSectionContent");
        groupBox.appendChild((Component)this.groupBoxContent);
        this.listView = loadListView();
        if(this.listView == null)
        {
            this.groupBoxContent.appendChild((Component)new Label("Nothing to display"));
        }
        else
        {
            this.groupBoxContent.appendChild((Component)this.listView);
        }
        return groupBox;
    }


    protected EventListener getAddBtnEventListener(Div captionDiv, Toolbarbutton addElementButton)
    {
        return (EventListener)new Object(this);
    }


    protected CMSAdminContentSlotService getCMSAdminContentSlotService()
    {
        if(this.cmsAdminSlotService == null)
        {
            this.cmsAdminSlotService = (CMSAdminContentSlotService)SpringUtil.getBean("cmsAdminContentSlotService");
        }
        return this.cmsAdminSlotService;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }
}
