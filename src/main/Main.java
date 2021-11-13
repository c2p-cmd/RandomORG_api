package main;

public class Main {
    public static void main(String[] args) throws InvalidResponseException {
        RandomORG r1 = new RandomORG();

        String s1 = r1.generateString(10, 8, "abcdefghijklmnopqrstuvwxyz.ABCDEGHIJKLMNOPQRSTUVWXYZ#0123456789", false);
        System.out.println(
                "Generating data string:\n"+
                s1+"\n"
        );

        System.out.println(
                "Generating multiple Integer Sequences:\n"+
                r1.generateIntegerSequence(1,10,21,101,true,NumberBases.Decimal.getBase())+"\n"
        );

        System.out.println(
                "Api Deatils:\n"+
                        r1.getAPIDetails()+"\n"
        );
    }
}
