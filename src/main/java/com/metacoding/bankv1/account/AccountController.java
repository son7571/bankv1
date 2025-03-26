package com.metacoding.bankv1.account;


import com.metacoding.bankv1.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AccountController {
    private final AccountService accountService;
    private final HttpSession session;

    @GetMapping("/account")
    public String list(HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인후에 사용해주세요");

        // select * from account_tb where user_id = 1
        List<Account> accountList = accountService.계좌목록(sessionUser.getId());
        request.setAttribute("models", accountList);

        return "account/list";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/account/save-form")
    public String saveForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인후에 사용해주세요");
        return "account/save-form";
    }


    @PostMapping("/account/save")
    public String save(AccountRequest.SaveDTO saveDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인후에 사용해주세요");

        accountService.계좌생성(saveDTO, sessionUser.getId());
        return "redirect:/account";
    }

    @PostMapping("account/transfer")
    public String transfer(AccountRequest.TransferDTO transferDTO) {
        System.out.println(transferDTO);
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인후에 사용해주세요");
        accountService.계좌이체(transferDTO, sessionUser.getId());

        return "redirect:/"; // Todo
    }

    @GetMapping("account/transfer-form")
    public String transferForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인후에 사용해주세요"); //공통 부가로직
        return "account/transfer-form";
    }

    // /account/1111?type=입금
    @GetMapping("account/{number}")
    public String detail(@PathVariable("number") int number, @RequestParam(value = "type", required = false, defaultValue = "전체") String type) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인후에 사용해주세요"); //공통 부가로직
        accountService.계좌상세보기(number, type, sessionUser.getId());
//        System.out.println("number = " + number);
//        System.out.println("type = " + type);
        return "account/detail";
    }

}
