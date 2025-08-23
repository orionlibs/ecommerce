package de.hybris.bootstrap.beangenerator.definitions.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bean", propOrder = {"description", "hints", "imports", "annotations", "property"})
public class Bean extends AbstractPojo
{
    protected String description;
    protected Hints hints;
    protected Annotations annotations;
    protected List<Property> property;
    @XmlElement(name = "import")
    protected List<Import> imports;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "extends")
    protected String _extends;
    @XmlAttribute(name = "deprecated")
    protected String _deprecated;
    @XmlAttribute(name = "deprecatedSince")
    protected String _deprecatedSince;
    @XmlAttribute(name = "abstract")
    protected boolean _abstract;
    @XmlAttribute(name = "superEquals")
    protected boolean superEquals;


    public String getType()
    {
        return this.type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String value)
    {
        this.description = value;
    }


    public Hints getHints()
    {
        return this.hints;
    }


    public void setHints(Hints value)
    {
        this.hints = value;
    }


    public List<Property> getProperty()
    {
        if(this.property == null)
        {
            this.property = new ArrayList<>();
        }
        return this.property;
    }


    public List<Import> getImports()
    {
        if(this.imports == null)
        {
            this.imports = new ArrayList<>();
        }
        return this.imports;
    }


    public String getExtends()
    {
        return this._extends;
    }


    public void setExtends(String value)
    {
        this._extends = value;
    }


    public String getDeprecated()
    {
        return this._deprecated;
    }


    public void setDeprecated(String value)
    {
        this._deprecated = value;
    }


    public String getDeprecatedSince()
    {
        return this._deprecatedSince;
    }


    public void setDeprecatedSince(String value)
    {
        this._deprecatedSince = value;
    }


    public Annotations getAnnotations()
    {
        return this.annotations;
    }


    public void setAnnotations(Annotations annotations)
    {
        this.annotations = annotations;
    }


    public boolean isAbstract()
    {
        return this._abstract;
    }


    public void setAbstract(boolean _abstract)
    {
        this._abstract = _abstract;
    }


    public boolean isSuperEquals()
    {
        return this.superEquals;
    }


    public void setSuperEquals(boolean superEquals)
    {
        this.superEquals = superEquals;
    }
}
