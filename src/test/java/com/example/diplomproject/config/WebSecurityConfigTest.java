//package com.example.diplomproject.config;
//
//@TestConfiguration
//public class WebSecurityConfigTest {
//
//    private static final String[] AUTH_WHITELIST = {
//            "/swagger-resources/**",
//            "/swagger-ui.html",
//            "/v3/api-docs",
//            "/webjars/**",
//            "/login", "/register"
//
//    };
//
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user@gmail.com")
//                .password("password")
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeHttpRequests((authz) ->
//                        authz
//                                .mvcMatchers(AUTH_WHITELIST).permitAll()
//                                .mvcMatchers("/ads/**", "/users/**").authenticated()
//
//                )
//                .cors().disable()
//                .httpBasic(withDefaults());
//        return http.build();
//    }
//
//
//}
