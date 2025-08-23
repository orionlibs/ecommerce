package de.hybris.platform.servicelayer.cronjob;

import de.hybris.platform.core.PK;

public interface RunCronJobMessageCreatorAndSender
{
    void createAndSendMessage(int paramInt, PK paramPK);
}
