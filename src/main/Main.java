package main;

public class Main {
    public static void main(String[] args) throws InvalidResponseException {
        RandomORG r1 = new RandomORG();

        String s1 = r1.generateString(10, 8, "abcdefghijklmnopqrstuvwxyz.ABCDEGHIJKLMNOPQRSTUVWXYZ#,;:0123456789", false);
        System.out.println(
                "Generating 10 alphanumeric 8 length strings:\n"+
                s1+"\n"
        );

        System.out.println(
                "Generating 5 Integer Sequences ranging from 21-101\n"+
                r1.generateIntegerSequence(5,10,21,101,false,NumberBases.Decimal.getBase())+"\n"
        );

        System.out.println(
                "Api Details:\n"+
                        r1.getAPIDetails()+"\n"
        );
    }
}
