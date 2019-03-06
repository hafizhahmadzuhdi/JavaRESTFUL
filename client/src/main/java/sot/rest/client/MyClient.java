/*
This class includes methods as following :
1. add Account
2. add Product
3. get All Accounts (for the admin)
4. get All Products
5. delete Product
6. delete Account
7. update Product

TO DO :
8. update Account
9. get Product based on Category //query
10. get Product based on City //query
11. get Product based on ID //query
12. get Account based on ID //path

 */

package sot.rest.client;

import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.glassfish.jersey.client.ClientConfig;
import sun.awt.geom.AreaOp;

import javax.print.attribute.standard.Media;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;

public class MyClient {

    /*
    Username : hafizh@Gmail.com
    Password : Eindhoven
     */

    public static String productsURL = "http://localhost:3000/markplaats/rest/markplaats/products";
    //int id, String name, String category, double price, String city, int seller_id
    public static String accountsURL = "http://localhost:3000/markplaats/rest/markplaats/accounts";

    /*public static String productsURL = "http://localhost:3000/markplaats/products";
    //int id, String name, String category, double price, String city, int seller_id
    public static String accountsURL = "http://localhost:3000/markplaats/accounts";*/

    //Client configuration
    static ClientConfig config = new ClientConfig();
    static Client client = ClientBuilder.newClient(config);

    //Product URI
    static URI baseURIProduct = UriBuilder.fromUri(productsURL).build();
    static WebTarget serviceTargetProduct = client.target(baseURIProduct);
    //Account URI
    static URI baseURIAccount = UriBuilder.fromUri(accountsURL).build();
    static WebTarget serviceTargetAccount = client.target(baseURIAccount);

    //Temp account and product for login
    static Account adm = null;
    static Account tempAcc = null;
    static ArrayList<Product> tempPro = new ArrayList<>();
    static ArrayList<Account> tempAccount = new ArrayList<>();



    public static void main (String args[]){
        Scanner scanner = new Scanner(System.in);

        while (true){
        System.out.println("Welcome to Markplaats by Hafizh Ahmad Zuhdi 3578399");
        System.out.println("---------------------------------------------------");
        System.out.println("1. Home Page");
        System.out.println("2. Register");
        System.out.println("3. Admin Page");
        System.out.println("4. Exit");
        System.out.println("Please select the menu");
            int x = scanner.nextInt();
            if(x==1){
                homePage();
            } else if(x==2){
                registerPage();
            } else if(x==3){
                adminPage();
            } else if(x==4){
                break;
            } else {
                System.out.println("Sorry you put the wrong input");
            }
        }
    }

    public static void homePage(){

        Scanner scanner = new Scanner(System.in);
        Account existAccount = loginPage();
        if(existAccount != null){
            while(true){
                System.out.println("Markplaats HOME PAGE");
                System.out.println("---------------------------------------------------");
                System.out.println("Hello USER with ID " + existAccount.getUser_id());
                System.out.println("1. Available products");
                System.out.println("2. Search product based on category");
                System.out.println("3. Search product based on city");
                System.out.println("4. Search product based on ID");
                System.out.println("5. Sell a product");
                System.out.println("6. Buy a product");
                System.out.println("7. Delete your product");
                System.out.println("8. Update your product");
                System.out.println("9. Delete your account");
                System.out.println("10. Update your account");
                System.out.println("11. Exit");
                System.out.println("Please select the menu");
                int x = scanner.nextInt();
                if(x==1){
                    allProducts();
                }
                else if(x==2){
                    searchCategory();
                } else if(x==3){
                    searchCity();
                } else if(x==4){
                    searchID();
                } else if(x==5){
                    sellProduct();
                } else if(x==6) {
                    buyProduct();
                } else if(x==7){
                    deleteProduct();
                } else if(x==8){
                    updateProduct();
                } else if(x==9){
                    deleteAccount();
                    break;
                } else if(x==10) {
                    updateAccount();
                } else if(x==11){
                    logoutUsers();
                    break;
                } else {
                    System.out.println("Sorry you put the wrong input");
                }
            }
        } else {
            System.out.println("Sorry you put the wrong password/mail");
        }

    }


