package bsuir.vintsarevich.task.entity;

public class Message {
    private String content;
    private int userId;
    private String photo;
    private String nickname;

    public Message() {
    }

    public Message(String message, int userId, String photo, String nickname) {
        this.content = message;
        this.userId = userId;
        this.photo = photo;
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String message) {
        this.content = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + content + '\'' +
                ", userId=" + userId +
                ", photo='" + photo + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
