package crypto.wallet.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.savedrequest.NullRequestCache;


@Configuration @EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable();
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);  
		// RequestCache 인터페이스는 로그인 화면을 보여주기 전에 사용자 요청을 저장하고
		// 이를 꺼내오는 메카니즘을 정의하는 인터페이스다. REST Server에서는 필요없다.
		http.requestCache().requestCache(new NullRequestCache());
		authorizeRequestsConfigure(http);
  }

  private void authorizeRequestsConfigure(HttpSecurity http) throws Exception { }
  
  @Override
  public void configure(WebSecurity web) throws Exception {
      // spring security 제외 경로설정 
//	  web.ignoring().antMatchers("/static/**");
//      web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**");
  }
}

