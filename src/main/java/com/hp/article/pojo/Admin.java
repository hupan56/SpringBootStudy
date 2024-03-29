package com.hp.article.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName(value = "admin")
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @TableId(type = IdType.AUTO)
    private long id;

    private String adminName;

    private String adminPassword;
}
