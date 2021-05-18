package com.example.csvfileprocessing;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class CsvUtils {

    @Autowired
    private Validator validator;

    //Adding validator bean to have custom validations
    @Bean
    public Validator validatorBean() {
        return new LocalValidatorFactoryBean();
    }

    private static final CsvMapper mapper = new CsvMapper();
    public <T> List<T> read(Class<T> clazz, InputStream stream) throws IOException {
        CsvSchema schema = mapper.schemaFor(clazz).withHeader().withColumnReordering(true);
        ObjectReader reader = mapper.readerFor(clazz).with(schema);
        List<T> list = reader.<T>readValues(stream).readAll();
        System.out.println(list);
        return list;
    }

    public Collection<User> processCSV(MultipartFile file) throws Exception {

        String [] HEADERS = {"mobileNumber", "message", "channel", "firstName", "points", "reason"};

        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withIgnoreEmptyLines(true)
                .withHeader(HEADERS)
                .withFirstRecordAsHeader()
                .withAllowDuplicateHeaderNames(false)
                .parse(new InputStreamReader(file.getInputStream()));

        return convertCsvToDto(records);
    }

    private Collection<User> convertCsvToDto(Iterable<CSVRecord> records) throws Exception {
        Collection<User> users = new ArrayList<>();
        for (CSVRecord record : records) {
            int count = 0;
            for(int i=0; i<record.size(); i++) {
                if(record.get(i).isBlank()) {
                    count++;
                }
            }
            if(count == record.size()) {
                continue;
            }
            User user = getUser(record);
            users.add(user);
        }
        return users;
    }

    private User getUser(CSVRecord record) throws Exception {
        User user = new User();

        System.out.println("Processing record: "+record.getRecordNumber());
        user.setMobileNumber(record.get("mobileNumber"));
        validateChannelType(record);
        user.setChannel(User.Channel.valueOf((record.get("channel"))));
        user.setMessage(record.get("message"));
        user.setFirstName(record.get("firstName"));
        user.setPoints(record.get("points"));
        user.setReason(record.get("reason"));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        List<String> errors = new ArrayList<>();
        violations.forEach(v -> errors.add(v.getMessageTemplate()));
        if(!violations.isEmpty()) {
//            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
            throw new Exception("Validation Exception: "+ errors + " on line "+ record.getRecordNumber());
        }
        return user;
    }

    private void validateChannelType(CSVRecord record) throws Exception {
        if(!(record.get("channel").toString().equals("SMS")
                || record.get("channel").toString().equals("PUSH_NOTIFICATION"))) {
            throw new Exception("Invalid channel format");
        }
    }
}