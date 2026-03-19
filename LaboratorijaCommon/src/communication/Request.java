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
