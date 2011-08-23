package exanpe.t5.lib.demo.bean;

import org.apache.tapestry5.beaneditor.NonVisual;

public class User
{
    @NonVisual
    private long id;

    private String firstName;

    private String lastName;

    private int age;

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age; }
}