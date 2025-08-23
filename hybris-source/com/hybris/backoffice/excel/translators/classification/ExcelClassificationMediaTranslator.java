package com.hybris.backoffice.excel.translators.classification;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.ExcelImportContext;
import com.hybris.backoffice.excel.translators.ExcelMediaUrlExportDecorator;
import com.hybris.backoffice.excel.translators.media.MediaFolderProvider;
import de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.impex.jalo.media.MediaDataTranslator;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ExcelClassificationMediaTranslator extends AbstractClassificationAttributeTranslator
{
    public static final String PARAM_FILE_PATH = "filePath";
    public static final String PARAM_FOLDER = "folder";
    public static final String PARAM_CODE = "code";
    protected static final String MEDIA_CONTENT_HEADER_NAME = "@media";
    private static final int MEDIA_IMPEX_ORDER = -1000;
    private final ExcelMediaUrlExportDecorator excelMediaUrlExportDecorator = new ExcelMediaUrlExportDecorator();
    private TypeService typeService;
    private KeyGenerator mediaCodeGenerator;
    private MediaFolderProvider mediaFolderProvider;
    private boolean exportUrl;
    private int order;


    public boolean canHandleAttribute(ExcelClassificationAttribute excelClassificationAttribute)
    {
        ComposedTypeModel referenceType = excelClassificationAttribute.getAttributeAssignment().getReferenceType();
        return (referenceType != null && this.typeService.isAssignableFrom("Media", referenceType.getCode()));
    }


    public String singleReferenceFormat(ExcelClassificationAttribute excelAttribute)
    {
        String referenceFormat = "filePath:code:catalog:version:folder";
        if(this.exportUrl)
        {
            referenceFormat = this.excelMediaUrlExportDecorator.decorateReferenceFormat(referenceFormat);
        }
        return referenceFormat;
    }


    public Optional<String> exportSingle(ExcelClassificationAttribute excelAttribute, FeatureValue featureToExport)
    {
        if(featureToExport.getValue() instanceof MediaModel)
        {
            MediaModel media = (MediaModel)featureToExport.getValue();
            Optional<String> exportedValue = Optional.of(String.format(":%s:%s:%s", new Object[] {media.getCode(),
                            exportCatalogVersionData(media.getCatalogVersion()), media.getFolder().getQualifier()}));
            if(this.exportUrl)
            {
                exportedValue = this.excelMediaUrlExportDecorator.addUrlToMediaExport(exportedValue, media);
            }
            return exportedValue;
        }
        return Optional.empty();
    }


    protected String exportCatalogVersionData(CatalogVersionModel objectToExport)
    {
        if(objectToExport != null && objectToExport.getCatalog() != null && objectToExport.getCatalog().getId() != null && objectToExport
                        .getVersion() != null)
        {
            return String.format("%s:%s", new Object[] {objectToExport.getCatalog().getId(), objectToExport.getVersion()});
        }
        return ":";
    }


    public Impex importData(ExcelAttribute excelAttribute, ImportParameters importParameters, ExcelImportContext excelImportContext)
    {
        Impex impex = new Impex();
        if(excelAttribute instanceof ExcelClassificationAttribute)
        {
            ExcelClassificationAttribute excelClassificationAttribute = (ExcelClassificationAttribute)excelAttribute;
            ImpexForType mediaImpex = impex.findUpdates("Media");
            mediaImpex.setOrder(-1000);
            ImpexHeaderValue mediaHeader = createMediaHeader(excelClassificationAttribute);
            List<String> mediaValues = new ArrayList<>();
            for(Map<String, String> params : (Iterable<Map<String, String>>)importParameters.getMultiValueParameters())
            {
                if(hasImportData(params))
                {
                    mediaImpex.addRow(createMediaRow(params));
                    mediaValues.add(createMediaValue(params));
                }
            }
            ImpexForType impexForType = impex.findUpdates(importParameters.getTypeCode());
            impexForType.putValue(Integer.valueOf(0), mediaHeader, String.join(",", (Iterable)mediaValues));
        }
        return impex;
    }


    protected boolean hasImportData(Map<String, String> singleParams)
    {
        String code = singleParams.get("code");
        String filePath = singleParams.get("filePath");
        String catalog = singleParams.get("catalog");
        String version = singleParams.get("version");
        boolean hasCodeAndPath = !StringUtils.isAllBlank(new CharSequence[] {code, filePath});
        boolean hasCatalogAndVersion = !StringUtils.isAnyBlank(new CharSequence[] {catalog, version});
        return (hasCodeAndPath && hasCatalogAndVersion);
    }


    protected Map<ImpexHeaderValue, Object> createMediaRow(Map<String, String> params)
    {
        Map<ImpexHeaderValue, Object> row = new HashMap<>();
        row.put(createMediaCodeHeader(), getCode(params));
        row.put(createMediaCatalogVersionHeader(), catalogVersionData(params));
        String filePath = params.get("filePath");
        if(StringUtils.isNotBlank(filePath))
        {
            row.put(createMediaContentHeader(), filePath);
        }
        row.put(createMediaFolderHeader(), this.mediaFolderProvider.provide(params));
        return row;
    }


    protected ImpexHeaderValue createMediaCodeHeader()
    {
        return (new ImpexHeaderValue.Builder("code")).withUnique(true).build();
    }


    protected String getCode(Map<String, String> params)
    {
        String code = params.get("code");
        return StringUtils.isNotBlank(code) ? code : this.mediaCodeGenerator.generate().toString();
    }


    protected ImpexHeaderValue createMediaCatalogVersionHeader()
    {
        return (new ImpexHeaderValue.Builder("catalogVersion(version,catalog(id))")).withUnique(true).build();
    }


    protected String catalogVersionData(Map<String, String> params)
    {
        String version = params.get("version");
        String catalog = params.get("catalog");
        return (StringUtils.isNotBlank(version) && StringUtils.isNotBlank(catalog)) ? String.format("%s:%s", new Object[] {version, catalog}) : null;
    }


    protected ImpexHeaderValue createMediaFolderHeader()
    {
        return (new ImpexHeaderValue.Builder(String.format("%s(%s)", new Object[] {"folder", "qualifier"}))).build();
    }


    protected ImpexHeaderValue createMediaContentHeader()
    {
        return (new ImpexHeaderValue.Builder("@media")).withTranslator(MediaDataTranslator.class.getName()).build();
    }


    protected String createMediaValue(Map<String, String> params)
    {
        return String.format("%s:%s:%s", new Object[] {params.get("code"), params.get("version"), params
                        .get("catalog")});
    }


    protected ImpexHeaderValue createMediaHeader(ExcelClassificationAttribute attribute)
    {
        ClassificationSystemVersionModel catalogVersion = attribute.getAttributeAssignment().getClassificationClass().getCatalogVersion();
        if(StringUtils.isBlank(attribute.getIsoCode()))
        {
            return (new ImpexHeaderValue.Builder(String.format("@%s(code,catalogVersion(version, catalog(id)))[system='%s',attributeType='null',version='%s',translator=%s]", new Object[] {attribute
                            .getQualifier(), catalogVersion.getCatalog().getId(), catalogVersion.getVersion(), ClassificationAttributeTranslator.class
                            .getCanonicalName()}))).withQualifier(attribute.getQualifier()).build();
        }
        return (new ImpexHeaderValue.Builder(String.format("@%s(code,catalogVersion(version, catalog(id)))[system='%s',attributeType='null',version='%s',lang=%s,translator=%s]", new Object[] {attribute
                        .getQualifier(), catalogVersion.getCatalog().getId(), catalogVersion.getVersion(), attribute.getIsoCode(), ClassificationAttributeTranslator.class
                        .getCanonicalName()}))).withQualifier(attribute.getQualifier()).build();
    }


    @Nullable
    protected ImpexValue importSingle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters, @Nonnull ExcelImportContext excelImportContext)
    {
        return null;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setMediaCodeGenerator(KeyGenerator mediaCodeGenerator)
    {
        this.mediaCodeGenerator = mediaCodeGenerator;
    }


    @Required
    public void setMediaFolderProvider(MediaFolderProvider mediaFolderProvider)
    {
        this.mediaFolderProvider = mediaFolderProvider;
    }


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }


    public void setExportUrl(boolean exportUrl)
    {
        this.exportUrl = exportUrl;
    }
}
