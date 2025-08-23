package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.sectionpanel.AbstractSectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.Message;
import de.hybris.platform.cockpit.components.sectionpanel.RowlayoutSectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRow;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.helpers.validation.ValidationUIHelper;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.config.CustomEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.impl.InitialAttrEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.undo.UndoManager;
import de.hybris.platform.cockpit.services.validation.CockpitValidationService;
import de.hybris.platform.cockpit.services.validation.pojos.CockpitValidationDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueHandler;
import de.hybris.platform.cockpit.services.values.ObjectValueHandlerRegistry;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueHandlerPermissionException;
import de.hybris.platform.cockpit.services.values.ValueService;
import de.hybris.platform.cockpit.session.EditorAreaController;
import de.hybris.platform.cockpit.session.EditorAreaListener;
import de.hybris.platform.cockpit.session.FocusablePerspectiveArea;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UIEditorArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.SingletonMap;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;

public class EditorArea implements UIEditorArea
{
    private static final Logger LOG = LoggerFactory.getLogger(EditorArea.class);
    public static final String EDITOR_AREA_CONFIG_CODE = "editorArea";
    private ObjectValueHandlerRegistry valueHandlerRegistry;
    private CommonI18NService commonI18NService;
    private SessionService sessionService;
    private SystemService systemService;
    private TypeService typeService;
    private ValueService valueService;
    private UndoManager undoManager;
    private UIConfigurationService uiConfigurationService;
    private UICockpitPerspective perspective;
    private EditorAreaController editorAreaController;
    private Set<CockpitValidationDescriptor> validationInfo;
    private final List<String> constraintsPk = new ArrayList<>();
    private final List<TypedObject> itemStack = new ArrayList<>();
    private boolean useItemStack = false;


    public Set<CockpitValidationDescriptor> getValidationInfo()
    {
        return this.validationInfo;
    }


    public void setValidationInfo(Set<CockpitValidationDescriptor> validationInfo)
    {
        this.validationInfo = validationInfo;
    }


    private final List<EditorAreaListener> listeners = new ArrayList<>();
    private TypedObject currentObject;
    private ObjectType currentObjectType;
    private ObjectValueContainer currentObjectValues;
    private EditorConfiguration typeConfiguration;
    private String viewURI;
    private UICockpitPerspective managingPerspective;
    private final List<CockpitEventAcceptor> cockpitEventAcceptors = new LinkedList<>();
    private String width;
    private CockpitValidationService validationService;
    private ValidationUIHelper validationUIHelper;
    private ModelService modelService;
    private I18NService i18nService;
    private TypedObject previousObject;


    public EditorArea()
    {
        getEditorAreaController().setModel(this);
    }


    public String getViewURI()
    {
        return this.viewURI;
    }


    @Required
    public void setViewURI(String viewURI)
    {
        this.viewURI = viewURI;
    }


    public void afterLogin(UserModel user)
    {
    }


    public void beforeLogout(UserModel user)
    {
    }


    public void globalDataLanguageChanged()
    {
        fireCurrentObjectChanged(getCurrentObject(), getCurrentObject());
    }


    public void perspectiveChanged(UICockpitPerspective previous, UICockpitPerspective newOne)
    {
        setManagingPerspective(newOne);
    }


    public UICockpitPerspective getPerspective()
    {
        return this.perspective;
    }


    public void setPerspective(UICockpitPerspective perspective)
    {
        this.perspective = perspective;
    }


    public ObjectValueHandlerRegistry getValueHandlerRegistry()
    {
        return this.valueHandlerRegistry;
    }


    @Required
    public void setValueHandlerRegistry(ObjectValueHandlerRegistry valueHandlerRegistry)
    {
        this.valueHandlerRegistry = valueHandlerRegistry;
    }


