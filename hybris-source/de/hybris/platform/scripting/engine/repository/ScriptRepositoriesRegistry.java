package de.hybris.platform.scripting.engine.repository;

public interface ScriptRepositoriesRegistry
{
    ScriptsRepository getRepositoryByProtocol(String paramString);
}
