package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImportParameters;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ExcelMediaImportTranslator extends AbstractExcelMediaImportTranslator<MediaModel>
{
    public boolean canHandle(AttributeDescriptorModel attributeDescriptor)
    {
        return (getTypeService().isAssignableFrom("Media", attributeDescriptor.getAttributeType().getCode()) ||
                        isLocalizedOfType(attributeDescriptor, "Media"));
    }


    public Optional<Object> exportData(MediaModel objectToExport)
    {
        Objects.requireNonNull(Object.class);
        return exportMedia(objectToExport).map(Object.class::cast);
    }


    public Impex importData(AttributeDescriptorModel attributeDescriptor, ImportParameters importParameters)
    {
        Impex impex = new Impex();
        if(hasImportData(importParameters.getSingleValueParameters()))
        {
            ImpexForType mediaImpex = impex.findUpdates("Media");
            Map<String, String> params = importParameters.getSingleValueParameters();
            String mediaRefId = generateMediaRefId(attributeDescriptor, params);
            mediaImpex.addRow(createMediaRow(attributeDescriptor, mediaRefId, params));
            ImpexForType impexForType = impex.findUpdates(importParameters.getTypeCode());
            addReferencedMedia(impexForType, attributeDescriptor, mediaRefId, importParameters.getIsoCode());
        }
        return impex;
    }
}
