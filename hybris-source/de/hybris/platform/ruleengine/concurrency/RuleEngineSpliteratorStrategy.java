package de.hybris.platform.ruleengine.concurrency;

public interface RuleEngineSpliteratorStrategy
{
    int getNumberOfThreads();


    static int getPartitionSize(int totalSize, int numberOfPartitions)
    {
        int partitionSize = totalSize;
        if(numberOfPartitions > 1 && partitionSize >= numberOfPartitions * numberOfPartitions)
        {
            if(totalSize % numberOfPartitions == 0)
            {
                partitionSize = totalSize / numberOfPartitions;
            }
            else if(numberOfPartitions > 1)
            {
                partitionSize = totalSize / (numberOfPartitions - 1);
            }
        }
        return partitionSize;
    }
}
