package com.lgq.netty.rpc;

import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * @author lgq
 */
@Message
public class InputParam implements Serializable {
    private int num1;
    private int num2;

    public int getNum1() {
        return num1;
    }

    public void setNum1(int num1) {
        this.num1 = num1;
    }

    public int getNum2() {
        return num2;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }

    @Override
    public String toString() {
        return "InputParam{" +
                "num1=" + num1 +
                ", num2=" + num2 +
                '}';
    }
}
