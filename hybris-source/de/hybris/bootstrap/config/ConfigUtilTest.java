package de.hybris.bootstrap.config;

import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.util.Utilities;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@ManualTest
@RunWith(MockitoJUnitRunner.class)
public class ConfigUtilTest
{
    private static final Logger LOG = Logger.getLogger(ConfigUtilTest.class);
    private static final String BUILD_VERSION_KEY = "build.version";
    private static final String NEW_BUILD_VERSION_VALUE = "bar";
    private static final String NEW_FOO_KEY = "test.runtime.foo";
    private static final String NEW_FOO_VALUE = "1111.1111.111.0";
    private static final String NEW_FOO_KEY_SPECIALCHARS = "test.rüntime.föö";
    private static final String NEW_FOO_VALUE_SPECIALCHARS = "11$11.11%11.11äöüß1.0";
    private final String HYBRIS_OPT_CONFIG_DIR = "hybris.optional.config.dir";
    private Path runtimePropertiesFile;
    private Path runtimePropertiesFileUTF8;
    private Path optConfigDir;
    @Mock
    ConfigUtil.EnvProvider mockedEnvProvider;
    private PrintStream systemOut;


    @Before
    public void createTempPropertiesFile() throws IOException
    {
        Properties runtimeProperties = new Properties();
        runtimeProperties.setProperty("test.runtime.foo", "1111.1111.111.0");
        runtimeProperties.setProperty("test.rüntime.föö", "11$11.11%11.11äöüß1.0");
        runtimeProperties.setProperty("build.version", "bar");
        this.runtimePropertiesFile = Files.createTempFile("runtime", ".properties", (FileAttribute<?>[])new FileAttribute[0]);
        OutputStream out = new FileOutputStream(this.runtimePropertiesFile.toFile());
        runtimeProperties.store(out, "");
        out.close();
        this.runtimePropertiesFileUTF8 = Files.createTempFile("runtimeUTF8", ".properties", (FileAttribute<?>[])new FileAttribute[0]);
        out = new FileOutputStream(this.runtimePropertiesFileUTF8.toFile());
        OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
        runtimeProperties.store(writer, "");
        writer.close();
        out.close();
        this.optConfigDir = Files.createTempDirectory("opt-config", (FileAttribute<?>[])new FileAttribute[0]);
        this.systemOut = System.out;
    }


    @After
    public void deleteTempPropertiesFile() throws IOException
    {
        Files.delete(this.runtimePropertiesFile);
        FileUtils.deleteDirectory(this.optConfigDir.toFile());
        System.clearProperty("hybris.config.file.encoding");
        System.out.flush();
        System.setOut(this.systemOut);
    }


    @Test
    public void loadFromUTF8Properties() throws IOException
    {
        PlatformConfig platformConfig = Utilities.getPlatformConfig();
        Properties platformWithRuntimeProperties = new Properties();
        Mockito.when(this.mockedEnvProvider.getRuntimePropertiesPath()).thenReturn(this.runtimePropertiesFileUTF8.toString());
        ConfigUtil.envProvider = this.mockedEnvProvider;
        System.setProperty("hybris.config.file.encoding", "UTF-8");
        ConfigUtil.loadRuntimeProperties(platformWithRuntimeProperties, platformConfig);
        Assertions.assertThat(platformWithRuntimeProperties.getProperty("test.runtime.foo")).isEqualTo("1111.1111.111.0");
        Assertions.assertThat(platformWithRuntimeProperties.getProperty("test.rüntime.föö")).isEqualTo("11$11.11%11.11äöüß1.0");
        Assertions.assertThat(platformWithRuntimeProperties.getProperty("build.version")).isEqualTo("bar");
    }


    @Test
    public void loadFromPropertiesWithUnsupportedEncoding() throws IOException
    {
        PlatformConfig platformConfig = Utilities.getPlatformConfig();
        Properties platformWithRuntimeProperties = new Properties();
        Mockito.when(this.mockedEnvProvider.getRuntimePropertiesPath()).thenReturn(this.runtimePropertiesFile.toString());
        ConfigUtil.envProvider = this.mockedEnvProvider;
        System.setProperty("hybris.config.file.encoding", "No-Such-Charset");
        ConfigUtil.loadRuntimeProperties(platformWithRuntimeProperties, platformConfig);
        Assertions.assertThat(platformWithRuntimeProperties.getProperty("test.runtime.foo")).isEqualTo("1111.1111.111.0");
        Assertions.assertThat(platformWithRuntimeProperties.getProperty("test.rüntime.föö")).isEqualTo("11$11.11%11.11äöüß1.0");
        Assertions.assertThat(platformWithRuntimeProperties.getProperty("build.version")).isEqualTo("bar");
    }


