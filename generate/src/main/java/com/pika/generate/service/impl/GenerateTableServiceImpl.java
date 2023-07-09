package com.pika.generate.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.common.R;
import com.pika.generate.constant.GenerateType;
import com.pika.generate.entity.GenerateCol;
import com.pika.generate.entity.properties.WebGenProperties;
import com.pika.generate.entity.properties.SqlGenProperties;
import com.pika.generate.entity.template.Column;
import com.pika.generate.entity.template.Rule;
import com.pika.generate.entity.template.Table;
import com.pika.generate.mapper.GenerateTableMapper;
import com.pika.generate.entity.GenerateTable;
import com.pika.generate.service.GenerateColService;
import com.pika.generate.service.GenerateTableService;
import com.pika.generate.utils.GenerateUtil;
import freemarker.template.TemplateException;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (GenerateTable)表服务实现类
 *
 * @author makejava
 * @since 2023-07-05 12:16:21
 */
@Service("generateTableService")
public class GenerateTableServiceImpl extends ServiceImpl<GenerateTableMapper, GenerateTable> implements GenerateTableService {

    @Resource
    private DataSource dataSource;
    @Resource
    private GenerateColService generateColService;
    @Value("${generate.default-pkg}")
    private String defaultPkg;
    @Value("${generate.root-path}")
    private String rootPath;

//    @Value("${generate.templatePath}")
//    private String templatePath;

//    public static void main(String[] args) throws TemplateException, IOException {
//        String rootPath = "F:\\Develop\\Java\\pika-lowcoding\\pika-lowcoding-cloud\\generate\\src\\main\\java";
//        String packageName = "com.pika.generate.entity";
//        String templatePath = "F:\\Develop\\Java\\pika-lowcoding\\pika-lowcoding-cloud\\generate\\src\\main\\resources\\templates";
//        String templateName = "entity.ftl";
//        JavaProperties userEntity = new JavaProperties("UserEntity", packageName);
//
//        userEntity.addField(String.class, "username");
//        userEntity.addField(Integer.class, "gender");
//        userEntity.addField(Integer.class, "age");
//        autoCodingJavaEntity(rootPath, templatePath, templateName, userEntity);
//    }

    @Override
    public R generateCode(Long tableId) {
        String templatePath = "F:\\Develop\\Java\\pika-lowcoding\\pika-lowcoding-cloud\\generate\\src\\main\\resources\\templates";
        GenerateTable generateTable = getById(tableId);
        List<GenerateCol> generateCols = generateColService.lambdaQuery().eq(GenerateCol::getTableId, tableId).list();
        SqlGenProperties sqlGenProperties = new SqlGenProperties(generateTable, generateCols);
        String sql = GenerateUtil.autoCodingSql(templatePath, "sql.ftl", sqlGenProperties);
        WebGenProperties webGenProperties = new WebGenProperties(generateTable, defaultPkg);
        webGenProperties.addFields(generateCols);
        String entity = GenerateUtil.autoCodingJavaOrWeb(templatePath, "entity.ftl", webGenProperties);
        String mapper = GenerateUtil.autoCodingJavaOrWeb(templatePath, "mapper.ftl", webGenProperties);
        String service = GenerateUtil.autoCodingJavaOrWeb(templatePath, "service.ftl", webGenProperties);
        String serviceImpl = GenerateUtil.autoCodingJavaOrWeb(templatePath, "serviceImpl.ftl", webGenProperties);
        String controller = GenerateUtil.autoCodingJavaOrWeb(templatePath, "controller.ftl", webGenProperties);
        String api = GenerateUtil.autoCodingJavaOrWeb(templatePath, "api.ftl", webGenProperties);
        return R.ok().data("sql", sql).data("entity", entity)
                .data("mapper", mapper).data("service", service)
                .data("serviceImpl", serviceImpl).data("controller", controller)
                .data("api",api);
    }

    @Override
    public R generateCodeAndLoad(Long tableId, String pkg) {
        try {
            if (StrUtil.isBlank(pkg)) {
                pkg = defaultPkg;
            }
            GenerateTable generateTable = getById(tableId);
            List<GenerateCol> generateCols = generateColService.lambdaQuery().eq(GenerateCol::getTableId, tableId).list();
            SqlGenProperties sqlGenProperties = new SqlGenProperties(generateTable, generateCols);
            WebGenProperties webGenProperties = new WebGenProperties(generateTable, pkg);
            webGenProperties.addFields(generateCols);
            String templatePath = new ClassPathResource("/templates").getFile().getAbsolutePath();
            // 加载 sql 文件
            Connection connection = DataSourceUtils.getConnection(dataSource);
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            String sqlScript = GenerateUtil.autoCodingSql(templatePath, GenerateUtil.SQL_FTL, sqlGenProperties);
            // 输出日志
            StringWriter stringWriter = new StringWriter();
            stringWriter.write(sqlScript);
            PrintWriter printWriter = new PrintWriter(stringWriter);
            scriptRunner.setLogWriter(printWriter);
            scriptRunner.setErrorLogWriter(printWriter);
            // 执行
            StringReader stringReader = new StringReader(sqlScript);
            scriptRunner.runScript(stringReader);
            // 关闭
            scriptRunner.closeConnection();
            // 加载 java 文件
            GenerateUtil.autoCodingJavaOrWeb(rootPath, templatePath, GenerateUtil.ENTITY_FTL, webGenProperties);
            GenerateUtil.autoCodingJavaOrWeb(rootPath, templatePath, GenerateUtil.MAPPER_FTL, webGenProperties);
            GenerateUtil.autoCodingJavaOrWeb(rootPath, templatePath, GenerateUtil.SERVICE_FTL, webGenProperties);
            GenerateUtil.autoCodingJavaOrWeb(rootPath, templatePath, GenerateUtil.SERVICE_IMPL_FTL, webGenProperties);
            GenerateUtil.autoCodingJavaOrWeb(rootPath, templatePath, GenerateUtil.CONTROLLER_FTL, webGenProperties);
            return R.ok();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public R queryTemplateInfo(Long tableId) {
        List<GenerateCol> generateCols = generateColService.lambdaQuery().eq(GenerateCol::getTableId, tableId).list();
        List<Column> columns = generateCols.stream()
                .map(generateCol -> {
                    Column column = new Column();
                    column.setTableColumnLabel(generateCol.getColName());
                    column.setTableColumnProp(generateCol.getColName());
                    String generateFormType = GenerateType.getGenerateFormType(generateCol.getColType());
                    column.setTableColumnType(generateFormType);
                    if ("mapper".equals(generateFormType)) {
                        column.addMapper("是", true);
                        column.addMapper("否", false);
                    }
                    if (!generateCol.getNullable()) {
                        Rule rule = Rule.builder().required(true).build();
                        column.addTableColumnRules(rule);
                    }
                    column.setSearchFlag(true);
                    return column;
                }).collect(Collectors.toList());
        Table table = Table.builder().tableColumns(columns).build();
        return R.ok().data(table);
    }

}

