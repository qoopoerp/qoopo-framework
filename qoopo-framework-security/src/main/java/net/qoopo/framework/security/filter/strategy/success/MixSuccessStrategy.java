package net.qoopo.framework.security.filter.strategy.success;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.NullArgumentException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.qoopo.framework.security.authentication.Authentication;

public class MixSuccessStrategy implements SuccessStrategy {

    private List<SuccessStrategy> strategies;

    public MixSuccessStrategy(SuccessStrategy... strategies) {
        this(Arrays.asList(strategies));
    }

    public MixSuccessStrategy(List<SuccessStrategy> strategies) {
        if (strategies == null)
            throw new NullArgumentException();
        this.strategies = strategies;
    }

    @Override
    public void onSucess(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        for (SuccessStrategy strategy : strategies) {
            strategy.onSucess(request, response, chain, authResult);
        }
    }

}
