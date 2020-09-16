package tt;

import java.io.File;
import java.util.Map;
import java.util.Set;

import ek.schema.pds.dd.DDAttr;
import ek.schema.pds.dd.DDClass;
import ek.schema.pds.dd.DataDictionary;
import ek.schema.pds.dd.DataTypeMap;
import ek.schema.pds.dd.JsonDDParser;
import ek.util.CounterMap;


public class PrintCounts
{
    static DataTypeMap dtMap = new DataTypeMap();
    static CounterMap counters = new CounterMap();

    
    public static void main(String[] args) throws Exception
    {
        dtMap.load(new File("/tmp/schema/data-types-2.cfg"));
                
        JsonDDParser parser = new JsonDDParser(new File("/tmp/schema/PDS4_DAWN_1B00_1000.JSON"));
        DataDictionary dd = parser.parse();
        parser.close();
        
        processSchema(dd);
        
        for(CounterMap.Item item: counters.getCounts())
        {
            System.out.println(item.name + "  " + item.count);
        }
    }
    
    
    private static void processSchema(DataDictionary dd) throws Exception
    {
        Map<String, String> attrId2Type = dd.getAttributeDataTypeMap();
        Set<String> dataTypes = dd.getDataTypes();
        
        for(DDClass ddClass: dd.getClassMap().values())
        {
            // Skip type definitions.
            if(dataTypes.contains(ddClass.nsName)) continue;
            
            addClassAttributes(ddClass, attrId2Type);
        }
    }

    
    private static void addClassAttributes(DDClass ddClass, Map<String, String> attrId2Type) throws Exception
    {
        for(DDAttr attr: ddClass.attributes)
        {
            String pdsDataType = attrId2Type.get(attr.id);
            if(pdsDataType == null) throw new Exception("No data type mapping for attribute " + attr.id);
            
            //String fieldName = ddClass.nsName + "." + attr.nsName;
            String targetDataType = dtMap.getTargetType(pdsDataType);
            
            counters.inc(targetDataType);
        }
    }


}
