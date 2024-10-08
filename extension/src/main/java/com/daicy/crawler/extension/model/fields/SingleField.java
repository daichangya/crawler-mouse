package com.daicy.crawler.extension.model.fields;

import java.lang.reflect.InvocationTargetException;

import com.daicy.crawler.extension.model.FieldExtractor;
import org.slf4j.Logger;

import lombok.Getter;

public class SingleField extends PageField {
   @Getter
   private String fieldName; 

   public SingleField(String fieldName) {
      this.fieldName = fieldName;
   }

   public boolean operation(Object o, FieldExtractor fieldExtractor, Logger logger) throws IllegalAccessException, InvocationTargetException {
      if (fieldExtractor.getObjectFormatter() != null) {
         Object converted = this.convert(this.fieldName, fieldExtractor.getObjectFormatter(), logger);
         if (converted == null && fieldExtractor.isNotNull())
            return false;
         setField(o, fieldExtractor, converted);
      } else
         setField(o, fieldExtractor, this.fieldName);
      return true;
   }
}
