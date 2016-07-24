package io.github.patrnk.checkmate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Knows how classes are written in String; how answers are written.
 * @author vergeev
 */
public final class AnswerFormatter 
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
     * Applies normalize(String value) to each answer.
     * @param values list of lists of strings to normalize.
     * @return normalized strings.
     */
    public List<List<String>> normalize(List<List<String>> values) 
    {
        List<List<String>> normalStrings = new ArrayList();
        for (List<String> stringList : values) {
            normalStrings.add(new ArrayList());
            for (String answer : stringList) {
                String normalAnswer = AnswerFormatter.this.normalize(answer);
                normalStrings.get(normalStrings.size() - 1).add(normalAnswer);
            }
        }
        return normalStrings;
    }
    
    /**
     * Makes the passed string "normal".
     * "Normal" means that all characters are lower-case, alphabetically sorted.
     * If the answer starts with *, then its normal form is null value.
     * @param value a string to normalize
     * @return normal (see definition above) string
     */
    public String normalize(String value) {
        if (value.startsWith("*")) {
            return null;
        }
        String[] elements = value.split(""); // behavior may vary between ver.
        Arrays.sort(elements);
        String normalValue = "";
        for (String element : elements) {
            normalValue += element;
        }
        return normalValue;
    }
    
    /**
     * Returns answers that are to be graded. Don't confuse with answer key.
     * @param answerList list of answers like one returned by getSeparatedAnswers().
     * @return array of answers without grades.
     * @throws MalformedTestDescriptionException if an element of answerList 
     *      has size bigger than 1.
     */
    public ArrayList<TestAnswer> getTestAnswers(List<List<String>> answerList) 
        throws MalformedTestDescriptionException 
    {
        ArrayList<TestAnswer> answers = nullTestAnswerList(answerList.size());
        for (int i = 0; i < answerList.size(); i++) {
            if (answerList.get(i).size() > 1) {
                throw new MalformedTestDescriptionException("There must be only "
                    + "one answer to be graded.", i + 1);
            }
            String answer = "";
            if (answerList.get(i).size() == 1) {
                answer = answerList.get(i).get(0);
            }
            answers.set(i, new TestAnswer(answer));
        }
        return answers;
    }
    
    private ArrayList<TestAnswer> nullTestAnswerList(Integer size) 
    {
        ArrayList<TestAnswer> nullList = new ArrayList(size);
        while (nullList.size() < size) {
            nullList.add(null);
        }
        return nullList;
    }
    
    /**
     * Turns a formatted description of test answers into regex patterns that
     *      can be used as answer key.
     * Take the example input:
     * <code> 
     * 1) 3
     * 2) абвг
     * 2) abcde
     * 4) *Proton
     * 5) *256
     * </code>
     * Note that questions are separated by line breaks and answers by ")".
     * Answer to the first question is "3", so the pattern matches only "3".
     * Answer to the second question is either "абвг" or "abcde". Pattern also 
     *      matches symbols in any order, i.e. "abdec" is a correct answer too. 
     *      Notice that "abc" is an incorrect answer.
     * Answer to the third question is "Proton". And "proton". 
     *      And even "pRoToN". What matters is the order of the symbols.
     * Answer to the fifth answer is only 256.
     * Every pattern is case-insensitive.
     * @param rawAnswers answer description in the format described above.
     * @return list of patterns that works as described above. 
     *      Element is null if no answer provided.
     * @throws MalformedTestDescriptionException if rawAnswers is not formatted correctly.
     * @throws TooManyQuestionsException if number of one of the questions exceeds 1000.
     * @throws TooManyAnswersException if there are more than 1000 answers provided.
     */
    public List<Pattern> getAnswerKey(String rawAnswers) 
        throws MalformedTestDescriptionException, 
        TooManyQuestionsException, TooManyAnswersException 
    {
        List<List<String>> questions = this.getSeparatedLowerCaseAnswers(rawAnswers);
        List<Pattern> answerKey = nullPatternList(questions.size());
        for (int i = 0; i < questions.size(); i++) {
            for (String answer : questions.get(i)) {
                Pattern regex = formRegex(answerKey.get(i), answer);
                answerKey.set(i, regex);
            }
        }
        return answerKey;
    }
    
    /**
     * Creates List<Pattern> of a given size with null elements.
     * @param size size of the list
     * @return list of nulls.
     */
    private List<Pattern> nullPatternList(Integer size) 
    {
        List<Pattern> nullList = new ArrayList(size);
        while (nullList.size() < size) {
            nullList.add(null);
        }
        return nullList;
    }
    
    private Pattern formRegex(Pattern previousPattern, String answer) 
    {
        String PREFIX = "^(";
        String SUFFIX = ")$";
        String regex = PREFIX;
        if (previousPattern != null) {
            // copy previous answer without prefix and suffix.
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
        return Pattern.compile(regex);
    }
    
    private String formUnorderedAnwser(String answer) 
    {
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
    
    /**
     * Same as getSeparatedAnswers() but case-insensitive.
     */
    public List<List<String>> getSeparatedLowerCaseAnswers(String rawAnswers) 
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
    public List<List<String>> getSeparatedAnswers(String rawAnswers)
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
