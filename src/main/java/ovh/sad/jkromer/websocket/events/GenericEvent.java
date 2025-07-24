package ovh.sad.jkromer.websocket.events;

public class GenericEvent {

    public String type;
    public String event;

    public GenericEvent(String type, String event) {
        this.type = type;
        this.event = event;
    }
}