package com.bits.assetTranslator;

import com.bits.assetTranslator.codex.translators.ConstellationTranslator;
import com.bits.assetTranslator.codex.translators.GroundAssetTranslator;
import com.bits.assetTranslator.codex.translators.GroundSegmentTranslator;
import com.bits.assetTranslator.codex.translators.OrganizationTranslator;
import com.bits.assetTranslator.codex.translators.ProcessorTranslator;
import com.bits.assetTranslator.codex.translators.SpaceMissionTranslator;
import com.bits.assetTranslator.codex.translators.SpaceVehiclePayloadTranslator;
import com.bits.assetTranslator.codex.translators.SpaceVehicleTranslator;
import com.bits.assetTranslator.codex.translators.TranslationException;
import com.bits.assetTranslator.scope.beans.AssetEntity;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodexAssetTranslator {

    private static final Logger log = LoggerFactory.getLogger(CodexAssetTranslator.class);

    @Autowired
    ConstellationTranslator constellationTranslator;
    
    @Autowired
    GroundAssetTranslator groundAssetTranslator;
    
    @Autowired
    GroundSegmentTranslator groundSegmentTranslator;
    
    @Autowired
    OrganizationTranslator organizationTranslator;
    
    @Autowired
    ProcessorTranslator processorTranslator;
    
    @Autowired
    SpaceMissionTranslator spaceMissionTranslator;
    
    @Autowired
    SpaceVehicleTranslator spaceVehicleTranslator;
    
    @Autowired
    SpaceVehiclePayloadTranslator spaceVehiclePayloadTranslator;
    
    @Autowired
    ScopeAssetSender assetSender;

    public void run() {
        List<AssetEntity> scopeAssets = new ArrayList<>();
        try {
            //translate the assets into SCOPE AssetEntity
            scopeAssets.addAll(constellationTranslator.translate());
        } catch (TranslationException ex) {
            log.error(ex.getMessage());
        } catch (RuntimeException ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error(sw.toString());
        }
        try {
            //translate the assets into SCOPE AssetEntity
            scopeAssets.addAll(groundAssetTranslator.translate());
        } catch (TranslationException ex) {
            log.error(ex.getMessage());
        } catch (RuntimeException ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error(sw.toString());
        }
        try {
            //translate the assets into SCOPE AssetEntity
            scopeAssets.addAll(groundSegmentTranslator.translate());
        } catch (TranslationException ex) {
            log.error(ex.getMessage());
        } catch (RuntimeException ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error(sw.toString());
        }
        try {
            //translate the assets into SCOPE AssetEntity
            scopeAssets.addAll(organizationTranslator.translate());
        } catch (TranslationException ex) {
            log.error(ex.getMessage());
        } catch (RuntimeException ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error(sw.toString());
        }
        try {
            //translate the assets into SCOPE AssetEntity
            scopeAssets.addAll(processorTranslator.translate());
        } catch (TranslationException ex) {
            log.error(ex.getMessage());
        } catch (RuntimeException ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error(sw.toString());
        }
        try {
            //translate the assets into SCOPE AssetEntity
            scopeAssets.addAll(spaceMissionTranslator.translate());
        } catch (TranslationException ex) {
            log.error(ex.getMessage());
        } catch (RuntimeException ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error(sw.toString());
        }
        try {
            //translate the assets into SCOPE AssetEntity
            scopeAssets.addAll(spaceVehicleTranslator.translate());
        } catch (TranslationException ex) {
            log.error(ex.getMessage());
        } catch (RuntimeException ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error(sw.toString());
        }
        try {
            //translate the assets into SCOPE AssetEntity
            scopeAssets.addAll(spaceVehiclePayloadTranslator.translate());
        } catch (TranslationException ex) {
            log.error(ex.getMessage());
        } catch (RuntimeException ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error(sw.toString());
        }
        
        if (!scopeAssets.isEmpty()) log.info("scopeAssets = " + scopeAssets.toString());
        
        try {
            int assetsSent = assetSender.sendAssetsToScope(scopeAssets);
            log.info("CODEX Assets translated: " + scopeAssets.size());
            log.info("   Assets sent to SCOPE: " + assetsSent);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            log.error(sw.toString());
        }
    }
}
