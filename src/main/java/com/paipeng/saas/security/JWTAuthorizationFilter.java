package com.paipeng.saas.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paipeng.saas.config.ApplicationConfig;
import com.paipeng.saas.tenant.model.User;
import com.paipeng.saas.tenant.repository.UserRepository;
import com.paipeng.saas.util.CommonUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private final static Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);
    private final static String HEADER = "Authorization";
    private final static String PREFIX = "Bearer ";
    //public static final String SECRET = "";

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        logger.info("doFilterInternal " + request.getRemoteHost() + " " + request.getMethod() + " " + request.getRequestURI());

        try {
            if (checkJWTToken(request, response)) {
                logger.info("jwt token found");
                String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
                logger.info("jwtToken: " + jwtToken);
                Claims claims = CommonUtil.validateToken(applicationConfig.getSecurityJwtSecret(), jwtToken);
                if (claims != null && claims.get("authorities") != null) {
                    setUpSpringAuthentication(claims, null);
                } else {
                    logger.error("validateToken failed");
                    SecurityContextHolder.clearContext();
                }
                User user = userRepository.findByToken(jwtToken);
                logger.info("findUser: " + user);
                if (claims != null && claims.get("authorities") != null) {
                    setUpSpringAuthentication(claims, user);
                } else {
                    logger.error("validateToken failed");
                    SecurityContextHolder.clearContext();
                }

                chain.doFilter(request, response);

            } else {
                logger.error("checkJWTToken failed");
                if (request.getMethod().equals("POST") && "/login".equals(request.getRequestURI())) {

                    //ServletRequest requestWrapper = new MultiReadHttpServletRequest(request);

                    RequestWrapper requestWrapper = new RequestWrapper(request);
                    ObjectMapper mapper = new ObjectMapper();
                    User user = mapper.readValue(requestWrapper.getInputStream(), User.class);
                    logger.info("login user: " + user.getUsername());
                    logger.info("login tenant: " + user.getTenant());
                    logger.info("login password: " + user.getPassword());
                    AppAuthenticationToken authToken = new AppAuthenticationToken(user.getUsername(), user.getPassword(), user.getTenant(), null);
                    authToken.setDetails(user);
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    chain.doFilter(requestWrapper, response);
                } else {
                    chain.doFilter(request, response);
                }
                SecurityContextHolder.clearContext();
            }

        } catch (ExpiredJwtException e) {
            logger.error("doFilterInternal ExpiredJwtException: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            //response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            logger.error("doFilterInternal exception local: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            logger.error("doFilterInternal exception21: " + e.getMessage());
            if (e.getMessage().endsWith("java.lang.Exception: 400")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            } else if (e.getMessage().endsWith("java.lang.Exception: 403")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            } else if (e.getMessage().endsWith("java.lang.Exception: 401")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            } else if (e.getMessage().endsWith("java.lang.Exception: 404")) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            } else if (e.getMessage().endsWith("java.lang.Exception: 409")) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
            } else if (e.getMessage().startsWith("JWT expired")) {
                logger.error("doFilterInternal JWT expired -> 401");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            } else {
                logger.error("exception not handle");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    /**
     * Authentication method in Spring flow
     *
     * @param claims claims
     */
    private void setUpSpringAuthentication(Claims claims, User user) {
        logger.info("setUpSpringAuthentication");
        @SuppressWarnings("unchecked")
        List<String> authorities = (List<String>) claims.get("authorities");

        /*
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        auth.setDetails(user);
        */

        AppAuthenticationToken authToken = new AppAuthenticationToken(claims.getSubject(), "", claims.getAudience(),
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

        authToken.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private boolean checkJWTToken(HttpServletRequest request, HttpServletResponse res) {
        String authenticationHeader = request.getHeader(HEADER);
        if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
            return false;
        return true;
    }

    public class RequestWrapper extends HttpServletRequestWrapper {
        private byte[] body;
        public RequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            this.body = StreamUtils.copyToByteArray(request.getInputStream());
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new ServletInputStreamWrapper(this.body);
        }
    }

    public class ServletInputStreamWrapper extends ServletInputStream {
        private InputStream inputStream;
        public ServletInputStreamWrapper(byte[] body) {
            this.inputStream = new ByteArrayInputStream(body);
        }

        @Override
        public boolean isFinished() {
            try {
                return inputStream.available() == 0;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {

        }

        @Override
        public int read() throws IOException {
            return this.inputStream.read();
        }
    }
}
