/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.breadboard;

/**
 *
 */
public class BreadboardSnippet
{
    private String codeSnippet;
    private String label;
    private String description;
    private String category;


    public BreadboardSnippet(final String codeSnippet, final String label, final String description, final String category)
    {
        super();
        this.codeSnippet = codeSnippet;
        this.label = label;
        this.description = description;
        this.category = category;
    }


    public BreadboardSnippet()
    {
        super();
    }


    public String getCodeSnippet()
    {
        return codeSnippet;
    }


    public void setCodeSnippet(final String codeSnippet)
    {
        this.codeSnippet = codeSnippet;
    }


    public String getLabel()
    {
        return label;
    }


    public void setLabel(final String label)
    {
        this.label = label;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(final String description)
    {
        this.description = description;
    }


    public String getCategory()
    {
        return category;
    }


    public void setCategory(final String category)
    {
        this.category = category;
    }
}
