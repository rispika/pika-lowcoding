package com.pika.generate.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pika.common.R;
import com.pika.common.constant.ResMsg;
import com.pika.generate.constant.GenerateType;
import com.pika.generate.entity.GenerateCol;
import com.pika.generate.entity.GenerateTable;
import com.pika.generate.service.GenerateColService;
import com.pika.generate.service.GenerateTableService;
import com.pika.generate.vo.GenerateForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "控制自动生成相关API")
@RestController
@RequestMapping("/generate")
public class GenerateController {

    @Resource
    private GenerateColService generateColService;
    @Resource
    private GenerateTableService generateTableService;

    @ApiOperation("根据tableId删除表")
    @DeleteMapping("/removeGenerateTable/{tableId}")
    public R removeGenerateTable(@ApiParam(value = "表id", required = true) @NotNull @PathVariable Long tableId) {
        if (generateTableService.removeById(tableId)) {
            return R.ok();
        } else {
            return R.fail(500, "删除失败,请查看系统日志");
        }
    }

    @ApiOperation("根据colId删除表")
    @DeleteMapping("/removeCol/{tableId}")
    public R removeCol(@ApiParam(value = "列id", required = true) @NotNull @PathVariable Long colId) {
        if (generateColService.removeById(colId)) {
            return R.ok();
        } else {
            return R.fail(500, "删除失败,请查看系统日志");
        }
    }

    @ApiOperation("获取所有sql信息")
    @GetMapping("/queryAllTablesInfo")
    public R queryAllTablesInfo() {
        return generateColService.queryAllTablesInfo();
    }

    @ApiOperation("获取所有生成表sql信息")
    @GetMapping("/queryAllGenerateTables/{page}/{size}")
    public R queryAllGenerateTables(@ApiParam(value = "当前页", required = true) @NotNull @PathVariable Long page,
                                    @ApiParam(value = "大小", required = true) @NotNull @PathVariable Long size) {
        Page<GenerateTable> tablePage = generateTableService.page(new Page<>(page, size), null);
        return R.ok().data("tables", tablePage);
    }

    @ApiOperation("添加一个生成表sql数据")
    @PutMapping("/insertGenerateTable")
    public R insertGenerateTable(@RequestBody GenerateForm generateForm) {
        GenerateTable generateTable = generateForm.getGenerateTable();
        List<GenerateCol> generateCols = generateForm.getGenerateCols();
        // 添加
        if (generateTableService.save(generateTable)) {
            // 添加id 以及 判断nullable
            for (GenerateCol generateCol : generateCols) {
                generateCol.setTableId(generateTable.getTableId());
                if (generateCol.getIsPrimary()) generateCol.setNullable(false);
            }
            // 添加
            if (generateColService.saveBatch(generateCols)) {
                return R.ok();
            } else {
                return R.fail(500, "生成表添加失败,请检查故障");
            }
        } else {
            return R.fail(500, "生成表添加失败,请检查故障");
        }
    }

    @ApiOperation("根据表id查询列")
    @GetMapping("/queryColsByTableId/{tableId}")
    public R queryColsByTableId(@ApiParam(value = "表id", required = true) @NotNull @PathVariable Long tableId) {
        List<GenerateCol> generateCols = generateColService.lambdaQuery().eq(GenerateCol::getTableId, tableId)
                .list();
        return R.ok().data("cols", generateCols);
    }

    @ApiOperation("根据列id查询列信息")
    @GetMapping("/queryColByColId/{colId}")
    public R queryColByColId(@ApiParam(value = "列id", required = true) @NotNull @PathVariable Long colId) {
        return R.ok().data("col", generateColService.getById(colId));
    }

    /**
     * 查询所有sql类型
     *
     * @return {@link R}
     */
    @ApiOperation("查询所有sql类型")
    @GetMapping("/queryAllSqlTypes")
    public R queryAllSqlTypes() {
        return R.ok().data(GenerateType.getAllTypes());
    }


    /**
     * 插入列信息
     *
     * @param generateCol 生成列
     * @return {@link R}
     */
    @ApiOperation("插入列信息")
    @PutMapping("/insertColInfo")
    public R insertColInfo(@RequestBody GenerateCol generateCol) {
        if (generateColService.save(generateCol)) {
            return R.ok();
        } else {
            return R.fail(500, ResMsg.INSERT_FAIL);
        }
    }


    /**
     * 更新列信息
     *
     * @param generateCol 生成列
     * @return {@link R}
     */
    @ApiOperation("更新列信息")
    @PostMapping("/updateColInfo")
    public R updateColInfo(@RequestBody GenerateCol generateCol) {
        if (generateCol.getIsPrimary()) generateCol.setNullable(false);
        if (generateColService.updateById(generateCol)) {
            return R.ok();
        } else {
            return R.fail(500, ResMsg.UPDATE_FAIL);
        }
    }

    /**
     * 生成代码
     *
     * @param tableId 表id
     * @return {@link R}
     */
    @ApiOperation("生成代码")
    @PostMapping("/generateCode/{tableId}")
    public R generateCode(@ApiParam(value = "sql表id", required = true) @PathVariable Long tableId) {
        return generateTableService.generateCode(tableId);
    }

    /**
     * 生成代码文件加载
     *
     * @param tableId 表id
     * @param pkg     包裹
     * @return {@link R}
     */
    @ApiOperation("生成代码文件加载")
    @PostMapping("/generateCodeAndLoad/{tableId}")
    public R generateCodeAndLoad(@ApiParam(value = "sql表id", required = true) @PathVariable Long tableId,
                                 @ApiParam(value = "包") @RequestParam String pkg) {
        return generateTableService.generateCodeAndLoad(tableId, pkg);
    }

    /**
     * 查询模板信息
     *
     * @param tableId 表id
     * @return {@link R}
     */
    @ApiOperation("查询表信息")
    @GetMapping("/queryTemplateInfo/{tableId}")
    public R queryTemplateInfo(@ApiParam(value = "sql表id", required = true) @PathVariable Long tableId) {
        return generateTableService.queryTemplateInfo(tableId);
    }


}
