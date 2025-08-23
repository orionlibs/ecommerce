package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.ticket.model.CsAgentGroupModel;

public class CsAgentGroupModelBuilder
{
    private final CsAgentGroupModel model = new CsAgentGroupModel();


    public static CsAgentGroupModelBuilder aModel()
    {
        return new CsAgentGroupModelBuilder();
    }


    private CsAgentGroupModel getModel()
    {
        return this.model;
    }


    public CsAgentGroupModel build()
    {
        return getModel();
    }


    public CsAgentGroupModelBuilder withUid(String uid)
    {
        getModel().setUid(uid);
        return this;
    }
}
