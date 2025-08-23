/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.jaxb.wizard;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.hybris.cockpitng.config.jaxb.wizard package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory
{
    private static final QName _ViewTypeRenderer_QNAME = new QName("http://www.hybris.com/cockpitng/config/wizard-config", "renderer");


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.hybris.cockpitng.config.jaxb.wizard
     *
     */
    public ObjectFactory()
    {
        // default constructor
    }


    /**
     * Create an instance of {@link Flow }
     *
     */
    public Flow createFlow()
    {
        return new Flow();
    }


    /**
     * Create an instance of {@link PrepareType }
     *
     */
    public PrepareType createPrepareType()
    {
        return new PrepareType();
    }


    /**
     * Create an instance of {@link StepType }
     *
     */
    public StepType createStepType()
    {
        return new StepType();
    }


    /**
     * Create an instance of {@link SubflowType }
     *
     */
    public SubflowType createSubflowType()
    {
        return new SubflowType();
    }


    /**
     * Create an instance of {@link ViewType }
     *
     */
    public ViewType createViewType()
    {
        return new ViewType();
    }


    /**
     * Create an instance of {@link SaveType }
     *
     */
    public SaveType createSaveType()
    {
        return new SaveType();
    }


    /**
     * Create an instance of {@link IfType }
     *
     */
    public IfType createIfType()
    {
        return new IfType();
    }


    /**
     * Create an instance of {@link PropertyListType }
     *
     */
    public PropertyListType createPropertyListType()
    {
        return new PropertyListType();
    }


    /**
     * Create an instance of {@link CustomType }
     *
     */
    public CustomType createCustomType()
    {
        return new CustomType();
    }


    /**
     * Create an instance of {@link PropertyType }
     *
     */
    public PropertyType createPropertyType()
    {
        return new PropertyType();
    }


    /**
     * Create an instance of {@link SaveAllType }
     *
     */
    public SaveAllType createSaveAllType()
    {
        return new SaveAllType();
    }


    /**
     * Create an instance of {@link BackType }
     *
     */
    public BackType createBackType()
    {
        return new BackType();
    }


    /**
     * Create an instance of {@link InitializeType }
     *
     */
    public InitializeType createInitializeType()
    {
        return new InitializeType();
    }


    /**
     * Create an instance of {@link NextType }
     *
     */
    public NextType createNextType()
    {
        return new NextType();
    }


    /**
     * Create an instance of {@link DoneType }
     *
     */
    public DoneType createDoneType()
    {
        return new DoneType();
    }


    /**
     * Create an instance of {@link Parameter }
     *
     */
    public Parameter createParameter()
    {
        return new Parameter();
    }


    /**
     * Create an instance of {@link AssignType }
     *
     */
    public AssignType createAssignType()
    {
        return new AssignType();
    }


    /**
     * Create an instance of {@link NavigationType }
     *
     */
    public NavigationType createNavigationType()
    {
        return new NavigationType();
    }


    /**
     * Create an instance of {@link CancelType }
     *
     */
    public CancelType createCancelType()
    {
        return new CancelType();
    }


    /**
     * Create an instance of {@link ContentType }
     *
     */
    public ContentType createContentType()
    {
        return new ContentType();
    }


    /**
     * Create an instance of {@link InfoType }
     *
     */
    public InfoType createInfoType()
    {
        return new InfoType();
    }


    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link Renderer }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.hybris.com/cockpitng/config/wizard-config", name = "renderer", scope = ViewType.class)
    public JAXBElement<Renderer> createViewTypeRenderer(final Renderer value)
    {
        return new JAXBElement<>(_ViewTypeRenderer_QNAME, Renderer.class, ViewType.class, value);
    }
}
