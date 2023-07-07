/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.dolphinscheduler.dao.entity.workflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sixkery
 * @since 2023/6/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_ds_catalog")
public class CatalogEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * name
     */
    @TableField("name")
    private String name;

    /**
     * parentId
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 类型：1：工作流定义 2：数据流定义 3：数据集
     */
    private Integer type;

    /**
     * create_by
     */
    @TableField("create_by")
    private String createBy;
    /**
     * create time
     */
    private LocalDateTime createTime;

    /**
     * update_by
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * update time
     */
    private LocalDateTime updateTime;

    /**
     * 子级目录
     */
    @TableField(exist = false)
    private List<CatalogEntity> children = new ArrayList<>();
}
