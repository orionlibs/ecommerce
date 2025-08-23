package de.hybris.bootstrap.codegenerator;

public enum Visibility
{
    PUBLIC("public"),
    PROTECTED("protected"),
    PRIVATE("private"),
    PACKAGE_PROTECTED("");
    final String javaStr;


    Visibility(String java)
    {
        this.javaStr = java;
    }


    public String toString()
    {
        return this.javaStr;
    }
}
