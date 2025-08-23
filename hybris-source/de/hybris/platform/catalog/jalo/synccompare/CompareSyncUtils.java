package de.hybris.platform.catalog.jalo.synccompare;

import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompareSyncUtils
{
    public static Map<String, Catalog> getCatalogs()
    {
        String query = "SELECT {PK} FROM {Catalog}";
        List<Catalog> list = FlexibleSearch.getInstance().search("SELECT {PK} FROM {Catalog}", Catalog.class).getResult();
        Map<String, Catalog> retmap = new HashMap<>(list.size());
        for(Catalog cat : list)
        {
            retmap.put(cat.getId(), cat);
        }
        return retmap;
    }


    public static Map<String, CatalogVersionSyncJob> getSyncJob(Catalog cat)
    {
        String query = "SELECT {PK} FROM {CatalogVersionSyncJob} WHERE {sourceVersion} IN ({{ SELECT {PK} FROM {CatalogVersion} WHERE {catalog} = ?cat }})";
        Map<String, Item> values = new HashMap<>();
        values.put("cat", cat);
        List<CatalogVersionSyncJob> list = FlexibleSearch.getInstance().search("SELECT {PK} FROM {CatalogVersionSyncJob} WHERE {sourceVersion} IN ({{ SELECT {PK} FROM {CatalogVersion} WHERE {catalog} = ?cat }})", values, CatalogVersionSyncJob.class).getResult();
        Map<String, CatalogVersionSyncJob> retmap = new HashMap<>(list.size());
        for(CatalogVersionSyncJob syncjob : list)
        {
            retmap.put(syncjob.getCode(), syncjob);
        }
        return retmap;
    }


    public static String createHTMLResultTable(ItemCompareResult icr, boolean fulloutput, boolean withheader)
    {
        StringBuilder htmlTable = new StringBuilder();
        htmlTable.append("<table border=\"1\"  cellspacing=\"0\" cellpadding=\"2\">");
        if(fulloutput || icr.getResult() == null || !icr.getResult().equals(Status.EQUAL))
        {
            if(withheader)
            {
                htmlTable.append("<tr><td><b>source pk</b></td>");
                htmlTable.append("<td><b>target pk</b></td>");
                htmlTable.append("<td><b>composed type</b></td>");
                htmlTable.append("<td><b>result</b></td></tr>");
            }
            htmlTable.append(getItemAsHTMLTableRow(icr, fulloutput));
        }
        htmlTable.append("</table>");
        return htmlTable.toString();
    }


    private static String getItemAsHTMLTableRow(AttributeDescriptorCompareResult adcr, boolean fulloutput)
    {
        StringBuilder htmlTableRow = new StringBuilder();
        htmlTableRow.append("<tr>");
        htmlTableRow.append("<td>" + adcr.getCode(fulloutput) + "</td>");
        htmlTableRow.append("<td>" + adcr.getResult() + "</td>");
        htmlTableRow.append("</tr>");
        if(adcr.getResult() != null && adcr.getResult().equals(Status.NOT_EQUAL) && adcr
                        .hasAdditionalInformations())
        {
            htmlTableRow.append("<tr>");
            htmlTableRow.append("<td >" + adcr.getProblemDescription() + "</td><td>" + adcr.getDifference() + "</td>");
            htmlTableRow.append("</tr>");
        }
        if(adcr.getSubResults().size() > 0)
        {
            htmlTableRow.append("<tr>");
            htmlTableRow.append("<td colspan=\"2\">");
            htmlTableRow.append("<table border=\"1\"  cellspacing=\"0\" cellpadding=\"2\">");
            for(ItemCompareResult icr : adcr.getSubResults())
            {
                if(fulloutput || icr.getResult() == null || !icr.getResult().equals(Status.EQUAL))
                {
                    htmlTableRow.append(getItemAsHTMLTableRow(icr, fulloutput));
                }
            }
            htmlTableRow.append("</table>");
            htmlTableRow.append("</td></tr>");
        }
        return htmlTableRow.toString();
    }


    private static String getItemAsHTMLTableRow(ItemCompareResult icr, boolean fulloutput)
    {
        StringBuilder htmlTableRow = new StringBuilder();
        htmlTableRow.append("<tr>");
        htmlTableRow.append("<td>" + icr.getSourcePK() + "</td>");
        htmlTableRow.append("<td>" + icr.getTargetPK() + "</td>");
        htmlTableRow.append("<td>" + icr.getComposedTypeCode() + "</td>");
        htmlTableRow.append("<td>" + icr.getResult() + "</td>");
        htmlTableRow.append("</tr>");
        if(icr.getResult() != null && icr.getResult()
                        .equals(Status.NOT_EQUAL) && icr.hasAdditionalInformations())
        {
            htmlTableRow.append("<tr>");
            htmlTableRow.append("<td colspan=\"2\">" + icr
                            .getProblemDescription() + "</td><td colspan=\"2\">" + icr.getDifference() + "</td>");
            htmlTableRow.append("</tr>");
        }
        if(!icr.getSubResults().isEmpty())
        {
            htmlTableRow.append("<tr><td colspan=\"4\">");
            htmlTableRow.append("<table border=\"1\"  cellspacing=\"0\" cellpadding=\"2\">");
            for(AttributeDescriptorCompareResult adcr : icr.getSubResults())
            {
                if(fulloutput || adcr.getResult() == null || !adcr.getResult().equals(Status.EQUAL))
                {
                    htmlTableRow.append(getItemAsHTMLTableRow(adcr, fulloutput));
                }
            }
            htmlTableRow.append("</table>");
            htmlTableRow.append("</td></tr>");
        }
        return htmlTableRow.toString();
    }
}
