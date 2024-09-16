package net.qoopo.qoopoframework.util.exceptions;

/**
 * Excepcion personalizada de Qoopo
 *
 * @author alberto
 */
public class QoopoException extends Exception {

    /**
     * Creates a new instance of <code>QoopoException</code> without detail
     * message.
     */
    public QoopoException() {
        //
    }

    /**
     * Constructs an instance of <code>QoopoException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public QoopoException(String msg) {
        super(msg);
    }
}
