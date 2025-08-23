package de.hybris.platform.cockpit.components.sectionpanel;

import java.util.List;
import java.util.Set;
import org.zkoss.zul.Menupopup;

public interface RowlayoutSectionPanelModel extends SectionPanelModel
{
    List<SectionRow> getRows(Section paramSection);


    Set<SectionRow> getAllRows();


    void showRow(SectionRow paramSectionRow);


    void updateRow(SectionRow paramSectionRow);


    void setRowStatus(SectionRow paramSectionRow, int paramInt);


    void setRowStatus(SectionRow paramSectionRow, int paramInt, String paramString);


    void setValidationIconMenu(SectionRow paramSectionRow, Menupopup paramMenupopup);


    void setSectionHeaderStatus(Section paramSection, int paramInt);


    SectionRow getNextVisibleRow(SectionRow paramSectionRow);
}
