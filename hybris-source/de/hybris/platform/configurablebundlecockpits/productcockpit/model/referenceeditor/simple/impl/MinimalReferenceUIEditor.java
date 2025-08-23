package de.hybris.platform.configurablebundlecockpits.productcockpit.model.referenceeditor.simple.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleReferenceSelector;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.simple.UISimpleReferenceSelector;
import de.hybris.platform.cockpit.model.referenceeditor.simple.impl.DefaultSimpleReferenceSelectorController;
import de.hybris.platform.cockpit.model.referenceeditor.simple.impl.DefaultSimpleReferenceUIEditor;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Label;

public class MinimalReferenceUIEditor extends DefaultSimpleReferenceUIEditor
{
    public MinimalReferenceUIEditor()
    {
    }


    public MinimalReferenceUIEditor(ObjectType rootType)
    {
        super(rootType);
    }


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ?> parameters, EditorListener listener)
    {
        parseInitialParameters(parameters);
        if(!isEditable())
        {
            LabelService labelService = UISessionUtils.getCurrentSession().getLabelService();
            return (initialValue != null) ?
                            (HtmlBasedComponent)new Label(labelService.getObjectTextLabelForTypedObject((TypedObject)initialValue)) :
                            (HtmlBasedComponent)new Label();
        }
        this.simpleSelector = new SimpleReferenceSelector();
        this.simpleSelector.setAutocompletionAllowed(false);
        this.simpleSelector.setDisabled(!isEditable());
        Object createContext = parameters.get("createContext");
        if(createContext instanceof CreateContext)
        {
            this.simpleSelector.setCreateContext((CreateContext)createContext);
        }
        this.simpleSelector.setAllowcreate(isAllowCreate());
        Integer maxAC = findMaxAutocompleteSearchResults(parameters);
        if(maxAC != null && maxAC.intValue() > 0)
        {
            this.model.setMaxAutoCompleteResultSize(maxAC.intValue());
        }
        this.model.setParameters(parameters);
        setInitialValue(initialValue);
        setParametersForTestIds(parameters);
        createTestControllerForReferenceSelector(parameters, listener);
        this.simpleSelector.setModel((SimpleReferenceSelectorModel)this.model);
        return (HtmlBasedComponent)createCancelButtonContainer(parameters, listener);
    }


    private void createTestControllerForReferenceSelector(Map<String, ?> parameters, EditorListener listener)
    {
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
    }


    private AbstractUIEditor.CancelButtonContainer createCancelButtonContainer(Map<String, ?> parameters, EditorListener listener)
    {
        AbstractUIEditor.CancelButtonContainer cancelButtonContainer = new AbstractUIEditor.CancelButtonContainer((AbstractUIEditor)this, listener, () -> this.simpleSelector.setFocus(false));
        cancelButtonContainer.setSclass("simpleReferenceEditorContainer");
        this.simpleSelector.addEventSelectorListener("onFinishEdit", (EventListener)new EditFinishEventListener(cancelButtonContainer));
        this.simpleSelector.addEventSelectorListener("onEditStart", (EventListener)new EditStartEventListener(cancelButtonContainer));
        this.simpleSelector.addEventSelectorListener("onBlur", (EventListener)new OnBlurEventListener(this, listener, cancelButtonContainer));
        this.simpleSelector.addEventSelectorListener("onOK", (EventListener)new OnOkEventListener(this, listener, cancelButtonContainer));
        if(!isAllowActivateNew((Map)parameters))
        {
            UITools.modifySClass((HtmlBasedComponent)this.simpleSelector, "disallowActivate", true);
        }
        cancelButtonContainer.setContent((Component)this.simpleSelector);
        return cancelButtonContainer;
    }


    private void setParametersForTestIds(Map<String, ?> parameters)
    {
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
    }


    private void setInitialValue(Object initialValue)
    {
        if(initialValue == null)
        {
            this.model.setValue(null);
            return;
        }
        if(initialValue instanceof TypedObject)
        {
            this.model.setValue(initialValue);
        }
        else
        {
            throw new IllegalArgumentException("Initial value '" + initialValue + "' not a typed object.");
        }
    }


    protected boolean isAllowActivateNew(Map<String, ? extends Object> parameters)
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
