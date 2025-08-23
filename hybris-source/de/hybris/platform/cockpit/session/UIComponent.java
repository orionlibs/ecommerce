package de.hybris.platform.cockpit.session;

import java.util.Map;

public interface UIComponent
{
    String getViewURI();


    void initialize(Map<String, Object> paramMap);


    String getLabel();
}
