package cl.municipalidad.msreport.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad HTTP para el microservicio de reportes.
 *
 * <p>Define la política de autenticación y autorización usando Spring Security.
 * Al ser un servicio REST sin estado (stateless), se desactivan las sesiones HTTP
 * y la protección CSRF (no aplica para APIs JSON).</p>
 *
 * <p>Todos los endpoints de reportes son accesibles sin token JWT, ya que
 * la autenticación se valida en el BFF antes de llegar a este microservicio.
 * Este MS opera dentro de la red privada del clúster y no está expuesto
 * directamente al exterior.</p>
 *
 * @author Municipalidad Valle del Sol
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define la cadena de filtros de seguridad HTTP.
     *
     * <p>Configuración aplicada:
     * <ul>
     *   <li>CSRF desactivado: no necesario en APIs REST sin sesión.</li>
     *   <li>Sesiones STATELESS: cada request debe ser autónomo.</li>
     *   <li>Todos los endpoints de reportes permitidos sin token.</li>
     * </ul></p>
     *
     * @param http Objeto de configuración de seguridad HTTP inyectado por Spring.
     * @return {@link SecurityFilterChain} construida con la configuración definida.
     * @throws Exception si ocurre un error en la configuración de seguridad.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/reportes/**").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }
}