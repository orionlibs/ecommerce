package de.hybris.bootstrap.ddl.sql;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

interface TableStructureChangesProcessor
{
    Platform getPlatform();


    void processTableStructureChanges(Database paramDatabase1, Database paramDatabase2, Table paramTable1, Table paramTable2, Map paramMap, List paramList) throws IOException;
}
