package ek.pds.dd;

/**
 * Data dictionary attribute (attribute) 
 * @author karpenko
 */
public class DDAttr
{
    public String id;
    // <namespace>.<name>
    public String nsName;
    
    public DDAttr(String id)
    {
        this.id = id;
    }
}
