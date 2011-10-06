package exanpe.t5.lib.demo.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ValueEncoder;

public abstract class AjaxFormLoopEngine<T>
{
    private List<T> list = new ArrayList<T>();

    private T t;

    public List<T> getList()
    {
        return list;
    }

    public void setList(List<T> list)
    {
        this.list = list;
    }

    public T getT()
    {
        return t;
    }

    public void setT(T t)
    {
        this.t = t;
    }

    public abstract ValueEncoder<T> getEncoder();

    public T createNew()
    {
        T t = instanciate();
        list.add(t);
        return t;
    }

    protected abstract T instanciate();

    public void remove(T t)
    {
        if (!list.remove(t)) { throw new IllegalArgumentException("Impossible to remove the object " + t.toString()); }
    }

    public void clear()
    {
        list.clear();
    }
}
