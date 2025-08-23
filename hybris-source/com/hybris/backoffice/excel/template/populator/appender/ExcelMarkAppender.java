package com.hybris.backoffice.excel.template.populator.appender;

import java.util.function.BiFunction;
import org.springframework.core.Ordered;

public interface ExcelMarkAppender<T extends com.hybris.backoffice.excel.data.ExcelAttribute> extends Ordered, BiFunction<String, T, String>
{
}
