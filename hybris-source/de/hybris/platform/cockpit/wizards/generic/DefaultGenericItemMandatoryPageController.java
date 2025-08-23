package de.hybris.platform.cockpit.wizards.generic;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.helpers.validation.ValidationUIHelper;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.WizardConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.validation.CockpitValidationService;
import de.hybris.platform.cockpit.services.validation.pojos.CockpitValidationDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zkplus.spring.SpringUtil;

public class DefaultGenericItemMandatoryPageController extends DefaultPageController
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultGenericItemMandatoryPageController.class);
    private String position;
    private UIConfigurationService uiConfigurationService;
    private WizardPage nextPage = null;
    private TypeService typeService;
    private CommonI18NService commonI18nService;
    private ModelService modelService;
    private I18NService i18nService;
    private CockpitValidationService validationService;


    public String getPosition()
    {
        return this.position;
    }


    public void setPosition(String position)
    {
        this.position = position;
    }


    public void done(Wizard wizard, WizardPage page)
    {
        TypedObject newItem = createItem(wizard, page);
        dispatchEventAfterCreate(newItem);
    }


    public void beforeNext(Wizard wizard, WizardPage page)
    {
        if(this.nextPage != null && page instanceof GenericItemMandatoryPage && ((GenericItemMandatoryPage)page).getValue() == null)
        {
            createItem(wizard, page);
        }
    }


    protected TypedObject createItem(Wizard wizard, WizardPage page)
    {
        GenericItemWizard genericItemWizard = (GenericItemWizard)wizard;
        TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
        TypedObject newItem = null;
        try
        {
            ObjectTemplate template = typeService.getObjectTemplate(((GenericItemWizard)wizard).getCurrentType().getCode());
            newItem = UISessionUtils.getCurrentSession().getNewItemService().createNewItem(genericItemWizard.getObjectValueContainer(), template);
        }
        catch(Exception e)
        {
            Message msg = createErrorMessageForException(e);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Could not create item.", e);
            }
            genericItemWizard.addMessage(msg);
            throw new WizardConfirmationException(e);
        }
        if(page instanceof GenericItemMandatoryPage)
        {
            ((GenericItemMandatoryPage)page).setValue(newItem);
        }
        genericItemWizard.setItem(newItem);
        return newItem;
    }


    protected Message createErrorMessageForException(Exception exception)
    {
        Message msg = new Message(3, Labels.getLabel("editorarea.persist.error", (Object[])new String[] {":" + exception.getMessage()}), null);
        return msg;
    }


    protected void dispatchEventAfterCreate(TypedObject typedObject)
    {
        UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, typedObject, Collections.EMPTY_LIST, ItemChangedEvent.ChangeType.CREATED));
    }


    public boolean validate(Wizard wizard, WizardPage page)
    {
        boolean ret = true;
        if(wizard instanceof GenericItemWizard)
        {
            GenericItemWizard genericItemWizard = (GenericItemWizard)wizard;
            if(genericItemWizard.getCurrentPage() instanceof GenericItemMandatoryPage)
            {
                ValidationUIHelper validationUIHelper = (ValidationUIHelper)SpringUtil.getBean("validationUIHelper");
                ObjectValueContainer currentObjectValues = genericItemWizard.getObjectValueContainer();
                TypedObject typedObject = getTypeService().wrapItem(currentObjectValues.getObject());
                for(ObjectValueContainer.ObjectValueHolder ovh : currentObjectValues.getAllValues())
                {
                    try
                    {
                        PropertyDescriptor propertyDescriptor = ovh.getPropertyDescriptor();
                        if(getTypeService().getObjectTypeFromPropertyQualifier(propertyDescriptor.getQualifier())
                                        .isAssignableFrom((ObjectType)typedObject.getType()) && propertyDescriptor.isWritable())
                        {
                            Object value = TypeTools.container2Item(this.typeService, ovh.getLocalValue());
                            if(propertyDescriptor.isLocalized())
                            {
                                Map<Locale, Object> values = new HashMap<>();
                                values.put(getCommonI18NService()
                                                .getLocaleForLanguage(getCommonI18NService().getLanguage(ovh.getLanguageIso())), value);
                                setModelLocalizedValue((ItemModel)currentObjectValues.getObject(),
                                                getTypeService().getAttributeCodeFromPropertyQualifier(propertyDescriptor.getQualifier()), ovh
                                                                .getLanguageIso(), values);
                                continue;
                            }
                            setModelSingleValue((ItemModel)currentObjectValues.getObject(),
                                            getTypeService().getAttributeCodeFromPropertyQualifier(propertyDescriptor.getQualifier()), value);
                        }
                    }
                    catch(AttributeNotSupportedException ex)
                    {
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug("Attribute: " + ex.getQualifier() + " is not supported by servicelayer! ");
                        }
                    }
                }
                currentObjectValues.setObject(typedObject.getObject());
                Set<CockpitValidationDescriptor> violations = validationUIHelper.sortCockpitValidationDescriptors(
                                getValidationService().validateModel((ItemModel)currentObjectValues.getObject()));
                if(!violations.isEmpty() &&
                                !validationUIHelper.allWarningsForced(currentObjectValues.getIgnoredValidationConstraints(), violations))
                {
                    ret = false;
                }
                for(CockpitValidationDescriptor violation : violations)
                {
                    PropertyDescriptor propertyDescriptor = violation.getPropertyDescriptor();
                    String qualifier = (propertyDescriptor == null) ? null : propertyDescriptor.getQualifier();
                    Message msg = new Message(violation.getCockpitMessageLevel(), violation.getValidationMessage(), qualifier, violation.getConstraintPk());
                    genericItemWizard.addMessage(msg);
                    if(genericItemWizard.getWizardConfiguration().isValidationInfoIgnored() && violations
                                    .stream().allMatch(v -> (v.getCockpitMessageLevel() == 0)))
                    {
                        currentObjectValues.addToIgnoredValidationConstraints(violation.getConstraintPk());
                    }
                }
            }
        }
        return ret;
    }


    public WizardPage next(Wizard wizard, WizardPage page)
    {
        return (this.nextPage == null) ? super.next(wizard, page) : this.nextPage;
    }


    public void initPage(Wizard wizard, WizardPage page)
    {
        if(wizard instanceof GenericItemWizard)
        {
            GenericItemWizard genericItemWizard = (GenericItemWizard)wizard;
            GenericItemMandatoryPage genericPage = (GenericItemMandatoryPage)page;
            CreateContext createContext = genericItemWizard.getCreateContext();
            Map<String, Object> initParameters = new HashMap<>();
            initParameters.putAll(genericItemWizard.getPredefinedValues());
            Map<String, Object> parameters = new HashMap<>();
            parameters.putAll((genericPage.getParameters() != null) ? genericPage.getParameters() : Collections.EMPTY_MAP);
            boolean forceCreateInPopup = getBooleanParameter(parameters, "forceCreateInPopup", false);
            boolean forceCreateInEditor = getBooleanParameter(parameters, "forceCreateInEditor", false);
            boolean forceCreateInWizard = getBooleanParameter(parameters, "forceCreateInWizard", false);
            if(forceCreateInWizard && !forceCreateInPopup && !forceCreateInEditor)
            {
                Object bean = SpringUtil.getBean("genericCreateWizardPageMapping");
                if(bean instanceof AdditionalPageMapping)
                {
                    AdditionalPageMapping pageMapping = (AdditionalPageMapping)bean;
                    ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(genericItemWizard.getCurrentType().getCode());
                    List<WizardPage> pageList = pageMapping.getPageList(objectTemplate);
                    if(CollectionUtils.isNotEmpty(pageList))
                    {
                        wizard.getPages().addAll(pageList);
                        this.nextPage = pageList.iterator().next();
                        wizard.setShowDone(false);
                        wizard.setShowNext(true);
                    }
                }
            }
            else if((forceCreateInPopup && !forceCreateInEditor) || (genericItemWizard.getWizardConfiguration() != null && genericItemWizard
                            .getWizardConfiguration().isCreateWithinPopupEditorArea()))
            {
                if(genericItemWizard.getBrowserModel() != null)
                {
                    boolean missingMandatory = !hasAllMandatoryFields(initParameters, genericItemWizard.getCurrentType());
                    UISessionUtils.getCurrentSession().getCurrentPerspective().createItemInPopupEditor(genericItemWizard
                                    .getCurrentType(), initParameters, genericItemWizard.getBrowserModel(), true, missingMandatory);
                }
                else
                {
                    UISessionUtils.getCurrentSession().getCurrentPerspective()
                                    .createItemInPopupEditor(genericItemWizard.getCurrentType(), initParameters, createContext);
                }
                wizard.doCancel();
            }
            else if(genericItemWizard.getWizardConfiguration() == null ||
                            getWizardConfiguration(genericItemWizard).isCreateWithinEditorArea())
            {
                ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(genericItemWizard.getCurrentType().getCode());
                ((BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective()).createNewItem(objectTemplate, createContext, initParameters, true, true, false);
                wizard.doCancel();
            }
        }
    }


    private boolean getBooleanParameter(Map<String, Object> parameters, String param, boolean defaultValue)
    {
        boolean ret = defaultValue;
        if(parameters.containsKey(param))
        {
            Object value = parameters.get(param);
            if(value instanceof Boolean)
            {
                ret = ((Boolean)value).booleanValue();
            }
            else if(value instanceof String && !StringUtils.isEmpty((String)value))
            {
                ret = Boolean.parseBoolean((String)value);
            }
        }
        return ret;
    }


    protected WizardConfiguration getWizardConfiguration(GenericItemWizard wizard)
    {
        WizardConfiguration ret = null;
        if(wizard.getCurrentType() != null)
        {
            ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(wizard.getCurrentType().getCode());
            ret = (WizardConfiguration)getUIConfigurationService().getComponentConfiguration(objectTemplate, "wizardConfig", WizardConfiguration.class);
        }
        return ret;
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    protected boolean hasAllMandatoryFields(Map<String, Object> initialParameters, ObjectType objectType)
    {
        Set<PropertyDescriptor> mandatoryFields = TypeTools.getMandatoryAttributes(objectType, true);
        for(PropertyDescriptor mandatoryDescr : mandatoryFields)
        {
            if(initialParameters.get(mandatoryDescr.getQualifier()) == null)
            {
                return false;
            }
        }
        return true;
    }


    private TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = (TypeService)SpringUtil.getBean("cockpitTypeService");
        }
        return this.typeService;
    }


    private CommonI18NService getCommonI18NService()
    {
        if(this.commonI18nService == null)
        {
            this.commonI18nService = (CommonI18NService)SpringUtil.getBean("commonI18NService");
        }
        return this.commonI18nService;
    }


    public void setModelSingleValue(ItemModel model, String qualifier, Object value) throws AttributeNotSupportedException
    {
        try
        {
            Object valueModel = getModelService().toModelLayer(value);
            getModelService().setAttributeValue(model, qualifier, valueModel);
        }
        catch(IllegalArgumentException e)
        {
            throw new AttributeNotSupportedException(e.getMessage(), e, qualifier);
        }
    }


    public void setModelLocalizedValue(ItemModel model, String qualifier, String languageIso, Object value) throws AttributeNotSupportedException
    {
        I18NService i18n = getI18NService();
        Locale backup = i18n.getCurrentLocale();
        try
        {
            i18n.setCurrentLocale(getCommonI18NService().getLocaleForLanguage(getCommonI18NService().getLanguage(languageIso)));
            Object valueModel = getModelService().toModelLayer(value);
            getModelService().setAttributeValue(model, qualifier, valueModel);
        }
        catch(IllegalArgumentException e)
        {
            throw new AttributeNotSupportedException(e.getMessage(), e, qualifier);
        }
        finally
        {
            i18n.setCurrentLocale(backup);
        }
    }


    private ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)SpringUtil.getBean("modelService");
        }
        return this.modelService;
    }


    private I18NService getI18NService()
    {
        if(this.i18nService == null)
        {
            this.i18nService = (I18NService)SpringUtil.getBean("i18nService");
        }
        return this.i18nService;
    }


    private CockpitValidationService getValidationService()
    {
        if(this.validationService == null)
        {
            this.validationService = (CockpitValidationService)SpringUtil.getBean("cockpitValidationService");
        }
        return this.validationService;
    }
}
