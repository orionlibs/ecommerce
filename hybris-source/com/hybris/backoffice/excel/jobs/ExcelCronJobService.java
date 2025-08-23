package com.hybris.backoffice.excel.jobs;

import com.hybris.backoffice.model.ExcelImportCronJobModel;

public interface ExcelCronJobService
{
    ExcelImportCronJobModel createImportJob(FileContent paramFileContent1, FileContent paramFileContent2);
}