    public static void allProducts(){

        Invocation.Builder requestBuilder = serviceTargetProduct.path("all").request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();
        GenericType<ArrayList<Product>> genericType = new GenericType<ArrayList<Product>>() {};
        ArrayList<Product> entity = response.readEntity(genericType);

        System.out.println("----------------------------------------------------------------------------------------");
        System.out.printf("%10s %30s %20s %10s %5s %5s", "ID", "NAME", "CATEGORY", "CITY", "PRICE", "SELLER");
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------");
        for (Product myProd : entity) {
            tempPro.add(myProd);
            if(myProd.getBuyer_id()==0){
                System.out.format("%10s %30s %20s %10s %5s %5s",
                        myProd.getId(), myProd.getName(), myProd.getCategory(), myProd.getCity(), myProd.getPrice(), myProd.getSeller_id());
                System.out.println();
            }
        }
        System.out.println("----------------------------------------------------------------------------------------");

    }

    public static void updateAccount(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Change your profile below : ");
        System.out.println("Name : ");
        String name = sc.nextLine();
        System.out.println("Email : ");
        String mail = sc.nextLine();
        System.out.println("Password : ");
        String password = sc.nextLine();
        String id = Integer.toString(tempAcc.getUser_id());
        Account currentAccount = new Account(tempAcc.getUser_id(), mail, name, password);
        Entity <Account> entity = Entity.entity(currentAccount, MediaType.APPLICATION_JSON);
        Response response = serviceTargetAccount.request().accept(MediaType.TEXT_PLAIN).put(entity);
        if(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()){
            System.out.println("User Successfully updated");
        } else {
            System.out.println("Error : " + response.readEntity(String.class));
        }
    }

