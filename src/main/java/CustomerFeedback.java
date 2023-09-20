import lombok.Data;

@Data
public class CustomerFeedback {

    public enum MessageTypes {
        SUGGESTION, COMPLIMENT, CRITICISM
    };

    private long Id;
    private String Status;
    private String Message;
    private MessageTypes Type;

}
