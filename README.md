# Conversor de Monedas

Este es un conversor de monedas desarrollado en Java que permite a los usuarios convertir entre diferentes monedas utilizando datos actualizados de una API externa.

## Funcionalidades

* Obtiene las tasas de cambio actuales de una API.
* Permite al usuario seleccionar la moneda de origen y la moneda de destino.
* Permite al usuario ingresar la cantidad a convertir.
* Muestra el resultado de la conversión.
* Muestra un historial de las últimas 10 conversiones realizadas.
* Lista las monedas soportadas por la aplicación.

## Cómo usar

1.  Asegúrate de tener Java instalado en tu sistema.
2.  Clona este repositorio.
3.  Compila los archivos Java.
4.  Ejecuta la aplicación `ConversorApp.java`.
5.  Sigue las instrucciones en la consola para realizar conversiones.

## Estructura del proyecto

El proyecto está organizado en las siguientes carpetas:

* `src/Cliente`: Contiene la clase `ClienteTasaDeCambio.java` que se encarga de realizar la llamada a la API de tasas de cambio.
* `src/Modelo`: Contiene la clase `RespuestaTasaDeCambio.java` que representa la estructura de la respuesta de la API.
* `src/Servicio`: Contiene la clase `ServicioConversor.java` que implementa la lógica de negocio para la conversión de monedas.
* `src/UI`: Contiene la clase `InterfazConsola.java` que maneja la interacción con el usuario a través de la consola.
* `src/ConversorApp.java`: Contiene la clase `ConversorApp.java` que es el punto de entrada de la aplicación.

## Tecnologías utilizadas

* Java
* Gson (para el manejo de JSON)
* Java HttpClient (para realizar las peticiones a la API)

## API utilizada

La API de tasas de cambio utilizada en este proyecto es: [https://app.exchangerate-api.com/dashboard](https://app.exchangerate-api.com/dashboard)

## Próximas mejoras

* Implementar una interfaz gráfica de usuario.
* Agregar soporte para más monedas.
* Mejorar el manejo de errores y la validación de la entrada del usuario.

## Autor

Glenis Corona Lemus
