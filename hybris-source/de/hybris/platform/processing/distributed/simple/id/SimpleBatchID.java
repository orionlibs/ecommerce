package de.hybris.platform.processing.distributed.simple.id;

import de.hybris.platform.core.Registry;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import java.util.Objects;

public class SimpleBatchID
{
    protected static final String DELIMITER = "_";
    private final BatchType batchType;
    private final String postfix;


    protected SimpleBatchID(BatchType batchType, String postfix)
    {
        this.batchType = Objects.<BatchType>requireNonNull(batchType, "batchType is required");
        this.postfix = Objects.<String>requireNonNull(postfix, "postfix is required");
    }


    public static SimpleBatchID asInitialBatch()
    {
        return new SimpleBatchID(BatchType.INITIAL, generateCode());
    }


    public static SimpleBatchID asResultBatchID()
    {
        return new SimpleBatchID(BatchType.RESULT, generateCode());
    }


    public static SimpleBatchID asInputBatchID()
    {
        return new SimpleBatchID(BatchType.INPUT, generateCode());
    }


    public boolean isInitial()
    {
        return (this.batchType == BatchType.INITIAL);
    }


    public boolean isInput()
    {
        return (this.batchType == BatchType.INPUT);
    }


    public boolean isResult()
    {
        return (this.batchType == BatchType.RESULT);
    }


    public String toString()
    {
        return "" + this.batchType + "_" + this.batchType;
    }


    protected static String generateCode()
    {
        PersistentKeyGenerator generator = (PersistentKeyGenerator)Registry.getApplicationContext().getBean("distributedProcessCodeGenerator", PersistentKeyGenerator.class);
        return (String)generator.generate();
    }
}
