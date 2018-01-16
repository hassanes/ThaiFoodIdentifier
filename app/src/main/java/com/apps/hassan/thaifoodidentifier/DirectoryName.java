package com.apps.hassan.thaifoodidentifier;


public class DirectoryName {
    public String uid;
    public String name;

    public DirectoryName() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public DirectoryName(String name) {
        this.name = name;
    }

    public DirectoryName(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }
}
