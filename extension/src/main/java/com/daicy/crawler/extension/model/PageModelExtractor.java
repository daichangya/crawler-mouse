package com.daicy.crawler.extension.model;

import com.daicy.crawler.core.Page;
import com.daicy.crawler.core.selector.*;
import com.daicy.crawler.extension.model.annotation.*;
import com.daicy.crawler.extension.model.formatter.ObjectFormatter;
import com.daicy.crawler.extension.model.formatter.ObjectFormatterBuilder;
import com.daicy.crawler.extension.utils.ExtractorUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.daicy.crawler.core.Request.DEL_KEY_WORD;
import static com.daicy.crawler.extension.model.annotation.ExtractBy.Source.RawText;


/**
 * The main internal logic of page model extractor.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.2.0
 */
class PageModelExtractor {

    private List<Pattern> targetUrlPatterns = new ArrayList<Pattern>();

    private Selector targetUrlRegionSelector;

    private List<Pattern> helpUrlPatterns = new ArrayList<Pattern>();

    private Selector helpUrlRegionSelector;

    private DynamicModel dynamicModel;

    private List<FieldExtractor> fieldExtractors;

    private Extractor objectExtractor;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static PageModelExtractor create(Class clazz) {
        DynamicModel dynamicModel = ClassToModel.toModel(clazz);
        return create(dynamicModel);
    }

    public static PageModelExtractor create(DynamicModel dynamicModel) {
        PageModelExtractor pageModelExtractor = new PageModelExtractor();
//        DynamicModel dynamicModel = ClassToModel.toModel(clazz);
        pageModelExtractor.init(dynamicModel);
        return pageModelExtractor;
    }

    private void init(DynamicModel dynamicModel) {
        this.dynamicModel = dynamicModel;
        initClassExtractors();
        fieldExtractors = new ArrayList<FieldExtractor>();
        for (FieldModel fieldModel : dynamicModel.getFieldModels()) {
            FieldExtractor fieldExtractor = getAnnotationExtractBy(fieldModel);
            FieldExtractor fieldExtractorTmp = getAnnotationExtractCombo(fieldModel);
            if (fieldExtractor != null && fieldExtractorTmp != null) {
                throw new IllegalStateException("Only one of 'ExtractBy ComboExtract ExtractByUrl' can be added to a field!");
            } else if (fieldExtractor == null && fieldExtractorTmp != null) {
                fieldExtractor = fieldExtractorTmp;
            }
            fieldExtractorTmp = getAnnotationExtractByUrl(fieldModel);
            if (fieldExtractor != null && fieldExtractorTmp != null) {
                throw new IllegalStateException("Only one of 'ExtractBy ComboExtract ExtractByUrl' can be added to a field!");
            } else if (fieldExtractor == null && fieldExtractorTmp != null) {
                fieldExtractor = fieldExtractorTmp;
            }
            if (fieldExtractor != null) {
                fieldExtractor.setObjectFormatter(new ObjectFormatterBuilder().setField(fieldModel).build());
                fieldExtractors.add(fieldExtractor);
            }
        }
    }

    private FieldExtractor getAnnotationExtractByUrl(FieldModel fieldModel) {
        FieldExtractor fieldExtractor = null;
        ExtractByUrl extractByUrl = fieldModel.getExtractByUrl();
        if (extractByUrl != null) {
            String regexPattern = extractByUrl.value();
            if (regexPattern.trim().equals("")) {
                regexPattern = ".*";
            }
            fieldExtractor = new FieldExtractor(fieldModel,
                    new RegexSelector(regexPattern), FieldExtractor.Source.Url, extractByUrl.notNull(),
                    extractByUrl.multi() || List.class.isAssignableFrom(fieldModel.getClassType()));
        }
        return fieldExtractor;
    }

