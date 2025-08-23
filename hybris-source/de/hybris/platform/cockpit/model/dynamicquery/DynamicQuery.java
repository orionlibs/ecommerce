package de.hybris.platform.cockpit.model.dynamicquery;

import java.util.Map;

public interface DynamicQuery
{
    Map<String, Object> getParameters();


    String getLabel();


    String getFexibleQuery();


    String getDescription();


    void addQueryParameter(String paramString, Object paramObject);


    void removeQueryParameter(String paramString);


    void setDescription(String paramString);


    void setLabel(String paramString);
}
