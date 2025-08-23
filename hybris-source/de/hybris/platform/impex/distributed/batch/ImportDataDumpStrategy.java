package de.hybris.platform.impex.distributed.batch;

import de.hybris.platform.impex.jalo.AbstractCodeLine;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import java.util.List;

public interface ImportDataDumpStrategy
{
    String dump(List<ValueLine> paramList);


    String dump(ValueLine paramValueLine);


    String dump(HeaderDescriptor paramHeaderDescriptor);


    String dump(AbstractCodeLine paramAbstractCodeLine);
}
