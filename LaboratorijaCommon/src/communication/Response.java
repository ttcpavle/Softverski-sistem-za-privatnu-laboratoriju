/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package communication;

import java.io.Serializable;

/**
 *
 * @author student2
 */
public class Response implements Serializable{
    private boolean success;
    private Object result;
    private Exception exception;

    public Response() {
    }

    public Response(Object result, Exception exception, boolean success) {
        this.success = success;
        this.result = result;
        this.exception = exception;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "Response{" + "result=" + result + ", exception=" + exception + '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    
}
