package de.hybris.platform.cmscockpit.wizard.page;

import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.services.config.ContentElementListConfiguration;
import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cmscockpit.wizard.CmsWizard;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.meta.ObjectTypeFilter;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.wizards.MutableWizardContext;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.generic.CreateItemWizardRegistry;
import de.hybris.platform.cockpit.wizards.impl.DefaultWizardContext;
import de.hybris.platform.core.model.type.TypeModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;

public class TypeSelectorPage extends AbstractCmsWizardPage implements CmsComponentSelectorPage, CmsPageSelectorPage
{
    protected static final String SCLASS_ELEMENT_BOX = "contentElementBox";
    protected static final String SCLASS_ELEMENT_IMAGE = "contentElementImage";
    protected static final String SCLASS_ELEMENT_NAME = "contentElementName";
    protected static final String SCLASS_ELEMENT_DESC = "contentElementDescription";
    protected static final String TYPE_SELECTOR_CONTAINER_PAGE_SCLASS = "typeSelectorPageContainer";
    protected static final String TYPE_SELECTOR_CMSWIZARD_PAGE_SCLASS = "typSelectorCmsWizardPage";
    protected static final String TYPE_SELECTOR_CMSWIZARD_ROW_SCLASS = "typSelectorRow";
    protected static final String COCKPIT_ID_CREATE_PAGE_TYPE_PREFIX = "CreatePage_Type";
    protected static final String EXCLUDED_TYPES = "excludeCreateTypes";
    protected static final String RESTRICTED_TYPES = "restrictToCreateTypes";
    private static final Logger LOG = Logger.getLogger(TypeSelectorPage.class);
    protected ObjectType chosenType = null;
    protected ObjectType rootSelectorType = null;
    private CMSAdminSiteService adminSiteService;
    private boolean displaySubtypes;
    private String position;
    private PropertyDescriptor parentPropertyDescriptor;
    private TypedObject parentObject;
    private Map<String, Object> pageParameters;
    private UIConfigurationService uiConfigurationService;
    private CreateItemWizardRegistry createItemWizardRegistry;
    private TypeService typeService;
    private TypeService cockpitTypeService;
    private List<String> excludeCreateTypes = new ArrayList<>();
    private List<String> allowCreateTypes = new ArrayList<>();
    private ObjectTypeFilter<ObjectTemplate, ContentSlotNameModel> contentSlotObjectTypesFilter;


    public TypeSelectorPage()
    {
        super(null, null);
    }


    public TypeSelectorPage(String pageTitle, Wizard wizard)
    {
        super(pageTitle, wizard);
    }


    public Component createRepresentationItself()
    {
        this.pageContent.getChildren().clear();
        Div firstStep = new Div();
        firstStep.setSclass("typeSelectorPageContainer");
        firstStep.setParent((Component)this.pageContent);
        Listbox listbox = new Listbox();
        listbox.setSclass("typSelectorCmsWizardPage");
        listbox.setWidth("100%");
        listbox.setParent((Component)firstStep);
        listbox.setModel((ListModel)new SimpleListModel(new ArrayList<>(getInstanceableObjectTypes())));
        listbox.setItemRenderer((ListitemRenderer)new Object(this));
        return (Component)this.pageContainer;
    }


    protected MutableWizardContext createWizardContext()
    {
        DefaultWizardContext ctx = new DefaultWizardContext();
        ctx.setAttribute("predefinedValues", getWizard().getPredefinedValues());
        BaseType baseType = (this.parentObject == null) ? null : this.parentObject.getType();
        CreateContext createContext = new CreateContext((ObjectType)baseType, this.parentObject, getParentPropertyDescriptor(), null);
        ctx.setAttribute("parentWizardCreateContext", createContext);
        return (MutableWizardContext)ctx;
    }


    protected List<ObjectTemplate> filterTypes(List<ObjectTemplate> types)
    {
        if(this.contentSlotObjectTypesFilter != null)
        {
            try
            {
                AbstractPageModel page = getCurrentPage();
                if(page != null && getPosition() != null)
                {
                    ContentSlotNameModel contentSlotName = getContentSlotName(page.getMasterTemplate(), getPosition());
                    if(contentSlotName != null)
                    {
                        return (List<ObjectTemplate>)this.contentSlotObjectTypesFilter.filterObjectTypes(types, contentSlotName);
                    }
                }
            }
            catch(Exception e)
            {
                LOG.error("Could not filter types by " + this.contentSlotObjectTypesFilter.getClass().getSimpleName() + " due to : " + e
                                .getMessage(), e);
            }
        }
        return types;
    }


