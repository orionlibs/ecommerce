package de.hybris.platform.persistence.audit.internal.conditional;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "conditional-audit-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConditionalAuditConfig
{
    @XmlElement(name = "audit-group", required = true)
    private List<ConditionalAuditGroup> auditGroups;


    public ConditionalAuditGroup getGroup(String name)
    {
        return (ConditionalAuditGroup)this.auditGroups.stream()
                        .filter(i -> name.equals(i.getName()))
                        .findFirst().orElseThrow(RuntimeException::new);
    }


    public List<ConditionalAuditGroup> getGroups()
    {
        return this.auditGroups;
    }
}
