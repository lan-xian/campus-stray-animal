package com.campus.strayanimal.common;

/**
 * 业务异常：携带 HTTP 状态码，由全局异常处理器统一转换为响应体
 */
public class BusinessException extends RuntimeException {

    private final int status;

    public BusinessException(int status, String message) {
        super(message);
        this.status = status;
    }

    /** 资源不存在 */
    public static BusinessException notFound(String message) {
        return new BusinessException(404, message);
    }

    /** 未认证 / 认证失败 */
    public static BusinessException unauthorized(String message) {
        return new BusinessException(401, message);
    }

    /** 数据冲突（如唯一约束冲突） */
    public static BusinessException conflict(String message) {
        return new BusinessException(409, message);
    }

    /** 通用参数/业务规则错误 */
    public static BusinessException badRequest(String message) {
        return new BusinessException(400, message);
    }

    public int getStatus() {
        return status;
    }
}
