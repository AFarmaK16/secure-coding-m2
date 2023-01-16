package com.eknox.moneytransfer.web;

import com.eknox.moneytransfer.dao.TransactionRepository;
import com.eknox.moneytransfer.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

public class TransactionController {
    //CRUD
    // respecter le standard REST
    @Autowired
    private TransactionRepository transactionRepository;

    @RequestMapping(value = "/index")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "8") int size,
                        @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<Transaction> pageTransactions = transactionRepository.searchTransaction("%" + keyword + "%", PageRequest.of(page, size));
        model.addAttribute("listTransactions", pageTransactions.getContent());
        int[] pages = new int[pageTransactions.getTotalPages()];
        model.addAttribute("pages", pages);
        model.addAttribute("size", size);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);

        return "Transaction";
    }



    @GetMapping(value = "/delete")
    public String deleteById(Long idTransaction, int page, int size, String keyword) {
        transactionRepository.deleteById(idTransaction);
        return "redirect:/index?page=" + page + "&size=" + size + "&keyword=" + keyword;
    }

    @GetMapping(value = "/formTransaction")
    public String addNewTransaction(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "formTransaction";
    }

    @PostMapping(value = "/addTransaction")
    public String save(Model model, @Validated Transaction transaction, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "formTransaction";
        transactionRepository.save(transaction);
        return "saveConfirmation";

    }

    @GetMapping(value = "/editTransaction")
    public String editTransaction( Long idTransaction ,Model model) {
        Optional<Transaction> transaction = transactionRepository.findById(idTransaction);
        model.addAttribute("transaction", transaction);
        return "editTransaction";
    }

    @GetMapping(value="/")
    public String homePage() {
        return "redirect:/index";
    }
}
