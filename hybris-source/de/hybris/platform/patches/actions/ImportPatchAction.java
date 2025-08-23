package de.hybris.platform.patches.actions;

import de.hybris.platform.patches.Patch;
import de.hybris.platform.patches.actions.data.PatchActionData;
import de.hybris.platform.patches.actions.data.PatchActionDataOption;
import de.hybris.platform.patches.data.ImpexDataFile;
import de.hybris.platform.patches.data.ImpexHeaderFile;
import de.hybris.platform.patches.data.ImpexHeaderOption;
import de.hybris.platform.patches.data.ImpexImportPack;
import de.hybris.platform.patches.data.ImpexImportUnit;
import de.hybris.platform.patches.data.ImpexImportUnitOption;
import de.hybris.platform.patches.data.ImpexImportUnitResult;
import de.hybris.platform.patches.exceptions.PatchImportException;
import de.hybris.platform.patches.internal.logger.PatchLogger;
import de.hybris.platform.patches.internal.logger.PatchLoggerFactory;
import de.hybris.platform.patches.organisation.ImportLanguage;
import de.hybris.platform.patches.organisation.ImportOrganisationUnit;
import de.hybris.platform.patches.service.PatchImportService;
import de.hybris.platform.patches.utils.PatchExecutionUnitUtils;
import de.hybris.platform.patches.utils.PermutationUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ImportPatchAction extends AbstractImportPatchAction implements PatchAction
{
    private static final PatchLogger LOG = PatchLoggerFactory.getLogger(ImportPatchAction.class);
    private static final String IMPEX_FILES_PREFIX = "r";
    private static final String IMPEX_FILE_SUFFIX = ".impex";
    private static final String PATH_SEPARATOR = "/";
    private PatchImportService patchImportService;


    public void perform(PatchActionData data)
    {
        ImpexImportPack basePack;
        String fileName = getFileName(data);
        Collection<ImportLanguage> languages = getImportLanguages(data);
        if(isRunAgain(data) && languages == null)
        {
            LOG.debug("There are no new languages defined for already imported file '{}': skip entry.", fileName);
            return;
        }
        LOG.info(PatchLogger.LoggingMode.HAC_CONSOLE, "Executing import: {}...", fileName);
        if(!isValidImpexFileSuffix(fileName))
        {
            LOG.error(PatchLogger.LoggingMode.CONSOLE, "Incorrect suffix for file: '{}'", fileName);
            LOG.error(PatchLogger.LoggingMode.HAC, "Executing import: {} failed", fileName);
            return;
        }
        Patch patch = data.getPatch();
        String basePath = getBasePath(patch);
        String filePrefix = "r" + patch.getPatchName();
        List<ImpexImportPack> importPackList = new ArrayList<>();
        ImportOrganisationUnit organisationUnit = data.getOrganisationUnit();
        if(organisationUnit == null)
        {
            basePack = createGlobalPack(basePath, fileName, filePrefix);
        }
        else
        {
            basePack = createOrganisationPacks(basePath, fileName, filePrefix, organisationUnit);
        }
        if(!isRunAgain(data))
        {
            importPackList.add(basePack);
        }
        else
        {
            LOG.info(PatchLogger.LoggingMode.CONSOLE, "'{}' was already imported therefore only language specific impexes will be imported.", fileName);
        }
        importPackList.addAll(createLanguagesVariantsForUnit(basePack, languages));
        setOrganisationUnitOnImportUnit(importPackList, organisationUnit);
        importPackList = useHeaderOptions(importPackList, getHeaderOptions(data));
        updateImpexOptions(importPackList, data);
        if(!importPacks(importPackList, getFileName(data)))
        {
            LOG.error(PatchLogger.LoggingMode.HAC_CONSOLE, "Executing import: {} failed", fileName);
        }
    }


    protected void updateImpexOptions(List<ImpexImportPack> importPackList, PatchActionData data)
    {
        ImpexImportUnitOption[] impexOptions = getImportOptions(data);
        if(ArrayUtils.isEmpty((Object[])impexOptions))
        {
            return;
        }
        for(ImpexImportPack importPack : importPackList)
        {
            for(ImpexImportUnit unit : importPack.getImportUnits())
            {
                unit.setImpexImportUnitOptions(impexOptions);
            }
        }
    }


    private void setOrganisationUnitOnImportUnit(List<ImpexImportPack> importPackList, ImportOrganisationUnit organisationUnit)
    {
        String organisationUnitRepresentation = PatchExecutionUnitUtils.generateOrganisationUnitName(organisationUnit);
        for(ImpexImportPack importPack : importPackList)
        {
            for(ImpexImportUnit unit : importPack.getImportUnits())
            {
                unit.setOrganisationUnitRepresentation(organisationUnitRepresentation);
            }
        }
    }


    protected boolean importPacks(List<ImpexImportPack> importPacks, String fileName)
    {
        int iteration = 0;
        boolean success = true;
        for(ImpexImportPack pack : importPacks)
        {
            ImpexImportUnit importUnit = pack.getImportUnits().get(0);
            if(StringUtils.isNotBlank(importUnit.getLanguageIsoCode()))
            {
                LOG.info(PatchLogger.LoggingMode.CONSOLE, "Executing pack {} of {} for: {} and Language {}", new Object[] {Integer.valueOf(++iteration),
                                Integer.valueOf(importPacks.size()), fileName, importUnit.getLanguageIsoCode()});
            }
            else
            {
                LOG.info(PatchLogger.LoggingMode.CONSOLE, "Executing pack {} of {} for: {}", new Object[] {Integer.valueOf(++iteration),
                                Integer.valueOf(importPacks.size()), fileName});
            }
            success = (success && importPackUnits(pack));
        }
        return success;
    }


    private boolean importPackUnits(ImpexImportPack pack)
    {
        boolean success = true;
        boolean anyExecuted = false;
        String filePath = null;
        String languageIsoCode = null;
        List<String> errors = new LinkedList<>();
        List<String> skipped = new LinkedList<>();
        try
        {
            for(ImpexImportUnit unit : pack.getImportUnits())
            {
                String unitPath = unit.getImpexDataFile().getFilePath();
                if(getClass().getResource(unitPath) == null)
                {
                    skipped.add(unitPath);
                    continue;
                }
                anyExecuted = true;
                try
                {
                    ImpexImportUnitResult result = this.patchImportService.importImpexUnit(unit);
                    if(result.getImportResult().isSuccessful())
                    {
                        filePath = unitPath;
                        languageIsoCode = unit.getLanguageIsoCode();
                        break;
                    }
                    if(!pack.isOptional())
                    {
                        success = false;
                    }
                    if(result.getImportResult().hasUnresolvedLines())
                    {
                        errors.add(unitPath + " - unresolved lines.");
                        break;
                    }
                    errors.add(unitPath);
                    break;
                }
                catch(PatchImportException e)
                {
                    errors.add(e.getMessage());
                    success = false;
                }
            }
            if(shouldFailedImportBeTracked(pack, anyExecuted))
            {
                try
                {
                    this.patchImportService.importImpexUnit(pack.getImportUnits().get(0));
                }
                catch(PatchImportException e)
                {
                    errors.add(e.getMessage());
                    success = false;
                }
            }
        }
        catch(Throwable ex)
        {
            errors.add(ex.getMessage());
            throw ex;
        }
        finally
        {
            logPackageImport(pack, errors, skipped, filePath, languageIsoCode);
        }
        return success;
    }


    private boolean shouldFailedImportBeTracked(ImpexImportPack pack, boolean anyExecuted)
    {
        return (!anyExecuted && !pack.isOptional() && CollectionUtils.isNotEmpty(pack.getImportUnits()));
    }


    private void logPackageImport(ImpexImportPack pack, List<String> notImported, List<String> skipped, String filePath, String language)
    {
        try
        {
            if(filePath == null)
            {
                ImpexImportUnit importUnit = pack.getImportUnits().get(0);
                String unitFileName = importUnit.getImpexDataFile().getName();
                String unitFilePath = importUnit.getImpexDataFile().getFilePath();
                String unitLanguage = importUnit.getLanguageIsoCode();
                if(pack.isOptional())
                {
                    if(!skipped.contains(unitFilePath))
                    {
                        if(StringUtils.isNotBlank(unitLanguage))
                        {
                            LOG.error(PatchLogger.LoggingMode.CONSOLE, "##### Optional pack {} for language {} not imported successfully", unitFileName, unitLanguage);
                        }
                        else
                        {
                            LOG.error(PatchLogger.LoggingMode.CONSOLE, "##### Optional pack {} not imported successfully", unitFileName);
                        }
                    }
                }
                else if(StringUtils.isNotBlank(unitLanguage))
                {
                    LOG.error(PatchLogger.LoggingMode.CONSOLE, "##### Pack {} for language {} import failed", unitFileName, unitLanguage);
                }
                else
                {
                    LOG.error(PatchLogger.LoggingMode.CONSOLE, "##### Pack {} import failed", unitFileName);
                }
            }
            else if(StringUtils.isNotBlank(language))
            {
                LOG.info(PatchLogger.LoggingMode.CONSOLE, "##### Pack {} for language {} imported successfully", filePath, language);
            }
            else
            {
                LOG.info(PatchLogger.LoggingMode.CONSOLE, "##### Pack {} imported successfully", filePath);
            }
            logFilesList("Files skipped:", skipped);
            logFilesList("Files not imported:", notImported);
        }
        catch(Throwable e)
        {
            LOG.error(PatchLogger.LoggingMode.CONSOLE, "There was a problem during logging result of ImpexImportPack", e);
        }
    }


    private void logFilesList(String header, List<String> fileNames)
    {
        if(CollectionUtils.isEmpty(fileNames))
        {
            return;
        }
        StringBuilder builder = new StringBuilder(header);
        for(String message : fileNames)
        {
            builder.append('\n');
            builder.append(message);
        }
        LOG.debug(builder.toString());
    }


    protected List<ImpexImportPack> useHeaderOptions(List<ImpexImportPack> importPacks, ImpexHeaderOption[][] dataHeaderOptions)
    {
        if(dataHeaderOptions == null)
        {
            return importPacks;
        }
        ImpexHeaderOption[][] headerOptionsPermutations = getHeaderOptionsPermutations(dataHeaderOptions);
        List<ImpexImportPack> newImportPacks = new ArrayList<>(importPacks.size() * headerOptionsPermutations.length);
        for(ImpexHeaderOption[] headerOptions : headerOptionsPermutations)
        {
            for(ImpexImportPack pack : importPacks)
            {
                ImpexImportPack clone = pack.clone();
                for(ImpexImportUnit unit : clone.getImportUnits())
                {
                    updatedHeaderOptions(unit, headerOptions);
                }
                newImportPacks.add(clone);
            }
        }
        return newImportPacks;
    }


    protected ImpexHeaderOption[][] getHeaderOptionsPermutations(ImpexHeaderOption[][] headerOptions)
    {
        return PermutationUtils.permutate(headerOptions);
    }


    protected ImpexImportPack createGlobalPack(String basePath, String fileName, String filePrefix)
    {
        ImpexImportPack basePack = new ImpexImportPack();
        ImpexImportUnit unit = createGlobalUnit(basePath, fileName, filePrefix);
        basePack.addImportUnit(unit);
        return basePack;
    }


    protected ImpexImportPack createOrganisationPacks(String basePath, String fileName, String filePrefix, ImportOrganisationUnit unit)
    {
        List<ImportOrganisationUnit> hierarchy = createUnitHierarchy(unit);
        List<String> paths = createPaths(hierarchy, basePath, fileName);
        ImpexHeaderFile header = new ImpexHeaderFile();
        String headerPath = basePath;
        for(ImportOrganisationUnit parent : hierarchy)
        {
            headerPath = headerPath + "/" + headerPath + "/" + parent.getFolderName();
        }
        header.setName(filePrefix + "_" + filePrefix + "_header.impex");
        header.setFilePath(headerPath + "/" + headerPath + "_" + filePrefix + "_header.impex");
        return createUnitForPath(fileName, paths, header);
    }


    private ImpexImportPack createUnitForPath(String fileName, List<String> paths, ImpexHeaderFile header)
    {
        ImpexImportPack basePack = new ImpexImportPack();
        for(String currentPath : paths)
        {
            ImpexImportUnit importUnit = new ImpexImportUnit();
            ImpexDataFile data = new ImpexDataFile();
            data.setFilePath(currentPath);
            data.setName(fileName);
            importUnit.setImpexDataFile(data);
            importUnit.setImpexHeaderFiles(new ImpexHeaderFile[] {header});
            basePack.addImportUnit(importUnit);
        }
        return basePack;
    }


    protected List<String> createPaths(List<ImportOrganisationUnit> hierarchy, String basePath, String fileName)
    {
        List<String> paths = new ArrayList<>();
        int parentSize = hierarchy.size();
        for(int i = 0; i < parentSize + 1; i++)
        {
            int j = parentSize;
            String currentPath = basePath;
            for(ImportOrganisationUnit parent : hierarchy)
            {
                currentPath = currentPath + "/" + currentPath + "/";
                if(j > i)
                {
                    currentPath = currentPath + currentPath;
                }
                else
                {
                    currentPath = currentPath + currentPath;
                }
                j--;
            }
            currentPath = currentPath + "/" + currentPath;
            paths.add(currentPath);
        }
        return paths;
    }


    protected List<ImportOrganisationUnit> createUnitHierarchy(ImportOrganisationUnit unit)
    {
        LinkedList<ImportOrganisationUnit> parents = new LinkedList<>();
        ImportOrganisationUnit parentUnit = unit.getParent();
        while(parentUnit != null)
        {
            parents.add(0, parentUnit);
            parentUnit = parentUnit.getParent();
        }
        parents.add(unit);
        return parents;
    }


    protected ImpexImportUnit createGlobalUnit(String basePath, String fileName, String filePrefix)
    {
        ImpexImportUnit unit = new ImpexImportUnit();
        ImpexDataFile dataFile = new ImpexDataFile();
        String globalPath = getGlobalPath();
        dataFile.setFilePath(basePath + basePath + "/" + globalPath);
        dataFile.setName(fileName);
        unit.setImpexDataFile(dataFile);
        ImpexHeaderFile[] headerFileArray = new ImpexHeaderFile[1];
        String headerFileName = filePrefix + "_globalHeader.impex";
        ImpexHeaderFile headerFile = new ImpexHeaderFile();
        headerFile.setFilePath(basePath + basePath + "/" + globalPath);
        headerFile.setName(headerFileName);
        headerFileArray[0] = headerFile;
        unit.setImpexHeaderFiles(headerFileArray);
        return unit;
    }


    protected List<ImpexImportPack> createLanguagesVariantsForUnit(ImpexImportPack pack, Collection<ImportLanguage> languages)
    {
        List<ImpexImportPack> result = new ArrayList<>();
        for(ImportLanguage lang : languages)
        {
            ImpexImportPack languagePack = new ImpexImportPack();
            languagePack.setOptional(true);
            languagePack.setImportUnits(new ArrayList());
            for(ImpexImportUnit unit : pack.getImportUnits())
            {
                languagePack.getImportUnits().addAll(createLanguageVariantsForUnit(unit, lang));
            }
            result.add(languagePack);
        }
        return result;
    }


    protected List<ImpexImportUnit> createLanguageVariantsForUnit(ImpexImportUnit unit, ImportLanguage language)
    {
        List<ImpexImportUnit> languageUnitList = new ArrayList<>(2);
        ImpexImportUnit clone2 = unit.clone();
        updatedFilePathWithLanguage(clone2, language);
        updateHeaderOptionsWithLanguage(clone2, language);
        clone2.setLanguageIsoCode(language.getCode());
        languageUnitList.add(clone2);
        ImpexImportUnit clone1 = unit.clone();
        updatedHeaderOptionsWithLanguageAndAddLangFileSuffix(clone1, language);
        clone1.setLanguageIsoCode(language.getCode());
        languageUnitList.add(clone1);
        return languageUnitList;
    }


    private void updatedFilePathWithLanguage(ImpexImportUnit unit, ImportLanguage language)
    {
        updatedFilePathWithString(unit, language.getCode());
    }


    protected void updatedHeaderOptionsWithLanguageAndAddLangFileSuffix(ImpexImportUnit unit, ImportLanguage language)
    {
        updateHeaderOptionsWithLanguage(unit, language);
        updatedFilePathWithString(unit, "lang");
    }


    protected void updateHeaderOptionsWithLanguage(ImpexImportUnit unit, ImportLanguage language)
    {
        ImpexHeaderOption option = new ImpexHeaderOption();
        option.setMacro("$lang=" + language.getCode());
        updatedHeaderOptions(unit, option);
    }


    void updatedFilePathWithString(ImpexImportUnit unit, String suffix)
    {
        ImpexDataFile file = unit.getImpexDataFile();
        String filePath = file.getFilePath();
        filePath = StringUtils.removeEnd(filePath, ".impex");
        filePath = StringUtils.join((Object[])new String[] {filePath, "_", suffix, ".impex"});
        file.setFilePath(filePath);
    }


    void updatedHeaderOptions(ImpexImportUnit unit, ImpexHeaderOption option)
    {
        ImpexHeaderOption[] newOptions, options = unit.getImpexHeaderOptions();
        if(options == null)
        {
            newOptions = new ImpexHeaderOption[] {option};
        }
        else
        {
            int length = options.length;
            newOptions = Arrays.<ImpexHeaderOption>copyOf(options, length + 1);
            newOptions[length] = option;
        }
        unit.setImpexHeaderOptions(newOptions);
    }


    void updatedHeaderOptions(ImpexImportUnit unit, ImpexHeaderOption[] headerOptions)
    {
        ImpexHeaderOption[] originalOptions = unit.getImpexHeaderOptions();
        if(originalOptions == null)
        {
            unit.setImpexHeaderOptions(headerOptions);
        }
        else
        {
            int originalLength = originalOptions.length;
            int newLength = headerOptions.length;
            ImpexHeaderOption[] newOptions = Arrays.<ImpexHeaderOption>copyOf(originalOptions, originalLength + newLength);
            for(int counter = 0; counter < newLength; counter++)
            {
                newOptions[counter + originalLength] = headerOptions[counter];
            }
            unit.setImpexHeaderOptions(newOptions);
        }
    }


    boolean isValidImpexFileSuffix(String filePath)
    {
        return StringUtils.endsWith(filePath, ".impex");
    }


    private boolean isRunAgain(PatchActionData data)
    {
        return data.getBooleanOption((PatchActionDataOption)PatchActionDataOption.Impex.RUN_AGAIN).booleanValue();
    }


    private ImpexImportUnitOption[] getImportOptions(PatchActionData data)
    {
        return (ImpexImportUnitOption[])data.getOption((PatchActionDataOption)PatchActionDataOption.Impex.IMPORT_OPTIONS);
    }


    private String getFileName(PatchActionData data)
    {
        return data.getStringOption((PatchActionDataOption)PatchActionDataOption.Impex.FILE_NAME);
    }


    private Collection<ImportLanguage> getImportLanguages(PatchActionData data)
    {
        return (Collection<ImportLanguage>)data.getOption((PatchActionDataOption)PatchActionDataOption.Impex.IMPORT_LANGUAGES);
    }


    private ImpexHeaderOption[][] getHeaderOptions(PatchActionData data)
    {
        return (ImpexHeaderOption[][])data.getOption((PatchActionDataOption)PatchActionDataOption.Impex.HEADER_OPTIONS);
    }


    @Required
    public void setPatchImportService(PatchImportService patchImportService)
    {
        this.patchImportService = patchImportService;
    }
}
