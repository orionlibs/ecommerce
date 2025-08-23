package de.hybris.platform.webservicescommons.testsupport.server;

import de.hybris.platform.testframework.runlistener.CustomRunListener;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.springframework.beans.factory.annotation.Required;

public class EmbeddedServerTestRunListener extends CustomRunListener
{
    private EmbeddedServerController embeddedServerController;


    public void testRunStarted(Description description)
    {
        NeedsEmbeddedServer nes = (NeedsEmbeddedServer)description.getAnnotation(NeedsEmbeddedServer.class);
        if(nes == null)
        {
            return;
        }
        this.embeddedServerController.start(nes.webExtensions());
    }


    public void testRunFinished(Result result)
    {
        this.embeddedServerController.stop();
    }


    public EmbeddedServerController getEmbeddedServerController()
    {
        return this.embeddedServerController;
    }


    @Required
    public void setEmbeddedServerController(EmbeddedServerController embeddedServerController)
    {
        this.embeddedServerController = embeddedServerController;
    }
}
