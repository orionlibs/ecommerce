package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.CollectionEditorModel;
import de.hybris.platform.cockpit.model.referenceeditor.UIReferenceCollectionEditor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;

@Deprecated
public class DefaultMediaReferenceCollectionUIEditor extends AbstractReferenceUIEditor
{
    private ObjectType rootType;
    private ObjectType rootSearchType;
    private String mimeType;
    private TypedObject sourceItem = null;
    private final DefaultMediaReferenceCollectionEditorModel model;
    private MediaReferenceCollectionEditor collectionEditor;
    private ReferenceCollectionEditorController collectionController;


    public DefaultMediaReferenceCollectionUIEditor()
    {
        this(null);
    }


    public DefaultMediaReferenceCollectionUIEditor(ObjectType rootType)
    {
        this.model = new DefaultMediaReferenceCollectionEditorModel(rootType);
    }


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Object additionalListenerParam = parameters.get(AdditionalReferenceEditorListener.class.getName());
        AdditionalReferenceEditorListener additionalListener = null;
        if(additionalListenerParam instanceof AdditionalReferenceEditorListener)
        {
            additionalListener = (AdditionalReferenceEditorListener)additionalListenerParam;
        }
        this.model.setParameters(parameters);
        this.collectionEditor = getCollectionEditor(listener, additionalListener);
        this.collectionEditor.setAllowCreate(isAllowCreate());
        Object createContext = parameters.get("createContext");
        if(createContext instanceof CreateContext)
        {
            this.collectionEditor.setCreateContext((CreateContext)createContext);
        }
        this.collectionEditor.setDisabled(!isEditable());
        this.collectionEditor.setParameters(parameters);
        if(initialValue != null)
        {
            if(initialValue instanceof Collection)
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
        else
        {
            this.model.setCollectionItems(Collections.EMPTY_LIST);
        }
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            String id = "MediaReferenceSelector_";
            String attQual = (String)parameters.get("attributeQualifier");
            if(attQual != null)
            {
                attQual = attQual.replaceAll("\\W", "");
                id = id + id;
            }
            UITools.applyTestID((Component)this.collectionEditor, id);
        }
        if(this.collectionController != null)
        {
            this.collectionController.unregisterListeners();
        }
        this.collectionController = new ReferenceCollectionEditorController((UIReferenceCollectionEditor)this.collectionEditor, (DefaultReferenceCollectionEditorModel)this.model, listener, this.rootType);
        this.collectionController.initialize();
        this.collectionEditor.setModel((CollectionEditorModel)this.model);
        AbstractUIEditor.CancelButtonContainer cancelButtonContainer = new AbstractUIEditor.CancelButtonContainer((AbstractUIEditor)this, listener, (AbstractUIEditor.CancelListener)new Object(this));
        cancelButtonContainer.setSclass("referenceMediaSelectorContainer");
        this.collectionEditor.addEventCollectionEditorListener("onFocus", (EventListener)new Object(this, cancelButtonContainer));
        this.collectionEditor.addEventCollectionEditorListener("onClose", (EventListener)new Object(this, cancelButtonContainer));
        cancelButtonContainer.setContent((Component)this.collectionEditor);
        return (HtmlBasedComponent)cancelButtonContainer;
    }


    public String getMimeType()
    {
        return this.mimeType;
    }


    public ObjectType getRootSearchType()
    {
        return this.model.getRootSearchType();
    }


    public ObjectType getRootType()
    {
        return this.model.getRootType();
    }


    public TypedObject getSourceItem()
    {
        return this.sourceItem;
    }


    public Object getValue()
    {
        return this.model.getCollectionItems();
    }


    public boolean isInline()
    {
        return true;
    }


    public void setFocus(HtmlBasedComponent rootEditorComponent, boolean selectAll)
    {
    }


    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
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


    public void setRootType(ObjectType rootType)
    {
        if((this.rootType == null && rootType != null) || (this.rootType != null && !this.rootType.equals(rootType)))
        {
            this.rootType = rootType;
            this.model.setRootType(rootType);
        }
    }


    public void setSourceItem(TypedObject sourceItem)
    {
        this.sourceItem = sourceItem;
    }


    public void setValue(Object value)
    {
        this.model.setCollectionItems((List)value);
    }


    protected DefaultReferenceCollectionEditorModel getModel()
    {
        return (DefaultReferenceCollectionEditorModel)this.model;
    }


    protected MediaReferenceCollectionEditor getCollectionEditor(EditorListener editorListener, AdditionalReferenceEditorListener additionalListener)
    {
        return new MediaReferenceCollectionEditor(editorListener, additionalListener);
    }
}
