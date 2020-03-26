package ar.com.playmedia;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ArrayList<Contact> iContacts = new ArrayList<Contact>();
        Contact oContact = new Contact();
		Connection oConnection = initializeConnection();
        
        System.out.println("Agenda 3.0");
        System.out.println("");
        Integer intAction = 0;

        while(openSchedule()){
            while (intAction < 1 || intAction >5) {
                intAction = getAction();
            }
            switch (intAction) {
                case 1:
                    oContact = new Contact();
                    iContacts = searchContacts(oContact, oConnection);
                    printContacts(iContacts);
                    break;
                case 2:
                    System.out.println("Ingrese los criterios de busqueda (puede dejar campos vacios)");
                    oContact = null;
                    oContact = getContact(oContact);
                    iContacts = searchContacts(oContact, oConnection);
                    printContacts(iContacts);
                break;
                case 3:
                    System.out.println("Ingrese los datos segun se le indique");
                    oContact = null;
                    oContact = getContact(oContact);
                    saveContact(oContact, oConnection, 1);
                    break;
                case 4:
                    System.out.println("si no quiere cambiar la informacion, deje el campo vacio");
                    printContacts(iContacts);
                    oContact = getSelectedContact(iContacts);
                    oContact = getContact(oContact);
                    saveContact(oContact, oConnection, 0);
                    break;
                case 5:
                    System.out.println("selecciono Eliminar");
                    printContacts(iContacts);  
                    oContact = getSelectedContact(iContacts); 
                    oContact = getContact(oContact);
                    System.out.println("Está seguri de eliminar a " + oContact.getStrName() + "? s/n");
                    if(getYesNo()){
                        deleteContact(oContact, oConnection);   
                    }                     
                    break;        
                default:
                    break;
            }
            intAction = 0;
        }
        
    }
    
    private static Connection initializeConnection() {
        Connection oConnection = null;//lazy
		String strConnUrl = "jdbc:postgresql://127.0.0.1:5432/Agenda";
		String strConnUser = "dba";
        String strConnPass = "123456";

        try {
			Class.forName("org.postgresql.Driver");
			oConnection = DriverManager.getConnection(strConnUrl, strConnUser, strConnPass);
		} catch(Exception e) {
			System.out.println("ERROR: " + e);
		}
        return oConnection;
    }

    private static boolean openSchedule() {
        boolean answer = true;
        System.out.println("Desea utilizar la agenda? s/n");
        return true;
        //return getYesNo();
    }

    private static Contact getSelectedContact(ArrayList<Contact> iContacts) {
        System.out.println("Cual selecciona? (ingrese el numero de orden)");
        Scanner oScanner = new Scanner(System.in);
        Integer selection = oScanner.nextInt();
        while(selection < 0 || selection > iContacts.size()){
            selection = oScanner.nextInt();
        }
        Contact oContact = iContacts.get(selection);
        return oContact;
    }

    private static Contact getContact(Contact oContact) {
        Scanner oScanner = new Scanner(System.in);
        String strQuestion = "";
        if (oContact == null) {
            oContact = new Contact();
            //contacto nuevos
            System.out.println("Ingrese el dni");
            Integer id = oScanner.nextInt();
            oContact.setIntId(id);
        }
        strQuestion = "Ingrese el nombre";
        if(oContact.getStrName().equals("") == false){strQuestion += "(" + oContact.getStrName() + ")";}
        System.out.println(strQuestion);
        String name = oScanner.next();
        oContact.setStrName(name);

        strQuestion = "Ingrese el telefono";
        if(oContact.getStrName().equals("") == false){strQuestion += "(" + oContact.getStrPhone() + ")";}
        System.out.println(strQuestion);
        String phone = oScanner.next();
        oContact.setStrPhone(phone);

        strQuestion = "Ingrese el mail";
        if(oContact.getStrName().equals("") == false){strQuestion += "(" + oContact.getStrMail() + ")";}
        System.out.println(strQuestion);
        String mail = oScanner.next();
        oContact.setStrMail(mail);

        return oContact;
    }
    private static boolean getYesNo(){
        boolean answer = true;        
        Scanner oScanner = new Scanner(System.in);
        String selection = oScanner.nextLine();
        String yes = "S";
        String no = "N";
        selection = selection.toUpperCase();
        while((selection.equals(yes) == false) || (selection.equals(no) == false)){
            System.out.println("Indique una opción válida");
            selection = oScanner.nextLine();
        }

        answer =  selection.toUpperCase().equals("S") ? true : false;
        return answer;
    }
    private static void printContacts(ArrayList<Contact> iContacts) {
        if(iContacts.size() == 0){
            System.out.println("No se encontraron registros.");
        }
        else{
            System.out.println("Orden\tDNI\t\t\tNombre\t\t\tTelefono\t\t\tMail");
            for(int i = 0;i<iContacts.size();i++){
                System.out.println(i + "\t" + iContacts.get(i).toString());
            }
        }
    }

    public static Integer getAction() {
        Scanner oScanner = new Scanner(System.in);
        Integer intAction = 0;
        try {
            System.out.println("Indique la acción que desea realizar");
            System.out.println("");
            System.out.println("1 - Mostrar lista de contactos");
            System.out.println("2 - Buscar un contacto");
            System.out.println("3 - Nuevo contacto");
            System.out.println("4 - Actualizar un contacto");
            System.out.println("5 - Eliminar un contacto");
            System.out.println("");
            intAction = oScanner.nextInt();
            //oScanner.close();
        } catch (Exception e) {
            System.out.println("getAction error. " + e.getMessage());
            intAction = 0;
        }
        return intAction;
    }
    public static void saveContact(Contact oContact, Connection oConnection, Integer intAction){
        String strQuery = "";
        ResultSet result = null;
        Statement query = null;
        if (intAction == 1) {
            //guardar contacto
            strQuery = "insert into Contacts values(" + oContact.getIntId() + ", '" + oContact.getStrName() + "', '" + oContact.getStrPhone() + "', '" + oContact.getStrMail() + "')";
        } else {
            //actualizar contacto
            strQuery = "update Contacts set names = '" + oContact.getStrName() + "', phone = '" + oContact.getStrPhone() + "', mail = '" + oContact.getStrMail() + "' where id = " + oContact.getIntId();
        }
        try {
			query = oConnection.createStatement();
			query.execute(strQuery);
			query.close();
		} catch (Exception e) {
			System.out.println("ERROR: No fue posible guardar el contacto. " + e);
        }
        System.out.println("Se registro el contacto correctamente");
    }
    public static ArrayList<Contact> searchContacts(Contact oContact, Connection oConnection){
        ArrayList<Contact> iContacts = new ArrayList<Contact>();
        
        String strQuery = "SELECT id, names, phone, mail FROM Contacts where (id = " + oContact.getIntId() + " or " + oContact.getIntId() + " = 0 or " + oContact.getIntId() + " is null) " + 
                                                    "and (names like '" + oContact.getStrName() + "' or '" + oContact.getStrName() + "' = '' or '" + oContact.getStrName() + "' is null) " + 
                                                    "and (phone like '" + oContact.getStrPhone() + "' or '" + oContact.getStrPhone() + "' = '' or '" + oContact.getStrPhone() + "' is null) " + 
                                                    "and (mail like '" + oContact.getStrMail() + "' or '" + oContact.getStrMail() + "' = '' or '" + oContact.getStrMail() + "' is null)";
        ResultSet result = null;
        Statement query = null;
		
		try {
			query = oConnection.createStatement();
			result = query.executeQuery(strQuery);
			
			while(result.next()) {
                Integer id = result.getInt(1);
                String names = result.getString(2);
                String phone = result.getString(3);
                String mail = result.getString(4);
                iContacts.add(new Contact(id, names, phone, mail));
			}			
			query.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
        }
        return iContacts;
    }
    public static void deleteContact(Contact oContact, Connection oConnection){

    }
}