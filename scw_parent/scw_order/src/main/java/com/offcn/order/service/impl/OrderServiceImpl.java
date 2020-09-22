package com.offcn.order.service.impl;

import com.offcn.dycommon.enums.OrderStatusEnumes;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.order.mapper.TOrderMapper;
import com.offcn.order.po.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import com.offcn.order.vo.resp.TReturn;
import com.offcn.util.AppDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TOrderMapper tOrderMapper;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;


    /**保存订单
     * @param submitVo
     * @return
     */
    @Override
    public TOrder saveOrder(OrderInfoSubmitVo submitVo) {
        TOrder order = new TOrder();
        String accessToken = submitVo.getAccessToken();
        String memberId = stringRedisTemplate.opsForValue().get(accessToken);
        order.setMemberid(Integer.parseInt(memberId));
        order.setProjectid(submitVo.getProjectid());
        order.setReturnid(submitVo.getReturnid());
        String orderNum = UUID.randomUUID().toString().replace("-", "");
        order.setOrdernum(orderNum);
        order.setCreatedate(AppDateUtils.getFormatTime());
        AppResponse<List<TReturn>> appResponse = projectServiceFeign.getReturn(submitVo.getProjectid());
        List<TReturn> tReturns = appResponse.getData();
        System.out.println(tReturns);
        TReturn tReturn = tReturns.get(0);
        Integer totalMoney = submitVo.getRtncount() * tReturn.getSupportmoney() + tReturn.getFreight();
        order.setMoney(totalMoney);
        //回报数量
        order.setRtncount(submitVo.getRtncount());
        //支付状态  未支付
        order.setStatus(OrderStatusEnumes.UNPAY.getCode()+"");
        //收货地址
        order.setAddress(submitVo.getAddress());
        //是否开发票
        order.setInvoice(submitVo.getInvoice().toString());
        //发票名头
        order.setInvoictitle(submitVo.getInvoictitle());
        //备注
        order.setRemark(submitVo.getRemark());
        tOrderMapper.insertSelective(order);

        return order;
    }
}
