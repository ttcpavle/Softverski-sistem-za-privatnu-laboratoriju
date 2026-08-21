package communication;

import java.io.Serializable;

public class Request implements Serializable{
    private Operacija operation;
    private Object argument;

    public Request(Operacija operation, Object argument) {
        this.operation = operation;
        this.argument = argument;
    }

    public Request() {
    }

    public Operacija getOperation() {
        return operation;
    }

    public void setOperation(Operacija operation) {
        this.operation = operation;
    }

    public Object getArgument() {
        return argument;
    }

    public void setArgument(Object argument) {
        this.argument = argument;
    }

    @Override
    public String toString() {
        return "Request{" + "operation=" + operation + ", argument=" + argument + '}';
    }

    
}
