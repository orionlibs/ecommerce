package de.hybris.platform.payment.commands.request;

import de.hybris.platform.payment.dto.BasicCardInfo;

public class IsApplicableCommandReqest
{
    private final BasicCardInfo card;
    private final boolean threeD;


    public IsApplicableCommandReqest(BasicCardInfo card, boolean threeD)
    {
        this.card = card;
        this.threeD = threeD;
    }


    public BasicCardInfo getCard()
    {
        return this.card;
    }


    public boolean isThreeD()
    {
        return this.threeD;
    }
}
