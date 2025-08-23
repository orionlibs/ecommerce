package de.hybris.platform.cmscockpit.wizard;

import de.hybris.platform.cmscockpit.wizard.page.CmsWizardPage;
import de.hybris.platform.cmscockpit.wizard.page.TypeSelectorPage;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueHandler;
import de.hybris.platform.cockpit.services.values.ObjectValueHandlerRegistry;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueHandlerPermissionException;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

public class CmsWizard extends Wizard
{
    private static final Logger LOG = Logger.getLogger(CmsWizard.class);
    protected ObjectType currentType = null;
    protected ObjectValueContainer objectValueContainer = null;
    protected Component parent = null;
    protected Vbox containerBox = null;
    protected boolean showPrefilledValues = false;
    private boolean displaySubtypes;
    private BrowserSectionModel browserSectionModel = null;
    private BrowserModel browserModel = null;
    private Map<String, Object> predefinedValues = null;


    public boolean isDisplaySubtypes()
    {
        return this.displaySubtypes;
    }


    public void setDisplaySubtypes(boolean displaySubtypes)
    {
        this.displaySubtypes = displaySubtypes;
    }


    public BrowserSectionModel getBrowserSectionModel()
    {
        return this.browserSectionModel;
    }


    public BrowserModel getBrowserModel()
    {
        return this.browserModel;
    }


    public void setBrowserModel(BrowserModel browserModel)
    {
        this.browserModel = browserModel;
    }


    public CmsWizard(BrowserModel browserModel, BrowserSectionModel browserSectionModel)
    {
        this.browserModel = browserModel;
        this.browserSectionModel = browserSectionModel;
    }


    public boolean isShowPrefilledValues()
    {
        return this.showPrefilledValues;
    }


    public void setShowPrefilledValues(boolean showPrefilledValues)
    {
        this.showPrefilledValues = showPrefilledValues;
    }


    public Map<String, Object> getPredefinedValues()
    {
        return this.predefinedValues;
    }


    protected void reloadPredefinedValues()
    {
        loadPredefinedValues(this.predefinedValues);
    }


    protected void loadPredefinedValues(Map<String, Object> predefinedValues)
    {
        this.predefinedValues = predefinedValues;
        if(this.predefinedValues == null)
        {
            LOG.info("Cannot load predefined values since ther are not defined!");
            return;
        }
        for(String propertyQualifier : predefinedValues.keySet())
        {
            PropertyDescriptor currentDescriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(propertyQualifier);
            ObjectValueContainer globalObjectValueContainer = getObjectValueContainer();
            if(globalObjectValueContainer.getPropertyDescriptors().contains(currentDescriptor))
            {
                ObjectValueContainer.ObjectValueHolder localValueHolder = null;
                if(currentDescriptor.isLocalized())
                {
                    localValueHolder = globalObjectValueContainer.getValue(currentDescriptor, UISessionUtils.getCurrentSession()
                                    .getLanguageIso());
                }
                else
                {
                    localValueHolder = globalObjectValueContainer.getValue(currentDescriptor, null);
                }
                localValueHolder.setLocalValue(predefinedValues.get(propertyQualifier));
                localValueHolder.stored();
            }
        }
    }


    public void loadDefaultValues()
    {
        TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
        ObjectTemplate objectTemplate = typeService.getObjectTemplate(getCurrentType().getCode());
        ObjectValueContainer globalObjectValueContainer = getObjectValueContainer();
        Map<PropertyDescriptor, Object> defaultValues = TypeTools.getAllDefaultValues(typeService, objectTemplate,
                        getLoadLanguages());
        for(PropertyDescriptor currentDescriptor : defaultValues.keySet())
        {
            if(globalObjectValueContainer.getPropertyDescriptors().contains(currentDescriptor))
            {
                ObjectValueContainer.ObjectValueHolder localValueHolder = null;
                if(currentDescriptor.isLocalized())
                {
                    localValueHolder = globalObjectValueContainer.getValue(currentDescriptor, UISessionUtils.getCurrentSession()
                                    .getLanguageIso());
                }
                else
                {
                    localValueHolder = globalObjectValueContainer.getValue(currentDescriptor, null);
                }
                localValueHolder.setLocalValue(defaultValues.get(currentDescriptor));
                localValueHolder.stored();
            }
        }
    }


