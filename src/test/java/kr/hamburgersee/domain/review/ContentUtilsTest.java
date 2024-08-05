package kr.hamburgersee.domain.review;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContentUtilsTest {
    @Test
    void contentTest() {
        String testContent = "<p>few &lt;img src=\"htt\"&gt;  <img src=\"http..\" alt=\"image\" " +
                "contenteditable=\"false\"><br></p><p>wefewfw</p><p>fewfewfew</p><p>fweewf</p>";

        System.out.println("testContent = " + testContent);

        String purifiedContent = ContentUtils.purifyHtmlTagContent(testContent, "(사진)");
        System.out.println("purifiedContent = " + purifiedContent);

        String omittedContent = ContentUtils.omitContent(purifiedContent, "...", 50);
        System.out.println("omittedContent = " + omittedContent);
    }
}
