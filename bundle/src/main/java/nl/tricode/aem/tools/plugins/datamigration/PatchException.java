package nl.tricode.aem.tools.plugins.datamigration;

public class PatchException extends Exception {

    public PatchException(String msg) {
        super(msg);
    }

    public PatchException(String msg, Throwable c) {
        super(msg, c);
    }
}
