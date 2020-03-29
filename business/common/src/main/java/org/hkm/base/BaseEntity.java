package org.hkm.base;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;

public class BaseEntity {
    public Map<String, Object> map(Map<String, Object> map) {
        Class<?> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                map.put(f.getName(), f.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public void entity(Map<String, Object> map) {
        Class clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            Class fc = f.getType();
            try {

                Object v = map.get(f.getName());
                if (v == null) {
                    continue;
                }

                if (fc.equals(String.class)) {
                    f.set(this, v.toString());
                } else if (fc.equals(Integer.class)) {
                    f.set(this, Integer.parseInt(v.toString()));
                } else if (fc.equals(Long.class)) {
                    f.set(this, Long.parseLong(v.toString()));
                } else if (fc.equals(Float.class)) {
                    f.set(this, Float.parseFloat(v.toString()));
                } else if (fc.equals(Double.class)) {
                    f.set(this, Double.parseDouble(v.toString()));
                } else if (fc.equals(BigDecimal.class)) {
                    f.set(this, new BigDecimal(v.toString()));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
