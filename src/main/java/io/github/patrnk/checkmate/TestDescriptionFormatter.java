package io.github.patrnk.checkmate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

final class TestDescriptionFormatter {
    /**
     * A symbol that separates two consecutive answers.
     */
    private static final String QUESTION_SEPARATOR_REGEX = "\n";
    /**
     * A symbol that separates a question number and an answer.
     */
    private static final String ANSWER_SEPARATOR_REGEX = "\\)";
    
    // List<Pattern>.get(0) is always null in order to make indices correspond
    // to question numbers.
    // TODO write javadoc for formRegexList
    public List<Pattern> formRegexList(String testDescription) 
        throws ParseException, PatternSyntaxException, AnswerNotProvidedException,
        TooManyAnswersProvidedException, IncorrectQuestionNumberException  {
        List<Pattern> answerKey = new ArrayList();
        int MAX_NUMBER_OF_ANSWERS = 1000;
        int NUMBER_OF_QUESTIONS = 1000;
        
        testDescription = testDescription.trim();
        testDescription = testDescription.toLowerCase();
        String[] questions = testDescription.split(QUESTION_SEPARATOR_REGEX);
        if (questions.length > MAX_NUMBER_OF_ANSWERS) {
            throw new TooManyAnswersProvidedException("Too many answers provided "
                + "(max: " + MAX_NUMBER_OF_ANSWERS + " input: " 
                + questions.length + ")");
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
            if (questionNumber <= 0 || questionNumber > NUMBER_OF_QUESTIONS) {
                throw new IncorrectQuestionNumberException("Question number "
                    + "must be positive integer no greater than "
                    + NUMBER_OF_QUESTIONS + " (input: " + questionNumber + ")", i);
            }
            assert(questionNumber > 0);
            if (questionNumber >= answerKey.size()) {
                for (int j = answerKey.size(); j <= questionNumber; j++) {
                    answerKey.add(null);
                }
            }
            assert(questionNumber < answerKey.size());
            
            String answer = questions[i].split(ANSWER_SEPARATOR_REGEX)[1].trim();
            String regex = formRegex(answerKey.get(questionNumber), answer);
            answerKey.set(questionNumber, Pattern.compile(regex));
        }
        for (int i = 1; i < answerKey.size(); i++) {
            if (answerKey.get(i) == null) {
                throw new AnswerNotProvidedException("Question number " 
                    + (i + 1) + " does not have an answer.");
            }
        }
        return answerKey;
    }
    
    private String formRegex(Pattern previousPattern, String answer) {
        String PREFIX = "^(";
        String SUFFIX = ")$";
        String regex = PREFIX;
        // copy previous answer without prefix and suffix.
        if (previousPattern != null) {
            String previousAnswer = previousPattern.toString();
            assert(previousAnswer.startsWith(PREFIX));
            assert(previousAnswer.endsWith(SUFFIX));
            regex += previousAnswer.substring(PREFIX.length(),
                previousAnswer.length() - SUFFIX.length());
            regex += "|";
        }
        
        if (answer.charAt(0) == '*') {
            regex += Pattern.quote(answer.substring(1));
        } else {
            regex += formUnorderedAnwser(answer);
        }
        
        regex += SUFFIX;
        return regex;
    }
    
    // Example output: (?=.*a)(?=.*b)[ab]{2}
    private String formUnorderedAnwser(String answer) {
        String unorderedAnswer = "";
        for (char c : answer.toCharArray()) {
            unorderedAnswer += "(?=.*";
            unorderedAnswer += Pattern.quote(String.valueOf(c));
            unorderedAnswer += ")";
        }
        unorderedAnswer += "[";
        for (char c : answer.toCharArray()) {
            unorderedAnswer += Pattern.quote(String.valueOf(c));
        }
        unorderedAnswer += "]";
        unorderedAnswer += "{" + answer.length() + "}";
        return unorderedAnswer;
    }
}
