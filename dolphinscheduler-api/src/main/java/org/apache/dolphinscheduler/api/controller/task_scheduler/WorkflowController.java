package org.apache.dolphinscheduler.api.controller.task_scheduler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dolphinscheduler.api.aspect.AccessLogAnnotation;
import org.apache.dolphinscheduler.api.exceptions.ApiException;
import org.apache.dolphinscheduler.api.service.workflow.WorkflowService;
import org.apache.dolphinscheduler.api.utils.Result;
import org.apache.dolphinscheduler.dao.entity.workflow.CatalogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.apache.dolphinscheduler.api.enums.Status.WORKFLOW_CATALOG_CREATE_ERROR;

/**
 * 任务调度-工作流定义控制器
 *
 * @author sixkery
 * @since 2023/6/29
 */
@Api(tags = "任务调度-工作流定义控制器")
@RestController
@RequestMapping("/task/scheduler")
public class WorkflowController {


    @Autowired
    private WorkflowService workflowService;

    /**
     * 目录新增
     * @param name 名称
     * @param id id
     * @return result
     */
    @PostMapping(value = "/catalog")
    @ApiException(WORKFLOW_CATALOG_CREATE_ERROR)
    public Result<String> create(@RequestParam(value = "name") String name,
                                 @RequestParam(value = "id", required = false) Integer id) {

        return workflowService.createCatalog(name, id);
    }


    /**
     * 目录查询
     *
     * @param searchVal search
     * @return login result
     */
    @ApiOperation(value = "check_pgp_token", notes = "PGP_CHECK_TOKEN")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "PGP_CHECK_TOKEN", required = true, dataType = "String")
    })
    @GetMapping(value = "/catalog")
    @AccessLogAnnotation(ignoreRequestArgs = {"searchVal"})
    public Result<List<CatalogEntity>> findAll(@RequestParam(value = "searchVal", required = false) String searchVal) {

        return workflowService.findAllCatalog();
    }


    /**
     * 根据目录 ID 查询工作流定义
     *
     * @param id search
     * @return login result
     */
    @GetMapping(value = "/catalog/id")
    @AccessLogAnnotation(ignoreRequestArgs = {"id"})
    public Result<List<CatalogEntity>> findWorkflowById(@RequestParam(value = "id") Integer id) {
        return workflowService.findWorkflowById(id);
    }

}