    public static void deleteAccount() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Are you sure want to delete your account ? (Y/N)");
        String x = sc.nextLine();
        if(x.equalsIgnoreCase("Y")){
            WebTarget resourceTarget = serviceTargetAccount.path(Integer.toString(tempAcc.getUser_id()));
            Invocation.Builder requestBuilder = resourceTarget.request().accept(MediaType.APPLICATION_JSON);
            Response response = requestBuilder.delete();
            if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
                System.out.println("Account deleted successfully");
            } else {
                System.out.println(response.readEntity(String.class));
            }
        }

    }

    public static void searchCategory(){

        Scanner scanner = new Scanner(System.in);

        System.out.println("Input category name : ");
        String cat = scanner.nextLine();

        Invocation.Builder requestBuilder = serviceTargetProduct.path("category/"+cat).request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();

        if(response.getStatus() == Response.Status.OK.getStatusCode()){
            GenericType<ArrayList<Product>> genericType = new GenericType<ArrayList<Product>>() {};
            ArrayList<Product> entity = response.readEntity(genericType);
            System.out.println("----------------------------------------------------------------------------------------");
            System.out.printf("%10s %30s %20s %10s %5s %5s", "ID", "NAME", "CATEGORY", "CITY", "PRICE", "SELLER");
            System.out.println();
            System.out.println("----------------------------------------------------------------------------------------");
            for(Product myProd : entity){
                System.out.format("%10s %30s %20s %10s %5s %5s",
                        myProd.getId(), myProd.getName(), myProd.getCategory(), myProd.getCity(), myProd.getPrice(), myProd.getSeller_id());
                System.out.println();
            }
            System.out.println("----------------------------------------------------------------------------------------");
        } else {
            System.out.println();
            System.out.println("Cannot find product with category" + cat);
            System.out.println();
        }

    }

    public static void searchCity(){

        Scanner scanner = new Scanner(System.in);

        System.out.println("Input city name : ");
        String city = scanner.nextLine();

        Invocation.Builder requestBuilder = serviceTargetProduct.path("city/"+city).request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();

        if(response.getStatus() == Response.Status.OK.getStatusCode()){
            GenericType<ArrayList<Product>> genericType = new GenericType<ArrayList<Product>>() {};
            ArrayList<Product> entity = response.readEntity(genericType);
            System.out.println("----------------------------------------------------------------------------------------");
            System.out.printf("%10s %30s %20s %10s %5s %5s", "ID", "NAME", "CATEGORY", "CITY", "PRICE", "SELLER");
            System.out.println();
            System.out.println("----------------------------------------------------------------------------------------");
            for(Product myProd : entity){
                System.out.format("%10s %30s %20s %10s %5s %5s",
                        myProd.getId(), myProd.getName(), myProd.getCategory(), myProd.getCity(), myProd.getPrice(), myProd.getSeller_id());
                System.out.println();
            }
            System.out.println("----------------------------------------------------------------------------------------");
        } else {
            System.out.println();
            System.out.println("Cannot find product with city" + city);
            System.out.println();
        }

    }

    public static void sellProduct(){
        allProducts();
        Scanner sc = new Scanner(System.in);
        System.out.println("Please input the product specification below : ");
        Form prodForm = new Form();

        ArrayList<Product> myProducts = getProducts();

        prodForm.param("id", Integer.toString(myProducts.get(myProducts.size()-1).getId()+1));

        System.out.println("Name : ");
        String name = sc.nextLine();
        prodForm.param("name", name);

        System.out.println("Category : ");
        String prodCat = sc.nextLine();
        prodForm.param("category", prodCat);

        System.out.println("Price : ");
        String price = sc.nextLine();
        prodForm.param("price", price);

        System.out.println("City : ");
        String city = sc.nextLine();
        prodForm.param("city", city);

        prodForm.param("seller_id", Integer.toString(tempAcc.getUser_id()));

        Entity<Form> entity = Entity.entity(prodForm, MediaType.APPLICATION_FORM_URLENCODED);

        Response response = serviceTargetProduct.request().accept(MediaType.TEXT_PLAIN).post(entity);
        if(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()){
            System.out.println("Product created!");
        } else {
            System.out.println("Sorry there was an error");
        }
    }

    public static void buyProduct(){
        allProducts();
        Scanner sc = new Scanner(System.in);
        ArrayList<Product> myProducts = getProducts();
        System.out.println("Select product id you want to buy");
        int x = sc.nextInt();
        for(Product p : myProducts){
            if(p.getId() == x){
                if(p.getSeller_id() != tempAcc.getUser_id()){
                    Form form = new Form();
                    form.param("id", Integer.toString(x));
                    form.param("buyer_id", Integer.toString(tempAcc.getUser_id()));
                    Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
                    Invocation.Builder requestBuilder = serviceTargetProduct.path("buyer").request().accept(MediaType.TEXT_PLAIN);
                    Response response = requestBuilder.put(entity);
                    if(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()){
                        System.out.println("Product bought successfully!");
                    } else {
                        System.out.println("Error : " + response.readEntity(String.class));
                    }
                }  else {
                    System.out.println("Sorry you cant do that");
                }
            }
        }
    }

    public static void deleteProduct(){

        allProducts();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input your id wish to delete");
        String prodid = scanner.nextLine();

        ArrayList<Product> pro = getProducts();

        Response response2 = null;
        WebTarget resourceTarget = serviceTargetProduct.path(prodid);
        Invocation.Builder requestBuilder2 = resourceTarget.request().accept(MediaType.APPLICATION_JSON);
        for(Product ent : pro){
            if(tempAcc.getUser_id() == ent.getSeller_id()){
                if(ent.getId() == Integer.parseInt(prodid)){
                    response2 = requestBuilder2.delete();
                    String entityResp = response2.readEntity(String.class);
                    if(response2.getStatus() == Response.Status.NO_CONTENT.getStatusCode()){
                        System.out.println("Product deleted successfully");
                    } else {
                        System.out.println("There is no such product : " +entityResp);
                    }
                }
            }
        }
    }

    public static Account loginPage(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("You need to be authenticated for using Markplaats");
        System.out.print("User mail : ");
        String mail = scanner.next();
        System.out.print("Password : ");
        String password = scanner.next();

        Invocation.Builder requestBuilder = serviceTargetAccount.path("all").request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();
        GenericType<ArrayList<Account>> genericType = new GenericType<ArrayList<Account>>() {};
        ArrayList<Account> entity = response.readEntity(genericType);

        //Store account
        Account existAccount = null;
        for (Account acc : entity)  {
            if(acc.getUser_mail().equalsIgnoreCase(mail) && acc.getPassword().equals(password)){
                existAccount = acc;
            }
        }
        tempAcc = existAccount;
        return existAccount;
    }

    public static void updateProduct(){
        Scanner sc = new Scanner(System.in);
        allProducts();
        Response response = null;
        System.out.println("Select the id product you want to edit");
        int myId = Integer.parseInt(sc.nextLine());
        ArrayList<Product> x = getProducts();
        for(Product p : x){
            if(p.getId() == myId){
                if(p.getSeller_id() == tempAcc.getUser_id()){
                    System.out.println("New Name : ");
                    String name = sc.nextLine();
                    System.out.println("New Category : ");
                    String category = sc.nextLine();
                    System.out.println("New Price : ");
                    double price = Double.parseDouble(sc.nextLine());
                    System.out.println("New City : ");
                    String city = sc.nextLine();

                    Product currentProduct = new Product(myId, name, category, price, city, tempAcc.getUser_id());
                    Entity <Product> entity = Entity.entity(currentProduct, MediaType.APPLICATION_JSON);
                    response = serviceTargetProduct.request().accept(MediaType.TEXT_PLAIN).put(entity);
                    if(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()){
                        System.out.println("Product Successfully updated");
                    } else {
                        System.out.println("Error : " + response.readEntity(String.class));
                    }

                } else {
                    System.out.println("You are not allowed");
                }
            }
        }

    }

    public static ArrayList<Product> getProducts(){
        Invocation.Builder requestBuilder = serviceTargetProduct.path("all").request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();
        GenericType<ArrayList<Product>> genericType = new GenericType<ArrayList<Product>>() {};
        ArrayList<Product> entity = response.readEntity(genericType);
        return entity;
    }

    public static ArrayList<Account> getAccounts(){
        Invocation.Builder requestBuilder = serviceTargetAccount.path("all").request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();
        GenericType<ArrayList<Account>> genericType = new GenericType<ArrayList<Account>>() {};
        ArrayList<Account> entity = response.readEntity(genericType);
        return entity;
    }

    public static void logoutUsers(){
        tempAcc = null;
        adm = null;
    }

    public static void searchID(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Input ID : ");
        int id = scanner.nextInt();

        Invocation.Builder requestBuilder = serviceTargetProduct.queryParam("id", Integer.toString(id)).request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();

        if(response.getStatus() == Response.Status.OK.getStatusCode()){
            Product p = response.readEntity(Product.class);
            System.out.println("----------------------------------------------------------------------------------------");
            System.out.printf("%10s %30s %20s %10s %5s %5s", "ID", "NAME", "CATEGORY", "CITY", "PRICE", "SELLER");
            System.out.println();
            System.out.println("----------------------------------------------------------------------------------------");
                System.out.format("%10s %30s %20s %10s %5s %5s",
                        p.getId(), p.getName(), p.getCategory(), p.getCity(), p.getPrice(), p.getSeller_id());
                System.out.println();

            System.out.println("----------------------------------------------------------------------------------------");
        } else {
            System.out.println();
            System.out.println("Cannot find product with id " + id);
            System.out.println();
        }
    }

    public static Account adminLogin(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Admin Authentication : (hafizh@Gmail.com & Eindhoven)");
        System.out.print("User mail : ");
        String mail = scanner.next();
        System.out.print("Password : ");
        String password = scanner.next();

        Invocation.Builder requestBuilder = serviceTargetAccount.path("all").request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();
        GenericType<ArrayList<Account>> genericType = new GenericType<ArrayList<Account>>() {};
        ArrayList<Account> entity = response.readEntity(genericType);

        Account akun = null;
        for (Account acc : entity)  {
            if(acc.getUser_mail().equalsIgnoreCase("hafizh@gmail.com") && acc.getPassword().equals("Eindhoven")){
                akun = acc;
            }
        }
        adm = akun;
        return akun;
    }

    public static void adminPage(){

        Scanner scanner = new Scanner(System.in);
        Account adminAccount = adminLogin();
        if(adminAccount != null){
            while(true){
                System.out.println("Markplaats ADMIN PAGE");
                System.out.println("---------------------------------------------------");
                System.out.println("Hello ADMIN with ID " + adminAccount.getUser_id());
                System.out.println("1. All Account");
                System.out.println("2. Search Account based on id");
                System.out.println("3. Exit");
                System.out.println("Please select the menu");
                int x = scanner.nextInt();
                if(x==1){
                    allAccounts();
                }
                else if(x==2){
                    searchAccountID();
                } else if(x==3){
                    logoutUsers();
                    break;
                }  else {
                    System.out.println("Sorry you put the wrong input");
                }
            }
        } else {
            System.out.println("Sorry you put the wrong password/mail");
        }

    }

    public static void allAccounts(){
        Invocation.Builder requestBuilder = serviceTargetAccount.path("all").request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();
        GenericType<ArrayList<Account>> genericType = new GenericType<ArrayList<Account>>() {};
        ArrayList<Account> entity = response.readEntity(genericType);

        System.out.println("----------------------------------------------------------------------------------------");
        System.out.printf("%10s %30s %20s %20s", "ID", "MAIL", "NAME", "PASSWORD");
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------");
        for (Account myAcc : entity) {
            tempAccount.add(myAcc);
                System.out.format("%10s %30s %20s %20s",
                        myAcc.getUser_id(), myAcc.getUser_mail(), myAcc.getName(), myAcc.getPassword());
                System.out.println();

        }
        System.out.println("----------------------------------------------------------------------------------------");
    }

    public static void searchAccountID(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Input user id : ");
        int id = scanner.nextInt();

        Invocation.Builder requestBuilder = serviceTargetAccount.queryParam("user_id", Integer.toString(id)).request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();

        if(response.getStatus() == Response.Status.OK.getStatusCode()){
            Account myAcc = response.readEntity(Account.class);
            System.out.println("----------------------------------------------------------------------------------------");
            System.out.printf("%10s %30s %20s %20s", "ID", "MAIL", "NAME", "PASSWORD");
            System.out.println();
            System.out.println("----------------------------------------------------------------------------------------");
            System.out.format("%10s %30s %20s %20s",
                    myAcc.getUser_id(), myAcc.getUser_mail(), myAcc.getName(), myAcc.getPassword());
            System.out.println();
            System.out.println("----------------------------------------------------------------------------------------");
        } else {
            System.out.println();
            System.out.println("Cannot find account with id " + id + " " +response.readEntity(String.class));
            System.out.println();
        }
    }

    public static void registerPage(){
        ArrayList<Account> acc = getAccounts();

        Scanner sc = new Scanner(System.in);
        System.out.println("Please input your details below : ");
        Form accForm = new Form();

        accForm.param("user_id", Integer.toString(acc.get(acc.size()-1).getUser_id()+1));

        System.out.println("User Mail : ");
        String mail = sc.nextLine();
        accForm.param("user_mail", mail);

        System.out.println("Name : ");
        String name = sc.nextLine();
        accForm.param("name", name);

        System.out.println("Password : ");
        String password = sc.nextLine();
        accForm.param("password", password);

        Entity<Form> entity = Entity.entity(accForm, MediaType.APPLICATION_FORM_URLENCODED);

        Response response = serviceTargetAccount.request().accept(MediaType.TEXT_PLAIN).post(entity);
        if(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()){
            System.out.println("User successfully created!");
        } else {
            System.out.println("Sorry there was an error : " + response.readEntity(String.class));
        }
    }



}
