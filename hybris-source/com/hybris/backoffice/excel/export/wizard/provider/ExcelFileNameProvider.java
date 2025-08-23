/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.export.wizard.provider;

import com.hybris.backoffice.excel.export.wizard.ExcelExportWizardForm;

/**
 * Provides excel file name which should be used during downloading file.
 */
public interface ExcelFileNameProvider
{
    /**
     * Provides excel file name.
     *
     * @param form
     *           wizard form
     * @return generated excel file name
     */
    String provide(ExcelExportWizardForm form);


    /**
     * Provides excel file name.
     *
     * @param typeCode
     *           typeCode of type to export
     * @return generated excel file name
     */
    String provide(String typeCode);
}
