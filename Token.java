package com.company;

import java.util.Objects;

public class Token {
    private int number;
    private boolean isSetted = false;
    Token(int number) {this.number = number;}
    Token(){}

    public int getNumber() {
        return number;
    }

    public boolean isSetted() {
        return isSetted;
    }

    public void setNumber(int number) {
        if(!isSetted) this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return number == token.number &&
                isSetted == token.isSetted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, isSetted);
    }
}
