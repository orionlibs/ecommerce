package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.AbstractReferenceCollectionEditor;
import de.hybris.platform.cockpit.model.referenceeditor.CollectionEditorModel;
import de.hybris.platform.cockpit.model.referenceeditor.ReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.UIReferenceSelector;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.DesktopRemovalAwareComponent;
import de.hybris.platform.cockpit.util.UITools;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Center;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
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

@Deprecated
public class ReferenceCollectionEditor extends AbstractReferenceCollectionEditor implements DesktopRemovalAwareComponent
{
    private static final Logger log = LoggerFactory.getLogger(ReferenceCollectionEditor.class);
    private static final int MAX_COLLECTION_ITEM = 10;
    private static final String VERTICAL = "vertical";
    protected static final String DROP_DRAG_ID = "collectionItem";
    protected static final String WIDTH = "width";
    protected static final String HEIGHT = "height";
    protected static final String _100PERCENT = "100%";
    private static final String REFERENCE_EDITOR_BTN_SCLASS = "referenceEditorButton";
    private transient Listbox collectionItems;
    private transient Toolbarbutton expandAll;
    protected transient DefaultReferenceSelectorController selectorController = null;
    protected transient ReferenceSelector referenceSelector;
    protected transient Div defaultEmptyLabelContainer;
    private Map<String, ? extends Object> parameters = new HashMap<>();
    private boolean expanded;
    private boolean initialized;
    private boolean disabled;
    private Boolean allowCreate = Boolean.TRUE;
    private CreateContext createContext = null;
    private CollectionEditorModel model;
    protected final EditorListener editorListener;
    protected AdditionalReferenceEditorListener additionalListener;


    public Boolean isAllowCreate()
    {
        return this.allowCreate;
    }


    public void setAllowCreate(Boolean allowCreate)
    {
        this.allowCreate = allowCreate;
    }


    public ReferenceCollectionEditor(EditorListener editorListener, AdditionalReferenceEditorListener additionalListener)
    {
        this.editorListener = editorListener;
        this.additionalListener = additionalListener;
    }


    public boolean update()
    {
        boolean success = false;
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
        this.expandAll.setVisible((this.model.getCollectionItems().size() > 10));
        this.collectionItems.setModel((ListModel)new SimpleListModel(this.model.getCollectionItems()));
        trimToLimit();
    }


    private void trimToLimit()
    {
        int size = this.model.getCollectionItems().size();
        this.collectionItems.setRows(size);
        if(size > 10 && !this.expanded)
        {
            this.collectionItems.setRows(10);
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
            verticalContainer.setSclass("referenceCollectionEditorContainer");
            Div listContainer = new Div();
            this.collectionItems = new Listbox();
            this.collectionItems.setDisabled(isDisabled());
            this.collectionItems.setSclass("referenceCollectioEditorItems");
            this.collectionItems.setOddRowSclass("oddRowRowSclass");
            this.collectionItems.setModel((ListModel)new SimpleListModel(this.model.getCollectionItems()));
            this.collectionItems.setFixedLayout(false);
            this.defaultEmptyLabelContainer = new Div();
            this.defaultEmptyLabelContainer.setSclass("defaultEmptyLabelPanel");
            loadDefaultEmptyLabel();
            listContainer.appendChild((Component)this.defaultEmptyLabelContainer);
            this.collectionItems.setItemRenderer(createCollectionItemListRenderer());
            listContainer.appendChild((Component)this.collectionItems);
            listContainer.appendChild((Component)createVericalSpacer("3px"));
            this.expandAll = new Toolbarbutton(Labels.getLabel("referenceCollectionEditor.expandall"));
            this.expandAll.setVisible((this.model.getCollectionItems().size() > 10));
            this.expandAll.setSclass("referenceEditorButton");
            this.expandAll.addEventListener("onClick", (EventListener)new Object(this));
            Center center = new Center();
            center.appendChild((Component)this.expandAll);
            listContainer.appendChild((Component)center);
            verticalContainer.appendChild((Component)listContainer);
            verticalContainer.appendChild((Component)createVericalSpacer("3px"));
            Hbox selectorContainer = new Hbox();
            selectorContainer.setAlign("center");
            selectorContainer.setSclass("referenceSelectorContainer");
            selectorContainer.setWidth("100%");
            this.referenceSelector = initializeReferenceSelector();
            this.referenceSelector.setDisabled(isDisabled());
            selectorContainer.appendChild((Component)this.referenceSelector);
            Toolbarbutton addSelected = new Toolbarbutton(Labels.getLabel("general.add"));
            addSelected.setSclass("referenceEditorButton");
            addSelected.addEventListener("onClick", (EventListener)new Object(this));
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
        if(this.referenceSelector != null)
        {
            this.referenceSelector.fireOpenReferencedItem(item);
        }
    }


    protected ListitemRenderer createCollectionItemListRenderer()
    {
        return (ListitemRenderer)new Object(this);
    }


    public void setAutocompletionSearchType(ObjectType autocompletionSearchType)
    {
        this.model.getReferenceSelectorModel().setAutocompleteSearchType(autocompletionSearchType);
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
            catch(IllegalArgumentException illegalArgumentException)
            {
            }
        }
    }


    protected ReferenceSelector initializeReferenceSelector()
    {
        ReferenceSelector4Collection referenceSelector4Collection = new ReferenceSelector4Collection(this);
        referenceSelector4Collection.setAllowcreate(isAllowCreate());
        referenceSelector4Collection.setCreateContext(this.createContext);
        referenceSelector4Collection.setWidth("100%");
        referenceSelector4Collection.setParameters(this.parameters);
        referenceSelector4Collection.setModel((ReferenceSelectorModel)getModel().getReferenceSelectorModel());
        this.selectorController = new DefaultReferenceSelectorController(getModel().getReferenceSelectorModel(), (UIReferenceSelector)referenceSelector4Collection, null, this.additionalListener);
        this.selectorController.initialize();
        return (ReferenceSelector)referenceSelector4Collection;
    }


    public void fireCancel()
    {
        if(this.referenceSelector != null)
        {
            this.referenceSelector.fireCancel();
        }
    }


    public void addEventCollectionEditorListener(String event, EventListener listener)
    {
        if(this.referenceSelector != null)
        {
            this.referenceSelector.addEventSelectorListener(event, listener);
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
}
