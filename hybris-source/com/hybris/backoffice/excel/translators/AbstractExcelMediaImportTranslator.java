package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import com.hybris.backoffice.excel.translators.media.MediaFolderProvider;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.impex.jalo.media.MediaDataTranslator;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractExcelMediaImportTranslator<T> extends AbstractCatalogVersionAwareTranslator<T>
{
    public static final String PARAM_FILE_PATH = "filePath";
    public static final String PARAM_FOLDER = "folder";
    public static final String PARAM_CODE = "code";
    protected static final String MEDIA_CONTENT_HEADER_NAME = "@media";
    private KeyGenerator mediaCodeGenerator;
    private ExcelFilter<AttributeDescriptorModel> mandatoryFilter;
    private MediaFolderProvider mediaFolderProvider;


    public String referenceFormat(AttributeDescriptorModel attributeDescriptor)
    {
        return "filePath:code:" + referenceCatalogVersionFormat() + ":folder";
    }


    public Optional<String> exportMedia(MediaModel media)
    {
        return (media != null) ? Optional.<String>of(String.format(":%s:%s:%s", new Object[] {media.getCode(),
                        exportCatalogVersionData(media.getCatalogVersion()), media.getFolder().getQualifier()})) : Optional.<String>empty();
    }


    protected Map<ImpexHeaderValue, Object> createMediaRow(AttributeDescriptorModel attributeDescriptor, String mediaRefId, Map<String, String> params)
    {
        Map<ImpexHeaderValue, Object> row = new HashMap<>();
        row.put(createMediaReferenceIdHeader(attributeDescriptor, params), mediaRefId);
        row.put(createMediaCodeHeader(attributeDescriptor, params), getCode(attributeDescriptor, params));
        row.put(createMediaCatalogVersionHeader(attributeDescriptor, params), catalogVersionData(params));
        if(StringUtils.isNotEmpty(getFolder(attributeDescriptor, params)))
        {
            row.put(createMediaFolderHeader(attributeDescriptor, params), getFolder(attributeDescriptor, params));
        }
        String filePath = getFilePath(attributeDescriptor, params);
        if(StringUtils.isNotBlank(filePath))
        {
            row.put(createMediaContentHeader(attributeDescriptor, params), filePath);
        }
        return row;
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


    protected String generateMediaRefId(AttributeDescriptorModel attributedescriptor, Map<String, String> params)
    {
        if(params != null)
        {
            String code = params.get("code");
            if(StringUtils.isNotBlank(code))
            {
                String catalog = params.get("catalog");
                String version = params.get("version");
                String folder = getFolder(attributedescriptor, params);
                String combinedValue = String.format("%s_%s_%s_%s_%s", new Object[] {AbstractExcelMediaImportTranslator.class.getSimpleName(), code, catalog, version, folder});
                return Base64.getEncoder().encodeToString(combinedValue.getBytes(StandardCharsets.UTF_8));
            }
        }
        return UUID.randomUUID().toString();
    }


    protected void addReferencedMedia(ImpexForType impexForType, AttributeDescriptorModel attributeDescriptor, Collection<String> mediaRefs)
    {
        addReferencedMedia(impexForType, attributeDescriptor, String.join(",", (Iterable)mediaRefs));
    }


    protected void addReferencedMedia(ImpexForType impexForType, AttributeDescriptorModel attributeDescriptor, String mediaRef)
    {
        addReferencedMedia(impexForType, attributeDescriptor, mediaRef, null);
    }


    protected void addReferencedMedia(ImpexForType impexForType, AttributeDescriptorModel attributeDescriptor, String mediaRef, String isoCode)
    {
        ImpexHeaderValue mediaHeader = StringUtils.isBlank(isoCode) ? createReferenceHeader(attributeDescriptor) : createReferenceHeader(attributeDescriptor, isoCode);
        impexForType.putValue(Integer.valueOf(0), mediaHeader, mediaRef);
    }


    protected ImpexHeaderValue createReferenceHeader(AttributeDescriptorModel attributeDescriptor)
    {
        return (new ImpexHeaderValue.Builder(
                        String.format("%s(%s)", new Object[] {attributeDescriptor.getQualifier(), "&ExcelImportRef"}))).withQualifier(attributeDescriptor.getQualifier()).build();
    }


    protected ImpexHeaderValue createReferenceHeader(AttributeDescriptorModel attributeDescriptor, String isoCode)
    {
        return (new ImpexHeaderValue.Builder(
                        String.format("%s(%s)", new Object[] {attributeDescriptor.getQualifier(), "&ExcelImportRef"}))).withQualifier(attributeDescriptor.getQualifier()).withLang(isoCode).build();
    }


    protected ImpexHeaderValue createMediaContentHeader(AttributeDescriptorModel attributeDescriptor, Map<String, String> params)
    {
        return (new ImpexHeaderValue.Builder("@media")).withTranslator(MediaDataTranslator.class.getName()).build();
    }


    protected ImpexHeaderValue createMediaCatalogVersionHeader(AttributeDescriptorModel attributeDescriptor, Map<String, String> params)
    {
        return (new ImpexHeaderValue.Builder(catalogVersionHeader("Media"))).withUnique(true)
                        .withMandatory(this.mandatoryFilter.test(attributeDescriptor)).build();
    }


    protected ImpexHeaderValue createMediaCodeHeader(AttributeDescriptorModel attributeDescriptor, Map<String, String> params)
    {
        return (new ImpexHeaderValue.Builder("code")).withUnique(true)
                        .withMandatory(this.mandatoryFilter.test(attributeDescriptor)).build();
    }


    protected ImpexHeaderValue createMediaReferenceIdHeader(AttributeDescriptorModel attributeDescriptor, Map<String, String> params)
    {
        return (new ImpexHeaderValue.Builder("&ExcelImportRef")).build();
    }


    protected ImpexHeaderValue createMediaFolderHeader(AttributeDescriptorModel attributeDescriptor, Map<String, String> params)
    {
        return (new ImpexHeaderValue.Builder(String.format("%s(%s)", new Object[] {"folder", "qualifier"}))).build();
    }


    protected String getCode(AttributeDescriptorModel attributeDescriptor, Map<String, String> params)
    {
        String code = params.get("code");
        return StringUtils.isNotBlank(code) ? code : this.mediaCodeGenerator.generate().toString();
    }


    protected String getFilePath(AttributeDescriptorModel attributeDescriptor, Map<String, String> params)
    {
        return params.get("filePath");
    }


    protected String getFolder(AttributeDescriptorModel attributeDescriptor, Map<String, String> params)
    {
        return this.mediaFolderProvider.provide(params);
    }


    public ImpexValue importValue(AttributeDescriptorModel attributeDescriptor, ImportParameters importParameters)
    {
        throw new UnsupportedOperationException("Sub class should override importData method");
    }


    public Impex importData(AttributeDescriptorModel attributeDescriptor, ImportParameters importParameters)
    {
        throw new UnsupportedOperationException("Sub class should override importData method");
    }


    public KeyGenerator getMediaCodeGenerator()
    {
        return this.mediaCodeGenerator;
    }


    @Required
    public void setMediaCodeGenerator(KeyGenerator mediaCodeGenerator)
    {
        this.mediaCodeGenerator = mediaCodeGenerator;
    }


    public ExcelFilter<AttributeDescriptorModel> getMandatoryFilter()
    {
        return this.mandatoryFilter;
    }


    @Required
    public void setMandatoryFilter(ExcelFilter<AttributeDescriptorModel> mandatoryFilter)
    {
        this.mandatoryFilter = mandatoryFilter;
    }


    @Required
    public void setMediaFolderProvider(MediaFolderProvider mediaFolderProvider)
    {
        this.mediaFolderProvider = mediaFolderProvider;
    }
}
