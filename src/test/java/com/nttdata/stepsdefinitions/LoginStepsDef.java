package com.nttdata.stepsdefinitions;

import com.epam.healenium.SelfHealingDriver;
import com.nttdata.steps.InventorySteps;
import com.nttdata.steps.LoginSteps;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginStepsDef {

    private SelfHealingDriver driver;
    private WebDriver delegate;
    private Scenario scenario;

    private InventorySteps inventorySteps(SelfHealingDriver driver){
        return new InventorySteps(driver);
    }

    @Before(order = 0)
    public void setUp(){
        //setUp
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\calcantv\\Documents\\QA Innovation Lab\\Diapositivas\\Pruebas Automatizadas\\web-automation\\qa-lab-selenium\\drivers\\chromedriver.exe");
        //crear el driver
        delegate = new ChromeDriver();

        driver= SelfHealingDriver.create(delegate);
        //max
        driver.manage().window().maximize();
    }

    @Before(order = 1)
    public void setScenario(Scenario scenario){
        this.scenario = scenario;
    }

    @After
    public void quitDriver(){
        if (driver != null) {
            driver.quit();
        }
    }

    @Dado("que me encuentro en la página de login de Saucedemo")
    public void que_me_encuentro_en_la_pagina_de_login_de_sacedemo() {
        driver.get("https://www.saucedemo.com/");
        screenShot();
    }
    @Cuando("inicio sesión con las credenciales usuario: {string} y contraseña: {string}")
    public void inicio_sesion_con_las_credenciales_usuario_y_contrasena(String user, String password) throws InterruptedException {
        LoginSteps loginSteps = new LoginSteps(driver);
        Thread.sleep(20000);
        loginSteps.typeUser(user);
        loginSteps.typePassword(password);
        loginSteps.login();
        screenShot();
    }
    @Entonces("valido que debería aparecer el título de {string}")
    public void valido_que_debería_aparecer_el_titulo_de(String expectedTitle) {
        String title =  inventorySteps(driver).getTitle();
        //prueba: validamos el título del producto
        Assertions.assertEquals(expectedTitle, title);
    }
    @Y("también valido que al menos exista un item")
    public void tambien_valido_que_al_menos_exista_un_item() {
        int itemsListSize = inventorySteps(driver).getItemSize();
        //prueba: validar que al menos exista un item
        screenShot();
        Assertions.assertTrue(itemsListSize > 0, "El tamaño de la lista es: " + itemsListSize);
    }

    public void screenShot(){
        byte[] evidencia = ((TakesScreenshot) driver.getDelegate()).getScreenshotAs(OutputType.BYTES);
        this.scenario.attach(evidencia, "image/png", "evidencias");
    }

}
