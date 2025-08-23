package com.hybris.backoffice.excel.importing.data;

import java.util.Optional;

public interface TypeSystem<SYSTEMROW>
{
    Optional<SYSTEMROW> findRow(String paramString);
}
