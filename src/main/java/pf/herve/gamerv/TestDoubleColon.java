package pf.herve.gamerv;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author chahd
 */
//https://blog.ippon.fr/2014/03/18/java-8-interfaces-fonctionnelles/
public class TestDoubleColon {

    public class Name {

        private String firstName;
        private String lastName;

        public Name(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

    }

    public class NameParser<T> {

        /*public T parse(String name, Creator creator) {
            String[] tokens = name.split(" ");
            String firstName = tokens[0];
            String lastName = tokens[1];
            return (T) creator.create(firstName, lastName);
        }*/
        
        public T parse(String name, Creator creator) {
            String[] tokens = name.split(" ");
            String firstName = tokens[0];
            String lastName = tokens[1];
            return (T) creator.create(firstName, lastName);
        }
    }
    @FunctionalInterface
    public interface Creator<T> {

        T create(String firstName, String lastName);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestDoubleColon programm = new TestDoubleColon();
        programm.start();
    }

    public void start() {
        NameParser<Name> parser = new NameParser();
        
        Name res = parser.parse("Eric Clapton", new Creator<Name>() {
            @Override
            public Name create(String firstName, String lastName) {
                return new Name(firstName, lastName);
            }
        });
        
        System.out.println(res.getLastName()+" "+res.getFirstName());
        
        Name res1 = parser.parse("John Lennon", Name::new);
        System.out.println(res1.getLastName()+" "+res1.getFirstName());

    }

}
