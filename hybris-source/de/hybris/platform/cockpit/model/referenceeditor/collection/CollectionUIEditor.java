package de.hybris.platform.cockpit.model.referenceeditor.collection;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.collection.controller.CollectionUIEditorController;
import de.hybris.platform.cockpit.model.referenceeditor.collection.model.CollectionEditorModel;
import de.hybris.platform.cockpit.model.referenceeditor.collection.model.DefaultCollectionEditorModel;
import de.hybris.platform.cockpit.model.referenceeditor.impl.AbstractReferenceUIEditor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;

public class CollectionUIEditor extends AbstractReferenceUIEditor
{
    private ObjectType rootType;
    private ObjectType rootSearchType;
    protected DefaultCollectionEditorModel model;
    protected CollectionEditor collectionEditor;
    protected CollectionUIEditorController collectionController;


    public CollectionUIEditor()
    {
        this(null);
    }


    public CollectionUIEditor(ObjectType rootType)
    {
        this.model = new DefaultCollectionEditorModel(rootType);
    }


    public void setRootType(ObjectType rootType)
    {
        if((this.rootType == null && rootType != null) || (this.rootType != null && !this.rootType.equals(rootType)))
        {
            this.rootType = rootType;
            this.model.setRootType(rootType);
        }
    }


    public void setRootSearchType(ObjectType rootSearchType)
    {
        if((this.rootSearchType == null && rootSearchType != null) || (this.rootSearchType != null &&
                        !this.rootType.equals(rootSearchType)))
        {
            this.rootSearchType = rootSearchType;
        }
        this.model.setRootSearchType(this.rootSearchType);
    }


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Object object1 = new Object(this, listener);
        Object additionalListenerParam = parameters.get(AdditionalReferenceEditorListener.class.getName());
        AdditionalReferenceEditorListener additionalListener = null;
        if(additionalListenerParam instanceof AdditionalReferenceEditorListener)
        {
            additionalListener = (AdditionalReferenceEditorListener)additionalListenerParam;
        }
        Integer maxAC = findMaxAutocompleteSearchResults(parameters);
        if(maxAC != null && maxAC.intValue() > 0)
        {
            this.model.getSimpleReferenceSelectorModel().setMaxAutoCompleteResultSize(maxAC.intValue());
        }
        this.model.setParameters(parameters);
        this.collectionEditor = createCollectionEditor((EditorListener)object1, additionalListener);
        Object createContext = parameters.get("createContext");
        if(createContext instanceof CreateContext)
        {
            this.collectionEditor.setCreateContext((CreateContext)createContext);
        }
        this.collectionEditor.setDisabled(!isEditable());
        this.collectionEditor.setParameters(parameters);
        Optional<Boolean> allowCreateConfiguredByParam = getBooleanParameter("allowCreate", parameters);
        if(allowCreateConfiguredByParam.isPresent())
        {
            this.collectionEditor.setAllowCreate(allowCreateConfiguredByParam.get());
        }
        else
        {
            this.collectionEditor.setAllowCreate(isAllowCreate());
        }
        assignedPassedValue(initialValue);
        applyTestUserID(parameters);
        if(this.collectionController != null)
        {
            this.collectionController.unregisterListeners();
        }
        this.collectionController = new CollectionUIEditorController((AbstractCollectionEditor)this.collectionEditor, this.model, (EditorListener)object1, this.rootType);
        this.collectionController.initialize();
        this.collectionEditor.setModel((CollectionEditorModel)this.model);
        AbstractUIEditor.CancelButtonContainer cancelButtonContainer = new AbstractUIEditor.CancelButtonContainer((AbstractUIEditor)this, (EditorListener)object1, (AbstractUIEditor.CancelListener)new Object(this));
        registerEventListeners(cancelButtonContainer);
        cancelButtonContainer.setSclass("collectionReferenceEditor");
        cancelButtonContainer.setContent((Component)this.collectionEditor);
        return (HtmlBasedComponent)cancelButtonContainer;
    }


    protected void registerEventListeners(AbstractUIEditor.CancelButtonContainer cancelButtonContainer)
    {
        this.collectionEditor.addEventCollectionEditorListener("onEditStart", (EventListener)new Object(this, cancelButtonContainer));
        this.collectionEditor.addEventCollectionEditorListener("onFinishEdit", (EventListener)new Object(this, cancelButtonContainer));
        this.collectionEditor.addEventCollectionEditorListener("onBlur", (EventListener)new Object(this));
        this.collectionEditor.addEventCollectionEditorListener("onOK", (EventListener)new Object(this));
    }


    public Object getValue()
    {
        return this.model.getCollectionItems();
    }


    public boolean isInline()
    {
        return true;
    }


    public void setValue(Object value)
    {
        List<Object> currentList = new ArrayList();
        if(value instanceof Collection)
        {
            currentList.addAll((Collection)value);
        }
        this.model.setCollectionItems(currentList);
    }


    public ObjectType getRootSearchType()
    {
        return this.model.getRootSearchType();
    }


    public ObjectType getRootType()
    {
        return this.model.getRootType();
    }


    protected DefaultCollectionEditorModel getModel()
    {
        return this.model;
    }


    private void applyTestUserID(Map<String, ? extends Object> parameters)
    {
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            String id = "ReferenceSelector_";
            String attQual = (String)parameters.get("attributeQualifier");
            if(attQual != null)
            {
                attQual = attQual.replaceAll("\\W", "");
                id = id + id;
            }
            UITools.applyTestID((Component)this.collectionEditor, id);
        }
    }


    protected void assignedPassedValue(Object initialValue)
    {
        if(initialValue == null)
        {
            this.model.setCollectionItems(Collections.EMPTY_LIST);
        }
        else if(initialValue instanceof Collection)
        {
            if(!((Collection)initialValue).isEmpty())
            {
                Object firstItem = ((Collection)initialValue).iterator().next();
                if(firstItem instanceof TypedObject &&
                                UISessionUtils.getCurrentSession().getTypeService().getObjectType(this.model.getRootType().getCode())
                                                .isAssignableFrom((ObjectType)((TypedObject)firstItem).getType()))
                {
                    this.model.setCollectionItems(new ArrayList((Collection)initialValue));
                }
                else
                {
                    throw new IllegalArgumentException("Initial value '" + initialValue + "' can not be assigned to root type '" + this.model
                                    .getRootType() + "'");
                }
            }
        }
        else if(initialValue instanceof TypedObject)
        {
            this.model.setCollectionItems(Collections.singletonList(initialValue));
        }
        else
        {
            throw new IllegalArgumentException("Initial value '" + initialValue + "' not a typed object.");
        }
    }


    public CollectionEditor createCollectionEditor(EditorListener listener, AdditionalReferenceEditorListener additionalListener)
    {
        return new CollectionEditor(listener, additionalListener);
    }
}
