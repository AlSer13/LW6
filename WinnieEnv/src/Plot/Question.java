package Plot;

import java.util.*;

public class Question implements Phrase {
    private final WTPcharacter speaker; //кто задает вопрос
    private ArrayList<WTPcharacter> listeners = new ArrayList<>(); //от кого ожидается ответ
    private Type type;
    private String text;
    private ArrayList<Answer> answers = new ArrayList<>();
    static public Set<String> Yes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("YES", "Y", "ДА", "Д","ДАВАЙ","OK")));
    static public Set<String> No = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("NO", "N", "НЕТ", "Н")));

    public Question(Type type, String text, WTPcharacter speaker, WTPcharacter... listeners) {
        this.text = text;
        this.speaker = speaker;
        this.listeners.addAll(Arrays.asList(listeners));
        this.type = type;
    }

    public class Answer implements Phrase{
        public String text;
        Answer(WTPcharacter answerer){
            this.answerer = answerer;
            answers.add(listeners.indexOf(answerer),this);
        }
        private WTPcharacter answerer;
        public void say() {
            System.out.println(answerer.getName() + ": " + text);
        }
    }

    public Answer getAnswer(WTPcharacter answerer){
        return answers.get(listeners.indexOf(answerer));
    }

    public void say() {
        StringBuilder appeal = new StringBuilder();
        for (WTPcharacter c : listeners) {
            appeal.append(", ").append(c.getName());
        }
        appeal.append("?");
        text = text.replaceAll("[?]", "");
        System.out.println(speaker.getName() + ": " + text + appeal);

    }

    public void makeAnswer() {
        class Answer implements Phrase {
            private WTPcharacter answerer;
            private String text;

            public String toString() {
                return this.text;
            }

            private Answer(WTPcharacter answerer) {
                this.answerer = answerer;
            }

            private void waitForAnsw(WTPcharacter l) {
                try {
                    System.out.println("Что же ответит " + l.getName() + "?");
                    Scanner in = new Scanner(System.in);
                    text = in.next();
                    if (!AnswIsValid(text)) {
                        throw new InvalidPhraseException();
                    }
                } catch (InvalidPhraseException e) {
                    e.tryAgain();
                    waitForAnsw(l);
                }
            }

            @Override
            public void say() {
                System.out.println(answerer.getName() + ": " + text);
            }
        }
        for (WTPcharacter l : listeners) {
            Answer a = new Answer(l);
            a.waitForAnsw(l);
            a.say();
            Question.Answer A = this.new Answer(l);
            A.text = a.text;
        }
    }

    private boolean AnswIsValid(String text) throws InvalidPhraseException {
        switch (type) {
            case Text:
                return true;
            case YorN:
                //case Choice:
                if (!Contains(text,type.ANSWERS)){
                    throw new InvalidPhraseException();
                }
                return true;
            default:
                return true;
        }
    }

    public static boolean Contains(String text, Set<String> set){
        return set.contains(text.toUpperCase().replaceAll(",",""));
    }

    public static class InvalidPhraseException extends Exception {
        static int k = 0;

        void tryAgain() {
            System.err.println("Наверняка персонаж не станет такого говорить.");
            k++;
            if (k >= 6) {
                System.err.println("Я так не играю :-b");
                System.exit(0);
            }
        }
    }

    public enum Type {
        YorN(Collections.unmodifiableSet(new HashSet<>(Arrays.asList("YES", "Y", "ДА", "Д","NO", "N", "НЕТ", "Н")))),
        //Choice(),
        Text();
        private Set<String> ANSWERS;

        Type(Set<String> ANSWERS) {
            this.ANSWERS = ANSWERS;
        }

        Type() {
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        if (speaker != null ? !speaker.equals(question.speaker) : question.speaker != null) return false;
        if (listeners != null ? !listeners.equals(question.listeners) : question.listeners != null) return false;
        if (type != question.type) return false;
        if (text != null ? !text.equals(question.text) : question.text != null) return false;
        return answers != null ? answers.equals(question.answers) : question.answers == null;
    }

    @Override
    public int hashCode() {
        int result = speaker != null ? speaker.hashCode() : 0;
        result = 31 * result + (listeners != null ? listeners.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (answers != null ? answers.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return this.text;
    }
}