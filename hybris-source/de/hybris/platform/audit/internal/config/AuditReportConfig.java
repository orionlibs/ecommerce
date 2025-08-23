package de.hybris.platform.audit.internal.config;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "audit-report-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class AuditReportConfig
{
    private static final String DEFAULT_PROVIDER_BEAN_ID = "auditRecordsProvider";
    @XmlIDREF
    @XmlElement(name = "given-root-type")
    private Type givenRootType;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String auditRecordsProvider;
    @XmlElementWrapper
    @XmlElement(name = "type")
    private List<Type> types;
    @XmlAttribute(name = "mode")
    private String combineMode;


    private AuditReportConfig()
    {
    }


    private AuditReportConfig(Builder builder)
    {
        this.givenRootType = builder.givenRootType;
        this.name = builder.name;
        this.auditRecordsProvider = builder.auditRecordsProvider;
        this.types = builder.types;
        this.combineMode = builder.combineMode;
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public Type getGivenRootType()
    {
        return this.givenRootType;
    }


    public String getName()
    {
        return this.name;
    }


    public String getAuditRecordsProvider()
    {
        return (this.auditRecordsProvider == null) ? "auditRecordsProvider" : this.auditRecordsProvider;
    }


    public List<Type> getTypes()
    {
        return (List<Type>)ImmutableList.copyOf(returnOnlyValidTypes(this.types));
    }


    public List<Type> getAllTypes()
    {
        return (List<Type>)ImmutableList.copyOf(this.types);
    }


    public Type getType(String code)
    {
        Objects.requireNonNull(code, "code is required to do a search within types");
        return returnOnlyValidTypes(this.types).stream().filter(t -> code.equals(t.getCode())).findFirst().orElse(null);
    }


    public Type getTypeInAll(String code)
    {
        Objects.requireNonNull(code, "code is required to do a search within types");
        return this.types.stream().filter(t -> code.equals(t.getCode())).findFirst().orElse(null);
    }


    private List<Type> returnOnlyValidTypes(List<Type> types)
    {
        return (List<Type>)types.stream().filter(Type::isValid).collect(Collectors.toList());
    }


    public String getCombineMode()
    {
        return this.combineMode;
    }
}
