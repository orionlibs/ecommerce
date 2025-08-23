package de.hybris.platform.cmscockpit.wizard.page;

import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmscockpit.services.GenericRandomNameProducer;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.wizards.Wizard;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public abstract class ReferenceSelectorPage extends AbstractCmsWizardPage
{
    protected static final String SCLASS_ELEMENT_BOX = "contentElementBox";
    protected static final String SCLASS_ELEMENT_IMAGE = "contentElementImage";
    protected static final String SCLASS_ELEMENT_NAME = "contentElementName";
    protected static final String TYPE_SELECTOR_CMSWIZARD_PAGE_SCLASS = "typSelectorCmsWizardPage";
    protected static final String TYPE_SELECTOR_CMSWIZARD_ROW_SCLASS = "typSelectorRow";
    private CMSAdminPageService cmsPageService;
    private BrowserSectionModel sectionModel;
    private String position;
    private ObjectValueContainer objctValueContainer;


    public ObjectValueContainer getObjctValueContainer()
    {
        return this.objctValueContainer;
    }


    public String getPosition()
    {
        return this.position;
    }


    public void setPosition(String position)
    {
        this.position = position;
    }


    public BrowserSectionModel getSectionModel()
    {
        return this.sectionModel;
    }


    public void setSectionModel(BrowserSectionModel sectionModel)
    {
        this.sectionModel = sectionModel;
    }


    public CMSAdminPageService getCmsAdminPageService()
    {
        if(this.cmsPageService == null)
        {
            this.cmsPageService = (CMSAdminPageService)SpringUtil.getBean("cmsAdminPageService");
        }
        return this.cmsPageService;
    }


    protected TypedObject chosenReference = null;


    public TypedObject getChosenReference()
    {
        return this.chosenReference;
    }


    protected ObjectType rootSelectorType = null;
    private GenericRandomNameProducer genericRandomNameProducer;


    public ObjectType getRootSelectorType()
    {
        return this.rootSelectorType;
    }


    public void setRootSelectorType(ObjectType rootSelectorType)
    {
        this.rootSelectorType = rootSelectorType;
    }


    public ReferenceSelectorPage(String pageTitle, Wizard wizard, BrowserSectionModel model)
    {
        super(pageTitle, wizard);
        this.sectionModel = model;
    }


    public abstract PropertyDescriptor getPropertyDescriptor();


    public abstract TypedObject getCurrentObject();


    public Component createRepresentationItself()
    {
        this.pageContent.getChildren().clear();
        Label label = new Label(getPropertyDescriptor().getName());
        label.setParent((Component)this.pageContent);
        label.setClass("reference_wizard_header");
        Div selectorContent = new Div();
        selectorContent.setParent((Component)this.pageContent);
        selectorContent.setWidth("90%");
        selectorContent.setHeight("90%");
        selectorContent.setStyle("padding-left:30px;padding-top:10px");
        Map<String, Object> params = new HashMap<>();
        this.objctValueContainer = TypeTools.createValueContainer(getCurrentObject(), Collections.singleton(getPropertyDescriptor()),
                        UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos());
        EditorHelper.createEditor(getCurrentObject(), getPropertyDescriptor(), (HtmlBasedComponent)selectorContent, this.objctValueContainer, false, "multi", params);
        return (Component)this.pageContainer;
    }


    public GenericRandomNameProducer getGenericRandomNameProducer()
    {
        if(this.genericRandomNameProducer == null)
        {
            this.genericRandomNameProducer = (GenericRandomNameProducer)SpringUtil.getBean("genericRandomNameProducer");
        }
        return this.genericRandomNameProducer;
    }


    public BrowserModel getBrowserModel()
    {
        return UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser();
    }
}
