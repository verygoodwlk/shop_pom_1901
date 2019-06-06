package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.CartMapper;
import com.qf.entity.Cart;
import com.qf.entity.Goods;
import com.qf.entity.User;
import com.qf.service.ICartService;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/6 9:12
 */
@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public int addCart(Cart cart) {
        return cartMapper.insert(cart);
    }

    /**
     * 获得用户的购物车信息
     * @param cartToken
     * @param user
     * @return
     */
    @Override
    public List<Cart> getCartList(String cartToken, User user) {

        List<Cart> list = null;

        if(user != null){
            //已经登录，购物车从数据库中获取
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("uid", user.getId());
            list = cartMapper.selectList(queryWrapper);

        } else {
            //未登录，redis中获得购物车信息
            if(cartToken != null){
                //获得redis中购物车的长度
                Long size = redisTemplate.opsForList().size(cartToken);
                list = redisTemplate.opsForList().range(cartToken, 0, size);//含头含尾
            }
        }

        //根据购物车信息 查询商品对象
        if(list != null){
            for (Cart cart : list) {
                Goods goods = goodsService.queryById(cart.getGid());
                cart.setGoods(goods);
            }
        }

        return list;
    }

    /**
     * 同步购物车信息
     * @param cartToken
     * @param user
     * @return
     */
    @Override
    public int syncCarts(String cartToken, User user) {

        if(cartToken != null){
            long size = redisTemplate.opsForList().size(cartToken);
            List<Cart> list = redisTemplate.opsForList().range(cartToken, 0, size);

            for (Cart cart : list) {
                cart.setUid(user.getId());
                //将购物车信息保存到数据库中
                this.addCart(cart);
            }

            //删除redis
            redisTemplate.delete(cartToken);

            return 1;
        }

        return 0;
    }
}
