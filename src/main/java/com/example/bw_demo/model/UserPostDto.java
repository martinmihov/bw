package com.example.bw_demo.model;

public class UserPostDto {

    private Long postId;
    private String userName;
    private String userEmail;
    private String postTitle;
    private String postBody;

    public UserPostDto() {
    }

    public UserPostDto(Long postId, String userName, String userEmail, String postTitle, String postBody) {
        this.postId = postId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.postTitle = postTitle;
        this.postBody = postBody;
    }

    public Long getPostId() {
        return postId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }
}

