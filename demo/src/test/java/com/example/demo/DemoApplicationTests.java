package com.example.demo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.controller.MemoController;
import com.example.demo.dataaccess.MemoRepository;
import com.example.demo.model.MemoEntity;
import com.example.demo.service.MemoService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DemoApplicationTests {

	@InjectMocks
	private MemoController memoController;

	@Mock
	private MemoRepository memoRepository;

	@MockBean
	private MemoService memoService;

	private AutoCloseable closeable;

	private MockMvc mvc;

	@BeforeEach
	public void openMocks() {
		closeable = MockitoAnnotations.openMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(memoController).build();
	}

	@AfterEach
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testIndex() {
		try {
			mvc.perform(MockMvcRequestBuilders.get("/"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("memoHome"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testToMemoCreate() {
		try {
			mvc.perform(MockMvcRequestBuilders.get("/toMemoCreate"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("memoCreate"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testToUpdate() {
		try {
			mvc.perform(MockMvcRequestBuilders.get("/toUpdate"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("memoEdit"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testToHome() {
		try {
			mvc.perform(MockMvcRequestBuilders.get("/toHome"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("memoHome"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testToDetail() {
		try {
			mvc.perform(MockMvcRequestBuilders.get("/toDetail"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("memoDetail"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSelectMemo() {
		// Mock
		Mockito.when(memoRepository.findById(Mockito.anyInt())).thenReturn(memoEntity());

		try {
			mvc.perform(MockMvcRequestBuilders.post("/select")
					.param("id", "1"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.request().sessionAttribute("memo", memoEntity()))
					.andExpect(MockMvcResultMatchers.view().name("memoDetail"));

			Mockito.verify(memoRepository, Mockito.times(1)).findById(Mockito.anyInt());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateMemo() {
		// Mock 条件あり
		Mockito.when(memoRepository.save(Mockito.any())).thenReturn(memoEntity());

		String formData = "title=title&content=content";

		try {
			mvc.perform(MockMvcRequestBuilders.post("/createMemo")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(formData))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("memoHome"));

			Mockito.verify(memoRepository).save(Mockito.any());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateMemo_Error() {
		String formData = "title=&content=content";

		try {
			mvc.perform(MockMvcRequestBuilders.post("/createMemo")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(formData))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("memoCreate"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateMemo() {
		// Mock 条件あり
		Mockito.when(memoRepository.save(Mockito.any())).thenReturn(memoEntity());

		// Mock
		Mockito.when(memoRepository.findById(Mockito.anyInt())).thenReturn(memoEntity());

		String formData = "title=title&content=content";

		try {
			mvc.perform(MockMvcRequestBuilders.post("/update")
					.param("id", "1")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(formData))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("memoDetail"));

			Mockito.verify(memoRepository, Mockito.times(2)).findById(Mockito.anyInt());
			Mockito.verify(memoRepository).save(Mockito.any());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateMemo_Error() {
		// Mock
		Mockito.when(memoRepository.findById(Mockito.anyInt())).thenReturn(memoEntity());

		String formData = "title=&content=content";

		try {
			mvc.perform(MockMvcRequestBuilders.post("/update")
					.param("id", "1")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(formData))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("memoEdit"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDeleteMemo() {
		// Mock 何もしない
		Mockito.doNothing().when(memoRepository).deleteById(Mockito.anyInt());

		try {
			mvc.perform(MockMvcRequestBuilders.post("/delete")
					.param("id", "1"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("memoHome"));

			Mockito.verify(memoRepository, Mockito.times(1)).deleteById(Mockito.anyInt());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetMemoList() {
		// Mock
		Mockito.when(memoRepository.findAll()).thenReturn(memoList());
		try {
			mvc.perform(MockMvcRequestBuilders.get("/sort")
					.param("sortKey", "id")
					.param("sortDirection", "asc"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("memoHome"));

			Mockito.verify(memoRepository, Mockito.times(1)).findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MemoEntity memoEntity() {
		MemoEntity memo = new MemoEntity();
		memo.setId(1);
		memo.setTitle("title");
		memo.setContent("content");
		Timestamp ts = Timestamp.valueOf("2024-04-10 09:00:00.01");
		memo.setCreate_time(ts);
		return memo;
	}

	public List<MemoEntity> memoList() {
		List<MemoEntity> memoList = new ArrayList<>();
		memoList.add(memoEntity());
		return memoList;
	}

}