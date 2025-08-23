package de.hybris.platform.platformbackoffice.bulkedit.renderer;

import com.hybris.backoffice.attributechooser.Attribute;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Map;

public interface ClassificationEditorBulkEditRenderer
{
    Editor render(WidgetInstanceManager paramWidgetInstanceManager, Attribute paramAttribute, Map<String, String> paramMap);
}
