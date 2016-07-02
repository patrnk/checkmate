package io.github.patrnk.checkmate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

final class TestDescriptionRegexFormatter {
    /**
     * A symbol that separates two consecutive answers.
     */
    private static final String QUESTION_SEPARATOR_REGEX = "\n";
    /**
     * A symbol that separates a question number and an answer.
     */
    private static final String ANSWER_SEPARATOR_REGEX = "\\)";
    
    // TODO write javadoc for formRegexList
    public List<Pattern> formRegexList(String testDescription) throws ParseException  {
        List<Pattern> answerKey = new ArrayList();
        
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

//    /**
//     * List of regex which are used to check the answers.
//     * An answer can be expected to be in one of three forms:
//     * <br> 1) <code>^[abc]$</code> — used when there are several mutually
//     *      exclusive answers. Here, "a", "b" or "c" are all accepted answers.
//     * <br> 2) <code>^word$</code> — used when an answer is an ordered 
//     *      sequence of symbols. Here, "word" is only accepted answer.
//     * <br> 3) <code>^(?=.*a)(?=.*b)[ab]{2}$</code> — used when an answer
//     *      is a unordered sequence of symbols. Here, both "ab" and "ba" are accepted.
//     */
//    private final List<Pattern> answerKey = new ArrayList();