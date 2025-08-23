package de.hybris.platform.test;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.type.AttributeDescriptorRemote;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.persistence.type.HierarchieTypeRemote;
import de.hybris.platform.persistence.type.TypeManagerEJB;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

@IntegrationTest
public class DumpHierarchy extends HybrisJUnit4TransactionalTest
{
    private static final Logger LOG = Logger.getLogger(DumpHierarchy.class.getName());


    @Test
    public void testDump()
    {
        try
        {
            dumpTypeHierarchie(SystemEJB.getInstance().getTypeManager().getRootComposedTypeForJaloClass(Item.class.getName()), 0);
        }
        catch(EJBItemNotFoundException e)
        {
            Assert.fail(e.getMessage());
        }
    }


    private void dumpTypeHierarchie(ComposedTypeRemote type, int level)
    {
        char[] spacesArray = new char[2 * level];
        Arrays.fill(spacesArray, ' ');
        String spaces = new String(spacesArray);
        LOG.info(spaces + spaces);
        TypeManagerEJB typeManagerEJB = SystemEJB.getInstance().getTypeManager();
        for(Iterator<AttributeDescriptorRemote> iterator = typeManagerEJB.getDeclaredAttributeDescriptors(type).iterator(); iterator.hasNext(); )
        {
            AttributeDescriptorRemote attributeDescriptorRemote = iterator.next();
            LOG.info(spaces + " " + spaces + "(" + attributeDescriptorRemote.getQualifier() + "):" +
                            modifiersToString(attributeDescriptorRemote.getModifiers()));
        }
        for(Iterator<ComposedTypeRemote> iter = typeManagerEJB.getSubTypes((HierarchieTypeRemote)type).iterator(); iter.hasNext(); )
        {
            dumpTypeHierarchie(iter.next(), level + 1);
        }
    }


    private static String modifiersToString(int mod)
    {
        StringBuilder stringBuilder = new StringBuilder();
        if((mod & 0x1) != 0)
        {
            stringBuilder.append('r');
        }
        if((mod & 0x2) != 0)
        {
            stringBuilder.append('w');
        }
        if((mod & 0x4) != 0)
        {
            stringBuilder.append('R');
        }
        if((mod & 0x8) != 0)
        {
            stringBuilder.append('O');
        }
        if((mod & 0x10) != 0)
        {
            stringBuilder.append('S');
        }
        if((mod & 0x20) != 0)
        {
            stringBuilder.append('P');
        }
        if((mod & 0x100) != 0)
        {
            stringBuilder.append('P');
        }
        if((mod & 0x200) != 0)
        {
            stringBuilder.append('L');
        }
        return stringBuilder.toString();
    }
}
