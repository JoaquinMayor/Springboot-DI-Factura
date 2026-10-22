# Práctica Spring Boot Inyección de Depedencia Ejemplo Factura

### El siguiente código cuenta con ejemplos y comentarios del trabajo práctico realizado donde se aplica el concepto de Inyección de Dependencia (DI), donde se va a realizar la creación de facturas.

El código cuenta con solo las clases Client, Invoice, Item, Product.
La factura y el cliente se crearán automáticamente mediante la configuración del `@Value` `@Autowired`, con los datos del archivo `values.properties` para facilitar y demoestrar el concepto.


<p align="center">
  <img src="https://media.istockphoto.com/id/1224630183/es/vector/libro-blanco-o-icono-de-verificaci%C3%B3n-financiera-aislado-con-sombra-larga-cheque-de-impresi%C3%B3n.jpg?s=612x612&w=0&k=20&c=rDxCKdWRGsW6cPy99mQhsRy9xcu864kGi9oVmfrsJ_w=" alt="Spring Relationship" width="500">
</p>

---

## Objetivo

Proyecto de práctica del curso de Spring Boot 3 que arma una **factura** completamente a partir de componentes de Spring: el cliente y la descripción se inyectan desde `values.properties`, los ítems se definen como beans en una clase de configuración y la factura se compone mediante `@Autowired`. Sirve para practicar inyección de dependencias, `@Bean`/`@Primary`, *scopes* (`@RequestScope`) y el ciclo de vida de los beans (`@PostConstruct` / `@PreDestroy`). No usa base de datos.

## Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot (starter parent) | 3.2.1 |
| Spring Web | gestionada por Spring Boot |
| Spring Boot Actuator | gestionada por Spring Boot |
| Spring Boot DevTools | gestionada por Spring Boot |
| Maven (Maven Wrapper incluido) | - |

## Estructura del proyecto

```
src/main/java/com/joaquin/curso/springboot/di/factura/springbootdifactura
├── SpringbootDiFacturaApplication.java  # Clase principal
├── ConfigApp.java                       # @Configuration: carga values.properties y define las listas de ítems como @Bean
├── controllers
│   └── InvoiceController.java           # GET /invoice/show
└── models
    ├── Client.java                      # @Component @RequestScope: nombre y apellido desde values.properties
    ├── Invoice.java                     # @Component @RequestScope: cliente + descripción + ítems, total calculado
    ├── Item.java                        # Producto + cantidad, con getImporte()
    └── Product.java                     # Nombre y precio
src/main/resources
├── application.properties               # Sin configuración adicional
└── values.properties                    # Datos del cliente y descripción de la factura
```

- **ConfigApp** declara dos beans `List<Item>`: `itemsInvoice` (marcado con `@Primary`: Cámara Sony x2 y Bicicleta Bianchi 26 x4) e `itemsInvoiceOffice` (Monitor, Notebook, Impresora y Escritorio). La factura recibe la lista `@Primary`.
- **Invoice** inyecta `Client`, la descripción (`@Value`) y la lista de ítems; `getTotal()` suma los importes con `stream().map(...).reduce(...)`, por lo que el total aparece en el JSON sin necesidad de un atributo.
- **InvoiceController** copia los datos del bean de factura en objetos nuevos antes de devolverlos, porque al ser componentes con `@RequestScope` (proxies) el JSON incluiría atributos que no interesan.

## Conceptos aplicados

- **Inyección con `@Autowired`** de componentes dentro de otros componentes (`Client` y `List<Item>` dentro de `Invoice`, `Invoice` dentro del controlador).
- **`@Value("${...}")`** para inyectar valores desde `values.properties`, cargado con `@PropertySources` / `@PropertySource` (encoding UTF-8).
- **`@Configuration` + `@Bean`** para registrar una lista como componente y **`@Primary`** para elegir cuál se inyecta cuando hay varios beans del mismo tipo.
- **Scopes**: `@RequestScope` en `Client` e `Invoice` (se crea una instancia por petición); `@SessionScope` queda comentado como alternativa.
- **`@JsonIgnoreProperties({"targetSource", "advisors"})`**: evita errores al serializar componentes cuyo alcance es menor que el de los componentes que los usan (proxies).
- **Ciclo de vida del bean**: `@PostConstruct` se ejecuta tras crear el componente y cargar sus datos (pasa el nombre del cliente a mayúsculas junto con el apellido); `@PreDestroy` se ejecuta antes de destruir el bean.
- **Propiedades calculadas en el JSON** mediante getters (`getTotal()`, `getImporte()`).
- **Streams** con `map` y `reduce` para calcular el total.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/invoice/show` | Retorna la factura: cliente (nombre y apellido), descripción, ítems (producto, cantidad e importe) y total. |

## Configuración

No requiere base de datos. Los datos de la factura se modifican en `src/main/resources/values.properties`:

```properties
value.client.name = Joaquín
value.client.lastname = Mayor
value.invoice.description = Mi factura de la compra
```

Los productos y cantidades de los ítems se definen en `ConfigApp.java`.

## Cómo ejecutarlo

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

Luego probar en Postman o el navegador:

```
GET http://localhost:8080/invoice/show
```

En la consola se verán los mensajes de `@PostConstruct` ("Creando el componente de la factura") y `@PreDestroy` en cada petición.
