package io.github.patrnk.checkmate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


final class AnswerParser {
    /**
     * A symbol that separates two consecutive answers.
     */
    private static final String QUESTION_SEPARATOR_REGEX = "\n";
    /**
     * A symbol that separates a question number and an answer.
     */
    private static final String ANSWER_SEPARATOR_REGEX = "\\)";
    
    private static final Integer QUESTIONS_MAX = 1000;
    
    public static List<TestAnswer> getTestAnswers(String rawAnswers) 
        throws ParseException, TooManyQuestionsException, TooManyAnswersException {
        
        List<TestAnswer> answers = new ArrayList();
        rawAnswers = rawAnswers.trim();
        rawAnswers = rawAnswers.toLowerCase();
        String[] questions = rawAnswers.split(QUESTION_SEPARATOR_REGEX);
        if (questions.length > QUESTIONS_MAX) {
            throw new TooManyAnswersException("Provided: " + 
                    questions.length + " while the upper limit is " + QUESTIONS_MAX);
        }
        for (int i = 0; i < questions.length; i++) {
            questions[i] = questions[i].trim();
            if (!questions[i].matches(".+" + ANSWER_SEPARATOR_REGEX + ".+")) {
                throw new ParseException("The line number "
                        + i + " formatted incorrectly. ", i);
            }

            Integer questionNumber = -1;
            try {
                questionNumber = Integer.valueOf(
                        questions[i].split(ANSWER_SEPARATOR_REGEX)[0].trim());
            } catch (NumberFormatException e) {
                throw new ParseException("The question number is not a number "
                    + "on line " + i, i);
            }
            questionNumber -= 1;
            assert(questionNumber >= 0);
            if (questionNumber > QUESTIONS_MAX) {
                throw new TooManyQuestionsException("Provided: " + 
                    questionNumber + " while the upper limit is " + QUESTIONS_MAX);
            }
            if (questionNumber >= answers.size()) {
                for (int j = answers.size(); j <= questionNumber; j++) {
                    answers.add(null);
                }
            }
            assert(questionNumber < answers.size());
            
            String answer = questions[i].split(ANSWER_SEPARATOR_REGEX)[1].trim();
            answers.set(i, new TestAnswer(answer));
        }
        return answers;
    }
    
    private AnswerParser() {
        throw new AssertionError("AnswerParser cannot be instantiated.");
    }
}
