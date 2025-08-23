package de.hybris.platform.platformbackoffice.renderers;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.common.EditorBuilder;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPanel;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.util.CockpitGlobalEventPublisher;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.DefaultEditorAreaPanelRenderer;
import de.hybris.platform.commons.jalo.CommonsManager;
import de.hybris.platform.commons.jalo.Document;
import de.hybris.platform.commons.jalo.Format;
import de.hybris.platform.commons.model.DocumentModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Vlayout;

public class GenerateOutputDocumentPanelRenderer extends DefaultEditorAreaPanelRenderer
{
    public static final String MODEL_DATA_QUALIFIER = "currentObject";
    public static final String BUTTON_CREATE_DOCUMENT = "hmc.button.create.document";
    public static final String BUTTON_CREATE_DOCUMENT_TOOLTIP = "hmc.button.create.document.tooltip";
    public static final String FILTERED_DOCUMENTS = "filteredDocuments";
    public static final String EDITOR_CODE_REFERENCES_COM_HYBRIS_COCKPITNG_EDITOR_EXTENDEDMULTIREFERENCEEDITOR = "com.hybris.cockpitng.editor.extendedmultireferenceeditor";
    public static final String EDITOR_VALUE_TYPE_EXTENDED_MULTI_REFERENCE_COLLECTION_DOCUMENT = "ExtendedMultiReference-COLLECTION(Document)";
    public static final String I18N_HMC_TAB_DOCUMENTS_LABEL_DOCUMENTS = "hmc.tab.documents.label.documents";
    public static final String I18N_HMC_TAB_DOCUMENTS_LABEL_FORMATS = "hmc.tab.documents.label.formats";
    public static final String SCLASS_YW_GENERATE_OUT_DOC_DATA_ROW = "yw-generate-out-doc-data-row";
    public static final String SCLASS_YW_GENERATE_OUT_DOC_DATA_ROW_TOP_SELECTOR = "yw-generate-out-doc-data-row-top-selector";
    public static final String LIST_CONFIG_CONTEXT = "listConfigContext";
    public static final String COMPONENT_OUT_DOC_EDITOR_LIST = "outDocEditorList";
    private static final Logger LOG = LoggerFactory.getLogger(GenerateOutputDocumentPanelRenderer.class);
    private ObjectFacade objectFacade;
    private ModelService modelService;
    private CockpitProperties cockpitProperties;
    private CockpitGlobalEventPublisher eventPublisher;
    private NotificationService notificationService;


    public void render(Component parent, AbstractPanel abstractPanelConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        Div createRow = new Div();
        createRow.setSclass("yw-generate-out-doc-data-row-top-selector");
        UITools.addSClass((HtmlBasedComponent)parent, "yw-generate-out-doc-data-row");
        widgetInstanceManager.getModel().setValue("filteredDocuments", Collections.emptyList());
        Editor dynamicDocumentList = prepareDynamicDocumentList(widgetInstanceManager);
        Combobox formatCombobox = new Combobox();
        Button createDocumentButton = new Button(getLabel("hmc.button.create.document"));
        widgetInstanceManager.getModel().addObserver("currentObject", () -> handleModelDataChanged(widgetInstanceManager, formatCombobox, createDocumentButton));
        populateComboWithFormats(formatCombobox, object);
        formatCombobox.setReadonly(true);
        formatCombobox.addEventListener("onSelection", e -> handleFormatSelection(widgetInstanceManager, dynamicDocumentList, formatCombobox, createDocumentButton));
        createDocumentButton.setTooltiptext(getLabel("hmc.button.create.document.tooltip"));
        createDocumentButton.addEventListener("onClick", e -> executeDocumentCreation(widgetInstanceManager, formatCombobox, dynamicDocumentList));
        createDocumentButton.setDisabled((formatCombobox.isDisabled() || formatCombobox.getSelectedIndex() < 0));
        createRow.appendChild((Component)formatCombobox);
        createRow.appendChild((Component)createDocumentButton);
        Vlayout vlayout = new Vlayout();
        vlayout.appendChild((Component)new Label(getLabel("hmc.tab.documents.label.formats")));
        vlayout.appendChild((Component)createRow);
        vlayout.appendChild((Component)new Label(dynamicDocumentList.getEditorLabel()));
        vlayout.appendChild((Component)dynamicDocumentList);
        parent.appendChild((Component)vlayout);
    }


    protected void handleModelDataChanged(WidgetInstanceManager widgetInstanceManager, Combobox formatCombobox, Button createDocumentButton)
    {
        Object value = extractCurrentObject(widgetInstanceManager);
        boolean disabled = (value == null || this.objectFacade.isModified(value) || this.objectFacade.isNew(value));
        formatCombobox.setDisabled(disabled);
        createDocumentButton.setDisabled((disabled || formatCombobox.getSelectedIndex() < 0));
    }


