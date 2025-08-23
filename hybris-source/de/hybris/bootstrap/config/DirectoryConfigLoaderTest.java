package de.hybris.bootstrap.config;

import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.annotations.UnitTest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@UnitTest
public class DirectoryConfigLoaderTest
{
    private static final String LOCAL_PROPERTIES = "local.properties";
    private Path optConfigDir;


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


    @Before
    public void createTempPropertiesFile() throws IOException
    {
        this.optConfigDir = Files.createTempDirectory("opt-config", (FileAttribute<?>[])new FileAttribute[0]);
    }


    @After
    public void deleteTempPropertiesFile() throws IOException
    {
        FileUtils.deleteDirectory(this.optConfigDir.toFile());
    }


    @Test
    public void shouldReturnZeroPropsForNullPath()
    {
        Properties props = DirectoryConfigLoader.loadFromDir((File)null, "local.properties");
        Assertions.assertThat(props).hasSize(0);
    }


    @Test
    public void shouldNotLoadFromNonexistentPath()
    {
        createPropFile("10-local.properties", (Map)ImmutableMap.of("foo", "bar"));
        Properties props = DirectoryConfigLoader.loadFromDir(this.optConfigDir.resolve("aa").toFile(), "local.properties");
        Assertions.assertThat(props).hasSize(0);
    }


    @Test
    public void shouldNotLoadFromExistingPropertyFile()
    {
        File createdFile = createPropFile("10-local.properties", (Map)ImmutableMap.of("foo", "bar"));
        Properties props = DirectoryConfigLoader.loadFromDir(createdFile, "local.properties");
        Assertions.assertThat(props).hasSize(0);
    }


    @Test
    public void shouldReadSingleFileFromDir()
    {
        createPropFile("10-local.properties", (Map)ImmutableMap.of("foo", "bar"));
        Properties props = DirectoryConfigLoader.loadFromDir(this.optConfigDir.toFile(), "local.properties");
        Assertions.assertThat(props.getProperty("foo")).isEqualTo("bar");
    }


    @Test
    public void shouldNotReadFromFilesNotMatchingPattern()
    {
        createPropFile("1-local.properties", (Map)ImmutableMap.of("foo", "bar"));
        createPropFile("100-local.properties", (Map)ImmutableMap.of("foo2", "bar2"));
        Properties props = DirectoryConfigLoader.loadFromDir(this.optConfigDir.toFile(), "local.properties");
        Assertions.assertThat(props).hasSize(0);
    }


    @Test
    public void shouldMergeAndOverrideProperties()
    {
        createPropFile("10-local.properties", (Map)ImmutableMap.of("foo", "bar"));
        createPropFile("20-local.properties", (Map)ImmutableMap.of("foo", "bar2", "hybris.url", "www.hybris.com"));
        Properties props = DirectoryConfigLoader.loadFromDir(this.optConfigDir.toFile(), "local.properties");
        Assertions.assertThat(props).hasSize(2);
        Assertions.assertThat(props.getProperty("foo")).isEqualTo("bar2");
        Assertions.assertThat(props.getProperty("hybris.url")).isEqualTo("www.hybris.com");
    }


    @Test
    public void shouldSkipNotMatchingSuffix()
    {
        createPropFile("34-prop", (Map)ImmutableMap.of("foo", "bar"));
        createPropFile("45-prop", (Map)ImmutableMap.of("foo", "bar2", "hybris.url", "www.hybris.com"));
        createPropFile("50-local.properties", (Map)ImmutableMap.of("foo", "bar3", "role", "b2c"));
        Properties props = DirectoryConfigLoader.loadFromDir(this.optConfigDir.toFile(), "prop");
        Assertions.assertThat(props).hasSize(2);
        Assertions.assertThat(props.getProperty("foo")).isEqualTo("bar2");
        Assertions.assertThat(props.getProperty("hybris.url")).isEqualTo("www.hybris.com");
    }


    @Test
    public void shouldHandlePrioritiesUnderTen()
    {
        createPropFile("10-local.properties", (Map)ImmutableMap.of("foo", "bar3"));
        createPropFile("09-local.properties", (Map)ImmutableMap.of("foo", "bar2", "foo09", "09"));
        createPropFile("01-local.properties", (Map)ImmutableMap.of("foo", "bar1", "foo01", "01"));
        createPropFile("00-local.properties", (Map)ImmutableMap.of("foo", "bar0", "foo00", "00", "foo01", "00"));
        Properties props = DirectoryConfigLoader.loadFromDir(this.optConfigDir.toFile(), "local.properties");
        Assertions.assertThat(props).hasSize(4);
        Assertions.assertThat(props.getProperty("foo")).isEqualTo("bar3");
        Assertions.assertThat(props.getProperty("foo00")).isEqualTo("00");
        Assertions.assertThat(props.getProperty("foo01")).isEqualTo("01");
        Assertions.assertThat(props.getProperty("foo09")).isEqualTo("09");
    }
}
