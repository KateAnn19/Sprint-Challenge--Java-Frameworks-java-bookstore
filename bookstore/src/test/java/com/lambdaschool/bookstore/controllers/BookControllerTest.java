package com.lambdaschool.bookstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.models.Author;
import com.lambdaschool.bookstore.models.Book;
import com.lambdaschool.bookstore.models.Section;
import com.lambdaschool.bookstore.models.Wrote;
import com.lambdaschool.bookstore.services.BookService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)

/*****
 * Due to security being in place, we have to switch out WebMvcTest for SpringBootTest
 * @WebMvcTest(value = BookController.class)
 */
@SpringBootTest(classes = BookstoreApplication.class)

/****
 * This is the user and roles we will use to test!
 */
@WithMockUser(username = "admin", roles = {"ADMIN", "DATA"})
public class BookControllerTest
{
    /******
     * WebApplicationContext is needed due to security being in place.
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    List<Book> bookList = new ArrayList<>();

    @Before
    public void setUp() throws
            Exception
    {
        /*****
         * The following is needed due to security being in place!
         */
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        /*****
         * Note that since we are only testing bookstore data, you only need to mock up bookstore data.
         * You do NOT need to mock up user data. You can. It is not wrong, just extra work.
         */

        /************
         * Seed Books
         ************/

        Author a1 = new Author("John", "Mitchell");
        Author a2 = new Author("Dan", "Brown");
        Author a3 = new Author("Jerry", "Poe");
        Author a4 = new Author("Wells", "Teague");
        Author a5 = new Author("George", "Gallinger");
        Author a6 = new Author("Ian", "Stewart");

        a1.setAuthorid(1);
        a2.setAuthorid(2);
        a3.setAuthorid(3);


        Section s1 = new Section("Fiction");
        Section s2 = new Section("Technology");
        Section s3 = new Section("Travel");


        s1.setSectionid(1);
        s2.setSectionid(2);
        s3.setSectionid(3);

        Book b1 = new Book("Flatterland", "9780738206752", 2001, s1);
        b1.getWrotes()
            .add(new Wrote(a6, new Book()));
        bookList.add(b1);

        Book b2 = new Book("Digital Fortess", "9788489367012", 2007, s1);
        b2.getWrotes()
            .add(new Wrote(a2, new Book()));
        bookList.add(b2);

        Book b3 = new Book("The Da Vinci Code", "9780307474278", 2009, s1);
        b3.getWrotes()
            .add(new Wrote(a2, new Book()));
        bookList.add(b3);


    }

    @After
    public void tearDown() throws
            Exception
    {
    }

    @Test
    public void a_listAllBooks() throws
            Exception
    {
        String apiUrl = "/books/books";
        Mockito.when(bookService.findAll()).thenReturn(bookList);
        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb).andReturn(); //this can throw an exception
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(bookList);

        System.out.println("Expect: " + er);
        System.out.println("Actual: " + tr);
        assertEquals("Rest API Returns List", er, tr);
    }

    @Test
    public void b_getBookById() throws
            Exception
    {
        String apiUrl = "/books/book/3";

        Mockito.when(bookService.findBookById(3))
            .thenReturn(bookList.get(1));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn(); // this could throw an exception
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(bookList.get(1));

        System.out.println("Expect: " + er);
        System.out.println("Actual: " + tr);

        assertEquals("Rest API Returns List", er, tr);
    }

    @Test
    public void c_getNoBookById() throws
            Exception
    {
        String apiUrl = "/books/book/77";

        Mockito.when(bookService.findBookById(77)).thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn(); // this could throw an exception
        String tr = r.getResponse().getContentAsString();

        String er = "";

        System.out.println("Expect: " + er);
        System.out.println("Actual: " + tr);

        assertEquals("Rest API Returns List", er, tr);
    }

    @Test
    public void d_addNewBook() throws
            Exception
    {
        String apiUrl = "/books/book";

        // build a book
       Section newSection = new Section("test");
       newSection.setSectionid(10);


        Book b42 = new Book("Test of the century",
            "22222222222",
            2025, newSection);
        b42.setBookid(100);
        ObjectMapper mapper = new ObjectMapper();
        String restaurantString = mapper.writeValueAsString(b42);

        Mockito.when(bookService.save(any(Book.class))).thenReturn(b42);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(restaurantString);
        mockMvc.perform(rb)
            .andExpect(status().isCreated())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void e_updateFullBook()
    {
    }

    @Test
    public void f_deleteBookById() throws
            Exception
    {
        String apiUrl = "/books/book/{id}";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, "12").contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }
}