package com.example.washab.trafficapp;

import java.io.Serializable;

/**
 * Created by wasif on 12/2/15.
 */
public class Comment implements Serializable{

    private int commenterId;
    private String commentText;
    private String timeStamp;

    public Comment(String commentText, String timeStamp, String commenterName, int commenterId) {
        this.commenterId = commenterId;
        this.commentText = commentText;
        this.timeStamp = timeStamp;
        this.commenterName = commenterName;
        //this.commentId = commentId;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Comment)) return false;

        Comment comment = (Comment) o;

        if (getCommenterId() != comment.getCommenterId()) return false;
        if (getCommentId() != comment.getCommentId()) return false;
        if (getCommentText() != null ? !getCommentText().equals(comment.getCommentText()) : comment.getCommentText() != null)
            return false;
        if (getTimeStamp() != null ? !getTimeStamp().equals(comment.getTimeStamp()) : comment.getTimeStamp() != null)
            return false;
        return !(getCommenterName() != null ? !getCommenterName().equals(comment.getCommenterName()) : comment.getCommenterName() != null);

    }

    @Override
    public int hashCode() {
        int result = getCommenterId();
        result = 31 * result + (getCommentText() != null ? getCommentText().hashCode() : 0);
        result = 31 * result + (getTimeStamp() != null ? getTimeStamp().hashCode() : 0);
        result = 31 * result + (getCommenterName() != null ? getCommenterName().hashCode() : 0);
        result = 31 * result + getCommentId();
        return result;
    }

    public String getCommenterName() {

        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    private String commenterName;

    public int getCommenterId() {
        return commenterId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commenterId=" + commenterId +
                ", commentText='" + commentText + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", commentId=" + commentId +
                '}';
    }

    public void setCommenterId(int commenterId) {
        this.commenterId = commenterId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    private int commentId;

    public Comment(int commenterId, String commentText, String timeStamp, int commentId) {
        this.commenterId = commenterId;
        this.commentText = commentText;
        this.timeStamp = timeStamp;
        this.commentId = commentId;
    }
}
