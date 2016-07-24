package io.github.patrnk.checkmate.test.exception;


public class BadTestInfoException extends Exception {
    
    public BadTestInfoException() {
        super();
    }
    
    public BadTestInfoException(String message) {
        super(message);
    }
    
    public BadTestInfoException(Throwable cause) {
        super(cause);
    }
    
    public BadTestInfoException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Returns text of error meant for the user.
     * @param ex the caught exception
     * @return error meassage that can be embedded into the UI
     */
    public static String getAppropriateErrorMessage(BadTestInfoException ex) {
        if (ex.getClass().equals(BadTestNameException.class)) {
            return "Плохо задано имя теста. "
                + "Оно не может быть пустым или очень длинным.";
        } else
        if (ex.getClass().equals(BadTestIdException.class)) {
            return "Идентификатор должен быть целым числом. "
                + "Убедитесь, что он уникален.";
        } else
        if (ex.getClass().equals(MalformedTestDescriptionException.class)) {
            MalformedTestDescriptionException specificEx = 
                (MalformedTestDescriptionException) ex;
            return "Нарушен формат записи ответа (см. строку " 
                + specificEx.getBadLine() + "). "
                + "Вот пример правильной записи: \"12)abc\".";
        } else
        if (ex.getClass().equals(AnswerNotProvidedException.class)) {
            AnswerNotProvidedException specificEx = 
                (AnswerNotProvidedException) ex;
            return "Не задан правильный ответ для номера " 
                + specificEx.getQuestionNumber() + ".";
        } else
        if (ex.getClass().equals(TooManyAnswersException.class)) {
            return "Задано слишком много ответов.";
        } else
        if (ex.getClass().equals(TooManyQuestionsException.class)) {
            return "Задано слишком много вопросов или номер одного из вопросов "
                + "невероятно большой.";
        } else
        return "Разработчик сломал эту функцию. Напишите ему: patrnk@gmail.com";
    }
}
