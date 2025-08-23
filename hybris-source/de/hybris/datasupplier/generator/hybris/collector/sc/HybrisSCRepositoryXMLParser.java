package de.hybris.datasupplier.generator.hybris.collector.sc;

import com.sap.sup.admin.sldsupplier.sc.DynamicProductInformationCreator;

public class HybrisSCRepositoryXMLParser extends DynamicProductInformationCreator
{
    public static final String HYBRIS_SC_REPOSITORY_XML = "./repositories/hybris_sc_repository.xml";


    public HybrisSCRepositoryXMLParser()
    {
        super("./repositories/hybris_sc_repository.xml", null);
    }
}
