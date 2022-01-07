package io.github.tivecs.wanderer.language;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Message {

    public enum MessageType{
        SINGLE_LINE, MULTI_LINE
    }

    private final String messagePath;
    private final Language language;
    private final MessageType messageType;
    private final List<String> messages = new ArrayList<>();

    protected Message(@Nonnull Language language, @Nonnull MessageType messageType, @Nonnull String messagePath){
        this.language = language;
        this.messageType = messageType;
        this.messagePath = messagePath;
    }

    protected void setMessage(@Nonnull String... msg){
        getMessages().clear();
        for (String m : msg){
            getMessages().add(m);
            if (getMessageType().equals(MessageType.SINGLE_LINE)) return;
        }
    }

    public String getMessagePath() {
        return messagePath;
    }

    public List<String> getMessages() {
        return messages;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Language getLanguage() {
        return language;
    }
}
