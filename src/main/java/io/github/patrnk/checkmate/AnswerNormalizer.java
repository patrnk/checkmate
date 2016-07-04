package io.github.patrnk.checkmate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


final class AnswerNormalizer {
    /**
     * A symbol that separates two consecutive answers.
     */
    private static final String QUESTION_SEPARATOR_REGEX = "\n";
    /**
     * A symbol that separates a question number and an answer.
     */
    private static final String ANSWER_SEPARATOR_REGEX = "\\)";
    
    public List<List<String>> getNormalAnswers(String testDescription) throws ParseException {
        List<List<String>> normalAnswers = new ArrayList();
        testDescription = testDescription.trim();
        testDescription = testDescription.toLowerCase();
        String[] questions = testDescription.split(QUESTION_SEPARATOR_REGEX);
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
            if (questionNumber >= normalAnswers.size()) {
                for (int j = normalAnswers.size(); j <= questionNumber; j++) {
                    normalAnswers.add(new ArrayList());
                }
            }
            assert(questionNumber < normalAnswers.size());
            
            String answer = questions[i].split(ANSWER_SEPARATOR_REGEX)[1].trim();
            String normalAnswer = normalize(answer);
            normalAnswers.get(questionNumber).add(normalAnswer);
        }
        return normalAnswers;
    }
    
    public String normalize(String answer) {
        if (answer.startsWith("*")) {
            return null;
        }
        String[] elements = answer.split(""); // in java prior to 8.0 behavior is different
        Arrays.sort(elements);
        String normalAnswer = "";
        for (String element : elements) {
            normalAnswer += element;
        }
        return normalAnswer;
    }
}
