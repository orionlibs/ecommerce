package de.hybris.platform.patches.data;

import org.apache.commons.lang.ArrayUtils;

public class ImpexImportUnit implements Cloneable
{
    private ImpexHeaderOption[] impexHeaderOptions;
    private ImpexHeaderFile[] impexHeaderFiles;
    private ImpexDataFile impexDataFile;
    private ImpexImportUnitOption[] impexImportUnitOptions;
    private String organisationUnitRepresentation;
    private String languageIsoCode;


    public ImpexImportUnit clone()
    {
        return clone(new ImpexImportUnit());
    }


    protected ImpexImportUnit clone(ImpexImportUnit clone)
    {
        if(this.impexDataFile != null)
        {
            clone.setImpexDataFile(this.impexDataFile.clone());
        }
        if(this.impexHeaderOptions != null)
        {
            int length = this.impexHeaderOptions.length;
            ImpexHeaderOption[] cloneImpexHeaderOptions = new ImpexHeaderOption[length];
            for(int i = 0; i < length; i++)
            {
                cloneImpexHeaderOptions[i] = this.impexHeaderOptions[i].clone();
            }
            clone.setImpexHeaderOptions(cloneImpexHeaderOptions);
        }
        if(this.impexImportUnitOptions != null)
        {
            int length = this.impexImportUnitOptions.length;
            ImpexImportUnitOption[] cloneImpexImportUnitOptions = new ImpexImportUnitOption[length];
            for(int i = 0; i < length; i++)
            {
                cloneImpexImportUnitOptions[i] = this.impexImportUnitOptions[i];
            }
            clone.setImpexImportUnitOptions(cloneImpexImportUnitOptions);
        }
        if(this.impexHeaderFiles != null)
        {
            int length = this.impexHeaderFiles.length;
            ImpexHeaderFile[] cloneImpexHeaderFiles = new ImpexHeaderFile[length];
            for(int i = 0; i < length; i++)
            {
                cloneImpexHeaderFiles[i] = this.impexHeaderFiles[i].clone();
            }
            clone.setImpexHeaderFiles(cloneImpexHeaderFiles);
        }
        return clone;
    }


    public ImpexHeaderOption[] getImpexHeaderOptions()
    {
        return this.impexHeaderOptions;
    }


    public void setImpexHeaderOptions(ImpexHeaderOption[] impexHeaderOptions)
    {
        this.impexHeaderOptions = (ImpexHeaderOption[])impexHeaderOptions.clone();
    }


    public ImpexHeaderFile[] getImpexHeaderFiles()
    {
        return this.impexHeaderFiles;
    }


    public void setImpexHeaderFiles(ImpexHeaderFile[] impexHeaderFiles)
    {
        this.impexHeaderFiles = (ImpexHeaderFile[])impexHeaderFiles.clone();
    }


    public ImpexDataFile getImpexDataFile()
    {
        return this.impexDataFile;
    }


    public void setImpexDataFile(ImpexDataFile impexDataFile)
    {
        this.impexDataFile = impexDataFile;
    }


    public ImpexImportUnitOption[] getImpexImportUnitOptions()
    {
        return this.impexImportUnitOptions;
    }


    public void setImpexImportUnitOptions(ImpexImportUnitOption[] impexImportUnitOptions)
    {
        this.impexImportUnitOptions = (ImpexImportUnitOption[])impexImportUnitOptions.clone();
    }


    public void addHeaderOption(ImpexHeaderOption headerOptionToAdd)
    {
        this.impexHeaderOptions = (ImpexHeaderOption[])ArrayUtils.add((Object[])this.impexHeaderOptions, headerOptionToAdd);
    }


    public void addHeaderFile(ImpexHeaderFile headerFileToAdd)
    {
        this.impexHeaderFiles = (ImpexHeaderFile[])ArrayUtils.add((Object[])this.impexHeaderFiles, headerFileToAdd);
    }


    public void addImpexImportUnitOption(ImpexImportUnitOption importUnitOptionToAdd)
    {
        this.impexImportUnitOptions = (ImpexImportUnitOption[])ArrayUtils.add((Object[])this.impexImportUnitOptions, importUnitOptionToAdd);
    }


    public String getOrganisationUnitRepresentation()
    {
        return this.organisationUnitRepresentation;
    }


    public void setOrganisationUnitRepresentation(String organisationUnitRepresentation)
    {
        this.organisationUnitRepresentation = organisationUnitRepresentation;
    }


    public void setLanguageIsoCode(String languageIsoCode)
    {
        this.languageIsoCode = languageIsoCode;
    }


    public String getLanguageIsoCode()
    {
        return this.languageIsoCode;
    }
}
