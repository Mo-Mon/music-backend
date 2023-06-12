package com.example.musicbackend.Utils;


import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ConvertUtil {
    public static void copyProIgNull(Object b, Object c){
        Field[] fields = getAllFields(b.getClass()); // Lấy danh sách các thuộc tính của đối tượng B
        for (Field field : fields) {
            field.setAccessible(true); // Cho phép truy cập vào thuộc tính private
            try {
                Object value = field.get(b); // Lấy giá trị của từng thuộc tính
                if (value != null) {
                    BeanUtils.copyProperty(c, field.getName(), value); // Chỉ copy thuộc tính khác null
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("lỗi convert properties");
            }
        }
    }

    private static Field[] getAllFields(Class<?> cls) {
        if (cls == null) {
            return new Field[0];
        }

        Field[] declaredFields = cls.getDeclaredFields();
        Field[] parentFields = getAllFields(cls.getSuperclass());
        Field[] allFields = new Field[declaredFields.length + parentFields.length];

        System.arraycopy(declaredFields, 0, allFields, 0, declaredFields.length);
        System.arraycopy(parentFields, 0, allFields, declaredFields.length, parentFields.length);

        return allFields;
    }
}
