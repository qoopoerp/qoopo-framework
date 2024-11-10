package net.qoopo.framework.security.web.repository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HttpWarehouse {
    private HttpServletRequest request;
    private HttpServletResponse response;

    public static HttpWarehouse of(HttpServletRequest request, HttpServletResponse response) {
        return new HttpWarehouse(request, response);
    }
}
