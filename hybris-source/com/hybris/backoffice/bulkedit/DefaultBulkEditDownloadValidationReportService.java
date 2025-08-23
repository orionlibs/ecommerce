/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.bulkedit;

import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.Filedownload;

public class DefaultBulkEditDownloadValidationReportService implements BulkEditDownloadValidationReportService
{
    private LabelService labelService;


    @Override
    public void downloadReport(final List<ValidationResult> validationResults)
    {
        final String fileContent = generateValidationContent(validationResults);
        triggerDownload(fileContent);
    }


    protected String generateValidationContent(final List<ValidationResult> validationResults)
    {
        final StringBuilder sb = new StringBuilder();
        validationResults.forEach(validationResult -> generateValidationRow(sb, validationResult));
        return sb.toString();
    }


    protected void generateValidationRow(final StringBuilder sb, final ValidationResult validationResult)
    {
        generateValidationRowHeader(sb, validationResult);
        for(final ValidationInfo validationInfo : validationResult.getValidationInfos())
        {
            generateValidationError(sb, validationInfo);
        }
        appendNewLine(sb);
    }


    protected void generateValidationRowHeader(final StringBuilder sb, final ValidationResult validationResult)
    {
        sb.append(labelService.getShortObjectLabel(validationResult.getItem()));
        appendNewLine(sb);
    }


    public void generateValidationError(final StringBuilder sb, final ValidationInfo validationInfo)
    {
        sb.append("\t" + createValidationMessage(validationInfo));
        appendNewLine(sb);
    }


    protected String createValidationMessage(final ValidationInfo validationInfo)
    {
        final String notLocalizedPath = ObjectValuePath.getNotLocalizedPath(validationInfo.getInvalidPropertyPath());
        final String attributeName = labelService.getObjectLabel(notLocalizedPath);
        final String locale = ObjectValuePath.getLocaleFromPath(validationInfo.getInvalidPropertyPath());
        final String attributeLabel = locale != null ? String.format("%s[%s]", attributeName, locale) : attributeName;
        return String.format("[%s]: %s", attributeLabel, validationInfo.getValidationMessage());
    }


    protected void appendNewLine(final StringBuilder sb)
    {
        sb.append(System.lineSeparator());
    }


    protected void triggerDownload(final String fileContent)
    {
        Filedownload.save(fileContent, MediaType.TEXT_PLAIN, "bulk-edit-validation-report.txt");
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }
}
