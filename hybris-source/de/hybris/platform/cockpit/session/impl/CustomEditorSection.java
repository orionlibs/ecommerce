package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.services.config.CustomEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import java.util.HashMap;
import java.util.Map;

public class CustomEditorSection extends EditorSection
{
    private final Map<String, Object> attributes = new HashMap<>();


    public CustomEditorSection(EditorSectionConfiguration secConf)
    {
        super(secConf);
    }


    public SectionRenderer getCustomRenderer()
    {
        return (getSectionConfiguration() instanceof CustomEditorSectionConfiguration) ? (
                        (CustomEditorSectionConfiguration)getSectionConfiguration()).getCustomRenderer() :
                        null;
    }


    public void setAttribute(String key, Object value)
    {
        this.attributes.put(key, value);
    }


    public Object getAttribute(String key)
    {
        return this.attributes.get(key);
    }
}
