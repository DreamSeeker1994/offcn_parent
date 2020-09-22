package com.offcn.order.service;

import com.offcn.order.po.TOrder;
import com.offcn.order.vo.req.OrderInfoSubmitVo;

public interface OrderService {
    /**保存订单
     * @param submitVo
     * @return
     */
    public TOrder saveOrder(OrderInfoSubmitVo submitVo);
}
