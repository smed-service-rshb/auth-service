package ru.softlab.efr.services.auth.exceptions;

/**
 * Исключение, обозначающее отсутствие информации о сегменте
 */
public class SegmentNotFoundException extends AuthServiceException {
    /**
     * Конструктор
     */
    public SegmentNotFoundException(){
        super();
    }

    /**
     * Конструктор
     * @param message сообщение
     */
    public SegmentNotFoundException(String message){
        super(message);
    }

    /**
     * Конструктор
     * @param e исходное исключение
     */
    public SegmentNotFoundException(Exception e){
        super(e);
    }

    /**
     * Конструктор
     *
     * @param message сообщение
     * @param e исходное исключение
     */
    public SegmentNotFoundException(String message, Exception e){
        super(message, e);
    }
}
