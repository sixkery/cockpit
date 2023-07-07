package org.apache.dolphinscheduler.api.service.workflow.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.dolphinscheduler.api.enums.Status;
import org.apache.dolphinscheduler.api.service.impl.BaseServiceImpl;
import org.apache.dolphinscheduler.api.service.workflow.WorkflowService;
import org.apache.dolphinscheduler.api.utils.Result;
import org.apache.dolphinscheduler.dao.entity.workflow.CatalogEntity;
import org.apache.dolphinscheduler.dao.mapper.WorkflowCatalogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sixkery
 * @since 2023/6/29
 */
@Service
public class WorkflowServiceImpl extends BaseServiceImpl implements WorkflowService {

    @Resource
    private WorkflowCatalogMapper workflowCatalogMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> createCatalog(String name, Integer id) {

        Result<String> result = new Result<>();
        CatalogEntity workflowCatalogEntity = new CatalogEntity();

        if (id == null) {
            // 父节点列表
            workflowCatalogEntity.setAncestors("1");
            workflowCatalogEntity.setParentId(1);

        } else {
            CatalogEntity workflowCatalog = workflowCatalogMapper.selectById(id);
            String ancestors = workflowCatalog.getAncestors();
            List<String> strings = new ArrayList<>(Collections.singletonList(ancestors));
            strings.add(String.valueOf(id));

            workflowCatalogEntity.setAncestors(strings.toString());
            workflowCatalogEntity.setParentId(id);
        }
        workflowCatalogEntity.setType(1);
        workflowCatalogEntity.setName(name);
        workflowCatalogEntity.setCreateTime(LocalDateTime.now());

        final int insert = workflowCatalogMapper.insert(workflowCatalogEntity);
        if (insert > 0) {
            result.setData("新增成功！");
            putMsg(result, Status.SUCCESS);
        } else {
            result.setData("新增失败！");
            putMsg(result, Status.WORKFLOW_CATALOG_CREATE_ERROR);

        }
        return result;


    }

    @Override
    public Result deleteCatalog() {
        return null;
    }

    @Override
    public Result updateCatalog() {
        return null;
    }

    @Override
    public Result<List<CatalogEntity>> findAllCatalog() {

        List<CatalogEntity> catalogList = workflowCatalogMapper
                .selectList(Wrappers.<CatalogEntity>lambdaQuery().eq(CatalogEntity::getType, 1));

        Map<Integer, CatalogEntity> map = catalogList.stream()
                .collect(Collectors.toMap(CatalogEntity::getId, o -> o));
        List<CatalogEntity> catalogs = new ArrayList<>();

        catalogList.forEach(item -> {
            if (item.getParentId() == 0) {
                catalogs.add(item);
            } else {
                CatalogEntity workflowCatalog = map.get(item.getParentId());
                workflowCatalog.getChildren().add(item);
            }
        });
        Result<List<CatalogEntity>> result = new Result<>();

        result.setData(catalogs);
        putMsg(result,Status.SUCCESS);
        return result;
    }

    @Override
    public Result<List<CatalogEntity>> findWorkflowById(Integer id) {
        return null;
    }
}
