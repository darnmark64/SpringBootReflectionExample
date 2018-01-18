package com.bits.assetTranslator.codex.readers;

import com.bits.assetTranslator.codex.beans.Asset;
import java.util.List;
import java.util.Map;

public interface AssetReaderInterface<T extends Asset> {
    public void generateMap() throws Exception;
    public Map<String, T> getMap() throws Exception;
    public void generateList() throws Exception;
    public List<T> getList() throws Exception;
}
