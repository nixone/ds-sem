package exceptions;

import OSPExceptions.SimException;

public class DopravnikPrazdnyException extends SimException
{
	public DopravnikPrazdnyException(String message)
	{
		super(message);
	}
}