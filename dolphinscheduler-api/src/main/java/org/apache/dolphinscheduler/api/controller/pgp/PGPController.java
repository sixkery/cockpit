package org.apache.dolphinscheduler.api.controller.pgp;

import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dolphinscheduler.api.aspect.AccessLogAnnotation;
import org.apache.dolphinscheduler.api.controller.BaseController;
import org.apache.dolphinscheduler.api.enums.Status;
import org.apache.dolphinscheduler.api.exceptions.ApiException;
import org.apache.dolphinscheduler.api.security.Authenticator;
import org.apache.dolphinscheduler.api.utils.Result;
import org.apache.dolphinscheduler.common.utils.HttpUtils;
import org.apache.dolphinscheduler.common.utils.JSONUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static org.apache.dolphinscheduler.api.enums.Status.IP_IS_EMPTY;
import static org.apache.dolphinscheduler.api.enums.Status.USER_LOGIN_FAILURE;


/**
 * PGP 跳转 ds 验证 token 控制器
 *
 * @author sixkery
 * @since 2023/5/6
 */
@Api(tags = "PGP_CHECK_TOKEN")
@RestController
@RequestMapping("/pgp")
public class PGPController extends BaseController {
    @Autowired
    private Authenticator authenticator;

    @Value("${pgpUrl:http://172.2.0.96:9100/auth/validToken?token=}")
    private String pgpToken;

    /**
     * check pgp token
     *
     * @param token    pgp token
     * @param request  request
     * @param response response
     * @return login result
     */
    @ApiOperation(value = "check_pgp_token", notes = "PGP_CHECK_TOKEN")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "PGP_CHECK_TOKEN", required = true, dataType = "String")
    })
    @GetMapping(value = "/check")
    @ApiException(USER_LOGIN_FAILURE)
    @AccessLogAnnotation(ignoreRequestArgs = {"token", "request", "response"})
    public Result login(@RequestParam(value = "token") String token,
                        HttpServletRequest request,
                        HttpServletResponse response) {

        // check pgp token
        String pgpResp = HttpUtils.get(pgpToken + token);
        int code = JSONUtils.parseObject(pgpResp).get("code").asInt();
        if (code != HttpStatus.SC_OK) {
            return null;
        }

        // user ip check
        String ip = getClientIpAddress(request);
        if (StringUtils.isEmpty(ip)) {
            return error(IP_IS_EMPTY.getCode(), IP_IS_EMPTY.getMsg());
        }
        // verify username and password
        Result<Map<String, String>> result = authenticator.authenticate("admin", "wayz@1234", ip);
        if (result.getCode() != Status.SUCCESS.getCode()) {
            return result;
        }

        response.setStatus(HttpStatus.SC_OK);
        Map<String, String> cookieMap = result.getData();
        for (Map.Entry<String, String> cookieEntry : cookieMap.entrySet()) {
            Cookie cookie = new Cookie(cookieEntry.getKey(), cookieEntry.getValue());
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }
        return result;
    }
}


