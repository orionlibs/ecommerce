package de.hybris.platform.masterserver.collector.system.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.hybris.statistics.collector.SystemStatisticsCollector;
import com.sap.security.core.server.likey.LicenseChecker;
import com.sap.security.core.server.likey.LicenseKey;
import com.sap.security.core.server.likey.LogAndTrace;
import com.sap.security.core.server.likey.Persistence;
import de.hybris.platform.licence.sap.DefaultKeySystem;
import de.hybris.platform.licence.sap.DefaultPersistence;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class SAPLicenseStatisticsCollector implements SystemStatisticsCollector<Map<String, List<Map<String, String>>>>
{
    private final String systemId;
    private final String hwId;


    public SAPLicenseStatisticsCollector()
    {
        DefaultKeySystem keySystem = new DefaultKeySystem();
        this.systemId = keySystem.getSystemId();
        this.hwId = keySystem.getHwId();
    }


    public Map<String, List<Map<String, String>>> collectStatistics()
    {
        List<Map<String, String>> result = new ArrayList<>();
        for(LicenseKey licenseKey : getAllLicenses())
        {
            result.add(getLicenseData(licenseKey));
        }
        return (Map<String, List<Map<String, String>>>)ImmutableMap.builder().put("sap licenses", result).build();
    }


    private List<LicenseKey> getAllLicenses()
    {
        Vector allLicenses = LicenseChecker.getAllLicenses(getPersistence(), (LogAndTrace)new Object(this));
        return (allLicenses.size() == 0) ? Collections.<LicenseKey>emptyList() : Lists.newArrayList(allLicenses);
    }


    Persistence getPersistence()
    {
        return (Persistence)new DefaultPersistence();
    }


    private Map<String, String> getLicenseData(LicenseKey licenseKey)
    {
        Map<String, String> result = new HashMap<>();
        result.put("System", licenseKey.getSystemId());
        result.put("Hardware Key", licenseKey.getHwKey());
        result.put("SW Product", licenseKey.getSwProduct());
        result.put("SW Product Limit", licenseKey.getSwProductLimit());
        result.put("Begin date", (licenseKey.getBeginDate() == null) ? "no begin date" : licenseKey.getBeginDate());
        result.put("Expiration date", licenseKey.getEndDate());
        result.put("License key type", licenseKey.getType());
        result.put("System No.", licenseKey.getSysNo());
        result.put("Validity", validateLicenseAndGetResult(licenseKey));
        result.put("Installation Number", licenseKey.getInstNo());
        return result;
    }


    private String validateLicenseAndGetResult(LicenseKey licenseKey)
    {
        boolean result = licenseKey.preCheck(this.systemId, this.hwId);
        if(result)
        {
            result = licenseKey.check();
        }
        return result ? "valid" : ("invalid (" + licenseKey.getCheckReason() + ")");
    }
}
