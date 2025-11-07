package ExpenseTracker.ProjectExpenseTracker.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;

/**
 * RequestCache implementation that ignores certain request paths (error, login, static, root)
 * so these are not stored as saved requests. Delegates to HttpSessionRequestCache otherwise.
 */
public class CustomRequestCache implements RequestCache {
    private final HttpSessionRequestCache delegate = new HttpSessionRequestCache();

    private final List<RequestMatcher> ignore = Arrays.asList(
            new AntPathRequestMatcher("/error"),
            new AntPathRequestMatcher("/error/**"),
            new AntPathRequestMatcher("/login"),
            new AntPathRequestMatcher("/favicon.ico"),
            new AntPathRequestMatcher("/"),
            new AntPathRequestMatcher("/css/**"),
            new AntPathRequestMatcher("/register"),
            new AntPathRequestMatcher("/verify"),
            new AntPathRequestMatcher("/reset/**"),
            new AntPathRequestMatcher("/ws/**")
    );

    private boolean shouldSave(HttpServletRequest request) {
        if (request == null) return false;
        for (RequestMatcher m : ignore) {
            if (m.matches(request)) return false;
        }
        return true;
    }

    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        if (shouldSave(request)) {
            delegate.saveRequest(request, response);
        }
    }

    @Override
    public SavedRequest getRequest(HttpServletRequest request, HttpServletResponse response) {
        return delegate.getRequest(request, response);
    }

    @Override
    public HttpServletRequest getMatchingRequest(HttpServletRequest request, HttpServletResponse response) {
        return delegate.getMatchingRequest(request, response);
    }

    @Override
    public void removeRequest(HttpServletRequest request, HttpServletResponse response) {
        delegate.removeRequest(request, response);
    }
}
