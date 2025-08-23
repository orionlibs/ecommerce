package com.hybris.backoffice.excel.importing.parser;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.ExcelImportContext;
import com.hybris.backoffice.excel.translators.classification.ClassificationAttributeHeaderValueCreator;
import com.hybris.backoffice.excel.translators.classification.ExcelClassificationJavaTypeTranslator;
import com.hybris.backoffice.excel.util.ExcelDateUtils;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.features.FeatureValue;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractClassificationRangeTranslatorTest
{
    @Mock
    private ClassificationAttributeHeaderValueCreator creator;
    @Mock
    private ExcelDateUtils excelDateUtils;
    @InjectMocks
    @Spy
    private ExcelClassificationJavaTypeTranslator translator = new ExcelClassificationJavaTypeTranslator();


    @Test
    public void shouldExportRangeValues()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ClassAttributeAssignmentModel assignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        Mockito.lenient().when(assignmentModel.getRange()).thenReturn(Boolean.valueOf(true));
        Mockito.lenient().when(assignmentModel.getAttributeType()).thenReturn(ClassificationAttributeTypeEnum.NUMBER);
        Mockito.lenient().when(attribute.getAttributeAssignment()).thenReturn(assignmentModel);
        String fromValue = "from";
        String toValue = "to";
        FeatureValue fromFeatureValue = mockFeatureValue("from");
        FeatureValue toFeatureValue = mockFeatureValue("to");
        Optional<String> output = this.translator.exportRange(attribute,
                        Lists.newArrayList((Object[])new Pair[] {(Pair)ImmutablePair.of(fromFeatureValue, toFeatureValue)}));
        Assertions.assertThat(output.isPresent()).isTrue();
        Assertions.assertThat(output.get()).isEqualTo("RANGE[from;to]");
    }


    @Test
    public void shouldImportRangeValues()
    {
        BDDMockito.given(this.creator.create((ExcelClassificationAttribute)Matchers.any(), (ExcelImportContext)Matchers.any())).willReturn("");
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ClassAttributeAssignmentModel assignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(assignmentModel.getRange()).willReturn(Boolean.valueOf(true));
        BDDMockito.given(assignmentModel.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.NUMBER);
        BDDMockito.given(attribute.getAttributeAssignment()).willReturn(assignmentModel);
        String left = "value1";
        String right = "value2";
        String cellValue = "RANGE[;value2]";
        ImportParameters importParameters = new ImportParameters(null, null, "RANGE[;value2]", null, createParams("value1", "value2"));
        Impex impex = this.translator.importData((ExcelAttribute)attribute, importParameters, new ExcelImportContext());
        Assertions.assertThat(((ImpexForType)impex.getImpexes().get(0)).getImpexTable().row(Integer.valueOf(0)).values())
                        .containsExactly(new Object[] {"value1,value2"});
    }


    @Test
    public void shouldRangeImportParametersBeSplitInMultiValueCase()
    {
        BDDMockito.given(this.creator.create((ExcelClassificationAttribute)Matchers.any(), (ExcelImportContext)Matchers.any())).willReturn("");
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ClassAttributeAssignmentModel assignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(assignmentModel.getRange()).willReturn(Boolean.valueOf(true));
        BDDMockito.given(assignmentModel.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.NUMBER);
        BDDMockito.given(attribute.getAttributeAssignment()).willReturn(assignmentModel);
        Map<String, String> from1 = new HashMap<>();
        from1.put(RangeParserUtils.prependFromPrefix("rawValue"), "val");
        Map<String, String> from2 = new HashMap<>();
        from2.put(RangeParserUtils.prependFromPrefix("rawValue"), "val");
        Map<String, String> to1 = new HashMap<>();
        to1.put(RangeParserUtils.prependToPrefix("rawValue"), "val");
        Map<String, String> to2 = new HashMap<>();
        to2.put(RangeParserUtils.prependToPrefix("rawValue"), "val");
        List<Map<String, String>> params = Lists.newArrayList((Object[])new Map[] {from1, from2, to1, to2});
        String range1 = "RANGE[val;val]";
        String range2 = "RANGE[val;val]";
        String cellValue = "RANGE[val;val],RANGE[val;val]";
        ImportParameters importParameters = new ImportParameters(null, null, "RANGE[val;val],RANGE[val;val]", null, params);
        Impex impex = this.translator.importData((ExcelAttribute)attribute, importParameters, new ExcelImportContext());
        ((ExcelClassificationJavaTypeTranslator)BDDMockito.then(this.translator).should(Mockito.times(4))).importSingle((ExcelClassificationAttribute)Matchers.any(), (ImportParameters)Matchers.any(), (ExcelImportContext)Matchers.any());
        Assertions.assertThat(((ImpexForType)impex.getImpexes().get(0)).getImpexTable().cellSet()).hasSize(1)
                        .hasOnlyOneElementSatisfying(cell -> Assertions.assertThat(cell.getValue()).isEqualTo("val,val,val,val"));
    }


    private FeatureValue mockFeatureValue(String value)
    {
        FeatureValue featureValue = (FeatureValue)Mockito.mock(FeatureValue.class);
        BDDMockito.given(featureValue.getValue()).willReturn(value);
        return featureValue;
    }


    private List<Map<String, String>> createParams(String left, String right)
    {
        Map<String, String> fromParams = new LinkedHashMap<>();
        fromParams.put(RangeParserUtils.prependFromPrefix("someValue1"), left);
        fromParams.put(RangeParserUtils.prependFromPrefix("rawValue"), left);
        Map<String, String> toParams = new LinkedHashMap<>();
        toParams.put(RangeParserUtils.prependToPrefix("someValue2"), right);
        toParams.put(RangeParserUtils.prependToPrefix("rawValue"), right);
        return Arrays.asList((Map<String, String>[])new Map[] {fromParams, toParams});
    }
}
