package de.hybris.platform.cockpit.services.reports;

import de.hybris.platform.cockpit.reports.model.CompiledJasperMediaModel;
import de.hybris.platform.cockpit.reports.model.JasperMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JasperReport;

public interface ReportsService
{
    JasperReport getMainReport(String paramString);


    Map<String, Object> getMainReportParameters(String paramString);


    JasperReport compileReport(JasperMediaModel paramJasperMediaModel);


    MediaModel createCompiledCounterpart(CompiledJasperMediaModel paramCompiledJasperMediaModel);


    List<JasperMediaModel> findJasperMediasByMediaFolder(String paramString);
}
