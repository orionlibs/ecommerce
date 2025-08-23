package de.hybris.platform.cockpit.reports.widgets.factory.impl;

import de.hybris.platform.cockpit.model.WidgetPreferencesModel;
import de.hybris.platform.cockpit.reports.widgets.ReportWidget;
import de.hybris.platform.cockpit.reports.widgets.factory.ReportWidgetFactory;
import de.hybris.platform.cockpit.reports.widgets.factory.WidgetException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportWidgetFactoryImpl implements ReportWidgetFactory
{
    private List<ReportWidget> widgets;
    protected FlexibleSearchService flexibleSearchService;


    public ReportWidget getWidget(WidgetPreferencesModel configuration)
    {
        for(ReportWidget widget : this.widgets)
        {
            if(widget.getClass().getGenericSuperclass() != null)
            {
                Type[] arrt = widget.getClass().getGenericInterfaces();
                for(Type type : arrt)
                {
                    if(type instanceof ParameterizedType)
                    {
                        ParameterizedType parameterizedType = (ParameterizedType)type;
                        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                        if(actualTypeArguments != null && actualTypeArguments.length > 0)
                        {
                            for(Type actualTypeArg : actualTypeArguments)
                            {
                                if(actualTypeArg instanceof Class)
                                {
                                    if(((Class)actualTypeArg).isAssignableFrom(configuration.getClass()))
                                    {
                                        return widget;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        throw new WidgetException("No Widget for configuration: " + configuration.getClass());
    }


    public Collection<WidgetPreferencesModel> getConfigurations()
    {
        StringBuffer query = (new StringBuffer("select {")).append("pk").append("} ");
        query.append("  from {").append("JasperWidgetPreferences").append("!}");
        query.append(" where {").append("ownerUser").append("}= ?user");
        Map<String, Object> params = new HashMap<>();
        params.put("user", UISessionUtils.getCurrentSession().getUser());
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), params);
        SearchResult<WidgetPreferencesModel> result = this.flexibleSearchService.search(searchQuery);
        return result.getResult();
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public void setWidgets(List<ReportWidget> widgets)
    {
        this.widgets = widgets;
    }
}
