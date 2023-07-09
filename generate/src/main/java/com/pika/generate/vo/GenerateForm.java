package com.pika.generate.vo;

import com.pika.generate.entity.GenerateCol;
import com.pika.generate.entity.GenerateTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("生成数据表的表单传输VO")
public class GenerateForm {
    @NotNull
    @ApiModelProperty("对应表数据实体")
    private GenerateTable generateTable;
    @NotBlank
    @ApiModelProperty("对应列数据实体")
    private List<GenerateCol> generateCols;

}
