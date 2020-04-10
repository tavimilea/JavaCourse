package com.company;

import java.util.Objects;

public class Token implements Comparable {
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

    @Override
    public int compareTo(Object o) {
        if(o  instanceof Token) {
            Token tokenCmp = (Token) o;
            return tokenCmp.getNumber() == this.getNumber() ? 0 : tokenCmp.getNumber() > this.getNumber() ? -1 : 1;
        } else if( o instanceof Integer) {
            int cmp = (int) o;
            return  cmp == this.getNumber() ? 0 : cmp > this.getNumber() ? -1 : 1;
        }
        throw new NullPointerException();
    }
}
