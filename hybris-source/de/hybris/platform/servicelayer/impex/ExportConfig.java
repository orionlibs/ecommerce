package de.hybris.platform.servicelayer.impex;

import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.util.CSVConstants;

public class ExportConfig
{
    private ImpExResource script;
    private ValidationMode validationMode = ValidationMode.STRICT;
    private boolean failOnError = true;
    private boolean synchronous = true;
    private char fieldSeparator = CSVConstants.DEFAULT_FIELD_SEPARATOR;
    private char quoteCharacter = CSVConstants.DEFAULT_QUOTE_CHARACTER;
    private char commentCharacter = '#';
    private String exportedDataCode;
    private String exportedMediaCode;
    private boolean singleFile = false;
    private String encoding;
    private EmployeeModel sessionUser;


    public ImpExResource getScript()
    {
        return this.script;
    }


    public void setScript(ImpExResource script)
    {
        this.script = script;
    }


    public ValidationMode getValidationMode()
    {
        return this.validationMode;
    }


    public boolean isFailOnError()
    {
        return this.failOnError;
    }


    public boolean isSynchronous()
    {
        return this.synchronous;
    }


    public char getFieldSeparator()
    {
        return this.fieldSeparator;
    }


    public char getQuoteCharacter()
    {
        return this.quoteCharacter;
    }


    public char getCommentCharacter()
    {
        return this.commentCharacter;
    }


    public boolean isSingleFile()
    {
        return this.singleFile;
    }


    public void setValidationMode(ValidationMode validationMode)
    {
        this.validationMode = validationMode;
    }


    public void setFailOnError(boolean failOnError)
    {
        this.failOnError = failOnError;
    }


    public void setSynchronous(boolean synchronous)
    {
        this.synchronous = synchronous;
    }


    public void setFieldSeparator(char fieldSeparator)
    {
        this.fieldSeparator = fieldSeparator;
    }


    public void setQuoteCharacter(char quoteCharacter)
    {
        this.quoteCharacter = quoteCharacter;
    }


    public void setCommentCharacter(char commentCharacter)
    {
        this.commentCharacter = commentCharacter;
    }


    public void setSingleFile(boolean singleFile)
    {
        this.singleFile = singleFile;
    }


    public String getEncoding()
    {
        return this.encoding;
    }


    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }


    public String getExportedDataCode()
    {
        return this.exportedDataCode;
    }


    public void setExportedDataCode(String exportedDataCode)
    {
        this.exportedDataCode = exportedDataCode;
    }


    public String getExportedMediaCode()
    {
        return this.exportedMediaCode;
    }


    public void setExportedMediaCode(String exportedMediaCode)
    {
        this.exportedMediaCode = exportedMediaCode;
    }


    public EmployeeModel getSessionUser()
    {
        return this.sessionUser;
    }


    public void setSessionUser(EmployeeModel sessionUser)
    {
        this.sessionUser = sessionUser;
    }
}
