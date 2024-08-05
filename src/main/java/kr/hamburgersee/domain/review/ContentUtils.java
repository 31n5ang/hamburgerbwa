package kr.hamburgersee.domain.review;

import java.util.Stack;

public final class ContentUtils {
    /**
     * 본문이 '본문입니다.', 생략기호가 '...' 이라면,
     * '본문입...'으로 반환합니다.
     * @param content 본문
     * @param ellipsis 생략 기호
     * @return 생략된 본문
     */
    public static String omitContent(String content, String ellipsis, int omittedLength) {
        String omittedContent = content;
        if (content.length() >= omittedLength) {
            omittedContent = content.substring(0, omittedLength);
            return omittedContent + ellipsis;
        }
        return omittedContent;
    }

    /**
     * html 태그를 제거하고, img 태그를 다른 문자열로 대체합니다.
     * @param content 본문
     * @param replace 대체 문자열
     * @return 태그가 모두 제거된 순수한 본문
     */
    public static String purifyHtmlTagContent(String content, String replace) {
        String replacedImgTagContent = content.replaceAll("<img[^>]*>", replace);
        return replacedImgTagContent.replaceAll("<[^>]*>", "");
    }
}
