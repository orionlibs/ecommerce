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

public class AdmincockpitConstraintBrowserModel extends DefaultSearchBrowserModel
{
    private List<MainAreaComponentFactory> viewModes = null;
    private Set<ComposedTypeModel> composedTypeModelsSet = null;


    public AdmincockpitConstraintBrowserModel(ObjectTemplate rootType)
    {
        super(rootType);
    }


    public void setComposedTypeModelsSet(Set<ComposedTypeModel> composedTypeModelsSet)
    {
        this.composedTypeModelsSet = composedTypeModelsSet;
    }


    public Set<ComposedTypeModel> getComposedTypeModelsSet()
    {
        return this.composedTypeModelsSet;
    }


    public AbstractContentBrowser createViewComponent()
    {
        return (AbstractContentBrowser)new Object(this);
    }


    protected ExtendedSearchResult doSearchInternal(Query query)
    {
        List<List<SearchParameterValue>> orValues = new LinkedList<>();
        List<SearchParameterValue> searchParameterValues = new LinkedList<>();
        if(this.composedTypeModelsSet == null)
        {
            this.composedTypeModelsSet = new HashSet<>();
        }
        PropertyDescriptor propertyDescriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("abstractConstraint.type");
        ItemAttributeSearchDescriptor searchDescriptor = new ItemAttributeSearchDescriptor((ItemAttributePropertyDescriptor)propertyDescriptor);
        for(ComposedTypeModel composedTypeModel : getComposedTypeModelsSet())
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
        return Labels.getLabel("ba.constraint_browsermodel_label");
    }


    public Object clone() throws CloneNotSupportedException
    {
        AdmincockpitConstraintBrowserModel admincockpitConstraintBrowserModel = new AdmincockpitConstraintBrowserModel(getRootType());
        admincockpitConstraintBrowserModel.setSearchProvider(getSearchProvider());
        admincockpitConstraintBrowserModel.setResult(getResult());
        admincockpitConstraintBrowserModel.setLastQuery(getLastQuery());
        admincockpitConstraintBrowserModel.setSortableProperties(getAdvancedSearchModel().getSortableProperties());
        admincockpitConstraintBrowserModel.setSortAsc(getAdvancedSearchModel().isSortAscending());
        admincockpitConstraintBrowserModel.setOffset(getOffset());
        admincockpitConstraintBrowserModel.setPageSize(getPageSize());
        admincockpitConstraintBrowserModel.setTotalCount(getTotalCount());
        admincockpitConstraintBrowserModel.setBrowserFilterFixed(getBrowserFilterFixed());
        admincockpitConstraintBrowserModel.setBrowserFilter(getBrowserFilter());
        admincockpitConstraintBrowserModel.setLabel(getLabel());
        admincockpitConstraintBrowserModel.setViewMode(getViewMode());
        admincockpitConstraintBrowserModel.setComposedTypeModelsSet(getComposedTypeModelsSet());
        admincockpitConstraintBrowserModel.setSimplePaging(isSimplePaging());
        return admincockpitConstraintBrowserModel;
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
