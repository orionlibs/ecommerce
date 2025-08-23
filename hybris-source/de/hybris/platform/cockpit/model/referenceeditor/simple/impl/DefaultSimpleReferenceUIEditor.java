package de.hybris.platform.cockpit.model.referenceeditor.simple.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.impl.AbstractReferenceUIEditor;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleReferenceSelector;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.simple.UISimpleReferenceSelector;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang.BooleanUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Label;

public class DefaultSimpleReferenceUIEditor extends AbstractReferenceUIEditor
{
    public static final String AUTOCOMPLETION_SEARCHTYPE = "autocompletionSearchType";
    public static final String PREDEFINED_PROPERTY_VALUES = "predefinedPropertyValues";
    public static final String DISABLE_ON_BLUR_PARAMETER = "disableOnBlur";
    private ObjectType rootType;
    private ObjectType rootSearchType;
    private boolean allowAutocompletion = true;
    protected DefaultSimpleReferenceSelectorModel model = null;
    protected SimpleReferenceSelector simpleSelector;
    protected DefaultSimpleReferenceSelectorController selectorController;


    public DefaultSimpleReferenceUIEditor()
    {
        this(null);
    }


    public DefaultSimpleReferenceUIEditor(ObjectType rootType)
    {
        this.model = new DefaultSimpleReferenceSelectorModel(rootType);
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
        Label label;
        parseInitialParameters(parameters);
        if(isEditable())
        {
            this.simpleSelector = new SimpleReferenceSelector();
            this.simpleSelector.setAutocompletionAllowed(this.allowAutocompletion);
            this.simpleSelector.setDisabled(!isEditable());
            Object createContext = parameters.get("createContext");
            if(createContext instanceof CreateContext)
            {
                this.simpleSelector.setCreateContext((CreateContext)createContext);
            }
            Optional<Boolean> allowCreateConfiguredByParam = getBooleanParameter("allowCreate", parameters);
            if(allowCreateConfiguredByParam.isPresent())
            {
                this.simpleSelector.setAllowcreate(allowCreateConfiguredByParam.get());
            }
            else
            {
                this.simpleSelector.setAllowcreate(isAllowCreate());
            }
            Integer maxAC = findMaxAutocompleteSearchResults(parameters);
            if(maxAC != null && maxAC.intValue() > 0)
            {
                this.model.setMaxAutoCompleteResultSize(maxAC.intValue());
            }
            this.model.setParameters(parameters);
            if(initialValue != null)
            {
                if(initialValue instanceof TypedObject)
                {
                    this.model.setValue(initialValue);
                }
                else if(initialValue instanceof Collection && ((Collection)initialValue).size() == 1 && ((Collection)initialValue).iterator().next() instanceof TypedObject)
                {
                    this.model.setValue(((Collection)initialValue).iterator().next());
                }
                else
                {
                    throw new IllegalArgumentException("Initial value '" + initialValue + "' not a typed object.");
                }
            }
            else
            {
                this.model.setValue(initialValue);
            }
            if(UISessionUtils.getCurrentSession().isUsingTestIDs())
            {
                String id = "SimpleReferenceSelector_";
                String attQual = (String)parameters.get("attributeQualifier");
                if(attQual != null)
                {
                    attQual = attQual.replaceAll("\\W", "");
                    id = id + id;
                }
                UITools.applyTestID((Component)this.simpleSelector, id);
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
            this.selectorController = new DefaultSimpleReferenceSelectorController(this.model, (UISimpleReferenceSelector)this.simpleSelector, listener, additionalListener);
            this.selectorController.initialize();
            this.simpleSelector.setModel((SimpleReferenceSelectorModel)this.model);
            AbstractUIEditor.CancelButtonContainer cancelButtonContainer = new AbstractUIEditor.CancelButtonContainer((AbstractUIEditor)this, listener, (AbstractUIEditor.CancelListener)new Object(this, listener));
            cancelButtonContainer.setSclass("simpleReferenceEditorContainer");
            this.simpleSelector.addEventSelectorListener("onFinishEdit", (EventListener)new Object(this, cancelButtonContainer));
            this.simpleSelector.addEventSelectorListener("onEditStart", (EventListener)new Object(this, cancelButtonContainer));
            if(BooleanUtils.isNotTrue((this.model.getParameters().get("disableOnBlur") == null) ? Boolean.TRUE : (Boolean)this.model
                            .getParameters().get("disableOnBlur")))
            {
                this.simpleSelector.addEventSelectorListener("onBlur", (EventListener)new Object(this, listener, cancelButtonContainer));
            }
            this.simpleSelector.addEventSelectorListener("onOK", (EventListener)new Object(this, listener, cancelButtonContainer));
            if(!isAllowActivate(parameters))
            {
                UITools.modifySClass((HtmlBasedComponent)this.simpleSelector, "disallowActivate", true);
            }
            cancelButtonContainer.setContent((Component)this.simpleSelector);
            return (HtmlBasedComponent)cancelButtonContainer;
        }
        LabelService labelService = UISessionUtils.getCurrentSession().getLabelService();
        if(initialValue instanceof TypedObject)
        {
            label = new Label(labelService.getObjectTextLabel((TypedObject)initialValue));
        }
        else
        {
            label = new Label();
        }
        return (HtmlBasedComponent)label;
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
        this.model.setValue(value);
    }


    public void setFocus(HtmlBasedComponent rootEditorComponent, boolean selectAll)
    {
        if(isEditable())
        {
            ((AbstractUIEditor.CancelButtonContainer)rootEditorComponent).showButton(true);
        }
        SimpleReferenceSelector element = (SimpleReferenceSelector)((AbstractUIEditor.CancelButtonContainer)rootEditorComponent).getContent();
        element.setFocus(true);
        if(this.initialInputString != null)
        {
            this.simpleSelector.setInitString(this.initialInputString);
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


    public DefaultSimpleReferenceSelectorModel getModel()
    {
        return this.model;
    }


    protected void parseInitialParameters(Map<String, ? extends Object> parameters)
    {
        parseInitialInputString(parameters);
        Object object = parameters.get("allowAutocompletion");
        if(object instanceof String)
        {
            this.allowAutocompletion = BooleanUtils.toBoolean((String)object);
        }
        Object autocompletionSearchTypeObject = parameters.get("autocompletionSearchType");
        if(autocompletionSearchTypeObject instanceof String && !((String)autocompletionSearchTypeObject).isEmpty())
        {
            TypeService typeService = getTypeService();
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


    protected TypeService getTypeService()
    {
        return (TypeService)SpringUtil.getBean("cockpitTypeService");
    }


    private boolean isAllowActivate(Map<String, ? extends Object> parameters)
    {
        boolean isActivate = true;
        String paramIsActivate = (String)parameters.get("allowActivate");
        if(paramIsActivate != null)
        {
            isActivate = Boolean.parseBoolean(paramIsActivate);
        }
        return isActivate;
    }
}
