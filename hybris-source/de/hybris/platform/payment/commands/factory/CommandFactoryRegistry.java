package de.hybris.platform.payment.commands.factory;

import de.hybris.platform.payment.dto.BasicCardInfo;

public interface CommandFactoryRegistry
{
    CommandFactory getFactory(String paramString);


    CommandFactory getFactory(BasicCardInfo paramBasicCardInfo, boolean paramBoolean);
}
