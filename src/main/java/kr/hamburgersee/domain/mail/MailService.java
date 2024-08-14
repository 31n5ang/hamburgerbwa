package kr.hamburgersee.domain.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public long sendVerifyCode(String to, int digits) {
        long code = generateCode(digits);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(to);
            helper.setSubject("함버거봐 - 회원가입 인증번호 : " + code);
            helper.setText(String.format(
                    """
                            <html>
                                <body>
                                    <h1>함버거봐</h1>
                                    <h3>인증번호는 다음과 같습니다.</h3>
                                    <p style='color: red; font-weight: bold;'>%s</p>
                                </body>
                            </html>
                            """, code), true
            );
            mailSender.send(message);
            return code;
        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송 오류입니다!", e);
        }
    }

    private static long generateCode(int digits) {
        long min = (long) Math.pow(10, digits - 1);
        long max = (long) Math.pow(10, digits) - 1;
        return (min + (long) (Math.random() * (max - min + 1)));
    }
}
