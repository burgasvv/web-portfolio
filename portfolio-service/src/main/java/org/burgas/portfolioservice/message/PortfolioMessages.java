package org.burgas.portfolioservice.message;

import lombok.Getter;

@Getter
public enum PortfolioMessages {

    PORTFOLIO_DELETED("Portfolio deleted"),
    PORTFOLIO_NOT_FOUND("Portfolio not found"),
    PORTFOLIO_FIELD_NAME_EMPTY("Portfolio field name is empty"),
    PORTFOLIO_FIELD_DESCRIPTION_EMPTY("Portfolio field description is empty"),
    PORTFOLIO_FIELD_IDENTITY_EMPTY("Portfolio field identity is empty"),
    PORTFOLIO_FIELD_PROFESSION_EMPTY("Portfolio field profession is empty"),
    PORTFOLIO_FIELD_OPENED_EMPTY("Portfolio field opened is empty");

    private final String message;

    PortfolioMessages(String message) {
        this.message = message;
    }
}
