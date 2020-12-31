package com.example.ubertaxi;

public class RegisterResponse {
  String   objectId;
  String   createdAt;
  String   sessionToken;

  public RegisterResponse(String objectId, String createdAt, String sessionToken) {
    this.objectId = objectId;
    this.createdAt = createdAt;
    this.sessionToken = sessionToken;
  }

  public String getObjectId() {
    return objectId;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public String getSessionToken() {
    return sessionToken;
  }
}
