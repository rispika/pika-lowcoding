package com.pika.generate.entity.template;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规则
 * https://github.com/yiminghe/async-validator
 *
 * @author RIS
 * @date 2023/07/09
 */
@Data
@Builder
public class Rule {

    /**
     * 类型
     * string: Must be of type string. This is the default type.
     * number: Must be of type number.
     * boolean: Must be of type boolean.
     * method: Must be of type function.
     * regexp: Must be an instance of RegExp or a string that does not generate an exception when creating a new RegExp.
     * integer: Must be of type number and an integer.
     * float: Must be of type number and a floating point number.
     * array: Must be an array as determined by Array.isArray.
     * object: Must be of type object and not Array.isArray.
     * enum: Value must exist in the enum.
     * date: Value must be valid as determined by Date
     * url: Must be of type url.
     * hex: Must be of type hex.
     * email: Must be of type email.
     * any: Can be any type.
     */
    private String type;

    /**
     * 是否需要
     */
    private Boolean required;

    /**
     * 正则匹配
     */
    private String pattern;
    /**
     * 最小值
     */
    private Long min;
    /**
     * 最大值
     */
    private Long max;

    /**
     * 自定义验证器
     */
    private String validator;

    /**
     * 异步验证器
     */
    private String asyncValidator;

    /**
     * 消息
     */
    private String message;

    /**
     * 触发
     */
    private static String trigger = "blur";

    public String getTrigger() {
        return trigger;
    }


}
