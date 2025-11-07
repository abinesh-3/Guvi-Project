package ExpenseTracker.ProjectExpenseTracker.config;


import ExpenseTracker.ProjectExpenseTracker.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.RequestCache;


@Configuration
public class SecurityConfig {


@Bean
PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

@Bean
UserDetailsService userDetailsService(UserRepository users) {
	return username -> users.findByEmail(username)
		.map(u -> org.springframework.security.core.userdetails.User
			.withUsername(u.getEmail())
			.password(u.getPassword())
			.disabled(!u.isEnabled())
			.roles(u.getRole().name())
			.build())
		.orElseThrow(() -> new UsernameNotFoundException("User not found"));
}

@Bean
RequestCache customRequestCache() {
	return new CustomRequestCache();
}


@Bean
SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	http
		.csrf(csrf -> csrf.disable())
		.requestCache(rc -> rc.requestCache(customRequestCache()))
		.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
		.formLogin(form -> form.loginPage("/login").permitAll().defaultSuccessUrl("/dashboard"))
		.logout(logout -> logout
			.logoutSuccessUrl("/login?logout")
			.permitAll()
		);

	return http.build();
}
}
