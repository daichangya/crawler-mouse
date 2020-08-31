package com.daicy.crawler.extension.model;

import com.daicy.crawler.core.Page;
import com.daicy.crawler.core.Request;
import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.processor.PageProcessor;
import com.daicy.crawler.core.selector.Html;
import com.daicy.crawler.core.selector.Selector;
import com.daicy.crawler.extension.model.annotation.ExtractBy;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The extension to PageProcessor for page model extractor.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.2.0
 */
class ModelPageProcessor implements PageProcessor {

    private List<PageModelExtractor> pageModelExtractorList = new ArrayList<PageModelExtractor>();

    private Site site;

    private boolean extractLinks = false;


    public static ModelPageProcessor create(Site site, DynamicModel... dynamicModels) {
        ModelPageProcessor modelPageProcessor = new ModelPageProcessor(site);
        for (DynamicModel dynamicModel : dynamicModels) {
            modelPageProcessor.addPageModel(dynamicModel);
        }
        return modelPageProcessor;
    }

    public ModelPageProcessor addPageModel(DynamicModel dynamicModel) {
        PageModelExtractor pageModelExtractor = PageModelExtractor.create(dynamicModel);
        pageModelExtractorList.add(pageModelExtractor);
        return this;
    }

    private ModelPageProcessor(Site site) {
        this.site = site;
    }

    @Override
    public void process(Page page) {
        for (PageModelExtractor pageModelExtractor : pageModelExtractorList) {
//            List<Pattern> targetUrlPatterns = pageModelExtractor.getTargetUrlPatterns();
//            boolean matched = false;
//            for (Pattern targetPattern : targetUrlPatterns) {
//                if (targetPattern.matcher(page.getUrl().toString()).matches()) {
//                    matched = true;
//                }
//            }
//            if (!matched) {
//                continue;
//            }
            if (extractLinks) {
                Extractor extractor = pageModelExtractor.getObjectExtractor();
                extractLinks(page, extractor, pageModelExtractor.getHelpUrlRegionSelector(), pageModelExtractor.getHelpUrlPatterns());
                extractLinks(page, extractor, pageModelExtractor.getTargetUrlRegionSelector(), pageModelExtractor.getTargetUrlPatterns());
            }
            Object process = pageModelExtractor.process(page);
            if (process == null || (process instanceof List && ((List) process).size() == 0)) {
                continue;
            }
            page.putField(pageModelExtractor.getDynamicModel().getModelName(), process);
            page.putField(DynamicModel.KEY, pageModelExtractor.getDynamicModel());
        }
        if (page.getResultItems().getAll().size() == 0) {
            page.getResultItems().setSkip(true);
        }
    }

    private void extractLinks(Page page, Extractor objectExtractor, Selector urlRegionSelector, List<Pattern> urlPatterns) {

        if (objectExtractor == null) {
            extractLinksSingle(page, page.getHtml(), urlRegionSelector, urlPatterns);
        } else {
            if (objectExtractor.multi) {
                List<String> list = objectExtractor.getSelector().selectList(page.getRawText());
                for (String s : list) {
                    extractLinksSingle(page, new Html(s), urlRegionSelector, urlPatterns);
                }
            } else {
                String select = objectExtractor.getSelector().select(page.getRawText());
                extractLinksSingle(page, new Html(select), urlRegionSelector, urlPatterns);
            }
        }

    }

    private void extractLinksSingle(Page page, Html html, Selector urlRegionSelector, List<Pattern> urlPatterns) {
        List<String> links;
        if (urlRegionSelector == null) {
            links = html.links().all();
        } else {
            links = html.selectList(urlRegionSelector).links().all();
        }
        for (String link : links) {
            for (Pattern targetUrlPattern : urlPatterns) {
                Matcher matcher = targetUrlPattern.matcher(link);
                if (matcher.find()) {
                    page.addTargetRequest(new Request(matcher.group(0)));
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public boolean isExtractLinks() {
        return extractLinks;
    }

    public void setExtractLinks(boolean extractLinks) {
        this.extractLinks = extractLinks;
    }
}
