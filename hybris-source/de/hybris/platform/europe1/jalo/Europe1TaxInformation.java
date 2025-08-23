package de.hybris.platform.europe1.jalo;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.order.price.Tax;
import de.hybris.platform.jalo.order.price.TaxInformation;
import de.hybris.platform.util.TaxValue;
import java.util.Collections;

public class Europe1TaxInformation extends TaxInformation
{
    private final PK srcPK;


    public static TaxValue createTaxValue(TaxRow taxRow)
    {
        Tax tax = taxRow.getTax();
        double val = taxRow.hasValue() ? taxRow.getValue().doubleValue() : tax.getValue().doubleValue();
        boolean abs = taxRow.hasValue() ? taxRow.isAbsoluteAsPrimitive() : tax.isAbsolute().booleanValue();
        return new TaxValue(tax.getCode(), val, abs,
                        abs ? (taxRow.hasValue() ? taxRow.getCurrency() : tax.getCurrency()).getIsoCode() :
                                        null);
    }


    public Europe1TaxInformation(TaxRow taxRow)
    {
        super(Collections.EMPTY_MAP, createTaxValue(taxRow));
        this.srcPK = taxRow.getPK();
    }


    public Europe1TaxInformation(TaxValue taxValue, PK rowPK)
    {
        super(Collections.EMPTY_MAP, taxValue);
        this.srcPK = rowPK;
    }


    public TaxRow getTaxRow()
    {
        return (TaxRow)JaloSession.getCurrentSession().getItem(this.srcPK);
    }
}
