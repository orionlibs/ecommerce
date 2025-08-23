package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.components.sectionpanel.SectionRowRenderer;
import java.util.Map;

public interface CustomEditorRow
{
    void save();


    void load();


    SectionRowRenderer getRenderer();


    void setParameters(Map<String, Object> paramMap);


    String getID();
}
