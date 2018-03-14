package bsuir.vintsarevich.task.authorization.controller;

import bsuir.vintsarevich.task.authorization.config.AppConfig;
import bsuir.vintsarevich.task.entity.Chat;
import bsuir.vintsarevich.task.entity.Message;
import bsuir.vintsarevich.task.entity.User;
import bsuir.vintsarevich.task.repository.ChatRepository;
import bsuir.vintsarevich.task.repository.UserRepository;
import com.auth0.SessionUtils;
import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.UserInfo;
import com.auth0.net.Request;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;

    private String domain;
    private String clientId;
    private String clientSecret;
    private AuthAPI authAPI;
    private List<Integer> ids;

    @Autowired
    public HomeController(AppConfig config) {
        ids = new ArrayList<>();
        domain = config.getDomain();
        clientId = config.getClientId();
        clientSecret = config.getClientSecret();
        authAPI = new AuthAPI(domain, clientId, clientSecret);
    }

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/portal/home", method = RequestMethod.GET)
    protected String home(final Map<String, Object> model, final HttpServletRequest req) {
        LOGGER.info("Home page");
        try {
            String accessToken = (String) SessionUtils.get(req, "accessToken");
            Request<UserInfo> userInfoRequest = authAPI.userInfo(accessToken);
            String auth0Id = userInfoRequest.execute().getValues().get("sub").toString();
            JSONObject userInfo = getUserInfo(auth0Id);
            String nickname;
            if (auth0Id.contains("vkontakte") || auth0Id.contains("google-oauth2")) {
                nickname = userInfo.getString("given_name") + " " + userInfo.getString("family_name");
            } else {
                nickname = userInfo.getString("nickname");
            }
            User user = getUser(auth0Id, userInfo, nickname);
            if (ids.isEmpty() || !ids.contains(user.getId())) {
                ids.add(user.getId());
                setInfo(user, model);
                return "home";
            } else {
                ids.remove(user.getId());
                return "error";
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (Auth0Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    private User getUser(String auth0Id, JSONObject userInfo, String nickname) {
        List<User> users = userRepository.findByAuth0Id(auth0Id);
        if (users.isEmpty()) {
            List<Chat> chatList = chatRepository.findAll();
            User user;
            if (!chatList.isEmpty()) {
                user = userRepository.save(new User(auth0Id, chatList.get(chatList.size() - 1).getId(), userInfo.getString("picture"), nickname));
            } else {
                user = userRepository.save(new User(auth0Id, 0, userInfo.getString("picture"), nickname));
            }
            return user;
        }
        return users.get(0);
    }

    private JSONObject getUserInfo(String auth0Id) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post("https://" + domain + "/oauth/token")
                .header("content-type", "application/json")
                .body("{\"grant_type\":\"client_credentials\",\"client_id\": \"" + clientId + "\"," +
                        "\"client_secret\": \"" + clientSecret + "\"," +
                        "\"audience\": \"https://" + domain + "/api/v2/\"}")
                .asJson();
        String access_token = response.getBody().getObject().getString("access_token");
        response = Unirest.get("https://uladzislau123.auth0.com/api/v2/users/" + auth0Id)
                .header("authorization", "Bearer " + access_token)
                .asJson();
        return response.getBody().getObject();
    }

    private void setInfo(User user, final Map<String, Object> model) {
        Iterator<Chat> iterator = chatRepository.findAll().iterator();
        List<Message> messageList = new ArrayList<>();
        for (; iterator.hasNext(); ) {
            Chat chat = iterator.next();
            if (user.getNumberOfPost() < chat.getId() || user.getNumberOfPost() == 0) {
                User userInf = userRepository.getById(chat.getUserId());
                messageList.add(new Message(chat.getMessage(), userInf.getId(), userInf.getPhoto(), userInf.getNickname()));
            }
        }
        model.put("photo", user.getPhoto());
        model.put("userId", user.getId());
        model.put("nickname", user.getNickname());
        model.put("messageList", messageList);
    }

}
