package de.hybris.platform.advancedsavedquery.systemsetup;

import de.hybris.platform.advancedsavedquery.jalo.ASQManager;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.impex.jalo.ImpExManager;
import java.io.InputStream;

@SystemSetup(extension = "advancedsavedquery")
public class ASQSystemSetup
{
    @SystemSetup(process = SystemSetup.Process.ALL, type = SystemSetup.Type.PROJECT)
    public void createProjectData()
    {
        InputStream inputstream = ASQManager.class.getResourceAsStream("/advancedsavedquery/AdvancedSavedQuery.csv");
        ImpExManager.getInstance().importData(inputstream, "windows-1252", ';', '"', true);
    }
}
