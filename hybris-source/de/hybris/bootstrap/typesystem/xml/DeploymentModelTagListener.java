package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import java.util.Arrays;
import java.util.Collection;

public class DeploymentModelTagListener extends AbstractTypeSystemTagListener
{
    public DeploymentModelTagListener(HybrisTypeSystemParser parser)
    {
        super(parser, "model");
    }


    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new AbstractTypeSystemTagListener[] {(AbstractTypeSystemTagListener)new DBSchemaTagListener(this), (AbstractTypeSystemTagListener)new PackageTagListener(this)});
    }


    protected Object processStartElement(ObjectProcessor processor) throws ParseAbortException
    {
        getParser().startLoadingExtensionDeployments();
        return null;
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        getParser().finishedLoadingExtensionDeployments();
        return null;
    }
}
