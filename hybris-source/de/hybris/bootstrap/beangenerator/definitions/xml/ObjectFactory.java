package de.hybris.bootstrap.beangenerator.definitions.xml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory
{
    private static final QName _Beans_QNAME = new QName("", "beans");


    public Bean createBean()
    {
        return new Bean();
    }


    public AbstractPojos createAbstractPojos()
    {
        return new AbstractPojos();
    }


    public Property createProperty()
    {
        return new Property();
    }


    public Enum createEnum()
    {
        return new Enum();
    }


    @XmlElementDecl(namespace = "", name = "beans")
    public JAXBElement<AbstractPojos> createBeans(AbstractPojos value)
    {
        return new JAXBElement(_Beans_QNAME, AbstractPojos.class, null, value);
    }
}
