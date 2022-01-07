package io.github.tivecs.wanderer.language;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class Language {

    private final LanguageManager manager;
    private final String id;
    private final HashMap<String, Message> messages = new HashMap<>();

    public Language(@Nonnull LanguageManager manager, @Nonnull String id){
        this.manager = manager;
        this.id = id;
    }

    public void addMessage(@Nonnull String messagePath, @Nonnull String... messages){
        boolean isMultiLine = messages.length > 1;
        Message.MessageType messageType = isMultiLine ? Message.MessageType.MULTI_LINE : Message.MessageType.SINGLE_LINE;

        Message message = new Message(this, messageType, messagePath);
        message.setMessage(messages);

        getMessages().put(messagePath, message);
    }

    public Message getMessage(String messagePath){
        return getMessages().get(messagePath);
    }

    public HashMap<String, Message> getMessages() {
        return messages;
    }

    public String getId() {
        return id;
    }

    public LanguageManager getManager() {
        return manager;
    }
}
