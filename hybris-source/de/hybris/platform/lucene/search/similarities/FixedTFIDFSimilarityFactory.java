package de.hybris.platform.lucene.search.similarities;

import org.apache.lucene.search.similarities.Similarity;
import org.apache.solr.schema.SimilarityFactory;

@Deprecated
public class FixedTFIDFSimilarityFactory extends SimilarityFactory
{
    public Similarity getSimilarity()
    {
        return (Similarity)new FixedTFIDFSimilarity();
    }
}
