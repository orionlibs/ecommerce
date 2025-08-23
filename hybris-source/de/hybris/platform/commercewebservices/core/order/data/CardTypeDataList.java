package de.hybris.platform.commercewebservices.core.order.data;

import de.hybris.platform.commercefacades.order.data.CardTypeData;
import java.io.Serializable;
import java.util.List;

public class CardTypeDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<CardTypeData> cardTypes;


    public void setCardTypes(List<CardTypeData> cardTypes)
    {
        this.cardTypes = cardTypes;
    }


    public List<CardTypeData> getCardTypes()
    {
        return this.cardTypes;
    }
}
