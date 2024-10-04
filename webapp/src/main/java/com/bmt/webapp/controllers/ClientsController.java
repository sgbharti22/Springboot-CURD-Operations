package com.bmt.webapp.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bmt.webapp.models.ClientDto;
import com.bmt.webapp.models.Clients;
import com.bmt.webapp.repositories.ClientRepository;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;






@Controller
@RequestMapping("/clients")
public class ClientsController {
    @Autowired
    private ClientRepository clientRepo;

    @GetMapping({"","/"})
    public String getClients(Model model) {
        var clients=clientRepo.findAll(Sort.by(Sort.Direction.DESC,"id"));
        model.addAttribute("clients",clients);
        return "clients/index";
    }
    
    @GetMapping("/create")
    public String createClient(Model model) {
        ClientDto clientdto= new ClientDto();
        model.addAttribute("clientdto",clientdto);
        return "clients/create";
    }

    @PostMapping("/create")
    public String createClient(@Valid @ModelAttribute("clientdto") ClientDto clientdto,BindingResult result) {
        System.out.println("Email: " + clientdto.getEmail());

        if(clientRepo.findByEmail(clientdto.getEmail())!=null){
            result.addError(
                new FieldError("clientdto","email",clientdto.getEmail(),false,null,null,"Email address already used!!")
                );
        }

        if(result.hasErrors()){
            return "clients/create";
        }

        Clients c=new Clients();
        c.setFirstName(clientdto.getFirstName()); 
        c.setLasttName(clientdto.getLastName());
        c.setEmail(clientdto.getEmail());
        c.setPhone(clientdto.getPhone());
        c.setAddress(clientdto.getAddress());
        c.setStatus(clientdto.getStatus());
        c.setCreatedAt(new Date());

        clientRepo.save(c);
        return "redirect:/clients";
    }

    @GetMapping("/edit")
    public String editClient(Model model, @RequestParam("id") int id) {
        Clients clients =clientRepo.findById(id).orElse(null);
        if(clients==null){
            return "redirect:/clients";
        }

        ClientDto clientdto =new ClientDto();
        clientdto.setFirstName(clients.getFirstName());
        clientdto.setLastName(clients.getLasttName());
        clientdto.setEmail(clients.getEmail());
        clientdto.setPhone(clients.getPhone());
        clientdto.setAddress(clients.getAddress());
        clientdto.setStatus(clients.getStatus());

        model.addAttribute("clients",clients);
        model.addAttribute("clientdto",clientdto);

        return "clients/edit";
    }

    @PostMapping("/edit")
    public String editClient(Model model,@RequestParam int id,@Valid @ModelAttribute ClientDto clientdto,BindingResult result) {
        //TODO: process POST request
        Clients clients =clientRepo.findById(id).orElse(null);
        if(clients==null){
            return "redirect:/clients";
        }
        model.addAttribute("clients",clients);
        if(result.hasErrors()){
            return "clients/edit";
        }

        clients.setFirstName(clientdto.getFirstName());
        clients.setLasttName(clientdto.getLastName());
        clients.setEmail(clientdto.getEmail());
        clients.setPhone(clientdto.getPhone());
        clients.setAddress(clientdto.getAddress());
        clients.setStatus(clientdto.getStatus());

        try{
            clientRepo.save(clients);
        }
        catch(Exception e){
            result.addError(
                new FieldError("clientdto","email",clientdto.getEmail(),false,null,null,"Email address can't be updated !!")
                );
            return "clients/edit";
        }

        return "redirect:/clients";
    }
    

    @GetMapping("/delete")
    public String deleteClient(@RequestParam("id") int id) {
        Clients clients =clientRepo.findById(id).orElse(null);
        if(clients!=null){
            clientRepo.delete(clients);
        }
        return "redirect:/clients";
    }
    
    
    
}
