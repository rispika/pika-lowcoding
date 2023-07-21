package com.pika.generate.service.impl;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.sql.SqlUtil;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlExportParameterVisitor;
import com.alibaba.druid.sql.visitor.ExportParameterVisitor;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
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

    @Override
    public R createTableBySqlFile(MultipartFile file) {
        try {
            FastByteArrayOutputStream read = IoUtil.read(file.getInputStream());
            String sqlContext = read.toString(StandardCharsets.UTF_8);
            System.out.println(sqlContext);
            sqlContext = sqlContext.substring(sqlContext.indexOf("CREATE TABLE"));
            sqlContext = sqlContext.substring(0, sqlContext.indexOf(";"));
            //开始解析sql
            SQLStatement sqlStatement = SQLUtils.parseSingleStatement(sqlContext, JdbcConstants.MYSQL);
            SchemaStatVisitor statVisitor = SQLUtils.createSchemaStatVisitor(JdbcConstants.MYSQL);
            sqlStatement.accept(statVisitor);
            if (statVisitor.getTables().size() != 1 || statVisitor.getColumns().isEmpty()) {
                log.error("解析sql文件出错!");
                return R.fail(500, "sql文件解析错误,请检查sql文件后重试!");
            }
            for (TableStat.Name tableName : statVisitor.getTables().keySet()) {
                System.out.println(tableName);
            }
            for (TableStat.Column column : statVisitor.getColumns()) {
                System.out.println("=========================");
                System.out.println(column.getName());
                System.out.println(column.getDataType());
                System.out.println("=========================");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.ok();
    }

}

