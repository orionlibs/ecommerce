package de.hybris.platform.returns;

import com.google.common.base.Preconditions;
import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.PK;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;

public class ReturnActionRequest
{
    private final ReturnRequestModel returnRequest;
    private final List<ReturnActionEntry> entriesToTakeAction;
    private final boolean partialAction;
    private final boolean partialEntryAction;
    private String requestToken;
    private String notes;
    private HybrisEnumValue actionReason;


    public ReturnActionRequest(ReturnRequestModel returnRequest)
    {
        this(returnRequest, (HybrisEnumValue)CancelReason.NA);
    }


    public ReturnActionRequest(ReturnRequestModel returnRequest, HybrisEnumValue actionReason)
    {
        this(returnRequest, actionReason, null);
    }


    public ReturnActionRequest(ReturnRequestModel returnRequest, HybrisEnumValue actionReason, String notes)
    {
        this.returnRequest = returnRequest;
        List<ReturnActionEntry> tmpList = new ArrayList<>();
        for(ReturnEntryModel rem : returnRequest.getReturnEntries())
        {
            tmpList.add(new ReturnActionEntry(rem, rem.getExpectedQuantity().longValue()));
        }
        Collections.sort(tmpList, (return1, return2) -> return1.getReturnEntry().getPk().compareTo(return2.getReturnEntry().getPk()));
        this.entriesToTakeAction = Collections.unmodifiableList(tmpList);
        this.partialAction = false;
        this.partialEntryAction = false;
        this.actionReason = actionReason;
        this.notes = notes;
    }


    public ReturnActionRequest(ReturnRequestModel returnRequest, List<ReturnActionEntry> returnActionEntries)
    {
        this(returnRequest, returnActionEntries, null);
    }


    public ReturnActionRequest(ReturnRequestModel returnRequest, List<ReturnActionEntry> returnActionEntries, String notes)
    {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(returnActionEntries), "returnActionEntries is null or empty");
        this.returnRequest = returnRequest;
        this.notes = notes;
        Map<PK, ReturnActionEntry> actionEntriesMap = new HashMap<>();
        boolean partialEntryActionDetected = false;
        for(ReturnActionEntry rae : returnActionEntries)
        {
            if(!returnRequest.equals(rae.getReturnEntry().getReturnRequest()))
            {
                throw new IllegalArgumentException("Attempt to add Return Entry that belongs to another return");
            }
            if(actionEntriesMap.containsKey(rae.getReturnEntry().getPk()))
            {
                throw new IllegalArgumentException("Attempt to add Return Entry twice");
            }
            actionEntriesMap.put(rae.getReturnEntry().getPk(), rae);
            if(rae.getActionQuantity() < rae.getReturnEntry().getExpectedQuantity().longValue())
            {
                partialEntryActionDetected = true;
            }
        }
        List<ReturnActionEntry> tmpList = new ArrayList<>(actionEntriesMap.values());
        Collections.sort(tmpList, (rae1, rae2) -> rae1.getReturnEntry().getPk().compareTo(rae2.getReturnEntry().getPk()));
        this.entriesToTakeAction = Collections.unmodifiableList(tmpList);
        this.partialEntryAction = partialEntryActionDetected;
        if(partialEntryActionDetected)
        {
            this.partialAction = true;
        }
        else
        {
            boolean allReturnEntriesDone = true;
            for(ReturnEntryModel rem : returnRequest.getReturnEntries())
            {
                if(!actionEntriesMap.containsKey(rem.getPk()))
                {
                    allReturnEntriesDone = false;
                }
            }
            this.partialAction = !allReturnEntriesDone;
        }
    }


    public String getRequestToken()
    {
        return this.requestToken;
    }


    public void setRequestToken(String requestToken)
    {
        this.requestToken = requestToken;
    }


    public HybrisEnumValue getActionReason()
    {
        return this.actionReason;
    }


    public void setActionReason(HybrisEnumValue actionReason)
    {
        this.actionReason = actionReason;
    }


    public ReturnRequestModel getReturnRequest()
    {
        return this.returnRequest;
    }


    public List<ReturnActionEntry> getEntriesToTakeAction()
    {
        return this.entriesToTakeAction;
    }


    public String getNotes()
    {
        return this.notes;
    }


    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    public boolean isPartialAction()
    {
        return this.partialAction;
    }


    public boolean isPartialEntryAction()
    {
        return this.partialEntryAction;
    }
}
