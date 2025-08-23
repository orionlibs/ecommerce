package de.hybris.platform.fraud.symptom.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.fraud.impl.FraudServiceResponse;
import de.hybris.platform.fraud.impl.FraudSymptom;
import de.hybris.platform.fraud.strategy.AbstractOrderFraudSymptomDetection;
import java.util.Collections;
import java.util.Set;

public class BlackListSymptom extends AbstractOrderFraudSymptomDetection
{
    private Set<String> bannedEmails;
    private Set<String> bannedUserIDs;


    public Set<String> getBannedEmails()
    {
        return (this.bannedEmails == null) ? Collections.<String>emptySet() : this.bannedEmails;
    }


    public void setBannedEmails(Set<String> bannedEmails)
    {
        this.bannedEmails = bannedEmails;
    }


    public Set<String> getBannedUserIDs()
    {
        return (this.bannedUserIDs == null) ? Collections.<String>emptySet() : this.bannedUserIDs;
    }


    public void setBannedUserIDs(Set<String> bannedUserIDs)
    {
        this.bannedUserIDs = bannedUserIDs;
    }


    public FraudServiceResponse recognizeSymptom(FraudServiceResponse fraudResponse, AbstractOrderModel order)
    {
        boolean foundEmail = false;
        boolean foundUid = false;
        if(getBannedUserIDs().contains(order.getUser().getUid()))
        {
            fraudResponse.addSymptom(new FraudSymptom(getSymptomName() + ":Uid", getIncrement()));
            foundUid = true;
        }
        for(AddressModel address : order.getUser().getAddresses())
        {
            if(isUsersCurrentAddress(address) && address.getEmail() != null && getBannedEmails().contains(address.getEmail()))
            {
                fraudResponse.addSymptom(new FraudSymptom(getSymptomName() + ":email", getIncrement()));
                foundEmail = true;
            }
        }
        if(!foundEmail && !foundUid)
        {
            fraudResponse.addSymptom(new FraudSymptom(getSymptomName(), 0.0D));
        }
        return fraudResponse;
    }


    protected boolean isUsersCurrentAddress(AddressModel address)
    {
        return Boolean.FALSE.equals(address.getDuplicate());
    }
}