    public SystemService getSystemService()
    {
        return this.systemService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    @Required
    public void setSystemService(SystemService systemService)
    {
        this.systemService = systemService;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setCockpitTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public UndoManager getUndoManager()
    {
        return this.undoManager;
    }


    @Required
    public void setUndoManager(UndoManager undoManager)
    {
        this.undoManager = undoManager;
    }


    @Required
    public void setValueService(ValueService valueService)
    {
        this.valueService = valueService;
    }


    protected ValueService getValueService()
    {
        return this.valueService;
    }


    protected void fireCurrentObjectUpdated(TypedObject item) throws RuntimeException
    {
        if(!this.listeners.isEmpty())
        {
            for(EditorAreaListener l : (EditorAreaListener[])this.listeners.<EditorAreaListener>toArray(new EditorAreaListener[this.listeners.size()]))
            {
                try
                {
                    l.currentObjectUpdated();
                }
                catch(RuntimeException ex)
                {
                    if(!getTypeService().checkItemAlive(item))
                    {
                        throw new IllegalArgumentException(ex.getCause());
                    }
                    throw ex;
                }
                catch(Exception ex)
                {
                    LOG.error("error notifying editor area listeners: " + ex.getMessage(), ex);
                }
            }
        }
    }


    protected void fireCurrentObjectChanged(TypedObject prev, TypedObject newOne) throws RuntimeException
    {
        if(!this.listeners.isEmpty())
        {
            for(EditorAreaListener listener : (EditorAreaListener[])this.listeners.<EditorAreaListener>toArray(new EditorAreaListener[this.listeners.size()]))
            {
                try
                {
                    listener.currentObjectChanged(prev, newOne);
                }
                catch(RuntimeException ex)
                {
                    if(!getTypeService().checkItemAlive(newOne))
                    {
                        throw new IllegalArgumentException(ex.getCause());
                    }
                    throw ex;
                }
                catch(Exception ex)
                {
                    LOG.error("error notifying editor area listeners: " + ex.getMessage(), ex);
                }
            }
        }
    }


    protected void fireValuesStored(ObjectValueContainer cont)
    {
        if(!this.listeners.isEmpty())
        {
            for(EditorAreaListener l : (EditorAreaListener[])this.listeners.<EditorAreaListener>toArray(new EditorAreaListener[this.listeners.size()]))
            {
                try
                {
                    l.valuesStored(cont);
                }
                catch(Exception ex)
                {
                    LOG.error("error notifying editor area listeners: " + ex.getMessage(), ex);
                }
            }
        }
    }


    protected void fireValuesUpdated(ObjectValueContainer cont)
    {
        if(!this.listeners.isEmpty())
        {
            for(EditorAreaListener l : (EditorAreaListener[])this.listeners.<EditorAreaListener>toArray(new EditorAreaListener[this.listeners.size()]))
            {
                try
                {
                    l.valuesUpdated(cont);
                }
                catch(Exception ex)
                {
                    LOG.error("error notifying editor area listeners: " + ex.getMessage(), ex);
                }
            }
        }
    }


    protected void fireValueChanged(ObjectValueContainer cont, PropertyDescriptor propertyDescriptor)
    {
        if(!this.listeners.isEmpty())
        {
            for(EditorAreaListener l : (EditorAreaListener[])this.listeners.<EditorAreaListener>toArray(new EditorAreaListener[this.listeners.size()]))
            {
                try
                {
                    l.valueChanged(cont, propertyDescriptor);
                }
                catch(Exception ex)
                {
                    LOG.error("error notifying editor area listeners: " + ex.getMessage(), ex);
                }
            }
        }
    }


    public void addEditorAreaListener(EditorAreaListener editorAreaListener)
    {
        if(!this.listeners.contains(editorAreaListener))
        {
            this.listeners.add(editorAreaListener);
        }
    }


    public void removeEditorAreaListener(EditorAreaListener editorAreaListener)
    {
        this.listeners.remove(editorAreaListener);
    }


    public TypedObject getCurrentObject()
    {
        return this.currentObject;
    }


    public EditorConfiguration getTypeConfiguration()
    {
        if(this.typeConfiguration == null && getCurrentObjectType() != null)
        {
            if(this.currentObjectType == null && this.currentObject != null)
            {
                setCurrentObjectType((ObjectType)this.currentObject.getType());
            }
            EditorConfiguration configuration = getConfiguration(getCurrentObjectType().getCode());
            try
            {
                this.typeConfiguration = configuration.clone();
            }
            catch(CloneNotSupportedException e)
            {
                LOG.error(e.getMessage(), e);
            }
            if(this.currentObject == null)
            {
                InitialAttrEditorSectionConfiguration initAttrSecConf = new InitialAttrEditorSectionConfiguration();
                initAttrSecConf.setQualifier("create_init_attrs");
                initAttrSecConf.setAllLabel(getAllLabels("editor.section.initial_attrs"));
                List<EditorSectionConfiguration> sections = new ArrayList<>(this.typeConfiguration.getSections().size() + 1);
                sections.add(initAttrSecConf);
                sections.addAll(this.typeConfiguration.getSections());
                this.typeConfiguration.setSections(sections);
            }
            EditorHelper.initializeSections(this.typeConfiguration, this.currentObjectType, this.currentObject, getTypeService());
            if(this.currentObject == null)
            {
                for(EditorSectionConfiguration secConf : this.typeConfiguration.getSections())
                {
                    boolean empty = true;
                    for(EditorRowConfiguration rowConf : secConf.getSectionRows())
                    {
                        if(rowConf.isVisible())
                        {
                            empty = false;
                            break;
                        }
                    }
                    if(empty && !secConf.showIfEmpty())
                    {
                        secConf.setVisible(false);
                    }
                }
            }
        }
        return this.typeConfiguration;
    }


    protected Set<PropertyDescriptor> getLoadPropertyDescriptors()
    {
        Set<PropertyDescriptor> ret = new LinkedHashSet<>();
        EditorConfiguration configuration = getTypeConfiguration();
        if(configuration == null)
        {
            LOG.error("Could not load property descriptors. Reason: No Type configuration available.");
        }
        else
        {
            for(EditorSectionConfiguration sec : getTypeConfiguration().getSections())
            {
                for(EditorRowConfiguration row : sec.getSectionRows())
                {
                    if(row instanceof de.hybris.platform.cockpit.services.config.impl.PropertyEditorRowConfiguration)
                    {
                        ret.add(row.getPropertyDescriptor());
                    }
                }
            }
            ret.addAll(getCurrentObjectType().getPropertyDescriptors());
        }
        return ret;
    }


    protected Set<String> getLoadLanguages()
    {
        Set<String> ret = new LinkedHashSet<>();
        for(LanguageModel l : getSystemService().getAvailableLanguages())
        {
            ret.add(l.getIsocode());
        }
        return ret;
    }


    public void setCurrentObject(TypedObject object)
    {
        TypedObject currentObject = object;
        if(currentObject != null && currentObject.getType() == null)
        {
            throw new IllegalArgumentException("object type was NULL for object " + currentObject);
        }
        if(currentObject instanceof de.hybris.platform.cockpit.model.search.ResultObject)
        {
            currentObject = getTypeService().wrapItem(currentObject.getObject());
        }
        if(this.currentObject != currentObject && (this.currentObject == null || !this.currentObject.equals(currentObject)))
        {
            TypedObject prev = this.currentObject;
            this.currentObject = currentObject;
            if(this.currentObject != null)
            {
                this.currentObjectType = (ObjectType)getTypeService().getBestTemplate(this.currentObject);
            }
            else
            {
                this.currentObjectType = null;
            }
            this.currentObjectValues = null;
            this.typeConfiguration = null;
            if(this.useItemStack)
            {
                this.itemStack.add(currentObject);
            }
            fireCurrentObjectChanged(prev, currentObject);
        }
        else if(this.currentObject != null && currentObject == null)
        {
            TypedObject prev = this.currentObject;
            this.currentObject = null;
            fireCurrentObjectChanged(prev, null);
        }
        else if(this.currentObject != null)
        {
            update();
        }
    }


    public void setCurrentObjectType(ObjectType currentObjectType)
    {
        if(this.currentObjectType != currentObjectType && (this.currentObjectType == null ||
                        !this.currentObjectType.equals(currentObjectType)))
        {
            this.currentObjectType = currentObjectType;
            this.currentObjectValues = null;
            this.typeConfiguration = null;
        }
    }


    public ObjectType getCurrentObjectType()
    {
        return this.currentObjectType;
    }


    public void updateAllValues()
    {
        if(getCurrentObject() != null)
        {
            for(ObjectValueHandler valueHandler : getValueHandlerRegistry()
                            .getValueHandlerChain((ObjectType)getCurrentObject().getType()))
            {
                try
                {
                    valueHandler.updateValues(this.currentObjectValues, getLoadLanguages());
                }
                catch(ValueHandlerPermissionException valueHandlerPermissionException)
                {
                }
                catch(ValueHandlerException e)
                {
                    LOG.error("error updating editor values", (Throwable)e);
                }
            }
        }
    }


    public void updateValues(Set<PropertyDescriptor> properties)
    {
        if(getCurrentObject() != null)
        {
            for(ObjectValueHandler valueHandler : getValueHandlerRegistry()
                            .getValueHandlerChain((ObjectType)getCurrentObject().getType()))
            {
                try
                {
                    valueHandler.updateValues(getCurrentObjectValues(), getLoadLanguages(), properties);
                }
                catch(ValueHandlerPermissionException valueHandlerPermissionException)
                {
                }
                catch(ValueHandlerException e)
                {
                    LOG.error("error updating editor values", (Throwable)e);
                }
            }
        }
    }


    public ObjectValueContainer getCurrentObjectValues()
    {
        if(this.currentObject != null)
        {
            if(this.currentObjectValues == null)
            {
                this.currentObjectValues = TypeTools.createValueContainer(getCurrentObject(), getLoadPropertyDescriptors(),
                                getLoadLanguages(), true);
                for(EditorSectionConfiguration secConf : this.typeConfiguration.getSections())
                {
                    if(secConf instanceof CustomEditorSectionConfiguration)
                    {
                        ((CustomEditorSectionConfiguration)secConf).loadValues(this.typeConfiguration, (ObjectType)getCurrentObject().getType(),
                                        getCurrentObject(), this.currentObjectValues);
                    }
                }
            }
        }
        else if(this.currentObjectValues == null)
        {
            this.currentObjectValues = new ObjectValueContainer(this.currentObjectType, null);
            for(PropertyDescriptor pd : getLoadPropertyDescriptors())
            {
                Object currentValue = null;
                String editorType = pd.getEditorType();
                if("TEXT".equals(editorType))
                {
                    currentValue = "";
                }
                if(pd.isLocalized())
                {
                    for(String langIso : getLoadLanguages())
                    {
                        this.currentObjectValues.addValue(pd, langIso, currentValue);
                    }
                    continue;
                }
                this.currentObjectValues.addValue(pd, null, currentValue);
            }
        }
        return this.currentObjectValues;
    }


    private CockpitValidationService getValidationService()
    {
        if(this.validationService == null)
        {
            this.validationService = (CockpitValidationService)SpringUtil.getBean("cockpitValidationService");
        }
        return this.validationService;
    }


    private ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)SpringUtil.getBean("modelService");
        }
        return this.modelService;
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


