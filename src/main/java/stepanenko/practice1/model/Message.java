package stepanenko.practice1.model;

import stepanenko.homework2.model.Command;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Liubomyr Stepanenko
 */
public class Message {
    private int cType;
    private Command command;
    private int bUserId;
    private byte[] message;

    public Message() {
    }

    public Message(int cType, int bUserId, byte[] message) {
        this.cType = cType;
        this.command = Command.values()[cType];
        this.bUserId = bUserId;
        this.message = message;
    }

    public Message(Command command, int bUserId, byte[] message) {
        this.cType = command.ordinal();
        this.command = command;
        this.bUserId = bUserId;
        this.message = message;
    }

    public int getCType() {
        return cType;
    }

    public void setCType(int cType) {
        this.cType = cType;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public int getUserId() {
        return bUserId;
    }

    public void setbUserId(int bUserId) {
        this.bUserId = bUserId;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return cType == message1.cType && bUserId == message1.bUserId
                && Arrays.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(cType, bUserId);
        result = 31 * result + Arrays.hashCode(message);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("cType=").append(cType);
        sb.append(", bUserId=").append(bUserId);
        sb.append(", message=").append(Arrays.toString(message));
        sb.append('}');
        return sb.toString();
    }
}
