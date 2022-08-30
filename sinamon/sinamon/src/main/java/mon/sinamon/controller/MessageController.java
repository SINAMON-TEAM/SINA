package mon.sinamon.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Controller
@ServerEndpoint("/websocket")
public class MessageController extends Socket {
    private static final List<Session> session = new ArrayList<Session>();

    @GetMapping("/chatting")
    public String chattingDemo(Model model){
        //model.addAttribute("postForm",new PostForm());
        return "chatting/chattingPage";
    }

    @OnOpen
    public void open(Session newUser) {
        session.add(newUser);
        System.out.println("connected : " + newUser.getId());
    }

    @OnMessage
    public void getMsg(Session recieveSession, String msg) {
        System.out.println(msg);

        Message message = new Gson().fromJson(msg, Message.class);

        for (int i = 0; i < session.size(); i++) {
            if (!recieveSession.getId().equals(session.get(i).getId())) {
                try {
                    session.get(i).getBasicRemote().sendText(""+recieveSession.getId()+" : "+msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    session.get(i).getBasicRemote().sendText(""+recieveSession.getId()+" : "+msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Getter @Setter
    static class Message{
        private String type;
        private String text;
        private int id;

    }
}