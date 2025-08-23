package de.hybris.platform.impex.jalo.imp;

public class ImpExReadingFailedResult extends ImpExWorkerResult
{
    public ImpExReadingFailedResult(ImpExReaderWorker worker, Exception error)
    {
        super((ImpExWorker)worker, null, null, error);
    }
}
