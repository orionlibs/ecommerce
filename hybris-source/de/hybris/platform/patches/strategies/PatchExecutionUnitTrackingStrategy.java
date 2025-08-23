package de.hybris.platform.patches.strategies;

import de.hybris.platform.patches.actions.data.PatchActionData;
import de.hybris.platform.patches.model.PatchExecutionUnitModel;

public interface PatchExecutionUnitTrackingStrategy
{
    PatchExecutionUnitModel trackBeforePerform(PatchActionData paramPatchActionData);


    void trackAfterPerform(PatchExecutionUnitModel paramPatchExecutionUnitModel, PatchActionData paramPatchActionData, Throwable paramThrowable);
}