    protected void handleFormatSelection(WidgetInstanceManager widgetInstanceManager, Editor dynamicDocumentList, Combobox formatCombobox, Button createDocumentButton)
    {
        boolean disabled = (formatCombobox.getSelectedIndex() < 0);
        createDocumentButton.setDisabled(disabled);
        List<?> documents = Collections.emptyList();
        if(!disabled)
        {
            Object selectedFormat = formatCombobox.getItemAtIndex(formatCombobox.getSelectedIndex()).getValue();
            Object currentObjectJalo = getModelService().getSource(extractCurrentObject(widgetInstanceManager));
            if(selectedFormat instanceof Format && currentObjectJalo instanceof Item)
            {
                Collection<Document> jaloDocuments = findDocumentsOfFormat((Format)selectedFormat, (Item)currentObjectJalo);
                documents = (List)jaloDocuments.stream().map(el -> getModelService().toModelLayer(el)).collect(Collectors.toList());
            }
        }
        widgetInstanceManager.getModel().setValue("filteredDocuments", documents);
        dynamicDocumentList.reload();
    }


    protected Object extractCurrentObject(WidgetInstanceManager widgetInstanceManager)
    {
        return widgetInstanceManager.getModel().getValue("currentObject", Object.class);
    }


    protected Collection<Document> findDocumentsOfFormat(Format value, Item jalo)
    {
        return CommonsManager.getInstance().getDocuments(jalo, value);
    }


    protected void executeDocumentCreation(WidgetInstanceManager widgetInstanceManager, Combobox formatCombobox, Editor dynamicDocumentList) throws JaloBusinessException
    {
        try
        {
            if(formatCombobox.getSelectedIndex() > -1)
            {
                Object selectedFormat = formatCombobox.getItemAtIndex(formatCombobox.getSelectedIndex()).getValue();
                if(selectedFormat instanceof Format)
                {
                    Item currentObjectJalo = (Item)getModelService().getSource(extractCurrentObject(widgetInstanceManager));
                    Document document = createDocumentOfFormat((Format)selectedFormat, currentObjectJalo);
                    List<DocumentModel> documents = (List<DocumentModel>)widgetInstanceManager.getModel().getValue("filteredDocuments", List.class);
                    List<DocumentModel> newDocuments = (documents != null) ? new ArrayList<>(documents) : new ArrayList<>();
                    newDocuments.add((DocumentModel)getModelService().toModelLayer(document));
                    widgetInstanceManager.getModel().setValue("filteredDocuments", newDocuments);
                    dynamicDocumentList.reload();
                    notifyObjectUpdated(currentObjectJalo);
                }
            }
        }
        catch(RuntimeException re)
        {
            LOG.warn(re.getMessage(), re);
            getNotificationService().notifyUser(widgetInstanceManager, "General", NotificationEvent.Level.FAILURE, new Object[] {re});
        }
    }


    protected Document createDocumentOfFormat(Format value, Item jalo) throws JaloBusinessException
    {
        return value.format(jalo);
    }


    protected Editor prepareDynamicDocumentList(WidgetInstanceManager widgetInstanceManager)
    {
        String label = getLabel("hmc.tab.documents.label.documents");
        return (new EditorBuilder(widgetInstanceManager))
                        .attach("filteredDocuments")
                        .addParameter("listConfigContext", "outDocEditorList")
                        .setLabel(label)
                        .setReadOnly(true)
                        .useEditor("com.hybris.cockpitng.editor.extendedmultireferenceeditor")
                        .setValueType("ExtendedMultiReference-COLLECTION(Document)")
                        .setValueCreationEnabled(false)
                        .build();
    }


    protected String getLabel(String i18nKey)
    {
        return Labels.getLabel(i18nKey);
    }


    protected void populateComboWithFormats(Combobox combobox, Object object)
    {
        if(!(object instanceof de.hybris.platform.core.model.ItemModel))
        {
            throw new IllegalArgumentException("Expected Item");
        }
        Item jalo = (Item)this.modelService.getSource(object);
        Collection<Format> formats = getFormatsForItem(jalo);
        combobox.setModel((ListModel)new ListModelList(formats));
        combobox.setItemRenderer((comboitem, o, i) -> {
            comboitem.setValue(o);
            comboitem.setLabel(getLabelService().getObjectLabel(o));
        });
    }


    protected Collection<Format> getFormatsForItem(Item jalo)
    {
        return CommonsManager.getInstance().getFormatsForItem(jalo);
    }


    protected void notifyObjectUpdated(Object object)
    {
        if(isCockpitEventNotificationEnabled())
        {
            getEventPublisher().publish("objectsUpdated", object, (Context)new DefaultContext());
        }
    }


    protected boolean isCockpitEventNotificationEnabled()
    {
        return BooleanUtils.isNotFalse(
                        BooleanUtils.toBooleanObject(this.cockpitProperties.getProperty("cockpitng.crud.cockpit.event.notification")));
    }


    public ObjectFacade getObjectFacade()
    {
        return this.objectFacade;
    }


    @Required
    public void setObjectFacade(ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public CockpitProperties getCockpitProperties()
    {
        return this.cockpitProperties;
    }


    @Required
    public void setCockpitProperties(CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    public CockpitGlobalEventPublisher getEventPublisher()
    {
        return this.eventPublisher;
    }


    @Required
    public void setEventPublisher(CockpitGlobalEventPublisher eventPublisher)
    {
        this.eventPublisher = eventPublisher;
    }


    public static String getModelDataQualifier()
    {
        return "currentObject";
    }


    public NotificationService getNotificationService()
    {
        return this.notificationService;
    }


    @Required
    public void setNotificationService(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }
}
