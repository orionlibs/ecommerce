package de.hybris.platform.cockpit.components.editorarea.export;

import de.hybris.platform.util.Config;
import java.util.HashMap;
import java.util.Map;

public class ReportsConfiguration
{
    private static final String MAIN_REPORT_PREFERENCES_TITLE = Config.getParameter("cockpit.editor.area.main.report.preferences.title");
    private static final String EDITOR_AREA_MAIN_REPORT_NAME = Config.getParameter("cockpit.editor.area.main.report.name");
    private static final String EDITOR_AREA_MAIN_REPORT_FILE_NAME = Config.getParameter("cockpit.editor.area.main.report");
    private static final String MAIN_REPORT_PARAM_NAME = "MAIN_REPORT";
    private static final String MAIN_REPORT_DS_EXP_PARAM_NAME = Config.getParameter("cockpit.editor.area.main.report.ds.xpath.param.name");
    private static final String MAIN_REPORT_DS_EXP = Config.getParameter("cockpit.editor.area.main.report.ds.xpath");
    public static final String MAIN_REPORT_TITLE_PARAM_NAME = "MAIN_REPORT_TITLE";
    private static final String MAIN_REPORT_TITLE = Config.getParameter("cockpit.editor.area.main.report.title");
    private static final String EDITOR_AREA_SECTIONS_REPORT_NAME = Config.getParameter("cockpit.editor.area.sections.report.name");
    private static final String EDITOR_AREA_SECTIONS_REPORT_FILE_NAME = Config.getParameter("cockpit.editor.area.sections.report");
    private static final String SECTIONS_REPORT_PARAM_NAME = "SECTIONS_SUBREPORT";
    private static final String EDITOR_AREA_ROWS_REPORT_NAME = Config.getParameter("cockpit.editor.area.rows.report.name");
    private static final String EDITOR_AREA_ROWS_REPORT_FILE_NAME = Config.getParameter("cockpit.editor.area.rows.report");
    private static final String ROWS_REPORT_PARAM_NAME = "ROWS_SUBREPORT";
    private static final String EDITOR_AREA_MEDIA_REPORT_NAME = Config.getParameter("cockpit.editor.area.media.report.name");
    private static final String EDITOR_AREA_ATTRIBUTES_REPORT_NAME = Config.getParameter("cockpit.editor.area.attributes.report.name");
    private static final String EDITOR_AREA_MEDIA_REPORT_FILE_NAME = Config.getParameter("cockpit.editor.area.media.report");
    private static final String EDITOR_AREA_ATTRIBUTES_REPORT_FILE_NAME = Config.getParameter("cockpit.editor.area.attributes.report");
    private static final String MEDIA_REPORT_PARAM_NAME = "MEDIA_SUBREPORT";
    private static final String ATTRIBUTES_REPORT_PARAM_NAME = "ATTRIBUTES_SUBREPORT";
    private static final String EDITOR_AREA_TABLE_REPORT_NAME = Config.getParameter("cockpit.editor.area.table.report.name");
    private static final String EDITOR_AREA_TABLE_REPORT_FILE_NAME = Config.getParameter("cockpit.editor.area.table.report");
    private static final String TABLE_REPORT_PARAM_NAME = "TABLE_SUBREPORT";
    private static final String EDITOR_AREA_TABLE_COLUMNS_TITLES_REPORT_NAME = Config.getParameter("cockpit.editor.area.table.columns.titles.report.name");
    private static final String EDITOR_AREA_TABLE_COLUMNS_TITLES_REPORT_FILE_NAME = Config.getParameter("cockpit.editor.area.table.columns.titles.report");
    private static final String TABLE_COLUMNS_TITLES_PARAM_NAME = "TABLE_COLUMNS_TITLES_SUBREPORT";
    private static final String EDITOR_AREA_TABLE_ROWS_REPORT_NAME = Config.getParameter("cockpit.editor.area.table.rows.report.name");
    private static final String EDITOR_AREA_TABLE_ROWS_REPORT_FILE_NAME = Config.getParameter("cockpit.editor.area.table.rows.report");
    private static final String TABLE_ROWS_PARAM_NAME = "TABLE_ROWS_SUBREPORT";
    private static final String EDITOR_AREA_REFERENCE_TABLE_REPORT_NAME = Config.getParameter("cockpit.editor.area.reference.table.report.name");
    private static final String EDITOR_AREA_REFERENCE_TABLE_REPORT_FILE_NAME = Config.getParameter("cockpit.editor.area.reference.table.report");
    private static final String REFERENCE_TABLE_PARAM_NAME = "REFERENCE_TABLE_SUBREPORT";
    private static final String EDITOR_AREA_REFERENCE_TABLE_ROWS_REPORT_NAME = Config.getParameter("cockpit.editor.area.reference.table.rows.report.name");
    private static final String EDITOR_AREA_REFERENCE_TABLE_ROWS_REPORT_FILE_NAME = Config.getParameter("cockpit.editor.area.reference.table.rows.report");
    private static final String REFERENCE_TABLE_ROWS_PARAM_NAME = "REFERENCE_TABLE_ROWS_SUBREPORT";
    private static final String EDITOR_AREA_LOCALIZED_ROW_REPORT_NAME = Config.getParameter("cockpit.editor.area.localization.report.name");
    private static final String EDITOR_AREA_LOCALIZED_ROW_REPORT_FILE_NAME = Config.getParameter("cockpit.editor.area.localization.report");
    private static final String LOCALIZED_ROWS_PARAM_NAME = "LOCALIZED_ROW_SUBREPORT";
    private static final String LOCALIZED_ROWS_DS_EXP_PARAM_NAME = Config.getParameter("cockpit.editor.area.localization.ds.xpath.param.name");
    private static final String LOCALIZED_ROWS_DS_EXP = Config.getParameter("cockpit.editor.area.localization.ds.xpath");
    private static final String EDITOR_AREA_CUSTOM_SECTION_REPORT_NAME = Config.getParameter("cockpit.editor.area.custom.section.report.name");
    private static final String EDITOR_AREA_CUSTOM_SECTION_REPORT_FILE_NAME = Config.getParameter("cockpit.editor.area.custom.section.report");
    private static final String CUSTOM_SECTION_PARAM_NAME = "CUSTOM_SECTION_SUBREPORT";
    private static final String CUSTOM_SECTION_DS_EXP_PARAM_NAME = Config.getParameter("cockpit.editor.area.custom.section.ds.xpath.param.name");
    private static final String CUSTOM_SECTION_DS_EXP = Config.getParameter("cockpit.editor.area.custom.section.ds.xpath");
    private static final String EDITOR_AREA_CUSTOM_ROW_REPORT_NAME = Config.getParameter("cockpit.editor.area.custom.row.report.name");
    private static final String EDITOR_AREA_CUSTOM_ROW_REPORT_FILE_NAME = Config.getParameter("cockpit.editor.area.custom.row.report");
    private static final String CUSTOM_ROW_PARAM_NAME = "CUSTOM_ROW_SUBREPORT";
    private static final String CUSTOM_ROW_DS_EXP_PARAM_NAME = Config.getParameter("cockpit.editor.area.custom.row.ds.xpath.param.name");
    private static final String CUSTOM_ROW_DS_EXP = Config.getParameter("cockpit.editor.area.custom.row.ds.xpath");
    private Map<String, String> reportsNames;
    private Map<String, String> subReportsParams;


