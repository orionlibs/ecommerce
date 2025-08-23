package de.hybris.platform.cockpit.services.exporter;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import java.util.List;

public interface EditorAreaExporter
{
    byte[] export(List<EditorSectionConfiguration> paramList, TypedObject paramTypedObject);


    String getExportContentType();
}
