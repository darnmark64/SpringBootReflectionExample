/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bits.assetTranslator.util;

import com.bits.assetTranslator.codex.beans.Capability;
import com.bits.assetTranslator.codex.beans.Constellation;
import com.bits.assetTranslator.codex.beans.GroundSegment;
import com.bits.assetTranslator.codex.beans.LoginToken;
import com.bits.assetTranslator.codex.translators.ConstellationTranslator;
import com.bits.assetTranslator.codex.translators.ScopeAttributeMapper;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 *
 * @author darnellm
 */
public class CodexUtils {

    public static HttpHeaders getHeaders(LoginToken codexLoginToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> acceptable = new ArrayList<>();
        acceptable.add(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptable);
        headers.set("X-Auth-Token", codexLoginToken.getToken());
        
        return headers;
    }
}
