package com.bits.assetTranslator.codex.readers;

import com.bits.assetTranslator.codex.beans.Asset;
import com.bits.assetTranslator.codex.beans.LoginToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class AssetReader<T extends Asset> implements AssetReaderInterface<T> {

    static final Logger log = LoggerFactory.getLogger(AssetReader.class);
    
    @Autowired
    @Qualifier("codexToken")
    LoginToken codexLoginToken;
    
    @Autowired
    @Qualifier("codexUrl")
    String codexBaseUrl;
}
