package model.message;

import java.io.Serializable;

public class Message implements Serializable {
    private final MessageInstruction messageInstruction;
    private final MessageLabel messageLabel;
    private final MessageTag[] messageTags;
    private Object[] orderedTagValues;

    public Message(MessageInstruction messageInstruction, MessageLabel messageLabel, MessageTag... messageTags) {
        this.messageInstruction = messageInstruction;
        this.messageLabel = messageLabel;
        this.messageTags = messageTags;
    }

    public void setTagsInOrder(Object... orderedTagValues) {
        this.orderedTagValues = orderedTagValues;
    }

    public MessageInstruction getMessageInstruction() {
        return messageInstruction;
    }

    public MessageLabel getMessageLabel() {
        return messageLabel;
    }

    public MessageTag[] getMessageTags() {
        return messageTags;
    }

    public Object[] getOrderedTagValues() {
        return orderedTagValues;
    }

    @Override
    public String toString() {
        StringBuilder tags = new StringBuilder();
        for (int i = 0; i < messageTags.length; i++) {
            tags.append(" --").append(messageTags[i].name).append(" ").append(orderedTagValues[i]);
        }
        return messageInstruction.name + " --" + messageLabel.name + tags;
    }
}
