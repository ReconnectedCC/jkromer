package ovh.sad.jkromer.websocket.events;

public class SubscribeEvent extends GenericEvent {

    public int id;

    public SubscribeEvent(String event, int id) {
        super("subscribe", event);
        this.id = id;
    }
}