package com.lambdaschool.bookstore.services;

import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.exceptions.ResourceNotFoundException;
import com.lambdaschool.bookstore.models.Book;
import com.lambdaschool.bookstore.models.Section;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApplication.class)
//**********
// Note security is handled at the controller, hence we do not need to worry about security here!
//**********
public class BookServiceImplTest
{

    @Autowired
    private BookService bookService;
    @Autowired
    private SectionService sectionService;

    @Before
    public void setUp() throws
            Exception
    {
        MockitoAnnotations.initMocks(this);
        List<Book> myList = bookService.findAll();
        List<Section> mySection = sectionService.findAll();
        //print out test data
        for(Book b : myList)
        {
            System.out.println(b.getTitle() + " " + b.getBookid());
        }
        for(Section s : mySection)
        {
            System.out.println(s.getSectionid() + " " + s.getName());
        }
    }

    @After
    public void tearDown() throws
            Exception
    {
    }

    @Test
    public void a_findAll()
    {
        assertEquals(5, bookService.findAll().size());
    }

    @Test
    public void b_findBookById()
    {
        assertEquals("Flatterland", bookService.findBookById(26).getTitle());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void c_notFindBookById()
    {
        assertEquals("the awesome world of java", bookService.findBookById(8888).getTitle());
    }

    @Test
    public void d_delete()
    {
        bookService.delete(30);
        assertEquals(4, bookService.findAll().size()); //running findAll again here
    }

    @Test
    public void e_save()
    {
        List<Section> sectionList = sectionService.findAll();

//        System.out.println("Hereeeeeee");
//       Section s100 = new Section("unknown");
//        System.out.println("!!!!!!!!!!!!!");
//       sectionService.save(s100);
//        System.out.println("**********************");
//        System.out.println(s100.getSectionid());

        Book newBook = new Book("graham's great adventures",
            "111111111",
            2025, sectionList.get(0));

        Book addBook = bookService.save(newBook);
        System.out.println("PRINT" + addBook);
        Book foundBook = bookService.findBookById(addBook.getBookid());
        assertNotNull(addBook);
        assertEquals(addBook.getTitle(), foundBook.getTitle());
    }

    @Test
    public void f_update()
    {
    }


}