package de.hybris.platform.ordermanagementfacades.fraud.data;

import java.io.Serializable;
import java.util.List;

public class FraudReportDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<FraudReportData> reports;


    public void setReports(List<FraudReportData> reports)
    {
        this.reports = reports;
    }


    public List<FraudReportData> getReports()
    {
        return this.reports;
    }
}
