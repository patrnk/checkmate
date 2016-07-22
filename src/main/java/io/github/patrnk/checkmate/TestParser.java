package io.github.patrnk.checkmate;

import java.util.ArrayList;
import java.util.List;

/**
 * Knows how classes are written in String; how answers are written.
 * @author vergeev
 */
public final class TestParser 
{
    private static final Integer QUESTIONS_MAX = 1000;
    
    /**
     * A symbol that separates two consecutive answers.
     */
    private static final String QUESTION_SEPARATOR_REGEX = "\n";
    
    /**
     * A symbol that separates a question number and an answer.
     */
    private static final String ANSWER_SEPARATOR_REGEX = "\\)";
    
    
    
    /**
     * Same as getSeparatedAnswers() but case-insensitive.
     */
    private List<List<String>> getSeparatedLowerCaseAnswers(String rawAnswers) 
        throws MalformedTestDescriptionException, 
        TooManyQuestionsException, TooManyAnswersException 
    {
        return getSeparatedAnswers(rawAnswers.toLowerCase());
    }
    
    /**
     * Turns a formatted description of test into list of answers.
     * If a question has number i than its index in the 
     *      returned list will be i - 1.
     * The format is simple: questions must be separated with symbol described 
     * in QUESTION_SEPARATOR_REGEX and answers with ANSWER_SEPARATOR_REGEX.
     * For case-insensitive version, use getSeparatedLowerCaseAnswers.
     * @param rawAnswers the formatted description of test.
     * @return list of questions, each containing list of answers.
     * @throws MalformedTestDescriptionException if rawAnswers is not formatted correctly.
     * @throws TooManyQuestionsException if number of one of the questions exceeds 1000.
     * @throws TooManyAnswersException if there are more than 1000 answers provided.
     */
    private List<List<String>> getSeparatedAnswers(String rawAnswers)
        throws MalformedTestDescriptionException, 
        TooManyQuestionsException, TooManyAnswersException 
    {
        List<List<String>> answers = new ArrayList();
        rawAnswers = rawAnswers.trim();
        String[] questions = rawAnswers.split(QUESTION_SEPARATOR_REGEX);
        if (questions.length > QUESTIONS_MAX) {
            throw new TooManyAnswersException("Provided: " + 
                questions.length + " while the upper limit is " + QUESTIONS_MAX);
        }
        for (int i = 0; i < questions.length; i++) {
            questions[i] = questions[i].trim();
            throwExceptionIfQuestionMalformed(questions[i], i);
            Integer questionIndex = getQuestionIndex(questions[i], i);
            for (int j = answers.size(); j <= questionIndex; j++) {
                answers.add(new ArrayList());
            }
            assert(questionIndex < answers.size());
            String answer = getAnswer(questions[i]);
            answers.get(questionIndex).add(answer);
        }
        return answers;
    }
    
    /**
     * Specifies format in which questions are set.
     * @param question the question to check
     * @param errorOffset error offset which will be passed in case of exception
     * @throws MalformedTestDescriptionException if the question doesn't match
     *      to the format.
     */
    private void throwExceptionIfQuestionMalformed(String question, Integer errorOffset) 
        throws MalformedTestDescriptionException 
    {
        if (!question.matches(".+" + ANSWER_SEPARATOR_REGEX + ".+")) {
            throw new MalformedTestDescriptionException("The line number "
                + (errorOffset+1) + " formatted incorrectly. ", (errorOffset+1));
        }
    }
    
    /**
     * Parses out the number of the question and subtracts 1.
     * @param question question in a format specified 
     *      by throwExceptionIfQuestionMalformed
     * @param errorOffset error offset which will be passed in case of exception
     * @throws MalformedTestDescriptionException if the question doesn't match
     *      to the format.
     * @throws TooManyQuestionsException if the index of the question 
     *      greater than 1000.
     * @return index of the corresponding answer list. 
     *      It's guaranteed that <code>0 <= index <= 1000</code>.
     */
    private Integer getQuestionIndex(String question, Integer errorOffset)
        throws MalformedTestDescriptionException, TooManyQuestionsException
    {
        Integer questionNumber = -1;
        try {
            questionNumber = Integer.valueOf(
                    question.split(ANSWER_SEPARATOR_REGEX)[0].trim());
            if (questionNumber < 1) {
                throw new NumberFormatException("questionNumber is not natural");
            }
        } catch (NumberFormatException ex) {
            throw new MalformedTestDescriptionException("The question number "
                + "is not a natural number on line " + (errorOffset+1), 
                ex, (errorOffset+1));
        }
        Integer questionIndex = questionNumber - 1;
        assert(questionIndex >= 0);
        if (questionIndex > QUESTIONS_MAX) {
            throw new TooManyQuestionsException("Provided: " + 
                questionIndex + " while the upper limit is " + QUESTIONS_MAX);
        }
        return questionIndex;
    }
    
    private String getAnswer(String question) {
        return question.split(ANSWER_SEPARATOR_REGEX)[1].trim();
    }
}
