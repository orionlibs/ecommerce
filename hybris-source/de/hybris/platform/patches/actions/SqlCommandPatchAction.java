package de.hybris.platform.patches.actions;

import de.hybris.platform.patches.actions.data.PatchActionData;
import de.hybris.platform.patches.actions.data.PatchActionDataOption;
import de.hybris.platform.patches.exceptions.PatchActionException;
import de.hybris.platform.patches.utils.DbUtils;

public class SqlCommandPatchAction implements PatchAction
{
    public void perform(PatchActionData data)
    {
        try
        {
            DbUtils.executeUpdate(data.getStringOption((PatchActionDataOption)PatchActionDataOption.Sql.QUERY));
        }
        catch(IllegalArgumentException ex)
        {
            throw new PatchActionException(ex);
        }
    }
}
