package com.example.demo.controller;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dataaccess.MemoRepository;
import com.example.demo.model.Memo;
import com.example.demo.model.MemoEntity;
import com.example.demo.service.MemoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemoController {

	@Autowired
	private MemoRepository memoRepository;
	@Autowired
	private MemoService memoService;

	// ホーム画面
	@RequestMapping("/")
	public String index(HttpSession session) {
		clearSession(session);

		return "memoHome";
	}

	// 新規作成画面へ遷移
	@GetMapping("/toMemoCreate")
	public String toMemoCreate(@ModelAttribute Memo memo, HttpSession session) {

		MemoEntity memoEntity = new MemoEntity();
		session.setAttribute("memo", memoEntity);

		return "memoCreate";
	}

	// 更新画面へ遷移
	// @GetMapping("/toUpdate")
	// public String toUpdate() {
	// return "memoEdit";
	// }

	// 戻る（ホーム画面）
	@GetMapping("/toHome")
	public String toHome(Model model, HttpSession session) {

		// model.addAttribute("sortMemoList", findAll());

		return "memoHome";
	}

	// 戻る（詳細画面）
	// @GetMapping("/toDetail")
	// public String toDetail() {
	// return "memoDetail";
	// }

	// 表示
	@PostMapping("/select")
	public String selectMemo(@RequestParam("id") int id, HttpSession session, MemoEntity memoEntity) {
		// 検索
		memoEntity = memoRepository.findById(id);

		session.setAttribute("memo", memoEntity);

		return "memoEdit";
	}

	// 登録
	@PostMapping("/createMemo")
	public String createMemo(MemoEntity memoEntity, Memo memo, HttpSession session, Model model) {

		System.out.println("title:" + memo.getTitle());
		setMemo(session, memo);

		String chkTitle = memoService.chkTitle(memo.getTitle());
		if (!chkTitle.isEmpty()) {
			model.addAttribute("message", chkTitle);
			return "memoCreate";
		}

		String chkContent = memoService.chkContent(memo.getContent());
		if (!chkContent.isEmpty()) {
			model.addAttribute("message", chkContent);
			return "memoCreate";
		}

		memoEntity.setTitle(memo.getTitle());
		memoEntity.setContent(memo.getContent());
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		memoEntity.setCreate_time(currentTime);

		// 登録
		memoRepository.save(memoEntity);

		clearSession(session);
		model.addAttribute("message", "登録しました。");

		return "memoHome";
	}

	// 更新
	@PostMapping("/update")
	public String updateMemo(@RequestParam("id") int id, HttpSession session, MemoEntity memoEntity, Memo memo,
			Model model) {

		setMemo(session, memo);

		String chkTitle = memoService.chkTitle(memo.getTitle());
		if (!chkTitle.isEmpty()) {
			model.addAttribute("message", chkTitle);
			return "memoEdit";
		}

		String chkContent = memoService.chkContent(memo.getContent());
		if (!chkContent.isEmpty()) {
			model.addAttribute("message", chkContent);
			return "memoEdit";
		}

		memoEntity = memoRepository.findById(id);
		memoEntity.setTitle(memo.getTitle());
		memoEntity.setContent(memo.getContent());
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		memoEntity.setCreate_time(currentTime);

		// 更新
		memoRepository.save(memoEntity);

		// 検索
		// memoEntity = memoRepository.findById(id);

		// session.setAttribute("memo", memoEntity);

		clearSession(session);
		model.addAttribute("message", "保存しました。");

		return "memoHome";
	}

	// 削除
	@PostMapping("/delete")
	public String deleteMemo(@RequestParam("id") int id, Model model, HttpSession session) {
		// 削除
		memoRepository.deleteById(id);

		List<MemoEntity> memoList = findAll();
		String sortKey = (String) session.getAttribute("sortKey");
		String sortDirection = (String) session.getAttribute("sortDirection");
		memoList = memoService.sort(memoList, sortKey, sortDirection);

		session.setAttribute("sortMemoList", memoList);
		model.addAttribute("message", "削除しました。");

		return "memoHome";
	}

	// ソート
	@GetMapping("/sort")
	public String getMemoList(@RequestParam("sortKey") String sortKey,
			@RequestParam("sortDirection") String sortDirection, Model model, HttpSession session) {
		List<MemoEntity> memoList = findAll();

		memoList = memoService.sort(memoList, sortKey, sortDirection);

		session.setAttribute("sortKey", sortKey);
		session.setAttribute("sortDirection", sortDirection);
		session.setAttribute("sortMemoList", memoList);

		return "memoHome";
	}

	// 全件検索
	public List<MemoEntity> findAll() {
		List<MemoEntity> memoEntity = memoRepository.findAll();
		return memoEntity;
	}

	public MemoEntity setMemo(HttpSession session, Memo memo) {
		MemoEntity memoEntity = (MemoEntity) session.getAttribute("memo");
		memoEntity.setTitle(memo.getTitle());
		memoEntity.setContent(memo.getContent());

		return memoEntity;
	}

	public void clearSession(HttpSession session) {
		session.setAttribute("sortKey", "id");
		session.setAttribute("sortDirection", "asc");
		List<MemoEntity> memoList = findAll();
		memoList = memoService.sort(memoList, "id", "asc");
		session.setAttribute("sortMemoList", memoList);
	}

	// 全件検索(jdbcTemplate)
	// public List<MemoEntity> getAllMemo() {
	// List<MemoEntity> memolist = memoService.getAllMemo();
	// return memolist;
	// }
}