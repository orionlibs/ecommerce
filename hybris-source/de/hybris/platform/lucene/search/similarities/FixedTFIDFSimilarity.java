package de.hybris.platform.lucene.search.similarities;

import org.apache.lucene.search.similarities.ClassicSimilarity;

@Deprecated
public class FixedTFIDFSimilarity extends ClassicSimilarity
{
    public float idf(long docFreq, long numDocs)
    {
        return 1.0F;
    }


    public float tf(float freq)
    {
        return 1.0F;
    }
}
