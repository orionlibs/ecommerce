package de.hybris.platform.ordercancel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CancelDecision implements Serializable
{
    private final boolean allowed;
    private final List<OrderCancelDenialReason> denialReasons;


    public CancelDecision(boolean allowed, List<OrderCancelDenialReason> denialReasons)
    {
        this.allowed = allowed;
        this.denialReasons = Collections.unmodifiableList(new ArrayList<>(denialReasons));
    }


    public boolean isAllowed()
    {
        return this.allowed;
    }


    public List<OrderCancelDenialReason> getDenialReasons()
    {
        return this.denialReasons;
    }
}