    @Test
    public void loadFromUTF8PropertiesWithWrongConfig() throws IOException
    {
        PlatformConfig platformConfig = Utilities.getPlatformConfig();
        Properties platformWithRuntimeProperties = new Properties();
        Mockito.when(this.mockedEnvProvider.getRuntimePropertiesPath()).thenReturn(this.runtimePropertiesFileUTF8.toString());
        ConfigUtil.envProvider = this.mockedEnvProvider;
        ConfigUtil.loadRuntimeProperties(platformWithRuntimeProperties, platformConfig);
        Assertions.assertThat(platformWithRuntimeProperties.getProperty("test.rüntime.föö")).isNullOrEmpty();
        Assertions.assertThat(platformWithRuntimeProperties.getProperty("build.version")).isEqualTo("bar");
        System.setProperty("hybris.config.file.encoding", "ISO-8859-1");
        ConfigUtil.loadRuntimeProperties(platformWithRuntimeProperties, platformConfig);
        Assertions.assertThat(platformWithRuntimeProperties.getProperty("test.rüntime.föö")).isNullOrEmpty();
        Assertions.assertThat(platformWithRuntimeProperties.getProperty("build.version")).isEqualTo("bar");
    }


    @Test
    public void loadAdditionalRuntimeProperties()
    {
        PlatformConfig platformConfig = Utilities.getPlatformConfig();
        Properties platformProperties = new Properties();
        ConfigUtil.loadRuntimeProperties(platformProperties, platformConfig);
        Assertions.assertThat(platformProperties.getProperty("build.version")).isNotEqualTo("bar");
        Assertions.assertThat(platformProperties.getProperty("test.runtime.foo")).isNullOrEmpty();
        Properties platformWithRuntimeProperties = new Properties();
        Mockito.when(this.mockedEnvProvider.getRuntimePropertiesPath()).thenReturn(this.runtimePropertiesFile.toString());
        ConfigUtil.envProvider = this.mockedEnvProvider;
        ConfigUtil.loadRuntimeProperties(platformWithRuntimeProperties, platformConfig);
        Assertions.assertThat(platformWithRuntimeProperties.getProperty("test.runtime.foo")).isEqualTo("1111.1111.111.0");
        Assertions.assertThat(platformWithRuntimeProperties.getProperty("build.version")).isEqualTo("bar");
    }


    @Test
    public void loadOptionalConfig()
    {
        PlatformConfig platformConfig = Utilities.getPlatformConfig();
        Properties platformProperties = new Properties();
        platformProperties.setProperty("hybris.optional.config.dir", this.optConfigDir.toString());
        createPropFile("34-local.properties", (Map)ImmutableMap.of("fooz", "bar"));
        createPropFile("45-local.properties", (Map)ImmutableMap.of("fooz", "bar2", "hybris.url", "www.hybris.com"));
        ConfigUtil.loadRuntimeProperties(platformProperties, platformConfig);
        if(platformProperties.getProperty("hybris.optional.config.dir").equals(this.optConfigDir.toString()))
        {
            Assertions.assertThat(platformProperties.getProperty("fooz")).isEqualTo("bar2");
            Assertions.assertThat(platformProperties.getProperty("hybris.url")).isEqualTo("www.hybris.com");
        }
        else
        {
            LOG.info("hybris.optional.config.dir was overwritten - skipping test");
        }
    }


    @Test
    public void loadOptionalConfigViaEnvVariable()
    {
        PlatformConfig platformConfig = Utilities.getPlatformConfig();
        Properties platformProperties = new Properties();
        Mockito.when(this.mockedEnvProvider.getOptionalConfigDirPath()).thenReturn(this.optConfigDir.toString());
        ConfigUtil.envProvider = this.mockedEnvProvider;
        createPropFile("34-local.properties", (Map)ImmutableMap.of("fooz", "bar"));
        createPropFile("45-local.properties", (Map)ImmutableMap.of("fooz", "bar3", "hybris.url", "www.hybris.com"));
        ConfigUtil.loadRuntimeProperties(platformProperties, platformConfig);
        Assertions.assertThat(platformProperties.getProperty("fooz")).isEqualTo("bar3");
        Assertions.assertThat(platformProperties.getProperty("hybris.url")).isEqualTo("www.hybris.com");
    }


    @Test
    public void shouldLogOnlyOnceUsageOfOptionalPropertiesEnvVariable()
    {
        PlatformConfig platformConfig = Utilities.getPlatformConfig();
        Properties platformProperties = new Properties();
        PrintStream out = (PrintStream)Mockito.spy(this.systemOut);
        System.setOut(out);
        Mockito.when(this.mockedEnvProvider.getOptionalConfigDirPath()).thenReturn(this.optConfigDir.toString());
        ConfigUtil.envProvider = this.mockedEnvProvider;
        createPropFile("34-local.properties", (Map)ImmutableMap.of("fooz", "bar"));
        createPropFile("45-local.properties", (Map)ImmutableMap.of("fooz", "bar3", "hybris.url", "www.hybris.com"));
        ConfigUtil.loadRuntimeProperties(platformProperties, platformConfig);
        ConfigUtil.loadRuntimeProperties(platformProperties, platformConfig);
        ((PrintStream)Mockito.verify(out, Mockito.times(1))).print("HYBRIS_OPT_CONFIG_DIR environment variable is set.");
        ((PrintStream)Mockito.verify(out, Mockito.times(1))).println(" Loading optional hybris properties from " + this.optConfigDir.toString() + " directory");
    }


