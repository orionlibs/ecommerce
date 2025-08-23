package de.hybris.platform.persistence.link;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.ExtensibleItemRemote;
import de.hybris.platform.persistence.ItemRemote;

public interface LinkRemote extends ExtensibleItemRemote
{
    String getQualifier();


    void setQualifier(String paramString);


    void setSourcePK(PK paramPK);


    PK getSourcePK();


    void setTargetPK(PK paramPK);


    PK getTargetPK();


    void setSource(ItemRemote paramItemRemote);


    ItemRemote getSource();


    void setTarget(ItemRemote paramItemRemote);


    ItemRemote getTarget();


    int getSequenceNumber();


    void setSequenceNumber(int paramInt);


    int getReverseSequenceNumber();


    void setReverseSequenceNumber(int paramInt);
}