    public Map<String, String> getReportsFileNamesMap()
    {
        if(this.reportsNames == null)
        {
            this.reportsNames = new HashMap<>();
            this.reportsNames.put(EDITOR_AREA_MAIN_REPORT_NAME, EDITOR_AREA_MAIN_REPORT_FILE_NAME);
            this.reportsNames.put(EDITOR_AREA_SECTIONS_REPORT_NAME, EDITOR_AREA_SECTIONS_REPORT_FILE_NAME);
            this.reportsNames.put(EDITOR_AREA_ROWS_REPORT_NAME, EDITOR_AREA_ROWS_REPORT_FILE_NAME);
            this.reportsNames.put(EDITOR_AREA_MEDIA_REPORT_NAME, EDITOR_AREA_MEDIA_REPORT_FILE_NAME);
            this.reportsNames.put(EDITOR_AREA_TABLE_REPORT_NAME, EDITOR_AREA_TABLE_REPORT_FILE_NAME);
            this.reportsNames.put(EDITOR_AREA_TABLE_COLUMNS_TITLES_REPORT_NAME, EDITOR_AREA_TABLE_COLUMNS_TITLES_REPORT_FILE_NAME);
            this.reportsNames.put(EDITOR_AREA_TABLE_ROWS_REPORT_NAME, EDITOR_AREA_TABLE_ROWS_REPORT_FILE_NAME);
            this.reportsNames.put(EDITOR_AREA_REFERENCE_TABLE_REPORT_NAME, EDITOR_AREA_REFERENCE_TABLE_REPORT_FILE_NAME);
            this.reportsNames.put(EDITOR_AREA_REFERENCE_TABLE_ROWS_REPORT_NAME, EDITOR_AREA_REFERENCE_TABLE_ROWS_REPORT_FILE_NAME);
            this.reportsNames.put(EDITOR_AREA_LOCALIZED_ROW_REPORT_NAME, EDITOR_AREA_LOCALIZED_ROW_REPORT_FILE_NAME);
            this.reportsNames.put(EDITOR_AREA_CUSTOM_SECTION_REPORT_NAME, EDITOR_AREA_CUSTOM_SECTION_REPORT_FILE_NAME);
            this.reportsNames.put(EDITOR_AREA_CUSTOM_ROW_REPORT_NAME, EDITOR_AREA_CUSTOM_ROW_REPORT_FILE_NAME);
            this.reportsNames.put(EDITOR_AREA_ATTRIBUTES_REPORT_NAME, EDITOR_AREA_ATTRIBUTES_REPORT_FILE_NAME);
        }
        return this.reportsNames;
    }


