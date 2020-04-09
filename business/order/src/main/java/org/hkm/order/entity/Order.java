package org.hkm.order.entity;

import lombok.Data;
import org.hkm.base.BaseEntity;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class Order extends BaseEntity {

    private Long id;

    private Long orderNo;

    private Long uId;


    private Long createTime;
    private Long payTime;
    private Long updateTime;

    // 应付额
    private BigDecimal amount;

    // 抵扣额
    private BigDecimal discount;

    // 订单总额
    private BigDecimal value;

    // 抵扣方式
    private DiscountType discountType;

    private State state;

    /**
     * 订单状态
     */
    public enum State {

        // 创建订单
        CREATE(0),
        // 已支付-待发货
        PAIED(1),
        // 已取消
        CANCLED(-1),
        // 已发货-配送中
        DELIVER(2),
        // 订单完成-确认收货
        FINISHED(3),
        ;

        private int v;

        private State(int v) {
            this.v = v;
        }

        public int val() {
            return this.v;
        }

        public static State state(int v) {
            State[] states = State.values();
            for (State state : states) {
                if (state.v == v) {
                    return state;
                }
            }
            throw new RuntimeException(("不认识的枚举值"));
        }
    }




    /**
     * 获取优惠的方式
     */
    public enum DiscountType {
        // 优惠券
        COUPON(1),
        // 折扣
        DISCOUNT(2),
        // 一口价/秒杀
        FIXED(3),
        // 满减
        OVERFLOW(0),
        // 无
        NONE(-1),
        ;

        private int v;

        private DiscountType(int v) {
            this.v = v;
        }

        public int val() {
            return this.v;
        }

        public static DiscountType discountType(int v) {
            DiscountType[] discountTypes = DiscountType.values();
            for (DiscountType discountType : discountTypes) {
                if (discountType.v == v) {
                    return discountType;
                }
            }
            throw new RuntimeException(("不认识的枚举值"));
        }

    }



}
