package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;

public class SimpleReferenceUIEditor extends AbstractReferenceUIEditor
{
    private static final Logger LOG = LoggerFactory.getLogger(SimpleReferenceUIEditor.class);
    private static final String SIMPLE_REFERENCE_SORT_PROPERTY = "simpleReferenceSortProperty";
    private static final String SIMPLE_REFERENCE_SORT_ORDER = "simpleReferenceSortOrder";
    private ObjectType rootType = null;
    private ObjectType rootSearchType = null;
    private List<TypedObject> availableValues = null;
    private int maxResults = 50;
    private TypeService typeService = null;


    @Deprecated
    protected boolean updateAvailableValues()
    {
        int count = getTypeService().countAllInstancesOf(getRootSearchType());
        if((this.maxResults >= 0 && count > this.maxResults) || count < 0)
        {
            return false;
        }
        setAvailableValues(new ArrayList<>(getTypeService().getAllInstancesOf(getRootSearchType())));
        return true;
    }


    protected boolean updateAvailableValues(Map<String, ? extends Object> parameters)
    {
        int count = getTypeService().countAllInstancesOf(getRootSearchType());
        if((this.maxResults >= 0 && count > this.maxResults) || count < 0)
        {
            return false;
        }
        List<TypedObject> currentValues = new ArrayList<>();
        if(parameters.containsKey("simpleReferenceSortProperty"))
        {
            String sortBy = (String)parameters.get("simpleReferenceSortProperty");
            String sortOrder = (String)parameters.get("simpleReferenceSortOrder");
            currentValues.addAll(getTypeService().getAllInstancesOf(getRootSearchType(), sortBy, sortOrder));
        }
        else
        {
            currentValues.addAll(getTypeService().getAllInstancesOf(getRootSearchType()));
        }
        setAvailableValues(currentValues);
        return true;
    }


    public void setAvailableValues(List<TypedObject> availableValues)
    {
        this.availableValues = availableValues;
    }


    protected List<TypedObject> getAvailableValues()
    {
        return (this.availableValues == null) ? Collections.EMPTY_LIST : this.availableValues;
    }


    protected void fillComboBox(Combobox combobox, TypedObject selected)
    {
        combobox.getChildren().clear();
        Comboitem nullItem = new Comboitem("");
        nullItem.setHeight("1em");
        nullItem.setValue(null);
        combobox.appendChild((Component)nullItem);
        List<TypedObject> availableValues = new ArrayList<>(getAvailableValues());
        if(selected != null && !availableValues.contains(selected))
        {
            availableValues.add(0, selected);
        }
        Comboitem selectedItem = null;
        for(TypedObject to : availableValues)
        {
            Comboitem item = new Comboitem(getLabel(to));
            item.setValue(to);
            combobox.appendChild((Component)item);
            if(to.equals(selected))
            {
                selectedItem = item;
            }
        }
        combobox.setSelectedItem(selectedItem);
    }


    protected void parseParams(Map<String, ? extends Object> parameters)
    {
        Object maxRes = parameters.get("maxResults");
        if(maxRes instanceof String)
        {
            try
            {
                this.maxResults = Integer.parseInt((String)maxRes);
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        Object searchTypeStr = parameters.get("searchTypeCode");
        if(searchTypeStr instanceof String)
        {
            try
            {
                this.rootSearchType = getTypeService().getObjectType((String)searchTypeStr);
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
    }


    private String getLabel(Object object)
    {
        if(object instanceof TypedObject)
        {
            return UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel((TypedObject)object);
        }
        return (object == null) ? null : object.toString();
    }


    private void updateView(Combobox combobox)
    {
        if(getValue() == null)
        {
            combobox.setValue(null);
            combobox.setSelectedItem(null);
        }
        else
        {
            for(Object citem : combobox.getItems())
            {
                if(getValue().equals(((Comboitem)citem).getValue()))
                {
                    combobox.setSelectedItem((Comboitem)citem);
                    break;
                }
            }
        }
    }


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Label label;
        if(!(initialValue instanceof TypedObject) && initialValue != null)
        {
            return (HtmlBasedComponent)new AbstractUIEditor.ErrorDiv((AbstractUIEditor)this, Labels.getLabel("editor.error.wrongvaluetype"));
        }
        parseInitialInputString(parameters);
        Combobox editorView = new Combobox();
        parseParams(parameters);
        if(!updateAvailableValues(parameters))
        {
            return (HtmlBasedComponent)new AbstractUIEditor.ErrorDiv((AbstractUIEditor)this, Labels.getLabel("editor.error.toomanyitems"));
        }
        if(isEditable())
        {
            fillComboBox(editorView, (TypedObject)initialValue);
            editorView.setReadonly(Boolean.TRUE.booleanValue());
            editorView.addEventListener("onFocus", (EventListener)new Object(this, editorView));
            editorView.addEventListener("onChange", (EventListener)new Object(this, editorView, listener));
            editorView.addEventListener("onOK", (EventListener)new Object(this, editorView, listener));
            editorView.addEventListener("onCancel", (EventListener)new Object(this, editorView, listener));
            return (HtmlBasedComponent)editorView;
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


    public ObjectType getRootSearchType()
    {
        return (this.rootSearchType == null) ? getRootType() : this.rootSearchType;
    }


    public ObjectType getRootType()
    {
        return this.rootType;
    }


    public void setRootSearchType(ObjectType rootSearchType)
    {
        this.rootSearchType = rootSearchType;
    }


    public void setRootType(ObjectType rootType)
    {
        this.rootType = rootType;
    }


    public boolean isInline()
    {
        return true;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }
}
