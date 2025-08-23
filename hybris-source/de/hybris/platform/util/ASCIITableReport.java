package de.hybris.platform.util;

import com.bethecoder.ascii_table.ASCIITable;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ASCIITableReport
{
    public static final String EMPTY_CELL_MARKER = "-";
    private final List<String> topHeaders = new ArrayList<>();
    private final List<DataRow> dataRows = new ArrayList<>();
    private Boolean rowTitlesEnabled;


    public static ASCIITableReport builder()
    {
        return new ASCIITableReport();
    }


    public ASCIITableReport disableRowTitles()
    {
        this.rowTitlesEnabled = Boolean.FALSE;
        return this;
    }


    public ASCIITableReport enableRowTitles()
    {
        this.rowTitlesEnabled = Boolean.TRUE;
        return this;
    }


    public ASCIITableReport withTopHeaders(String... headers)
    {
        Collections.addAll(this.topHeaders, headers);
        return this;
    }


    public ASCIITableReport addDataRow(String... dataRow)
    {
        Preconditions.checkNotNull(dataRow, "dataRow must not be null");
        DataRow dr = new DataRow(this);
        dr.setDataRow(dataRow);
        this.dataRows.add(dr);
        return this;
    }


    public ASCIITableReport titledBy(String rowTitle)
    {
        Preconditions.checkNotNull(rowTitle, "rowTitle must not be null");
        DataRow last = (DataRow)Iterables.getLast(this.dataRows);
        last.setRowTitle(rowTitle);
        this.rowTitlesEnabled = Boolean.TRUE;
        return this;
    }


    public void printTable()
    {
        if(this.dataRows.isEmpty())
        {
            System.out.println("No data specified. Report disabled.");
        }
        else
        {
            ASCIITable asciiTable = ASCIITable.getInstance();
            asciiTable.printTable(getHeader(), getRowsData());
        }
    }


    public String getTable()
    {
        if(this.dataRows.isEmpty())
        {
            return "No data specified. Report disabled.";
        }
        ASCIITable asciiTable = ASCIITable.getInstance();
        return asciiTable.getTable(getHeader(), getRowsData());
    }


    private String[] getHeader()
    {
        List<String> header = new ArrayList<>(this.topHeaders.size());
        if(shouldPrintTopHeaders())
        {
            header.add("-");
        }
        header.addAll(this.topHeaders);
        return (String[])Iterables.toArray(header, String.class);
    }


    private boolean shouldPrintTopHeaders()
    {
        return (shouldPrintRowTitles() && !this.topHeaders.isEmpty());
    }


    private String[][] getRowsData()
    {
        Iterable<String[]> result = Iterables.transform(this.dataRows, (Function)new Object(this));
        return (String[][])Iterables.toArray(result, String[].class);
    }


    private boolean shouldPrintRowTitles()
    {
        return (this.rowTitlesEnabled != null && this.rowTitlesEnabled.booleanValue());
    }
}
