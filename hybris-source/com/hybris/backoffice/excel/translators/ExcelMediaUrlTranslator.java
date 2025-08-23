package com.hybris.backoffice.excel.translators;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Objects;
import java.util.Optional;

public class ExcelMediaUrlTranslator extends ExcelMediaImportTranslator
{
    private final ExcelMediaUrlExportDecorator urlExportDecorator = new ExcelMediaUrlExportDecorator();


    public Optional<Object> exportData(MediaModel media)
    {
        Objects.requireNonNull(Object.class);
        return this.urlExportDecorator.addUrlToMediaExport(exportMedia(media), media).map(Object.class::cast);
    }


    public String referenceFormat(AttributeDescriptorModel attributeDescriptor)
    {
        return this.urlExportDecorator.decorateReferenceFormat(super.referenceFormat(attributeDescriptor));
    }
}
