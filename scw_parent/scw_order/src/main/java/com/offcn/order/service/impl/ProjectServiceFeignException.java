package com.offcn.order.service.impl;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectServiceFeignException implements ProjectServiceFeign {
    @Override
    public AppResponse<List<TReturn>> getReturn(Integer projectId) {
        AppResponse<List<TReturn>> appResponse = AppResponse.fail(null);
        appResponse.setMsg("调用远程服务器失败(订单)");
        return appResponse;
    }
}
