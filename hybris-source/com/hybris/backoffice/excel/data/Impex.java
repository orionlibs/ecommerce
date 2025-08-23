package com.hybris.backoffice.excel.data;

import com.google.common.collect.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Impex implements Serializable
{
    public static final String EXCEL_IMPORT_DOCUMENT_REF_HEADER_NAME = "&ExcelImportRef";
    private final List<ImpexForType> impexes = new ArrayList<>();


    public ImpexForType findUpdates(String typeCode)
    {
        return this.impexes.stream()
                        .filter(impex -> typeCode.equals(impex.getTypeCode()))
                        .findFirst()
                        .orElseGet(() -> createNewImpex(typeCode));
    }


    protected ImpexForType createNewImpex(String typeCode)
    {
        ImpexForType newlyCreatedImpex = new ImpexForType(typeCode);
        this.impexes.add(newlyCreatedImpex);
        return newlyCreatedImpex;
    }


    public List<ImpexForType> getImpexes()
    {
        return (List<ImpexForType>)this.impexes.stream()
                        .sorted(Comparator.comparing(this::usesDocumentRef).thenComparing(ImpexForType::getOrder)).collect(Collectors.toList());
    }


    protected boolean usesDocumentRef(ImpexForType impexForType)
    {
        return impexForType.getImpexTable().columnKeySet().stream()
                        .anyMatch(header -> header.getName().contains(String.format("(%s)", new Object[] {"&ExcelImportRef"})));
    }


    public void mergeImpex(Impex subImpex, String typeCode, Integer rowIndex)
    {
        if(subImpex != null)
        {
            for(ImpexForType impexForType : subImpex.getImpexes())
            {
                ImpexForType mainImpexForType = findUpdates(impexForType.getTypeCode());
                mainImpexForType.setOrder(impexForType.getOrder());
                int lastRowIndex = mainImpexForType.getImpexTable().rowKeySet().size();
                for(Table.Cell<Integer, ImpexHeaderValue, Object> cell : (Iterable<Table.Cell<Integer, ImpexHeaderValue, Object>>)impexForType.getImpexTable().cellSet())
                {
                    Integer index = Integer.valueOf(typeCode.equals(impexForType.getTypeCode()) ? rowIndex.intValue() : (((Integer)cell.getRowKey()).intValue() + lastRowIndex));
                    mainImpexForType.putValue(index, (ImpexHeaderValue)cell.getColumnKey(), cell.getValue());
                }
            }
        }
    }


    public void mergeImpex(Impex subImpex)
    {
        if(subImpex != null)
        {
            for(ImpexForType impexForType : subImpex.getImpexes())
            {
                ImpexForType mainImpexForType = findUpdates(impexForType.getTypeCode());
                int latestRowIndex = mainImpexForType.getImpexTable().rowKeySet().size();
                for(Table.Cell<Integer, ImpexHeaderValue, Object> cell : (Iterable<Table.Cell<Integer, ImpexHeaderValue, Object>>)impexForType.getImpexTable().cellSet())
                {
                    mainImpexForType.putValue(Integer.valueOf(latestRowIndex + ((Integer)cell.getRowKey()).intValue()), (ImpexHeaderValue)cell.getColumnKey(), cell.getValue());
                }
            }
        }
    }
}
