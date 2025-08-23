package de.hybris.platform.impex.distributed.process;

import de.hybris.platform.processing.distributed.BatchCreationData;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImportBatchCreationData implements BatchCreationData
{
    public final String header;
    private final long remainingWorkLoad;
    private final String content;


    public ImportBatchCreationData(String header, Collection<String> valueLines)
    {
        Objects.requireNonNull(header, "header mustn't be null");
        Objects.requireNonNull(valueLines, "valueLines mustn't be null");
        this.header = header;
        this.remainingWorkLoad = valueLines.size() * 20L;
        this.content = Stream.<CharSequence>concat(Stream.of(header), valueLines.stream()).collect(Collectors.joining("\n"));
    }


    public String getHeader()
    {
        return this.header;
    }


    public long getRemainingWorkLoad()
    {
        return this.remainingWorkLoad;
    }


    String getContent()
    {
        return this.content;
    }
}
