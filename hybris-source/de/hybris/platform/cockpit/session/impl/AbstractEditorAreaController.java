package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.sectionpanel.DefaultSectionPanelLabelRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.EditableSection;
import de.hybris.platform.cockpit.components.sectionpanel.Message;
import de.hybris.platform.cockpit.components.sectionpanel.RowEvent;
import de.hybris.platform.cockpit.components.sectionpanel.RowlayoutSectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionChangeEvent;
import de.hybris.platform.cockpit.components.sectionpanel.SectionEvent;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelEvent;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelLabelRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRow;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRowRenderer;
import de.hybris.platform.cockpit.helpers.validation.ValidationUIHelper;
import de.hybris.platform.cockpit.model.meta.EditorFactory;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ClassAttributePropertyDescriptor;
import de.hybris.platform.cockpit.services.NewItemService;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.impl.DefaultEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.services.validation.CockpitValidationService;
import de.hybris.platform.cockpit.services.validation.pojos.CockpitValidationDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.EditorAreaController;
import de.hybris.platform.cockpit.session.EditorAreaListener;
import de.hybris.platform.cockpit.session.EditorSectionPanelModel;
import de.hybris.platform.cockpit.session.MutableSectionModelController;
import de.hybris.platform.cockpit.session.UIEditorArea;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionListener;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericComposer;
import org.zkoss.zul.Div;

