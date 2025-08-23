package de.hybris.platform.cache2;

public interface ObjectKey<T>
{
    Object getSignature();


    boolean getExpired();


    ObjectCreator<T> getObjectCreator();
}
