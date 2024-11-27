package global.goit.edu.todolist.controller;

import global.goit.edu.todolist.model.note.Note;
import global.goit.edu.todolist.model.service.NoteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class NoteControllerTests {

    @Autowired
    MockMvc mvc;

    @MockBean
    NoteService noteService;

    @Test
    public void testThatMethodGetListWorkOk() throws Exception {
        Mockito.when(noteService.getAll()).thenReturn(getNotes());

        mvc.perform(get("/note/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("note/list"))
                .andExpect(model().attribute("notes", hasSize(5)))
                .andExpect(model().attribute("notes", hasItem(
                        allOf(
                                hasProperty("id", is(
                                        getNotes().get(0).getId())
                                ),
                                hasProperty("title", is(
                                        getNotes().get(0).getTitle())
                                ),
                                hasProperty("content", is(
                                        getNotes().get(0).getContent())
                                )
                        )
                )))
                .andExpect(model().attribute("notes", hasItem(
                        allOf(
                                hasProperty("id", is(
                                        getNotes().get(1).getId())
                                ),
                                hasProperty("title", is(
                                        getNotes().get(1).getTitle())
                                ),
                                hasProperty("content", is(
                                        getNotes().get(1).getContent())
                                )
                        )
                )))
                .andExpect(model().attribute("notes", hasItem(
                        allOf(
                                hasProperty("id", is(
                                        getNotes().get(2).getId())
                                ),
                                hasProperty("title", is(
                                        getNotes().get(2).getTitle())
                                ),
                                hasProperty("content", is(
                                        getNotes().get(2).getContent())
                                )
                        )
                )))
                .andExpect(model().attribute("notes", hasItem(
                        allOf(
                                hasProperty("id", is(
                                        getNotes().get(3).getId())
                                ),
                                hasProperty("title", is(
                                        getNotes().get(3).getTitle())
                                ),
                                hasProperty("content", is(
                                        getNotes().get(3).getContent())
                                )
                        )
                )))
                .andExpect(model().attribute("notes", hasItem(
                        allOf(
                                hasProperty("id", is(
                                        getNotes().get(4).getId())
                                ),
                                hasProperty("title", is(
                                        getNotes().get(4).getTitle())
                                ),
                                hasProperty("content", is(
                                        getNotes().get(4).getContent())
                                )
                        )
                )));

    }

    @Test
    public void testThatMethodDeleteWorkOk() throws Exception {
        Mockito.when(noteService.getById(1L)).thenReturn(getNotes().get(0));

        mvc.perform(post("/note/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/note/list"));

    }

    @Test
    public void testThatMethodEditNotePageWorkOk() throws Exception {
        Mockito.when(noteService.getById(getNotes().get(0).getId())).thenReturn(getNotes().get(0));

        mvc.perform(get("/note/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("note/edit"))
                .andExpect(model().attribute("note",
                                allOf(
                                        hasProperty("id", is(
                                                getNotes().get(0).getId())
                                        ),
                                        hasProperty("title", is(
                                                getNotes().get(0).getTitle())
                                        ),
                                        hasProperty("content", is(
                                                getNotes().get(0).getContent())
                                        )
                                )
                        )
                );
    }

    @Test
    public void testThatMethodEditNoteWorkOk() throws Exception {
        mvc.perform(post("/note/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/note/list")
                );
    }

    @Test
    public void testThatMethodAddWorkOk() throws Exception {

        Note note = Note.builder()
                .id(5L)
                .title("Water")
                .content("Water is the more useful liquid than bear")
                .build();

        Mockito.when(noteService.save(note)).thenReturn(note);

        RequestBuilder request = post("/note/add")
                .param("id", String.valueOf(5L))
                .param("title", "Water")
                .param("content", "Water is the more useful liquid than bear");

        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/note/list")
                );
    }

    private List<Note> getNotes() {
        Note one = Note.builder()
                .id(1L)
                .title("Do Androids Dream of Electric Sheep?")
                .content("It was January 2021, and Rick Deckard had a license to kill. Somewhere among the hordes of " +
                        "humans out there, lurked several rogue androids. Deckards assignment--find them and then..." +
                        "retire them. Trouble was, the androids all looked exactly like humans, and they didnt want " +
                        "to be found!")
                .build();

        Note two = Note.builder()
                .id(2L)
                .title("The Hitchhiker’s Guide to the Galaxy")
                .content("Seconds before the Earth is demolished to make way for a galactic freeway, " +
                        "Arthur Dent is plucked off the planet by his friend Ford Prefect, a researcher for " +
                        "the revised edition of The Hitchhikers Guide to the Galaxy who, for the last fifteen years, " +
                        "has been posing as an out-of-work actor.")
                .build();

        Note three = Note.builder()
                .id(3L)
                .title("Something Wicked This Way Comes")
                .content("One of Ray Bradbury’s best-known and most popular novels, Something Wicked This Way Comes, " +
                        "now featuring a new introduction and material about its longstanding influence on culture " +
                        "and genre.")
                .build();

        Note four = Note.builder()
                .id(4L)
                .title("A Story of Yesterday")
                .content("A Story of Yesterday is a concise and straight punch to the jaw of life.Under a sky of" +
                        " different colors germinates a magical story of survival, where the result of each choice, " +
                        "enclosed in this particular tale, will snatch the whereabouts of each story forever.")
                .build();

        Note five = Note.builder()
                .id(4L)
                .title("To Kill a Mockingbird")
                .content("The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience " +
                        "that rocked it. To Kill A Mockingbird became both an instant bestseller and a critical success " +
                        "when it was first published in 1960. It went on to win the Pulitzer Prize in 1961 and was " +
                        "later made into an Academy Award-winning film, also a classic.")
                .build();

        return Arrays.asList(one, two, three, four, five);

    }

}
