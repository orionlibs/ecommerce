package de.hybris.platform.cockpit.model.referenceeditor.simple.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.referenceeditor.simple.MediaSimpleReferenceSelector;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleReferenceSelector;
import de.hybris.platform.cockpit.model.referenceeditor.simple.SimpleReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.simple.UISimpleReferenceSelector;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang.BooleanUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;

public class DefaultSimpleMediaReferenceUIEditor extends DefaultSimpleReferenceUIEditor
{
    public DefaultSimpleMediaReferenceUIEditor()
    {
        this(null);
    }


    public DefaultSimpleMediaReferenceUIEditor(ObjectType rootType)
    {
        this.model = (DefaultSimpleReferenceSelectorModel)new DefaultSimpleMediaReferenceSelectorModel(rootType);
    }


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        parseInitialInputString(parameters);
        this.simpleSelector = (SimpleReferenceSelector)new MediaSimpleReferenceSelector();
        Object imgHeight = parameters.get("imageHeight");
        if(imgHeight instanceof String)
        {
            ((MediaSimpleReferenceSelector)this.simpleSelector).setImageHeight((String)imgHeight);
        }
        imgHeight = parameters.get("imageHeightInList");
        if(imgHeight instanceof String)
        {
            ((MediaSimpleReferenceSelector)this.simpleSelector).setImageHeightInAutocompleteList((String)imgHeight);
        }
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
        this.simpleSelector.setDisabled(!isEditable());
        this.model.setParameters(parameters);
        if(initialValue != null)
        {
            if(initialValue instanceof de.hybris.platform.cockpit.model.meta.TypedObject)
            {
                this.model.setValue(initialValue);
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
        this.selectorController = (DefaultSimpleReferenceSelectorController)new DefaultSimpleMediaReferenceSelectorController(this.model, (UISimpleReferenceSelector)this.simpleSelector, listener, additionalListener);
        this.selectorController.initialize();
        this.simpleSelector.setModel((SimpleReferenceSelectorModel)this.model);
        AbstractUIEditor.CancelButtonContainer cancelButtonContainer = new AbstractUIEditor.CancelButtonContainer((AbstractUIEditor)this, listener, (AbstractUIEditor.CancelListener)new Object(this, listener));
        cancelButtonContainer.setSclass("simpleMediaReferenceSelectorContainer");
        this.simpleSelector.addEventSelectorListener("onFinishEdit", (EventListener)new Object(this, cancelButtonContainer));
        this.simpleSelector.addEventSelectorListener("onEditStart", (EventListener)new Object(this, cancelButtonContainer));
        this.simpleSelector.addEventSelectorListener("onCancel", (EventListener)new Object(this, cancelButtonContainer));
        if(BooleanUtils.isNotTrue((Boolean)this.model.getParameters().get("disableOnBlur")))
        {
            this.simpleSelector.addEventSelectorListener("onBlur", (EventListener)new Object(this, listener, cancelButtonContainer));
        }
        this.simpleSelector.addEventSelectorListener("onOK", (EventListener)new Object(this, cancelButtonContainer, listener));
        cancelButtonContainer.setContent((Component)this.simpleSelector);
        return (HtmlBasedComponent)cancelButtonContainer;
    }
}
