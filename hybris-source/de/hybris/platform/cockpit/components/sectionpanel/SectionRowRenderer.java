package de.hybris.platform.cockpit.components.sectionpanel;

import java.util.Map;
import org.zkoss.zk.ui.Component;

public interface SectionRowRenderer
{
    void render(SectionPanel paramSectionPanel, Component paramComponent, SectionRow paramSectionRow, Map<String, Object> paramMap);
}
