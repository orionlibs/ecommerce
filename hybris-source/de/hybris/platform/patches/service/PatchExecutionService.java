package de.hybris.platform.patches.service;

import de.hybris.platform.patches.Patch;
import de.hybris.platform.patches.model.PatchExecutionModel;
import java.util.List;

public interface PatchExecutionService
{
    List<PatchExecutionModel> getExecutedPatches();


    PatchExecutionModel getPatchExecutionById(String paramString);


    PatchExecutionModel getLatestPatchExecutionById(String paramString);


    void registerPatchExecution(PatchExecutionModel paramPatchExecutionModel);


    void registerPatchExecutionError(String paramString);


    PatchExecutionModel createPatchExecution(Patch paramPatch);


    PatchExecutionModel getSessionPatchExecution();
}
