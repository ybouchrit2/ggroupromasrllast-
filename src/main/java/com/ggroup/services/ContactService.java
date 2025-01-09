package com.ggroup.services;

import com.ggroup.models.Contact;
import com.ggroup.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ContactService {

    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);  // تعريف الكائن logger

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private EmailService emailService;  // إضافة خدمة البريد الإلكتروني

    // إنشاء رسالة جديدة
    public Contact createContact(Contact contact) {
        // حفظ الرسالة في قاعدة البيانات
        Contact savedContact = contactRepository.save(contact);
        logger.info("Contact saved with ID: {}", savedContact.getId());
    
        // إرسال البريد الإلكتروني للعميل
        try {
            emailService.sendEmail(contact);  // استدعاء خدمة إرسال البريد
            logger.info("Email sent to: {}", contact.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send email to: {}. Error: {}", contact.getEmail(), e.getMessage());
        }
    
        return savedContact;
    }
    
    

    // استرجاع جميع الرسائل
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    // استرجاع رسالة بناءً على ID
    public Contact getContactById(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with ID: " + id));
    }

    // حذف رسالة بناءً على ID
    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
}
