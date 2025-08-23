package de.hybris.platform.cmscockpit.wizard.page;

import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmscockpit.services.config.ContentElementConfiguration;
import de.hybris.platform.cmscockpit.services.config.ContentElementListConfiguration;
import de.hybris.platform.cmscockpit.services.config.impl.DefaultContentElementConfiguration;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;

public class ReferencePage extends AbstractCmsWizardPage
{
    protected static final String SCLASS_ELEMENT_BOX = "contentElementBox";
    protected static final String SCLASS_ELEMENT_IMAGE = "contentElementImage";
    protected static final String SCLASS_ELEMENT_NAME = "contentElementName";
    protected static final String TYPE_SELECTOR_CMSWIZARD_PAGE_SCLASS = "typSelectorCmsWizardPage";
    protected static final String TYPE_SELECTOR_CMSWIZARD_ROW_SCLASS = "typSelectorRow";
    protected static final String COCKPIT_ID_CREATE_PAGE_TEMPLATES_PREFIX = "CreatePage_Templates";
    private CMSAdminPageService cmsPageService;
    private UIConfigurationService uiConfigurationService;
    private TypeService typeService;
    private boolean jumpOverPage;


    public CMSAdminPageService getCmsAdminPageService()
    {
        if(this.cmsPageService == null)
        {
            this.cmsPageService = (CMSAdminPageService)SpringUtil.getBean("cmsAdminPageService");
        }
        return this.cmsPageService;
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = (TypeService)SpringUtil.getBean("typeService");
        }
        return this.typeService;
    }


    protected TypedObject chosenReference = null;


    public TypedObject getChosenReference()
    {
        return this.chosenReference;
    }


    protected ObjectType rootSelectorType = null;


    public ObjectType getRootSelectorType()
    {
        return this.rootSelectorType;
    }


    public void setRootSelectorType(ObjectType rootSelectorType)
    {
        this.rootSelectorType = rootSelectorType;
    }


    public ReferencePage(String pageTitle, Wizard wizard)
    {
        super(pageTitle, wizard);
    }


    public List<TypedObject> getReferences()
    {
        String currentTypeCode = getWizard().getCurrentType().getCode();
        ComposedTypeModel type = getTypeService().getComposedType(currentTypeCode);
        if(type instanceof CMSPageTypeModel)
        {
            return UISessionUtils.getCurrentSession().getTypeService()
                            .wrapItems(getCmsAdminPageService().getAllRestrictedPageTemplates(true, (CMSPageTypeModel)type));
        }
        return Collections.EMPTY_LIST;
    }


    public void initView(Wizard wizard, Component comp)
    {
        super.initView(wizard, comp);
        if(this.jumpOverPage)
        {
            this.jumpOverPage = false;
            wizard.doBack();
        }
        else
        {
            List<TypedObject> reference = getReferences();
            if(reference.size() == 1)
            {
                this.jumpOverPage = true;
                doItemSelected(reference.get(0));
            }
        }
    }


    public Component createRepresentationItself()
    {
        this.pageContent.getChildren().clear();
        Div firstStep = new Div();
        firstStep.setParent((Component)this.pageContent);
        Listbox listbox = new Listbox();
        listbox.setSclass("typSelectorCmsWizardPage");
        listbox.setWidth("100%");
        listbox.setParent((Component)firstStep);
        listbox.setModel((ListModel)new SimpleListModel(getReferences()));
        listbox.setItemRenderer((ListitemRenderer)new Object(this));
        return (Component)this.pageContainer;
    }


    protected void doItemSelected(TypedObject processedObject)
    {
        this.chosenReference = processedObject;
        getWizard().getPredefinedValues().put("AbstractPage.masterTemplate", this.chosenReference);
        PropertyDescriptor descriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("AbstractPage.masterTemplate");
        getWizard().setValue(descriptor, this.chosenReference);
        getWizard().loadAndFilter();
        WizardPage nextPage = getCurrentController().next((Wizard)getWizard(), getWizard().getCurrentPage());
        if(nextPage == null)
        {
            getWizard().refreshButtons();
        }
        else
        {
            getWizard().doNext();
        }
    }


    protected ContentElementConfiguration getElementConfiguration(TypedObject object)
    {
        ContentElementConfiguration ret = null;
        for(ObjectTemplate template : object.getAssignedTemplates())
        {
            ret = (ContentElementConfiguration)getContentElementConfiguration(template).getContentElements().get(template);
            if(ret != null)
            {
                break;
            }
        }
        if(ret == null)
        {
            BaseType type = object.getType();
            ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getBestTemplate(object);
            Map<ObjectType, ContentElementConfiguration> contentElements = getContentElementConfiguration(objectTemplate).getContentElements();
            ret = contentElements.get(type);
        }
        if(object.getObject() instanceof PageTemplateModel)
        {
            ret = getElementConfigurationForTypedObject(object, ret);
        }
        return ret;
    }


    protected ContentElementListConfiguration getContentElementConfiguration(ObjectTemplate objectTemplate)
    {
        return (ContentElementListConfiguration)getUIConfigurationService().getComponentConfiguration(objectTemplate, "contentElement", ContentElementListConfiguration.class);
    }


    protected ContentElementListConfiguration getContentElementConfiguration()
    {
        ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate("Item");
        return getContentElementConfiguration(objectTemplate);
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    protected ContentElementConfiguration getElementConfigurationForTypedObject(TypedObject object, ContentElementConfiguration configuartionfromtype)
    {
        DefaultContentElementConfiguration defaultContentElementConfiguration;
        ServicesUtil.validateParameterNotNull(configuartionfromtype, "No template configuartion specified.");
        ServicesUtil.validateParameterNotNull(object, "No Typed object specified.");
        if(object.getObject() instanceof PageTemplateModel)
        {
            PageTemplateModel template = (PageTemplateModel)object.getObject();
            if(template.getPreviewIcon() != null)
            {
                defaultContentElementConfiguration = new DefaultContentElementConfiguration((ObjectType)configuartionfromtype.getType(), configuartionfromtype.getName(), configuartionfromtype.getDescription(), UITools.getAdjustedUrl(template
                                .getPreviewIcon().getURL()));
            }
        }
        return (ContentElementConfiguration)defaultContentElementConfiguration;
    }
}
