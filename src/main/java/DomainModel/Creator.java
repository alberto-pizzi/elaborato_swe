package main.java.DomainModel;


abstract class Creator {


    // methods
    public Product factoryMethod(){
        Product product = createProduct();
        product.build();
        return product;
    }

    public abstract Product createProduct();

}