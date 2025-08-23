package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.components.sectionpanel.AbstractSectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.Message;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.helpers.validation.ValidationUIHelper;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.undo.UndoableOperation;
import de.hybris.platform.cockpit.model.undo.impl.ItemChangeUndoableOperation;
import de.hybris.platform.cockpit.services.NewItemService;
import de.hybris.platform.cockpit.services.config.CustomEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.impl.DefaultEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.validation.pojos.CockpitValidationDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueHandler;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueHandlerPermissionException;
import de.hybris.platform.cockpit.services.values.ValueService;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.EditorAreaController;
import de.hybris.platform.cockpit.session.PopupEditorAreaController;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UIEditorArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.NewItemPersistencePredicate;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UndoTools;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Messagebox;

public class NewItemSection extends CustomEditorSection
{
    private static final Logger LOG = LoggerFactory.getLogger(NewItemSection.class);
    protected final UIEditorArea editorArea;
    protected final EditorAreaController editorAreaController;
    private ModelService modelService;
    private ValueService valueService;
    private ValidationUIHelper validationUIHelper;


    public NewItemSection(EditorAreaController editorAreaController, String label)
    {
        super((EditorSectionConfiguration)new DefaultEditorSectionConfiguration(null));
        if(editorAreaController == null)
        {
            throw new IllegalArgumentException("Editor area can not be null.");
        }
        this.editorArea = editorAreaController.getModel();
        this.editorAreaController = editorAreaController;
        setLabel(label);
    }


    public SectionRenderer getCustomRenderer()
    {
        return (SectionRenderer)new Object(this);
    }


    protected void addMessage(SectionPanel panel, Exception exc)
    {
        SectionPanelModel panelModel = panel.getModel();
        if(panelModel instanceof de.hybris.platform.cockpit.components.sectionpanel.RowlayoutSectionPanelModel)
        {
            Message msg = new Message(exc.getMessage(), 3, true);
            ((AbstractSectionPanelModel)panelModel).addMessage(msg);
        }
    }


