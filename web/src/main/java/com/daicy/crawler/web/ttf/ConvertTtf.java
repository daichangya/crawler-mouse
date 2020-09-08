package com.daicy.crawler.web.ttf;

import com.daicy.crawler.common.utils.JsonUtils;
import com.daicy.crawler.common.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.fontbox.ttf.GlyfSimpleDescript;
import org.apache.fontbox.ttf.GlyphData;
import org.apache.fontbox.ttf.TTFParser;
import org.apache.fontbox.ttf.TrueTypeFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.web.ttf
 * @date:9/4/20
 */
public class ConvertTtf {
    private static Logger logger = LoggerFactory.getLogger(ConvertTtf.class);

    private static Map<String, String> md5ToString = new HashMap<String, String>();

    private static String[] TTFDicChars = new String(" ,手,一,高,左,里,光,十,更,很,电,量,控,矮,问,上," +
            "是,开,内,好,着,中,硬,空,雨,软,右,来,低,坏,了,皮," +
            "盘,副,耗,八,养,二,无,性,七,比,档,过,实,路,保,动," +
            "坐,六,味,呢,真,启,不,大,少,泥,下,身,冷,三,当,公," +
            "多,孩,外,排,九,油,自,地,音,的,加,长,只,四,有,机," +
            "远,五,小,门,灯,级,和,得,短,响,近").split(",");

    static {
        try {
            Resource resource = new ClassPathResource("TTFDic.ttf");
            TrueTypeFont trueTypeFont = new TTFParser().parse(resource.getInputStream());
            GlyphData[] glyphDatas = trueTypeFont.getGlyph().getGlyphs();
            for (int i = 1; i < glyphDatas.length; i++) {
                GlyfSimpleDescript glyfSimpleDescript = (GlyfSimpleDescript) glyphDatas[i].getDescription();
                String md5 = MD5Utils.MD5(JsonUtils.toJson(glyfSimpleDescript.getInstructions()));
                md5ToString.put(md5, TTFDicChars[i]);
            }
        } catch (Exception e) {
            logger.error("ConvertTtf error", e);
        }
    }

    public static Map<Integer, String> getFontMap(TrueTypeFont currentFont) {
        Map<Integer, String> codeToString = new HashMap<Integer, String>();
        try {
            GlyphData[] currentGlyphDatas = currentFont.getGlyph().getGlyphs();
            for (int i = 1; i < currentGlyphDatas.length; i++) {
                Integer charCode = currentFont.getCmap().getCmaps()[0].getCharacterCode(i);
                GlyfSimpleDescript glyfSimpleDescript = (GlyfSimpleDescript) currentGlyphDatas[i].getDescription();
                String md5 = MD5Utils.MD5(JsonUtils.toJson(glyfSimpleDescript.getInstructions()));
                codeToString.put(charCode, md5ToString.get(md5));
            }
        } catch (Exception e) {
            logger.error("ConvertTtf error", e);
        }
        return codeToString;
    }

    public static String convertStr(TrueTypeFont currentFont, String content) {
        if (StringUtils.isEmpty(content)) {
            return content;
        }
        Map<Integer, String> codeToString = getFontMap(currentFont);
        StringBuffer result = new StringBuffer(content);
        for (int i = 0; i < content.length(); i++) {
            int key = content.charAt(i);
            if (codeToString.containsKey(key)) {
                result.setCharAt(i, codeToString.get(key).charAt(0));
            }
        }
        return result.toString();
    }
}
