package de.hybris.platform.cockpit.reports.factories;

import java.sql.Connection;

public interface JasperReportConnectionFactory
{
    Connection createConnection();


    Connection createConnection(String paramString1, String paramString2);


    Connection createConnection(String paramString1, String paramString2, String paramString3);
}
