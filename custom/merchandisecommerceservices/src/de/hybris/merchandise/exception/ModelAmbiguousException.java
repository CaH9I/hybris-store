package de.hybris.merchandise.exception;

public class ModelAmbiguousException extends RuntimeException
{
	public ModelAmbiguousException()
	{
	}

	public ModelAmbiguousException(final String message)
	{
		super(message);
	}

	public ModelAmbiguousException(final Throwable cause)
	{
		super(cause);
	}

	public ModelAmbiguousException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
