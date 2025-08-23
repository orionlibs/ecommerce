package de.hybris.platform.ticketsystem.data;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ticket.enums.CsEventReason;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import java.io.Serializable;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class CsTicketParameter implements Serializable
{
    private static final long serialVersionUID = 1L;
    private UserModel customer;
    private AbstractOrderModel associatedTo;
    private CsTicketCategory category;
    private CsTicketPriority priority;
    private EmployeeModel assignedAgent;
    private CsAgentGroupModel assignedGroup;
    private String headline;
    private CsInterventionType interventionType;
    private CsEventReason reason;
    private String creationNotes;
    private List<MultipartFile> attachments;


    public void setCustomer(UserModel customer)
    {
        this.customer = customer;
    }


    public UserModel getCustomer()
    {
        return this.customer;
    }


    public void setAssociatedTo(AbstractOrderModel associatedTo)
    {
        this.associatedTo = associatedTo;
    }


    public AbstractOrderModel getAssociatedTo()
    {
        return this.associatedTo;
    }


    public void setCategory(CsTicketCategory category)
    {
        this.category = category;
    }


    public CsTicketCategory getCategory()
    {
        return this.category;
    }


    public void setPriority(CsTicketPriority priority)
    {
        this.priority = priority;
    }


    public CsTicketPriority getPriority()
    {
        return this.priority;
    }


    public void setAssignedAgent(EmployeeModel assignedAgent)
    {
        this.assignedAgent = assignedAgent;
    }


    public EmployeeModel getAssignedAgent()
    {
        return this.assignedAgent;
    }


    public void setAssignedGroup(CsAgentGroupModel assignedGroup)
    {
        this.assignedGroup = assignedGroup;
    }


    public CsAgentGroupModel getAssignedGroup()
    {
        return this.assignedGroup;
    }


    public void setHeadline(String headline)
    {
        this.headline = headline;
    }


    public String getHeadline()
    {
        return this.headline;
    }


    public void setInterventionType(CsInterventionType interventionType)
    {
        this.interventionType = interventionType;
    }


    public CsInterventionType getInterventionType()
    {
        return this.interventionType;
    }


    public void setReason(CsEventReason reason)
    {
        this.reason = reason;
    }


    public CsEventReason getReason()
    {
        return this.reason;
    }


    public void setCreationNotes(String creationNotes)
    {
        this.creationNotes = creationNotes;
    }


    public String getCreationNotes()
    {
        return this.creationNotes;
    }


    public void setAttachments(List<MultipartFile> attachments)
    {
        this.attachments = attachments;
    }


    public List<MultipartFile> getAttachments()
    {
        return this.attachments;
    }
}
