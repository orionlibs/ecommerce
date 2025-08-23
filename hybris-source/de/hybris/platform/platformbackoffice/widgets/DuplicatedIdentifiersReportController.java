package de.hybris.platform.platformbackoffice.widgets;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.catalog.DuplicatedItemIdentifier;
import de.hybris.platform.platformbackoffice.data.DuplicatedItemsReport;
import java.util.Collection;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

public class DuplicatedIdentifiersReportController extends DefaultWidgetController
{
    protected Grid duplicatedIDsGrid;
    @Wire("reportTextBox")
    private Textbox reportTextBox;


    public void initialize(Component comp)
    {
        super.initialize(comp);
        setWidgetTitle(getLabel("title"));
    }


    @SocketEvent(socketId = "createContext")
    public void displayReport(DuplicatedItemsReport duplicatedItemsReport)
    {
        Collection<DuplicatedItemIdentifier> duplicates = duplicatedItemsReport.getDuplicatedIdentifiers();
        renderTableRows(duplicates);
        this.reportTextBox.setText(duplicatedItemsReport.renderTextualReport());
    }


    private void renderTableRows(Collection<DuplicatedItemIdentifier> duplicates)
    {
        for(DuplicatedItemIdentifier duplicate : duplicates)
        {
            Row currentRow = new Row();
            currentRow.appendChild((Component)new Label(duplicate.getComposedType()));
            currentRow.appendChild((Component)new Label(String.valueOf(duplicate.getCount())));
            String formattedCode = duplicate.getCode().replaceAll(";", " ");
            Label label = new Label(formattedCode);
            label.setMultiline(true);
            currentRow.appendChild((Component)label);
            this.duplicatedIDsGrid.getRows().appendChild((Component)currentRow);
        }
    }
}
