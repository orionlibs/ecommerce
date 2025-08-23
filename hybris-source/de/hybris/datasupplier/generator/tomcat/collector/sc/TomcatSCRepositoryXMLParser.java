package de.hybris.datasupplier.generator.tomcat.collector.sc;

import com.sap.sup.admin.sldsupplier.sc.DynamicProductInformationCreator;

public class TomcatSCRepositoryXMLParser extends DynamicProductInformationCreator
{
    public static final String TOMCAT_SC_REPOSITORY_XML = "./repositories/tomcat_sc_repository.xml";


    public TomcatSCRepositoryXMLParser()
    {
        super("./repositories/tomcat_sc_repository.xml", null);
    }
}
