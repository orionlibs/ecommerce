package de.hybris.platform.ruleengineservices.setup.tasks;

import de.hybris.platform.core.initialization.SystemSetupContext;

public interface MigrationTask
{
    void execute(SystemSetupContext paramSystemSetupContext);
}
