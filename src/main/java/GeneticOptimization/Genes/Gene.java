package GeneticOptimization.Genes;

import Simulation.Configuration;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;

public abstract class Gene<T> implements Cloneable, Serializable {
    final private String name;
    final private T value;
    Gene(String name, T value)
    {
        this.name=name;
        this.value=value;
    }
    final T getValue()
    {
        return value;
    }
    final String getName() {return  name; }

    /**
     *
     * @param variationMagnitude
     * A number between 0 and 1, where 0 means the variation will always be small, and 1 that will be big.
     * So it represents the probability of having a big variation
     * @return
     */
    public abstract Gene<T> getMutation(Double variationMagnitude);

    public abstract Gene<T> getRandomGene();
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gene)) return false;
        Gene<?> gene = (Gene<?>) o;
        return Objects.equals(getName(), gene.getName()) &&
                Objects.equals(getValue(), gene.getValue());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getName(), getValue());
    }

    public abstract Gene<T> clone();
    public final void setConfigurationParameter(Configuration configuration)
    {
        Class<?> c = configuration.getClass();

        Field parameter;
        try {
            parameter = c.getDeclaredField(this.name);

            parameter.set(configuration, value);
        }
        catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException("Error in gene.setConfigurationParameter");
        }

    }
   @Override public String toString(){
        return name+":"+value;
    }
}
