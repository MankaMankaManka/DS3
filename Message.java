import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

class Message implements Serializable {
    String type;  // "prepare", "promise", "accept", "accepted"
    int proposalId;
    String value;
    
    public Message(String type, int proposalId, String value) {
        this.type = type;
        this.proposalId = proposalId;
        this.value = value;
    }
}