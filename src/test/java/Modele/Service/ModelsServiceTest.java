package Modele.Service;

import Modele.Entity.Category;
import Modele.Entity.Model;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
@Transactional
public class ModelsServiceTest {


    private String randomString="";

    @Autowired
    private IModelsManager modelsManager;

    @Before
    public void setUp() throws Exception {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        randomString= sb.toString();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void addCategory() throws Exception {
        Category puzzle=new Category();
        puzzle.setNazwaKat(randomString);
        puzzle.setNumer(1234);
        puzzle.setOpis("Pieknosci");
        modelsManager.addCategory(puzzle);
        assertEquals(puzzle.getNazwaKat(),
                modelsManager
                        .findByNazwa(puzzle.getNazwaKat())
                        .getNazwaKat());

    }

    @Test
    public void getAllCategories() throws Exception {

        Category puzzle=new Category();
        puzzle.setNazwaKat(randomString);
        puzzle.setNumer(1234);
        puzzle.setOpis("Wspanialosci");
        modelsManager.addCategory(puzzle);
        List<Category> categories=modelsManager.getAllCategories();

        assertTrue(categories.size()>=1);
    }

    @Test
    public void findByIdCategory() throws Exception {
        Category puzzle=new Category();
        puzzle.setNazwaKat(randomString);
        puzzle.setNumer(1234);
        puzzle.setOpis("Opisik");
        Long id=modelsManager.addCategory(puzzle);
        assertEquals(randomString,modelsManager.findByIdCategory(id).getNazwaKat());

    }

    @Test
    public void deleteCategory() throws Exception {

        Category puzzle=new Category();
        puzzle.setNazwaKat(randomString);
        puzzle.setNumer(1234);
        puzzle.setOpis("Dobroci");
        Long idC=modelsManager.addCategory(puzzle);

        Model m1=new Model();
        m1.setNazwaMod("Wielgasny");
        m1l.setProducent(randomString);
        m1.setProgram("Najnowszy");
        m1.setCategory(puzzle);
        Long idM= modelsManager.addModel(m1);
        //usuwanie kategorii
        modelsManager.deleteCategory(puzzle);
        boolean ifExists=false;
        try {
            //oczekujemu ze znajdziemy rekord i pole nazwa kat bedzie miala taka sama wartosc jaka wprowadzilismy
            assertTrue(modelsManager.findByIdCategory(idC).getNazwaKat()==randomString);
        }catch (NullPointerException e)
        {
            ifExists=true;
        }
        assertTrue(ifExists);
        //sprawdzamy czy po usunieciu kategorii model ktory byl do niej przypisany  nadal jest
        //jesli chcesz zeby zostal usuiety to trzeba zmienic tym cascade z persist na all
        assertEquals(randomString,
                modelsManager
                        .findByIdModel(idM)
                        .getProducent());


    }
@Test
    public void addModel() throws Exception {
        Model m1=new Model();
        m1.setNazwaMod("Wspanialy");
        m1.setProducent(randomString);
        m1.setProgram("Wyborny");

        Long idM= modelsManager.addModel(m1);
        //test znajdowania modelu po id
        assertEquals(randomString,
                modelsManager
                        .findByIdModel(idM)
                            .getProducent());
    }

    @Test
    public void getAllModels() throws Exception {
        Model m1=new Model();
        m1.setNazwaMod("Ladny");
        m1.setProducent(randomString);
        m1.setProgram("Nowy");
        setUp();
        Model m2=new Model();
        m2.setNazwaMod("Ladniejszy");
        m2.setProducent(randomString);
        m2.setProgram("Nowszy");
        modelsManager.addModel(m1);
        modelsManager.addModel(m2);
        List<Model> models=modelsManager.getAllModels();
        assertTrue(models.size()>=2);
    }


    @Test
    public void findByProd() throws Exception {

        Model m2=new Model();
        m2.setNazwaMod("Nazwunia");
        m2.setProducent(randomString);
        m2.setProgram("3ds");
        modelsManager.addModel(m2);
        assertEquals(randomString,modelsManager
                                    .findByProd(m2.getProducent())
                                        .getProducent());
    }

     @Test
    public void deleteModel() throws Exception {
        Model m1=new Model();
        m1.setNazwaMod("biurko");
        m1.setProducent(randomString);
        m1.setProgram("3ds");

        Long idM= modelsManager.addModel(m1);
        setUp();
        Model m2=new Model();
        m2.setNazwaMod("lawa");
        m2.setProducent(randomString);
        m2.setProgram("3ds");
        modelsManager.addModel(m2);
        Long idM2=modelsManager.addModel(m2);
        //usuwanie modelu
        modelsManager.deleteModel(m1);
        boolean ifExists=false;
        try {
            assertFalse(modelsManager.findByIdModel(idM).getProducent() != randomString);
        }catch (NullPointerException e)
        {
            ifExists=true;
        }

        assertTrue(ifExists);
        //sprawdzamy czy nie zosatly usuniete inne rekordy
        assertTrue(modelsManager.findByIdModel(idM2).getProducent()==randomString);
    }

    @Test
    public void getAllCategoryModel() throws Exception {
        Category puzzle=new Category();
        puzzle.setNazwaKat(randomString);
        puzzle.setNumer(1234);
        puzzle.setOpis("salon");
        Long id=modelsManager.addCategory(puzzle);
        Model m1=new Model();
        m1.setNazwaMod("komoa");
        m1.setProducent(randomString);
        m1.setProgram("3ds");
        m1.setCategory(puzzle);
        setUp();
        Model m2=new Model();
        m2.setNazwaMod("szafa");
        m2.setProducent(randomString);
        m2.setProgram("3ds");
        m2.setCategory(puzzle);
        setUp();
        Model m3=new Model();
        m3.setNazwaMod("lozko");
        m3.setProducent(randomString);
        m3.setProgram("3ds");

        modelsManager.addModel(m1);
        modelsManager.addModel(m2);
        Long idM3=modelsManager.addModel(m3);
        List<Model> models=modelsManager.getAllCategoryModel(id);
        assertTrue(models.size()==2);
        //test sprawdzajacy czy przypisuje tylko te odele ktore ma przypisac
        boolean ifExists=false;
        try {

            assertTrue(modelsManager
                    .findByIdModel(idM3)
                    .getCategory()
                    .getId() == id);
        }
        catch (Exception e){
            ifExists=true;

        }

        assertTrue(ifExists);
    }
    @Test
    public void addCategoryToModel() throws Exception {
        Category puzzle=new Category();
        puzzle.setNazwaKat(randomString);
        puzzle.setNumer(1234);
        puzzle.setOpis("kuchnia");
        Long idC=modelsManager.addCategory(puzzle);
        Model m1=new Model();
        m1.setNazwaMod("piekarnik");
        m1.setProducent(randomString);
        m1.setProgram("3ds");
        Long idM=modelsManager.addModel(m1);
        modelsManager.addCategoryToModel(idC,idM);
        List<Model> models=modelsManager.getAllCategoryModel(idC);
        assertTrue(models.size()!=0);
        assertEquals(idM,models.get(0).getId());
    }

}
