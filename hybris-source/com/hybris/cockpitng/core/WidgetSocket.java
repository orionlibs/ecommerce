/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

import com.hybris.cockpitng.core.util.Validate;
import java.io.Serializable;

/**
 * Represents a single widget socket (input or output). See {@link WidgetDefinition#getInputs()} and
 * {@link WidgetDefinition#getOutputs()}.
 */
public class WidgetSocket implements Serializable
{
    private static final long serialVersionUID = 3502329224735154356L;
    private final Widget declaringWidget;
    private final int metadata;
    private String id;
    private Multiplicity dataMultiplicity;
    private SocketVisibility visibility;
    private String dataType;


    public WidgetSocket(final Widget declaringWidget, final int metadata)
    {
        Validate.assertTrue("Socket must be either IN or OUT.",
                        CNG.matchesMask(metadata, CNG.SOCKET_IN) || CNG.matchesMask(metadata, CNG.SOCKET_OUT));
        this.declaringWidget = declaringWidget;
        this.metadata = metadata;
    }


    public WidgetSocket(final Widget declaringWidget, final WidgetSocket socket)
    {
        this(declaringWidget, socket.getMetadata());
        this.id = socket.getId();
        this.dataMultiplicity = socket.getDataMultiplicity();
        this.visibility = socket.getVisibility();
        this.dataType = socket.getDataType();
    }


    public String getId()
    {
        return id;
    }


    public void setId(final String id)
    {
        this.id = id;
    }


    public Multiplicity getDataMultiplicity()
    {
        return dataMultiplicity;
    }


    public void setDataMultiplicity(final Multiplicity dataMultiplicity)
    {
        this.dataMultiplicity = dataMultiplicity;
    }


    public SocketVisibility getVisibility()
    {
        return visibility;
    }


    public void setVisibility(final SocketVisibility visibility)
    {
        this.visibility = visibility;
    }


    public String getDataType()
    {
        return dataType;
    }


    public void setDataType(final String dataType)
    {
        this.dataType = dataType;
    }


    public Widget getDeclaringWidget()
    {
        return declaringWidget;
    }


    public int getMetadata()
    {
        return metadata;
    }


    public boolean isInput()
    {
        return CNG.matchesMask(metadata, CNG.SOCKET_IN);
    }


    @Override
    public String toString()
    {
        return id + "[" + dataType + "," + dataMultiplicity + "]";
    }


    public enum Multiplicity
    {
        COLLECTION("collection"), SET("set"), LIST("list");
        private final String code;


        private Multiplicity(final String code)
        {
            this.code = code.intern();
        }


        public String getCode()
        {
            return this.code;
        }


        public String getType()
        {
            return getClass().getSimpleName();
        }
    }


    public enum SocketVisibility
    {
        EXTERNAL, INTERNAL, HIDDEN;


        public static SocketVisibility fromJAXB(final com.hybris.cockpitng.core.persistence.impl.jaxb.SocketVisibility visibility)
        {
            if(visibility == null)
            {
                return null;
            }
            switch(visibility)
            {
                case EXTERNAL:
                    return WidgetSocket.SocketVisibility.EXTERNAL;
                case INTERNAL:
                    return WidgetSocket.SocketVisibility.INTERNAL;
                case INVISIBLE:
                    return WidgetSocket.SocketVisibility.HIDDEN;
                default:
                    return WidgetSocket.SocketVisibility.EXTERNAL;
            }
        }
    }
}
