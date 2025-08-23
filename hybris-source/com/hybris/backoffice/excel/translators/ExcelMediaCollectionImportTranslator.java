package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImportParameters;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class ExcelMediaCollectionImportTranslator extends AbstractExcelMediaImportTranslator<Collection<MediaModel>>
{
    public boolean canHandle(AttributeDescriptorModel attributeDescriptor)
    {
        return (attributeDescriptor.getAttributeType() instanceof CollectionTypeModel && getTypeService().isAssignableFrom(((CollectionTypeModel)attributeDescriptor
                        .getAttributeType()).getElementType().getCode(), "Media"));
    }


    public Optional<Object> exportData(Collection<MediaModel> mediasToExport)
    {
        if(CollectionUtils.isEmpty(mediasToExport))
        {
            return Optional.empty();
        }
        String export = String.join(",", (Iterable<? extends CharSequence>)mediasToExport.stream().map(x$0 -> exportMedia(x$0)).filter(Optional::isPresent)
                        .map(Optional::get).collect(Collectors.toList()));
        return StringUtils.isNotBlank(export) ? Optional.<Object>of(export) : Optional.<Object>empty();
    }


    public Impex importData(AttributeDescriptorModel attributeDescriptor, ImportParameters importParameters)
    {
        Impex impex = new Impex();
        ImpexForType mediaImpex = impex.findUpdates("Media");
        Collection<String> mediaReferences = new ArrayList<>();
        importParameters.getMultiValueParameters().stream().filter(this::hasImportData).forEach(params -> {
            String mediaRefId = generateMediaRefId(attributeDescriptor, params);
            mediaImpex.addRow(createMediaRow(attributeDescriptor, mediaRefId, params));
            mediaReferences.add(mediaRefId);
        });
        if(CollectionUtils.isNotEmpty(mediaReferences))
        {
            ImpexForType impexForType = impex.findUpdates(importParameters.getTypeCode());
            addReferencedMedia(impexForType, attributeDescriptor, mediaReferences);
        }
        return impex;
    }
}
