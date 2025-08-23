package de.hybris.platform.cmscockpit.components.contentbrowser;

import de.hybris.platform.cmscockpit.session.impl.CmsListBrowserSectionModel;
import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cockpit.components.AdvancedGroupbox;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractSectionComponent;
import de.hybris.platform.cockpit.components.contentbrowser.BrowserComponent;
import de.hybris.platform.cockpit.components.dialog.DefaultPopupDialog;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.Lockable;
import de.hybris.platform.cockpit.session.SectionModel;
import de.hybris.platform.cockpit.util.ComponentInjector;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.impl.AbstractTag;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;

public class CMSStructViewInjector implements ComponentInjector
{
    private static final Logger LOG = Logger.getLogger(CMSStructViewInjector.class);
    private static final String STRUCTURE_SECTION_LOCKED = "structureViewLocked";
    private static final String STRUCTURE_SECTION_NOT_DEFINED = "structureViewNotDefineSection";
    private static final String ADD_BTN = "/cmscockpit/images/add_btn.gif";
    private static final String SPLITTER_IMG = "/cockpit/images/splitter_grey.gif";
    private CmsPageBrowserModel cmsStructBrowserModel = null;
    private transient Map<BrowserSectionModel, BrowserComponent> sectionMap = null;


    public CMSStructViewInjector(CmsPageBrowserModel browserModel, Map<BrowserSectionModel, BrowserComponent> sectionMap)
    {
        if(browserModel == null)
        {
            throw new IllegalArgumentException("Cms structure browser model cannot be empty!");
        }
        this.cmsStructBrowserModel = browserModel;
        this.sectionMap = sectionMap;
    }


    public void injectComponent(HtmlBasedComponent parent, Map<String, ? extends Object> params)
    {
        if(parent == null || params == null)
        {
            throw new IllegalArgumentException("Parent componend and code can not be null.");
        }
        if(!params.containsKey("code") || params.get("code") == null)
        {
            throw new IllegalArgumentException("Parameter has no 'code' value set.");
        }
        String code = params.get("code").toString();
        Component parentComponent = parent.getParent();
        BrowserComponent browserComponent = null;
        if(!"editor".equals(code))
        {
            browserComponent = lookupBrowserComponentByCode(code);
            if(browserComponent != null)
            {
                renderSection(parent, browserComponent);
            }
            else
            {
                renderEmptySection(parentComponent, parent, code);
            }
        }
    }


    protected void renderEmptySection(Component parentComponent, HtmlBasedComponent parent, String code)
    {
        applySclass(parentComponent, "structureViewNotDefineSection");
        AdvancedGroupbox advancedGroupbox = createEmptySectionView(code);
        parent.appendChild((Component)advancedGroupbox);
    }


    protected void renderSection(HtmlBasedComponent parent, BrowserComponent browserComponent)
    {
        if(browserComponent instanceof AbstractSectionComponent)
        {
            SectionModel sectionModel = ((AbstractSectionComponent)browserComponent).getSectionModel();
            if(sectionModel instanceof Lockable && ((Lockable)sectionModel).isLocked())
            {
                UITools.modifySClass((HtmlBasedComponent)browserComponent, "structureViewLocked", true);
            }
        }
        parent.appendChild((Component)browserComponent);
        browserComponent.initialize();
    }


    protected void renderContentEditorSection(HtmlBasedComponent parent, BrowserComponent browserComponent)
    {
        parent.appendChild((Component)browserComponent);
        browserComponent.initialize();
    }


    public CmsPageBrowserModel getCmsStructBrowserModel()
    {
        return this.cmsStructBrowserModel;
    }


    protected BrowserComponent lookupBrowserComponentByCode(String position)
    {
        BrowserComponent ret = null;
        if(StringUtils.isEmpty(position))
        {
            LOG.warn("Passed code shouldn't be empty!");
            return ret;
        }
        for(BrowserSectionModel sectionModel : getCmsStructBrowserModel().getBrowserSectionModels())
        {
            if(sectionModel instanceof CmsListBrowserSectionModel)
            {
                if(position.equals(((CmsListBrowserSectionModel)sectionModel).getPosition()))
                {
                    return this.sectionMap.get(sectionModel);
                }
            }
        }
        return ret;
    }


    protected BrowserComponent lookupContentBrowserEditor()
    {
        boolean selected = false;
        BrowserComponent editorComponent = null;
        for(BrowserSectionModel sectionModel : getCmsStructBrowserModel().getBrowserSectionModels())
        {
            if(!selected)
            {
                selected = (sectionModel.getSelectedIndex() != null);
            }
            if(sectionModel instanceof de.hybris.platform.cmscockpit.session.impl.ContentEditorBrowserSectionModel)
            {
                editorComponent = this.sectionMap.get(sectionModel);
            }
        }
        if(!selected)
        {
            getCmsStructBrowserModel().getContentEditorSection().setVisible(Boolean.FALSE.booleanValue());
        }
        return editorComponent;
    }


    protected AdvancedGroupbox createEmptySectionView(String position)
    {
        AdvancedGroupbox groupBox = new AdvancedGroupbox();
        String CONTENT_ELEMENT_BUTTONS_CNT_SCLASS = "contentElementButtonsCnt";
        groupBox.setLabel(Labels.getLabel("general.notdefined") + " [ " + Labels.getLabel("general.notdefined") + " ]");
        groupBox.setOpen(Boolean.TRUE.booleanValue());
        Div contentSlotCnt = new Div();
        contentSlotCnt.setWidth("100%");
        contentSlotCnt.setAlign("right");
        contentSlotCnt.setSclass("groupboxCaption");
        contentSlotCnt.setStyle("white-space:nowrap;");
        contentSlotCnt.setSclass("contentElementButtonsCnt");
        Toolbarbutton addButton = new Toolbarbutton("", "/cmscockpit/images/add_btn.gif");
        addButton.setTooltiptext(Labels.getLabel("cmscockpit.create_item"));
        Object object = new Object(this, contentSlotCnt, position);
        UITools.addBusyListener((Component)addButton, "onClick", (EventListener)object, null, "general.updating.busy");
        contentSlotCnt.appendChild((Component)addButton);
        Image splitterAddBtn = new Image("/cockpit/images/splitter_grey.gif");
        splitterAddBtn.setSclass("splitter");
        contentSlotCnt.appendChild((Component)splitterAddBtn);
        groupBox.getCaptionContainer().appendChild((Component)contentSlotCnt);
        Div groupBoxContent = new Div();
        groupBoxContent.setSclass("browserSectionContent");
        groupBoxContent.appendChild((Component)new Label(Labels.getLabel("browser.section.nosections")));
        UITools.modifySClass((HtmlBasedComponent)groupBoxContent, "emptyGroupBox", true);
        groupBox.appendChild((Component)groupBoxContent);
        return groupBox;
    }


    protected EventListener getPopupElementEventListener(DefaultPopupDialog dialogPopup)
    {
        return (EventListener)new Object(this, dialogPopup);
    }


    protected void applySclass(Component component, String sclassToApply)
    {
        if(component instanceof AbstractTag)
        {
            ((AbstractTag)component).setSclass(sclassToApply);
        }
        else if(component instanceof HtmlBasedComponent)
        {
            ((HtmlBasedComponent)component).setStyle(sclassToApply);
        }
    }
}
