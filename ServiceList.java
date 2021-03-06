import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * ServiceList
 *
 * @author u360264
 * @version 20171007
 */
public class ServiceList
{
    public static final String SERVICE_FILE_PATH = "ServiceCodes.txt";
    public static final String TEXTSEPERATOR = ",";
    public static final String ORBITSEPERATOR = "\\|";

    // instance variables - replace the example below with your own
    private BufferedReader brReader;
    private List<Service> lsServiceList = new ArrayList();

    /**
     * Constructor for objects of class ServiceList
     */
    public ServiceList()
    { 
        // initialise instance variables
        loadServices();
    }

    public List<String> getNames() {
        List<String> lsStrings = new ArrayList();
        for(int i = 0; i<lsServiceList.size(); i++) {
            if(!("NOR".equals(lsServiceList.get(i).firstOrbit())))
                lsStrings.add(lsServiceList.get(i).getName());
        }
        return lsStrings;
    }

    public List<Service> getServiceList() { return lsServiceList;}
    
    public double getInsuranceCost() {
     return getServiceByCode("LLOYDS").getPrice();   
    }
    
    public double getNitrogenCost() {
     return getServiceByCode("ONF").getPrice();   
    }
    
    private Service getServiceByCode(String sCode) {
        String[] nor = new String[] {"NOR"};
    Service sService = new Service("Not Found", "Service Not Found", -10.00,nor, "N");
        for(Service tempService: lsServiceList) {
         if(tempService.getName().equals(sCode))
         return tempService;
        }
        return sService;
    }

    private void loadServices() {

        try {
            brReader =  new BufferedReader(new FileReader(SERVICE_FILE_PATH));
            String sLine;
            while ((sLine = brReader.readLine()) != null)
            {
                try {
                    String[] saLineArray= sLine.split(TEXTSEPERATOR);

                    String sServiceCode = saLineArray[0].trim();
                    String sDescription = saLineArray[1].trim();

                    Double dPrice = null;
                    try {
                        dPrice  = Double.parseDouble(saLineArray[2].trim());
                    } catch (Exception e) { System.out.println("Error converting price"); }

                    String sOrbits = saLineArray[3].replaceAll(" ","");
                    String[] saOrbits = saLineArray[3].split(ORBITSEPERATOR);
                    for(int i = 0; i < saOrbits.length; i++) {
                        saOrbits[i] = saOrbits[i].trim();

                    }

                    String sManned = saLineArray[4].trim();
                    Service sService = new Service(sServiceCode, sDescription, dPrice, saOrbits, sManned);    
                    lsServiceList.add(sService);
                } catch (Exception e) 
                {
                    System.out.println("Failed to add service, skipping to next");   
                }
            }
            brReader.close();

        }
        catch (IOException e) {
            System.out.println("File Not Found");
        }
    }
}
