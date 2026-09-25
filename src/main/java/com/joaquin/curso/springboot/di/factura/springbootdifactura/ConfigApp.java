package com.joaquin.curso.springboot.di.factura.springbootdifactura;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.joaquin.curso.springboot.di.factura.springbootdifactura.models.Item;
import com.joaquin.curso.springboot.di.factura.springbootdifactura.models.Product;

@Configuration
@PropertySources({
    @PropertySource(value="classpath:values.properties", encoding = "UTF-8")
})
public class ConfigApp {
    @Primary
    @Bean //Para que la lista se cuente como un componente
    List<Item> itemsInvoice(){
        Product p1 = new Product("Cámara Sony", 800);
        Product p2 = new Product("Bicicleta Bianchi 26", 1200);
        List<Item> items = Arrays.asList(new Item(p1,2), new Item(p2,4));
        
        return items;
    }

    
    @Bean //Para que la lista se cuente como un componente
    List<Item> itemsInvoiceOffice(){
        Product p1 = new Product("Monitor Asus 24", 700);
        Product p2 = new Product("Notebook Razer", 2400);
        Product p3 = new Product("Impresora HP", 800);
        Product p4 = new Product("Escritorio Oficina", 900);
        List<Item> items = Arrays.asList(new Item(p1,4), new Item(p2,6), new Item(p3,1), new Item(p4,4));
        
        return items;
    }
}
