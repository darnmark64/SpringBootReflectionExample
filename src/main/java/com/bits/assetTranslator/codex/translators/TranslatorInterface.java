package com.bits.assetTranslator.codex.translators;

import com.bits.assetTranslator.scope.beans.AssetEntity;
import java.util.List;

public interface TranslatorInterface {
    public List<AssetEntity> translate() throws Exception;
}
