package de.hybris.platform.persistence.audit.internal.conditional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "audit-group")
public class ConditionalAuditGroup
{
    @XmlAttribute(name = "name")
    private String name;
    @XmlElement(name = "condition")
    private String condition;
    @XmlElement(name = "type")
    ConditionalAuditType rootType;
    private Map<String, ConditionalAuditType> typeToConfig;


    private ConditionalAuditGroup()
    {
    }


    public ConditionalAuditGroup(String name, String condition, ConditionalAuditType rootType)
    {
        this.name = name;
        this.condition = condition;
        this.rootType = rootType;
        this.typeToConfig = prepareTypeToConfigMapping();
    }


    private Map<String, ConditionalAuditType> prepareTypeToConfigMapping()
    {
        Map<String, ConditionalAuditType> typeToConfigMapping = new HashMap<>();
        traverse(type -> {
            if(typeToConfigMapping.containsKey(type.getCode()))
            {
                throw new ConditionalAuditException("Type already exists in conditional audit configuration!");
            }
            typeToConfigMapping.put(type.getCode(), type);
        });
        return typeToConfigMapping;
    }


    public void traverse(Consumer<ConditionalAuditType> consumer)
    {
        Queue<ConditionalAuditType> queue = new LinkedList<>();
        ConditionalAuditType type = this.rootType;
        do
        {
            queue.addAll(type.getChildren());
            consumer.accept(type);
            type = queue.poll();
        }
        while(type != null);
    }


    public String getName()
    {
        return this.name;
    }


    public String getCondition()
    {
        return this.condition;
    }


    public ConditionalAuditType getRootType()
    {
        return this.rootType;
    }


    public ConditionalAuditType getConfigForType(String type)
    {
        if(this.typeToConfig == null)
        {
            this.typeToConfig = prepareTypeToConfigMapping();
        }
        return this.typeToConfig.get(type);
    }
}
