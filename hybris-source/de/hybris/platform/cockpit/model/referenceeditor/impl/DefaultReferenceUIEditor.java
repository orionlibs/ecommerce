package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.ReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.UIReferenceSelector;
import de.hybris.platform.cockpit.services.meta.TypeService;
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

public class DefaultReferenceUIEditor extends AbstractReferenceUIEditor
{
    public static final String AUTOCOMPLETION_SEARCHTYPE = "autocompletionSearchType";
    private ObjectType rootType;
    private ObjectType rootSearchType;
    private DefaultReferenceSelectorModel model = null;
    private ReferenceSelector selector;
    private DefaultReferenceSelectorController selectorController;


    public DefaultReferenceUIEditor()
    {
        this(null);
    }


    public DefaultReferenceUIEditor(ObjectType rootType)
    {
        this.model = new DefaultReferenceSelectorModel(rootType);
        this.model.setMultiple(false);
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


    public void setAutocompletionSearchType(ObjectType autocompletionSearchType)
    {
        this.model.setAutocompleteSearchType(autocompletionSearchType);
    }


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        parseInitialParameters(parameters);
        Integer maxAC = findMaxAutocompleteSearchResults(parameters);
        if(maxAC != null && maxAC.intValue() > 0)
        {
            this.model.setMaxAutoCompleteResultSize(maxAC.intValue());
        }
        this.model.setParameters(parameters);
        this.selector = new ReferenceSelector();
        this.selector.setDisabled(!isEditable());
        Object createContext = parameters.get("createContext");
        if(createContext instanceof CreateContext)
        {
            this.selector.setCreateContext((CreateContext)createContext);
        }
        this.selector.setAllowcreate(isAllowCreate());
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
            String id = "ReferenceSelector_";
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
        this.selectorController = new DefaultReferenceSelectorController(this.model, (UIReferenceSelector)this.selector, listener, additionalListener);
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
        ((AbstractUIEditor.CancelButtonContainer)rootEditorComponent).showButton(Boolean.TRUE.booleanValue());
        ReferenceSelector element = (ReferenceSelector)((AbstractUIEditor.CancelButtonContainer)rootEditorComponent).getContent();
        if(this.initialInputString != null)
        {
            this.selector.setInitString(this.initialInputString);
        }
        element.setFocus(Boolean.TRUE.booleanValue());
    }


    public ObjectType getRootSearchType()
    {
        return this.model.getRootSearchType();
    }


    public ObjectType getRootType()
    {
        return this.model.getRootType();
    }


    protected void parseInitialParameters(Map<String, ? extends Object> parameters)
    {
        parseInitialInputString(parameters);
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
}
