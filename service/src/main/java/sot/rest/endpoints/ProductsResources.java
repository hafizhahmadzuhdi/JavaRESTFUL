package sot.rest.endpoints;



import sot.rest.model.Product;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Path("markplaats/products")
@Singleton
public class ProductsResources {

    private List<Product> productList = new ArrayList<>();


    public ProductsResources(){
        //int id, String name, String category, double price, String city
        productList.add(new Product(1, "Nikon D5100", "Camera", 250, "Eindhoven", 101));
        productList.add(new Product(2, "Sony PS4", "Electronics", 171, "Eindhoven", 101));
        productList.add(new Product(3, "Canon Printer 1100", "Electronics", 75, "Utrecht", 101));
        productList.add(new Product(4, "Samsung Galaxy S9", "Smartphone", 750, "Rotterdam", 102));
        productList.add(new Product(5, "Phillips Speaker", "Electronics", 123, "Amsterdam", 102));

    }

    @GET//GET at http://localhost:9090/markplaats/products/hello
    @Path("hello")
    @Produces({MediaType.TEXT_PLAIN})
    public String sayHello(){
        return "Your product service is work";
    }

    @GET //GET at http://localhost:9090/markplaats/products/first
    @Path("first")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getFirstProduct(){
        if(productList.size() > 0) {
            return Response.ok(productList.get(0)).build();
        } else {
            return Response.serverError().entity("There are no products in the list").build();
        }
    }

    @GET//GET at http://localhost:9090/markplaats/products/all
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllProducts(){

        GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(productList){

        };
        return Response.ok(entity).build();
    }

    @GET //GET at http://localhost/markplaats/products?id=X
    @Produces({MediaType.APPLICATION_JSON})
    public Response getProductQuery(@QueryParam("id") int productID){
        for (Product product: productList){
            if(product.getId() == productID){
                return Response.ok(product).build();
            }
        }
        return Response.serverError().entity("Cannot find product with id : " + productID).build();
    }

    @GET
    @Path("category/{category}") //GET at http://localhost/markplaats/products/category/xxx
    @Produces({MediaType.APPLICATION_JSON})
    public Response getProductBasedOnCategory(@PathParam("category") String category){
        List<Product> tempArrayCategory = new ArrayList<>();
        Response myResp = Response.serverError().entity("Cannot find product with category : " + category).build();


        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getCategory().equalsIgnoreCase(category)) {
                tempArrayCategory.add(productList.get(i));
            }
        }
        if(tempArrayCategory.size()==0){
            return myResp;
        } else {
            GenericEntity<List<Product>> entityCategory = new GenericEntity<List<Product>>(tempArrayCategory) {
            };
            myResp = Response.ok(entityCategory).build();
            return myResp;
        }


        /*List<Product> tempArray1 = new ArrayList<>();
        for (Product product: productList){
            if(product.getCategory().equalsIgnoreCase(category)) {
                tempArray1.add(product);
                GenericEntity<List<Product>> entityCategory = new GenericEntity<List<Product>>(tempArray1) {
                };
                return Response.ok(entityCategory).build();
            }
        }
        return Response.serverError().entity("Cannot find product with category : " + category).build();*/
    }

    @GET
    @Path("city/{city}")//GET at http://localhost/markplaats/products/city/eindhoven
    @Produces({MediaType.APPLICATION_JSON})
    public Response getProductBasedOnCity(@PathParam("city") String city) {
        List<Product> tempArrayCity = new ArrayList<>();
        Response myResp = Response.serverError().entity("Cannot find product with city : " + city).build();


        for (int i = 0; i < productList.size(); i++) {
                    if (productList.get(i).getCity().equalsIgnoreCase(city)) {
                        tempArrayCity.add(productList.get(i));
                    }
                }
        if(tempArrayCity.size()==0){
            return myResp;
        } else {
            GenericEntity<List<Product>> entityCity = new GenericEntity<List<Product>>(tempArrayCity) {
            };
            myResp = Response.ok(entityCity).build();
            return myResp;
        }
    }


   /* @GET //GET at http://localhost/markplaats/products/X
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Product getProductPath(@PathParam("id") int productID){
        for (Product product: productList){
            if(product.getId() == productID){
                return product;
            }
        }
        throw new RuntimeException("There is no such product with id " + productID);
    }*/

    @DELETE //DELETE at http://localhost/markplaats/products/X
    @Path("{id}")
    public Response deleteProduct(@PathParam("id") int productID){
        Product oldProduct = this.getProduct(productID);
        if(oldProduct != null){
            productList.remove(oldProduct);
            return Response.noContent().build();
        } else {
            return Response.serverError().entity("Product with id : " + productID + " doesnt exist").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addProductForm(@FormParam("id") int id, @FormParam("name") String name, @FormParam("category") String category, @FormParam("price") double price, @FormParam("city") String city, @FormParam("seller_id") int seller_id){
        for(Product myProduct : productList) {
            if(myProduct.getId() == id){
                return Response.serverError().entity("Product with id : " + id + " already exist").build();
            }
        }
        Product product = new Product(id, name, category, price, city, seller_id);
        productList.add(product);
        return Response.noContent().build();
    }


    /*@POST //POST at http://localhost/markplaats/products/
    @Consumes(MediaType.APPLICATION_JSON)
    public void createProduct(Product product) {
        Product oldProduct = this.getProduct(product.getId());
        if (oldProduct != null) {
            throw new RuntimeException("Error create: product with id = " + product.getId() + " already existts");

        } else {
            productList.add(product);
        }
    }*/

    @PUT //PUT at http://localhost:9090/markplaats/products/
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateProduct(Product product) {
        Product existingProduct = this.getProduct(product.getId());
        if(existingProduct != null){
            existingProduct.setName(product.getName());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setCity(product.getCity());
            return Response.noContent().build();
        } else {
            return Response.serverError().entity("Product with id : " + product.getId() + " doesnt exist").build();
        }
    }

    @PUT
    @Path("buyer")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateBuyer(@FormParam("id") int id, @FormParam("buyer_id") int buyer_id){
        Product p = getProduct(id);
        if(p != null){
            p.setBuyer_id(buyer_id);
            return Response.noContent().build();
        } else {
            return Response.serverError().entity("Product with id : " + id + " doesnt exist").build();
        }
    }

    private Product getProduct(int product_id){
        Product p = null;
        for(Product myProduct : productList){
            if(myProduct.getId() == product_id){
                p = myProduct;
            }
        }
        return p;
    }

    private boolean exists(int id){
        Product p = null;
        Boolean b = false;
        for(Product myProduct : productList){
            if(myProduct.getId() == id){
                b = true;
            } else {
                b = false;
            }
        }
        return b;
    }


}
