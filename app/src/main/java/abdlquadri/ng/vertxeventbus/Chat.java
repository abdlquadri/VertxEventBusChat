package abdlquadri.ng.vertxeventbus;

/**
 * Created by user on 12/22/15.
 */
public class Chat {

    String text;

    public Chat(String text, String time) {
        this.text = text;
        this.time = time;
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String time;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
