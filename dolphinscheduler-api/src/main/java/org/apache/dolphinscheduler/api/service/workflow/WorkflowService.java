package org.apache.dolphinscheduler.api.service.workflow;

import org.apache.dolphinscheduler.api.utils.Result;
import org.apache.dolphinscheduler.dao.entity.workflow.CatalogEntity;

import java.util.List;

/**
 * 工作流定义 service
 *
 * @author sixkery
 * @since 2023/6/29
 */
public interface WorkflowService {

    /**
     * create catalog
     *
     * @param name catalog name
     * @param id   catalog id
     * @return result
     */
    Result<String> createCatalog(String name, Integer id);

    Result deleteCatalog();

    Result updateCatalog();


    Result<List<CatalogEntity>> findAllCatalog();

    Result<List<CatalogEntity>> findWorkflowById(Integer id);
}
