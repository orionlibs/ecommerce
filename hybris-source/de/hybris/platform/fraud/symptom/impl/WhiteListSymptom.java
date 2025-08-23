package de.hybris.platform.fraud.symptom.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.fraud.impl.FraudServiceResponse;
import de.hybris.platform.fraud.strategy.AbstractOrderFraudSymptomDetection;
import java.util.Collections;
import java.util.Set;

public class WhiteListSymptom extends AbstractOrderFraudSymptomDetection
{
    private Set<String> favoredEmails;
    private Set<String> favoredUserIDs;


    public Set<String> getFavoredEmails()
    {
        return (this.favoredEmails == null) ? Collections.<String>emptySet() : this.favoredEmails;
    }


    public void setFavoredEmails(Set<String> favoredEmails)
    {
        this.favoredEmails = favoredEmails;
    }


    public Set<String> getFavoredUserIDs()
    {
        return (this.favoredUserIDs == null) ? Collections.<String>emptySet() : this.favoredUserIDs;
    }


    public void setFavoredUserIDs(Set<String> favoredUserIDs)
    {
        this.favoredUserIDs = favoredUserIDs;
    }


    public FraudServiceResponse recognizeSymptom(FraudServiceResponse fraudResponse, AbstractOrderModel order)
    {
        boolean foundEmail = false;
        boolean foundUid = false;
        if(getFavoredUserIDs().contains(order.getUser().getUid()))
        {
            fraudResponse.addSymptom(createSymptom(getSymptomName() + ":Uid", true));
            foundUid = true;
        }
        for(AddressModel address : order.getUser().getAddresses())
        {
            if(address.getEmail() != null && getFavoredEmails().contains(address.getEmail()))
            {
                fraudResponse.addSymptom(createSymptom(getSymptomName() + ":email", true));
                foundEmail = true;
            }
        }
        if(!foundEmail && !foundUid)
        {
            fraudResponse.addSymptom(createSymptom(false));
        }
        return fraudResponse;
    }
}
