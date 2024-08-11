package kr.hamburgersee.domain.member;

public final class MemberFormConstant {
    private MemberFormConstant() {}
    public static final String DUPLICATE_ERROR_CODE = "Duplicate";
    public static final String INCORRECT_ERROR_CODE = "Incorrect";
    public static final String FAILED_ERROR_CODE = "Failed";

    public static final String INCORRECT_LOGIN_ERROR_CODE = "Incorrect.login";
    public static final String FAILED_UPLOAD_ERROR_CODE = "Failed.upload";

    public static final String EMAIL_FIELD = "email";
    public static final String NICKNAME_FIELD = "nickname";
    public static final String PROFILE_IMAGE_FIELD = "profileImageBase64";
}
