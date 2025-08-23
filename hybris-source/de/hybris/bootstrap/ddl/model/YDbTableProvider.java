package de.hybris.bootstrap.ddl.model;

import java.util.Map;

public interface YDbTableProvider
{
    Map<String, YTable> getTables();
}
