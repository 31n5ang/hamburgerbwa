package kr.hamburgersee.domain.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final HttpServletRequest request;

    public void create(SessionAttrType sessionAttrType, Object value) {
        HttpSession session = request.getSession(true);
        session.setAttribute(sessionAttrType.attribute, value);
    }

    public Object find(SessionAttrType sessionAttrType) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getAttribute(sessionAttrType.attribute);
        } else {
            return null;
        }
    }

    public void remove(SessionAttrType sessionAttrType) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(sessionAttrType.attribute);
        }
    }

    public void clear() {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
