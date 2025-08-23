package de.hybris.platform.admincockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.CompareMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.ListMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.MainAreaComponentFactory;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.services.search.impl.ItemAttributeSearchDescriptor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.zkoss.util.resource.Labels;

public class AdmincockpitConstraintGroupsBrowserModel extends DefaultSearchBrowserModel
{
    private List<MainAreaComponentFactory> viewModes = null;
    private Set<ComposedTypeModel> constraintGroupModelsSet = null;


    public AdmincockpitConstraintGroupsBrowserModel(ObjectTemplate rootType)
    {
        super(rootType);
    }


    public void setConstraintGroupModelsSet(Set<ComposedTypeModel> constraintGroupModelsSet)
    {
        this.constraintGroupModelsSet = constraintGroupModelsSet;
    }


    public Set<ComposedTypeModel> getConstraintGroupModelsSet()
    {
        return this.constraintGroupModelsSet;
    }


    public AbstractContentBrowser createViewComponent()
    {
        return (AbstractContentBrowser)new Object(this);
    }


    protected ExtendedSearchResult doSearchInternal(Query query)
    {
        List<List<SearchParameterValue>> orValues = new LinkedList<>();
        List<SearchParameterValue> searchParameterValues = new LinkedList<>();
        if(this.constraintGroupModelsSet == null)
        {
            this.constraintGroupModelsSet = new HashSet<>();
        }
        PropertyDescriptor propertyDescriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("abstractConstraint.constraintGroups");
        ItemAttributeSearchDescriptor searchDescriptor = new ItemAttributeSearchDescriptor((ItemAttributePropertyDescriptor)propertyDescriptor);
        for(ComposedTypeModel composedTypeModel : getConstraintGroupModelsSet())
        {
            SearchParameterValue searchParameter = new SearchParameterValue((SearchParameterDescriptor)searchDescriptor, composedTypeModel.getPk(), searchDescriptor.getDefaultOperator());
            searchParameterValues.add(searchParameter);
        }
        orValues.add(searchParameterValues);
        query.setParameterOrValues(orValues);
        return super.doSearchInternal(query);
    }


    public String getLabel()
    {
        return Labels.getLabel("ba.constraintgroups_browsermodel_label");
    }


    public Object clone() throws CloneNotSupportedException
    {
        AdmincockpitConstraintGroupsBrowserModel admincockpitConstraintGroupsBrowserModel = new AdmincockpitConstraintGroupsBrowserModel(getRootType());
        admincockpitConstraintGroupsBrowserModel.setSearchProvider(getSearchProvider());
        admincockpitConstraintGroupsBrowserModel.setResult(getResult());
        admincockpitConstraintGroupsBrowserModel.setLastQuery(getLastQuery());
        admincockpitConstraintGroupsBrowserModel.setSortableProperties(getAdvancedSearchModel().getSortableProperties());
        admincockpitConstraintGroupsBrowserModel.setSortAsc(getAdvancedSearchModel().isSortAscending());
        admincockpitConstraintGroupsBrowserModel.setOffset(getOffset());
        admincockpitConstraintGroupsBrowserModel.setPageSize(getPageSize());
        admincockpitConstraintGroupsBrowserModel.setTotalCount(getTotalCount());
        admincockpitConstraintGroupsBrowserModel.setBrowserFilterFixed(getBrowserFilterFixed());
        admincockpitConstraintGroupsBrowserModel.setBrowserFilter(getBrowserFilter());
        admincockpitConstraintGroupsBrowserModel.setLabel(getLabel());
        admincockpitConstraintGroupsBrowserModel.setViewMode(getViewMode());
        admincockpitConstraintGroupsBrowserModel.setConstraintGroupModelsSet(getConstraintGroupModelsSet());
        admincockpitConstraintGroupsBrowserModel.setSimplePaging(isSimplePaging());
        return admincockpitConstraintGroupsBrowserModel;
    }


    public List<MainAreaComponentFactory> getAvailableViewModes()
    {
        if(this.viewModes == null)
        {
            this.viewModes = new ArrayList<>();
            this.viewModes.add(new ListMainAreaComponentFactory());
            this.viewModes.add(new CompareMainAreaComponentFactory());
        }
        return this.viewModes;
    }
}
