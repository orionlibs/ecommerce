package de.hybris.platform.cockpit.reports;

import de.hybris.platform.cockpit.reports.exceptions.JasperReportLoadException;
import de.hybris.platform.cockpit.reports.model.CompiledJasperMediaModel;
import de.hybris.platform.cockpit.reports.model.JasperMediaModel;
import de.hybris.platform.cockpit.reports.model.JasperWidgetPreferencesModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.UserModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;

public interface JasperMediaService
{
    MediaFolderModel getJasperReportsMediaFolder();


    JasperMediaModel createJasperMedia(MediaFolderModel paramMediaFolderModel, String paramString);


    CompiledJasperMediaModel setCompiledCounterpartForMedia(CompiledJasperMediaModel paramCompiledJasperMediaModel) throws JRException;


    JasperReport getReportFromMedia(MediaModel paramMediaModel) throws JasperReportLoadException;


    JasperMediaModel getJasperMediaForCode(String paramString);


    JasperWidgetPreferencesModel createJasperWidgetPreferencesFromMedia(JasperReport paramJasperReport, JasperMediaModel paramJasperMediaModel, UserModel paramUserModel);
}