    private FieldExtractor getAnnotationExtractCombo(FieldModel fieldModel) {
        FieldExtractor fieldExtractor = null;
        ComboExtract comboExtract = fieldModel.getComboExtract();
        if (comboExtract != null) {
            ExtractBy[] extractBies = comboExtract.value();
            Selector selector;
            switch (comboExtract.op()) {
                case And:
                    selector = new AndSelector(ExtractorUtils.getSelectors(extractBies));
                    break;
                case Or:
                    selector = new OrSelector(ExtractorUtils.getSelectors(extractBies));
                    break;
                default:
                    selector = new AndSelector(ExtractorUtils.getSelectors(extractBies));
            }
            fieldExtractor = new FieldExtractor(fieldModel, selector, comboExtract.source() == ComboExtract.Source.RawHtml ? FieldExtractor.Source.RawHtml : FieldExtractor.Source.Html,
                    comboExtract.notNull(), comboExtract.multi() || List.class.isAssignableFrom(fieldModel.getClassType()));
        }
        return fieldExtractor;
    }

    private FieldExtractor getAnnotationExtractBy(FieldModel fieldModel) {
        FieldExtractor fieldExtractor = null;
        ExtractBy extractBy = fieldModel.getExtractBy();
        if (extractBy != null) {
            Selector selector = ExtractorUtils.getSelector(extractBy);
            FieldExtractor.Source source = getSource(extractBy);

            fieldExtractor = new FieldExtractor(fieldModel, selector, source,
                    extractBy.notNull(), List.class.isAssignableFrom(fieldModel.getClassType()));
        }
        return fieldExtractor;
    }

    private FieldExtractor.Source getSource(ExtractBy extractBy) {
        ExtractBy.Source source0 = extractBy.source();
        if (extractBy.type() == ExtractBy.Type.JsonPath) {
            source0 = RawText;
        }
        FieldExtractor.Source source = null;
        switch (source0) {
            case RawText:
                source = FieldExtractor.Source.RawText;
                break;
            case RawHtml:
                source = FieldExtractor.Source.RawHtml;
                break;
            case SelectedHtml:
                source = FieldExtractor.Source.Html;
                break;
            default:
                source = FieldExtractor.Source.Html;

        }
        return source;
    }


    private void initClassExtractors() {
        Annotation annotation = dynamicModel.getTargetUrl();
        if (annotation == null) {
            targetUrlPatterns.add(Pattern.compile(".*"));
        } else {
            TargetUrl targetUrl = (TargetUrl) annotation;
            String[] value = targetUrl.value();
            for (String s : value) {
                targetUrlPatterns.add(Pattern.compile(s));
            }
            if (!targetUrl.sourceRegion().equals("")) {
                targetUrlRegionSelector = new XpathSelector(targetUrl.sourceRegion());
            }
        }
        annotation = dynamicModel.getHelpUrl();
        if (annotation != null) {
            HelpUrl helpUrl = (HelpUrl) annotation;
            String[] value = helpUrl.value();
            for (String s : value) {
                helpUrlPatterns.add(Pattern.compile(s));
            }
            if (!helpUrl.sourceRegion().equals("")) {
                helpUrlRegionSelector = new XpathSelector(helpUrl.sourceRegion());
            }
        }
        annotation = dynamicModel.getExtractBy();
        if (annotation != null) {
            ExtractBy extractBy = (ExtractBy) annotation;
            Selector selector = ExtractorUtils.getSelector(extractBy);
            objectExtractor = new Extractor(selector, getSource(extractBy), extractBy.notNull(), extractBy.multi());
        }
    }

    public Object process(Page page) {
        boolean matched = false;
        for (Pattern targetPattern : targetUrlPatterns) {
            if (targetPattern.matcher(page.getUrl().toString()).matches()) {
                matched = true;
            }
        }
        if (!matched) {
            return null;
        }
        if (objectExtractor == null) {
            return processSingle(page, null, true);
        } else {
            if (objectExtractor.multi) {
                List<Object> os = new ArrayList<Object>();
                List<String> list = objectExtractor.getSelector().selectList(page.getRawText());
                for (String s : list) {
                    Object o = processSingle(page, s, false);
                    if (o != null) {
                        os.add(o);
                    }
                }
                return os;
            } else {
                String select = objectExtractor.getSelector().select(page.getRawText());
                Object o = processSingle(page, select, false);
                return o;
            }
        }
    }