    @Test
    public void loadFromEnv()
    {
        PlatformConfig platformConfig = Utilities.getPlatformConfig();
        Properties platformProperties = new Properties();
        Map<String, String> testEnv = new HashMap<>();
        testEnv.put("not_this_one", "xxx");
        testEnv.put("ynot_that_one", "xxx");
        testEnv.put("y_first_second_third", "true");
        testEnv.put("y_aaa__bb_c__dd", "false");
        Mockito.when(this.mockedEnvProvider.getEnv()).thenReturn(testEnv);
        ConfigUtil.envProvider = this.mockedEnvProvider;
        ConfigUtil.loadRuntimeProperties(platformProperties, platformConfig);
        Assertions.assertThat(platformProperties.getProperty("not_this_one")).isNull();
        Assertions.assertThat(platformProperties.getProperty("ynot_that_one")).isNull();
        Assertions.assertThat(platformProperties.getProperty("first.second.third")).isEqualTo("true");
        Assertions.assertThat(platformProperties.getProperty("aaa_bb.c_dd")).isEqualTo("false");
    }


    @Test
    public void getPropertyOrEnvShouldReturnNullIfNotSet()
    {
        Assertions.assertThat(ConfigUtil.getPropertyOrEnv("foo")).isNull();
    }


    @Test
    public void getPropertyOrEnvShouldReturnPropertyIfSet()
    {
        ConfigUtil.envProvider = this.mockedEnvProvider;
        Mockito.when(this.mockedEnvProvider.getenv("foo")).thenReturn("bar");
        Assertions.assertThat(ConfigUtil.getPropertyOrEnv("foo")).isEqualTo("bar");
    }


    @Test
    public void getPropertyOrEnvShouldReturnEnvIfSet()
    {
        ConfigUtil.envProvider = this.mockedEnvProvider;
        Mockito.when(this.mockedEnvProvider.getProperty("foo")).thenReturn("bar");
        Assertions.assertThat(ConfigUtil.getPropertyOrEnv("foo")).isEqualTo("bar");
    }


    @Test
    public void getPropertyOrEnvShouldPreferEnvToProperty()
    {
        ConfigUtil.envProvider = this.mockedEnvProvider;
        Mockito.when(this.mockedEnvProvider.getenv("foo")).thenReturn("env_value");
        Mockito.when(this.mockedEnvProvider.getProperty("foo")).thenReturn("property_value");
        Assertions.assertThat(ConfigUtil.getPropertyOrEnv("foo")).isEqualTo("env_value");
    }


    @Test
    public void shouldOverwritePropertiesFromEnvs()
    {
        PlatformConfig platformConfig = Utilities.getPlatformConfig();
        Properties platformProperties = new Properties();
        ConfigUtil.envProvider = this.mockedEnvProvider;
        Mockito.when(this.mockedEnvProvider.getenv("HTTP_CONNECTOR_SECURE")).thenReturn("envOverwritten");
        Mockito.when(this.mockedEnvProvider.getenv("HTTP_PORT")).thenReturn("envOverwritten");
        Mockito.when(this.mockedEnvProvider.getenv("HTTPS_PORT")).thenReturn("envOverwritten");
        ConfigUtil.loadRuntimeProperties(platformProperties, platformConfig);
        Assertions.assertThat(platformProperties.getProperty("tomcat.http.connector.secure")).isEqualTo("envOverwritten");
        Assertions.assertThat(platformProperties.getProperty("tomcat.http.port")).isEqualTo("envOverwritten");
        Assertions.assertThat(platformProperties.getProperty("tomcat.https.port")).isEqualTo("envOverwritten");
    }


    @Test
    public void shouldOverwritePropertiesFromProperties()
    {
        PlatformConfig platformConfig = Utilities.getPlatformConfig();
        Properties platformProperties = new Properties();
        ConfigUtil.envProvider = this.mockedEnvProvider;
        Mockito.when(this.mockedEnvProvider.getProperty("HTTP_CONNECTOR_SECURE")).thenReturn("propOverwritten");
        Mockito.when(this.mockedEnvProvider.getProperty("HTTP_PORT")).thenReturn("propOverwritten");
        Mockito.when(this.mockedEnvProvider.getProperty("HTTPS_PORT")).thenReturn("propOverwritten");
        ConfigUtil.loadRuntimeProperties(platformProperties, platformConfig);
        Assertions.assertThat(platformProperties.getProperty("tomcat.http.connector.secure")).isEqualTo("propOverwritten");
        Assertions.assertThat(platformProperties.getProperty("tomcat.http.port")).isEqualTo("propOverwritten");
        Assertions.assertThat(platformProperties.getProperty("tomcat.https.port")).isEqualTo("propOverwritten");
    }


    private File createPropFile(String name, Map<?, ?> props)
    {
        Properties properties = new Properties();
        properties.putAll(props);
        File file = this.optConfigDir.resolve(name).toFile();
        try
        {
            FileOutputStream fileOut = new FileOutputStream(file);
            properties.store(fileOut, name);
            fileOut.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Couldn't create temp property file for test");
        }
        return file;
    }
}
