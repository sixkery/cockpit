package org.apache.dolphinscheduler.api.controller.task_scheduler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dolphinscheduler.api.aspect.AccessLogAnnotation;
import org.apache.dolphinscheduler.api.enums.Status;
import org.apache.dolphinscheduler.api.exceptions.ApiException;
import org.apache.dolphinscheduler.api.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.dolphinscheduler.api.enums.Status.USER_LOGIN_FAILURE;

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
    @ApiException(USER_LOGIN_FAILURE)
    @AccessLogAnnotation(ignoreRequestArgs = {"searchVal"})
    public Result<List<Map<String, String>>> findAll(@RequestParam(value = "searchVal") String searchVal) {

        Result<List<Map<String, String>>> result = new Result<>();
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("id", "1");
        map.put("name", "目录 1");
        list.add(map);
        result.setData(list);
        result.setCode(Status.SUCCESS.getCode());
        result.setMsg("查询成功！");
        return result;
    }

}