    protected void createNewItem(SectionPanel panel)
    {
        NewItemService nis = getNewItemService();
        UICockpitPerspective perspective = getEditorArea().getManagingPerspective();
        ObjectValueContainer newContainer = getEditorArea().getCurrentObjectValues();
        SectionPanelModel panelModel = panel.getModel();
        ((AbstractSectionPanelModel)panelModel).clearMessages();
        List<TypedObject> originalContextItems = Collections.EMPTY_LIST;
        AdvancedBrowserModel ctxBrowser = null;
        boolean itemCreatedInPopupEditor = this.editorAreaController instanceof PopupEditorAreaController;
        if(itemCreatedInPopupEditor)
        {
            BrowserModel contextEditorBrowserModel = ((PopupEditorAreaController)this.editorAreaController).getContextEditorBrowserModel();
            if(contextEditorBrowserModel instanceof AdvancedBrowserModel)
            {
                ctxBrowser = (AdvancedBrowserModel)contextEditorBrowserModel;
                originalContextItems = new ArrayList<>(ctxBrowser.getContextItems());
            }
        }
        TypedObject newItem = null;
        try
        {
            newItem = nis.createNewItem(newContainer, (ObjectTemplate)getEditorArea().getCurrentObjectType());
        }
        catch(ValueHandlerPermissionException e)
        {
            try
            {
                Messagebox.show(Labels.getLabel("security.permision_denied"));
            }
            catch(InterruptedException e2)
            {
                LOG.error("Could not show messagebox, reason: ", e2);
            }
        }
        catch(ValueHandlerException e)
        {
            if(e instanceof de.hybris.platform.cockpit.services.values.ValueHandlerNotValidationFrameworkException || e.getCause() instanceof IllegalArgumentException)
            {
                addMessage(panel, (Exception)e);
            }
            Set<CockpitValidationDescriptor> validationInfo = e.getProperties();
            validateForm(panelModel, validationInfo, newItem);
            String logMsg = "Could not create item, reason: ";
            LOG.warn("Could not create item, reason: " + e.getMessage());
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Could not create item, reason: " + e.getMessage(), (Throwable)e);
            }
        }
        if(newItem != null)
        {
            if(!itemCreatedInPopupEditor)
            {
                getEditorArea().setCurrentObject(newItem);
                for(EditorSectionConfiguration secConf : getEditorArea().getTypeConfiguration().getSections())
                {
                    if(secConf instanceof CustomEditorSectionConfiguration)
                    {
                        ((CustomEditorSectionConfiguration)secConf).saveValues(getEditorArea().getTypeConfiguration(), (ObjectType)getEditorArea()
                                        .getCurrentObject().getType(), getEditorArea().getCurrentObject(), getEditorArea()
                                        .getCurrentObjectValues());
                    }
                }
                getEditorArea().getCurrentObjectValues().stored();
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this,
                                getEditorArea().getCurrentObject(), Collections.EMPTY_LIST, ItemChangedEvent.ChangeType.CREATED));
                getEditorArea().globalDataLanguageChanged();
            }
            Notification notification = new Notification(Labels.getLabel("cockpit.created_item"), Labels.getLabel("cockpit.created_instance_type", (Object[])new String[] {newItem
                            .getType().getName()}));
            if(perspective instanceof BaseUICockpitPerspective)
            {
                BaseUICockpitPerspective basePerspective = (BaseUICockpitPerspective)perspective;
                if(basePerspective.getNotifier() != null)
                {
                    basePerspective.getNotifier().setNotification(notification);
                }
                if(itemCreatedInPopupEditor)
                {
                    BrowserModel contextEditorBrowserModel = ((PopupEditorAreaController)this.editorAreaController).getContextEditorBrowserModel();
                    if(contextEditorBrowserModel instanceof AdvancedBrowserModel)
                    {
                        ctxBrowser = (AdvancedBrowserModel)contextEditorBrowserModel;
                        List<TypedObject> contextItems = new ArrayList<>(ctxBrowser.getContextItems());
                        NewItemPersistencePredicate filterPredicate = new NewItemPersistencePredicate();
                        CollectionUtils.filter(contextItems, (Predicate)filterPredicate);
                        CollectionUtils.filter(originalContextItems, (Predicate)filterPredicate);
                        TypedObject rootItem = ctxBrowser.getContextRootItem();
                        contextItems.add(newItem);
                        ctxBrowser.setContextItems(rootItem, contextItems);
                        PropertyDescriptor propertyDescriptor = ctxBrowser.getContextRootTypePropertyDescriptor();
                        if(propertyDescriptor == null)
                        {
                            LOG.warn("Could not update editor area, root property descriptor not set");
                        }
                        else if(rootItem != null)
                        {
                            handleSetValue(propertyDescriptor, rootItem, originalContextItems, contextItems, null, null);
                        }
                    }
                    else
                    {
                        Object context = ((PopupEditorAreaController)this.editorAreaController).getAttributesMap().get("createContext");
                        if(context instanceof CreateContext)
                        {
                            CreateContext createContext = (CreateContext)context;
                            if(createContext.getSourceObject() != null)
                            {
                                Set<String> langIsos = (createContext.getLangIso() != null) ? Collections.<String>singleton(createContext.getLangIso()) : Collections.EMPTY_SET;
                                PropertyDescriptor propertyDescriptor = createContext.getPropertyDescriptor();
                                ObjectValueContainer valueContainer = TypeTools.createValueContainer(createContext
                                                .getSourceObject(), Collections.singleton(propertyDescriptor), langIsos);
                                ObjectValueContainer.ObjectValueHolder valueHolder = valueContainer.getValue(propertyDescriptor,
                                                propertyDescriptor.isLocalized() ? createContext.getLangIso() : null);
                                if(createContext.getPropertyDescriptor().getMultiplicity().equals(PropertyDescriptor.Multiplicity.SINGLE))
                                {
                                    TypedObject originalValue = (TypedObject)valueHolder.getLocalValue();
                                    valueHolder.setLocalValue(newItem);
                                    storeContainer(valueContainer, (ObjectType)createContext.getSourceObject().getType());
                                    handleSetValue(createContext.getPropertyDescriptor(), createContext.getSourceObject(),
                                                    (originalValue == null) ? null : Collections.<TypedObject>singletonList(originalValue),
                                                    Collections.singletonList(newItem), valueContainer, null);
                                }
                                else if(createContext.getPropertyDescriptor().getMultiplicity().equals(PropertyDescriptor.Multiplicity.LIST) || createContext
                                                .getPropertyDescriptor().getMultiplicity().equals(PropertyDescriptor.Multiplicity.SET))
                                {
                                    List<TypedObject> originalValues = UISessionUtils.getCurrentSession().getTypeService().wrapItems((Collection)valueHolder.getLocalValue());
                                    List<TypedObject> newValues = new ArrayList<>(originalValues);
                                    newValues.add(newItem);
                                    valueHolder.setLocalValue(unwrap(newValues));
                                    storeContainer(valueContainer, (ObjectType)createContext.getSourceObject().getType());
                                    handleSetValue(createContext.getPropertyDescriptor(), createContext.getSourceObject(), originalValues, newValues, valueContainer, createContext
                                                    .getLangIso());
                                }
                            }
                            else if(createContext.getPropertyDescriptor() != null)
                            {
                                UIEditorArea editorArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getEditorArea();
                                if(editorArea instanceof EditorArea)
                                {
                                    ObjectValueContainer currentObjectValues = editorArea.getCurrentObjectValues();
                                    ObjectValueContainer.ObjectValueHolder valueHolder = currentObjectValues.getValue(createContext
                                                    .getPropertyDescriptor(), createContext.getLangIso());
                                    valueHolder.setLocalValue(newItem);
                                    EditorAreaController editorAreaController = editorArea.getEditorAreaController();
                                    editorAreaController.updateEditorRequest(null, createContext.getPropertyDescriptor());
                                }
                            }
                        }
                    }
                    basePerspective.closePopupEditor();
                    Map<String, Object> attributes = ((PopupEditorAreaController)this.editorAreaController).getAttributesMap();
                    if(attributes.containsKey("forceModelUpdate"))
                    {
                        attributes.remove("forceModelUpdate");
                    }
                }
            }
            perspective.getNavigationArea().update();
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, newItem, Collections.EMPTY_LIST, ItemChangedEvent.ChangeType.CREATED));
        }
    }


    protected void validateForm(SectionPanelModel panelModel, Set<CockpitValidationDescriptor> validationInfo, TypedObject currentObject)
    {
        getValidationUIHelper().clearRowMarkings(panelModel);
        getValidationUIHelper().clearSectionHeaderMarkings(panelModel);
        getValidationUIHelper().markField(panelModel, validationInfo, currentObject);
        getValidationUIHelper().markSectionHeader(panelModel, validationInfo);
        getValidationUIHelper().addTypeConstraintMessages(panelModel, validationInfo);
        ((EditorArea)getEditorArea()).setValidationInfo(validationInfo);
    }


    protected UIEditorArea getEditorArea()
    {
        return this.editorArea;
    }


    protected ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)SpringUtil.getBean("modelService");
        }
        return this.modelService;
    }


    protected ValueService getValueService()
    {
        if(this.valueService == null)
        {
            this.valueService = (ValueService)SpringUtil.getBean("valueService");
        }
        return this.valueService;
    }


    protected NewItemService getNewItemService()
    {
        return UISessionUtils.getCurrentSession().getNewItemService();
    }


    private void handleSetValue(PropertyDescriptor propertyDescriptor, TypedObject rootItem, List<TypedObject> originalValues, List<TypedObject> newValues, ObjectValueContainer valueContainer, String langIso)
    {
        String langIsoInternal = StringUtils.isNotBlank(langIso) ? langIso : UISessionUtils.getCurrentSession().getSystemService().getCurrentLanguage().getIsocode();
        if(propertyDescriptor instanceof ItemAttributePropertyDescriptor)
        {
            ItemAttributePropertyDescriptor iapd = (ItemAttributePropertyDescriptor)propertyDescriptor;
            JaloConnection.getInstance().logItemModification((Item)
                                            getModelService().getSource(rootItem.getObject()),
                            Collections.singletonMap(iapd.getLastAttributeDescriptor().getQualifier(), (newValues == null) ? null :
                                            unwrap(newValues)),
                            Collections.singletonMap(iapd.getLastAttributeDescriptor().getQualifier(), (originalValues == null) ? null :
                                            unwrap(originalValues)), false);
        }
        if(valueContainer != null)
        {
            UndoTools.addUndoOperationAndEvent(UISessionUtils.getCurrentSession().getUndoManager(), (UndoableOperation)new ItemChangeUndoableOperation(rootItem, valueContainer), this);
        }
        try
        {
            getValueService().setValue(rootItem, propertyDescriptor, newValues, langIsoInternal);
        }
        catch(ValueHandlerException e)
        {
            LOG.error("Could not save new values of '" + propertyDescriptor.getQualifier() + "' from item '" + rootItem + "'", (Throwable)e);
        }
        UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this.editorArea, rootItem,
                        Collections.singleton(propertyDescriptor)));
    }


    private void storeContainer(ObjectValueContainer container, ObjectType type)
    {
        try
        {
            for(ObjectValueHandler valueHandler : UISessionUtils.getCurrentSession().getValueHandlerRegistry()
                            .getValueHandlerChain(type))
            {
                valueHandler.storeValues(container);
            }
        }
        catch(Exception e)
        {
            LOG.error("Could not store values: " + e.getMessage(), e);
        }
    }


    private List<Object> unwrap(List<TypedObject> items)
    {
        if(items == null || items.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<Object> unwrapped = new ArrayList();
        for(TypedObject object : items)
        {
            Object model = object.getObject();
            if(model != null)
            {
                unwrapped.add(getModelService().getSource(model));
            }
        }
        return unwrapped;
    }


    private ValidationUIHelper getValidationUIHelper()
    {
        if(this.validationUIHelper == null)
        {
            this.validationUIHelper = (ValidationUIHelper)SpringUtil.getBean("validationUIHelper");
        }
        return this.validationUIHelper;
    }
}
