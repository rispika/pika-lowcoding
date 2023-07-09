package com.pika.generate.utils;

import com.pika.generate.entity.properties.WebGenProperties;
import com.pika.generate.entity.properties.SqlGenProperties;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerateUtil {

    public static String SQL_FTL = "sql.ftl";
    public static String ENTITY_FTL = "entity.ftl";
    public static String MAPPER_FTL = "mapper.ftl";
    public static String SERVICE_FTL = "service.ftl";
    public static String SERVICE_IMPL_FTL = "serviceImpl.ftl";
    public static String CONTROLLER_FTL = "controller.ftl";
    public static final String EXT_JAVA = ".java";
    public static final String EXT_SQL = ".sql";
    public static final String EXT_JS = ".js";
    public static final String EXT_VUE = ".vue";

    public static String getSuffix(String templateName, String javaName) {
        if (templateName.equals(ENTITY_FTL)) {
            return "/entity/".concat(javaName).concat(EXT_JAVA);
        } else if (templateName.equals(MAPPER_FTL)) {
            return "/mapper/".concat(javaName).concat("Mapper".concat(EXT_JAVA));
        } else if (templateName.equals(SERVICE_FTL)) {
            return "/service/".concat(javaName).concat("Service".concat(EXT_JAVA));
        } else if (templateName.equals(SERVICE_IMPL_FTL)) {
            return "/service/impl/".concat(javaName).concat("ServiceImpl".concat(EXT_JAVA));
        } else if (templateName.equals(CONTROLLER_FTL)) {
            return "/controller/".concat(javaName).concat("Controller".concat(EXT_JAVA));
        } else {
        return "";
        }
    }


    /**
     * 自动编码java实体类
     *
     * @param rootPath             maven 的  java 目录
     * @param templatePath         模板存放的文件夹
     * @param templateName         模板的名称
     * @param webGenProperties 需要渲染对象的封装
     * @throws IOException       the io exception
     * @throws TemplateException the template exception
     */
    public static void autoCodingJavaOrWeb(String rootPath,
                                           String templatePath,
                                           String templateName,
                                           WebGenProperties webGenProperties) throws IOException, TemplateException {

        // freemarker 配置
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding("UTF-8");
        // 指定模板的路径
        configuration.setDirectoryForTemplateLoading(new File(templatePath));
        // 根据模板名称获取路径下的模板
        Template template = configuration.getTemplate(templateName);
        // 处理路径问题
        String javaName = webGenProperties.getEntityName();
        String packageName = webGenProperties.getPkg();
        String out = rootPath.concat(Stream.of(packageName.split("\\."))
                .collect(Collectors.joining("/", "/", getSuffix(templateName, javaName))));
        // 定义一个输出流来导出代码文件
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get(out)));
        // freemarker 引擎将动态数据绑定的模板并导出为文件
        template.process(webGenProperties, outputStreamWriter);
    }

    public static String autoCodingJavaOrWeb(
            String templatePath,
            String templateName,
            WebGenProperties webGenProperties) {

        try {
            // freemarker 配置
            Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            configuration.setDefaultEncoding("UTF-8");
            // 指定模板的路径
            configuration.setDirectoryForTemplateLoading(new File(templatePath));
            // 根据模板名称获取路径下的模板
            Template template = configuration.getTemplate(templateName);
            // freemarker 引擎将动态数据绑定的模板并导出为字符串
            StringWriter stringWriter = new StringWriter();
            template.process(webGenProperties, stringWriter);
            return stringWriter.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 自动编码sql
     *
     * @param templatePath  模板路径
     * @param templateName  模板名称
     * @param sqlGenProperties sql特性
     * @return {@link String}
     */
    public static String autoCodingSql(String templatePath,
                                       String templateName,
                                       SqlGenProperties sqlGenProperties) {

        try {
            // freemarker 配置
            Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            configuration.setDefaultEncoding("UTF-8");
            // 指定模板的路径
            configuration.setDirectoryForTemplateLoading(new File(templatePath));
            // 根据模板名称获取路径下的模板
            Template template = configuration.getTemplate(templateName);
            // 处理路径问题
            // 定义一个输出流来导出代码文件
//            Path path = Paths.get(rootPath + EXT_SQL);
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
//                    Files.newOutputStream(path));
            // freemarker 引擎将动态数据绑定的模板并导出为文件
//            template.process(sqlProperties, outputStreamWriter);
//            List<String> strings = Files.readAllLines(path);
            StringWriter stringWriter = new StringWriter();
            template.process(sqlGenProperties, stringWriter);
            return stringWriter.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public static String joinStrings(List<String> strings) {
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(string).append("\n");
        }
        return sb.toString();
    }

}
