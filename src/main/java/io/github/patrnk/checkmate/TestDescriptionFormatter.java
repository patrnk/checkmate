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
    
    public List<Pattern> formRegexList(String testDescription) 
        throws ParseException, PatternSyntaxException {
        List<Pattern> answerKey = new ArrayList();
        int MAX_LINES = 1000;
        int MAX_RANGE = 1000;
        
        testDescription = testDescription.trim();
        String[] questions = testDescription.split(QUESTION_SEPARATOR_REGEX);
        if (questions.length > MAX_LINES) {
            //TODO custom exceptions
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
                if (questionNumber < 0 || questionNumber > MAX_RANGE) {
                    throw new IndexOutOfBoundsException();
                    // TODO custom exceptions
                }
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                throw new ParseException("The question number is incorrect.", i);
                // TODO custom exceptions
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
            regex += answer.substring(1);
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
            unorderedAnswer += c;
            unorderedAnswer += ")";
        }
        unorderedAnswer += "[";
        for (char c : answer.toCharArray()) {
            unorderedAnswer += c;
        }
        unorderedAnswer += "{" + answer.length() + "}";
        return unorderedAnswer;
    }
}
