package sot.rest.endpoints;

import sot.rest.model.Account;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("markplaats/accounts")
@Singleton
public class AccountsResources {

    private List<Account> accountList = new ArrayList<>();

    public AccountsResources(){
        accountList.add(new Account(101, "hafizh@Gmail.com", "Hafizh Zuhdi", "Eindhoven"));
        accountList.add(new Account(102, "gareth@Gmail.com", "Gareth Forshaw", "Utrecht"));
        accountList.add(new Account(103, "esther@Gmail.com", "Esther Nhi", "Amsterdam"));
        accountList.add(new Account(104, "mesfin@Gmail.com", "Mesfin", "Brussels"));
        accountList.add(new Account(105, "deniz@Gmail.com", "Deniz", "Eindhoven"));
    }

    private Account getAccount(int account_id){
        Account a = null;
        for(Account myAccount : accountList){
            if(myAccount.getUser_id() == account_id){
                a = myAccount;
            }
        }
        return a;
    }

    @GET //GET at http://localhost:9090/markplaats/accounts/hello
    @Path("hello")
    @Produces({MediaType.TEXT_PLAIN})
    public String sayHello(){
        return "Your account service is work";
    }

    @GET //GET at http://localhost:9090/markplaats/accounts/first
    @Path("first")
    @Produces({MediaType.APPLICATION_JSON})
    public Account getFirstAccount(){
        if(accountList.size() > 0) {
            return accountList.get(0);
        } else {
            throw new RuntimeException("There are no users in the list");
        }
    }

    @GET //GET at http://localhost:9090/markplaats/accounts/all
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllAccounts(){
        GenericEntity<List<Account>> entity = new GenericEntity<List<Account>>(accountList){

        };
        return Response.ok(entity).build();
    }

    @GET //GET at http://localhost:9090/markplaats/accounts?user_id=X
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAccountQuery(@QueryParam("user_id") int userID){
        for (Account account: accountList){
            if(account.getUser_id()== userID){
                return Response.ok(account).build();
            }
        }

        return Response.serverError().entity("Cannot find account with id " + userID).build();
    }

    /*@GET //GET at http://localhost:9090/markplaats/accounts/X
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Account getAccountPath(@PathParam("id") int userID){
        for (Account account: accountList){
            if(account.getUser_id() == userID){
                return account;
            }
        }
        throw new RuntimeException("There is no such account with id " + userID);
    }*/

    @DELETE //DELETE at http://localhost:9090/markplaats/accounts/X
    @Path("{id}")
    public Response deleteAccount(@PathParam("id") int userID){
        Account account = this.getAccount(userID);
        if(account != null){
            accountList.remove(account);
            return Response.noContent().build();
        } else {
            return Response.serverError().entity("Account with id : " + userID + " doesnt exist").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addAccountForm(@FormParam("user_id") int user_id, @FormParam("user_mail") String user_mail, @FormParam("name") String name, @FormParam("password") String password){
        for(Account myAccount : accountList) {
            if(myAccount.getUser_id() == user_id){
                return Response.serverError().entity("Account with id : " + user_id + " already exists").build();
            }
        }
        Account account = new Account(user_id, user_mail, name, password);
        accountList.add(account);
        return Response.noContent().build();
    }

    /*@POST //POST at http://localhost:9090/markplaats/accounts/
    @Consumes(MediaType.APPLICATION_JSON)
    public void createAccount(Account account) {
        Account oldAccount = this.getAccount(account.getUser_id());
        if (oldAccount != null) {
            throw new RuntimeException("Error create: account with id = " + account.getUser_id() + " already existts");

        } else {
            accountList.add(account);

        }
    }*/

    @PUT //PUT at http://localhost:9090/markplaats/accounts/
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateAccount(Account account) {
        Account existingAccount = this.getAccount(account.getUser_id());
        if(existingAccount != null){
            existingAccount.setUser_mail(account.getUser_mail());
            existingAccount.setName(account.getName());
            existingAccount.setPassword(account.getPassword());
            return Response.noContent().build();
        } else {
            return Response.serverError().entity("Account with id : " + account.getUser_id() + " doesnt exist").build();
        }
    }


}
