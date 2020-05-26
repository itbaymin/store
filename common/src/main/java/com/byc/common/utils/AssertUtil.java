package com.byc.common.utils;

import com.byc.common.mvc.exception.BizException;
import com.byc.common.mvc.exception.IErrorCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.text.MessageFormat;
import java.util.Collection;

/**
 * Created by baiyc
 * 2020/5/26/026 20:44
 * Description：断言工具
 */
public class AssertUtil {
    public static void assertNotNull(Object obj, IErrorCode errorCode) throws BizException {
        if(obj == null) {
            throw new BizException(errorCode);
        }
    }

    public static void assertNotNull(Object obj, IErrorCode errorCode, String errorMsg) throws BizException {
        if(obj == null) {
            throw new BizException(errorCode, errorMsg);
        }
    }

    public static void assertNotEmpty(Collection obj, IErrorCode errorCode, String errorMsg) {
        if(CollectionUtils.isEmpty(obj)) {
            throw new BizException(errorCode, errorMsg);
        }
    }

    public static void assertNotEmpty(Collection obj, IErrorCode errorCode) {
        assertNotEmpty(obj, errorCode, errorCode.message());
    }

    public static void assertNotEmpty(Object[] objects, IErrorCode errorCode, String errorMsg) {
        if(ArrayUtils.isEmpty(objects)) {
            throw new BizException(errorCode, errorMsg);
        }
    }

    public static void assertEqual(int int1, int in2, IErrorCode errorCode, String errorMsg, Object... args) {
        if(int1 != in2) {
            String msg = MessageFormat.format(errorMsg, args);
            throw new BizException(errorCode, msg);
        }
    }

    public static void assertEqual(int int1, int in2, IErrorCode errorCode) {
        if(int1 != in2) {
            throw new BizException(errorCode);
        }
    }

    public static void assertNotEqual(int int1, int in2, IErrorCode errorCode) {
        if(int1 == in2) {
            throw new BizException(errorCode);
        }
    }

    public static void assertNotEqual(int int1, int in2, IErrorCode errorCode, String errorMsg) {
        if(int1 == in2) {
            throw new BizException(errorCode, errorMsg);
        }
    }

    public static void assertNotEqual(long int1, long in2, IErrorCode errorCode, String errorMsg) {
        if(int1 == in2) {
            throw new BizException(errorCode, errorMsg);
        }
    }

    public static void assertEqual(long int1, long in2, IErrorCode errorCode, String errorMsg) {
        if(int1 != in2) {
            throw new BizException(errorCode, errorMsg);
        }
    }

    public static void assertNull(Object obj, IErrorCode errorCode) throws BizException {
        if(obj != null) {
            throw new BizException(errorCode);
        }
    }

    public static void assertNull(Object obj, IErrorCode errorCode, String errorMsg) throws BizException {
        if(obj != null) {
            throw new BizException(errorCode, errorMsg);
        }
    }

    public static void assertNotEqual(Integer obj1, Integer obj2, IErrorCode errorCode) throws BizException {
        if(obj1 != null && obj2 != null) {
            if(obj1.intValue() == obj2.intValue()) {
                throw new BizException(errorCode);
            }
        }
    }

    public static void assertTrue(boolean condition, IErrorCode errorCode) throws BizException {
        if(!condition) {
            throw new BizException(errorCode);
        }
    }

    public static void assertTrue(boolean condition, IErrorCode errorCode, String errorMsg) throws BizException {
        if(!condition) {
            throw new BizException(errorCode, errorMsg);
        }
    }

    public static void assertFalse(boolean condition, IErrorCode errorCode) throws BizException {
        if(condition) {
            throw new BizException(errorCode);
        }
    }

    public static void assertFalse(boolean condition, IErrorCode errorCode, String errorMsg) throws BizException {
        if(condition) {
            throw new BizException(errorCode, errorMsg);
        }
    }
}
