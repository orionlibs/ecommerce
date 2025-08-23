package de.hybris.platform.cockpit.model.referenceeditor.collection;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.referenceeditor.collection.controller.CollectionUIEditorController;
import de.hybris.platform.cockpit.model.referenceeditor.collection.model.CollectionEditorModel;
import de.hybris.platform.cockpit.model.referenceeditor.collection.model.DefaultCollectionEditorModel;
import de.hybris.platform.cockpit.model.referenceeditor.collection.model.media.DefaultMediaCollectionEditorModel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;

public class CollectionUIEditorMedia extends CollectionUIEditor
{
    private static final String PROPERTY_ALLOW_OVERLAP = "default.popUpEditor.allowOverlap";
    private static final Logger LOG = LoggerFactory.getLogger(CollectionUIEditorMedia.class);


    public CollectionUIEditorMedia()
    {
        this(null);
    }


    public CollectionUIEditorMedia(ObjectType rootType)
    {
        this.model = (DefaultCollectionEditorModel)new DefaultMediaCollectionEditorModel(rootType);
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
        this.collectionEditor = createCollectionEditor(listener, additionalListener);
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
        Object imgHeight = parameters.get("imageHeight");
        if(imgHeight instanceof String)
        {
            this.collectionEditor.setImageHeight((String)imgHeight);
        }
        imgHeight = parameters.get("imageHeightInList");
        if(imgHeight instanceof String)
        {
            this.collectionEditor.setImageHeight((String)imgHeight);
        }
        assignedPassedValue(initialValue);
        applyTestUserID(parameters);
        if(this.collectionController != null)
        {
            this.collectionController.unregisterListeners();
        }
        this.collectionController = new CollectionUIEditorController((AbstractCollectionEditor)this.collectionEditor, this.model, listener, getRootType());
        this.collectionController.initialize();
        this.collectionEditor.setModel((CollectionEditorModel)this.model);
        AbstractUIEditor.CancelButtonContainer cancelButtonContainer = new AbstractUIEditor.CancelButtonContainer((AbstractUIEditor)this, listener, (AbstractUIEditor.CancelListener)new Object(this));
        registerEventListeners(cancelButtonContainer);
        cancelButtonContainer.setSclass("collectionMediaReferenceEditor");
        cancelButtonContainer.setContent((Component)this.collectionEditor);
        return (HtmlBasedComponent)cancelButtonContainer;
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


    protected boolean isShowEditButton()
    {
        boolean result = true;
        if(isPopupEditorOpen())
        {
            result = isAllowOverlap();
        }
        return result;
    }


    private boolean isAllowOverlap()
    {
        boolean allowOverlap = false;
        try
        {
            allowOverlap = Boolean.parseBoolean(UITools.getCockpitParameter("default.popUpEditor.allowOverlap", Executions.getCurrent()));
        }
        catch(Exception e)
        {
            LOG.warn(String.format("Cannot read %s property", new Object[] {"default.popUpEditor.allowOverlap"}));
        }
        return allowOverlap;
    }


    private boolean isPopupEditorOpen()
    {
        UICockpitPerspective currentPerspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
        return (currentPerspective instanceof BaseUICockpitPerspective && ((BaseUICockpitPerspective)currentPerspective)
                        .isPopupEditorOpen());
    }


    public CollectionEditor createCollectionEditor(EditorListener listener, AdditionalReferenceEditorListener additionalListener)
    {
        return (CollectionEditor)new Object(this, listener, additionalListener);
    }
}
