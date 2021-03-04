package com.lf.goods;
import com.lf.goods.controller.GoodsController;
import com.lf.goods.controller.MemberController;
import com.lf.util.Dom4jUtil;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 1. 接收客户端的请求
 * 2. 分发请求
 * 3.IOC实现了对象的管理,然后对对象的属性进行了注入(spring1.0,2.0 基于xml的方式进行配置)
 */
public class DispatcherServlet {
    /**
     * 解析我们自定义的配置文件,把解析的所有的<name,Object>全部放入一个Map中
     * @return 返回一个map中存放的是  Map<beanName,Object>
     */
    public Map<String, Object> initBeanMap() throws DocumentException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Map<String, Object> beanMap = new HashMap<>();
        //使用Dom4j来解析我们的xml文件,然后存放到map集合中Map<beanName,object>
        Element rootElement = Dom4jUtil.getRootElement("application.xml");
        List<Element> beanElements = rootElement.elements("bean");
        for (Element beanElement : beanElements) {
            String beanName = beanElement.attributeValue("name");
            String className = beanElement.attributeValue("class");
            //通过反射机制,来获取类,然后创建类对对象, 然后把xml中配置的name 和class的对象 存在map集合中
            Class clazz = Class.forName(className);
            Object object = clazz.newInstance();
            beanMap.put(beanName, object);
        }
        return beanMap;
    }
    public void dispatcher() throws ClassNotFoundException, InstantiationException, DocumentException, IllegalAccessException {
        //1. 解析xml,得到一个存放className和Object的Map<beanName,Object>的容器
        Map<String, Object> beanMap = initBeanMap();

        //2.给我们容器中的对象注入属性
        diProp(beanMap);
         //通过我们xml中配置的name来获取他的对象
        MemberController memberController = (MemberController) beanMap.get("memberController");
        memberController.add();

        GoodsController goodsController = (GoodsController) beanMap.get("goodsController");
        goodsController.remove();
    }

    /**
     * 属性的注入
     *
     * @param beanMap 对象容器
     */
    private void diProp(Map<String, Object> beanMap) {
        Element rootElement = Dom4jUtil.getRootElement("application.xml");

        beanMap.forEach((beanName, obj) -> {
            //获得class对象
            Class clazz = obj.getClass();
            //获取本类中定义的全部的属性(包含私有的) getDeclaredFields 可以拿到对象的属性包含私有的(反射机制)
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                //获取属性名称
                String fieldName = declaredField.getName();
                //解析xml中配置的属性
                List<Element> beanNodes = rootElement.elements("bean");
                for (Element beanNode : beanNodes) {
                    Element propertiesElement = beanNode.element("properties");
                    if (propertiesElement != null) {
                        List<Element> propertyNodeList = propertiesElement.elements("property");
                        for (Element propertyNode : propertyNodeList) {
                            //获取xml中property标签中name属性的值
                            String nameAttrValue = propertyNode.attributeValue("name");
                            if (fieldName.equals(nameAttrValue)) {
                                try {
                                    declaredField.setAccessible(true);//强吻
                                    //配置了value属性
                                    if (propertyNode.attribute("value") != null) {
                                        //将我们在xml中配置的属性,注入到对象的属性中去
                                        declaredField.set(obj, propertyNode.attributeValue("value"));
                                    }
                                    //配置了bean属性
                                    if (propertyNode.attribute("bean") != null) {
                                        Object bean = beanMap.get(propertyNode.attributeValue("bean"));
                                        declaredField.set(obj, bean);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
