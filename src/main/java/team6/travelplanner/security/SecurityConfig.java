package team6.travelplanner.security;

import net.bytebuddy.asm.AsmVisitorWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Order(1)
    @Configuration
    public static class JWTConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        MyUserDetailsService userDetailsService;
        @Autowired
        private BCryptPasswordEncoder bCryptPasswordEncoder;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            //System.out.println("in auth config");
            auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable().authorizeRequests()
                    .antMatchers("/register").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login").successHandler(successHandler())
                    .and()
                    .oauth2Login().defaultSuccessUrl("/loginfromoauth");
                    //.addFilter(new JWTAuthenticationFilter(authenticationManager()));
                    // this disables session creation on Spring Security
                    //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

        /*
        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().anyRequest();
        }
         */

        private AuthenticationSuccessHandler successHandler() {
            return new AuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                    try {

                        String currentUserName = "";
                        if (!(authentication instanceof AnonymousAuthenticationToken)) {
                            currentUserName = authentication.getName();
                        }
                        httpServletResponse.getWriter().append("Login Success: " + currentUserName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    httpServletResponse.setStatus(200);
                }
            };
        }

    }

    @Order(2)
    @Configuration
    public static class GitHubConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable().authorizeRequests()
                    .antMatchers("/").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .oauth2Login();
        }
        /*
        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().anyRequest();
        }
        */

    }
}
