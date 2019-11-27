/* Copyright 2018 David Cai Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.niflheim.stockfish.exceptions;

/**
 * Thrown when {@link java.io.IOException} occurred during Stockfish instance initialization.
 *
 * @author Niflheim
 * @since 1.0
 */
@SuppressWarnings("unused")
public class StockfishEngineException extends RuntimeException {
    /**
     * Constructs a {@code StockfishEngineException} with no detail message.
     */
    public StockfishEngineException() {
        super();
    }

    /**
     * Constructs a {@code StockfishEngineException} with the specified
     * detail message.
     *
     * @param message the detail message.
     */
    public StockfishEngineException(String message) {
        super(message);
    }

    /**
     * Constructs a {@code StockfishEngineException} with the specified
     * detail message and the exception that caused.
     *
     * @param message the detail message.
     * @param cause   the exception that caused.
     */
    public StockfishEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a {@code StockfishEngineException} with the exception that caused.
     *
     * @param cause the exception that caused.
     */
    public StockfishEngineException(Throwable cause) {
        super(cause);
    }
}
