package ovh.sad.jkromer.websocket.events;


import ovh.sad.jkromer.models.Transaction;

public class TransactionEvent extends GenericEvent {
    public Transaction transaction;

    public TransactionEvent(Transaction transaction) {
        super("event", "transaction");
        this.transaction = transaction;
    }
}