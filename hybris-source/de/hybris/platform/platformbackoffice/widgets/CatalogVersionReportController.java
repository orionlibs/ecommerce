package de.hybris.platform.platformbackoffice.widgets;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.platformbackoffice.data.CatalogVersionReportDTO;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;

public class CatalogVersionReportController extends DefaultWidgetController
{
    protected Textbox report;
    protected Grid gridLanguages;
    protected Grid gridPermissions;
    protected Grid gridCategories;
    protected Grid gridTypeAmounts;
    protected Tab generalTab;
    protected Label catalogLabel;
    protected Label versionLabel;
    protected Label activeLabel;
    private static final String PERMISSION_READ = "READ";
    private static final String PERMISSION_WRITE = "WRITE";


    @SocketEvent(socketId = "catalogversionreport")
    public void showReport(CatalogVersionReportDTO reportDTO) throws InterruptedException
    {
        this.report.setValue(reportDTO.toString());
        this.catalogLabel.setValue(reportDTO.getCatalog());
        this.versionLabel.setValue(reportDTO.getVersion());
        this.activeLabel.setValue(reportDTO.getActive().toString());
        for(String language : reportDTO.getLanguages())
        {
            Row currentRow = new Row();
            currentRow.appendChild((Component)new Label(language));
            this.gridLanguages.getRows().appendChild((Component)currentRow);
        }
        for(String principal : reportDTO.getReadPrincipals())
        {
            Row currentRow = new Row();
            currentRow.appendChild((Component)new Label("READ"));
            currentRow.appendChild((Component)new Label(principal));
            this.gridPermissions.getRows().appendChild((Component)currentRow);
        }
        for(String principal : reportDTO.getWritePrincipals())
        {
            Row currentRow = new Row();
            currentRow.appendChild((Component)new Label("WRITE"));
            currentRow.appendChild((Component)new Label(principal));
            this.gridPermissions.getRows().appendChild((Component)currentRow);
        }
        for(String category : reportDTO.getRootCategories())
        {
            Row currentRow = new Row();
            currentRow.appendChild((Component)new Label(category));
            this.gridCategories.getRows().appendChild((Component)currentRow);
        }
        Map<ComposedTypeModel, Long> typeAmounts = reportDTO.getTypeStatistics();
        for(Map.Entry<ComposedTypeModel, Long> type : typeAmounts.entrySet())
        {
            Row currentRow = new Row();
            currentRow.appendChild((Component)new Label(((ComposedTypeModel)type.getKey()).getCode()));
            currentRow.appendChild((Component)new Label(((Long)type.getValue()).toString()));
            this.gridTypeAmounts.getRows().appendChild((Component)currentRow);
        }
    }
}
