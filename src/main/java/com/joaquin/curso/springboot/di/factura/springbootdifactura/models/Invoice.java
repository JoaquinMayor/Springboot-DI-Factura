package com.joaquin.curso.springboot.di.factura.springbootdifactura.models;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
@RequestScope
@JsonIgnoreProperties({"targetSource", "advisors"}) //Para evitar errores porque este componente tiene un alcance menor a los componentes de los cuales está conformado, básicamente ignora los errores que se ponen entre ()
public class Invoice {
    @Autowired //Porque es un componente se puede hacer esto
    private Client client;
    @Value("${value.invoice.description}")
    private String description;
    @Autowired
    private List<Item> items;
    
    @PostConstruct //Se ejecuta cuando se cree el componente en el contexto, se ejecuta después de que se hayan cargado los datos de la factura
    public void init(){
        System.out.println("Creando el componente de la factura");
        client.setName(client.getName().toUpperCase().concat(" ").concat(client.getLastaname().toUpperCase()));
    }

    @PreDestroy //Son acciones que tomamos antes de que se cierre o elimine el bean o componente
    public void destroy(){
        System.out.println("Destruyendo el componente o bean factura!");
    }

    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getTotal(){ //Al crear el get total cuando se pasa en la api este crea un dato de este tipo que se pasa, no es necesario crear el atributo
        /*int total = 0;
        for (Item item : items) {
            total += item.getImporte();
        }*/

        int total = items.stream()
        .map(item->item.getImporte())
        .reduce(0,(sum, importe)-> sum + importe); //sum empieza en 0 y va sumando lo del importe
        return total;
    }
}
