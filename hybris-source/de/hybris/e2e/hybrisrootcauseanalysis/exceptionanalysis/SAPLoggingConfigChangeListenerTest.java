package de.hybris.e2e.hybrisrootcauseanalysis.exceptionanalysis;

import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.util.logging.HybrisLogger;
import de.hybris.platform.util.logging.log4j2.HybrisLoggerContext;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class SAPLoggingConfigChangeListenerTest extends ServicelayerTest
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private SAPLoggingProxyListener proxyLoggingListener;
    private SAPLoggingConfigChangeListener configChangeListener;


    @Before
    public void setUp()
    {
        HybrisLogger.removeAllListeners();
        updateRootLoggerLevel(Level.WARN);
        this.proxyLoggingListener = new SAPLoggingProxyListener();
        this.proxyLoggingListener.setEnableEvents(true);
        this.proxyLoggingListener.setLogSeverity("DEBUG");
        this.proxyLoggingListener.setTraceSeverity("DEBUG");
        this.proxyLoggingListener.setEnableTracing(false);
        this.proxyLoggingListener.setRotationCount(0);
        this.proxyLoggingListener.setRotationSize(0);
        this.configChangeListener = new SAPLoggingConfigChangeListener();
        this.configChangeListener.setListLogLoggingListener(this.proxyLoggingListener);
    }


    @Test
    public void testAllowedRuntimeConfigurationChanges() throws IOException
    {
        File currentLogFile = this.temporaryFolder.newFile("logTestAllowedRuntimeConfigurationChanges.log");
        File currentTraceFile = this.temporaryFolder.newFile("traceTestAllowedRuntimeConfigurationChanges.trc");
        this.proxyLoggingListener.setLogFilePath(currentLogFile.getAbsolutePath());
        this.proxyLoggingListener.setTraceFilePath(currentTraceFile.getAbsolutePath());
        this.proxyLoggingListener.init();
        String newLogSeverity = "FATAL";
        String newTraceSeverity = "ERROR";
        String newEnableTracing = "TRUE";
        this.configChangeListener.configChanged("e2e.rootcauseanalysis.exceptionanalysis.listlog.logseverity", "FATAL");
        this.configChangeListener.configChanged("e2e.rootcauseanalysis.exceptionanalysis.listlog.traceseverity", "ERROR");
        this.configChangeListener.configChanged("e2e.rootcauseanalysis.exceptionanalysis.listlog.enabletracing", "TRUE");
        Assert.assertEquals(this.proxyLoggingListener.getLogSeverity(), "FATAL");
        Assert.assertEquals(this.proxyLoggingListener.getTraceSeverity(), "ERROR");
        Assert.assertTrue(this.proxyLoggingListener.isEnableTracing());
    }


    @Test
    public void testProhibitedRuntimeConfigurationChanges() throws IOException
    {
        File currentLogFile = this.temporaryFolder.newFile("logTestProhibitedRuntimeConfigurationChanges.log");
        File currentTraceFile = this.temporaryFolder.newFile("traceTestProhibitedRuntimeConfigurationChanges.trc");
        this.proxyLoggingListener.setLogFilePath(currentLogFile.getAbsolutePath());
        this.proxyLoggingListener.setTraceFilePath(currentTraceFile.getAbsolutePath());
        this.proxyLoggingListener.init();
        String newLogPath = "someweridpath";
        String newTracePath = "someevenweirderpath";
        String newRotationCount = "352365";
        String newRotationSize = "34";
        this.configChangeListener.configChanged("e2e.rootcauseanalysis.exceptionanalysis.listlog.logpath", "someweridpath");
        this.configChangeListener.configChanged("e2e.rootcauseanalysis.exceptionanalysis.listlog.tracepath", "someevenweirderpath");
        this.configChangeListener.configChanged("e2e.rootcauseanalysis.exceptionanalysis.listlog.rotationcount", "352365");
        this.configChangeListener.configChanged("e2e.rootcauseanalysis.exceptionanalysis.listlog.rotationsize", "34");
        Assert.assertFalse((this.proxyLoggingListener.getLogFilePath() == "someweridpath"));
        Assert.assertFalse((this.proxyLoggingListener.getTraceFilePath() == "someevenweirderpath"));
        Assert.assertFalse((this.proxyLoggingListener.getRotationCount() == Integer.parseInt("352365")));
        Assert.assertFalse((this.proxyLoggingListener.getRotationSize() == Integer.parseInt("34")));
        String fileContent = FileUtils.readFileToString(currentLogFile);
        int matchCount = StringUtils.countMatches(fileContent, "either invalid or not changeable at runtime");
        Assert.assertEquals(4L, matchCount);
    }


    @After
    public void tearDown()
    {
        this.proxyLoggingListener.deregister();
        this.proxyLoggingListener.setEnableEvents(false);
    }


    private void updateRootLoggerLevel(Level rootLoggerLevel)
    {
        HybrisLoggerContext loggerCtx = (HybrisLoggerContext)LogManager.getContext(false);
        Configuration loggerCfg = loggerCtx.getConfiguration();
        LoggerConfig rootLoggerCfg = loggerCfg.getLoggerConfig("");
        rootLoggerCfg.setLevel(rootLoggerLevel);
        loggerCtx.updateLoggers();
    }
}
