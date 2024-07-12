package com.codegym.model;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Component
public class Promotion implements Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String header;
    private Date timeStart;
    private Date timeEnd;
    private double discount;
    private String details;

    public Promotion() {
    }

    public Promotion(String header, Date timeStart, Date timeEnd, double discount, String details) {
        this.header = header;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.discount = discount;
        this.details = details;
    }

    public Promotion(Long id, String header, Date timeStart, Date timeEnd, double discount, String details) {
        this.id = id;
        this.header = header;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.discount = discount;
        this.details = details;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Promotion.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Promotion promotion = (Promotion) target;

        String header = promotion.getHeader();
        ValidationUtils.rejectIfEmpty(errors,"header","header.empty");
        if (header.length() > 100){
            errors.rejectValue("header", "header.length");
        }

        Date timeStart = promotion.getTimeStart();
        ValidationUtils.rejectIfEmpty(errors,"timeStart","timeStart.empty");
        if (timeStart != null) {
            Instant instantStart = timeStart.toInstant();
            LocalDate startDate = instantStart.atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentDate = LocalDate.now();
            if (startDate.isBefore(currentDate)) {
                errors.rejectValue("timeStart", "timeStart.past");
            }
        }

        Date timeEnd = promotion.getTimeEnd();
        ValidationUtils.rejectIfEmpty(errors,"timeEnd","timeEnd.empty");
        if (timeEnd != null || timeStart != null) {
            LocalDate startDate = timeStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Instant instantEnd = timeEnd.toInstant();
            LocalDate endDate = instantEnd.atZone(ZoneId.systemDefault()).toLocalDate();
            if (endDate.isBefore(startDate)) {
                errors.rejectValue("timeEnd", "timeEnd.before");
            }
        }

        double discount = promotion.getDiscount();
        ValidationUtils.rejectIfEmpty(errors,"discount","discount.empty");
        if (discount < 10000 ) {
            errors.rejectValue("discount", "discount.range");
        }

//        String details = promotion.getDetails();
        ValidationUtils.rejectIfEmpty(errors,"details","details.empty");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
