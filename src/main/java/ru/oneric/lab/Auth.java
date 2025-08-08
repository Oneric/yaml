package ru.oneric.lab;

public class Auth {
    private String role;
    private Credentials credentials;

    public Auth(String role, Credentials credentials) {
        this.role = role;
        this.credentials = credentials;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setUser(Credentials credentials) {
        this.credentials = credentials;
    }
}
