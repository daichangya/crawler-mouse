package com.daicy.crawler.extension.model;

import com.daicy.crawler.core.Site;
import com.daicy.crawler.core.Spider;
import com.daicy.crawler.core.pipeline.CollectorPipeline;
import com.daicy.crawler.core.processor.PageProcessor;
import com.daicy.crawler.extension.pipeline.PageModelPipeline;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The spider for page model extractor.<br>
 * In webmagic, we call a POJO containing extract result as "page model". <br>
 * You can customize a crawler by write a page model with annotations. <br>
 * Such as:
 * <pre>
 * {@literal @}TargetUrl("http://my.oschina.net/flashsword/blog/\\d+")
 *  public class OschinaBlog{
 *
 *      {@literal @}ExtractBy("//title")
 *      private String title;
 *
 *      {@literal @}ExtractBy(value = "div.BlogContent",type = ExtractBy.Type.Css)
 *      private String content;
 *
 *      {@literal @}ExtractBy(value = "//div[@class='BlogTags']/a/text()", multi = true)
 *      private List&lt;String&gt; tags;
 * }
 * </pre>
 * And start the spider by:
 * <pre>
 *   OOSpider.create(Site.me().addStartUrl("http://my.oschina.net/flashsword/blog")
 *        ,new JsonFilePageModelPipeline(), OschinaBlog.class).run();
 * }
 * </pre>
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.2.0
 */
public class OOSpider<T> extends Spider {

    private ModelPageProcessor modelPageProcessor;

    private ModelPipeline modelPipeline;

    private PageModelPipeline pageModelPipeline;

    private List<DynamicModel> pageModelClasses = new ArrayList<DynamicModel>();

    protected OOSpider(ModelPageProcessor modelPageProcessor) {
        super(modelPageProcessor);
        this.modelPageProcessor = modelPageProcessor;
    }

    public OOSpider(PageProcessor pageProcessor) {
        super(pageProcessor);
    }

    /**
     * create a spider
     *
     * @param site            site
     * @param pipelineToModel pipelineToModel
     */
    public OOSpider(Site site, Map<PageModelPipeline, List<DynamicModel>> pipelineToModel) {
        this(ModelPageProcessor.create(site, pipelineToModel.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList()).toArray(new DynamicModel[0])));
        this.modelPipeline = new ModelPipeline();
        super.addPipeline(modelPipeline);
        pageModelClasses.addAll(pipelineToModel.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
        for (PageModelPipeline pageModelPipeline : pipelineToModel.keySet()) {
            for (DynamicModel dynamicModel : pipelineToModel.get(pageModelPipeline)) {
                this.modelPipeline.put(dynamicModel, pageModelPipeline);
            }
        }
    }

    /**
     * create a spider
     *
     * @param site              site
     * @param pageModelPipeline pageModelPipeline
     * @param dynamicModels     dynamicModels
     */
    public OOSpider(Site site, PageModelPipeline pageModelPipeline, DynamicModel... dynamicModels) {
        this(ModelPageProcessor.create(site, dynamicModels));
        this.modelPipeline = new ModelPipeline();
        super.addPipeline(modelPipeline);
        for (DynamicModel dynamicModel : dynamicModels) {
            if (pageModelPipeline != null) {
                this.modelPipeline.put(dynamicModel, pageModelPipeline);
            }
            pageModelClasses.add(dynamicModel);
        }
    }

    @Override
    protected CollectorPipeline getCollectorPipeline() {
        return new PageModelCollectorPipeline<T>(pageModelClasses.get(0));
    }

    public static OOSpider create(Site site, Class... pageModels) {
        return new OOSpider(site, null, ClassToModel.toModel(pageModels));
    }

    public static OOSpider create(Site site, PageModelPipeline pageModelPipeline, Class... pageModels) {
        return new OOSpider(site, pageModelPipeline, ClassToModel.toModel(pageModels));
    }


    public static OOSpider create(Site site, DynamicModel... dynamicModels) {
        return new OOSpider(site, null, dynamicModels);
    }

    public static OOSpider create(Site site, PageModelPipeline pageModelPipeline, DynamicModel... dynamicModels) {
        return new OOSpider(site, pageModelPipeline, dynamicModels);
    }

    public OOSpider addPageModel(PageModelPipeline pageModelPipeline, DynamicModel... dynamicModels) {
        for (DynamicModel dynamicModel : dynamicModels) {
            modelPageProcessor.addPageModel(dynamicModel);
            modelPipeline.put(dynamicModel, pageModelPipeline);
        }
        return this;
    }

    public OOSpider setIsExtractLinks(boolean isExtractLinks) {
        modelPageProcessor.setExtractLinks(isExtractLinks);
        return this;
    }

}
