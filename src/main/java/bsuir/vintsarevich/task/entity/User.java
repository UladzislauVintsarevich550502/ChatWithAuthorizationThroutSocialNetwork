package bsuir.vintsarevich.task.entity;


import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String auth0Id;

    private Integer numberOfPost;

    private String photo;

    private String nickname;

    public User() {
    }

    public User(String auth0Id, Integer numberOfPost, String photo, String nickname) {
        this.auth0Id = auth0Id;
        this.numberOfPost = numberOfPost;
        this.photo = photo;
        this.nickname = nickname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuth0Id() {
        return auth0Id;
    }

    public void setAuth0Id(String auth0Id) {
        this.auth0Id = auth0Id;
    }

    public Integer getNumberOfPost() {
        return numberOfPost;
    }

    public void setNumberOfPost(Integer numberOfPost) {
        this.numberOfPost = numberOfPost;
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
        return "User{" +
                "id=" + id +
                ", auth0Id='" + auth0Id + '\'' +
                ", numberOfPost=" + numberOfPost +
                ", photo='" + photo + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
