package org.hkm.base;

public interface BaseService {

    default boolean isAllPositiveNumber(Number... numbers) {

        for (Number num : numbers) {

            if (num instanceof Long) {
                if (num.longValue()<0) {
                    return false;
                }
            }

            if (num instanceof Integer) {
                if (num.intValue()<0) {
                    return false;
                }
            }

            if (num instanceof Short) {
                if (num.shortValue()<0) {
                    return false;
                }
            }

            if (num instanceof Double) {
                if (num.doubleValue()<0) {
                    return false;
                }
            }

            if (num instanceof Float) {
                if (num.floatValue()<0) {
                    return false;
                }
            }

            if (num instanceof Byte) {
                if (num.byteValue()<0) {
                    return false;
                }
            }

        }
        return true;
    }

    default boolean isNumberBiggerThanZero(Number... numbers) {

        for (Number num : numbers) {

            if (num instanceof Long) {
                if (num.longValue()<=0) {
                    return false;
                }
            }

            if (num instanceof Integer) {
                if (num.intValue()<=0) {
                    return false;
                }
            }

            if (num instanceof Short) {
                if (num.shortValue()<=0) {
                    return false;
                }
            }

            if (num instanceof Double) {
                if (num.doubleValue()<=0) {
                    return false;
                }
            }

            if (num instanceof Float) {
                if (num.floatValue()<=0) {
                    return false;
                }
            }

            if (num instanceof Byte) {
                if (num.byteValue()<=0) {
                    return false;
                }
            }

        }
        return true;
    }

}
