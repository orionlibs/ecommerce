/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.imp.wizard.renderer;

import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.validation.enums.Severity;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Li;
import org.zkoss.zul.Label;

/**
 * Default implementation of {@link ExcelValidationDetailsComponentFactory}
 */
public class DefaultExcelValidationDetailsComponentFactory implements ExcelValidationDetailsComponentFactory<Li>
{
    public static final String YW_EXCEL_VALIDATION_RESULT_DETAILS_CELL = "yw-excel-validation-result-row-details-cell";
    public static final String YW_EXCEL_VALIDATION_RESULT_DETAILS_CELL_COLUMN_HEADER = YW_EXCEL_VALIDATION_RESULT_DETAILS_CELL
                    + "-column-header";


    @Override
    public Li createValidationDetails(final Object columnHeader, final ValidationMessage validationMessage)
    {
        final Li li = new Li();
        li.setSclass(YW_EXCEL_VALIDATION_RESULT_DETAILS_CELL);
        if(columnHeader != null && !StringUtils.isBlank(columnHeader.toString()))
        {
            final Label columnHeaderLabel = new Label(String.format("[%s]: ", columnHeader));
            columnHeaderLabel.setSclass(getDetailsCellSclass(validationMessage.getSeverity()));
            li.appendChild(columnHeaderLabel);
        }
        final Label validationMessageLabel = new Label(String.format("%s", getMessageValue(validationMessage)));
        li.appendChild(validationMessageLabel);
        return li;
    }


    protected String getMessageValue(final ValidationMessage validationMessage)
    {
        final String localizedMessage = Labels.getLabel(validationMessage.getMessageKey(), validationMessage.getParams());
        return StringUtils.isNotBlank(localizedMessage) ? localizedMessage : validationMessage.getMessageKey();
    }


    protected String getDetailsCellSclass(final Severity severity)
    {
        return YW_EXCEL_VALIDATION_RESULT_DETAILS_CELL_COLUMN_HEADER + "-" + StringUtils.lowerCase(severity.getCode());
    }
}
