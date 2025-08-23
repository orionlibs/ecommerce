package de.hybris.platform.impex.session;

public interface ProcessorSessionExecutionBody<X extends Exception>
{
    X getException();
}