    private Map processSingle(Page page, String html, boolean isRaw) {
        Map map = null;
        try {
            map = new HashMap();
            map.put(DynamicModel.REQUESTURL, page.getRequest().getUrl());
            for (FieldExtractor fieldExtractor : fieldExtractors) {
                if (fieldExtractor.isMulti()) {
                    List<String> value;
                    switch (fieldExtractor.getSource()) {
                        case RawHtml:
                            value = page.getHtml().selectDocumentForList(fieldExtractor.getSelector());
                            break;
                        case Html:
                            if (isRaw) {
                                value = page.getHtml().selectDocumentForList(fieldExtractor.getSelector());
                            } else {
                                value = fieldExtractor.getSelector().selectList(html);
                            }
                            break;
                        case Url:
                            value = fieldExtractor.getSelector().selectList(page.getUrl().toString());
                            break;
                        case RawText:
                            value = fieldExtractor.getSelector().selectList(page.getRawText());
                            break;
                        default:
                            value = fieldExtractor.getSelector().selectList(html);
                    }
                    if ((value == null || value.size() == 0) && fieldExtractor.isNotNull()) {
                        return null;
                    }
                    if (fieldExtractor.getObjectFormatter() != null) {
                        List<Object> converted = convert(value, fieldExtractor.getObjectFormatter());
                        map.put(fieldExtractor.getFieldModel().getName(), converted);
                    } else {
                        map.put(fieldExtractor.getFieldModel().getName(), value);
                    }
                } else {
                    String value;
                    switch (fieldExtractor.getSource()) {
                        case RawHtml:
                            value = page.getHtml().selectDocument(fieldExtractor.getSelector());
                            break;
                        case Html:
                            if (isRaw) {
                                value = page.getHtml().selectDocument(fieldExtractor.getSelector());
                            } else {
                                value = fieldExtractor.getSelector().select(html);
                            }
                            break;
                        case Url:
                            value = fieldExtractor.getSelector().select(page.getUrl().toString());
                            break;
                        case RawText:
                            value = fieldExtractor.getSelector().select(page.getRawText());
                            break;
                        default:
                            value = fieldExtractor.getSelector().select(html);
                    }
                    if (value == null && fieldExtractor.isNotNull()) {
                        return null;
                    }
                    if (fieldExtractor.getObjectFormatter() != null) {
                        Object converted = convert(value, fieldExtractor.getObjectFormatter());
                        if (converted == null && fieldExtractor.isNotNull()) {
                            return null;
                        }
                        map.put(fieldExtractor.getFieldModel().getName(), converted);
                    } else {
                        map.put(fieldExtractor.getFieldModel().getName(), value);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("extract fail content:{}", page.getRawText(), e);
            page.setDelTargetWords(Lists.newArrayList((String) page.getRequest().getExtra(DEL_KEY_WORD)));
            return null;
        }
        return map;
    }

    private Object convert(String value, ObjectFormatter objectFormatter) {
        try {
            Object format = objectFormatter.format(value);
            logger.debug("String {} is converted to {}", value, format);
            return format;
        } catch (Exception e) {
            logger.error("convert " + value + " to " + objectFormatter.clazz() + " error!", e);
        }
        return null;
    }

    private List<Object> convert(List<String> values, ObjectFormatter objectFormatter) {
        List<Object> objects = new ArrayList<Object>();
        for (String value : values) {
            Object converted = convert(value, objectFormatter);
            if (converted != null) {
                objects.add(converted);
            }
        }
        return objects;
    }

    //    private void setField(Object o, FieldExtractor fieldExtractor, Object value) throws IllegalAccessException, InvocationTargetException {
//        if (value == null) {
//            return;
//        }
//        if (fieldExtractor.getSetterMethod() != null) {
//            fieldExtractor.getSetterMethod().invoke(o, value);
//        }
//        fieldExtractor.getField().set(o, value);
//    }
//
    Class getClazz() {
        return dynamicModel.getClazz();
    }


    public DynamicModel getDynamicModel() {
        return dynamicModel;
    }

    List<Pattern> getTargetUrlPatterns() {
        return targetUrlPatterns;
    }

    List<Pattern> getHelpUrlPatterns() {
        return helpUrlPatterns;
    }

    Selector getTargetUrlRegionSelector() {
        return targetUrlRegionSelector;
    }

    Selector getHelpUrlRegionSelector() {
        return helpUrlRegionSelector;
    }

    public Extractor getObjectExtractor() {
        return objectExtractor;
    }
}