    public ObjectValueContainer getObjectValueContainer()
    {
        if(this.objectValueContainer == null)
        {
            this.objectValueContainer = new ObjectValueContainer(this.currentType, null);
            BaseType currentBaseType = null;
            if(this.currentType instanceof ObjectTemplate)
            {
                currentBaseType = ((ObjectTemplate)this.currentType).getBaseType();
            }
            else if(this.currentType instanceof BaseType)
            {
                currentBaseType = (BaseType)this.currentType;
            }
            if(currentBaseType != null)
            {
                TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
                ModelService modelService = UISessionUtils.getCurrentSession().getModelService();
                ObjectValueHandlerRegistry valueHandlerRegistry = UISessionUtils.getCurrentSession().getValueHandlerRegistry();
                Set<PropertyDescriptor> onlyWritableProperties = (Set<PropertyDescriptor>)currentBaseType.getPropertyDescriptors().stream().filter(e -> e.isWritable()).collect(Collectors.toSet());
                for(ObjectValueHandler valueHandler : valueHandlerRegistry.getValueHandlerChain((ObjectType)currentBaseType))
                {
                    try
                    {
                        TypedObject artificialTypedObject = typeService.wrapItem(modelService.create(currentBaseType.getCode()));
                        valueHandler.loadValues(this.objectValueContainer, (ObjectType)currentBaseType, artificialTypedObject, onlyWritableProperties,
                                        getLoadLanguages());
                    }
                    catch(ValueHandlerPermissionException e)
                    {
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug("Not sufficient privilages!", (Throwable)e);
                        }
                    }
                    catch(ValueHandlerException e)
                    {
                        LOG.error("error loading object values", (Throwable)e);
                    }
                }
            }
        }
        return this.objectValueContainer;
    }


    public ObjectType getCurrentType()
    {
        return this.currentType;
    }


    public TypedObject getCurrentTypeEmptyModel()
    {
        ObjectType baseType = (this.currentType instanceof ObjectTemplate) ? (ObjectType)((ObjectTemplate)this.currentType).getBaseType() : this.currentType;
        return UISessionUtils.getCurrentSession().getTypeService()
                        .wrapItem(UISessionUtils.getCurrentSession().getModelService().create(baseType.getCode()));
    }


    public void setCurrentType(ObjectType currentType)
    {
        if(this.currentType != currentType && currentType != null)
        {
            this.currentType = currentType;
            refreshObjectValueContainer();
            loadDefaultValues();
            reloadPredefinedValues();
            loadAndFilter();
        }
    }


    public void setParent(Component parent)
    {
        this.parent = parent;
    }


    protected Window createFrameComponent(String uri)
    {
        Window ret = null;
        if(StringUtils.isBlank(uri))
        {
            ret = new Window();
            ret.applyProperties();
            (new AnnotateDataBinder((Component)ret)).loadAll();
            this.containerBox = new Vbox();
            ret.appendChild((Component)this.containerBox);
            ret.setParent(this.parent);
            return ret;
        }
        ret = super.createFrameComponent(uri);
        ret.setParent(this.parent);
        return ret;
    }


    protected Component createPageComponent(Component parent, WizardPage page, WizardPageController controller)
    {
        Component ret = null;
        if(page instanceof CmsWizardPage)
        {
            Component component = ((CmsWizardPage)page).createRepresentationItself();
            ret = component;
        }
        else
        {
            ret = super.createPageComponent(parent, page, controller);
        }
        return ret;
    }


    public Set<String> getLoadLanguages()
    {
        Set<String> ret = new LinkedHashSet<>();
        for(LanguageModel l : UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguages())
        {
            ret.add(l.getIsocode());
        }
        return ret;
    }


    public void initialize(ObjectType currentType, Map<String, Object> predefinedValues)
    {
        this.predefinedValues = predefinedValues;
        if(currentType != null)
        {
            setCurrentType(currentType);
            loadDefaultValues();
        }
        loadAndFilter();
    }


    public void loadAndFilter()
    {
        if(this.predefinedValues != null)
        {
            List<String> prefilledValues = new ArrayList<>(getPredefinedValues().keySet());
            TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
            ObjectTemplate tempalte = null;
            Map<PropertyDescriptor, Object> defaultValues = null;
            if(getCurrentType() != null)
            {
                tempalte = typeService.getObjectTemplate(getCurrentType().getCode());
                defaultValues = TypeTools.getAllDefaultValues(typeService, tempalte, getLoadLanguages());
            }
            if(defaultValues != null && !defaultValues.isEmpty())
            {
                for(PropertyDescriptor propertyDescriptor : defaultValues.keySet())
                {
                    if(!prefilledValues.contains(propertyDescriptor.getQualifier()))
                    {
                        prefilledValues.add(propertyDescriptor.getQualifier());
                    }
                }
            }
            for(Iterator<WizardPage> iter = getPages().iterator(); iter.hasNext(); )
            {
                WizardPage wizardPage = iter.next();
                if(wizardPage instanceof TypeSelectorPage)
                {
                    TypeSelectorPage typeSelectorPage = (TypeSelectorPage)wizardPage;
                    if(!typeSelectorPage.isDisplaySubtypes() && getCurrentType() != null)
                    {
                        iter.remove();
                    }
                }
            }
        }
    }


    public void setValue(PropertyDescriptor descriptor, Object value)
    {
        ObjectValueContainer objectValueContainer = getObjectValueContainer();
        ObjectValueContainer.ObjectValueHolder valueHolder = null;
        if(descriptor.isLocalized())
        {
            valueHolder = objectValueContainer.getValue(descriptor, UISessionUtils.getCurrentSession().getLanguageIso());
        }
        else
        {
            valueHolder = objectValueContainer.getValue(descriptor, null);
        }
        if(valueHolder != null)
        {
            valueHolder.setLocalValue(value);
            valueHolder.stored();
        }
        else
        {
            LOG.warn("Cannot find value for " + descriptor.getQualifier());
        }
    }


    protected void refreshObjectValueContainer()
    {
        if(this.objectValueContainer != null)
        {
            this.objectValueContainer = null;
            getObjectValueContainer();
        }
    }
}
