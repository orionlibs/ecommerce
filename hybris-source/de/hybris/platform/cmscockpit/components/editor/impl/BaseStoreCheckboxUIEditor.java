package de.hybris.platform.cmscockpit.components.editor.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.CollectionEditorModel;
import de.hybris.platform.cockpit.model.referenceeditor.UIReferenceCollectionEditor;
import de.hybris.platform.cockpit.model.referenceeditor.impl.AbstractReferenceUIEditor;
import de.hybris.platform.cockpit.model.referenceeditor.impl.DefaultReferenceCollectionEditorModel;
import de.hybris.platform.cockpit.model.referenceeditor.impl.ReferenceCollectionEditorController;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.store.services.BaseStoreService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;

public class BaseStoreCheckboxUIEditor extends AbstractReferenceUIEditor
{
    private ObjectType rootType;
    private ObjectType rootSearchType;
    private final DefaultReferenceCollectionEditorModel model;
    private BaseStoreCheckboxComponent checkBoxComponent;
    private ReferenceCollectionEditorController collectionController;


    public BaseStoreCheckboxUIEditor()
    {
        this(null);
    }


    public BaseStoreCheckboxUIEditor(ObjectType rootType)
    {
        this.model = new DefaultReferenceCollectionEditorModel(rootType);
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
        Object additionalListenerParam = parameters.get(AdditionalReferenceEditorListener.class.getName());
        AdditionalReferenceEditorListener additionalListener = null;
        if(additionalListenerParam instanceof AdditionalReferenceEditorListener)
        {
            additionalListener = (AdditionalReferenceEditorListener)additionalListenerParam;
        }
        this.checkBoxComponent = (BaseStoreCheckboxComponent)new Object(this, listener, additionalListener, listener);
        this.checkBoxComponent.setDisabled(!isEditable());
        this.checkBoxComponent.setAllowCreate(isAllowCreate());
        Object createContext = parameters.get("createContext");
        if(createContext instanceof CreateContext)
        {
            this.checkBoxComponent.setCreateContext((CreateContext)createContext);
        }
        this.model.setParameters(parameters);
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
                        this.checkBoxComponent.setSelectedElements(new ArrayList((Collection)initialValue));
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
        BaseStoreService baseStoreService = (BaseStoreService)SpringUtil.getBean("baseStoreService");
        this.model.setCollectionItems(new ArrayList(UISessionUtils.getCurrentSession().getTypeService()
                        .wrapItems(baseStoreService.getAllBaseStores())));
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            String id = "ReferenceSelector_";
            String attQual = (String)parameters.get("attributeQualifier");
            if(attQual != null)
            {
                attQual = attQual.replaceAll("\\W", "");
                id = id + id;
            }
            UITools.applyTestID((Component)this.checkBoxComponent, id);
        }
        if(this.collectionController != null)
        {
            this.collectionController.unregisterListeners();
        }
        this.collectionController = new ReferenceCollectionEditorController((UIReferenceCollectionEditor)this.checkBoxComponent, this.model, listener, this.rootType);
        this.collectionController.initialize();
        this.checkBoxComponent.setModel((CollectionEditorModel)this.model);
        AbstractUIEditor.CancelButtonContainer cancelButtonContainer = new AbstractUIEditor.CancelButtonContainer((AbstractUIEditor)this, listener, (AbstractUIEditor.CancelListener)new Object(this));
        this.checkBoxComponent.addEventCollectionEditorListener("onFocus", (EventListener)new Object(this, cancelButtonContainer));
        this.checkBoxComponent.addEventCollectionEditorListener("onClose", (EventListener)new Object(this, cancelButtonContainer));
        cancelButtonContainer.setContent((Component)this.checkBoxComponent);
        return (HtmlBasedComponent)cancelButtonContainer;
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
        this.model.setCollectionItems((List)value);
    }


    public void setFocus(HtmlBasedComponent rootEditorComponent, boolean selectAll)
    {
    }


    public ObjectType getRootSearchType()
    {
        return this.model.getRootSearchType();
    }


    public ObjectType getRootType()
    {
        return this.model.getRootType();
    }


    protected DefaultReferenceCollectionEditorModel getModel()
    {
        return this.model;
    }
}
