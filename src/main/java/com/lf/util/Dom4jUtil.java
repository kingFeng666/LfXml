package com.lf.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Dom4jUtil {

    private static SAXReader saxReader = new SAXReader();

    private Dom4jUtil() {

    }
    public static Element getRootElement(String classPathFileName) {
        try {
            Document document = saxReader.read(Dom4jUtil.class.getClassLoader().getResourceAsStream(classPathFileName));
            return document.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}
