package de.hybris.platform.externaltax;

import de.hybris.platform.util.TaxValue;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalTaxDocument implements Serializable
{
    private Map<Integer, List<TaxValue>> lineItemTaxes;
    private List<TaxValue> shippingCostTaxes;


    public Map<Integer, List<TaxValue>> getAllTaxes()
    {
        return getTaxesMap(false);
    }


    protected Map<Integer, List<TaxValue>> getTaxesMap(boolean createIfAbsent)
    {
        if(this.lineItemTaxes == null && createIfAbsent)
        {
            this.lineItemTaxes = new HashMap<>();
        }
        return (this.lineItemTaxes == null) ? Collections.<Integer, List<TaxValue>>emptyMap() : this.lineItemTaxes;
    }


    public List<TaxValue> getTaxesForOrderEntry(int entryNumber)
    {
        List<TaxValue> ret = getTaxesMap(false).get(Integer.valueOf(entryNumber));
        return (ret == null) ? Collections.<TaxValue>emptyList() : ret;
    }


    public void setTaxesForOrderEntry(int entryNumber, List<TaxValue> taxes)
    {
        if(taxes == null)
        {
            getTaxesMap(true).remove(Integer.valueOf(entryNumber));
        }
        else
        {
            getTaxesMap(true).put(Integer.valueOf(entryNumber), taxes);
        }
    }


    public void setTaxesForOrderEntry(int entryNumber, TaxValue... taxes)
    {
        setTaxesForOrderEntry(entryNumber, (taxes == null) ? null : Arrays.<TaxValue>asList(taxes));
    }


    public List<TaxValue> getShippingCostTaxes()
    {
        return (this.shippingCostTaxes == null) ? Collections.<TaxValue>emptyList() : this.shippingCostTaxes;
    }


    public void setShippingCostTaxes(List<TaxValue> shippingCostTaxes)
    {
        this.shippingCostTaxes = shippingCostTaxes;
    }


    public void setShippingCostTaxes(TaxValue... shippingCostTaxes)
    {
        setShippingCostTaxes((shippingCostTaxes == null) ? null : Arrays.<TaxValue>asList(shippingCostTaxes));
    }
}
