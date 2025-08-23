package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.components.contentbrowser.ListSectionComponent;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.ListSectionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.DefaultListSectionModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.HtmlBasedComponent;

public class ListViewCollectionEditor extends AbstractReferenceUIEditor
{
    protected static final String LIST_VIEW_CONFIG_CODE = "listViewEditorSection";
    private static final Logger LOG = LoggerFactory.getLogger(ListViewCollectionEditor.class);
    private ListSectionModel model = null;
    private ListSectionComponent view = null;


    public ListViewCollectionEditor()
    {
        this(null);
    }


    public ListViewCollectionEditor(ObjectType rootType)
    {
        this
                        .model = (ListSectionModel)new DefaultListSectionModel("Fritz test", UISessionUtils.getCurrentSession().getCurrentPerspective().getActiveItem());
        this.model.setListViewConfigurationCode("listViewEditorSection");
        if(rootType instanceof ObjectTemplate)
        {
            this.model.setRootType((ObjectTemplate)rootType);
        }
        else
        {
            this.model.setRootType((rootType == null) ? null : UISessionUtils.getCurrentSession().getTypeService()
                            .getObjectTemplate(rootType.getCode()));
        }
    }


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
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
                this.model.setItems(Collections.singletonList((TypedObject)initialValue));
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
        this.view = createViewComponent(this.model);
        this.view.setWidth("100%");
        if(this.model == null)
        {
            throw new IllegalStateException("Model can not be null.");
        }
        this.model.initialize();
        this.view.initialize();
        return (HtmlBasedComponent)this.view;
    }


    public ObjectType getRootSearchType()
    {
        return getRootType();
    }


    public ObjectType getRootType()
    {
        return (ObjectType)this.model.getRootType();
    }


    protected ListSectionComponent createViewComponent(ListSectionModel model)
    {
        return (ListSectionComponent)new EditorListSectionComponent(model);
    }


    public void setAllowCreate(Boolean allowCreate)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("This editor does not support allow create state to be changed.");
        }
    }


    public void setRootSearchType(ObjectType rootSearchType)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("This editor currently does not support setting root search type.");
        }
    }


    public void setRootType(ObjectType rootType)
    {
        if(rootType instanceof ObjectTemplate)
        {
            this.model.setRootType((ObjectTemplate)rootType);
        }
        else
        {
            this.model.setRootType((rootType == null) ? null : UISessionUtils.getCurrentSession().getTypeService()
                            .getObjectTemplate(rootType.getCode()));
        }
    }


    protected ListSectionModel getModel()
    {
        return this.model;
    }


    public boolean isInline()
    {
        return true;
    }
}
