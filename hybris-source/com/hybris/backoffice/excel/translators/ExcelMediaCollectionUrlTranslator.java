package com.hybris.backoffice.excel.translators;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public class ExcelMediaCollectionUrlTranslator extends ExcelMediaCollectionImportTranslator
{
    private final ExcelMediaUrlExportDecorator urlExporter = new ExcelMediaUrlExportDecorator();


    public Optional<Object> exportData(Collection<MediaModel> mediasToExport)
    {
        String export = ((Stream)Optional.<Collection<MediaModel>>ofNullable(mediasToExport).map(Collection::stream).orElseGet(Stream::empty)).map(this::exportMedia).filter(Optional::isPresent).map(Optional::get).collect(Collectors.joining(","));
        return StringUtils.isNotBlank(export) ? Optional.<Object>of(export) : Optional.<Object>empty();
    }


    public Optional<String> exportMedia(MediaModel media)
    {
        return this.urlExporter.addUrlToMediaExport(super.exportMedia(media), media);
    }


    public String referenceFormat(AttributeDescriptorModel attributeDescriptor)
    {
        return this.urlExporter.decorateReferenceFormat(super.referenceFormat(attributeDescriptor));
    }
}
