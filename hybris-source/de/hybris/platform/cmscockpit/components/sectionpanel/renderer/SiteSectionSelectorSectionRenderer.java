package de.hybris.platform.cmscockpit.components.sectionpanel.renderer;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cmscockpit.services.GenericRandomNameProducer;
import de.hybris.platform.cockpit.components.navigationarea.renderer.DefaultSectionSelectorSectionRenderer;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Toolbarbutton;

public class SiteSectionSelectorSectionRenderer extends DefaultSectionSelectorSectionRenderer
{
    protected static final String ADD_BTN = "/cmscockpit/images/add_btn.gif";
    protected static final String REMOVE_BTN = "/cmscockpit/images/cnt_elem_remove_action.png";
    protected static final String CAPTION_IMAGE_LABEL_PANEL = "captionImageLabelPanel";
    protected static final String CMSSITE_UID = "CMSSite.uid";
    protected static final String CMSSITE_UID_PREFIX = "site";
    protected static final String DEFAULT_ELEMENT_IMAGE = "/cockpit/images/defaultWizardNode.gif";
    protected static final String COCKPIT_ID_NAVIGATION_AREA_WEBSITES_ADD_BUTTON = "NavigationArea_Websites_Add_button";
    private SiteSelectorListRenderer listItemRenderer = null;
    private SynchronizationService synchronizationService;


    public GenericRandomNameProducer getGenericRandomNameProducer()
    {
        return (GenericRandomNameProducer)SpringUtil.getBean("genericRandomNameProducer");
    }


    protected void loadCaptionComponent(Component parent)
    {
        if(UISessionUtils.getCurrentSession().getSystemService().checkPermissionOn(GeneratedCms2Constants.TC.CMSSITE, "create"))
        {
            Div captionImagePanel = new Div();
            captionImagePanel.setSclass("captionImageLabelPanel");
            Toolbarbutton captionLabelImage = new Toolbarbutton("", "/cmscockpit/images/add_btn.gif");
            captionLabelImage.setParent((Component)captionImagePanel);
            captionLabelImage.setTooltiptext(Labels.getLabel("cmscockpit.create_item"));
            UITools.applyTestID((Component)captionLabelImage, "NavigationArea_Websites_Add_button");
            captionLabelImage.addEventListener("onClick", (EventListener)new Object(this, parent));
            parent.appendChild((Component)captionImagePanel);
        }
    }


    public ListitemRenderer getListRenderer()
    {
        if(this.listItemRenderer == null)
        {
            this.listItemRenderer = new SiteSelectorListRenderer(this);
        }
        return (ListitemRenderer)this.listItemRenderer;
    }


    public SynchronizationService getSynchronizationService()
    {
        return this.synchronizationService;
    }


    public void setSynchronizationService(SynchronizationService synchronizationService)
    {
        this.synchronizationService = synchronizationService;
    }
}
