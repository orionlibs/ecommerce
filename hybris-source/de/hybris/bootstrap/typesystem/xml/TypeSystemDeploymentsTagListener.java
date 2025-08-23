package de.hybris.bootstrap.typesystem.xml;

import java.util.Arrays;
import java.util.Collection;
import org.apache.log4j.Logger;

public class TypeSystemDeploymentsTagListener extends AbstractTypeSystemTagListener
{
    private static final Logger LOG = Logger.getLogger(TypeSystemDeploymentsTagListener.class);


    public TypeSystemDeploymentsTagListener(HybrisTypeSystemParser parser)
    {
        super(parser, "items");
    }


    public static String processItemDeployment(HybrisTypeSystemParser parser, String code, Object[] deployment)
    {
        String packageName = "de.hybris.platform.persistence";
        String deplName = parser.getCurrentExtensionName() + "_" + parser.getCurrentExtensionName();
        String tableName = (String)deployment[0];
        String propsTableName = (String)deployment[2];
        int tc = ((Integer)deployment[1]).intValue();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Adding deployment " + deplName);
        }
        parser.addDeployment("de.hybris.platform.persistence", deplName, "GenericItem", tc, false, true, false, tableName, propsTableName);
        return "de.hybris.platform.persistence." + deplName;
    }


    public static String processRelationDeployment(HybrisTypeSystemParser parser, String code, Object[] deployment)
    {
        String packageName = "de.hybris.platform.persistence.link";
        String deplName = parser.getCurrentExtensionName() + "_" + parser.getCurrentExtensionName();
        String tableName = (String)deployment[0];
        int tc = ((Integer)deployment[1]).intValue();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Adding deployment " + deplName);
        }
        parser.addDeployment("de.hybris.platform.persistence.link", deplName, "Link", tc, false, true, false, tableName, null);
        return "de.hybris.platform.persistence.link." + deplName;
    }


    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new AbstractTypeSystemTagListener[] {(AbstractTypeSystemTagListener)new RelationTypesTagListener(this), (AbstractTypeSystemTagListener)new ItemTypesTagListener(this)});
    }
}
