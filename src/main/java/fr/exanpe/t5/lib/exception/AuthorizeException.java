package fr.exanpe.t5.lib.exception;

public class AuthorizeException extends RuntimeException
{
    /**
     * serial uid
     */
    private static final long serialVersionUID = -6093448612925417357L;

    public AuthorizeException(String message)
    {
        super(message);
    }

}
