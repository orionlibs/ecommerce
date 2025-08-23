package de.hybris.y2ysync.task.runner.internal;

import com.google.common.collect.ImmutableList;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.enums.ChangeType;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

@UnitTest
public class ChangesExporterTest
{
    private static final String TYPE_CODE = "ComposedType";
    private static final String IMPEX_HEADER = "code[unique=true];name[lang=de]";


    @Test
    public void shouldGenerateEmptyOutputWhenThereIsNoChanges()
    {
        ImmutableList immutableList = ImmutableList.of();
        ExportScriptCreator creator = new ExportScriptCreator("code[unique=true];name[lang=de]", "ComposedType", (Collection)immutableList);
        String script = creator.createInsertUpdateExportScript();
        Assertions.assertThat(script).isEmpty();
    }


    @Test
    public void shouldGenerateOnlyInsertUpdateScriptForNewItem()
    {
        ImmutableList immutableList = ImmutableList.of(added(1L));
        ExportScriptCreator creator = new ExportScriptCreator("code[unique=true];name[lang=de]", "ComposedType", (Collection)immutableList);
        String script = creator.createInsertUpdateExportScript();
        Assertions.assertThat(insertedOrUpdatedPartOF(script)).containsOnly((Object[])new Long[] {Long.valueOf(1L)});
        Assertions.assertThat(removedPartOf(script)).isEmpty();
    }


    @Test
    public void shouldGenerateOnlyInsertUpdateScriptForModifiedItem()
    {
        ImmutableList immutableList = ImmutableList.of(modified(2L));
        ExportScriptCreator creator = new ExportScriptCreator("code[unique=true];name[lang=de]", "ComposedType", (Collection)immutableList);
        String script = creator.createInsertUpdateExportScript();
        Assertions.assertThat(insertedOrUpdatedPartOF(script)).containsOnly((Object[])new Long[] {Long.valueOf(2L)});
        Assertions.assertThat(removedPartOf(script)).isEmpty();
    }


    @Test
    public void shouldGenerateOnlyInsertUpdateScriptForModifiedAndAddedItems()
    {
        ImmutableList immutableList = ImmutableList.of(modified(4L), added(5L));
        ExportScriptCreator creator = new ExportScriptCreator("code[unique=true];name[lang=de]", "ComposedType", (Collection)immutableList);
        String script = creator.createInsertUpdateExportScript();
        Assertions.assertThat(insertedOrUpdatedPartOF(script)).containsOnly((Object[])new Long[] {Long.valueOf(5L), Long.valueOf(4L)});
        Assertions.assertThat(removedPartOf(script)).isEmpty();
    }


    private static final List<Long> removedPartOf(String script)
    {
        String removedPart = StringUtils.substringBetween(script, "REMOVE", "})\"\n");
        return extractPKs(removedPart);
    }


    private static final List<Long> insertedOrUpdatedPartOF(String script)
    {
        String insertUpdatePart = StringUtils.substringBetween(script, "INSERT_UPDATE", "})\"\n");
        return extractPKs(insertUpdatePart);
    }


    private static List<Long> extractPKs(String impex)
    {
        if(impex == null)
        {
            return (List<Long>)ImmutableList.of();
        }
        ImmutableList.Builder resultBuilder = ImmutableList.builder();
        Pattern pattern = Pattern.compile("\"\"(\\d+)\"\"");
        Matcher matcher = pattern.matcher(impex);
        while(matcher.find())
        {
            resultBuilder.add(Long.valueOf(matcher.group(1)));
        }
        return (List<Long>)resultBuilder.build();
    }


    private static ItemChangeDTO added(long pk)
    {
        return new ItemChangeDTO(Long.valueOf(pk), new Date(), ChangeType.NEW, "Added", "ComposedType", "testStream");
    }


    private static ItemChangeDTO modified(long pk)
    {
        return new ItemChangeDTO(Long.valueOf(pk), new Date(), ChangeType.MODIFIED, "Modified", "ComposedType", "testStream");
    }


    private static ItemChangeDTO removed(long pk)
    {
        return new ItemChangeDTO(Long.valueOf(pk), new Date(), ChangeType.DELETED, String.valueOf(pk), "ComposedType", "testStream");
    }
}
