package de.hybris.platform.patches;

import de.hybris.platform.patches.actions.PatchAction;
import de.hybris.platform.patches.actions.data.PatchActionData;
import de.hybris.platform.patches.actions.data.PatchActionDataOption;
import de.hybris.platform.patches.data.ImpexHeaderOption;
import de.hybris.platform.patches.data.ImpexImportUnitOption;
import de.hybris.platform.patches.organisation.ImportLanguage;
import de.hybris.platform.patches.organisation.ImportOrganisationUnit;
import de.hybris.platform.patches.organisation.StructureState;
import de.hybris.platform.util.Config;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractPatch implements Patch
{
    protected String patchId;
    protected String patchName;
    protected Release release;
    protected StructureState structureState;
    protected PatchAction importPatchAction;
    protected PatchAction sqlCommandPatchAction;


    public AbstractPatch(String patchId, String patchName, Release release, StructureState structureState)
    {
        this.patchId = patchId;
        this.patchName = patchName;
        this.release = release;
        this.structureState = structureState;
    }


    protected void importGlobalData(String fileName, Collection<ImportLanguage> languages, boolean runAgain)
    {
        importGlobalData(fileName, languages, runAgain, null);
    }


    protected void importGlobalData(String fileName, Collection<ImportLanguage> languages, boolean runAgain, ImpexImportUnitOption[] importOptions)
    {
        importData(fileName, null, languages, runAgain, importOptions, null);
    }


    protected void importData(String fileName, ImportOrganisationUnit organisationUnit, Collection<ImportLanguage> languages, boolean runAgain, ImpexImportUnitOption[] importOptions, ImpexHeaderOption[][] headerOptions)
    {
        PatchActionData data = new PatchActionData(fileName, this);
        data.setOrganisationUnit(organisationUnit);
        data.setOption((PatchActionDataOption)PatchActionDataOption.Impex.FILE_NAME, fileName);
        data.setOption((PatchActionDataOption)PatchActionDataOption.Impex.IMPORT_LANGUAGES, languages);
        data.setOption((PatchActionDataOption)PatchActionDataOption.Impex.RUN_AGAIN, Boolean.valueOf(runAgain));
        data.setOption((PatchActionDataOption)PatchActionDataOption.Impex.IMPORT_OPTIONS, importOptions);
        data.setOption((PatchActionDataOption)PatchActionDataOption.Impex.HEADER_OPTIONS, headerOptions);
        this.importPatchAction.perform(data);
    }


    protected void executeUpdateOnDB(String actionName, String query)
    {
        executeUpdateOnDB(actionName, query, null);
    }


    protected void executeUpdateOnDB(String actionName, String query, ImportOrganisationUnit organisationUnit)
    {
        PatchActionData data = new PatchActionData(actionName, this);
        data.setOption((PatchActionDataOption)PatchActionDataOption.Sql.QUERY, query);
        data.setOrganisationUnit(organisationUnit);
        this.sqlCommandPatchAction.perform(data);
    }


    public StructureState getStructureState()
    {
        return this.structureState;
    }


    public String getPatchId()
    {
        return this.patchId;
    }


    public String getPatchName()
    {
        return this.patchName;
    }


    public String getPatchDescription()
    {
        return Config.getString("wiki.patch." + this.patchId + ".url", "Look for patch information on the wiki!");
    }


    public Release getRelease()
    {
        return this.release;
    }


    @Required
    public void setImportPatchAction(PatchAction importPatchAction)
    {
        this.importPatchAction = importPatchAction;
    }


    @Required
    public void setSqlCommandPatchAction(PatchAction sqlCommandPatchAction)
    {
        this.sqlCommandPatchAction = sqlCommandPatchAction;
    }
}
