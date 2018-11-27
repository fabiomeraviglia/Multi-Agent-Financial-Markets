package GeneticOptimization;

public abstract class Gene<T>{
    String name;
    T value;
    public Gene(String name, T value)
    {
        this.name=name;
        this.value=value;
    }
    public T getValue()
    {
        return value;
    }
    public abstract Gene<T> getMutation();


    public abstract boolean equals(Object other);
    public abstract int hashCode();
}
