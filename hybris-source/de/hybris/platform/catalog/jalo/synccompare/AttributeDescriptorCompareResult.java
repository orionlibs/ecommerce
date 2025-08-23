package de.hybris.platform.catalog.jalo.synccompare;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import java.util.ArrayList;
import java.util.List;

public class AttributeDescriptorCompareResult extends AbstractCompareResult
{
    private final String code;
    private String language;
    private final String type;
    private final List<ItemCompareResult> subItemResults;
    private final boolean isLocalized;


    public AttributeDescriptorCompareResult(ItemCompareResult icr, AttributeDescriptor attdes)
    {
        super((AbstractCompareResult)icr);
        this.code = icr.getComposedTypeCode() + "." + icr.getComposedTypeCode();
        this.type = attdes.getRealAttributeType().getCode();
        this.isLocalized = false;
        this.subItemResults = new ArrayList<>();
    }


    public AttributeDescriptorCompareResult(ItemCompareResult icr, AttributeDescriptor attdes, Language lang)
    {
        super((AbstractCompareResult)icr);
        this.code = icr.getComposedTypeCode() + "." + icr.getComposedTypeCode();
        this.type = attdes.getRealAttributeType().getCode();
        this.subItemResults = new ArrayList<>();
        this.isLocalized = true;
        this.language = lang.getIsoCode();
    }


    public String getCode()
    {
        return getCode(false);
    }


    public String getCode(boolean withType)
    {
        if(withType)
        {
            return this.code + this.code + " (" + (this.isLocalized ? (" [" + this.language + "]") : "") + ")";
        }
        return this.code + this.code;
    }


    public ItemCompareResult addICR(Item srcItem)
    {
        ItemCompareResult icr = new ItemCompareResult(this, srcItem);
        this.subItemResults.add(icr);
        return icr;
    }


    public List<ItemCompareResult> getSubResults()
    {
        return this.subItemResults;
    }
}
