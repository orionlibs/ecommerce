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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class SAPLoggingProxyListenerTest extends ServicelayerTest
{
    private static Logger LOG = LogManager.getLogger();
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private static final String NEEDLE = "03df2bc152bf47a3b95bc17db333533a";
    private SAPLoggingProxyListener proxyLoggingListener;


    @Before
    public void setUp()
    {
        HybrisLogger.removeAllListeners();
        updateRootLoggerLevel(Level.WARN);
        this.proxyLoggingListener = new SAPLoggingProxyListener();
        this.proxyLoggingListener.setEnableEvents(true);
        this.proxyLoggingListener.setRotationCount(0);
        this.proxyLoggingListener.setRotationSize(0);
        this.proxyLoggingListener.setLogSeverity("INFO");
        this.proxyLoggingListener.setTraceSeverity("ERROR");
        this.proxyLoggingListener.setEnableTracing(false);
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


    @Test
    public void testRootLoggerLevelChangeEffect() throws IOException
    {
        File currentLogFile = this.temporaryFolder.newFile("logtestRootLoggerLevelChangeEffect.log");
        File currentTraceFile = this.temporaryFolder.newFile("tracetestRootLoggerLevelChangeEffect.trc");
        this.proxyLoggingListener.setLogFilePath(currentLogFile.getAbsolutePath());
        this.proxyLoggingListener.setTraceFilePath(currentTraceFile.getAbsolutePath());
        this.proxyLoggingListener.init();
        updateRootLoggerLevel(Level.OFF);
        this.proxyLoggingListener.setLogSeverity("DEBUG");
        LOG.debug("03df2bc152bf47a3b95bc17db333533a");
        LOG.info("03df2bc152bf47a3b95bc17db333533a");
        LOG.warn("03df2bc152bf47a3b95bc17db333533a");
        LOG.error("03df2bc152bf47a3b95bc17db333533a");
        LOG.fatal("03df2bc152bf47a3b95bc17db333533a");
        updateRootLoggerLevel(Level.INFO);
        Assert.assertTrue((currentLogFile.exists() && !currentLogFile.isDirectory()));
        String fileContent = FileUtils.readFileToString(currentLogFile);
        int matchCount = StringUtils.countMatches(fileContent, "03df2bc152bf47a3b95bc17db333533a");
        Assert.assertEquals(0L, matchCount);
        updateRootLoggerLevel(Level.DEBUG);
        this.proxyLoggingListener.setLogSeverity("DEBUG");
        LOG.debug("03df2bc152bf47a3b95bc17db333533a");
        LOG.info("03df2bc152bf47a3b95bc17db333533a");
        LOG.warn("03df2bc152bf47a3b95bc17db333533a");
        LOG.error("03df2bc152bf47a3b95bc17db333533a");
        LOG.fatal("03df2bc152bf47a3b95bc17db333533a");
        updateRootLoggerLevel(Level.INFO);
        Assert.assertTrue((currentLogFile.exists() && !currentLogFile.isDirectory()));
        fileContent = FileUtils.readFileToString(currentLogFile);
        matchCount = StringUtils.countMatches(fileContent, "03df2bc152bf47a3b95bc17db333533a");
        Assert.assertEquals(5L, matchCount);
        updateRootLoggerLevel(Level.WARN);
        this.proxyLoggingListener.setLogSeverity("DEBUG");
        LOG.debug("03df2bc152bf47a3b95bc17db333533a");
        LOG.info("03df2bc152bf47a3b95bc17db333533a");
        LOG.warn("03df2bc152bf47a3b95bc17db333533a");
        LOG.error("03df2bc152bf47a3b95bc17db333533a");
        LOG.fatal("03df2bc152bf47a3b95bc17db333533a");
        updateRootLoggerLevel(Level.INFO);
        Assert.assertTrue((currentLogFile.exists() && !currentLogFile.isDirectory()));
        fileContent = FileUtils.readFileToString(currentLogFile);
        matchCount = StringUtils.countMatches(fileContent, "03df2bc152bf47a3b95bc17db333533a");
        Assert.assertEquals(8L, matchCount);
    }


    @Test
    public void testLogFileCreation() throws IOException
    {
        File currentLogFile = this.temporaryFolder.newFile("logtestLogFileCreation.log");
        File currentTraceFile = this.temporaryFolder.newFile("tracetestLogFileCreation.trc");
        this.proxyLoggingListener.setLogFilePath(currentLogFile.getAbsolutePath());
        this.proxyLoggingListener.setTraceFilePath(currentTraceFile.getAbsolutePath());
        this.proxyLoggingListener.init();
        this.proxyLoggingListener.setLogSeverity("INFO");
        LOG.info("03df2bc152bf47a3b95bc17db333533a");
        Assert.assertTrue((currentLogFile.exists() && !currentLogFile.isDirectory()));
    }


    @Test
    public void testDynamicTraceToggling() throws IOException
    {
        File currentLogFile = this.temporaryFolder.newFile("logtestDynamicTraceToggling.log");
        File currentTraceFile = this.temporaryFolder.newFile("tracetestDynamicTraceToggling.trc");
        this.proxyLoggingListener.setLogFilePath(currentLogFile.getAbsolutePath());
        this.proxyLoggingListener.setTraceFilePath(currentTraceFile.getAbsolutePath());
        this.proxyLoggingListener.init();
        this.proxyLoggingListener.setTraceSeverity("ERROR");
        this.proxyLoggingListener.setEnableTracing(false);
        LOG.error("03df2bc152bf47a3b95bc17db333533a");
        LOG.error("03df2bc152bf47a3b95bc17db333533a");
        LOG.error("03df2bc152bf47a3b95bc17db333533a");
        Assert.assertTrue((currentTraceFile.exists() && !currentTraceFile.isDirectory()));
        Assert.assertTrue(FileUtils.readFileToString(currentTraceFile).isEmpty());
        this.proxyLoggingListener.setTraceSeverity("ERROR");
        this.proxyLoggingListener.setEnableTracing(true);
        LOG.debug("03df2bc152bf47a3b95bc17db333533a");
        LOG.warn("03df2bc152bf47a3b95bc17db333533a");
        LOG.error("03df2bc152bf47a3b95bc17db333533a");
        Assert.assertTrue((currentTraceFile.exists() && !currentTraceFile.isDirectory()));
        String fileContent = FileUtils.readFileToString(currentTraceFile);
        int matchCount = StringUtils.countMatches(fileContent, "03df2bc152bf47a3b95bc17db333533a");
        Assert.assertEquals(1L, matchCount);
        this.proxyLoggingListener.setEnableTracing(false);
        LOG.debug("03df2bc152bf47a3b95bc17db333533a");
        LOG.warn("03df2bc152bf47a3b95bc17db333533a");
        LOG.error("03df2bc152bf47a3b95bc17db333533a");
        Assert.assertTrue((currentTraceFile.exists() && !currentTraceFile.isDirectory()));
        fileContent = FileUtils.readFileToString(currentTraceFile);
        matchCount = StringUtils.countMatches(fileContent, "03df2bc152bf47a3b95bc17db333533a");
        Assert.assertEquals(1L, matchCount);
    }


    @Test
    public void testLogDebugSeverity() throws IOException
    {
        File currentLogFile = this.temporaryFolder.newFile("logtestLogDebugSeverity.log");
        File currentTraceFile = this.temporaryFolder.newFile("tracetestLogDebugSeverity.trc");
        this.proxyLoggingListener.setLogFilePath(currentLogFile.getAbsolutePath());
        this.proxyLoggingListener.setTraceFilePath(currentTraceFile.getAbsolutePath());
        this.proxyLoggingListener.init();
        updateRootLoggerLevel(Level.DEBUG);
        this.proxyLoggingListener.setLogSeverity("DEBUG");
        LOG.debug("03df2bc152bf47a3b95bc17db333533a");
        LOG.info("03df2bc152bf47a3b95bc17db333533a");
        LOG.warn("03df2bc152bf47a3b95bc17db333533a");
        LOG.error("03df2bc152bf47a3b95bc17db333533a");
        LOG.fatal("03df2bc152bf47a3b95bc17db333533a");
        updateRootLoggerLevel(Level.INFO);
        String fileContent = FileUtils.readFileToString(currentLogFile);
        int matchCount = StringUtils.countMatches(fileContent, "03df2bc152bf47a3b95bc17db333533a");
        Assert.assertEquals(5L, matchCount);
    }


    @Test
    public void testLogWarnSeverity() throws IOException
    {
        File currentLogFile = this.temporaryFolder.newFile("logtestLogWarnSeverity.log");
        File currentTraceFile = this.temporaryFolder.newFile("tracetestLogWarnSeverity.trc");
        this.proxyLoggingListener.setLogFilePath(currentLogFile.getAbsolutePath());
        this.proxyLoggingListener.setTraceFilePath(currentTraceFile.getAbsolutePath());
        this.proxyLoggingListener.init();
        this.proxyLoggingListener.setLogSeverity("DEBUG");
        LOG.debug("03df2bc152bf47a3b95bc17db333533a");
        LOG.info("03df2bc152bf47a3b95bc17db333533a");
        LOG.warn("03df2bc152bf47a3b95bc17db333533a");
        LOG.error("03df2bc152bf47a3b95bc17db333533a");
        LOG.fatal("03df2bc152bf47a3b95bc17db333533a");
        String fileContent = FileUtils.readFileToString(currentLogFile);
        int matchCount = StringUtils.countMatches(fileContent, "03df2bc152bf47a3b95bc17db333533a");
        Assert.assertEquals(3L, matchCount);
    }


    @Test
    public void testNoneSeverityForLogAndTrace() throws IOException
    {
        File currentLogFile = this.temporaryFolder.newFile("logtestNoneSeverityForLogAndTrace.log");
        File currentTraceFile = this.temporaryFolder.newFile("tracetestNoneSeverityForLogAndTrace.trc");
        this.proxyLoggingListener.setLogFilePath(currentLogFile.getAbsolutePath());
        this.proxyLoggingListener.setTraceFilePath(currentTraceFile.getAbsolutePath());
        this.proxyLoggingListener.init();
        this.proxyLoggingListener.setLogSeverity("NONE");
        this.proxyLoggingListener.setTraceSeverity("NONE");
        this.proxyLoggingListener.setEnableTracing(true);
        LOG.debug("03df2bc152bf47a3b95bc17db333533a");
        LOG.info("03df2bc152bf47a3b95bc17db333533a");
        LOG.warn("03df2bc152bf47a3b95bc17db333533a");
        LOG.error("03df2bc152bf47a3b95bc17db333533a");
        LOG.fatal("03df2bc152bf47a3b95bc17db333533a");
        String fileContent = FileUtils.readFileToString(currentLogFile);
        int matchCount = StringUtils.countMatches(fileContent, "03df2bc152bf47a3b95bc17db333533a");
        Assert.assertEquals(0L, matchCount);
        Assert.assertTrue((currentTraceFile.exists() && !currentTraceFile.isDirectory()));
        Assert.assertTrue(FileUtils.readFileToString(currentTraceFile).isEmpty());
    }
}
