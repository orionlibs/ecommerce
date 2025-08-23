package com.hybris.pcmbackoffice.widgets.charts.facetchart.filter;

import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class CatalogVersionFacetChartFilterAdapter extends AbstractFacetChartFilterAdapter implements ReferenceEditorFacetChartFilterAdapter
{
    private LabelService labelService;


    public String convertToFacetValue(Object object)
    {
        return this.labelService.getObjectLabel(object);
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
            getCatalogVersions((Collection)event.getData()).forEach(filters::add);
        }
        return filters;
    }


    protected Stream<String> getCatalogVersions(Collection<?> items)
    {
        Objects.requireNonNull(ClassificationSystemVersionModel.class);
        Objects.requireNonNull(ClassificationSystemVersionModel.class);
        return items.stream().filter(ClassificationSystemVersionModel.class::isInstance).map(ClassificationSystemVersionModel.class::cast).map(this::getCatalogVersion).filter(Optional::isPresent)
                        .map(Optional::get);
    }


    protected Optional<String> getCatalogVersion(ClassificationSystemVersionModel model)
    {
        if(model != null)
        {
            String version = model.getVersion();
            return StringUtils.isNotBlank(version) ? Optional.<String>of(version) : Optional.<String>empty();
        }
        return Optional.empty();
    }


    protected boolean canDelete(Object filterValue, String nameOfDeletedItem)
    {
        if(filterValue instanceof ClassificationSystemVersionModel)
        {
            return ((ClassificationSystemVersionModel)filterValue).getVersion().equals(nameOfDeletedItem);
        }
        return false;
    }


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }
}