    protected CMSAdminSiteService getAdminSiteService()
    {
        if(this.adminSiteService == null)
        {
            this.adminSiteService = (CMSAdminSiteService)SpringUtil.getBean("cmsAdminSiteService");
        }
        return this.adminSiteService;
    }


    protected List<ObjectType> getAllSubTypes(String typeCode)
    {
        List<ObjectType> types = new LinkedList<>(getCockpitTypeService().getBaseType(typeCode).getSubtypes());
        List<ObjectType> itrTypes = new LinkedList<>(types);
        for(ObjectType subType : itrTypes)
        {
            types.addAll(getAllSubTypes(subType.getCode()));
        }
        return types;
    }


    public Object getChosenType()
    {
        return this.chosenType;
    }


    protected TypeService getCockpitTypeService()
    {
        if(this.cockpitTypeService == null)
        {
            this.cockpitTypeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.cockpitTypeService;
    }


    protected ContentElementListConfiguration getContentElementConfiguration()
    {
        ObjectTemplate objectTemplate = getCockpitTypeService().getObjectTemplate("Item");
        return getContentElementConfiguration(objectTemplate);
    }


    protected ContentElementListConfiguration getContentElementConfiguration(ObjectTemplate objectTemplate)
    {
        return (ContentElementListConfiguration)getUIConfigurationService().getComponentConfiguration(objectTemplate, "contentElement", ContentElementListConfiguration.class);
    }


    protected ContentSlotNameModel getContentSlotName(PageTemplateModel pageTemplate, String position)
    {
        ContentSlotNameModel ret = null;
        for(ContentSlotNameModel contentSlotName : pageTemplate.getAvailableContentSlots())
        {
            if(StringUtils.equals(position, contentSlotName.getName()))
            {
                ret = contentSlotName;
                break;
            }
        }
        return ret;
    }


    protected CreateItemWizardRegistry getCreateItemWizardRegistry()
    {
        if(this.createItemWizardRegistry == null)
        {
            this.createItemWizardRegistry = (CreateItemWizardRegistry)SpringUtil.getBean("createItemWizardRegistry", CreateItemWizardRegistry.class);
        }
        return this.createItemWizardRegistry;
    }


    protected AbstractPageModel getCurrentPage()
    {
        AbstractPageModel page = null;
        CmsWizard cmsWizard = getWizard();
        if(cmsWizard instanceof CmsWizard)
        {
            CmsWizard cmsWizard1 = cmsWizard;
            if(cmsWizard1.getBrowserModel() instanceof CmsPageBrowserModel)
            {
                page = (AbstractPageModel)((CmsPageBrowserModel)cmsWizard1.getBrowserModel()).getCurrentPageObject().getObject();
            }
        }
        return page;
    }


    protected List<ObjectTemplate> getInstanceableObjectTypes()
    {
        List<ObjectTemplate> types = new LinkedList<>();
        ObjectType rootSelectorType = getRootSelectorType();
        if(!rootSelectorType.isAbstract() && isValidForActiveSite(rootSelectorType))
        {
            BaseType baseType = getCockpitTypeService().getBaseType(rootSelectorType.getCode());
            List<ObjectTemplate> templates = getCockpitTypeService().getObjectTemplates(baseType);
            types.addAll(templates);
        }
        types.addAll(getInstanceableObjectTypes(rootSelectorType));
        Set<ObjectType> excludedTypes = new HashSet<>();
        Set<ObjectType> allowedTypes = new HashSet<>();
        if(MapUtils.isNotEmpty(this.pageParameters))
        {
            excludedTypes = EditorHelper.parseTemplateCodes((String)this.pageParameters.get("excludeCreateTypes"), getCockpitTypeService());
            allowedTypes = EditorHelper.parseTemplateCodes((String)this.pageParameters.get("restrictToCreateTypes"), getCockpitTypeService());
        }
        excludedTypes = mergeGivenTypes(excludedTypes, this.excludeCreateTypes);
        allowedTypes = mergeGivenTypes(allowedTypes, this.allowCreateTypes);
        if(CollectionUtils.isNotEmpty(allowedTypes))
        {
            types.clear();
            for(ObjectType allowedType : allowedTypes)
            {
                types.add(getCockpitTypeService().getObjectTemplate(allowedType.getCode()));
            }
        }
        else if(CollectionUtils.isNotEmpty(excludedTypes))
        {
            for(ObjectType excludedType : excludedTypes)
            {
                ObjectTemplate objectTemplate = getCockpitTypeService().getObjectTemplate(excludedType.getCode());
                if(types.contains(objectTemplate))
                {
                    types.remove(objectTemplate);
                }
            }
        }
        Collections.sort(types, (Comparator<? super ObjectTemplate>)new Object(this));
        return filterTypes(types);
    }


    protected List<ObjectTemplate> getInstanceableObjectTypes(ObjectType objectType)
    {
        List<ObjectTemplate> types = new LinkedList<>();
        List<ObjectType> safeTypeCopy = new LinkedList<>(objectType.getSubtypes());
        for(ObjectType subType : safeTypeCopy)
        {
            if(!subType.isAbstract() && isValidForActiveSite(subType))
            {
                BaseType baseType = getCockpitTypeService().getBaseType(subType.getCode());
                List<ObjectTemplate> templates = getCockpitTypeService().getObjectTemplates(baseType);
                types.addAll(templates);
                types.addAll(getInstanceableObjectTypes(subType));
                continue;
            }
            types.addAll(getInstanceableObjectTypes(subType));
        }
        return types;
    }


    public TypedObject getParentObject()
    {
        return this.parentObject;
    }


    public PropertyDescriptor getParentPropertyDescriptor()
    {
        return this.parentPropertyDescriptor;
    }


    protected String getPosition()
    {
        return this.position;
    }


    public ObjectType getRootSelectorType()
    {
        return this.rootSelectorType;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = (TypeService)SpringUtil.getBean("typeService");
        }
        return this.typeService;
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    public boolean isDisplaySubtypes()
    {
        return this.displaySubtypes;
    }


    protected boolean isValidForActiveSite(ObjectType type)
    {
        TypeModel typeModel = getTypeService().getType(type.getCode());
        if(getTypeService().isAssignableFrom(getTypeService().getType("ElementsForSlot"), typeModel))
        {
            return (getAdminSiteService().getActiveSite() == null) ? false : (
                            (getAdminSiteService().getActiveSite().getValidComponentTypes().isEmpty() ||
                                            getAdminSiteService().getActiveSite().getValidComponentTypes().contains(typeModel)));
        }
        return true;
    }


    protected Set<ObjectType> mergeGivenTypes(Collection<ObjectType> wizardConfigExcludedTypes, List<String> additionalExcluded)
    {
        Set<ObjectType> ret = new HashSet<>(wizardConfigExcludedTypes);
        for(String additionalTypeExcluded : additionalExcluded)
        {
            ObjectType excludedObjectType = getCockpitTypeService().getObjectType(additionalTypeExcluded);
            if(!ret.contains(excludedObjectType))
            {
                ret.add(excludedObjectType);
            }
        }
        return ret;
    }


    public void setDisplaySubtypes(boolean displaySubtypes)
    {
        this.displaySubtypes = displaySubtypes;
    }


    public void setParentObject(TypedObject parentObject)
    {
        this.parentObject = parentObject;
    }


    public void setParentPropertyDescriptor(PropertyDescriptor parentPropertyDescriptor)
    {
        this.parentPropertyDescriptor = parentPropertyDescriptor;
    }


    public void setPosition(String position)
    {
        this.position = position;
    }


    public void setRootSelectorType(ObjectType rootSelectorType)
    {
        this.rootSelectorType = rootSelectorType;
    }


    public Map<String, Object> getPageParameters()
    {
        return this.pageParameters;
    }


    public void setPageParameters(Map<String, Object> pageParameters)
    {
        this.pageParameters = pageParameters;
    }


    public List<String> getAllowCreateTypes()
    {
        return this.allowCreateTypes;
    }


    public void setAllowCreateTypes(List<String> allowCreateTypes)
    {
        this.allowCreateTypes = allowCreateTypes;
    }


    public List<String> getExcludeCreateTypes()
    {
        return this.excludeCreateTypes;
    }


    public void setExcludeCreateTypes(List<String> excludeCreateTypes)
    {
        this.excludeCreateTypes = excludeCreateTypes;
    }


    public void setContentSlotObjectTypesFilter(ObjectTypeFilter<ObjectTemplate, ContentSlotNameModel> contentSlotObjectTypesFilter)
    {
        this.contentSlotObjectTypesFilter = contentSlotObjectTypesFilter;
    }
}
