package ovh.sad.jkromer.websocket;

import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import ovh.sad.jkromer.models.Transaction;
import ovh.sad.jkromer.websocket.events.GenericEvent;
import ovh.sad.jkromer.websocket.events.SubscribeEvent;
import ovh.sad.jkromer.websocket.events.TransactionEvent;

import java.net.URI;
import java.util.Objects;

public abstract class AbstractKromerClient extends WebSocketClient {

    protected final Gson gson;

    public AbstractKromerClient(URI serverUri, Draft draft, Gson gson) {
        super(serverUri, draft);
        this.gson = gson;
    }

    public AbstractKromerClient(URI serverURI, Gson gson) {
        super(serverURI);
        this.gson = gson;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // add some kind of logging here

        send(gson.toJson(new SubscribeEvent("transactions", 0)));
        send(gson.toJson(new SubscribeEvent("names", 1)));

        onConnected();
    }

    @Override
    public void onMessage(String message) {
        GenericEvent event = gson.fromJson(message, GenericEvent.class);

        if (Objects.equals(event.event, "transaction")) {
            TransactionEvent txEvent = gson.fromJson(message, TransactionEvent.class);
            onTransactionReceived(txEvent.transaction);
        } else {
            onUnknownMessage(event.event, message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // figure out error handling for these exceptions

        onDisconnected(code, reason, remote);
        tryReconnect();
    }

    @Override
    public void onError(Exception ex) {
        // figure out error handling for these exceptions

        if (!isOpen()) {
            onDisconnected(-1, ex.getMessage(), false);
            tryReconnect();
        }
    }

    private void tryReconnect() {
        try {
            reconnectClient();
        } catch (Exception e) {
            // figure out error handling for these exceptions
        }
    }

    // ====== ABSTRACT HOOKS TO BE IMPLEMENTED ======

    protected abstract void onConnected();

    protected abstract void onDisconnected(int code, String reason, boolean remote);

    protected abstract void onTransactionReceived(Transaction tx);

    protected abstract void onUnknownMessage(String eventType, String rawMessage);

    protected abstract void reconnectClient();

    // ====== LOGGING HELPERS (OPTIONAL) ======
}
