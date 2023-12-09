package Linkify.shortlink.project.common.database;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.checkerframework.checker.formatter.qual.Format;

import java.util.Date;


/**
 * 数据库持久层对象基础属性
 */
@Data
public class BaseDO {

    /**
     * 创建时间
     */

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 删除标识 0：未删除 1：已删除
     */
    @TableField(value = "del_flag",fill = FieldFill.INSERT)
    private Integer delFlag;
}
