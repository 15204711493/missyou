package com.lin.missyou.service;

import com.lin.missyou.bo.OrderMessageBo;
import com.lin.missyou.exception.http.ServiereErrorException;
import com.lin.missyou.model.Order;
import com.lin.missyou.repository.OrderRepository;
import com.lin.missyou.repository.SkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrderCancelService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SkuRepository skuRepository;

    @Transactional
    public void cancel(OrderMessageBo messageBo){
        if(messageBo.getOrderId()<=0){
            throw new ServiereErrorException(9999);
        }

        this.cancel(messageBo.getOrderId());

    }
    public void cancel(Long oid){

        Optional<Order> orderOptional = orderRepository.findById(oid);
        Order order = orderOptional.orElseThrow(() -> {
            throw new ServiereErrorException(9999);
        });

        int res = orderRepository.cancelOrder(oid);
        if(res != 1){
            return;
        }
        order.getSnapItems().forEach(orderSku->{
            skuRepository.recoverStock1(orderSku.getId(),orderSku.getCount().longValue());
        });

    }

}
