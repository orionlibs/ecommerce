package com.hybris.pcmbackoffice.widgets.charts.facetchart.filter;

import com.hybris.backoffice.search.utils.CategoryCatalogVersionMapper;
import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class CategoryFacetChartFilterAdapter extends AbstractFacetChartFilterAdapter implements ReferenceEditorFacetChartFilterAdapter
{
    private CategoryCatalogVersionMapper categoryCatalogVersionMapper;


    public String convertToFacetValue(Object object)
    {
        return this.categoryCatalogVersionMapper.encode((CategoryModel)object);
    }


    public void deleteFilter(CockpitEvent event, String selectedFilterValue, WidgetInstanceManager widgetInstanceManager, Executable onDeletedCallback)
    {
        getNamesOfDeletedFilters(event).forEach(filterName -> deleteFilterFromModel(filterName, selectedFilterValue, widgetInstanceManager, onDeletedCallback));
    }


    protected List<String> getNamesOfDeletedFilters(CockpitEvent event)
    {
        List<String> filters = new LinkedList<>();
        if(event != null && event.getData() != null && event.getData() instanceof Collection)
        {
            Objects.requireNonNull(filters);
            getCategoryCodes((Collection)event.getData()).forEach(filters::add);
        }
        return filters;
    }


    protected Stream<String> getCategoryCodes(Collection<?> items)
    {
        Objects.requireNonNull(ClassificationClassModel.class);
        Objects.requireNonNull(ClassificationClassModel.class);
        return items.stream().filter(ClassificationClassModel.class::isInstance).map(ClassificationClassModel.class::cast)
                        .map(this::getCategoryCode).filter(Optional::isPresent).map(Optional::get);
    }


    protected Optional<String> getCategoryCode(ClassificationClassModel model)
    {
        if(model != null)
        {
            String code = model.getCode();
            return StringUtils.isNotBlank(code) ? Optional.<String>of(code) : Optional.<String>empty();
        }
        return Optional.empty();
    }


    protected boolean canDelete(Object filterValue, String nameOfDeletedItem)
    {
        if(filterValue instanceof ClassificationClassModel)
        {
            return ((ClassificationClassModel)filterValue).getCode().equals(nameOfDeletedItem);
        }
        return false;
    }


    @Required
    public void setCategoryCatalogVersionMapper(CategoryCatalogVersionMapper categoryCatalogVersionMapper)
    {
        this.categoryCatalogVersionMapper = categoryCatalogVersionMapper;
    }
}