    private I18NService getI18NService()
    {
        if(this.i18nService == null)
        {
            this.i18nService = (I18NService)SpringUtil.getBean("i18nService");
        }
        return this.i18nService;
    }


    public void setModelLocalizedValue(ItemModel model, String qualifier, String languageIso, Object value) throws AttributeNotSupportedException
    {
        ModelService modelService = getModelService();
        I18NService i18n = getI18NService();
        if(!(value instanceof Map) || ((Map)value).isEmpty() || !(((Map)value).keySet().iterator().next() instanceof LanguageModel))
        {
            modelService.setAttributeValue(model, qualifier, (Map)new SingletonMap(this.commonI18NService
                            .getLocaleForLanguage(this.commonI18NService.getLanguage(languageIso)), modelService
                            .toModelLayer(value)));
        }
        else
        {
            Locale backup = i18n.getCurrentLocale();
            try
            {
                i18n.setCurrentLocale(this.commonI18NService.getLocaleForLanguage(this.commonI18NService.getLanguage(languageIso)));
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
    }


    public void doSave()
    {
        ((AbstractSectionPanelModel)getEditorAreaController().getSectionPanelModel()).clearMessages();
        if(this.currentObjectValues != null && (this.currentObjectValues.isModified() || !this.currentObjectValues.isValidated()))
        {
            boolean error = false;
            Set<PropertyDescriptor> omittedProps = TypeTools.getOmittedProperties(this.currentObjectValues,
                            getLoadPropertyDescriptors(), false);
            Set<CockpitValidationDescriptor> validationInfo = new HashSet<>();
            for(PropertyDescriptor propertyDescriptor : omittedProps)
            {
                StringBuilder msg = new StringBuilder();
                msg.append(Labels.getLabel("required_attribute_missing")).append(": '").append(propertyDescriptor.getQualifier())
                                .append("'");
                validationInfo.add(new CockpitValidationDescriptor(propertyDescriptor, 3, msg.toString(), null));
            }
            createModelFromContainer();
            try
            {
                validationInfo.addAll(getValidationService().validateModel((ItemModel)this.currentObjectValues.getObject()));
                restatusIgnoredDescriptors(this.currentObjectValues, validationInfo);
            }
            catch(Exception e)
            {
                addMessage(getEditorAreaController().getSectionPanelModel(), e);
            }
            if(!validationInfo.isEmpty() && (
                            getValidationService().containsLevel(validationInfo, 3) || getValidationService().containsLevel(validationInfo, 2)))
            {
                error = true;
                validateForm(validationInfo, this.currentObject);
                this.currentObjectValues.setValidated(false);
            }
            else
            {
                try
                {
                    getValueService().setValues(getCurrentObject(), this.currentObjectValues);
                    for(EditorSectionConfiguration secConf : this.typeConfiguration.getSections())
                    {
                        if(secConf instanceof CustomEditorSectionConfiguration)
                        {
                            ((CustomEditorSectionConfiguration)secConf).saveValues(this.typeConfiguration, (ObjectType)
                                            getCurrentObject().getType(), getCurrentObject(), this.currentObjectValues);
                        }
                    }
                    this.currentObjectValues.resetModifiedFlag();
                }
                catch(ValueHandlerException vhe)
                {
                    this.currentObjectValues.setValidated(false);
                    error = true;
                    if(vhe.getProperties() != null)
                    {
                        validationInfo = vhe.getProperties();
                    }
                    validationInfo.addAll(getValidationService().validateModel((ItemModel)this.currentObject.getObject()));
                    validateForm(validationInfo, this.currentObject);
                    if(vhe instanceof de.hybris.platform.cockpit.services.values.ValueHandlerNotValidationFrameworkException)
                    {
                        addMessage(getEditorAreaController().getSectionPanelModel(), (Exception)vhe);
                    }
                    String logMsg = "Cound not update item, reason: " + vhe.getMessage();
                    LOG.warn(logMsg);
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(logMsg, (Throwable)vhe);
                    }
                }
            }
            if(!error)
            {
                this.currentObjectValues.setValidated(true);
                Set<PropertyDescriptor> modifiedProperties = new HashSet<>();
                for(ObjectValueContainer.ObjectValueHolder value : this.currentObjectValues.getModifiedValues())
                {
                    modifiedProperties.add(value.getPropertyDescriptor());
                }
                this.currentObjectValues.stored();
                fireValuesStored(this.currentObjectValues);
                UISessionUtils.getCurrentSession()
                                .sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, getCurrentObject(), modifiedProperties));
                validateForm(Collections.EMPTY_SET, this.currentObject);
                ((AbstractSectionPanelModel)getEditorAreaController().getSectionPanelModel()).clearMessages();
            }
        }
    }


    public void restatusIgnoredDescriptors(ObjectValueContainer valueContainer, Set<CockpitValidationDescriptor> validationInfo)
    {
        for(String ignoredConstraint : valueContainer.getIgnoredValidationConstraints())
        {
            for(CockpitValidationDescriptor cockpitValidationDescriptor : validationInfo)
            {
                if(cockpitValidationDescriptor.getConstraint() != null && cockpitValidationDescriptor
                                .getConstraint().getPk().toString().equals(ignoredConstraint))
                {
                    cockpitValidationDescriptor.setCockpitMessageLevel(1);
                }
            }
        }
    }


    public void createModelFromContainer()
    {
        Set<ObjectValueContainer.ObjectValueHolder> allValues = this.currentObjectValues.getModifiedValues();
        for(ObjectValueContainer.ObjectValueHolder ovh : allValues)
        {
            PropertyDescriptor propertyDescriptor = ovh.getPropertyDescriptor();
            if(ovh.isModified() && propertyDescriptor instanceof ItemAttributePropertyDescriptor && ((ItemAttributePropertyDescriptor)propertyDescriptor)
                            .isSingleAttribute())
            {
                Object currentValue = ovh.getCurrentValue();
                ItemAttributePropertyDescriptor iapd = (ItemAttributePropertyDescriptor)propertyDescriptor;
                try
                {
                    String attrQualifier = iapd.getAttributeQualifier();
                    currentValue = getModelService().toPersistenceLayer(TypeTools.container2Item(getTypeService(), currentValue));
                    if(propertyDescriptor.isLocalized())
                    {
                        setModelLocalizedValue((ItemModel)this.currentObjectValues.getObject(), attrQualifier, ovh.getLanguageIso(), currentValue);
                        continue;
                    }
                    setModelSingleValue((ItemModel)this.currentObjectValues.getObject(), attrQualifier, currentValue);
                }
                catch(AttributeNotSupportedException attributeNotSupportedException)
                {
                }
            }
        }
    }


    public void validateForm(Set<CockpitValidationDescriptor> validationInfo, TypedObject currentObject)
    {
        SectionPanelModel sectionPanelModel = getEditorAreaController().getSectionPanelModel();
        ((AbstractSectionPanelModel)sectionPanelModel).clearMessages();
        getValidationUIHelper().clearRowMarkings(sectionPanelModel);
        getValidationUIHelper().clearSectionHeaderMarkings(sectionPanelModel);
        getValidationUIHelper().markField(sectionPanelModel, validationInfo, currentObject);
        getValidationUIHelper().markSectionHeader(sectionPanelModel, validationInfo);
        getValidationUIHelper().addTypeConstraintMessages(sectionPanelModel, validationInfo);
        setValidationInfo(validationInfo);
    }


    protected void addMissingMessage(Set<PropertyDescriptor> relatedProps)
    {
        Set<PropertyDescriptor> omittedProps = new HashSet<>(relatedProps);
        SectionPanelModel panelModel = getEditorAreaController().getSectionPanelModel();
        if(panelModel instanceof RowlayoutSectionPanelModel)
        {
            Set<SectionRow> allRows = ((RowlayoutSectionPanelModel)getEditorAreaController().getSectionPanelModel()).getAllRows();
            for(SectionRow sectionRow : allRows)
            {
                if(sectionRow instanceof EditorPropertyRow)
                {
                    PropertyDescriptor prop = ((EditorPropertyRow)sectionRow).getRowConfiguration().getPropertyDescriptor();
                    if(omittedProps.contains(prop))
                    {
                        ((RowlayoutSectionPanelModel)panelModel).setRowStatus(sectionRow, 3);
                    }
                }
            }
            for(SectionRow sectionRow : allRows)
            {
                if(sectionRow instanceof EditorPropertyRow)
                {
                    PropertyDescriptor prop = ((EditorPropertyRow)sectionRow).getRowConfiguration().getPropertyDescriptor();
                    if(omittedProps.contains(prop))
                    {
                        Message msg = new Message(Labels.getLabel("required_attribute_missing") + ": '" + Labels.getLabel("required_attribute_missing") + "'.", 3, true);
                        msg.setSectionRow(sectionRow);
                        ((AbstractSectionPanelModel)panelModel).addMessage(msg);
                        omittedProps.remove(prop);
                    }
                }
            }
            for(PropertyDescriptor prop : omittedProps)
            {
                Message msg = new Message(Labels.getLabel("required_attribute_missing") + ": '" + Labels.getLabel("required_attribute_missing") + "'", 3, true);
                ((AbstractSectionPanelModel)panelModel).addMessage(msg);
            }
        }
    }


    protected void addMessage(SectionPanelModel panelModel, Exception exc)
    {
        if(panelModel instanceof RowlayoutSectionPanelModel)
        {
            Message msg = new Message(exc.getMessage(), 3, true);
            ((AbstractSectionPanelModel)panelModel).addMessage(msg);
        }
    }


    public boolean isFocused()
    {
        return equals(getManagingPerspective().getFocusedArea());
    }


    public void setFocus(boolean focus)
    {
        if(getManagingPerspective() != null)
        {
            getManagingPerspective().setFocusedArea((FocusablePerspectiveArea)this);
        }
    }


    public UICockpitPerspective getManagingPerspective()
    {
        if(this.managingPerspective == null)
        {
            return getPerspective();
        }
        return this.managingPerspective;
    }


    public void setManagingPerspective(UICockpitPerspective managingPerspective)
    {
        this.managingPerspective = managingPerspective;
    }


    public void initialize(Map<String, Object> params)
    {
    }


    public void update()
    {
        this.currentObjectValues = null;
        this.typeConfiguration = null;
        fireCurrentObjectUpdated(getCurrentObject());
    }


    public void fireNextItem(TypedObject currentObj)
    {
        if(!this.listeners.isEmpty())
        {
            for(EditorAreaListener l : (EditorAreaListener[])this.listeners.<EditorAreaListener>toArray(new EditorAreaListener[this.listeners.size()]))
            {
                l.nextItemClicked(currentObj);
            }
        }
    }


    public void firePreviousItem(TypedObject currentObj)
    {
        if(!this.listeners.isEmpty())
        {
            for(EditorAreaListener l : (EditorAreaListener[])this.listeners.<EditorAreaListener>toArray(new EditorAreaListener[this.listeners.size()]))
            {
                l.previousItemClicked(currentObj);
            }
        }
    }


    public void fireBrowseItem(TypedObject currentObj)
    {
        if(!this.listeners.isEmpty())
        {
            for(EditorAreaListener l : (EditorAreaListener[])this.listeners.<EditorAreaListener>toArray(new EditorAreaListener[this.listeners.size()]))
            {
                l.browseItemClicked(currentObj);
            }
        }
    }


    public void doNextItem()
    {
        if(this.currentObject != null)
        {
            fireNextItem(this.currentObject);
        }
    }


    public void doPreviousItem()
    {
        if(this.useItemStack)
        {
            int lastIndex = this.itemStack.size() - 1;
            if(lastIndex > 0)
            {
                this.itemStack.remove(lastIndex);
                TypedObject previousObject = this.itemStack.remove(lastIndex - 1);
                setCurrentObject(previousObject);
                getEditorAreaController().resetSectionPanelModel();
            }
        }
        else if(this.currentObject != null)
        {
            firePreviousItem(this.currentObject);
        }
    }


    public void doBrowseItem()
    {
        if(this.currentObject != null)
        {
            fireBrowseItem(this.currentObject);
        }
    }


    public void doOnChange(PropertyDescriptor propertyDescriptor)
    {
        fireValueChanged(this.currentObjectValues, propertyDescriptor);
    }


    public void reset()
    {
        if(this.useItemStack)
        {
            this.previousObject = this.currentObject;
        }
        this.currentObject = null;
        this.currentObjectType = null;
        this.typeConfiguration = null;
        this.currentObjectValues = null;
    }


    public EditorConfiguration getConfiguration(String typecode)
    {
        ObjectTemplate template = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(typecode);
        return (EditorConfiguration)getUIConfigurationService().getComponentConfiguration(template, "editorArea", EditorConfiguration.class);
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    protected Map<LanguageModel, String> getAllLabels(String labelLocKey)
    {
        Set<LanguageModel> languages = UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguages();
        Map<LanguageModel, String> allLabels = new HashMap<>(languages.size());
        for(LanguageModel lang : languages)
        {
            allLabels.put(lang, Labels.getLabel(labelLocKey));
        }
        return allLabels;
    }


    public EditorAreaController getEditorAreaController()
    {
        if(this.editorAreaController == null)
        {
            DefaultEditorAreaController eac = new DefaultEditorAreaController();
            eac.setModel(this);
            setEditorAreaController((EditorAreaController)eac);
        }
        return this.editorAreaController;
    }


    public void setEditorAreaController(EditorAreaController editorAreaController)
    {
        if((this.editorAreaController == null && editorAreaController != null) ||
                        !this.editorAreaController.equals(editorAreaController))
        {
            if(this.editorAreaController != null)
            {
                this.editorAreaController.setModel(null);
            }
            this.editorAreaController = editorAreaController;
            if(this.editorAreaController instanceof de.hybris.platform.cockpit.session.PopupEditorAreaController)
            {
                this.useItemStack = true;
            }
            if(this.editorAreaController != null)
            {
                if(this.editorAreaController.getModel() == null || !this.editorAreaController.getModel().equals(this))
                {
                    this.editorAreaController.setModel(this);
                }
            }
        }
    }


    public void addCockpitEventAcceptor(CockpitEventAcceptor acceptor)
    {
        this.cockpitEventAcceptors.add(acceptor);
    }


    public void removeCockpitEventAcceptor(CockpitEventAcceptor acceptor)
    {
        this.cockpitEventAcceptors.remove(acceptor);
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        if(event instanceof ItemChangedEvent)
        {
            boolean updateNeeded = false;
            TypedObject item = ((ItemChangedEvent)event).getItem();
            Set<PropertyDescriptor> updatedProperties = ((ItemChangedEvent)event).getProperties();
            if(item == null)
            {
                updateNeeded = (getCurrentObject() != null);
            }
            else if(getTypeService().getBaseType("Product").isAssignableFrom((ObjectType)item.getType()))
            {
                updateNeeded = (item.equals(getCurrentObject()) && containsPropertyDescriptor(updatedProperties, "Product", "supercategories") && isCurrentObjectSectionsRefreshNeeded(item));
            }
            else if(getCurrentObject() != null &&
                            getTypeService().getBaseType("Category").isAssignableFrom((ObjectType)item.getType()) &&
                            getTypeService().getBaseType("Product").isAssignableFrom((ObjectType)getCurrentObject().getType()))
            {
                updateNeeded = (containsPropertyDescriptor(updatedProperties, "Category", "products") && isCurrentObjectSectionsRefreshNeeded(getCurrentObject()));
            }
            if(!updateNeeded && item != null && getTypeService().getBaseType("ClassificationClass").isAssignableFrom((ObjectType)item.getType()))
            {
                updateNeeded = containsPropertyDescriptor(updatedProperties, "ClassificationClass", "products");
            }
            if(updateNeeded)
            {
                TypedObject currentObject2 = getCurrentObject();
                setCurrentObjectType(null);
                setCurrentObject(null);
                if(currentObject2 != null)
                {
                    setCurrentObject(getTypeService().wrapItem(currentObject2.getObject()));
                }
                getEditorAreaController().resetSectionPanelModel();
            }
            if(getCurrentObject() != null)
            {
                if(!equals(event.getSource()))
                {
                    Set<PropertyDescriptor> pds = ((ItemChangedEvent)event).getProperties();
                    if(pds != null && !pds.isEmpty())
                    {
                        for(PropertyDescriptor pd : ((ItemChangedEvent)event).getProperties())
                        {
                            getEditorAreaController().updateEditorRequest(((ItemChangedEvent)event).getItem(), pd);
                        }
                    }
                    else
                    {
                        getEditorAreaController().updateEditorRequest(((ItemChangedEvent)event).getItem(), null);
                    }
                }
                else
                {
                    EditorAreaController controller = getEditorAreaController();
                    if(controller instanceof AbstractEditorAreaController)
                    {
                        ((AbstractEditorAreaController)controller).updateLabel(controller.getSectionPanelModel());
                        for(PropertyDescriptor updatedProperty : ((ItemChangedEvent)event).getProperties())
                        {
                            ((AbstractEditorAreaController)controller).updateRegisteredSections((ObjectType)((ItemChangedEvent)event).getItem()
                                            .getType(), updatedProperty);
                        }
                    }
                }
                Set<PropertyDescriptor> properties = ((ItemChangedEvent)event).getProperties();
                if(!properties.isEmpty())
                {
                    for(PropertyDescriptor property : properties)
                    {
                        Collection<PropertyDescriptor> reverseSelectionOf = getTypeService().getReverseSelectionOf(property);
                        if(!reverseSelectionOf.isEmpty())
                        {
                            for(PropertyDescriptor propertyDescriptor : reverseSelectionOf)
                            {
                                getEditorAreaController().updateEditorRequest(((ItemChangedEvent)event).getItem(), propertyDescriptor);
                            }
                        }
                    }
                }
                if(equals(event.getSource()))
                {
                    this.currentObjectValues.resetIgnoredValidationConstraints();
                }
            }
        }
        for(CockpitEventAcceptor acceptor : this.cockpitEventAcceptors)
        {
            acceptor.onCockpitEvent(event);
        }
    }


    private boolean containsPropertyDescriptor(Set<PropertyDescriptor> propertyDescriptors, String typeCode, String qualifier)
    {
        String propertyQualifier = String.format("%s.%s", new Object[] {typeCode, qualifier});
        Optional<PropertyDescriptor> matchingDescriptor = propertyDescriptors.stream().filter(propertyDescriptor -> propertyQualifier.equalsIgnoreCase(propertyDescriptor.getQualifier())).findAny();
        return matchingDescriptor.isPresent();
    }


    private boolean isCurrentObjectSectionsRefreshNeeded(TypedObject item)
    {
        if(getCurrentObjectType() != null && ObjectUtils.equals(getCurrentObject(), item))
        {
            ObjectType oldTemplate = getCurrentObjectType();
            TypedObject refreshedObject = getTypeService().wrapItem(item.getObject());
            return (!oldTemplate.equals(this.typeService.getBestTemplate(refreshedObject)) ||
                            !haveTheSameExtendedTypes(getCurrentObject(), refreshedObject));
        }
        return true;
    }


    private boolean haveTheSameExtendedTypes(TypedObject currentObject, TypedObject updatedObject)
    {
        Collection<ExtendedType> extTypes = (currentObject.getExtendedTypes() != null) ? currentObject.getExtendedTypes() : Collections.<ExtendedType>emptyList();
        Collection<ExtendedType> updatedExtTypes = (updatedObject.getExtendedTypes() != null) ? updatedObject.getExtendedTypes() : Collections.<ExtendedType>emptyList();
        return CollectionUtils.isEqualCollection(extTypes, updatedExtTypes);
    }


    public void revalidateForm()
    {
        this.validationInfo = getValidationService().validateModel((ItemModel)this.currentObject.getObject());
        restatusIgnoredDescriptors(this.currentObjectValues, this.validationInfo);
        validateForm(this.validationInfo, this.currentObject);
    }


    public void resetStack()
    {
        this.itemStack.clear();
    }


    public String getLabel()
    {
        return null;
    }


    public String getWidth()
    {
        return this.width;
    }


    public void setWidth(String width)
    {
        this.width = width;
    }


    private ValidationUIHelper getValidationUIHelper()
    {
        if(this.validationUIHelper == null)
        {
            this.validationUIHelper = (ValidationUIHelper)SpringUtil.getBean("validationUIHelper");
        }
        return this.validationUIHelper;
    }


    public List<String> getConstraintsPk()
    {
        return this.constraintsPk;
    }


    public void addConstraintsPk(String constraintsPk)
    {
        this.constraintsPk.add(constraintsPk);
    }


    public SessionService getsessionService()
    {
        return this.sessionService;
    }


    public int getItemStackSize()
    {
        return this.itemStack.size();
    }
}
