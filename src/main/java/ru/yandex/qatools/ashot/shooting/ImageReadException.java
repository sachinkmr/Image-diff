package ru.yandex.qatools.ashot.shooting;

/**
 * @author <a href="frolic@yandex-team.ru">Vyacheslav Frolov</a>
 */
public class ImageReadException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageReadException(String message) {
	super(message);
    }

    public ImageReadException(String message, Exception e) {
	super(message, e);
    }
}
