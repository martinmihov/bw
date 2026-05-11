package com.example.bw_demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import static com.example.bw_demo.util.Constants.MAX_POST_TITLE_LENGTH;

@Entity
@Table(name = "user_posts")
public class UserPostEntity {

    @Id
    private Long postId;

    private String userName;

    private String userEmail;

    @Column(length = MAX_POST_TITLE_LENGTH)
    private String postTitle;

    private String postBody;

    public UserPostEntity() {
    }

    public UserPostEntity(Long postId, String userName, String userEmail, String postTitle, String postBody) {
        this.postId = postId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.postTitle = postTitle;
        this.postBody = postBody;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }
}
