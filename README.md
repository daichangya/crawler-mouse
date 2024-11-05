## 微信公众号

扫码关注微信公众号，Java码界探秘。
![Java码界探秘](http://images.zthinker.com/qrcode_for_gh_1e2587cc42b1_258_1587996055777.jpg)

[https://zthinker.com/](https://zthinker.com/)

# crawler-mouse （https://github.com/daichangya/crawler-mouse）
基于Java实现的 爬虫框架



```
                     +---------------------+  
                    |     Data Input      |  
                    | -------------------- |  
                    |  Config Files (JSON, |  
                    |    TXT) & Resources  |  
                    +--------+-------------+  
                              |  
                              v  
               +-----------------------+  
               |   Core Processing     |  
               |  ---------------------|  
               |  Spider Engine        |  
               |  - Task Management    |  
               |  - Data Extraction    |  
               |  - HTTP Requests      |  
               |  (Using Site Config)  |  
               +-----+--------+--------+  
                            |  
                            v  
               +-----------------------+  
               |     Data Storage      |  
               |  ---------------------|  
               |  File System (CSV)    |  
               |  (Or Databases)       |  
               +-------------+---------+  
                              |  
      +------------------------+------+  
      |                                |  
 +------+-----+          +-----+------+  
 | Logging   |          | Exceptions   |  
 | -------- |          | ----------   |  
 |  Logger   |          |  Handler     |  
 +-----------+          +-------------+
```

1.  **数据输入层**
    *   包含了配置文件、数据库或其他外部数据源，这些文件如`baiduindex/province.json`、`baiduindex/city.json`和`baiduindex/keywords.txt`，为爬虫提供地域、城市和关键词数据。
2.  **核心处理层**
    *   **Spider引擎**：利用`OOSpider`（或自定义的Spider引擎）进行任务的调度和管理，包括并发控制、任务分配等。
    *   **爬虫任务管理**：包括任务创建（如`baiduIndexBuild`方法内构造的请求列表）、执行（如`spider.run()`方法调用）以及任务间依赖的管理。
    *   **数据提取**：利用注解如`@ExtractBy`从网页或API返回的JSON数据中提取特定字段，如关键词、总体趋势平均值等。
    *   **网络请求**：使用配置好的Site信息（如用户代理、请求头、cookies等）通过HTTP请求API或网页数据。
3.  **数据存储层**
    *   将爬取到的数据存储到文件系统（如CSV文件通过`CsvFileModelPipeline`）或数据库等持久化存储系统中。
4.  **异常与日志处理**
    *   日志记录器（如使用`LoggerFactory.getLogger`）记录程序执行过程中的错误、警告等信息，方便调试和问题追踪。
    *   异常处理（如在文件读取、数据处理时捕获`IOException`并记录错误）。
5.  **外部接口与工具**
    *   使用第三方库如`json-utils`、`FileCopyUtils`、`StringUtils`等进行数据处理和文件操作。
    *   支持命令行接口（CLI），通过`main`方法启动爬虫任务。

### 爬取百度指数

```

/**
 * @author daichangya@163.com
 */
@TargetUrl("http://index.baidu.com/api/SearchApi/index.*")
public class BaiduIndexModel {

    private static Logger logger = LoggerFactory.getLogger(BaiduIndexModel.class);

    private static Site site = Site.me().setDomain("index.baidu.com").setSleepTime(1000)
            .addCookie("BDUSS",
                    "kJ4SEYtT05PM2U4NlVPQWY2dWNZRjNmMng1ZkI5TkVSclRWd3g5VmZaeWdlbjVmSVFBQUFBJCQAAAAAAAAAAAEAAAAQTRyIwfXP~rz8cQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKDtVl-g7VZfZ").
                    setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");


    @ExtractBy(value = "status", type = ExtractBy.Type.JsonPath)
    private String status;

    @ExtractBy(value = "data.userIndexes[0].word[0].name", type = ExtractBy.Type.JsonPath)
    private String keyWord;

    @ExtractBy(value = "data.generalRatio[0].all.avg", type = ExtractBy.Type.JsonPath)
    private String all;

    @ExtractBy(value = "data.generalRatio[0].wise.avg", type = ExtractBy.Type.JsonPath)
    private String wise;

    @ExtractBy(value = "data.generalRatio[0].pc.avg", type = ExtractBy.Type.JsonPath)
    private String pc;

    @ExtractByUrl("areaName=(.*)")
    private String areaName;

    public static void main(String[] args) {
        Spider spider = baiduIndexBuild(null);
        spider.run();
    }

    public static Spider baiduIndexBuild(String cookie) {
        List<Request> urlList = Lists.newArrayList();
        String urlTemp = "http://index.baidu.com/api/SearchApi/index?area={}&word=[[%7b%22name%22:%22{}%22,%22wordType%22:1%7d]]&days=7&areaName={}";
        Resource provinceRes = new ClassPathResource("baiduindex/province.json");
        String jsonStr = null;
        try {
            jsonStr = new String(FileCopyUtils.copyToByteArray(provinceRes.getInputStream()));
            Map<String, String> provinceMap = JsonUtils.toObject(jsonStr, Map.class);

            Resource cityRes = new ClassPathResource("baiduindex/city.json");
            String cityStr = new String(FileCopyUtils.copyToByteArray(cityRes.getInputStream()));
            Map<String, String> cityMap = JsonUtils.toObject(cityStr, Map.class);
            provinceMap.putAll(cityMap);

            Resource keyWordRes = new ClassPathResource("baiduindex/keywords.txt");
            String[] keyWords = new String(FileCopyUtils.copyToByteArray(keyWordRes.getInputStream())).split("\n");
            for (int i = 0; i < keyWords.length; i++) {
                for (String key : provinceMap.keySet()) {
                    String url = StringUtils.stringFormat(urlTemp, provinceMap.get(key), keyWords[i], key);
                    Request request = new Request(url);
                    request.putExtra(Request.DEL_KEY_WORD, keyWords[i]);
                    urlList.add(request);
                }
            }
        } catch (IOException e) {
            logger.error("baiduIndexBuild error", e);
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(cookie)){
            site.addCookie("BDUSS",cookie);
        }
        Filters filters = new Filters();
        filters.addFilters(Lists.newArrayList(new FilterKeyWordRequest()));
        Spider spider = OOSpider.create(site
                , new CsvFileModelPipeline(), BaiduIndexModel.class).addRequest(urlList.toArray(new Request[0])).thread(1)
                .setFilters(filters);
        return spider;
    }

}

```