public abstract class AbstractEditorAreaController extends GenericComposer implements EditorAreaController, MutableSectionModelController
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractEditorAreaController.class);
    private static final String EDITOR_CONFIG_STORE_ONTHEFLY = "editor.config.store.onthefly";
    protected UIEditorArea model;
    protected Component onLaterComponent;
    private SectionPanelModel sectionPanelModel = null;
    private SectionRowRenderer editorRowRenderer = null;
    private SectionPanelLabelRenderer panelLabelRenderer = null;
    private SectionRenderer sectionRenderer = null;
    private EditorFactory editorFactory;
    private UIConfigurationService uiConfigurationService = null;
    private CockpitValidationService validationService;
    protected boolean initialized = false;
    private EditorAreaListener editorAreaListener;
    protected ObjectValueContainer initialValues = null;
    private ValidationUIHelper validationUIHelper;
    private final Map<Object, Set<Section>> sectionUpdateRegistry = new HashMap<>();


    public void setCreateFromTemplate(ObjectType createFromTemplate)
    {
        getModel().setCurrentObjectType(createFromTemplate);
    }


    public void setCreateFromTemplate(ObjectType createFromTemplate, Map<String, ? extends Object> initialValues)
    {
        setCreateFromTemplate(createFromTemplate, initialValues, true);
    }


    public void setCreateFromTemplate(ObjectType createFromTemplate, Map<String, ? extends Object> initValues, boolean loadDefaultValues)
    {
        this.initialValues = new ObjectValueContainer(createFromTemplate, null);
        if(loadDefaultValues)
        {
            ObjectTemplate objTemplate = (createFromTemplate instanceof ObjectTemplate) ? (ObjectTemplate)createFromTemplate : UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(createFromTemplate.getCode());
            getNewItemService().setDefaultValues(this.initialValues, objTemplate);
        }
        TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
        for(Map.Entry<String, ? extends Object> entry : initValues.entrySet())
        {
            PropertyDescriptor propertyDescriptor = typeService.getPropertyDescriptor(entry.getKey());
            if(propertyDescriptor != null)
            {
                addValue(propertyDescriptor, entry.getValue());
            }
        }
        setCreateFromTemplate(createFromTemplate);
    }


    protected void addValue(PropertyDescriptor propertyDescriptor, Object value)
    {
        if(propertyDescriptor.isLocalized())
        {
            for(String langIso : UISessionUtils.getCurrentSession().getSystemService().getAllWriteableLanguageIsos())
            {
                Object locValue = value;
                if(value instanceof Map)
                {
                    locValue = ((Map)value).get(langIso);
                }
                addValue(propertyDescriptor, locValue, langIso);
            }
        }
        else
        {
            addValue(propertyDescriptor, value, null);
        }
    }


    protected void addValue(PropertyDescriptor propertyDescriptor, Object value, String langIso)
    {
        try
        {
            ObjectValueContainer.ObjectValueHolder valueHolder = this.initialValues.getValue(propertyDescriptor, langIso);
            valueHolder.setLocalValue(mergeInitValues(valueHolder.getLocalValue(), value));
        }
        catch(IllegalArgumentException e)
        {
            this.initialValues.addValue(propertyDescriptor, langIso, value);
        }
    }


    protected Object mergeInitValues(Object val1, Object val2)
    {
        Object ret = val2;
        if(val1 == null)
        {
            ret = val2;
        }
        else if(val2 == null)
        {
            ret = val1;
        }
        else if(val1 instanceof Collection && val2 instanceof Collection)
        {
            ret = mergeCollections((Collection)val1, (Collection)val2);
        }
        return ret;
    }


    protected Collection mergeCollections(Collection<?> coll1, Collection coll2)
    {
        Collection ret = coll2;
        if(coll1 instanceof Set)
        {
            ret = new HashSet(coll1);
        }
        else
        {
            ret = new ArrayList(coll1);
        }
        ret.addAll(coll2);
        return ret;
    }


    public boolean initialize()
    {
        this.initialized = false;
        if(getModel() != null)
        {
            if(UISessionUtils.getCurrentSession().isCachePerspectivesEnabled())
            {
                if(this.sectionPanelModel == null)
                {
                    this.sectionPanelModel = createSectionPanelModel();
                }
                if(this.panelLabelRenderer == null)
                {
                    this.panelLabelRenderer = createSectionPanelLabelRenderer();
                }
                if(this.sectionRenderer == null)
                {
                    this.sectionRenderer = createSectionRenderer();
                }
            }
            else
            {
                this.sectionPanelModel = createSectionPanelModel();
                this.panelLabelRenderer = createSectionPanelLabelRenderer();
                this.sectionRenderer = createSectionRenderer();
            }
            this.initialized = true;
        }
        return this.initialized;
    }


    public UIEditorArea getModel()
    {
        return this.model;
    }


    public void setModel(UIEditorArea model)
    {
        if(this.model != null)
        {
            this.model.removeEditorAreaListener(getEditorAreaListener());
        }
        this.model = model;
        if(this.model != null)
        {
            this.model.addEditorAreaListener(getEditorAreaListener());
        }
    }


    protected EditorAreaListener getEditorAreaListener()
    {
        if(this.editorAreaListener == null)
        {
            this.editorAreaListener = (EditorAreaListener)new Object(this);
        }
        return this.editorAreaListener;
    }


    protected void doOnChange(PropertyDescriptor propertyDescriptor)
    {
        if(getModel() != null)
        {
            getModel().doSave();
        }
    }


    public Component getOnLaterComponent()
    {
        return this.onLaterComponent;
    }


    public abstract void updateLabel(SectionPanelModel paramSectionPanelModel);


    public abstract void resetSectionPanelModel();


    protected abstract void setDefaultValues();


    protected void addCreationSection(EditorSectionPanelModel model)
    {
        EditorConfiguration cfg = getModel().getTypeConfiguration();
        EditorSection unassignedSection = null;
        NewItemSection newItemSection = new NewItemSection(this, null);
        newItemSection.setLabel(Labels.getLabel("editor.button.create"));
        model.addSection((Section)newItemSection);
        for(EditorSectionConfiguration secConf : cfg.getSections())
        {
            List<SectionRow> rows = new ArrayList<>();
            for(EditorRowConfiguration rowConf : secConf.getSectionRows())
            {
                EditorPropertyRow row = new EditorPropertyRow(rowConf);
                row.setVisible(isVisible(rowConf, model.isCreateMode()));
                row.setEditable(isEditable(rowConf, model.isCreateMode()));
                rows.add(row);
            }
            if(secConf instanceof de.hybris.platform.cockpit.services.config.impl.UnassignedEditorSectionConfiguration && unassignedSection == null)
            {
                unassignedSection = new EditorSection(secConf);
                ((DefaultEditorSectionPanelModel)model).addSection((Section)unassignedSection, rows);
                continue;
            }
            if(secConf instanceof de.hybris.platform.cockpit.services.config.CustomEditorSectionConfiguration)
            {
                ((DefaultEditorSectionPanelModel)model).addSection((Section)new CustomEditorSection(secConf), rows);
                continue;
            }
            ((DefaultEditorSectionPanelModel)model).addSection((Section)new EditorSection(secConf), rows);
        }
    }


    protected boolean isVisible(EditorRowConfiguration rowConf, boolean creationMode)
    {
        ObjectType type = getModel().getCurrentObjectType();
        PropertyDescriptor propertyDescriptor = rowConf.getPropertyDescriptor();
        boolean visible = (rowConf.isVisible() && getUIAccessRightService().isReadable(type, propertyDescriptor, creationMode));
        if(!visible && creationMode)
        {
            visible = TypeTools.isInitial(propertyDescriptor);
        }
        return visible;
    }


    protected boolean isEditable(EditorRowConfiguration rowConf, boolean creationMode)
    {
        ObjectType type = getModel().getCurrentObjectType();
        PropertyDescriptor propertyDescriptor = rowConf.getPropertyDescriptor();
        TypedObject currentObject = getModel().getCurrentObject();
        boolean editable = (rowConf.isEditable() && getUIAccessRightService().isWritable(type, currentObject, propertyDescriptor, creationMode));
        if(!editable && creationMode)
        {
            editable = (TypeTools.isInitial(propertyDescriptor) && propertyDescriptor.isReadable());
        }
        return editable;
    }


    protected UIAccessRightService getUIAccessRightService()
    {
        return UISessionUtils.getCurrentSession().getUiAccessRightService();
    }


    protected boolean isReadable(EditorRowConfiguration rowConf, boolean creationMode)
    {
        ObjectType type = getModel().getCurrentObjectType();
        PropertyDescriptor propertyDescriptor = rowConf.getPropertyDescriptor();
        return getUIAccessRightService().isReadable(type, propertyDescriptor, creationMode);
    }


    public void doAfterCompose(Component comp) throws Exception
    {
        super.doAfterCompose(comp);
        UISession session = UISessionUtils.getCurrentSession();
        if(this.model != null)
        {
            session.addSessionListener((UISessionListener)this.model);
        }
        this.onLaterComponent = comp;
    }


    public ObjectType getCreateFromTemplate()
    {
        return getModel().getCurrentObjectType();
    }


    public SectionPanelModel getSectionPanelModel()
    {
        return this.sectionPanelModel;
    }


    public void setSectionRowRenderer(SectionRowRenderer rowRenderer)
    {
        this.editorRowRenderer = rowRenderer;
    }


    public SectionRowRenderer getSectionRowRenderer()
    {
        return this.editorRowRenderer;
    }


    public SectionPanelLabelRenderer getSectionPanelLabelRenderer()
    {
        return this.panelLabelRenderer;
    }


    public SectionRenderer getSectionRenderer()
    {
        return this.sectionRenderer;
    }


    protected SectionPanelModel createSectionPanelModel()
    {
        return (SectionPanelModel)new DefaultEditorSectionPanelModel(getModel());
    }


    protected SectionPanelLabelRenderer createSectionPanelLabelRenderer()
    {
        return (SectionPanelLabelRenderer)new DefaultSectionPanelLabelRenderer(getModel());
    }


    protected SectionRenderer createSectionRenderer()
    {
        return (SectionRenderer)new EditorSectionRenderer(this);
    }


    protected void updateHiddenFieldsArea(Component propertyArea, SectionPanelModel model, Section section)
    {
        if(getSectionRenderer() instanceof EditorSectionRenderer)
        {
            ((EditorSectionRenderer)getSectionRenderer()).updatePropertyArea(propertyArea, model, section);
        }
    }


    protected abstract void storeEditorSectionOrder();


    protected void storeEditorSectionLabel(EditableSection section)
    {
        LOG.warn("Storing of editor section label not implemented.");
    }


    public void onShowAllRows(Section section, SectionPanel panel)
    {
        if(getSectionPanelModel() instanceof RowlayoutSectionPanelModel)
        {
            RowlayoutSectionPanelModel rowPanelModel = (RowlayoutSectionPanelModel)getSectionPanelModel();
            List<SectionRow> rows = rowPanelModel.getRows(section);
            for(SectionRow row : rows)
            {
                rowPanelModel.showRow(row);
            }
            SectionPanel.SectionComponent sectionComponent = panel.getSectionComponent(section);
            if(sectionComponent != null)
            {
                Div propertyArea = (Div)sectionComponent.getContainer().getFellowIfAny("property_edit_container");
                if(propertyArea != null)
                {
                    updateHiddenFieldsArea((Component)propertyArea, getSectionPanelModel(), section);
                }
                if(section.isTabbed())
                {
                    ((DefaultEditorSectionPanelModel)getSectionPanelModel()).sectionUpdated(section);
                }
            }
        }
    }


    public void onRowShow(SectionPanelEvent evt)
    {
        RowEvent rowEvent = (RowEvent)evt;
        ((EditorSectionPanelModel)getSectionPanelModel()).showRow(((RowEvent)evt).getRow());
        SectionPanel.SectionComponent sectionComponent = ((SectionPanel)rowEvent.getTarget()).getSectionComponent(rowEvent.getSection());
        if(sectionComponent != null)
        {
            Div propertyArea = (Div)sectionComponent.getContainer().getFellowIfAny("property_edit_container");
            if(propertyArea != null)
            {
                updateHiddenFieldsArea((Component)propertyArea, getSectionPanelModel(), rowEvent.getSection());
            }
            if(rowEvent.getSection().isTabbed())
            {
                ((DefaultEditorSectionPanelModel)getSectionPanelModel()).sectionUpdated(rowEvent.getSection());
            }
        }
        storeEditorSectionOrder();
    }


    public void onRowHide(SectionPanelEvent evt)
    {
        RowEvent rowEvent = (RowEvent)evt;
        ((DefaultEditorSectionPanelModel)getSectionPanelModel()).hideRow(rowEvent.getRow());
        SectionPanel.SectionComponent sectionComponent = ((SectionPanel)rowEvent.getTarget()).getSectionComponent(rowEvent.getSection());
        if(sectionComponent != null)
        {
            Div propertyArea = (Div)sectionComponent.getContainer().getFellowIfAny("property_edit_container");
            if(propertyArea != null)
            {
                updateHiddenFieldsArea((Component)propertyArea, getSectionPanelModel(), rowEvent.getSection());
            }
            if(rowEvent.getSection().isTabbed())
            {
                ((DefaultEditorSectionPanelModel)getSectionPanelModel()).sectionUpdated(rowEvent.getSection());
            }
        }
        storeEditorSectionOrder();
    }


    public void onRowMoved(SectionPanelEvent evt)
    {
        ((DefaultEditorSectionPanelModel)getSectionPanelModel()).moveRow(((RowEvent)evt).getRow(), ((RowEvent)evt).getSection(), ((RowEvent)evt)
                        .getIndex());
        storeEditorSectionOrder();
    }


    public void onSectionHide(SectionPanelEvent evt)
    {
        ((DefaultEditorSectionPanelModel)getSectionPanelModel()).hideSection(((SectionEvent)evt).getSection());
        storeEditorSectionOrder();
    }


    public void onSectionShow(SectionPanelEvent evt)
    {
        ((EditorSectionPanelModel)getSectionPanelModel()).showSection(((SectionEvent)evt).getSection());
        storeEditorSectionOrder();
    }


    public void onSectionMoved(SectionPanelEvent evt)
    {
        ((DefaultEditorSectionPanelModel)getSectionPanelModel()).moveSection(((SectionEvent)evt).getSection(), ((SectionEvent)evt)
                        .getIndex());
        storeEditorSectionOrder();
    }


    public void onSectionOpen(SectionPanelEvent evt)
    {
        String modifier = UITools.getCockpitParameter("editor.config.store.onthefly", Executions.getCurrent().getDesktop());
        if(((SectionPanel)evt.getTarget()).isEditMode() || Boolean.parseBoolean(modifier))
        {
            storeEditorSectionOrder();
        }
        Set<CockpitValidationDescriptor> validationInfo = ((EditorArea)getModel()).getValidationInfo();
        if(validationInfo != null && !validationInfo.isEmpty())
        {
            getValidationUIHelper().markField(getSectionPanelModel(), validationInfo, ((EditorArea)getModel()).getCurrentObject());
        }
    }


    public void onSectionClosed(SectionPanelEvent evt)
    {
        String modifier = UITools.getCockpitParameter("editor.config.store.onthefly", Executions.getCurrent().getDesktop());
        if(((SectionPanel)evt.getTarget()).isEditMode() || Boolean.parseBoolean(modifier))
        {
            storeEditorSectionOrder();
        }
    }


    public void onMessageClicked(SectionPanelEvent evt)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Message clicked " + ((Message)evt.getData()).getText());
        }
    }


    public void onMessageHide(SectionPanelEvent evt)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Message hide clicked " + ((Message)evt.getData()).getText());
        }
    }


    public void onSectionAdded(SectionPanelEvent event)
    {
        DefaultEditorSectionConfiguration defaultEditorSectionConfiguration = new DefaultEditorSectionConfiguration();
        defaultEditorSectionConfiguration.setQualifier("unnamed");
        EditorSection editorSection = new EditorSection((EditorSectionConfiguration)defaultEditorSectionConfiguration);
        this.sectionPanelModel.addSection((Section)editorSection);
        storeEditorSectionOrder();
        resetSectionPanelModel();
    }


    public void onSectionRemoved(SectionPanelEvent event)
    {
        if(event instanceof SectionEvent)
        {
            Section section = ((SectionEvent)event).getSection();
            this.sectionPanelModel.removeSection(section);
            storeEditorSectionOrder();
            resetSectionPanelModel();
        }
    }


    public void onSectionRenamed(SectionPanelEvent event)
    {
        if(event instanceof SectionChangeEvent)
        {
            Section section = ((SectionChangeEvent)event).getSection();
            String label = ((SectionChangeEvent)event).getLabel();
            if(section instanceof EditableSection)
            {
                ((EditableSection)section).setLabel(label);
                storeEditorSectionLabel((EditableSection)section);
                storeEditorSectionOrder();
                resetSectionPanelModel();
            }
        }
    }


    public void onLater(Event event)
    {
        resetSectionPanelModel();
    }


    protected NewItemService getNewItemService()
    {
        return UISessionUtils.getCurrentSession().getNewItemService();
    }


    @Required
    public void setEditorFactory(EditorFactory editorFactory)
    {
        this.editorFactory = editorFactory;
    }


    public EditorFactory getEditorFactory()
    {
        return this.editorFactory;
    }


    public void showAllSections()
    {
        for(Section section : getSectionPanelModel().getSections())
        {
            getSectionPanelModel().showSection(section);
        }
        storeEditorSectionOrder();
    }


    private void updateRowForDescriptor(PropertyDescriptor descriptor)
    {
        SectionPanelModel mod = getSectionPanelModel();
        if(mod instanceof RowlayoutSectionPanelModel)
        {
            Set<SectionRow> allRows = ((RowlayoutSectionPanelModel)mod).getAllRows();
            SectionRow toUpdate = null;
            String valueInfo = null;
            if(descriptor != null)
            {
                if(descriptor instanceof ClassAttributePropertyDescriptor)
                {
                    valueInfo = ((ClassAttributePropertyDescriptor)descriptor).getClassificationAttributeValueInfo();
                }
                else
                {
                    valueInfo = UISessionUtils.getCurrentSession().getTypeService().getAttributeCodeFromPropertyQualifier(descriptor.getQualifier());
                }
            }
            if(valueInfo != null)
            {
                for(SectionRow sectionRow : allRows)
                {
                    if(valueInfo.equals(sectionRow.getValueInfo()))
                    {
                        toUpdate = sectionRow;
                        break;
                    }
                }
            }
            if(toUpdate == null)
            {
                if(valueInfo != null)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Could not update row '" + valueInfo + "' because it's not present");
                    }
                }
            }
            else
            {
                getModel().updateValues(Collections.singleton(descriptor));
                ((RowlayoutSectionPanelModel)mod).updateRow(toUpdate);
                updateLabel(this.sectionPanelModel);
            }
        }
    }


    public void updateEditorRequest(TypedObject typedObject, PropertyDescriptor descriptor)
    {
        if(typedObject != null)
        {
            if(getModel().getCurrentObject().equals(typedObject))
            {
                updateRowForDescriptor(descriptor);
            }
            else
            {
                SectionPanelModel mod = getSectionPanelModel();
                if(mod instanceof RowlayoutSectionPanelModel)
                {
                    Set<SectionRow> allRows = ((RowlayoutSectionPanelModel)mod).getAllRows();
                    List<SectionRow> toUpdateList = new ArrayList<>();
                    Set<PropertyDescriptor> toUpdatePropList = new HashSet<>();
                    for(SectionRow sectionRow : allRows)
                    {
                        if(sectionRow instanceof EditorPropertyRow)
                        {
                            EditorRowConfiguration rowConf = ((EditorPropertyRow)sectionRow).getRowConfiguration();
                            ObjectTemplate rowObjTemplate = TypeTools.getValueTypeAsObjectTemplate(rowConf
                                            .getPropertyDescriptor(), UISessionUtils.getCurrentSession().getTypeService());
                            if(rowObjTemplate != null && rowObjTemplate.getBaseType() != null)
                            {
                                if(rowObjTemplate.getBaseType().isAssignableFrom((ObjectType)typedObject.getType()))
                                {
                                    toUpdateList.add(sectionRow);
                                    toUpdatePropList.add(rowConf.getPropertyDescriptor());
                                }
                            }
                        }
                    }
                    if(!toUpdateList.isEmpty())
                    {
                        getModel().updateValues(toUpdatePropList);
                        for(SectionRow toUpdateRow : toUpdateList)
                        {
                            ((RowlayoutSectionPanelModel)mod).updateRow(toUpdateRow);
                        }
                        updateLabel(this.sectionPanelModel);
                    }
                }
            }
        }
        else if(getModel().getCurrentObject() == null)
        {
            updateRowForDescriptor(descriptor);
        }
        else
        {
            SectionPanelModel mod = getSectionPanelModel();
            if(mod instanceof RowlayoutSectionPanelModel)
            {
                getModel().updateAllValues();
                ((RowlayoutSectionPanelModel)mod).update();
                updateLabel(this.sectionPanelModel);
            }
        }
        updateRegisteredSections((typedObject == null) ? null : (ObjectType)typedObject.getType(), descriptor);
    }


    protected void clearSectionUpdateRegistry()
    {
        this.sectionUpdateRegistry.clear();
    }


    protected void updateRegisteredSections(ObjectType type, PropertyDescriptor propertyDescriptor)
    {
        if(getSectionPanelModel() instanceof DefaultEditorSectionPanelModel)
        {
            DefaultEditorSectionPanelModel sectionPanelModel = (DefaultEditorSectionPanelModel)getSectionPanelModel();
            Set<Section> set = new HashSet<>();
            if(type != null)
            {
                Set<Section> sections = this.sectionUpdateRegistry.get(type);
                if(sections != null)
                {
                    set.addAll(sections);
                }
            }
            if(propertyDescriptor != null)
            {
                Set<Section> sections = this.sectionUpdateRegistry.get(propertyDescriptor);
                if(sections != null)
                {
                    set.addAll(sections);
                }
            }
            for(Section section : set)
            {
                sectionPanelModel.sectionUpdated(section);
            }
        }
    }


    public void registerSectionUpdateHandler(Set<PropertyDescriptor> propertyDescriptors, Set<ObjectType> types, Section section)
    {
        if(propertyDescriptors != null)
        {
            for(PropertyDescriptor propertyDescriptor : propertyDescriptors)
            {
                Set<Section> set = this.sectionUpdateRegistry.get(propertyDescriptor);
                if(set == null)
                {
                    set = new HashSet<>();
                    this.sectionUpdateRegistry.put(propertyDescriptor, set);
                }
                set.add(section);
            }
        }
        if(types != null)
        {
            for(ObjectType type : types)
            {
                Set<Section> set = this.sectionUpdateRegistry.get(type);
                if(set == null)
                {
                    set = new HashSet<>();
                    this.sectionUpdateRegistry.put(type, set);
                }
                set.add(section);
            }
        }
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    protected CockpitValidationService getValidationService()
    {
        if(this.validationService == null)
        {
            this.validationService = (CockpitValidationService)SpringUtil.getBean("cockpitValidationService");
        }
        return this.validationService;
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
