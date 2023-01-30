public class chapter {
    private int id;
    private String name;

    public chapter(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public chapter(String name) {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    @Override
    public String toString()
    {
        return "Chapter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}