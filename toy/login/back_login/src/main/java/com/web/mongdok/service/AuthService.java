//package com.web.mongdok.service;
//
//
//public interface AuthService {
//
//    final String REDIS_CHANGE_PASSWORD_PREFIX="CPW";
//
//    void signUpUser(Member member);
//
//    void signUpSocialUser(RequestSocialData member);
//
//    Member loginSocialUser(String id, String type) throws NotFoundException;
//
//    Member loginUser(String id, String password) throws Exception;
//
//    Member findByUsername(String username) throws NotFoundException;
//
//    void verifyEmail(String key) throws NotFoundException;
//
//    void sendVerificationMail(Member member) throws NotFoundException;
//
//    void modifyUserRole(Member member, UserRole userRole);
//
//    boolean isPasswordUuidValidate(String key);
//
//    void changePassword(Member member, String password) throws NotFoundException;
//
//    void requestChangePassword(Member member) throws NotFoundException;
//}
