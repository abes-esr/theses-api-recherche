package fr.abes.thesesapirecherche.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class MaintenanceFilter implements Filter {

    @Value("${maintenance}")
    private boolean isMaintenance;

    @Value("${maintenance.message}")
    private String maintenanceMsg;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (isMaintenance) {
            httpResponse.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            httpResponse.getWriter().write(maintenanceMsg);
        } else {
            chain.doFilter(request, response);
        }
    }
}