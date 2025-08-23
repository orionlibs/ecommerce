package de.hybris.platform.impex.distributed.batch;

public interface ImportBatchHandler
{
    public static final String IMPORT_BY_LINE_FLAG = "import.by.line";
    public static final String CODE_EXECUTION_FLAG = "code.execution";
    public static final String PROCESS_CODE = "process.code";
    public static final String SLD_ENABLED = "sld.enabled";


    String getInputData();


    void setOutputData(String paramString);


    void setRemainingWorkLoad(long paramLong);


    String getProperty(String paramString);


    void setProperty(String paramString1, String paramString2);
}
