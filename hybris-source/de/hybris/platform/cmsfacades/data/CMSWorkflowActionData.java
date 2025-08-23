package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CMSWorkflowActionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private Map<String, String> name;
    private Map<String, String> description;
    private String actionType;
    private String status;
    private boolean isCurrentUserParticipant;
    private Long startedAgoInMillis;
    private List<CMSWorkflowDecisionData> decisions;
    private Date modifiedtime;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setName(Map<String, String> name)
    {
        this.name = name;
    }


    public Map<String, String> getName()
    {
        return this.name;
    }


    public void setDescription(Map<String, String> description)
    {
        this.description = description;
    }


    public Map<String, String> getDescription()
    {
        return this.description;
    }


    public void setActionType(String actionType)
    {
        this.actionType = actionType;
    }


    public String getActionType()
    {
        return this.actionType;
    }


    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getStatus()
    {
        return this.status;
    }


    public void setIsCurrentUserParticipant(boolean isCurrentUserParticipant)
    {
        this.isCurrentUserParticipant = isCurrentUserParticipant;
    }


    public boolean isIsCurrentUserParticipant()
    {
        return this.isCurrentUserParticipant;
    }


    public void setStartedAgoInMillis(Long startedAgoInMillis)
    {
        this.startedAgoInMillis = startedAgoInMillis;
    }


    public Long getStartedAgoInMillis()
    {
        return this.startedAgoInMillis;
    }


    public void setDecisions(List<CMSWorkflowDecisionData> decisions)
    {
        this.decisions = decisions;
    }


    public List<CMSWorkflowDecisionData> getDecisions()
    {
        return this.decisions;
    }


    public void setModifiedtime(Date modifiedtime)
    {
        this.modifiedtime = modifiedtime;
    }


    public Date getModifiedtime()
    {
        return this.modifiedtime;
    }
}
