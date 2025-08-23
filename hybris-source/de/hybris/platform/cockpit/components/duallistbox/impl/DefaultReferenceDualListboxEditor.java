package de.hybris.platform.cockpit.components.duallistbox.impl;

import de.hybris.platform.cockpit.components.duallistbox.AbstractDualListboxEditor;
import de.hybris.platform.cockpit.components.duallistbox.DualListboxHelper;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.simple.impl.DefaultSimpleReferenceSelectorModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.comparators.UserNameComparator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Label;

public class DefaultReferenceDualListboxEditor extends AbstractDualListboxEditor<TypedObject>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultReferenceDualListboxEditor.class);
    private static final String SEARCH_USER_INFO_BOX = "Search for Users/User Groups";
    private int maxRows = -1;
    private ObjectType rootType = null;
    private ObjectType rootSearchType = null;
    protected Comparator comparator = null;
    protected DefaultSimpleReferenceSelectorModel model = null;


    public DefaultReferenceDualListboxEditor(List<TypedObject> assignedValuesList)
    {
        super(assignedValuesList);
    }


    public EventListener getOnUserSearchListener()
    {
        return (EventListener)new Object(this);
    }


    protected void parseParams(Map<String, ? extends Object> parameters)
    {
        Object maxRes = parameters.get("maxResults");
        if(maxRes instanceof String)
        {
            try
            {
                this.maxRows = Integer.parseInt((String)maxRes);
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
        if(getRootSearchType() != null)
        {
            setRootType(getRootSearchType());
            this
                            .model = new DefaultSimpleReferenceSelectorModel(UISessionUtils.getCurrentSession().getTypeService().getObjectType(getRootSearchType().getCode()));
            this.model.setMaxAutoCompleteResultSize(this.maxRows);
            DualListboxHelper.doAutoCompleteSearch("", this.model, getAssignedValuesList());
        }
        else
        {
            LOG.error("Root type must be defined for current editor");
        }
    }


    protected List<TypedObject> removeDuplicatedItems(List<TypedObject> searchResult)
    {
        return DualListboxHelper.removeDuplicatedItems(searchResult, getAssignedValuesList());
    }


    protected void updateCollectionAllItems()
    {
        String searchTerm = this.inputComponentDiv.getSearchInputComponent().getText().equals("Search for Users/User Groups") ? "" : this.inputComponentDiv.getSearchInputComponent().getText();
        if(this.inputComponentDiv.getSearchInputComponent() != null && this.model != null)
        {
            DualListboxHelper.doAutoCompleteSearch(searchTerm, this.model, this.assignedValuesList);
            List<TypedObject> resultList = this.model.getAutoCompleteResult();
            Collections.sort(resultList, (Comparator<? super TypedObject>)new UserNameComparator());
            setResultListData(this.collectionAllItems, removeDuplicatedItems(resultList));
        }
    }


    protected String getSearchInfoBox()
    {
        return "Search for Users/User Groups";
    }


    protected Label getAvailableValuesLabel()
    {
        return new Label(Labels.getLabel("duallistbox.users.assigned"));
    }


    protected Label getAssignedValuesLabel()
    {
        return new Label(Labels.getLabel("duallistbox.users.available"));
    }


    protected int getMaxRows()
    {
        return this.maxRows;
    }


    public void setMaxRows(int maxRows)
    {
        this.maxRows = maxRows;
    }


    protected ObjectType getRootSearchType()
    {
        return this.rootSearchType;
    }


    protected void setRootSearchType(ObjectType rootSearchType)
    {
        this.rootSearchType = rootSearchType;
    }


    protected ObjectType getRootType()
    {
        return this.rootType;
    }


    protected void setRootType(ObjectType rootType)
    {
        this.rootType = rootType;
    }


    protected Comparator getComparator()
    {
        if(this.comparator == null)
        {
            return (Comparator)new UserNameComparator();
        }
        return this.comparator;
    }


    public void setComparator(Comparator comparator)
    {
        this.comparator = comparator;
    }
}
