package de.hybris.platform.cockpit.model.referenceeditor.collection;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.collection.controller.CollectionSelectorController;
import de.hybris.platform.cockpit.model.referenceeditor.collection.model.CollectionEditorModel;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleReferenceSelector;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.simple.UISimpleReferenceSelector;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.DesktopRemovalAwareComponent;
import de.hybris.platform.cockpit.util.UITools;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Center;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Space;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;

public class CollectionEditor extends AbstractCollectionEditor implements DesktopRemovalAwareComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(CollectionEditor.class);
    protected static final String MIN_COLLECTION_EDITOR_ROWS = "editor.collection_editor_min_rows";
    protected static final String MAX_COLLECTION_EDITOR_ROWS = "editor.collection_editor_max_rows";
    private static final String VERTICAL = "vertical";
    private static final String REFERENCE_EDITOR_BTN_SCLASS = "referenceEditorButton";
    protected static final String DROP_DRAG_ID = "collectionItem";
    protected static final String WIDTH = "width";
    protected static final String HEIGHT = "height";
    protected static final String _100PERCENT = "100%";
    private transient Listbox collectionItems;
    private transient Toolbarbutton showMoreButton;
    protected transient CollectionSelectorController selectorController = null;
    protected transient SimpleReferenceSelector simpleReferenceSelector;
    protected transient Div defaultEmptyLabelContainer;
    protected final EditorListener editorListener;
    protected AdditionalReferenceEditorListener additionalListener;
    private String imageHeight;
    private boolean expanded;
    private boolean initialized;
    private boolean disabled;
    private Boolean allowCreate = Boolean.TRUE;
    private CreateContext createContext = null;
    private Map<String, ? extends Object> parameters = new HashMap<>();
    private CollectionEditorModel model;


    public CollectionEditor(EditorListener editorListener, AdditionalReferenceEditorListener additionalListener)
    {
        this.editorListener = editorListener;
        this.additionalListener = additionalListener;
    }


    public boolean update()
    {
        boolean success;
        if(this.initialized)
        {
            updateCollectionItems();
            success = true;
        }
        else
        {
            success = initialize();
        }
        return success;
    }


    public void updateCollectionItems()
    {
        loadDefaultEmptyLabel();
        this.showMoreButton.setVisible((this.model.getCollectionItems().size() > getMinRowsNumber()));
        this.collectionItems.setModel((ListModel)new SimpleListModel(this.model.getCollectionItems()));
        trimToLimit();
    }


    private void trimToLimit()
    {
        int size = this.model.getCollectionItems().size();
        this.collectionItems.setRows(Math.min(getMinRowsNumber(), size));
        if(size > getMinRowsNumber() && this.expanded)
        {
            this.collectionItems.setRows(Math.min(getMaxRowsNumber(), size));
        }
    }


    protected void loadDefaultEmptyLabel()
    {
        if(CollectionUtils.isEmpty(this.model.getCollectionItems()))
        {
            if(getParameters().get("defaultEmptyLabelKey") != null)
            {
                String localizedMeassage = Labels.getLabel(String.valueOf(getParameters().get("defaultEmptyLabelKey")));
                this.defaultEmptyLabelContainer.appendChild((Component)new Label(localizedMeassage));
            }
        }
        else
        {
            UITools.detachChildren((Component)this.defaultEmptyLabelContainer);
        }
    }


    public boolean initialize()
    {
        this.initialized = false;
        if(getModel() != null)
        {
            parseInitialParameters(this.parameters);
            setSclass("referenceCollectionEditor");
            Vbox verticalContainer = new Vbox();
            verticalContainer.setWidth("100%");
            verticalContainer.appendChild((Component)createVericalSpacer("3px"));
            verticalContainer.setSclass("collectionEditorContainer");
            Div listContainer = new Div();
            this.collectionItems = new Listbox();
            this.collectionItems.setDisabled(isDisabled());
            this.collectionItems.setSclass("collectionEditorItems");
            this.collectionItems.setOddRowSclass("oddRowRowSclass");
            this.collectionItems.setFixedLayout(false);
            this.defaultEmptyLabelContainer = new Div();
            this.defaultEmptyLabelContainer.setSclass("defaultEmptyLabelPanel");
            loadDefaultEmptyLabel();
            listContainer.appendChild((Component)this.defaultEmptyLabelContainer);
            this.collectionItems.setItemRenderer(createCollectionItemListRenderer());
            listContainer.appendChild((Component)this.collectionItems);
            this.showMoreButton = new Toolbarbutton(Labels.getLabel("collectionEditor.more"));
            this.showMoreButton.setVisible((this.model.getCollectionItems().size() > getMinRowsNumber()));
            this.showMoreButton.setSclass("referenceEditorButton");
            this.showMoreButton.addEventListener("onClick", (EventListener)new Object(this));
            this.collectionItems.setRows(Math.min(this.model.getCollectionItems().size(), getMinRowsNumber()));
            this.collectionItems.setModel((ListModel)new SimpleListModel(this.model.getCollectionItems()));
            Center center = new Center();
            center.appendChild((Component)this.showMoreButton);
            listContainer.appendChild((Component)center);
            if(isDisabled() && this.model.getCollectionItems().isEmpty())
            {
                listContainer.appendChild((Component)new Label(Labels.getLabel("collectionEditor.novalue")));
            }
            verticalContainer.appendChild((Component)listContainer);
            verticalContainer.appendChild((Component)createVericalSpacer("3px"));
            Hbox selectorContainer = new Hbox();
            selectorContainer.setAlign("center");
            selectorContainer.setSclass("referenceSelectorContainer");
            selectorContainer.setWidth("100%");
            this.simpleReferenceSelector = initializeReferenceSelector();
            this.simpleReferenceSelector.setDisabled(isDisabled());
            selectorContainer.appendChild((Component)this.simpleReferenceSelector);
            verticalContainer.appendChild((Component)selectorContainer);
            verticalContainer.appendChild((Component)createVericalSpacer("3px"));
            appendChild((Component)verticalContainer);
            this.initialized = true;
        }
        return this.initialized;
    }


    public void setModel(CollectionEditorModel model)
    {
        if(this.model != model)
        {
            this.model = model;
            if(this.model != null)
            {
                initialize();
            }
        }
    }


    public CollectionEditorModel getModel()
    {
        return this.model;
    }


    public void desktopRemoved(Desktop desktop)
    {
        cleanup();
    }


    public void detach()
    {
        super.detach();
        cleanup();
    }


    public void setParent(Component parent)
    {
        super.setParent(parent);
        if(parent == null)
        {
            cleanup();
        }
    }


    public void updateRootSearchTypeChanged()
    {
    }


    public void updateRootTypeChanged()
    {
    }


    protected void doCollectionItemDoubleClicked(TypedObject item)
    {
        if(this.simpleReferenceSelector != null)
        {
            this.simpleReferenceSelector.fireOpenReferencedItem(item);
        }
    }


    protected void fireAbortAndCloseAdvancedMode()
    {
        if(this.simpleReferenceSelector != null)
        {
            this.simpleReferenceSelector.fireAbortAndCloseAdvancedMode();
        }
    }


    protected void doCollectionItemEntered()
    {
        if(this.simpleReferenceSelector != null)
        {
            this.simpleReferenceSelector.fireSaveActualSelected();
        }
    }


    protected ListitemRenderer createCollectionItemListRenderer()
    {
        return (ListitemRenderer)new Object(this);
    }


    public void setAutocompletionSearchType(ObjectType autocompletionSearchType)
    {
        this.model.getSimpleReferenceSelectorModel().setAutocompleteSearchType(autocompletionSearchType);
    }


    protected void parseInitialParameters(Map<String, ? extends Object> parameters)
    {
        Object autocompletionSearchTypeObject = parameters.get("autocompletionSearchType");
        if(autocompletionSearchTypeObject instanceof String && !((String)autocompletionSearchTypeObject).isEmpty())
        {
            TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
            try
            {
                ObjectType type = typeService.getObjectType(((String)autocompletionSearchTypeObject).trim());
                setAutocompletionSearchType(type);
            }
            catch(IllegalArgumentException e)
            {
                LOG.debug(e.getMessage(), e);
            }
        }
    }


    protected SimpleReferenceSelector initializeReferenceSelector()
    {
        Object object1 = new Object(this);
        object1.setAllowcreate(isAllowCreate());
        object1.setCreateContext(this.createContext);
        object1.setWidth("100%");
        object1.setShowEditButton(false);
        object1.setDisabled(isDisabled());
        Object object = this.parameters.get("allowAutocompletion");
        if(object instanceof String)
        {
            object1.setAutocompletionAllowed(BooleanUtils.toBoolean((String)object));
        }
        object1.setModel((SimpleReferenceSelectorModel)getModel().getSimpleReferenceSelectorModel());
        this.selectorController = new CollectionSelectorController(getModel().getSimpleReferenceSelectorModel(), getModel(), (UISimpleReferenceSelector)object1, this.editorListener, this.additionalListener);
        this.selectorController.initialize();
        return (SimpleReferenceSelector)object1;
    }


    public void doSimpleReferenceSelectorCollaped()
    {
        if(this.simpleReferenceSelector != null)
        {
            this.simpleReferenceSelector.fireSaveActualSelected();
        }
    }


    public void fireCancel()
    {
        if(this.simpleReferenceSelector != null)
        {
            this.simpleReferenceSelector.fireCancel();
        }
    }


    public void addEventCollectionEditorListener(String event, EventListener listener)
    {
        if(this.simpleReferenceSelector != null)
        {
            this.simpleReferenceSelector.addEventSelectorListener(event, listener);
        }
    }


    private Space createVericalSpacer(String verticalSpace)
    {
        Space verticalSpacer = new Space();
        verticalSpacer.setOrient("vertical");
        verticalSpacer.setHeight(verticalSpace);
        return verticalSpacer;
    }


    private void cleanup()
    {
        if(this.selectorController != null)
        {
            this.selectorController.unregisterListeners();
        }
    }


    public boolean isDisabled()
    {
        return this.disabled;
    }


    public void setDisabled(boolean disabled)
    {
        if(this.disabled != disabled)
        {
            this.disabled = disabled;
        }
    }


    public void setCreateContext(CreateContext createContext)
    {
        this.createContext = createContext;
    }


    public CreateContext getCreateContext()
    {
        return this.createContext;
    }


    public Map<String, ? extends Object> getParameters()
    {
        return this.parameters;
    }


    public void setParameters(Map<String, ? extends Object> parameters)
    {
        this.parameters = parameters;
    }


    public Boolean isAllowCreate()
    {
        return this.allowCreate;
    }


    public void setAllowCreate(Boolean allowCreate)
    {
        this.allowCreate = allowCreate;
    }


    protected int getMinRowsNumber()
    {
        return Integer.parseInt(UITools.getCockpitParameter("editor.collection_editor_min_rows", Executions.getCurrent()));
    }


    protected int getMaxRowsNumber()
    {
        return Integer.parseInt(UITools.getCockpitParameter("editor.collection_editor_max_rows", Executions.getCurrent()));
    }


    public String getImageHeight()
    {
        return this.imageHeight;
    }


    public void setImageHeight(String imageHeight)
    {
        this.imageHeight = imageHeight;
    }
}
