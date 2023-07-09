package ${pkg}.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pika.common.R;
import com.pika.common.constant.ResMsg;
import ${pkg}.entity.${entityName};
import ${pkg}.service.${entityName}Service;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
* (${entityName})表控制层
*
* @author pikaGenerator
* @since ${generateTime}
*/
@RestController
@RequestMapping("${name}")
public class ${entityName}Controller {
    /**
    * 服务对象
    */
    @Resource
    private ${entityName}Service ${name}Service;

    /**
    * 分页查询所有数据
    *
    * @param page 当前页
    * @param size 页大小
    * @param ${name} 查询条件
    * @return 所有数据
    */
    @ApiOperation("分页查询所有数据")
    @GetMapping("/{page}/{size}")
    public R selectAll(@ApiParam(value = "当前页",required = true) @PathVariable Long page,
    @ApiParam(value = "页大小",required = true) @PathVariable Long size,
    @ApiParam("查询条件") ${entityName} ${name}) {
        return R.ok().data(this.${name}Service.page(new Page<>(page, size), new QueryWrapper<>(${name})));
    }

    /**
    * 通过主键查询单条数据
    *
    * @param id 主键
    * @return 单条数据
    */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("{id}")
    public R selectOne(@ApiParam(value = "id",required = true) @PathVariable Serializable id) {
        return R.ok().data("data",this.${name}Service.getById(id));
    }

    /**
    * 新增数据
    *
    * @param ${name} 实体对象
    * @return 新增结果
    */
    @ApiOperation("新增数据")
    @PostMapping
    public R insert(@ApiParam(value = "实体对象",required = true) @RequestBody ${entityName} ${name}) {
        if (this.${name}Service.save(${name})) {
            return R.ok();
        } else {
            return R.fail(500, ResMsg.INSERT_FAIL);
        }
    }

    /**
    * 修改数据
    *
    * @param ${name} 实体对象
    * @return 修改结果
    */
    @ApiOperation("修改数据")
    @PutMapping
    public R update(@ApiParam(value = "实体对象",required = true) @RequestBody ${entityName} ${name}) {
        if (this.${name}Service.updateById(${name})) {
            return R.ok();
        } else {
            return R.fail(500,ResMsg.UPDATE_FAIL);
        }
    }

    /**
    * 删除数据
    *
    * @param idList 主键集合
    * @return 删除结果
    */
    @ApiOperation("删除数据")
    @DeleteMapping
    public R delete(@ApiParam(value = "主键集合",required = true)@RequestParam("idList") List<Long> idList) {
        if (this.${name}Service.removeByIds(idList)) {
            return R.ok();
        } else {
            return R.fail(500, ResMsg.DELETE_FAIL);
        }
    }
}