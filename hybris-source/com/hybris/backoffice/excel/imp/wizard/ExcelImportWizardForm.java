/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.imp.wizard;

import com.hybris.cockpitng.editor.defaultfileupload.FileUploadResult;
import java.util.HashSet;
import java.util.Set;

/**
 * Pojo for excel import
 */
public class ExcelImportWizardForm
{
    private FileUploadResult excelFile;
    private FileUploadResult zipFile;
    private Set<FileUploadResult> fileUploadResult = new HashSet<>();


    public FileUploadResult getExcelFile()
    {
        return excelFile;
    }


    public void setExcelFile(final FileUploadResult excelFile)
    {
        this.excelFile = excelFile;
    }


    public FileUploadResult getZipFile()
    {
        return zipFile;
    }


    public void setZipFile(final FileUploadResult zipFile)
    {
        this.zipFile = zipFile;
    }


    /**
     * @deprecated since 6.7 use separate methods for zip and excel file
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public Set<FileUploadResult> getFileUploadResult()
    {
        return fileUploadResult;
    }


    /**
     * @deprecated since 6.7 use separate methods for zip {@link #getZipFile()} and excel file {@link #getExcelFile()}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public void setFileUploadResult(final Set<FileUploadResult> fileUploadResult)
    {
        this.fileUploadResult = fileUploadResult;
    }
}
