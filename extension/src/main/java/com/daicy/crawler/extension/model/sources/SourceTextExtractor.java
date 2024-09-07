package com.daicy.crawler.extension.model.sources;

import com.daicy.crawler.core.Page;
import com.daicy.crawler.extension.model.FieldExtractor;
import com.daicy.crawler.extension.model.fields.MultipleField;
import com.daicy.crawler.extension.model.fields.PageField;
import com.daicy.crawler.extension.model.fields.SingleField;

public class SourceTextExtractor {
   public static PageField getText(Page page, String html, boolean isRaw, FieldExtractor fieldExtractor) {
      Source source = fieldExtractor.getSource();
      if (fieldExtractor.isMulti())
         return new MultipleField(source.getTextList(page, html, isRaw, fieldExtractor));
      else
         return new SingleField(source.getText(page, html, isRaw, fieldExtractor));
   }
}