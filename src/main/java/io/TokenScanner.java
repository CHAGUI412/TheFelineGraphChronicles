package io;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Lee el texto de entrada como una secuencia de tokens separados por
 * espacios, sin importar los saltos de línea. Lo usan todos los parsers.
 */
public final class TokenScanner {

    private final StringTokenizer tokenizer;

    public TokenScanner(String rawInput) {
        this.tokenizer = new StringTokenizer(rawInput);
    }

    public boolean hasNext() {
        return tokenizer.hasMoreTokens();
    }

    public int nextInt() {
        return Integer.parseInt(nextToken());
    }

    public long nextLong() {
        return Long.parseLong(nextToken());
    }

    public String nextToken() {
        if (!hasNext()) {
            throw new NoSuchElementException("Unexpected end of input");
        }
        return tokenizer.nextToken();
    }
}