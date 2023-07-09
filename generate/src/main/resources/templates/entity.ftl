package ${pkg}.entity;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import cn.hutool.core.date.DatePattern;
import org.springframework.format.annotation.DateTimeFormat;
<#list imports as impt>
import ${impt};
</#list>

/**
* (${entityName})表实体类
*
* @author pikaGenerator
* @since ${generateTime}
*/
<#list entityAnnotations as entityAnnotation>
${entityAnnotation}
</#list>
@Data
public class ${entityName} extends Model<${entityName}> {

<#list fields as field>
    <#list field.fieldAnnotations as fieldAnnotation>
    ${fieldAnnotation}
    </#list>
    <#if field.swaggerComment ??>
    @ApiModelProperty("${field.swaggerComment}")
    </#if>
    private ${field.fieldType} ${field.fieldName};
</#list>
    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @ApiModelProperty("修改时间")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}