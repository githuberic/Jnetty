package com.lgq.netty.rpc.protocol;

/**
 * @author lgq
 */
public class TcpPackageProtocol {
    private int head = TcpPackageProtocolHead.head;
    private int messageLength;
    private byte[] message;

    public TcpPackageProtocol() {
    }

    public TcpPackageProtocol(int messageLength, byte[] message) {
        this.messageLength = messageLength;
        this.message = message;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }
}
