package net.qoopo.framework.security.filter.strategy.failure;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.qoopo.framework.exception.NullArgumentException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MixFailureStrategy implements FailureStrategy {

    private List<FailureStrategy> strategies;

    public MixFailureStrategy(FailureStrategy... strategies) {
        this(Arrays.asList(strategies));
    }

    public MixFailureStrategy(List<FailureStrategy> strategies) {
        if (strategies == null)
            throw new NullArgumentException();
        this.strategies = strategies;
    }

    @Override
    public void onFailure(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            net.qoopo.framework.security.exception.SecurityException exception)
            throws ServletException, IOException {
        for (FailureStrategy strategy : strategies) {
            strategy.onFailure(request, response, chain, exception);
        }
    }

}
