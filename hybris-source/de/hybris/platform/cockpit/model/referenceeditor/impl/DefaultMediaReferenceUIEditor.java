package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.ReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.UIReferenceSelector;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;

public class DefaultMediaReferenceUIEditor extends AbstractReferenceUIEditor
{
    private ObjectType rootType;
    private ObjectType rootSearchType;
    private DefaultMediaReferenceSelectorModel model = null;
    private MediaReferenceSelector selector;
    private DefaultReferenceSelectorController selectorController;


    public DefaultMediaReferenceUIEditor()
    {
        this(null);
    }


    public DefaultMediaReferenceUIEditor(ObjectType rootType)
    {
        this.model = new DefaultMediaReferenceSelectorModel(rootType);
        this.model.setMultiple(Boolean.FALSE.booleanValue());
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
        this.selector = new MediaReferenceSelector();
        Object imgHeight = parameters.get("imageHeight");
        if(imgHeight instanceof String)
        {
            this.selector.setImageHeight((String)imgHeight);
        }
        imgHeight = parameters.get("imageHeightInList");
        if(imgHeight instanceof String)
        {
            this.selector.setImageHeightInAutocompleteList((String)imgHeight);
        }
        Object createContext = parameters.get("createContext");
        if(createContext instanceof CreateContext)
        {
            this.selector.setCreateContext((CreateContext)createContext);
        }
        this.selector.setAllowcreate(isAllowCreate());
        this.selector.setDisabled(!isEditable());
        this.selector.setParameters(parameters);
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
                        this.model.setItems(new ArrayList((Collection)initialValue));
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
                this.model.setItems(Collections.singletonList(initialValue));
            }
            else
            {
                throw new IllegalArgumentException("Initial value '" + initialValue + "' not a typed object.");
            }
        }
        else
        {
            this.model.setItems(Collections.EMPTY_LIST);
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
            UITools.applyTestID((Component)this.selector, id);
        }
        if(this.selectorController != null)
        {
            this.selectorController.unregisterListeners();
        }
        Object additionalListenerParam = parameters.get(AdditionalReferenceEditorListener.class.getName());
        AdditionalReferenceEditorListener additionalListener = null;
        if(additionalListenerParam instanceof AdditionalReferenceEditorListener)
        {
            additionalListener = (AdditionalReferenceEditorListener)additionalListenerParam;
        }
        this.selectorController = (DefaultReferenceSelectorController)new DefaultMediaReferenceSelectorController((DefaultReferenceSelectorModel)this.model, (UIReferenceSelector)this.selector, listener, additionalListener);
        this.selectorController.initialize();
        this.selector.setModel((ReferenceSelectorModel)this.model);
        AbstractUIEditor.CancelButtonContainer cancelButtonContainer = new AbstractUIEditor.CancelButtonContainer((AbstractUIEditor)this, listener, (AbstractUIEditor.CancelListener)new Object(this));
        this.selector.addEventSelectorListener("onFocus", (EventListener)new Object(this, cancelButtonContainer));
        this.selector.addEventSelectorListener("onClose", (EventListener)new Object(this, cancelButtonContainer));
        cancelButtonContainer.setContent((Component)this.selector);
        return (HtmlBasedComponent)cancelButtonContainer;
    }


    public Object getValue()
    {
        return this.model.getValue();
    }


    public boolean isInline()
    {
        return true;
    }


    public void setValue(Object value)
    {
        if(value instanceof Collection)
        {
            this.model.setItems(new ArrayList((Collection)value));
        }
        else if(value != null)
        {
            this.model.setItems(Collections.singletonList(value));
        }
        else
        {
            this.model.setItems(null);
        }
    }


    public void setFocus(HtmlBasedComponent rootEditorComponent, boolean selectAll)
    {
        MediaReferenceSelector element = (MediaReferenceSelector)((AbstractUIEditor.CancelButtonContainer)rootEditorComponent).getContent();
        element.setFocus(Boolean.TRUE.booleanValue());
        if(this.initialInputString != null)
        {
            this.selector.setInitString(this.initialInputString);
        }
    }


    public ObjectType getRootSearchType()
    {
        return this.model.getRootSearchType();
    }


    public ObjectType getRootType()
    {
        return this.model.getRootType();
    }


    protected DefaultMediaReferenceSelectorModel getModel()
    {
        return this.model;
    }
}
