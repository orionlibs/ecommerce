/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.spring;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An interface of an object that is capable of providing contents of a module library.
 */
public interface ModuleContentProvider
{
    /**
     * Prepares a destination stream for library contents.
     * <P>
     * Whatever logic is implemented in scope of this method, a returned stream should not prevent from writing content into
     * provided stream.
     * </P>
     *
     * @param destination
     *           destination stream compatible with library handler
     * @return actual destination stream to be used by library handler
     * @throws IOException
     *            whenever there is a problem with initializing library destination stream
     */
    OutputStream prepareStream(final OutputStream destination) throws IOException;


    /**
     * Sends all library content to provided output stream.
     *
     * @param destination
     *           library output stream
     * @throws IOException
     *            whenever there is a problem with providing library contents
     */
    void writeContent(final OutputStream destination) throws IOException;


    /**
     * Finalizes a module library.
     * <P>
     * Method is called once per module library just before a stream is closed.
     * </P>
     *
     * @param destination
     *           module library stream
     * @throws IOException
     *            whenever there is a problem with finalizing library destination stream
     */
    void finalizeStream(final OutputStream destination) throws IOException;
}