    public Map<String, String> getReportsParamNamesMap()
    {
        if(this.subReportsParams == null)
        {
            this.subReportsParams = new HashMap<>();
            this.subReportsParams.put(EDITOR_AREA_MAIN_REPORT_NAME, "MAIN_REPORT");
            this.subReportsParams.put(EDITOR_AREA_SECTIONS_REPORT_NAME, "SECTIONS_SUBREPORT");
            this.subReportsParams.put(EDITOR_AREA_ROWS_REPORT_NAME, "ROWS_SUBREPORT");
            this.subReportsParams.put(EDITOR_AREA_MEDIA_REPORT_NAME, "MEDIA_SUBREPORT");
            this.subReportsParams.put(EDITOR_AREA_TABLE_REPORT_NAME, "TABLE_SUBREPORT");
            this.subReportsParams.put(EDITOR_AREA_TABLE_COLUMNS_TITLES_REPORT_NAME, "TABLE_COLUMNS_TITLES_SUBREPORT");
            this.subReportsParams.put(EDITOR_AREA_TABLE_ROWS_REPORT_NAME, "TABLE_ROWS_SUBREPORT");
            this.subReportsParams.put(EDITOR_AREA_REFERENCE_TABLE_REPORT_NAME, "REFERENCE_TABLE_SUBREPORT");
            this.subReportsParams.put(EDITOR_AREA_REFERENCE_TABLE_ROWS_REPORT_NAME, "REFERENCE_TABLE_ROWS_SUBREPORT");
            this.subReportsParams.put(EDITOR_AREA_LOCALIZED_ROW_REPORT_NAME, "LOCALIZED_ROW_SUBREPORT");
            this.subReportsParams.put(EDITOR_AREA_CUSTOM_SECTION_REPORT_NAME, "CUSTOM_SECTION_SUBREPORT");
            this.subReportsParams.put(EDITOR_AREA_CUSTOM_ROW_REPORT_NAME, "CUSTOM_ROW_SUBREPORT");
            this.subReportsParams.put(EDITOR_AREA_ATTRIBUTES_REPORT_NAME, "ATTRIBUTES_SUBREPORT");
        }
        return this.subReportsParams;
    }


    public String getMainReportName()
    {
        return EDITOR_AREA_MAIN_REPORT_NAME;
    }


    public String getMainReportParamName()
    {
        return "MAIN_REPORT";
    }


    public String getCustomSectionDSExp()
    {
        return CUSTOM_SECTION_DS_EXP;
    }


    public String getCustomSectionDSExpParamName()
    {
        return CUSTOM_SECTION_DS_EXP_PARAM_NAME;
    }


    public String getCustomRowDSExp()
    {
        return CUSTOM_ROW_DS_EXP;
    }


    public String getCustomRowDSExpParamName()
    {
        return CUSTOM_ROW_DS_EXP_PARAM_NAME;
    }


    public String getMainReportPreferencesTitle()
    {
        return MAIN_REPORT_PREFERENCES_TITLE;
    }


    public String getMainReportDsExp()
    {
        return MAIN_REPORT_DS_EXP;
    }


    public String getMainReportDsExpParamName()
    {
        return MAIN_REPORT_DS_EXP_PARAM_NAME;
    }


    public String getMainTitleReportParamName()
    {
        return "MAIN_REPORT_TITLE";
    }


    public Object getMainTitleReport()
    {
        return MAIN_REPORT_TITLE;
    }


    public String getLocalizedReportDSExp()
    {
        return LOCALIZED_ROWS_DS_EXP;
    }


    public String getLocalizedReportDsExpParamName()
    {
        return LOCALIZED_ROWS_DS_EXP_PARAM_NAME;
    }
}
