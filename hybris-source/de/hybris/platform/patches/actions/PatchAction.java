package de.hybris.platform.patches.actions;

import de.hybris.platform.patches.actions.data.PatchActionData;

public interface PatchAction
{
    void perform(PatchActionData paramPatchActionData);


    default String getCustomizedName(PatchActionData data)
    {
        return data.getName();
    }
}
