package commoncore.exceptionHandle;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-21-00:42
 */
public class MyException extends Exception {
    private String code;
    private String reason;
    private String msg;
    private String className;

    public MyException() {
    }

    @Override
    public String toString() {
        return "MyException{" +
                "code='" + code + '\'' +
                ", reason='" + reason + '\'' +
                ", msg='" + msg + '\'' +
                ", className='" + className + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
