package com.ggroup.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggroup.models.Contact;
import com.ggroup.services.ContactService;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    // إنشاء رسالة جديدة
    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        // بعد إزالة newsletterSubscription، لا حاجة لهذا المنطق
        Contact savedContact = contactService.createContact(contact);
        return ResponseEntity.ok(savedContact);
    }

    // استرجاع جميع الرسائل
    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactService.getAllContacts();
        return ResponseEntity.ok(contacts);
    }

    // استرجاع رسالة بناءً على ID
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        Contact contact = contactService.getContactById(id);
        return ResponseEntity.ok(contact);
    }

    // حذف رسالة بناءً على ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}
