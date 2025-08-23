package de.hybris.platform.catalog.jalo.synccompare;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import java.util.ArrayList;
import java.util.List;

public class ItemCompareResult extends AbstractCompareResult
{
    private String srcPK;
    private String trgPK;
    private String composedTypeCode = "n/a";
    private List<AttributeDescriptorCompareResult> adcResults;


    public ItemCompareResult(Item srcItem)
    {
        super(null);
        setSrcItem(srcItem);
    }


    public ItemCompareResult(AttributeDescriptorCompareResult superResult, Item currentSrcItem)
    {
        super((AbstractCompareResult)superResult);
        setSrcItem(currentSrcItem);
    }


    private void setSrcItem(Item currentSrcItem)
    {
        this.srcPK = (currentSrcItem == null) ? "null" : currentSrcItem.getPK().toString();
        this.composedTypeCode = (currentSrcItem == null) ? "n/a" : currentSrcItem.getComposedType().getCode();
        this.adcResults = new ArrayList<>();
    }


    public void setTargetItem(Item trgItem)
    {
        this.trgPK = (trgItem == null) ? "null" : trgItem.getPK().toString();
    }


    public String getSourcePK()
    {
        return this.srcPK;
    }


    public String getTargetPK()
    {
        return this.trgPK;
    }


    public String getComposedTypeCode()
    {
        return this.composedTypeCode;
    }


    public AttributeDescriptorCompareResult addADCR(AttributeDescriptor attdes)
    {
        AttributeDescriptorCompareResult adcr = new AttributeDescriptorCompareResult(this, attdes);
        this.adcResults.add(adcr);
        return adcr;
    }


    public AttributeDescriptorCompareResult addADCR(AttributeDescriptor attdes, Language lang)
    {
        AttributeDescriptorCompareResult adcr = new AttributeDescriptorCompareResult(this, attdes, lang);
        this.adcResults.add(adcr);
        return adcr;
    }


    public List<AttributeDescriptorCompareResult> getSubResults()
    {
        return this.adcResults;
    }
}
