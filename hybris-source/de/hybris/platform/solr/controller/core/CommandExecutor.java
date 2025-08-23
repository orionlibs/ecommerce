package de.hybris.platform.solr.controller.core;

import java.util.Collection;

public interface CommandExecutor
{
    CommandResult execute(Collection<CommandBuilder> paramCollection, CommandOption... paramVarArgs);
}
