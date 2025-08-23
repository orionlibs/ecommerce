package de.hybris.platform.cockpit.components.sectionpanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.zkoss.zul.Menupopup;

public class DefaultSectionPanelModel extends AbstractSectionPanelModel implements RowlayoutSectionPanelModel
{
    private final Map<Section, List<SectionRow>> rowsMap = new HashMap<>();


    public void setRows(Section section, List<SectionRow> rows)
    {
        this.rowsMap.put(section, (rows != null) ? new ArrayList<>(rows) : null);
        fireSectionUpdate(section);
    }


    public void addSection(Section section, List<SectionRow> rows)
    {
        this.rowsMap.put(section, (rows != null) ? new ArrayList<>(rows) : null);
        addSection(section);
    }


    public void reset()
    {
        clearMessages();
        setLabel(null);
        this.rowsMap.clear();
        removeAllSections();
    }


    public void removeAllSections()
    {
        super.removeAllSections();
        this.rowsMap.clear();
    }


    public void removeSection(Section section)
    {
        super.removeSection(section);
        this.rowsMap.remove(section);
    }


    public List<SectionRow> getRows(Section section)
    {
        List<SectionRow> sectionRowList = this.rowsMap.get(section);
        return (sectionRowList != null) ? Collections.<SectionRow>unmodifiableList(sectionRowList) : Collections.EMPTY_LIST;
    }


    public Section getSectionForRow(SectionRow row)
    {
        for(Map.Entry<Section, List<SectionRow>> e : this.rowsMap.entrySet())
        {
            if(((List)e.getValue()).contains(row))
            {
                return e.getKey();
            }
        }
        throw new IllegalArgumentException("row " + row + " does not belong to model " + this);
    }


    public void moveRow(SectionRow row, Section sectionTo, int index)
    {
        Section from = getSectionForRow(row);
        if(from.equals(sectionTo) && ((List)this.rowsMap.get(from)).indexOf(row) < index)
        {
            index--;
        }
        ((List)this.rowsMap.get(from)).remove(row);
        if(this.rowsMap.get(sectionTo) == null)
        {
            this.rowsMap.put(sectionTo, new ArrayList<>());
        }
        if(index >= 0 && index < ((List)this.rowsMap.get(sectionTo)).size())
        {
            ((List<SectionRow>)this.rowsMap.get(sectionTo)).add(index, row);
        }
        else
        {
            ((List<SectionRow>)this.rowsMap.get(sectionTo)).add(row);
        }
        fireRowMoved(from, sectionTo);
    }


    public void hideRow(SectionRow row)
    {
        if(row.isVisible())
        {
            ((DefaultSectionRow)row).setVisible(false);
            fireRowHide(row);
        }
    }


    public void showRow(SectionRow row)
    {
        if(!row.isVisible())
        {
            ((DefaultSectionRow)row).setVisible(true);
            fireRowShow(row);
        }
    }


    public SectionRow getNextVisibleRow(SectionRow row)
    {
        SectionRow ret = null;
        List<SectionRow> rows = getRows(getSectionForRow(row));
        int currentIndex = rows.indexOf(row) + 1;
        while(currentIndex < rows.size())
        {
            SectionRow sectionRow = rows.get(currentIndex);
            if(sectionRow.isVisible())
            {
                ret = sectionRow;
                break;
            }
            currentIndex++;
        }
        if(ret == null)
        {
            currentIndex = getSections().indexOf(getSectionForRow(row)) + 1;
            while(currentIndex < getSections().size())
            {
                Section section = getSections().get(currentIndex);
                if(section.isOpen() && section.isVisible())
                {
                    List<SectionRow> rows2 = getRows(section);
                    if(!rows2.isEmpty() && ((SectionRow)rows2.get(0)).isVisible())
                    {
                        ret = rows2.get(0);
                        break;
                    }
                    if(!rows2.isEmpty())
                    {
                        getNextVisibleRow(rows2.get(0));
                    }
                }
                currentIndex++;
            }
        }
        return ret;
    }


    public void setRowStatus(SectionRow row, int status)
    {
        fireRowStatusChange(row, status, null);
    }


    public void setRowStatus(SectionRow row, int status, String localizedMsg)
    {
        fireRowStatusChange(row, status, localizedMsg);
    }


    public void updateRow(SectionRow row)
    {
        fireRowUpdate(row);
    }


    public void hideSection(Section section)
    {
        if(section.isVisible())
        {
            ((DefaultSection)section).setVisible(false);
            fireSectionHide(section);
        }
    }


    public void showSection(Section section)
    {
        if(!section.isVisible())
        {
            ((DefaultSection)section).setVisible(true);
            fireSectionShow(section);
        }
    }


    public void initialize()
    {
    }


    public void update()
    {
        for(SectionRow row : getAllRows())
        {
            if(row.isVisible())
            {
                updateRow(row);
            }
        }
    }


    public Map<String, Object> getContext()
    {
        return Collections.EMPTY_MAP;
    }


    public Set<SectionRow> getAllRows()
    {
        Set<SectionRow> ret = new HashSet<>();
        for(Map.Entry<Section, List<SectionRow>> entry : this.rowsMap.entrySet())
        {
            ret.addAll(entry.getValue());
        }
        return ret;
    }


    public void setSectionHeaderStatus(Section section, int status)
    {
        fireSectionHeaderChange(section, status);
    }


    public void setValidationIconMenu(SectionRow row, Menupopup menuPopup)
    {
        fireAttacheValidationIconMenu(row, menuPopup);
    }
}
